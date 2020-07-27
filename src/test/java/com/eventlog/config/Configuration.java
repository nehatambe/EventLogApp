package com.eventlog.config;


import com.eventlog.service.IOService;
import com.eventlog.service.IOServiceImpl;
import com.eventlog.service.LogObjectService;
import com.eventlog.service.InvoiceServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class Configuration {

    @Bean
    public LogObjectService invoiceService() {
        return new InvoiceServiceImpl();
    }

    @Bean
    public IOService ioService() {
        return new IOServiceImpl();
    }
}
