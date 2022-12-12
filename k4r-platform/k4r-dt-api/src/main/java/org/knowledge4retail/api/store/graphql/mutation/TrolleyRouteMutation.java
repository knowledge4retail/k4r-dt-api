package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.TrolleyRouteFilter;
import org.knowledge4retail.api.store.model.TrolleyRoute;
import org.knowledge4retail.api.store.repository.TrolleyRouteRepository;
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
public class TrolleyRouteMutation implements GraphQLMutationResolver {

    private final TrolleyRouteRepository trolleyRouteRepository;

    public TrolleyRoute updateTrolleyRoute (TrolleyRoute updateTrolleyRoute, TrolleyRouteFilter filter) throws IllegalAccessException{

        log.info("update oder post a single trolleyRoute using graphQL");
        if (filter != null) {

            Specification<TrolleyRoute> spec = getSpecification(filter, null);
            try {

                TrolleyRoute originalTrolleyRoute = trolleyRouteRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateTrolleyRoute.setId(originalTrolleyRoute.getId());
                copyNonNullProperties(updateTrolleyRoute, originalTrolleyRoute);
                return trolleyRouteRepository.save(originalTrolleyRoute);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return trolleyRouteRepository.save(updateTrolleyRoute);
    }

    public List<TrolleyRoute> postTrolleyRoutes (List<TrolleyRoute> trolleyRoutes){

        log.info("create a list of trolley routes using graphQL");
        return trolleyRouteRepository.saveAll(trolleyRoutes);
    }
}
