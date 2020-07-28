package com.eventlog.service;

import com.eventlog.dto.EventLogDto;
import java.util.*;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

  private static final String SERVICE_NAME_SUFFIX = "Service";

  private final BeanFactory beanFactory;

  @Autowired IOService ioService;

  @Autowired
  public TaskServiceImpl(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  @Override
  public void processData(String filePath) {
    List<EventLogDto> eventLogDtos = ioService.readFile(filePath);
    LOGGER.info("Sorting event logs by timestamp in the ascending order");
    if (!eventLogDtos.isEmpty()) {
      Collections.sort(eventLogDtos, Comparator.comparing(EventLogDto::getTimestamp));
      Map<String, Set<String>> typeSetMap = new HashMap<>();

      eventLogDtos.forEach(
          eventLogDto -> {
            performOperation(eventLogDto);
            Set<String> tempSet =
                typeSetMap.getOrDefault(eventLogDto.getObjectType(), new HashSet<>());
            tempSet.add(eventLogDto.getObjectId());
            typeSetMap.put(eventLogDto.getObjectType(), tempSet);
          });
      generateJsonFiles(typeSetMap);
      ioService.generateFiles(eventLogDtos);
    }
  }

  public void performOperation(EventLogDto eventLogDto) {
    try {
      LogObjectService service =
          beanFactory.getBean(getServiceName(eventLogDto.getObjectType()), LogObjectService.class);

      service.performOperations(eventLogDto);

    } catch (BeansException e) {
      LOGGER.error("No bean found for given object type {}", eventLogDto.getObjectType());
    }
  }

  private String getServiceName(String objectType) {
    return WordUtils.capitalizeFully(objectType) + SERVICE_NAME_SUFFIX;
  }

  public void generateJsonFiles(Map<String, Set<String>> typeSetMap) {
    LOGGER.info("Generating JSON files");
    typeSetMap.forEach(
        (logObjectType, ids) -> {
          try {
            LogObjectService service =
                beanFactory.getBean(getServiceName(logObjectType), LogObjectService.class);

            service.generateJsonFiles(ids);

          } catch (BeansException e) {
            LOGGER.error("No bean found for given object type {}", logObjectType);
          }
        });
  }
}
