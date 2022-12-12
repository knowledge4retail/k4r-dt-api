package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.PlanogramFilter;
import org.knowledge4retail.api.store.model.Planogram;
import org.knowledge4retail.api.store.repository.PlanogramRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanogramQueryResolver implements GraphQLQueryResolver {

    private final PlanogramRepository planogramRepository;

    public List<Planogram> getPlanograms (PlanogramFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all planograms with the desired attributes");
            return planogramRepository.findAll();
        }

        log.info("Get all planograms with the desired attributes and filtered with: {}", filter);
        Specification<Planogram> spec = getSpecification(filter, null);
        return planogramRepository.findAll(spec);
    }
}
