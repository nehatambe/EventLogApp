package com.eventlog.service;

import com.eventlog.dto.EventLogDto;
import com.eventlog.dto.EventLogObjectDto;
import java.util.Set;

public interface LogObjectService {
  void performOperations(EventLogDto eventLogDto);

  void create(EventLogObjectDto objectDto);

  void update(EventLogObjectDto objectDto);

  void delete(String invoiceId);

  void generateJsonFiles(Set<String> ids);

  String getServiceName();
}
