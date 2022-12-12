package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.DespatchAdviceFilter;
import org.knowledge4retail.api.store.model.DespatchAdvice;
import org.knowledge4retail.api.store.repository.DespatchAdviceRepository;
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
public class DespatchAdviceMutation implements GraphQLMutationResolver {

    private final DespatchAdviceRepository despatchAdviceRepository;

    public DespatchAdvice updateDespatchAdvice (DespatchAdvice updateDespatchAdvice, DespatchAdviceFilter filter) throws IllegalAccessException{

        log.info("update oder post a single despatchAdvice using graphQL");
        if (filter != null) {

            Specification<DespatchAdvice> spec = getSpecification(filter, null);
            try {

                DespatchAdvice originalDespatchAdvice = despatchAdviceRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateDespatchAdvice.setId(originalDespatchAdvice.getId());
                copyNonNullProperties(updateDespatchAdvice, originalDespatchAdvice);
                return despatchAdviceRepository.save(originalDespatchAdvice);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return despatchAdviceRepository.save(updateDespatchAdvice);
    }

    public List<DespatchAdvice> postDespatchAdvices (List<DespatchAdvice> despatchAdvices){

        log.info("create a list of despatchAdvices using graphQL");
        return despatchAdviceRepository.saveAll(despatchAdvices);
    }
}
