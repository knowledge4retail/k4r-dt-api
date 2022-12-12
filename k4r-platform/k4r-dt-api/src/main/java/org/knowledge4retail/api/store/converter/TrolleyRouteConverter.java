package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.TrolleyRouteDto;
import org.knowledge4retail.api.store.model.TrolleyRoute;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrolleyRouteConverter {

    TrolleyRouteConverter INSTANCE = Mappers.getMapper(TrolleyRouteConverter.class);

    TrolleyRoute dtoToTrolleyRoute(TrolleyRouteDto trolleyRouteDto);
    TrolleyRouteDto trolleyRouteToDto(TrolleyRoute trolleyRoute);
    List<TrolleyRouteDto> trolleyRoutesToDtos(List<TrolleyRoute> trolleyRoutes);
}
