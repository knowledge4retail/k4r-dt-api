package org.knowledge4retail.api.product.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.UnitFilter;
import org.knowledge4retail.api.product.model.Unit;
import org.knowledge4retail.api.product.repository.UnitRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnitQueryResolver implements GraphQLQueryResolver {

    private final UnitRepository unitRepository;

    public List<Unit> getUnits (UnitFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all units with the desired attributes");
            return unitRepository.findAll();
        }

        log.info("Get all units with the desired attributes and filtered with: {}", filter);
        Specification<Unit> spec = getSpecification(filter, null);
        return unitRepository.findAll(spec);
    }
}
