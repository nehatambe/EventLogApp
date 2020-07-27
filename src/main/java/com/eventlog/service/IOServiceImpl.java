package com.eventlog.service;

import com.eventlog.dto.EventLogDto;
import com.eventlog.dto.EventLogObjectFactory;
import com.eventlog.dto.EventLogObjectDto;
import com.eventlog.enums.LogObjectType;
import com.eventlog.enums.Operation;
import com.eventlog.model.Invoice;
import com.eventlog.repository.InvoiceRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IOServiceImpl implements IOService{

    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    @Autowired
    InvoiceRepository invoiceRepository;

    @Override
    public List<EventLogDto> readFile(String filePath) {
        ArrayList<EventLogDto> dtos = new ArrayList<>();
        try
        {
            //java.nio.file.Path file1 = Paths.get(filePath);
            // Paths.get(ResourceUtils.getURL("classpath:Events.csv").toURI());


            CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath)).withSkipLines(1).build();

            /*ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);*/


				/*Stream<String> lines = Files.lines( file1, StandardCharsets.UTF_8 );

				int count = 1;
				for( String line : (Iterable<String>) lines::iterator )
				{
					if(count==1){
						count++;
						continue;
					}
					System.out.println(line);
					String []arr = line.split(",");
					EventLogDto<InvoiceDto> eventLogDto = new EventLogDto<>();
					eventLogDto.setOperationType(Operation.getByValue(arr[0]));
					ZonedDateTime parsed = ZonedDateTime.parse(arr[1]);

					eventLogDto.setTimestamp(Timestamp.valueOf(parsed.toLocalDateTime()));
					eventLogDto.setObjectType(arr[2]);
					eventLogDto.setObjectId(arr[3]);
					String payload =arr[4].trim().replaceAll("\n","");
					System.out.println(payload);

					//eventLogDto.setPayload(objectMapper.readValue(payload,InvoiceDto.class));
					dtos.add(eventLogDto);
				}*/
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                EventLogDto<EventLogObjectDto> eventLogDto = new EventLogDto<>();
                eventLogDto.setOperationType(Operation.getByValue(record[0]));
                ZonedDateTime parsed = ZonedDateTime.parse(record[1]);

                eventLogDto.setTimestamp(Timestamp.valueOf(parsed.toLocalDateTime()));
                String objectType = record[2];
                eventLogDto.setObjectType(LogObjectType.getByValue(objectType));
                eventLogDto.setObjectId(record[3]);

                String payload =record[4];
                if(!StringUtils.isEmpty(payload)) {
                    eventLogDto.setPayload(EventLogObjectFactory.getInstance(objectType,payload));
                }
                dtos.add(eventLogDto);
            }

        } catch (IOException | CsvValidationException ioe){
            ioe.printStackTrace();
        }
        return dtos;
    }

    @Override
    public void generateFiles(List<EventLogDto> eventLogDtos) {
        Set<String> invoiceIds = new HashSet<>();
        List<String[]> recordsList = eventLogDtos.stream().map(eventLogDto -> {
            invoiceIds.add(eventLogDto.getObjectId());
            return eventLogDto.toStringArray();
        }).collect(Collectors.toList());
        generateOutputCSV(recordsList);
        generateJsonFiles(invoiceIds);
    }

    private void generateOutputCSV(List<String[]> arrayOfRecords) {

        try{
            FileWriter fileWriter = new FileWriter("Events_Output.csv");
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            arrayOfRecords.forEach(record -> {
                csvWriter.writeNext(record);
            });
            csvWriter.close();
        }catch (IOException e){
            LOGGER.debug("Error in generating the CSV file",e);
        }
    }

    private void generateJsonFiles(Set<String> invoiceIds){
        List<Invoice> invoiceList = invoiceRepository.findAllById(invoiceIds);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        invoiceList.forEach(invoice -> {
            String fileName = invoice.getId()+".json";
            try {
                objectMapper.writeValue(new File(fileName), invoice);

            } catch (IOException e) {
                LOGGER.debug("Error in generating json file",e);
            }

        });
    }
}
