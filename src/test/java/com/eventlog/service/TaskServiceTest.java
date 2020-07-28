package com.eventlog.service;

import static org.hamcrest.CoreMatchers.*;

import com.eventlog.dto.EventLogDto;
import com.eventlog.utils.TestCreateObjectUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TaskServiceTest {

  @TestConfiguration
  static class TaskServiceTestContextConfiguration {

    @Bean
    public TaskService taskService() {
      BeanFactory beanFactory = Mockito.mock(BeanFactory.class);
      Mockito.when(
              beanFactory.getBean(
                  ArgumentMatchers.any(String.class), ArgumentMatchers.eq(LogObjectService.class)))
          .thenReturn(Mockito.mock(LogObjectService.class));
      return new TaskServiceImpl(beanFactory);
    }
  }

  @Autowired TaskService taskService;

  @MockBean IOService ioService;

  @MockBean LogObjectService logObjectService;

  @Test
  public void whenGivenEventLogData_processData_thenSuccess() {

    Mockito.when(ioService.readFile(ArgumentMatchers.any(String.class)))
        .thenReturn(TestCreateObjectUtils.getEventLogDtoList());
    Mockito.doNothing().when(ioService).generateFiles(ArgumentMatchers.any(List.class));
    Mockito.doNothing()
        .when(logObjectService)
        .performOperations(ArgumentMatchers.any(EventLogDto.class));
    Mockito.doNothing().when(logObjectService).generateJsonFiles(ArgumentMatchers.any(Set.class));
    taskService.processData("test");
    Mockito.verify(ioService, Mockito.times(1)).readFile(ArgumentMatchers.any(String.class));
  }

  @Test
  public void whenEmptyEventLogDataList_processData_thenSuccess() {

    Mockito.when(ioService.readFile(ArgumentMatchers.any(String.class)))
        .thenReturn(new ArrayList<>());
    taskService.processData("test");
    Mockito.verify(ioService, Mockito.times(1)).readFile(ArgumentMatchers.any(String.class));
  }
}
