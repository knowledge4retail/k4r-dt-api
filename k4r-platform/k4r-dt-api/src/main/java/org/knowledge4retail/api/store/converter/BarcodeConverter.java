package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.BarcodeDto;
import org.knowledge4retail.api.store.model.Barcode;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BarcodeConverter {
    BarcodeConverter INSTANCE = Mappers.getMapper(BarcodeConverter.class);

    BarcodeDto barcodeToDto(Barcode barcode);
    Barcode dtoToBarcode(BarcodeDto dto);
    List<BarcodeDto> barcodesToDtos(List<Barcode> barcodes);
    List<Barcode> dtosToBarcodes(List<BarcodeDto> barcodeDtos);
}




