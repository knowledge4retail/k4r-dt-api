package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.DespatchLineItemFilter;
import org.knowledge4retail.api.store.model.DespatchLineItem;
import org.knowledge4retail.api.store.repository.DespatchLineItemRepository;
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
public class DespatchLineItemMutation implements GraphQLMutationResolver {

    private final DespatchLineItemRepository despatchLineItemRepository;

    public DespatchLineItem updateDespatchLineItem (DespatchLineItem updateDespatchLineItem, DespatchLineItemFilter filter) throws IllegalAccessException{

        log.info("update oder post a single despatchLineItem using graphQL");
        if (filter != null) {

            Specification<DespatchLineItem> spec = getSpecification(filter, null);
            try {

                DespatchLineItem originalDespatchLineItem = despatchLineItemRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateDespatchLineItem.setId(originalDespatchLineItem.getId());
                copyNonNullProperties(updateDespatchLineItem, originalDespatchLineItem);
                return despatchLineItemRepository.save(originalDespatchLineItem);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return despatchLineItemRepository.save(updateDespatchLineItem);
    }

    public List<DespatchLineItem> postDespatchLineItems (List<DespatchLineItem> despatchLineItems){

        log.info("create a list of despatchLineItems using graphQL");
        return despatchLineItemRepository.saveAll(despatchLineItems);
    }
}
