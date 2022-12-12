package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.shared.util.OffSetDateTimeMapper;
import org.knowledge4retail.api.store.dto.SalesDto;
import org.knowledge4retail.api.store.model.Sales;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = OffSetDateTimeMapper.class)
public interface SalesConverter {
    SalesConverter INSTANCE = Mappers.getMapper(SalesConverter.class);

    SalesDto salesToDto(Sales sales);
    Sales dtoToSales(SalesDto dto);
    List<SalesDto> salesToDtos(List<Sales> sales);
    List<Sales> dtosToSales(List<SalesDto> salesDtos);
}




