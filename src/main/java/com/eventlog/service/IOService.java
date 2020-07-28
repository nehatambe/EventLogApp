package com.eventlog.service;

import com.eventlog.dto.EventLogDto;
import java.util.List;

public interface IOService {

  List<EventLogDto> readFile(String Path);

  void generateFiles(List<EventLogDto> eventLogDtos);
}
