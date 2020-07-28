package com.eventlog.service;

import com.eventlog.dto.EventLogDto;
import java.util.Set;

public interface LogObjectService {
  void performOperations(EventLogDto eventLogDto);

  void generateJsonFiles(Set<String> ids);
}
