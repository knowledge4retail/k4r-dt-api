package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.ShelfDto;
import org.knowledge4retail.api.store.model.Shelf;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShelfConverter {
    ShelfConverter INSTANCE = Mappers.getMapper(ShelfConverter.class);

    ShelfDto shelfToDto(Shelf shelf);
    Shelf dtoToShelf(ShelfDto dto);
    List<ShelfDto> shelvesToDtos(List<Shelf> shelves);
}




