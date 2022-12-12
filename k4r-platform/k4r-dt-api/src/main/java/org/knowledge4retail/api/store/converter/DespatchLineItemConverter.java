package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.DespatchLineItemDto;
import org.knowledge4retail.api.store.model.DespatchLineItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DespatchLineItemConverter {

    DespatchLineItemConverter INSTANCE = Mappers.getMapper(DespatchLineItemConverter.class);

    DespatchLineItem dtoToDespatchLineItem(DespatchLineItemDto despatchLineItemDto);
    DespatchLineItemDto despatchLineItemToDto(DespatchLineItem despatchLineItem);
    List<DespatchLineItemDto> despatchLineItemToDtos(List<DespatchLineItem> despatchLineItems);
}
