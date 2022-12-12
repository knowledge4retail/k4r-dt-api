package org.knowledge4retail.api.product.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.ProductFilter;
import org.knowledge4retail.api.product.model.Product;
import org.knowledge4retail.api.product.repository.ProductRepository;
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
public class ProductMutation implements GraphQLMutationResolver {

    private final ProductRepository productRepository;

    public Product updateProduct (Product updateProduct, ProductFilter filter) throws IllegalAccessException{

        log.info("update oder post a single product using graphQL");
        if (filter != null) {

            Specification<Product> spec = getSpecification(filter, null);
            try {

                Product originalProduct = productRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateProduct.setId(originalProduct.getId());
                copyNonNullProperties(updateProduct, originalProduct);
                return productRepository.save(originalProduct);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return productRepository.save(updateProduct);
    }

    public List<Product> postProducts (List<Product> products){

        log.info("create a list of products using graphQL");
        return productRepository.saveAll(products);
    }
}
