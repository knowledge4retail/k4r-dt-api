package org.knowledge4retail.api.device.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.filter.Map2dFilter;
import org.knowledge4retail.api.device.model.Map2d;
import org.knowledge4retail.api.device.repository.Map2dRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class Map2dQueryResolver implements GraphQLQueryResolver {

    private final Map2dRepository map2dRepository;

    public List<Map2d> getMaps2d (Map2dFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all maps2d with the desired attributes");
            return map2dRepository.findAll();
        }

        log.info("Get all maps2d with the desired attributes and filtered with: {}", filter);
        Specification<Map2d> spec = getSpecification(filter, null);
        return map2dRepository.findAll(spec);
    }
}
