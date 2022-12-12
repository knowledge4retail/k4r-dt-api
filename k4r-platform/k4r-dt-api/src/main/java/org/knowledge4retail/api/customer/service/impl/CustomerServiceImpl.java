package org.knowledge4retail.api.customer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.customer.converter.CustomerConverter;
import org.knowledge4retail.api.customer.dto.CustomerDto;
import org.knowledge4retail.api.customer.model.Customer;
import org.knowledge4retail.api.customer.repository.CustomerRepository;
import org.knowledge4retail.api.customer.service.CustomerService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    private final DefaultProducer producer;

    @Value("${org.knowledge4retail.api.listener.kafka.topics.customer}")
    private String kafkaTopic;


    public CustomerServiceImpl(CustomerRepository repository, DefaultProducer producer) {

        this.repository = repository;
        this.producer = producer;
    }

    @Override
    public CustomerDto create(CustomerDto customerDto) {

        customerDto.setId(null);
        Customer customer = CustomerConverter.INSTANCE.dtoToCustomer(customerDto);

        CustomerDto createdDto = CustomerConverter.INSTANCE.customerToDto(this.repository.save(customer));
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public CustomerDto update(Integer customerId, CustomerDto customerDto) {

        CustomerDto oldDto = read(customerId);

        customerDto.setId(customerId);

        Customer customer = CustomerConverter.INSTANCE.dtoToCustomer(customerDto);

        CustomerDto createdDto = CustomerConverter.INSTANCE.customerToDto(this.repository.save(customer));

        producer.publishUpdate(kafkaTopic, createdDto, oldDto);
        return createdDto;
    }

    @Override
    public List<CustomerDto> readAll() {
        return CustomerConverter.INSTANCE.customersToDtos(this.repository.findAll());
    }

    @Override
    public CustomerDto read(Integer customerId) {

        Customer customer = this.repository.getReferenceById(customerId);

        return CustomerConverter.INSTANCE.customerToDto(customer);

    }

    @Override
    public Integer delete(Integer customerId) {

        CustomerDto deletedDto = read(customerId);

        this.repository.deleteById(customerId);

        producer.publishDelete(kafkaTopic, deletedDto);
        return customerId;
    }

    @Override
    public boolean exists(Integer customerId) {
        return this.repository.existsById(customerId);
    }
}
