package com.eventlog;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class EventLogAppApplicationTests {

  /*@SpyBean
  CommandLineStartupRunner commandLineStartupRunner;

  @Test
  void whenContextLoads_thenRunnersRun() throws Exception {

  	Mockito.verify(commandLineStartupRunner, Mockito.times(1)).run(any());
  }*/

}
