package org.knowledge4retail.api.wireframe.queryresolver.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.wireframe.filter.WireframeFilter;
import org.knowledge4retail.api.wireframe.model.Wireframe;
import org.knowledge4retail.api.wireframe.repository.WireframeRepository;
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
public class WireframeMutation implements GraphQLMutationResolver {

    private final WireframeRepository WireframeRepository;

    public Wireframe updateWireframe (Wireframe updateWireframe, WireframeFilter filter) throws IllegalAccessException{

        log.info("update oder post a single Wireframe using graphQL");
        if (filter != null) {

            Specification<Wireframe> spec = getSpecification(filter, null);
            try {

                Wireframe originalWireframe = WireframeRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateWireframe.setId(originalWireframe.getId());
                copyNonNullProperties(updateWireframe, originalWireframe);
                return WireframeRepository.save(originalWireframe);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return WireframeRepository.save(updateWireframe);
    }

    public List<Wireframe> postWireframes (List<Wireframe> Wireframes){

        log.info("create a list of Wireframes using graphQL");
        return WireframeRepository.saveAll(Wireframes);
    }
}
