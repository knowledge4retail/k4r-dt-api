package org.knowledge4retail.api.product.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.ProductGroupFilter;
import org.knowledge4retail.api.product.model.ProductGroup;
import org.knowledge4retail.api.product.repository.ProductGroupRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductGroupQueryResolver implements GraphQLQueryResolver {

    private final ProductGroupRepository productGroupRepository;

    public List<ProductGroup> getProductGroups (ProductGroupFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all productGroups with the desired attributes");
            return productGroupRepository.findAll();
        }

        log.info("Get all productGroups with the desired attributes and filtered with: {}", filter);
        Specification<ProductGroup> spec = getSpecification(filter, null);
        return productGroupRepository.findAll(spec);
    }
}
