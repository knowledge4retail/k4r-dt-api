package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.ItemFilter;
import org.knowledge4retail.api.store.model.Item;
import org.knowledge4retail.api.store.repository.ItemRepository;
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
public class ItemMutation implements GraphQLMutationResolver {

    private final ItemRepository itemRepository;

    public Item updateItem (Item updateItem, ItemFilter filter) throws IllegalAccessException{

        log.info("update oder post a single item using graphQL");
        if (filter != null) {

            Specification<Item> spec = getSpecification(filter, null);
            try {

                Item originalItem = itemRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateItem.setId(originalItem.getId());
                copyNonNullProperties(updateItem, originalItem);
                return itemRepository.save(originalItem);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return itemRepository.save(updateItem);
    }

    public List<Item> postItems (List<Item> items){

        log.info("create a list of items using graphQL");
        return itemRepository.saveAll(items);
    }
}
