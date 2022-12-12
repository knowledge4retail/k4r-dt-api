package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.ShoppingBasketPositionFilter;
import org.knowledge4retail.api.store.model.ShoppingBasketPosition;
import org.knowledge4retail.api.store.repository.ShoppingBasketRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShoppingBasketPositionQueryResolver implements GraphQLQueryResolver {

    private final ShoppingBasketRepository shoppingBasketPositionRepository;

    public List<ShoppingBasketPosition> getShoppingBasketPositions (ShoppingBasketPositionFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all shoppingBasketPositions with the desired attributes");
            return shoppingBasketPositionRepository.findAll();
        }

        log.info("Get all shoppingBasketPositions with the desired attributes and filtered with: {}", filter);
        Specification<ShoppingBasketPosition> spec = getSpecification(filter, null);
        return shoppingBasketPositionRepository.findAll(spec);
    }
}
