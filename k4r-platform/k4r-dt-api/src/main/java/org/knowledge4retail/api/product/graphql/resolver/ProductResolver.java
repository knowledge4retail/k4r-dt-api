package org.knowledge4retail.api.product.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.jetbrains.annotations.NotNull;
import org.knowledge4retail.api.product.filter.ProductDescriptionFilter;
import org.knowledge4retail.api.product.model.Product;
import org.knowledge4retail.api.product.model.ProductDescription;
import org.knowledge4retail.api.shared.context.dataloader.DataLoaderRegistryFactory;
import org.knowledge4retail.api.shared.filter.FilterField;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Component
@RequiredArgsConstructor
public class ProductResolver implements GraphQLResolver<Product> {

    public CompletableFuture<List<ProductDescription>> getProductDescriptions(Product product, ProductDescriptionFilter filter, DataFetchingEnvironment environment) {

        log.info("Resolver is getting productDescriptions for product: {}", product.getId());
        filter = createMockFilterIfFilterNotUsed(filter);
        setChildFiltering(product, filter);

        DataLoader<String, List<ProductDescription>> dataLoader = environment.getDataLoader(DataLoaderRegistryFactory.PRODUCT_DESCRIPTION_DATA_LOADER);
        return dataLoader.load(product.getId(), filter);
    }

    @NotNull
    private ProductDescriptionFilter createMockFilterIfFilterNotUsed(ProductDescriptionFilter filter) {
        if(filter == null) {

            filter = new ProductDescriptionFilter();
        }
        return filter;
    }

    private void setChildFiltering(Product product, ProductDescriptionFilter filter) {
        if(filter.getProductId() == null) {
            FilterField filterField = new FilterField();
            filterField.setValue(product.getId());
            filterField.setType("String");
            filterField.setOperator("eq");
            filter.setProductId(filterField);
        }
    }
}
