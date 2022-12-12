package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.StorePropertyFilter;
import org.knowledge4retail.api.store.model.StoreProperty;
import org.knowledge4retail.api.store.repository.StorePropertyRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class StorePropertyQueryResolver implements GraphQLQueryResolver {

    private final StorePropertyRepository storePropertyRepository;

    public List<StoreProperty> getStoreProperties (StorePropertyFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all storeProperties with the desired attributes");
            return storePropertyRepository.findAll();
        }

        log.info("Get all storeProperties with the desired attributes and filtered with: {}", filter);
        Specification<StoreProperty> spec = getSpecification(filter, null);
        return storePropertyRepository.findAll(spec);
    }
}
