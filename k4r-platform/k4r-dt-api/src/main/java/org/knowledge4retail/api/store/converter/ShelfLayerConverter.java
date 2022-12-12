package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.ShelfLayerDto;
import org.knowledge4retail.api.store.model.ShelfLayer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShelfLayerConverter {
    ShelfLayerConverter INSTANCE = Mappers.getMapper(ShelfLayerConverter.class);

    ShelfLayerDto shelfLayerToDto(ShelfLayer shelfLayer);
    ShelfLayer dtoToShelfLayer(ShelfLayerDto dto);
    List<ShelfLayerDto> shelfLayersToDtos(List<ShelfLayer> shelfLayers);
}
