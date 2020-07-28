package com.eventlog.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.eventlog.dto.EventLogDto;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class IOServiceTest {

  @TestConfiguration
  static class IOServiceImplTestContextConfiguration {

    @Bean
    public IOService ioService() {
      return new IOServiceImpl();
    }
  }

  @Autowired IOService ioService;

  @Mock private CSVReader csvReader;

  @Mock private CSVWriter csvWriter;

  @Before
  public void setUp() throws Exception {}

  @Test
  public void whenGivenFilePath_readFile_thenListOfDtos() {

    Path resourceDirectory = Paths.get("src", "test", "resources");
    String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    List<EventLogDto> eventLogDtoList = ioService.readFile(absolutePath + "/Events_Test.csv");
    assertThat(eventLogDtoList.size(), is(7));
  }
}
