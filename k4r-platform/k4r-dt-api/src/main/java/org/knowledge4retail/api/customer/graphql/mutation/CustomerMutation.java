package org.knowledge4retail.api.customer.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.customer.filter.CustomerFilter;
import org.knowledge4retail.api.customer.model.Customer;
import org.knowledge4retail.api.customer.repository.CustomerRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

import static org.knowledge4retail.api.shared.util.SearchUtil.copyNonNullProperties;
import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerMutation implements GraphQLMutationResolver {

    private final CustomerRepository customerRepository;

    public Customer updateCustomer (Customer updateCustomer, CustomerFilter filter) throws IllegalAccessException{

        log.info("update oder post a single customer using graphQL");
        if (filter != null) {

            Specification<Customer> spec = getSpecification(filter, null);
            try {

                Customer originalCustomer = customerRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateCustomer.setId(originalCustomer.getId());
                copyNonNullProperties(updateCustomer, originalCustomer);
                return customerRepository.save(originalCustomer);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return customerRepository.save(updateCustomer);
    }

    public List<Customer> postCustomers (List<Customer> customers){

        log.info("create a list of customers using graphQL");
        return customerRepository.saveAll(customers);
    }
}
