package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.StoreObjectDto;
import org.knowledge4retail.api.store.model.StoreObject;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreObjectConverter {
    StoreObjectConverter INSTANCE = Mappers.getMapper(StoreObjectConverter.class);

    StoreObjectDto storeObjectToDto(StoreObject storeObject);
    StoreObject dtoToStoreObject(StoreObjectDto dto);
    List<StoreObjectDto> storeObjectsToDtos(List<StoreObject> storeObjects);
}




