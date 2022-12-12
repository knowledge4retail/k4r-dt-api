package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.StoreFilter;
import org.knowledge4retail.api.store.model.Store;
import org.knowledge4retail.api.store.repository.StoreRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

import static org.knowledge4retail.api.shared.util.SearchUtil.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreMutation implements GraphQLMutationResolver {

    private final StoreRepository storeRepository;

    public Store updateStore (Store updateStore, StoreFilter filter) throws IllegalAccessException{

        log.info("update oder post a single store using graphQL");
        if (filter != null) {

            Specification<Store> spec = getSpecification(filter, null);
            try {

                Store originalStore = storeRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateStore.setId(originalStore.getId());
                copyNonNullProperties(updateStore, originalStore);
                return storeRepository.save(originalStore);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return storeRepository.save(updateStore);
    }

    public List<Store> postStores (List<Store> stores){

        log.info("create a list of stores using graphQL");
        return storeRepository.saveAll(stores);
    }
}
