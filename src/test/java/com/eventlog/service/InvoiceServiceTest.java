package com.eventlog.service;

import com.eventlog.config.Configuration;
import com.eventlog.dto.EventLogDto;
import com.eventlog.dto.InvoiceDto;
import com.eventlog.enums.Currency;
import com.eventlog.enums.Operation;
import com.eventlog.enums.Status;
import com.eventlog.exception.InvoiceNotFoundException;
import com.eventlog.model.Invoice;
import com.eventlog.repository.InvoiceRepository;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Configuration.class)
public class InvoiceServiceTest {


    private static final String INVOICE_ID ="Inv1";
    private static final String INVOICE_ID_2 ="Inv2";

    @Autowired
    LogObjectService invoiceService;

    @MockBean
    InvoiceRepository invoiceRepository;

    @MockBean
    ModelMapper modelMapper;

    @Test
    public void whenValidInvoiceDataWithId_createInvoice_thenSuccess() {

        Mockito.when(modelMapper.map(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(getInvoice());
        BDDMockito.given(invoiceRepository.save(ArgumentMatchers.any(Invoice.class))).willReturn(getInvoice());
        Invoice invoice = invoiceService.create(getInvoiceDto(INVOICE_ID));
        Assert.assertNotNull(invoice);
        assertThat(invoice.getId())
                .isEqualTo(INVOICE_ID);
    }

    @Test
    public void whenValidInvoiceDataWithId_updateInvoice_thenSuccess() {
        BigDecimal newAmount = new BigDecimal(140);
        //change amount
        InvoiceDto invoiceDto = getInvoiceDto(INVOICE_ID);
        invoiceDto.setAmount(newAmount);
        Invoice invoice = getInvoice();
        invoice.setAmount(newAmount);
        BDDMockito.given(invoiceRepository.findById(ArgumentMatchers.any(String.class))).willReturn(Optional.of(invoice));

        BDDMockito.given(invoiceRepository.save(ArgumentMatchers.any(Invoice.class))).willReturn(invoice);
        Invoice invoice1 = invoiceService.update(invoiceDto);

        Assert.assertNotNull(invoice1);
        assertThat(invoice.getId())
                .isEqualTo(INVOICE_ID);
        assertThat(invoice.getAmount())
                .isEqualTo(newAmount);

    }

    @Test(expected = InvoiceNotFoundException.class)
    public void whenInvalidInvoiceDataWithId_updateInvoice_thenThrowException() {
        BigDecimal newAmount = new BigDecimal(140);
        //change amount
        InvoiceDto invoiceDto = getInvoiceDto(INVOICE_ID);
        invoiceDto.setAmount(newAmount);
        Invoice invoice = getInvoice();
        invoice.setAmount(newAmount);
        BDDMockito.given(invoiceRepository.findById(ArgumentMatchers.any(String.class))).willReturn(Optional.empty());

        BDDMockito.given(invoiceRepository.save(ArgumentMatchers.any(Invoice.class))).willReturn(invoice);
        Invoice invoice1 = invoiceService.update(invoiceDto);

    }

    @Test(expected = InvoiceNotFoundException.class)
    public void whenInvalidInvoiceDataWithId_deleteInvoice_thenThrowException() {

        BDDMockito.given(invoiceRepository.findById(ArgumentMatchers.any(String.class))).willReturn(Optional.empty());

        Mockito.doNothing().when(invoiceRepository).deleteById(ArgumentMatchers.any(String.class));
        invoiceService.delete(INVOICE_ID);

    }

    @Test
    public void whenValidInvoiceDataWithId_processInvoiceData_thenSuccess() {


    }

    private Invoice getInvoice(){
        Invoice invoice = new Invoice(INVOICE_ID,new BigDecimal(120.0), Currency.USD, Status.DRAFT,2);
        return invoice;
    }

    private InvoiceDto getInvoiceDto(String invoiceId){
        InvoiceDto invoiceDto = new InvoiceDto(invoiceId,new BigDecimal(120.0), Currency.USD, Status.DRAFT,2);
        return invoiceDto;
    }

    private List<InvoiceDto> getInvoiceDtoList(){
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        InvoiceDto invoiceDto = getInvoiceDto(INVOICE_ID);
        invoiceDtoList.add(invoiceDto);
        invoiceDto = getInvoiceDto(INVOICE_ID_2);
        invoiceDtoList.add(invoiceDto);
        return invoiceDtoList;
    }

    private EventLogDto getEventLogDto(String invoiceId, Operation operationType){
        EventLogDto eventLogDto = new EventLogDto();
        eventLogDto.setObjectId(invoiceId);
        eventLogDto.setOperationType(operationType);
        eventLogDto.setTimestamp(new Date());
        eventLogDto.setObjectType("INVOICE");

        return eventLogDto;
    }

    private List<EventLogDto> getEventLogDtoList(){
        List<EventLogDto> eventLogDtoList = new ArrayList<>();

        return eventLogDtoList;
    }

}
