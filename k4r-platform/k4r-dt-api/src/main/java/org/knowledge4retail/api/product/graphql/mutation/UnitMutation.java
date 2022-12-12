package org.knowledge4retail.api.product.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.UnitFilter;
import org.knowledge4retail.api.product.model.Unit;
import org.knowledge4retail.api.product.repository.UnitRepository;
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
public class UnitMutation implements GraphQLMutationResolver {

    private final UnitRepository unitRepository;

    public Unit updateUnit (Unit updateUnit, UnitFilter filter) throws IllegalAccessException{

        log.info("update oder post a single unit using graphQL");
        if (filter != null) {

            Specification<Unit> spec = getSpecification(filter, null);
            try {

                Unit originalUnit = unitRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateUnit.setId(originalUnit.getId());
                copyNonNullProperties(updateUnit, originalUnit);
                return unitRepository.save(originalUnit);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return unitRepository.save(updateUnit);
    }

    public List<Unit> postUnits (List<Unit> units){

        log.info("create a list of units using graphQL");
        return unitRepository.saveAll(units);
    }
}
