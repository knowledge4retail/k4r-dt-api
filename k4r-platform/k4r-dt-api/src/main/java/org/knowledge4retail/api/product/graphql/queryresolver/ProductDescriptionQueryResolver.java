package org.knowledge4retail.api.product.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.ProductDescriptionFilter;
import org.knowledge4retail.api.product.model.ProductDescription;
import org.knowledge4retail.api.product.repository.ProductDescriptionRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDescriptionQueryResolver implements GraphQLQueryResolver {

    private final ProductDescriptionRepository productDescriptionRepository;

    public List<ProductDescription> getProductDescriptions (ProductDescriptionFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all productDescriptions with the desired attributes");
            return productDescriptionRepository.findAll();
        }

        log.info("Get all productDescriptions with the desired attributes and filtered with: {}", filter);
        Specification<ProductDescription> spec = getSpecification(filter, null);
        return productDescriptionRepository.findAll(spec);
    }
}
