package org.knowledge4retail.api.scan.converter;

import org.knowledge4retail.api.scan.dto.ScanDto;
import org.knowledge4retail.api.scan.model.Scan;
import org.knowledge4retail.api.shared.util.OffSetDateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = OffSetDateTimeMapper.class)
public interface ScanConverter {
    ScanConverter INSTANCE = Mappers.getMapper(ScanConverter.class);

    ScanDto scanToDto(Scan scan);
    Scan dtoToScan(ScanDto dto);
    List<ScanDto> scansToDtos(List<Scan> scans);
    List<Scan> dtosToScans(List<ScanDto> scans);
}




