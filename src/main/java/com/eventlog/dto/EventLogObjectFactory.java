package com.eventlog.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class EventLogObjectFactory {

    public static EventLogObjectDto getInstance(String objectType, String payload) throws IOException {
        EventLogObjectDto dto = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        switch (objectType) {
            case "INVOICE":
                dto = objectMapper.readValue(payload, InvoiceDto.class);
                break;
        }

        return dto;
    }
}
