package org.knowledge4retail.api.wireframe.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.wireframe.filter.WireframeFilter;
import org.knowledge4retail.api.wireframe.model.Wireframe;
import org.knowledge4retail.api.wireframe.repository.WireframeRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class WireframeQueryResolver implements GraphQLQueryResolver {

    private final WireframeRepository WireframeRepository;

    public List<Wireframe> getWireframes (WireframeFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all Wireframes with the desired attributes");
            return WireframeRepository.findAll();
        }

        log.info("Get all Wireframes with the desired attributes and filtered with: {}", filter);
        Specification<Wireframe> spec = getSpecification(filter, null);
        return WireframeRepository.findAll(spec);
    }
}
