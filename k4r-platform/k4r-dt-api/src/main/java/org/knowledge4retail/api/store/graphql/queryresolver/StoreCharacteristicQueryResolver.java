package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.StoreCharacteristicFilter;
import org.knowledge4retail.api.store.model.StoreCharacteristic;
import org.knowledge4retail.api.store.repository.StoreCharacteristicRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreCharacteristicQueryResolver implements GraphQLQueryResolver {

    private final StoreCharacteristicRepository storeCharacteristicRepository;

    public List<StoreCharacteristic> getStoreCharacteristics (StoreCharacteristicFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all storeCharacteristics with the desired attributes");
            return storeCharacteristicRepository.findAll();
        }

        log.info("Get all storeCharacteristics with the desired attributes and filtered with: {}", filter);
        Specification<StoreCharacteristic> spec = getSpecification(filter, null);
        return storeCharacteristicRepository.findAll(spec);
    }
}
