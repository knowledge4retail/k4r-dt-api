package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.StoreCharacteristicFilter;
import org.knowledge4retail.api.store.model.StoreCharacteristic;
import org.knowledge4retail.api.store.repository.StoreCharacteristicRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

import static org.knowledge4retail.api.shared.util.SearchUtil.copyNonNullProperties;
import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreCharacteristicMutation implements GraphQLMutationResolver {

    private final StoreCharacteristicRepository storeCharacteristicRepository;

    public StoreCharacteristic updateStoreCharacteristic (StoreCharacteristic updateStoreCharacteristic, StoreCharacteristicFilter filter) throws IllegalAccessException{

        log.info("update oder post a single storeCharacteristic using graphQL");
        if (filter != null) {

            Specification<StoreCharacteristic> spec = getSpecification(filter, null);
            try {

                StoreCharacteristic originalStoreCharacteristic = storeCharacteristicRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateStoreCharacteristic.setId(originalStoreCharacteristic.getId());
                copyNonNullProperties(updateStoreCharacteristic, originalStoreCharacteristic);
                return storeCharacteristicRepository.save(originalStoreCharacteristic);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return storeCharacteristicRepository.save(updateStoreCharacteristic);
    }

    public List<StoreCharacteristic> postStoreCharacteristics (List<StoreCharacteristic> storeCharacteristics){

        log.info("create a list of storeCharacteristics using graphQL");
        return storeCharacteristicRepository.saveAll(storeCharacteristics);
    }
}
