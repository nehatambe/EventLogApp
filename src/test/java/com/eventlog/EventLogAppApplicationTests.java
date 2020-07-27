package com.eventlog;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
class EventLogAppApplicationTests {

	@Test
	void contextLoads() {
	}

	@SpyBean
	CommandLineStartupRunner commandLineStartupRunner;

	@Test
	void whenContextLoads_thenRunnersRun() throws Exception {

		Mockito.verify(commandLineStartupRunner, Mockito.times(1)).run(any());
	}

}
