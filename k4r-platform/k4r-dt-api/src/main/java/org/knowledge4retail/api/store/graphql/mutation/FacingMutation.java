package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.FacingFilter;
import org.knowledge4retail.api.store.model.Facing;
import org.knowledge4retail.api.store.repository.FacingRepository;
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
public class FacingMutation implements GraphQLMutationResolver {

    private final FacingRepository facingRepository;

    public Facing updateFacing (Facing updateFacing, FacingFilter filter) throws IllegalAccessException{

        log.info("update oder post a single facing using graphQL");
        if (filter != null) {

            Specification<Facing> spec = getSpecification(filter, null);
            try {

                Facing originalFacing = facingRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateFacing.setId(originalFacing.getId());
                copyNonNullProperties(updateFacing, originalFacing);
                return facingRepository.save(originalFacing);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return facingRepository.save(updateFacing);
    }

    public List<Facing> postFacings (List<Facing> facings){

        log.info("create a list of facings using graphQL");
        return facingRepository.saveAll(facings);
    }
}
