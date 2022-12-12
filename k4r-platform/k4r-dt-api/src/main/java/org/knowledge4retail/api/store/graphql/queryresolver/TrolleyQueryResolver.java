package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.TrolleyFilter;
import org.knowledge4retail.api.store.model.Trolley;
import org.knowledge4retail.api.store.repository.TrolleyRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrolleyQueryResolver implements GraphQLQueryResolver {

    private final TrolleyRepository trolleyRepository;

    public List<Trolley> getTrolleys (TrolleyFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all trolleys with the desired attributes");
            return trolleyRepository.findAll();
        }

        log.info("Get all trolleys with the desired attributes and filtered with: {}", filter);
        Specification<Trolley> spec = getSpecification(filter, null);
        return trolleyRepository.findAll(spec);
    }
}
