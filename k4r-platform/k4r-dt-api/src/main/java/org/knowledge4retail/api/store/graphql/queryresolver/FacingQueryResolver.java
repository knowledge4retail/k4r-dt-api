package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.FacingFilter;
import org.knowledge4retail.api.store.model.Facing;
import org.knowledge4retail.api.store.repository.FacingRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacingQueryResolver implements GraphQLQueryResolver {

    private final FacingRepository facingRepository;

    public List<Facing> getFacings (FacingFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all facings with the desired attributes");
            return facingRepository.findAll();
        }

        log.info("Get all facings with the desired attributes and filtered with: {}", filter);
        Specification<Facing> spec = getSpecification(filter, null);
        return facingRepository.findAll(spec);
    }
}
