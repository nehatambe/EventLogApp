package com.eventlog.service;

import com.eventlog.dto.EventLogDto;
import com.eventlog.enums.LogObjectType;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{

    private static final String SERVICE_NAME_SUFFIX = "Service";

    private final BeanFactory beanFactory;

    @Autowired
    public TaskServiceImpl(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


    @Override
    public void processData(List<EventLogDto> eventLogDtos) {
        //Sorting data by timestamp in the ascending order
        Collections.sort(eventLogDtos, Comparator.comparing(EventLogDto::getTimestamp));

        eventLogDtos.forEach(eventLogDto -> {
            performOperation(eventLogDto);

        });
    }

    public void performOperation(EventLogDto eventLogDto) {
        LogObjectService service = beanFactory.getBean(getServiceName(eventLogDto.getObjectType()),
                LogObjectService.class);

        service.performOperations(eventLogDto);
    }

    private String getServiceName(LogObjectType objectType) {

        return WordUtils.capitalizeFully(objectType.toString()) + SERVICE_NAME_SUFFIX;
    }
}
