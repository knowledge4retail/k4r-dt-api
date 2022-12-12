package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.ShelfFilter;
import org.knowledge4retail.api.store.model.Shelf;
import org.knowledge4retail.api.store.repository.ShelfRepository;
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
public class ShelfMutation implements GraphQLMutationResolver {

    private final ShelfRepository shelfRepository;

    public Shelf updateShelf (Shelf updateShelf, ShelfFilter filter) throws IllegalAccessException{

        log.info("update oder post a single shelf using graphQL");
        if (filter != null) {

            Specification<Shelf> spec = getSpecification(filter, null);
            try {

                Shelf originalShelf = shelfRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateShelf.setId(originalShelf.getId());
                copyNonNullProperties(updateShelf, originalShelf);
                return shelfRepository.save(originalShelf);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return shelfRepository.save(updateShelf);
    }

    public List<Shelf> postShelves (List<Shelf> shelves){

        log.info("create a list of shelves using graphQL");
        return shelfRepository.saveAll(shelves);
    }
}
