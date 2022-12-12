package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.StorePropertyDto;
import org.knowledge4retail.api.store.model.StoreProperty;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StorePropertyConverter {

    StorePropertyConverter INSTANCE = Mappers.getMapper(StorePropertyConverter.class);

    StoreProperty dtoToProperty(StorePropertyDto property);
    StorePropertyDto propertyToDto(StoreProperty storeProperty);
    List<StorePropertyDto> propertiesToDtos(List<StoreProperty>properties);
}
