package org.knowledge4retail.api.product.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.ProductCharacteristicFilter;
import org.knowledge4retail.api.product.model.ProductCharacteristic;
import org.knowledge4retail.api.product.repository.ProductCharacteristicRepository;
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
public class ProductCharacteristicMutation implements GraphQLMutationResolver {

    private final ProductCharacteristicRepository productCharacteristicRepository;

    public ProductCharacteristic updateProductCharacteristic (ProductCharacteristic updateProductCharacteristic, ProductCharacteristicFilter filter) throws IllegalAccessException{

        log.info("update oder post a single productCharacteristic using graphQL");
        if (filter != null) {

            Specification<ProductCharacteristic> spec = getSpecification(filter, null);
            try {

                ProductCharacteristic originalProductCharacteristic = productCharacteristicRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateProductCharacteristic.setId(originalProductCharacteristic.getId());
                copyNonNullProperties(updateProductCharacteristic, originalProductCharacteristic);
                return productCharacteristicRepository.save(originalProductCharacteristic);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return productCharacteristicRepository.save(updateProductCharacteristic);
    }

    public List<ProductCharacteristic> postProductCharacteristics (List<ProductCharacteristic> productCharacteristics){

        log.info("create a list of productCharacteristics using graphQL");
        return productCharacteristicRepository.saveAll(productCharacteristics);
    }
}
