package org.knowledge4retail.api.customer.converter;

import org.knowledge4retail.api.customer.dto.CustomerDto;
import org.knowledge4retail.api.customer.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerConverter {
    CustomerConverter INSTANCE = Mappers.getMapper(CustomerConverter.class);

    Customer dtoToCustomer(CustomerDto customerDto);

    CustomerDto customerToDto(Customer customer);

    List<CustomerDto> customersToDtos(List<Customer> customers);
}

