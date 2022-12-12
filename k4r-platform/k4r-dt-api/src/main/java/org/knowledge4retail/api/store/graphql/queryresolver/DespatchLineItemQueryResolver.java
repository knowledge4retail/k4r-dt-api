package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.DespatchLineItemFilter;
import org.knowledge4retail.api.store.model.DespatchLineItem;
import org.knowledge4retail.api.store.repository.DespatchLineItemRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class DespatchLineItemQueryResolver implements GraphQLQueryResolver {

    private final DespatchLineItemRepository despatchLineItemRepository;

    public List<DespatchLineItem> getDespatchLineItems (DespatchLineItemFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all despatchLineItems with the desired attributes");
            return despatchLineItemRepository.findAll();
        }

        log.info("Get all despatchLineItems with the desired attributes and filtered with: {}", filter);
        Specification<DespatchLineItem> spec = getSpecification(filter, null);
        return despatchLineItemRepository.findAll(spec);
    }
}
