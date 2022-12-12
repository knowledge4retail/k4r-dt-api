package org.knowledge4retail.api.product.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.model.Product;
import org.knowledge4retail.api.product.model.ProductUnit;
import org.knowledge4retail.api.product.repository.ProductRepository;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class ProductUnitResolver implements GraphQLResolver<ProductUnit> {

    private final ProductRepository productRepository;

    public Product getProduct(ProductUnit productUnit, DataFetchingEnvironment environment) {

        log.info("Resolver is getting product of productUnit: {}", productUnit.getId());
        return productRepository.getReferenceById(productUnit.getProductId());
    }
}
