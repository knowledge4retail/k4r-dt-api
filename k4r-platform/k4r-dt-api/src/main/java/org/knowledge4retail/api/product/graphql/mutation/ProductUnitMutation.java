package org.knowledge4retail.api.product.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.ProductUnitFilter;
import org.knowledge4retail.api.product.model.ProductUnit;
import org.knowledge4retail.api.product.repository.ProductUnitRepository;
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
public class ProductUnitMutation implements GraphQLMutationResolver {

    private final ProductUnitRepository productUnitRepository;

    public ProductUnit updateProductUnit (ProductUnit updateProductUnit, ProductUnitFilter filter) throws IllegalAccessException{

        log.info("update oder post a single productUnit using graphQL");
        if (filter != null) {

            Specification<ProductUnit> spec = getSpecification(filter, null);
            try {

                ProductUnit originalProductUnit = productUnitRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateProductUnit.setId(originalProductUnit.getId());
                copyNonNullProperties(updateProductUnit, originalProductUnit);
                return productUnitRepository.save(originalProductUnit);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return productUnitRepository.save(updateProductUnit);
    }

    public List<ProductUnit> postProductUnits (List<ProductUnit> productUnits){

        log.info("create a list of productUnits using graphQL");
        return productUnitRepository.saveAll(productUnits);
    }
}
