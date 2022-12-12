package org.knowledge4retail.api.product.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.ProductPropertyFilter;
import org.knowledge4retail.api.product.model.ProductProperty;
import org.knowledge4retail.api.product.repository.ProductPropertyRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductPropertyQueryResolver implements GraphQLQueryResolver {

    private final ProductPropertyRepository productPropertyRepository;

    public List<ProductProperty> getProductProperties (ProductPropertyFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all productProperties with the desired attributes");
            return productPropertyRepository.findAll();
        }

        log.info("Get all productProperties with the desired attributes and filtered with: {}", filter);
        Specification<ProductProperty> spec = getSpecification(filter, null);
        return productPropertyRepository.findAll(spec);
    }
}
