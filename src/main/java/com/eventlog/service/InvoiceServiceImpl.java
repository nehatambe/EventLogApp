package com.eventlog.service;

import com.eventlog.dto.EventLogObjectDto;
import com.eventlog.enums.Operation;
import com.eventlog.exception.InvoiceNotFoundException;
import com.eventlog.dto.EventLogDto;
import com.eventlog.dto.InvoiceDto;
import com.eventlog.model.Invoice;
import com.eventlog.repository.InvoiceRepository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service("InvoiceService")
public class InvoiceServiceImpl implements LogObjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void performOperations(EventLogDto eventLogDto) {

            if(eventLogDto.getOperationType().equals(Operation.CREATE)){
                create((InvoiceDto) eventLogDto.getPayload());
            }else if(eventLogDto.getOperationType().equals(Operation.UPDATE)){
                InvoiceDto invoiceDto = (InvoiceDto) eventLogDto.getPayload();
                invoiceDto.setId(eventLogDto.getObjectId());
                update(invoiceDto);
            }else if(eventLogDto.getOperationType().equals(Operation.DELETE)){
                delete(eventLogDto.getObjectId());
            }
    }

    @Override
    public void create(EventLogObjectDto objectDto) {
        invoiceRepository.save(modelMapper.map(objectDto,Invoice.class));
    }

    @Override
    public void update(EventLogObjectDto objectDto) {
        InvoiceDto invoiceDto = (InvoiceDto) objectDto;
        Invoice invoice = invoiceRepository.findById(invoiceDto.getId()).orElseThrow(InvoiceNotFoundException::new);
        if(invoiceDto.getAmount()!=null){
            invoice.setAmount(invoiceDto.getAmount());
        }
        if(invoiceDto.getCurrency()!=null){
            invoice.setCurrency(invoiceDto.getCurrency());
        }
        if(invoiceDto.getStatus()!=null){
            invoice.setStatus(invoiceDto.getStatus());
        }
        if(invoiceDto.getItems()!=null){
            invoice.setItems(invoiceDto.getItems());
        }
        invoiceRepository.save(invoice);
    }

    @Override
    public void delete(String invoiceId) {
        invoiceRepository.findById(invoiceId).orElseThrow(InvoiceNotFoundException::new);
        invoiceRepository.deleteById(invoiceId);
    }

    @Override
    public void generateJsonFiles(Set<String> invoiceIds){
        List<Invoice> invoiceList = invoiceRepository.findAllById(invoiceIds);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        invoiceList.forEach(invoice -> {
            String fileName = invoice.getId()+".json";
            try {
                objectMapper.writeValue(new File(fileName), invoice);

            } catch (IOException e) {
                LOGGER.debug("Error in generating json file",e);
            }

        });
    }

    @Override
    public String getServiceName() {
        return "Invoice";
    }


}
