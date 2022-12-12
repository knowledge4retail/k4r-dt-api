package org.knowledge4retail.api.product.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.filter.MaterialGroupFilter;
import org.knowledge4retail.api.product.model.MaterialGroup;
import org.knowledge4retail.api.product.repository.MaterialGroupRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class MaterialGroupQueryResolver implements GraphQLQueryResolver {

    private final MaterialGroupRepository materialGroupRepository;

    public List<MaterialGroup> getMaterialGroups (MaterialGroupFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all materialGroups with the desired attributes");
            return materialGroupRepository.findAll();
        }

        log.info("Get all materialGroups with the desired attributes and filtered with: {}", filter);
        Specification<MaterialGroup> spec = getSpecification(filter, null);
        return materialGroupRepository.findAll(spec);
    }
}
