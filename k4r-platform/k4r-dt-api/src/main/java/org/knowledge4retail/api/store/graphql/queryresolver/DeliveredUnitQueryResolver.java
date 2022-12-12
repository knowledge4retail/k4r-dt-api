package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.DeliveredUnitFilter;
import org.knowledge4retail.api.store.model.DeliveredUnit;
import org.knowledge4retail.api.store.repository.DeliveredUnitRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveredUnitQueryResolver implements GraphQLQueryResolver {

    private final DeliveredUnitRepository deliveredUnitRepository;

    public List<DeliveredUnit> getDeliveredUnits (DeliveredUnitFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all deliveredUnits with the desired attributes");
            return deliveredUnitRepository.findAll();
        }

        log.info("Get all deliveredUnits with the desired attributes and filtered with: {}", filter);
        Specification<DeliveredUnit> spec = getSpecification(filter, null);
        return deliveredUnitRepository.findAll(spec);
    }
}
