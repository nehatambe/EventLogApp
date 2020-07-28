package com.eventlog;

import com.eventlog.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class CommandLineStartupRunner implements CommandLineRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineStartupRunner.class);

  @Autowired TaskService taskService;

  @Override
  public void run(String... args) throws Exception {

    if (args.length == 0) {
      LOGGER.debug("Path is blank");
    }
    String filePath = args[0];
    taskService.processData(filePath);
  }
}
