package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.PlanogramFilter;
import org.knowledge4retail.api.store.model.Planogram;
import org.knowledge4retail.api.store.repository.PlanogramRepository;
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
public class PlanogramMutation implements GraphQLMutationResolver {

    private final PlanogramRepository planogramRepository;

    public Planogram updatePlanogram (Planogram updatePlanogram, PlanogramFilter filter) throws IllegalAccessException{

        log.info("update oder post a single planogram using graphQL");
        if (filter != null) {

            Specification<Planogram> spec = getSpecification(filter, null);
            try {

                Planogram originalPlanogram = planogramRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updatePlanogram.setId(originalPlanogram.getId());
                copyNonNullProperties(updatePlanogram, originalPlanogram);
                return planogramRepository.save(originalPlanogram);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return planogramRepository.save(updatePlanogram);
    }

    public List<Planogram> postPlanograms (List<Planogram> planograms){

        log.info("create a list of planograms using graphQL");
        return planogramRepository.saveAll(planograms);
    }
}
