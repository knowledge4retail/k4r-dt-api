package org.knowledge4retail.api.product.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.knowledge4retail.api.product.filter.MaterialGroupFilter;
import org.knowledge4retail.api.product.model.MaterialGroup;
import org.knowledge4retail.api.product.repository.MaterialGroupRepository;
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
public class MaterialGroupMutation implements GraphQLMutationResolver {

    private final MaterialGroupRepository materialGroupRepository;

    public MaterialGroup updateMaterialGroup (MaterialGroup updateMaterialGroup, MaterialGroupFilter filter) throws IllegalAccessException{

        log.info("update oder post a single materialGroup using graphQL");
        if (filter != null) {

            Specification<MaterialGroup> spec = getSpecification(filter, null);
            try {

                MaterialGroup originalMaterialGroup = materialGroupRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateMaterialGroup.setId(originalMaterialGroup.getId());
                copyNonNullProperties(updateMaterialGroup, originalMaterialGroup);
                return materialGroupRepository.save(originalMaterialGroup);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return materialGroupRepository.save(updateMaterialGroup);
    }

    public List<MaterialGroup> postMaterialGroups (List<MaterialGroup> materialGroups){

        log.info("create a list of materialGroups using graphQL");
        return materialGroupRepository.saveAll(materialGroups);
    }
}
