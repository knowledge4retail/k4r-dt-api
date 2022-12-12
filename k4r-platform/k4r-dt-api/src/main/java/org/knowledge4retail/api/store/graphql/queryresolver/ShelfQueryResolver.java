package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.ShelfFilter;
import org.knowledge4retail.api.store.model.Shelf;
import org.knowledge4retail.api.store.repository.ShelfRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShelfQueryResolver implements GraphQLQueryResolver {

    private final ShelfRepository shelfRepository;

    public Iterable<Shelf> getShelves (ShelfFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all shelves with the desired attributes ");
            return shelfRepository.findAll();
        }

        log.info("Get all shelves with the desired attributes and filtered with: {}", filter);
        Specification<Shelf> spec = getSpecification(filter, null);
        return shelfRepository.findAll(spec);
    }
}
