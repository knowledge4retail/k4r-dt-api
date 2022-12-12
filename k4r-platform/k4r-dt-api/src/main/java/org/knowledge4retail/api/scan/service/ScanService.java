package org.knowledge4retail.api.scan.service;

import org.knowledge4retail.api.scan.dto.ScanDto;

import java.util.List;

public interface ScanService {

    List<ScanDto> readAll();
    List<ScanDto> read(String entityType, String id);
    ScanDto create(ScanDto scanDto);
    boolean exists(String entityType, String id);
    void createOrUpdateStore(ScanDto scanDto);
    void createOrUpdateShelf(ScanDto scanDto);
    void createOrUpdateShelfLayer(ScanDto scanDto);
    void createOrUpdateBarcode(ScanDto scanDto);
}
