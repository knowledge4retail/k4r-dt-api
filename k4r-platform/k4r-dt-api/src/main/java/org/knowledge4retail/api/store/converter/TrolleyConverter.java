package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.TrolleyDto;
import org.knowledge4retail.api.store.model.Trolley;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrolleyConverter {

    TrolleyConverter INSTANCE = Mappers.getMapper(TrolleyConverter.class);

    Trolley dtoToTrolley(TrolleyDto trolleyDto);
    TrolleyDto trolleyToDto(Trolley trolley);
    List<TrolleyDto> trolleysToDtos(List<Trolley> trolleys);
}
