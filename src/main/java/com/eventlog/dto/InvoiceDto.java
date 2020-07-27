package com.eventlog.dto;

import com.eventlog.enums.Currency;
import com.eventlog.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

public class InvoiceDto extends EventLogObjectDto {

    String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    BigDecimal amount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Currency currency;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Status status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer items;


    public InvoiceDto() {
    }

    public InvoiceDto(String id, BigDecimal amount, Currency currency, Status status, Integer items) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getItems() {
        return items;
    }

    public void setItems(Integer items) {
        this.items = items;
    }
}
