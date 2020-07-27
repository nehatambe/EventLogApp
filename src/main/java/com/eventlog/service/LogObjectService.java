package com.eventlog.service;

import com.eventlog.dto.EventLogDto;
import com.eventlog.dto.EventLogObjectDto;
import com.eventlog.dto.InvoiceDto;
import com.eventlog.enums.LogObjectType;
import com.eventlog.model.Invoice;

import java.util.List;
import java.util.Set;


public interface LogObjectService {
    void performOperations(EventLogDto eventLogDto);
    void create(EventLogObjectDto objectDto);
    void update(EventLogObjectDto objectDto);
    void delete(String invoiceId);
    List<Invoice> findByIds(Set<String> ids);
    boolean match(LogObjectType logObjectType);
}
