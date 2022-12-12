package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.ShelfLayerFilter;
import org.knowledge4retail.api.store.model.ShelfLayer;
import org.knowledge4retail.api.store.repository.ShelfLayerRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShelfLayerQueryResolver implements GraphQLQueryResolver {

    private final ShelfLayerRepository shelfLayerRepository;

    public Iterable<ShelfLayer> getShelfLayers (ShelfLayerFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all shelf layers with the desired attributes ");
            return shelfLayerRepository.findAll();
        }

        log.info("Get all shelfLayers with the desired attributes and filtered with: {}", filter);
        Specification<ShelfLayer> spec = getSpecification(filter, null);
        return shelfLayerRepository.findAll(spec);
    }
}
