package org.knowledge4retail.api.store.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.jetbrains.annotations.NotNull;
import org.knowledge4retail.api.shared.context.dataloader.DataLoaderRegistryFactory;
import org.knowledge4retail.api.shared.filter.FilterField;
import org.knowledge4retail.api.store.filter.ShelfFilter;
import org.knowledge4retail.api.store.model.Shelf;
import org.knowledge4retail.api.store.model.Store;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Component
@RequiredArgsConstructor
public class StoreResolver implements GraphQLResolver<Store> {

    public CompletableFuture<List<Shelf>> getShelves(Store store, ShelfFilter filter, DataFetchingEnvironment environment) {

        log.info("Resolver is getting shelves for store: {}", store.getId());
        filter = createMockFilterIfFilterNotUsed(filter);
        setChildFiltering(store, filter);

        DataLoader<Integer, List<Shelf>> dataLoader = environment.getDataLoader(DataLoaderRegistryFactory.SHELF_DATA_LOADER);
        return dataLoader.load(store.getId(), filter);
    }

    @NotNull
    private ShelfFilter createMockFilterIfFilterNotUsed(ShelfFilter filter) {
        if(filter == null) {

            filter = new ShelfFilter();
        }
        return filter;
    }

    private void setChildFiltering(Store store, ShelfFilter filter) {
        if(filter.getStoreId() == null) {
            FilterField filterField = new FilterField();
            filterField.setValue(store.getId().toString());
            filterField.setType("Int");
            filterField.setOperator("eq");
            filter.setStoreId(filterField);
        }
    }
}
