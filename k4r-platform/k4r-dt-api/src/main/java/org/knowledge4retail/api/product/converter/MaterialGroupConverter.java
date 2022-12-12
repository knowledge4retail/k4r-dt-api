package org.knowledge4retail.api.product.converter;

import org.knowledge4retail.api.product.dto.MaterialGroupDto;
import org.knowledge4retail.api.product.model.MaterialGroup;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MaterialGroupConverter {

    MaterialGroupConverter INSTANCE = Mappers.getMapper(MaterialGroupConverter.class);

    MaterialGroup dtoToMaterialGroup(MaterialGroupDto materialGroupDto);
    MaterialGroupDto materialGroupToDto(MaterialGroup materialGroup);
    List<MaterialGroupDto> materialGroupsToDtos(List<MaterialGroup> materialGroups);
}
