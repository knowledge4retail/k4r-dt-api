package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.StorePropertyFilter;
import org.knowledge4retail.api.store.model.StoreProperty;
import org.knowledge4retail.api.store.repository.StorePropertyRepository;
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
public class StorePropertyMutation implements GraphQLMutationResolver {

    private final StorePropertyRepository storePropertyRepository;

    public StoreProperty updateStoreProperty (StoreProperty updateStoreProperty, StorePropertyFilter filter) throws IllegalAccessException{

        log.info("update oder post a single storeProperty using graphQL");
        if (filter != null) {

            Specification<StoreProperty> spec = getSpecification(filter, null);
            try {

                StoreProperty originalStoreProperty = storePropertyRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateStoreProperty.setId(originalStoreProperty.getId());
                copyNonNullProperties(updateStoreProperty, originalStoreProperty);
                return storePropertyRepository.save(originalStoreProperty);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return storePropertyRepository.save(updateStoreProperty);
    }

    public List<StoreProperty> postStoreProperties (List<StoreProperty> storeProperties){

        log.info("create a list of storeProperties using graphQL");
        return storePropertyRepository.saveAll(storeProperties);
    }
}
