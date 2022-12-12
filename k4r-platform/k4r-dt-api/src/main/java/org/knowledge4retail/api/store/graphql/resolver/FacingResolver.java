package org.knowledge4retail.api.store.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.model.Facing;
import org.knowledge4retail.api.store.model.ShelfLayer;
import org.knowledge4retail.api.store.repository.ShelfLayerRepository;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class FacingResolver implements GraphQLResolver<Facing> {

    private final ShelfLayerRepository shelfLayerRepository;

    public ShelfLayer getShelfLayer(Facing facing, DataFetchingEnvironment environment) {

        log.info("Resolver is getting shelfLayer of facing: {}", facing.getId());
        return shelfLayerRepository.getReferenceById(facing.getShelfLayerId());
    }
}
