package org.knowledge4retail.api.product.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.ProductUnitFilter;
import org.knowledge4retail.api.product.model.ProductUnit;
import org.knowledge4retail.api.product.repository.ProductUnitRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductUnitQueryResolver implements GraphQLQueryResolver {

    private final ProductUnitRepository productUnitRepository;

    public List<ProductUnit> getProductUnits (ProductUnitFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all productUnits with the desired attributes");
            return productUnitRepository.findAll();
        }

        log.info("Get all productUnits with the desired attributes and filtered with: {}", filter);
        Specification<ProductUnit> spec = getSpecification(filter, null);
        return productUnitRepository.findAll(spec);
    }
}
