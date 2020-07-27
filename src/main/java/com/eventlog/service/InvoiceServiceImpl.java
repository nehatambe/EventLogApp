package com.eventlog.service;

import com.eventlog.dto.EventLogObjectDto;
import com.eventlog.enums.LogObjectType;
import com.eventlog.enums.Operation;
import com.eventlog.exception.InvoiceNotFoundException;
import com.eventlog.dto.EventLogDto;
import com.eventlog.dto.InvoiceDto;
import com.eventlog.model.Invoice;
import com.eventlog.repository.InvoiceRepository;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
    public List<Invoice> findByIds(Set<String> invoiceIds) {
        return invoiceRepository.findAllById(invoiceIds);
    }

    @Override
    public boolean match(LogObjectType logObjectType) {
        return logObjectType.equals(LogObjectType.INVOICE);
    }

}
