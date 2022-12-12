package org.knowledge4retail.api.product.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.ProductCharacteristicFilter;
import org.knowledge4retail.api.product.model.ProductCharacteristic;
import org.knowledge4retail.api.product.repository.ProductCharacteristicRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductCharacteristicQueryResolver implements GraphQLQueryResolver {

    private final ProductCharacteristicRepository productCharacteristicRepository;

    public List<ProductCharacteristic> getProductCharacteristics (ProductCharacteristicFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all productCharacteristics with the desired attributes");
            return productCharacteristicRepository.findAll();
        }

        log.info("Get all productCharacteristics with the desired attributes and filtered with: {}", filter);
        Specification<ProductCharacteristic> spec = getSpecification(filter, null);
        return productCharacteristicRepository.findAll(spec);
    }
}
