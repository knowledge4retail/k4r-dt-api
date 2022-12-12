package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.customer.dto.CustomerDto;
import org.knowledge4retail.api.customer.repository.CustomerRepository;
import org.knowledge4retail.api.customer.service.impl.CustomerServiceImpl;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;
    @Mock
    private DefaultProducer producer;

    private CustomerServiceImpl service;


    @BeforeEach
    public void setUp(){

        service = new CustomerServiceImpl(repository, producer);
    }

    @Test
    public void createSendsMessageToKafka() {

        service.create(new CustomerDto());

        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        when(repository.getReferenceById(any())).thenReturn(any());

        service.update(1, new CustomerDto());

        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka() {

        service.delete(any());

        verify(producer).publishDelete(any(), any());
    }

    @Test
    public void updateCustomerUsesIdPassedAsMethodArgument() {

        Integer customerId = 500;
        Integer objectId = 1;

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(objectId);
        customerDto.setAnonymisedName("TestCustomer");

        service.update(customerId, customerDto);

        Assertions.assertEquals(customerId, customerDto.getId());
    }
}
