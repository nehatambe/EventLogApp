package com.eventlog.utils;

import com.eventlog.dto.EventLogDto;
import com.eventlog.dto.InvoiceDto;
import com.eventlog.enums.Currency;
import com.eventlog.enums.Operation;
import com.eventlog.enums.Status;
import com.eventlog.model.Invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestCreateObjectUtils {

    private static final String INVOICE_ID ="Inv1";
    private static final String INVOICE_ID_2 ="Inv2";

    public static EventLogDto getEventLogDto(String invoiceId, Operation operationType) {
        EventLogDto eventLogDto = new EventLogDto();
        eventLogDto.setObjectId(invoiceId);
        eventLogDto.setOperationType(operationType);
        eventLogDto.setTimestamp(new Date());
        eventLogDto.setObjectType("INVOICE");
        return eventLogDto;
    }

    public static List<EventLogDto> getEventLogDtoList() {
        List<EventLogDto> eventLogDtoList = new ArrayList<>();
        eventLogDtoList.add(getEventLogDto(INVOICE_ID, Operation.CREATE));
        return eventLogDtoList;
    }

    public static Invoice getInvoice(String invoiceId){
        Invoice invoice = new Invoice(invoiceId,new BigDecimal(120.0), Currency.USD, Status.DRAFT,2);
        return invoice;
    }

    public static InvoiceDto getInvoiceDto(String invoiceId){
        InvoiceDto invoiceDto = new InvoiceDto(invoiceId,new BigDecimal(120.0), Currency.USD, Status.DRAFT,2);
        return invoiceDto;
    }

    public static List<Invoice> getInvoiceList(){
        List<Invoice> invoiceList = new ArrayList<>();
        invoiceList.add(getInvoice(INVOICE_ID));
        invoiceList.add(getInvoice(INVOICE_ID_2));
        return invoiceList;
    }
}
