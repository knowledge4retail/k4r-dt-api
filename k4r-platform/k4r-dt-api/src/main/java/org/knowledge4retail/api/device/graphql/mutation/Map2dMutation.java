package org.knowledge4retail.api.device.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.filter.Map2dFilter;
import org.knowledge4retail.api.device.model.Map2d;
import org.knowledge4retail.api.device.repository.Map2dRepository;
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
public class Map2dMutation implements GraphQLMutationResolver {

    private final Map2dRepository map2dRepository;

    public Map2d updateMap2d (Map2d updateMap2d, Map2dFilter filter) throws IllegalAccessException{

        log.info("update oder post a single map2d using graphQL");
        if (filter != null) {

            Specification<Map2d> spec = getSpecification(filter, null);
            try {

                Map2d originalMap2d = map2dRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateMap2d.setId(originalMap2d.getId());
                copyNonNullProperties(updateMap2d, originalMap2d);
                return map2dRepository.save(originalMap2d);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return map2dRepository.save(updateMap2d);
    }

    public List<Map2d> postMap2ds (List<Map2d> map2ds){

        log.info("create a list of map2ds using graphQL");
        return map2dRepository.saveAll(map2ds);
    }
}
