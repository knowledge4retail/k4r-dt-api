package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.StoreCharacteristicDto;
import org.knowledge4retail.api.store.model.StoreCharacteristic;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreCharacteristicConverter {

    StoreCharacteristicConverter INSTANCE = Mappers.getMapper(StoreCharacteristicConverter.class);

    StoreCharacteristic dtoToCharacteristic(StoreCharacteristicDto storeCharacteristicDto);
    StoreCharacteristicDto characteristicToDto(StoreCharacteristic storeCharacteristic);
    List<StoreCharacteristicDto> characteristicsToDtos(List<StoreCharacteristic> storeCharacteristics);
}
