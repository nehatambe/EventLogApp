package com.eventlog.service;

import static org.mockito.ArgumentMatchers.any;

import com.eventlog.dto.EventLogDto;
import com.eventlog.dto.InvoiceDto;
import com.eventlog.enums.Operation;
import com.eventlog.exception.InvoiceNotFoundException;
import com.eventlog.model.Invoice;
import com.eventlog.repository.InvoiceRepository;
import com.eventlog.utils.TestCreateObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class InvoiceServiceTest {

  private static final String INVOICE_ID = "Inv1";
  private static final String INVOICE_ID_2 = "Inv2";

  @Autowired InvoiceService invoiceService;

  @MockBean InvoiceRepository invoiceRepository;

  @MockBean ModelMapper modelMapper;

  @Mock private ObjectMapper objectMapper;

  Invoice invoice;
  EventLogDto eventLogDto;
  InvoiceDto invoiceDto;

  @TestConfiguration
  static class InvoiceServiceImplTestContextConfiguration {

    @Bean
    public InvoiceService invoiceService() {
      return new InvoiceServiceImpl();
    }
  }

  @Before
  public void setUp() throws Exception {
    invoice = TestCreateObjectUtils.getInvoice(INVOICE_ID);
    eventLogDto = TestCreateObjectUtils.getEventLogDto(INVOICE_ID, Operation.CREATE);
    invoiceDto = TestCreateObjectUtils.getInvoiceDto(INVOICE_ID);
  }

  @Test
  public void whenValidInvoiceDataWithId_createInvoice_thenSuccess() {

    Mockito.when(modelMapper.map(any(), any())).thenReturn(invoice);
    BDDMockito.given(invoiceRepository.save(any(Invoice.class))).willReturn(invoice);
    Invoice invoice = invoiceService.create(invoiceDto);
    Assert.assertNotNull(invoice);
  }

  @Test
  public void whenValidInvoiceDataWithId_updateInvoice_thenSuccess() {
    BigDecimal newAmount = new BigDecimal(140);
    // change amount
    invoiceDto.setAmount(newAmount);
    invoice.setAmount(newAmount);
    BDDMockito.given(invoiceRepository.findById(any(String.class)))
        .willReturn(Optional.of(invoice));

    BDDMockito.given(invoiceRepository.save(any(Invoice.class))).willReturn(invoice);
    Invoice invoice = invoiceService.update(invoiceDto);
    Assert.assertNotNull(invoice);
  }

  @Test(expected = InvoiceNotFoundException.class)
  public void whenInvalidInvoiceDataWithId_updateInvoice_thenThrowException() {
    BigDecimal newAmount = new BigDecimal(140);
    // change amount
    invoiceDto.setAmount(newAmount);
    invoice.setAmount(newAmount);
    BDDMockito.given(invoiceRepository.findById(any(String.class))).willReturn(Optional.empty());

    BDDMockito.given(invoiceRepository.save(any(Invoice.class))).willReturn(invoice);
    invoiceService.update(invoiceDto);
  }

  @Test
  public void whenValidInvoiceDataWithId_generateJsonFiles_thenSuccess() throws IOException {
    Set<String> ids = new HashSet<>();
    ids.add(INVOICE_ID);
    ids.add(INVOICE_ID_2);
    BDDMockito.given(invoiceRepository.findAllById(any(Set.class)))
        .willReturn(TestCreateObjectUtils.getInvoiceList());
    Mockito.doNothing().when(objectMapper).writeValue(any(File.class), any(Object.class));
    invoiceService.generateJsonFiles(ids);
    Mockito.verify(invoiceRepository, Mockito.times(1)).findAllById(any(Set.class));
  }
}
