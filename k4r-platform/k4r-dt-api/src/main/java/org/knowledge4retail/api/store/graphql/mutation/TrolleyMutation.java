package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.TrolleyFilter;
import org.knowledge4retail.api.store.model.Trolley;
import org.knowledge4retail.api.store.repository.TrolleyRepository;
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
public class TrolleyMutation implements GraphQLMutationResolver {

    private final TrolleyRepository trolleyRepository;

    public Trolley updateTrolley (Trolley updateTrolley, TrolleyFilter filter) throws IllegalAccessException{

        log.info("update oder post a single trolley using graphQL");
        if (filter != null) {

            Specification<Trolley> spec = getSpecification(filter, null);
            try {

                Trolley originalTrolley = trolleyRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateTrolley.setId(originalTrolley.getId());
                copyNonNullProperties(updateTrolley, originalTrolley);
                return trolleyRepository.save(originalTrolley);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return trolleyRepository.save(updateTrolley);
    }

    public List<Trolley> postTrolleys (List<Trolley> trolleys){

        log.info("create a list of trolleys using graphQL");
        return trolleyRepository.saveAll(trolleys);
    }
}
