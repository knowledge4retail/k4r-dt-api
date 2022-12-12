package org.knowledge4retail.api.product.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.ProductGroupFilter;
import org.knowledge4retail.api.product.model.ProductGroup;
import org.knowledge4retail.api.product.repository.ProductGroupRepository;
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
public class ProductGroupMutation implements GraphQLMutationResolver {

    private final ProductGroupRepository productGroupRepository;

    public ProductGroup updateProductGroup (ProductGroup updateProductGroup, ProductGroupFilter filter) throws IllegalAccessException{

        log.info("update oder post a single productGroup using graphQL");
        if (filter != null) {

            Specification<ProductGroup> spec = getSpecification(filter, null);
            try {

                ProductGroup originalProductGroup = productGroupRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateProductGroup.setId(originalProductGroup.getId());
                copyNonNullProperties(updateProductGroup, originalProductGroup);
                return productGroupRepository.save(originalProductGroup);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return productGroupRepository.save(updateProductGroup);
    }

    public List<ProductGroup> postProductGroups (List<ProductGroup> productGroups){

        log.info("create a list of productGroupes using graphQL");
        return productGroupRepository.saveAll(productGroups);
    }
}
