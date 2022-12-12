package org.knowledge4retail.api.product.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.ProductPropertyFilter;
import org.knowledge4retail.api.product.model.ProductProperty;
import org.knowledge4retail.api.product.repository.ProductPropertyRepository;
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
public class ProductPropertyMutation implements GraphQLMutationResolver {

    private final ProductPropertyRepository productPropertyRepository;

    public ProductProperty updateProductProperty (ProductProperty updateProductProperty, ProductPropertyFilter filter) throws IllegalAccessException{

        log.info("update oder post a single productProperty using graphQL");
        if (filter != null) {

            Specification<ProductProperty> spec = getSpecification(filter, null);
            try {

                ProductProperty originalProductProperty = productPropertyRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateProductProperty.setId(originalProductProperty.getId());
                copyNonNullProperties(updateProductProperty, originalProductProperty);
                return productPropertyRepository.save(originalProductProperty);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return productPropertyRepository.save(updateProductProperty);
    }

    public List<ProductProperty> postProductProperties (List<ProductProperty> productProperties){

        log.info("create a list of productProperties using graphQL");
        return productPropertyRepository.saveAll(productProperties);
    }
}
