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

import com.eventlog.utils.TestCreateObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringRunner.class)
public class InvoiceServiceTest {


    private static final String INVOICE_ID ="Inv1";
    private static final String INVOICE_ID_2 ="Inv2";

    @Autowired
    LogObjectService invoiceService;

    @MockBean
    InvoiceRepository invoiceRepository;

    @MockBean
    ModelMapper modelMapper;

    @Mock
    private ObjectMapper objectMapper;

    Invoice invoice;
    EventLogDto eventLogDto;
    InvoiceDto invoiceDto;

    @TestConfiguration
    static class InvoiceServiceImplTestContextConfiguration {

        @Bean
        public LogObjectService invoiceService() {
            return new InvoiceServiceImpl();
        }
    }

    @Before
    public void setUp() throws Exception {
        invoice = TestCreateObjectUtils.getInvoice(INVOICE_ID);
        eventLogDto = TestCreateObjectUtils.getEventLogDto(INVOICE_ID,Operation.CREATE);
        invoiceDto = TestCreateObjectUtils.getInvoiceDto(INVOICE_ID);
    }

    @Test
    public void whenValidInvoiceDataWithId_createInvoice_thenSuccess() {

        Mockito.when(modelMapper.map(any(), any())).thenReturn(invoice);
        BDDMockito.given(invoiceRepository.save(any(Invoice.class))).willReturn(invoice);
        invoiceService.create(invoiceDto);
    }

    @Test
    public void whenValidInvoiceDataWithId_updateInvoice_thenSuccess() {
        BigDecimal newAmount = new BigDecimal(140);
        //change amount
        invoiceDto.setAmount(newAmount);
        invoice.setAmount(newAmount);
        BDDMockito.given(invoiceRepository.findById(any(String.class))).willReturn(Optional.of(invoice));

        BDDMockito.given(invoiceRepository.save(any(Invoice.class))).willReturn(invoice);
        invoiceService.update(invoiceDto);

    }

    @Test(expected = InvoiceNotFoundException.class)
    public void whenInvalidInvoiceDataWithId_updateInvoice_thenThrowException() {
        BigDecimal newAmount = new BigDecimal(140);
        //change amount
        invoiceDto.setAmount(newAmount);
        invoice.setAmount(newAmount);
        BDDMockito.given(invoiceRepository.findById(any(String.class))).willReturn(Optional.empty());

        BDDMockito.given(invoiceRepository.save(any(Invoice.class))).willReturn(invoice);
        invoiceService.update(invoiceDto);

    }

    @Test(expected = InvoiceNotFoundException.class)
    public void whenInvalidInvoiceDataWithId_deleteInvoice_thenThrowException() {

        BDDMockito.given(invoiceRepository.findById(any(String.class))).willReturn(Optional.empty());
        Mockito.doNothing().when(invoiceRepository).deleteById(any(String.class));
        invoiceService.delete(INVOICE_ID);

    }

    @Test
    public void whenValidInvoiceDataWithId_processInvoiceData_thenSuccess() {

        BDDMockito.given(invoiceRepository.findById(any(String.class))).willReturn(Optional.empty());
        Mockito.doNothing().when(invoiceRepository).deleteById(any(String.class));
        BDDMockito.given(invoiceRepository.save(any(Invoice.class))).willReturn(invoice);
        invoiceService.performOperations(eventLogDto);
    }

    @Test
    public void whenValidInvoiceDataWithId_generateJsonFiles_thenSuccess() throws IOException {
        Set<String> ids = new HashSet<>();
        ids.add(INVOICE_ID);
        ids.add(INVOICE_ID_2);
        BDDMockito.given(invoiceRepository.findAllById(any(Set.class))).willReturn(TestCreateObjectUtils.getInvoiceList());
        Mockito.doNothing().when(objectMapper).writeValue(any(File.class),any(Object.class));
        invoiceService.generateJsonFiles(ids);
    }




}
