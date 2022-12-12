package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.StoreObjectFilter;
import org.knowledge4retail.api.store.model.StoreObject;
import org.knowledge4retail.api.store.repository.StoreObjectRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreObjectQueryResolver implements GraphQLQueryResolver {

    private final StoreObjectRepository storeObjectRepository;

    public List<StoreObject> getStoreObjects (StoreObjectFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all storeObjects with the desired attributes");
            return storeObjectRepository.findAll();
        }

        log.info("Get all storeObjects with the desired attributes and filtered with: {}", filter);
        Specification<StoreObject> spec = getSpecification(filter, null);
        return storeObjectRepository.findAll(spec);
    }
}
