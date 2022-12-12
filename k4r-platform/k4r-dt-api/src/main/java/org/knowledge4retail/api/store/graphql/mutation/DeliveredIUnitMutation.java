package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.DeliveredUnitFilter;
import org.knowledge4retail.api.store.model.DeliveredUnit;
import org.knowledge4retail.api.store.repository.DeliveredUnitRepository;
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
public class DeliveredIUnitMutation implements GraphQLMutationResolver {

    private final DeliveredUnitRepository deliveredUnitRepository;

    public DeliveredUnit updateDeliveredUnit (DeliveredUnit updateDeliveredUnit, DeliveredUnitFilter filter) throws IllegalAccessException{

        log.info("update oder post a single deliveredUnit using graphQL");
        if (filter != null) {

            Specification<DeliveredUnit> spec = getSpecification(filter, null);
            try {

                DeliveredUnit originalDeliveredUnit = deliveredUnitRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateDeliveredUnit.setId(originalDeliveredUnit.getId());
                copyNonNullProperties(updateDeliveredUnit, originalDeliveredUnit);
                return deliveredUnitRepository.save(originalDeliveredUnit);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return deliveredUnitRepository.save(updateDeliveredUnit);
    }

    public List<DeliveredUnit> postDeliveredUnits (List<DeliveredUnit> deliveredUnits){

        log.info("create a list of deliveredUnits using graphQL");
        return deliveredUnitRepository.saveAll(deliveredUnits);
    }
}
