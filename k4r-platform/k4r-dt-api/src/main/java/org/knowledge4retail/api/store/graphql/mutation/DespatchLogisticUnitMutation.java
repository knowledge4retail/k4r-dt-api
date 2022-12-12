package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.DespatchLogisticUnitFilter;
import org.knowledge4retail.api.store.model.DespatchLogisticUnit;
import org.knowledge4retail.api.store.repository.DespatchLogisticUnitRepository;
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
public class DespatchLogisticUnitMutation implements GraphQLMutationResolver {

    private final DespatchLogisticUnitRepository despatchLogisticUnitRepository;

    public DespatchLogisticUnit updateDespatchLogisticUnit (DespatchLogisticUnit updateDespatchLogisticUnit, DespatchLogisticUnitFilter filter) throws IllegalAccessException{

        log.info("update oder post a single despatchLogisticUnit using graphQL");
        if (filter != null) {

            Specification<DespatchLogisticUnit> spec = getSpecification(filter, null);
            try {

                DespatchLogisticUnit originalDespatchLogisticUnit = despatchLogisticUnitRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateDespatchLogisticUnit.setId(originalDespatchLogisticUnit.getId());
                copyNonNullProperties(updateDespatchLogisticUnit, originalDespatchLogisticUnit);
                return despatchLogisticUnitRepository.save(originalDespatchLogisticUnit);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return despatchLogisticUnitRepository.save(updateDespatchLogisticUnit);
    }

    public List<DespatchLogisticUnit> postDespatchLogisticUnits (List<DespatchLogisticUnit> despatchLogisticUnits){

        log.info("create a list of despatchLogisticUnits using graphQL");
        return despatchLogisticUnitRepository.saveAll(despatchLogisticUnits);
    }
}
