package org.knowledge4retail.api.store.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.jetbrains.annotations.NotNull;
import org.knowledge4retail.api.shared.context.dataloader.DataLoaderRegistryFactory;
import org.knowledge4retail.api.shared.filter.FilterField;
import org.knowledge4retail.api.store.filter.BarcodeFilter;
import org.knowledge4retail.api.store.filter.FacingFilter;
import org.knowledge4retail.api.store.model.Barcode;
import org.knowledge4retail.api.store.model.Facing;
import org.knowledge4retail.api.store.model.Shelf;
import org.knowledge4retail.api.store.model.ShelfLayer;
import org.knowledge4retail.api.store.repository.ShelfRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Component
@RequiredArgsConstructor
public class ShelfLayerResolver implements GraphQLResolver<ShelfLayer> {

    private final ShelfRepository shelfRepository;

    public CompletableFuture<List<Facing>> getFacings(ShelfLayer shelfLayer, FacingFilter filter, DataFetchingEnvironment environment) {

        log.info("Resolver is getting facings for shelfLayer: {}", shelfLayer.getId());
        filter = createMockFilterIfFilterNotUsed(filter);
        setChildFiltering(shelfLayer, filter);

        DataLoader<Integer, List<Facing>> dataLoader = environment.getDataLoader(DataLoaderRegistryFactory.FACING_DATA_LOADER);
        return dataLoader.load(shelfLayer.getId(), filter);
    }

    @NotNull
    private FacingFilter createMockFilterIfFilterNotUsed(FacingFilter filter) {
        if(filter == null) {

            filter = new FacingFilter();
        }
        return filter;
    }

    private void setChildFiltering(ShelfLayer shelfLayer, FacingFilter filter) {

        if(filter.getShelfLayerId() == null) {
            FilterField filterField = new FilterField();
            filterField.setValue(shelfLayer.getId().toString());
            filterField.setType("Int");
            filterField.setOperator("eq");
            filter.setShelfLayerId(filterField);
        }
    }

    public CompletableFuture<List<Barcode>> getBarcodes(ShelfLayer shelfLayer, BarcodeFilter filter, DataFetchingEnvironment environment) {

        log.info("Resolver is getting facings for shelfLayer: {}", shelfLayer.getId());
        filter = createMockFilterIfFilterNotUsed(filter);
        setChildFiltering(shelfLayer, filter);

        DataLoader<Integer, List<Barcode>> dataLoader = environment.getDataLoader(DataLoaderRegistryFactory.BARCODE_DATA_LOADER);
        return dataLoader.load(shelfLayer.getId(), filter);
    }

    @NotNull
    private BarcodeFilter createMockFilterIfFilterNotUsed(BarcodeFilter filter) {
        if(filter == null) {

            filter = new BarcodeFilter();
        }
        return filter;
    }

    private void setChildFiltering(ShelfLayer shelfLayer, BarcodeFilter filter) {

        if(filter.getShelfLayerId() == null) {
            FilterField filterField = new FilterField();
            filterField.setValue(shelfLayer.getId().toString());
            filterField.setType("Int");
            filterField.setOperator("eq");
            filter.setShelfLayerId(filterField);
        }
    }

    public Shelf getShelf(ShelfLayer shelfLayer, DataFetchingEnvironment environment) {

        log.info("Resolver is getting shelf of shelfLayer: {}", shelfLayer.getId());
        return shelfRepository.getReferenceById(shelfLayer.getShelfId());
    }
}
