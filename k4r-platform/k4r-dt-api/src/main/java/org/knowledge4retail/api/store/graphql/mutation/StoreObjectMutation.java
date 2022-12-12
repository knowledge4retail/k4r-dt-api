package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.StoreObjectFilter;
import org.knowledge4retail.api.store.model.StoreObject;
import org.knowledge4retail.api.store.repository.StoreObjectRepository;
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
public class StoreObjectMutation implements GraphQLMutationResolver {

    private final StoreObjectRepository storeObjectRepository;

    public StoreObject updateStoreObject (StoreObject updateStoreObject, StoreObjectFilter filter) throws IllegalAccessException{

        log.info("update oder post a single storeObject using graphQL");
        if (filter != null) {

            Specification<StoreObject> spec = getSpecification(filter, null);
            try {

                StoreObject originalStoreObject = storeObjectRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateStoreObject.setId(originalStoreObject.getId());
                copyNonNullProperties(updateStoreObject, originalStoreObject);
                return storeObjectRepository.save(originalStoreObject);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return storeObjectRepository.save(updateStoreObject);
    }

    public List<StoreObject> postStoreObjects (List<StoreObject> storeObjects){

        log.info("create a list of storeObjects using graphQL");
        return storeObjectRepository.saveAll(storeObjects);
    }
}
