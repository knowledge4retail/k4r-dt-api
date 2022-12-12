package org.knowledge4retail.api.position.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.position.dto.PositionDto;
import org.knowledge4retail.api.position.service.PositionService;
import org.knowledge4retail.api.product.dto.ProductGtinDto;
import org.knowledge4retail.api.product.service.ProductGtinService;
import org.knowledge4retail.api.store.dto.*;
import org.knowledge4retail.api.store.exception.FacingNotFoundException;
import org.knowledge4retail.api.store.service.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PositionServiceImpl implements PositionService {


    private final ProductGtinService productGtinService;
    private final FacingService facingService;
    private final ShelfLayerService shelfLayerService;
    private final ShelfService shelfService;
    private final BarcodeService barcodeService;

    @Override
    public FacingDto getFacingByExternalReferenceIdAndGtin(String externalReferenceId, String gtin) {

        ProductGtinDto productGtinDto = productGtinService.readByGtin(gtin);
        List<FacingDto> facingDtos = facingService.readByProductUnitId(productGtinDto.getProductUnitId());
        for(FacingDto facingDto : facingDtos) {

            ShelfLayerDto shelfLayerDto = shelfLayerService.read(facingDto.getShelfLayerId());
            if (externalReferenceId.equals(shelfLayerDto.getExternalReferenceId())) {

                return facingDto;
            }
        }

        log.warn("Facing assigned to gtin {} with shelfLayerId {} does not exist", gtin, externalReferenceId);
        throw new FacingNotFoundException();
    }

    @Override
    public List<PositionDto> getAllPositionsByStoreIdAndGtin(Integer storeId, String gtin) {

        List<BarcodeDto> barcodeDtos = getBarcodeDtos(gtin);
        List<FacingDto> facingDtos = getFacingDtos(gtin);
        List<ShelfLayerDto> shelfLayerDtos = getShelfLayerDtos(barcodeDtos);
        List<ShelfDto> shelfDtos = getShelfDtos(storeId, shelfLayerDtos);

        return getPositionDtos(gtin, barcodeDtos, facingDtos, shelfLayerDtos, shelfDtos);
    }

    private List<PositionDto> getPositionDtos(String gtin, List<BarcodeDto> barcodeDtos, List<FacingDto> facingDtos, List<ShelfLayerDto> shelfLayerDtos, List<ShelfDto> shelfDtos) {

        List<PositionDto> positionDtos = new ArrayList<>();
        for(ShelfDto shelfDto: shelfDtos){

            for(ShelfLayerDto shelfLayerDto: shelfLayerDtos){

                if(shelfLayerDto.getShelfId().equals(shelfDto.getId())){

                    for(FacingDto facingDto: facingDtos){

                        if(facingDto.getShelfLayerId().equals(shelfLayerDto.getId())){

                            PositionDto positionDto = PositionDto.builder().facingDto(facingDto).shelfDto(shelfDto).shelfLayerDto(shelfLayerDto).gtin(gtin).build();
                            positionDtos.add(positionDto);
                        }
                    }

                    for(BarcodeDto barcodeDto: barcodeDtos){

                        if(barcodeDto.getShelfLayerId().equals(shelfLayerDto.getId())){

                            PositionDto positionDto = PositionDto.builder().barcodeDto(barcodeDto).shelfDto(shelfDto).shelfLayerDto(shelfLayerDto).gtin(gtin).build();
                            positionDtos.add(positionDto);
                        }
                    }
                }
            }
        }
        return positionDtos;
    }

    private List<ShelfDto> getShelfDtos(Integer storeId, List<ShelfLayerDto> shelfLayerDtos) {

        List<Integer> shelfIds = shelfLayerDtos.stream()
                .map(ShelfLayerDto::getShelfId)
                .collect(Collectors.toList());

        return shelfService.readByShelfIdsAndStoreId(shelfIds, storeId);
    }

    private List<ShelfLayerDto> getShelfLayerDtos(List<BarcodeDto> barcodeDtos) {

        List<Integer> shelfLayerIds = barcodeDtos.stream()
                .map(BarcodeDto::getShelfLayerId)
                .collect(Collectors.toList());

        return shelfLayerService.readByShelfLayerIds(shelfLayerIds);
    }

    private List<FacingDto> getFacingDtos(String gtin) {

        //ToDo: Man könnte, statt hier zwei Services zu verwenden und Listen von DTOs zu bauen, im BarcodeService eine Funktion mit der Signatur readBarcodeByProductGtin(String gtin) implementieren, welche das Barcode Repository nutzt um dort ein fetch mit einer Join-Abfrage zu machen (falls wir hier Performance sparen wollen)
        ProductGtinDto productGtinDto = productGtinService.readByGtin(gtin);
        List<FacingDto> facingDtos = facingService.readByProductUnitId(productGtinDto.getProductUnitId());

        return facingDtos;
    }

    private List<BarcodeDto> getBarcodeDtos(String gtin) {

        ProductGtinDto productGtinDto = productGtinService.readByGtin(gtin);

        return barcodeService.readBarcodeByProductGtinId(productGtinDto.getId());
    }
}
