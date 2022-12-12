package org.knowledge4retail.api.store.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.model.ProductGtin;
import org.knowledge4retail.api.product.model.ProductUnit;
import org.knowledge4retail.api.product.repository.ProductGtinRepository;
import org.knowledge4retail.api.product.repository.ProductUnitRepository;
import org.knowledge4retail.api.store.model.DeliveredUnit;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveredunitResolver implements GraphQLResolver<DeliveredUnit> {

    private final ProductGtinRepository productGtinRepository;
    private final ProductUnitRepository productUnitRepository;

    public ProductGtin getProductGtin(DeliveredUnit deliveredUnit, DataFetchingEnvironment environment) {

        log.info("Resolver is getting productGtin of deliveredUnit: {}", deliveredUnit.getId());
        return productGtinRepository.getReferenceById(deliveredUnit.getProductGtinId());
    }

    public ProductUnit getProductUnit(DeliveredUnit deliveredUnit, DataFetchingEnvironment environment) {

        log.info("Resolver is getting productUnit of deliveredUnit: {}", deliveredUnit.getId());
        return productUnitRepository.getReferenceById(deliveredUnit.getProductUnitId());
    }
}
