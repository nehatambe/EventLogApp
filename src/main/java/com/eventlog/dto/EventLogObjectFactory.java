package com.eventlog.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventLogObjectFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventLogObjectFactory.class);

  private EventLogObjectFactory() {
    // Empty Constructor
  }

  public static EventLogObjectDto getInstance(String objectType, String payload)
      throws IOException {
    EventLogObjectDto dto = null;
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    switch (objectType) {
      case "INVOICE":
        dto = objectMapper.readValue(payload, InvoiceDto.class);
        break;
    }
    LOGGER.info("Return the instance of EventLogObjectDto for object type {}", objectType);
    return dto;
  }
}
