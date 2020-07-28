package com.eventlog.service;

import com.eventlog.dto.EventLogObjectDto;
import com.eventlog.model.Invoice;

public interface InvoiceService extends LogObjectService {

  Invoice create(EventLogObjectDto objectDto);

  Invoice update(EventLogObjectDto objectDto);

  void delete(String invoiceId);
}
