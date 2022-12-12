package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.ShoppingBasketPositionFilter;
import org.knowledge4retail.api.store.model.ShoppingBasketPosition;
import org.knowledge4retail.api.store.repository.ShoppingBasketRepository;
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
public class ShoppingBasketPositionMutation implements GraphQLMutationResolver {

    private final ShoppingBasketRepository shoppingBasketPositionRepository;

    public ShoppingBasketPosition updateShoppingBasketPosition (ShoppingBasketPosition updateShoppingBasketPosition, ShoppingBasketPositionFilter filter) throws IllegalAccessException{

        log.info("update oder post a single shoppingBasketPosition using graphQL");
        if (filter != null) {

            Specification<ShoppingBasketPosition> spec = getSpecification(filter, null);
            try {

                ShoppingBasketPosition originalShoppingBasketPosition = shoppingBasketPositionRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateShoppingBasketPosition.setId(originalShoppingBasketPosition.getId());
                copyNonNullProperties(updateShoppingBasketPosition, originalShoppingBasketPosition);
                return shoppingBasketPositionRepository.save(originalShoppingBasketPosition);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return shoppingBasketPositionRepository.save(updateShoppingBasketPosition);
    }

    public List<ShoppingBasketPosition> postShoppingBasketPositions (List<ShoppingBasketPosition> shoppingBasketPositions){

        log.info("create a list of shoppingBasketPositions using graphQL");
        return shoppingBasketPositionRepository.saveAll(shoppingBasketPositions);
    }
}
