package com.eventlog.service;

import com.eventlog.dto.EventLogDto;

import java.util.List;

public interface TaskService {
    void processData(List<EventLogDto> eventLogDtos);
}
