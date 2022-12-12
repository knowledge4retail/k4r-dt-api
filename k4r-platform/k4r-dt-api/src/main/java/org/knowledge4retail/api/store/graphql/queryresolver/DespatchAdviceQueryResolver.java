package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.DespatchAdviceFilter;
import org.knowledge4retail.api.store.model.DespatchAdvice;
import org.knowledge4retail.api.store.repository.DespatchAdviceRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class DespatchAdviceQueryResolver implements GraphQLQueryResolver {

    private final DespatchAdviceRepository despatchAdviceRepository;

    public List<DespatchAdvice> getDespatchAdvices (DespatchAdviceFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all despatchAdvices with the desired attributes");
            return despatchAdviceRepository.findAll();
        }

        log.info("Get all despatchAdvices with the desired attributes and filtered with: {}", filter);
        Specification<DespatchAdvice> spec = getSpecification(filter, null);
        return despatchAdviceRepository.findAll(spec);
    }
}
