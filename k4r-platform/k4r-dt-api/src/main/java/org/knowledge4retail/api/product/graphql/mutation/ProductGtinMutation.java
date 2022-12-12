package org.knowledge4retail.api.product.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.ProductGtinFilter;
import org.knowledge4retail.api.product.model.ProductGtin;
import org.knowledge4retail.api.product.repository.ProductGtinRepository;
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
public class ProductGtinMutation implements GraphQLMutationResolver {

    private final ProductGtinRepository productGtinRepository;

    public ProductGtin updateProductGtin (ProductGtin updateProductGtin, ProductGtinFilter filter) throws IllegalAccessException{

        log.info("update oder post a single productGtin using graphQL");
        if (filter != null) {

            Specification<ProductGtin> spec = getSpecification(filter, null);
            try {

                ProductGtin originalProductGtin = productGtinRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateProductGtin.setId(originalProductGtin.getId());
                copyNonNullProperties(updateProductGtin, originalProductGtin);
                return productGtinRepository.save(originalProductGtin);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return productGtinRepository.save(updateProductGtin);
    }

    public List<ProductGtin> postProductGtins (List<ProductGtin> productGtins){

        log.info("create a list of productGtins using graphQL");
        return productGtinRepository.saveAll(productGtins);
    }
}
