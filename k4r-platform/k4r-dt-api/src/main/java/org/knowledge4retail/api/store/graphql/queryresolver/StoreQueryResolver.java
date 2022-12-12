package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.StoreFilter;
import org.knowledge4retail.api.store.model.Store;
import org.knowledge4retail.api.store.repository.StoreRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreQueryResolver implements GraphQLQueryResolver {

    private final StoreRepository storeRepository;

    public List<Store> getStores (StoreFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all stores with the desired attributes");
            return storeRepository.findAll();
        }

        log.info("Get all stores with the desired attributes and filtered with: {}", filter);
        Specification<Store> spec = getSpecification(filter, null);
        return storeRepository.findAll(spec);
    }
}
