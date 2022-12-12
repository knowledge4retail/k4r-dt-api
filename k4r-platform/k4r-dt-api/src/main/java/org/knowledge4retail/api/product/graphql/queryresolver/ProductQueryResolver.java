package org.knowledge4retail.api.product.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.ProductFilter;
import org.knowledge4retail.api.product.model.Product;
import org.knowledge4retail.api.product.repository.ProductRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductQueryResolver implements GraphQLQueryResolver {

    private final ProductRepository productRepository;

    public List<Product> getProducts (ProductFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all products with the desired attributes");
            return productRepository.findAll();
        }

        log.info("Get all products with the desired attributes and filtered with: {}", filter);
        Specification<Product> spec = getSpecification(filter, null);
        return productRepository.findAll(spec);
    }
}
