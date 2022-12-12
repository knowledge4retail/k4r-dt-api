package org.knowledge4retail.api.customer.service;

import org.knowledge4retail.api.customer.dto.CustomerDto;

import java.util.List;

public interface CustomerService {

    CustomerDto create(CustomerDto customer);

    List<CustomerDto> readAll();

    CustomerDto read(Integer customerId);


    CustomerDto update(Integer customerId, CustomerDto customer);

    Integer delete(Integer customerId);

    boolean exists(Integer customerId);
}
