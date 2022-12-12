package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.DespatchLogisticUnitFilter;
import org.knowledge4retail.api.store.model.DespatchLogisticUnit;
import org.knowledge4retail.api.store.repository.DespatchLogisticUnitRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class DespatchLogisticUnitQueryResolver implements GraphQLQueryResolver {

    private final DespatchLogisticUnitRepository despatchLogisticUnitRepository;

    public List<DespatchLogisticUnit> getDespatchLogisticUnits (DespatchLogisticUnitFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all despatchLogisticUnits with the desired attributes");
            return despatchLogisticUnitRepository.findAll();
        }

        log.info("Get all despatchLogisticUnits with the desired attributes and filtered with: {}", filter);
        Specification<DespatchLogisticUnit> spec = getSpecification(filter, null);
        return despatchLogisticUnitRepository.findAll(spec);
    }
}
