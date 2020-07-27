package com.eventlog;

import com.eventlog.dto.EventLogDto;
import com.eventlog.service.IOService;
import com.eventlog.service.LogObjectService;
import com.eventlog.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandLineStartupRunner implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineStartupRunner.class);

    @Autowired
    TaskService taskService;

    @Autowired
    IOService ioService;

    @Override
    public void run(String... args) throws Exception {

        if(args.length == 0){
            LOGGER.debug("Path is blank");
            //System.exit(0);
        }

        String filePath = args[0];
        List<EventLogDto> eventLogDtos = ioService.readFile(filePath);
        taskService.processData(eventLogDtos);
        ioService.generateFiles(eventLogDtos);
    }
}
