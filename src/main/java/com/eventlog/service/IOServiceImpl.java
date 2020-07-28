package com.eventlog.service;

import com.eventlog.dto.EventLogDto;
import com.eventlog.dto.EventLogObjectDto;
import com.eventlog.dto.EventLogObjectFactory;
import com.eventlog.enums.Operation;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class IOServiceImpl implements IOService {

  private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceServiceImpl.class);

  @Override
  public List<EventLogDto> readFile(String filePath) {
    LOGGER.info("Reading records from csv file from the given path {}", filePath);
    ArrayList<EventLogDto> dtos = new ArrayList<>();
    try {

      CSVReader csvReader = getCSVReader(filePath);

      String[] record;
      while ((record = csvReader.readNext()) != null) {
        EventLogDto<EventLogObjectDto> eventLogDto = new EventLogDto<>();
        eventLogDto.setOperationType(Operation.getByValue(record[0]));
        ZonedDateTime parsed = ZonedDateTime.parse(record[1]);

        eventLogDto.setTimestamp(Timestamp.valueOf(parsed.toLocalDateTime()));
        String objectType = record[2];
        eventLogDto.setObjectType(objectType);
        eventLogDto.setObjectId(record[3]);

        String payload = record[4];
        if (!StringUtils.isEmpty(payload)) {
          eventLogDto.setPayload(EventLogObjectFactory.getInstance(objectType, payload));
        }
        dtos.add(eventLogDto);
      }

    } catch (IOException | CsvValidationException ioe) {
      LOGGER.error("Error in reading the CSV file", ioe);
    }
    return dtos;
  }

  public CSVReader getCSVReader(String filePath) throws FileNotFoundException {
    // java.nio.file.Path file1 = Paths.get(filePath);
    // Paths.get(ResourceUtils.getURL("classpath:Events.csv").toURI());
    return new CSVReaderBuilder(new FileReader(filePath)).withSkipLines(1).build();
  }

  @Override
  public void generateFiles(List<EventLogDto> eventLogDtos) {
    List<String[]> recordsList =
        eventLogDtos
            .stream()
            .map(eventLogDto -> eventLogDto.toStringArray())
            .collect(Collectors.toList());
    generateOutputCSV(recordsList);
  }

  private void generateOutputCSV(List<String[]> arrayOfRecords) {
    LOGGER.info("Generating new output csv file");
    FileWriter fileWriter = null;
    CSVWriter csvWriter = null;
    try {
      fileWriter = new FileWriter("Events_Output.csv");
      csvWriter = new CSVWriter(fileWriter);
      CSVWriter finalCsvWriter = csvWriter;
      arrayOfRecords.forEach(
          record -> {
            finalCsvWriter.writeNext(record);
          });

    } catch (IOException e) {
      LOGGER.error("Error in generating the CSV file", e);
    } finally {
      try {
        if (csvWriter != null) csvWriter.close();
        if (fileWriter != null) fileWriter.close();

      } catch (IOException e) {
        LOGGER.error("Error in generating the CSV file", e);
      }
      ;
    }
  }
}
