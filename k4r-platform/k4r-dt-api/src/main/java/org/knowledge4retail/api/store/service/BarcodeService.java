package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.BarcodeDto;
import org.knowledge4retail.api.store.model.Barcode;

import java.util.List;
import java.util.Map;

public interface BarcodeService {

    BarcodeDto create(BarcodeDto barcode);
    List<BarcodeDto> readByShelfLayerId(Integer shelfLayerId);
    boolean exists(Integer barcodeId);
    void delete(Integer barcodeId);
    BarcodeDto update(Integer barcodeId, BarcodeDto dto);
    BarcodeDto read(Integer barcodeId);
    List<BarcodeDto> readBarcodeByProductGtinId(Integer productGtinId);
    Map<Integer, List<Barcode>> filterBarcode(Map<Object, Object> shelfLayerContext);
    BarcodeDto readByShelfLayerAndProductGtin(Integer shelfLayerId, Integer productGtinId);
    Boolean existsByShelfLayerIdAndProductGtinId(Integer shelfLayerId, Integer productGtinId);
}
