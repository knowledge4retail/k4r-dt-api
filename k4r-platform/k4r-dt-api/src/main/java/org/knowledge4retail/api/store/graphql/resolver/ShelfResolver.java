package org.knowledge4retail.api.store.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.jetbrains.annotations.NotNull;
import org.knowledge4retail.api.product.model.ProductGroup;
import org.knowledge4retail.api.product.repository.ProductGroupRepository;
import org.knowledge4retail.api.shared.context.dataloader.DataLoaderRegistryFactory;
import org.knowledge4retail.api.shared.filter.FilterField;
import org.knowledge4retail.api.store.filter.ShelfLayerFilter;
import org.knowledge4retail.api.store.model.Shelf;
import org.knowledge4retail.api.store.model.ShelfLayer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Component
@RequiredArgsConstructor
public class ShelfResolver implements GraphQLResolver<Shelf> {

    private final ProductGroupRepository productGroupRepository;

    public CompletableFuture<List<ShelfLayer>> getShelfLayers(Shelf shelf, ShelfLayerFilter filter, DataFetchingEnvironment environment) {

        log.info("Resolver is getting shelfLayers for shelf: {}", shelf.getId());
        filter = createMockFilterIfFilterNotUsed(filter);
        setChildFiltering(shelf, filter);

        DataLoader<Integer, List<ShelfLayer>> dataLoader = environment.getDataLoader(DataLoaderRegistryFactory.SHELFLAYER_DATA_LOADER);
        return dataLoader.load(shelf.getId(), filter);
    }

    @NotNull
    private ShelfLayerFilter createMockFilterIfFilterNotUsed(ShelfLayerFilter filter) {
        if(filter == null) {

            filter = new ShelfLayerFilter();
        }
        return filter;
    }

    private void setChildFiltering(Shelf shelf, ShelfLayerFilter filter) {
        if(filter.getShelfId() == null) {
            FilterField filterField = new FilterField();
            filterField.setValue(shelf.getId().toString());
            filterField.setType("Int");
            filterField.setOperator("eq");
            filter.setShelfId(filterField);
        }
    }

    public ProductGroup getProductGroup(Shelf shelf, DataFetchingEnvironment environment) {

        log.info("Resolver is getting productGroup of shelf: {}", shelf.getId());
        return productGroupRepository.getReferenceById(shelf.getProductGroupId());
    }
}
