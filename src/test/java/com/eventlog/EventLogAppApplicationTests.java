package com.eventlog;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class EventLogAppApplicationTests {

	@Test
	void contextLoads() {
	}

	/*@SpyBean
	CommandLineStartupRunner commandLineStartupRunner;

	@Test
	void whenContextLoads_thenRunnersRun() throws Exception {

		Mockito.verify(commandLineStartupRunner, Mockito.times(1)).run(any());
	}*/

}
