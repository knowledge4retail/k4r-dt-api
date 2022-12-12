package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.ShelfLayerFilter;
import org.knowledge4retail.api.store.model.ShelfLayer;
import org.knowledge4retail.api.store.repository.ShelfLayerRepository;
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
public class ShelfLayerMutation implements GraphQLMutationResolver {

    private final ShelfLayerRepository shelfLayerRepository;

    public ShelfLayer updateShelfLayer (ShelfLayer updateShelfLayer, ShelfLayerFilter filter) throws IllegalAccessException{

        log.info("update oder post a single shelfLayer using graphQL");
        if (filter != null) {

            Specification<ShelfLayer> spec = getSpecification(filter, null);
            try {

                ShelfLayer originalShelfLayer = shelfLayerRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateShelfLayer.setId(originalShelfLayer.getId());
                copyNonNullProperties(updateShelfLayer, originalShelfLayer);
                return shelfLayerRepository.save(originalShelfLayer);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return shelfLayerRepository.save(updateShelfLayer);
    }

    public List<ShelfLayer> postShelfLayers (List<ShelfLayer> shelfLayers){

        log.info("create a list of shelfLayers using graphQL");
        return shelfLayerRepository.saveAll(shelfLayers);
    }
}
