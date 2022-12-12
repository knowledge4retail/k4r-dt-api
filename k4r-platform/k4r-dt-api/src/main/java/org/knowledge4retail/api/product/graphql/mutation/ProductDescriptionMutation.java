package org.knowledge4retail.api.product.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.ProductDescriptionFilter;
import org.knowledge4retail.api.product.model.ProductDescription;
import org.knowledge4retail.api.product.repository.ProductDescriptionRepository;
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
public class ProductDescriptionMutation implements GraphQLMutationResolver {

    private final ProductDescriptionRepository productDescriptionRepository;

    public ProductDescription updateProductDescription (ProductDescription updateProductDescription, ProductDescriptionFilter filter) throws IllegalAccessException{

        log.info("update oder post a single productDescription using graphQL");
        if (filter != null) {

            Specification<ProductDescription> spec = getSpecification(filter, null);
            try {

                ProductDescription originalProductDescription = productDescriptionRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateProductDescription.setId(originalProductDescription.getId());
                copyNonNullProperties(updateProductDescription, originalProductDescription);
                return productDescriptionRepository.save(originalProductDescription);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return productDescriptionRepository.save(updateProductDescription);
    }

    public List<ProductDescription> postProductDescriptions (List<ProductDescription> productDescriptions){

        log.info("create a list of productDescriptions using graphQL");
        return productDescriptionRepository.saveAll(productDescriptions);
    }
}
