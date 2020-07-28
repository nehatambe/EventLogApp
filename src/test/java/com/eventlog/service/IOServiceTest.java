package com.eventlog.service;

import com.eventlog.config.Configuration;
import com.eventlog.dto.EventLogDto;
import com.eventlog.enums.Operation;
import com.eventlog.model.Invoice;
import com.eventlog.utils.TestCreateObjectUtils;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
public class IOServiceTest {

    @TestConfiguration
    static class IOServiceImplTestContextConfiguration {

        @Bean
        public IOService ioService() {
            return new IOServiceImpl();
        }
    }

  @Autowired
  IOService ioService;

  @Mock private CSVReader csvReader;

  @Mock
  private CSVWriter csvWriter;

  @Before
  public void setUp() throws Exception {}

  @Test
  public void whenGivenFilePath_readFile_thenListOfDtos() {

      Path resourceDirectory = Paths.get("src","test","resources");
      String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    List<EventLogDto> eventLogDtoList = ioService.readFile(absolutePath+"/Events_Test.csv");
    assertThat(eventLogDtoList.size(), is(7));
  }

    @Test
    public void whenGivenSortedRecords_writeFile_thenSuccess() {

        Mockito.doNothing().when(csvWriter).writeNext(Mockito.any(String[].class));

        ioService.generateFiles(TestCreateObjectUtils.getEventLogDtoList());
    }

}
