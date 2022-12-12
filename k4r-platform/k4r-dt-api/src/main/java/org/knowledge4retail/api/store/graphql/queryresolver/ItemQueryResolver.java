package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.ItemFilter;
import org.knowledge4retail.api.store.model.Item;
import org.knowledge4retail.api.store.repository.ItemRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemQueryResolver implements GraphQLQueryResolver {

    private final ItemRepository itemRepository;

    public List<Item> getItems (ItemFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all items with the desired attributes");
            return itemRepository.findAll();
        }

        log.info("Get all items with the desired attributes and filtered with: {}", filter);
        Specification<Item> spec = getSpecification(filter, null);
        return itemRepository.findAll(spec);
    }
}
