package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.DespatchAdviceDto;
import org.knowledge4retail.api.store.model.DespatchAdvice;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DespatchAdviceConverter {

    DespatchAdviceConverter INSTANCE = Mappers.getMapper(DespatchAdviceConverter.class);

    DespatchAdvice dtoToDespatchAdvice(DespatchAdviceDto despatchAdviceDto);
    DespatchAdviceDto despatchAdviceToDto(DespatchAdvice despatchAdvice);
    List<DespatchAdviceDto> despatchAdvicesToDtos(List<DespatchAdvice> despatchAdvices);
}
