package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.TrolleyRouteFilter;
import org.knowledge4retail.api.store.model.TrolleyRoute;
import org.knowledge4retail.api.store.repository.TrolleyRouteRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrolleyRouteQueryResolver implements GraphQLQueryResolver {

    private final TrolleyRouteRepository trolleyRouteRepository;

    public List<TrolleyRoute> getTrolleyRoutes (TrolleyRouteFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all trolley routes with the desired attributes");
            return trolleyRouteRepository.findAll();
        }

        log.info("Get all trolley routes with the desired attributes and filtered with: {}", filter);
        Specification<TrolleyRoute> spec = getSpecification(filter, null);
        return trolleyRouteRepository.findAll(spec);
    }
}
