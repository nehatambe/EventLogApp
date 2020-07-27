package com.eventlog.dto;

import com.eventlog.enums.LogObjectType;
import com.eventlog.enums.Operation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class EventLogDto<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLogDto.class);

    Operation operationType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date timestamp;

    LogObjectType objectType;
    String objectId;
    T payload;

    public EventLogDto() {
    }

    public EventLogDto(Operation operationType, Date timestamp, LogObjectType objectType, String objectId, T payload) {
        this.operationType = operationType;
        this.timestamp = timestamp;
        this.objectType = objectType;
        this.objectId = objectId;
        this.payload = payload;
    }

    public Operation getOperationType() {
        return operationType;
    }

    public void setOperationType(Operation operationType) {
        this.operationType = operationType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public LogObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(LogObjectType objectType) {
        this.objectType = objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public String[] toStringArray() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
        String formattedDate = DateFormatUtils.format(timestamp,"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String jsonVal = null;
        try {
            jsonVal = objectMapper.writeValueAsString(this.payload);
        } catch (JsonProcessingException e) {
            LOGGER.debug("Error in converting invoice dto to json string",e);
        }
        return new String[]{operationType.toString(),formattedDate,objectType.toString(),objectId,jsonVal};
    }

    @Override
    public String toString() {
        return "EventLogDto{" +
                "operationType=" + operationType +
                ", timestamp=" + timestamp +
                ", objectType='" + objectType + '\'' +
                ", objectId='" + objectId + '\'' +
                ", payload=" + payload +
                '}';
    }
}
