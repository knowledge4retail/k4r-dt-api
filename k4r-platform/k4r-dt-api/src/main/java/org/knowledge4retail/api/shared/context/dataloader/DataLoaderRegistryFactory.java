package org.knowledge4retail.api.shared.context.dataloader;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
import org.dataloader.DataLoaderRegistry;
import org.knowledge4retail.api.product.model.ProductDescription;
import org.knowledge4retail.api.product.service.ProductDescriptionService;
import org.knowledge4retail.api.store.model.*;
import org.knowledge4retail.api.store.service.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoaderRegistryFactory {

    public static final String SHELF_DATA_LOADER = "SHELF_DATA_LOADER";
    public static final String SHELFLAYER_DATA_LOADER = "SHELFLAYER_DATA_LOADER";
    public static final String FACING_DATA_LOADER = "FACING_DATA_LOADER";
    public static final String BARCODE_DATA_LOADER = "BARCODE_DATA_LOADER";
    public static final String PRODUCT_DESCRIPTION_DATA_LOADER = "PRODUCT_DESCRIPTION_DATA_LOADER";

    private final ShelfService shelfService;
    private final ShelfLayerService shelfLayerService;
    private final FacingService facingService;
    private final BarcodeService barcodeService;
    private final ProductDescriptionService productDescriptionService;

    private static final Executor threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public DataLoaderRegistry create() {

        var registry = new DataLoaderRegistry();

        registry.register(SHELF_DATA_LOADER, createShelfDataLoader());
        registry.register(SHELFLAYER_DATA_LOADER, createShelfLayerDataLoader());
        registry.register(FACING_DATA_LOADER, createFacingDataLoader());
        registry.register(BARCODE_DATA_LOADER, createBarcodeDataLoader());
        registry.register(PRODUCT_DESCRIPTION_DATA_LOADER, createProductDescriptionDataLoader());

        return registry;
    }

    private DataLoader<Integer, List<Shelf>> createShelfDataLoader() {

        log.info("dataLoaderFactory_Shelf");

        return DataLoaderFactory.newMappedDataLoader ((Set<Integer> Ids, BatchLoaderEnvironment environment) ->
                CompletableFuture.supplyAsync(() ->
                        shelfService.filterShelf( environment.getKeyContexts() ),  threadPool));
    }

    private DataLoader<Integer, List<ShelfLayer>> createShelfLayerDataLoader() {

        log.info("dataLoaderFactory_ShelfLayer");

        return DataLoaderFactory.newMappedDataLoader ((Set<Integer> Ids, BatchLoaderEnvironment environment) ->
                CompletableFuture.supplyAsync(() ->
                        shelfLayerService.filterShelfLayer( environment.getKeyContexts() ),  threadPool));
    }

    private DataLoader<Integer, List<Facing>> createFacingDataLoader() {

        log.info("dataLoaderFactory_Facing");

        return DataLoaderFactory.newMappedDataLoader ((Set<Integer> Ids, BatchLoaderEnvironment environment) ->
                CompletableFuture.supplyAsync(() ->
                        facingService.filterFacing( environment.getKeyContexts() ),  threadPool));
    }

    private DataLoader<Integer, List<Barcode>> createBarcodeDataLoader() {

        log.info("dataLoaderFactory_Barcode");

        return DataLoaderFactory.newMappedDataLoader ((Set<Integer> Ids, BatchLoaderEnvironment environment) ->
                CompletableFuture.supplyAsync(() ->
                        barcodeService.filterBarcode( environment.getKeyContexts() ),  threadPool));
    }

    private DataLoader<String, List<ProductDescription>> createProductDescriptionDataLoader() {

        log.info("dataLoaderFactory_ProductDescription");

        return DataLoaderFactory.newMappedDataLoader ((Set<String> Ids, BatchLoaderEnvironment environment) ->
                CompletableFuture.supplyAsync(() ->
                        productDescriptionService.filterProductDescription( environment.getKeyContexts() ),  threadPool));
    }
}
