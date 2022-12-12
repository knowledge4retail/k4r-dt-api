package org.knowledge4retail.api.product.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.ProductGtinFilter;
import org.knowledge4retail.api.product.model.ProductGtin;
import org.knowledge4retail.api.product.repository.ProductGtinRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductGtinQueryResolver implements GraphQLQueryResolver {

    private final ProductGtinRepository productGtinRepository;

    public List<ProductGtin> getProductGtins (ProductGtinFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all productGtins with the desired attributes");
            return productGtinRepository.findAll();
        }

        log.info("Get all productGtins with the desired attributes and filtered with: {}", filter);
        Specification<ProductGtin> spec = getSpecification(filter, null);
        return productGtinRepository.findAll(spec);
    }
}
