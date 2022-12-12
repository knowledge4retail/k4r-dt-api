package org.knowledge4retail.api.customer.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.customer.filter.CustomerFilter;
import org.knowledge4retail.api.customer.model.Customer;
import org.knowledge4retail.api.customer.repository.CustomerRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerQueryResolver implements GraphQLQueryResolver {

    private final CustomerRepository customerRepository;

    public List<Customer> getCustomers (CustomerFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all customers with the desired attributes");
            return customerRepository.findAll();
        }

        log.info("Get all customers with the desired attributes and filtered with: {}", filter);
        Specification<Customer> spec = getSpecification(filter, null);
        return customerRepository.findAll(spec);
    }
}
