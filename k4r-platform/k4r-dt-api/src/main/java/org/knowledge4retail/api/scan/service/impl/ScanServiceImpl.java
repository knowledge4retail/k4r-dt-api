package org.knowledge4retail.api.scan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.dto.ProductGtinDto;
import org.knowledge4retail.api.product.service.ProductGtinService;
import org.knowledge4retail.api.scan.converter.ScanConverter;
import org.knowledge4retail.api.scan.dto.ScanDto;
import org.knowledge4retail.api.scan.exception.EntityNotFoundException;
import org.knowledge4retail.api.scan.model.Scan;
import org.knowledge4retail.api.scan.repository.ScanRepository;
import org.knowledge4retail.api.scan.service.ScanService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.BarcodeDto;
import org.knowledge4retail.api.store.dto.ShelfDto;
import org.knowledge4retail.api.store.dto.ShelfLayerDto;
import org.knowledge4retail.api.store.dto.StoreDto;
import org.knowledge4retail.api.store.service.BarcodeService;
import org.knowledge4retail.api.store.service.ShelfLayerService;
import org.knowledge4retail.api.store.service.ShelfService;
import org.knowledge4retail.api.store.service.StoreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScanServiceImpl implements ScanService {

    private final ScanRepository scanRepository;
    private final DefaultProducer producer;
    @Value("${org.knowledge4retail.api.listener.kafka.topics.scan}")
    private String kafkaTopic;

    private final StoreService storeService;
    private final ShelfService shelfService;
    private final ShelfLayerService shelfLayerService;
    private final BarcodeService barcodeService;
    private final ProductGtinService productGtinService;

    @Override
    public List<ScanDto> readAll() {

        return ScanConverter.INSTANCE.scansToDtos(scanRepository.findAll());
    }

    @Override
    public List<ScanDto> read(String entityType, String id) {

        List<Scan> scans = scanRepository.findByEntityTypeAndId(entityType, id);
        return ScanConverter.INSTANCE.scansToDtos(scans);
    }

    @Override
    @Transactional
    public ScanDto create(ScanDto scanDto) {

        String type = scanDto.getEntityType();
        switch (type) {
            case "store" -> createOrUpdateStore(scanDto);
            case "shelf" -> createOrUpdateShelf(scanDto);
            case "shelfLayer" -> createOrUpdateShelfLayer(scanDto);
            case "barcode" -> createOrUpdateBarcode(scanDto);
            default -> throw new EntityNotFoundException();
        }

        Scan scanToSave = ScanConverter.INSTANCE.dtoToScan(scanDto);
        Scan savedScan = scanRepository.save(scanToSave);

        ScanDto createdDto = ScanConverter.INSTANCE.scanToDto(savedScan);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public boolean exists(String entityType, String id) {

        return scanRepository.existsByEntityTypeAndId(entityType, id);
    }

    @Override
    @Transactional
    public void createOrUpdateStore(ScanDto scan) {

        if(storeService.existsByExternalReferenceId(scan.getId())){

            log.info(String.format("new scan of store with the external Id %s", scan.getId()));
        } else {

            log.info("create new store by ubica Scan");
            StoreDto storeDto = StoreDto.builder()
                    .externalReferenceId(scan.getId())
                    .storeName("new_ubica_scan" + scan.getId())
                    .build();
            storeService.create(storeDto);
        }
    }

    @Override
    @Transactional
    public void createOrUpdateShelf(ScanDto scan) {

        StoreDto storeDto = storeService.readByExternalReferenceId(scan.getExternalReferenceId());

        String[] temp = scan.getAdditionalInfo().split("_");
        Integer runningNumber = Integer.parseInt(temp[0]);
        Integer blockId = Integer.parseInt(temp[1]);

        ShelfDto shelfDto = ShelfDto.builder()
                .storeId(storeDto.getId())
                .height(scan.getHeight())
                .width(scan.getWidth())
                .depth(scan.getDepth())
                .lengthUnitId(1)
                .positionX(scan.getPositionX())
                .positionY(scan.getPositionY())
                .positionZ(scan.getPositionZ())
                .orientationX(scan.getOrientationX())
                .orientationY(scan.getOrientationY())
                .orientationZ(scan.getOrientationZ())
                .orientationW(scan.getOrientationW())
                .blockId(blockId)
                .runningNumber(runningNumber)
                .externalReferenceId(scan.getId())
                .build();

        if(shelfService.existsByExternalReferenceId(scan.getId())){

            log.info(String.format("new scan of shelf with the ID %s", scan.getId()));
            List<ShelfDto> shelves = shelfService.readByStoreIdAndExternalReferenceId(storeDto.getId(), scan.getId());
            shelfDto.setId(shelves.get(0).getId());
            shelfDto.setCadPlanId(shelves.get(0).getCadPlanId());
            shelfDto.setProductGroupId(shelves.get(0).getProductGroupId());
            shelfDto.setLengthUnitId(shelves.get(0).getLengthUnitId());

            shelfService.update(shelfDto);
            log.info(String.format("new scan of shelfLayer with the ID %s", scan.getId()));
        } else {

            log.info("create new shelf from ubica scan");
            shelfService.create(shelfDto);
        }
    }

    @Override
    @Transactional
    public void createOrUpdateShelfLayer(ScanDto scan) {

        ShelfDto shelfDto = shelfService.readByExternalReferenceId(scan.getId());

        ShelfLayerDto shelfLayerDto = ShelfLayerDto.builder()
                .height(scan.getHeight())
                .width(scan.getWidth())
                .depth(scan.getDepth())
                .positionZ(scan.getPositionZ())
                .lengthUnitId(1)
                .shelfId(shelfDto.getId())
                .externalReferenceId(scan.getId())
                .level(Integer.parseInt(scan.getAdditionalInfo()))
                .build();


        if(shelfLayerService.existsByExternalReferenceIdAndLevel(scan.getId(), Integer.parseInt(scan.getAdditionalInfo()))){

            log.info(String.format("new scan of shelfLayer with the ID %s", scan.getId()));
            ShelfLayerDto shelfLayer = shelfLayerService.readByShelfIdAndExternalReferenceIdAndLevel(shelfDto.getId(), scan.getId(), Integer.parseInt(scan.getAdditionalInfo()));
            shelfLayerDto.setId(shelfLayer.getId());
            shelfLayerDto.setLengthUnitId(shelfLayer.getLengthUnitId());
            shelfLayerDto.setType(shelfLayer.getType());

            shelfLayerService.update(shelfLayerDto);
            log.info(String.format("new scan of shelfLayer with the ID %s", scan.getId()));
        } else {

            log.info("create new shelfLayer from ubica scan");
            shelfLayerService.create(shelfLayerDto);
        }
    }

    @Override
    @Transactional
    public void createOrUpdateBarcode(ScanDto scan) {

        ProductGtinDto productGtinDto = null;
        if(productGtinService.existsByExternalReferenceId(scan.getId())) {
            productGtinDto = productGtinService.readByExternalReferenceId(scan.getId());
        } else {
            productGtinDto = new ProductGtinDto();
            productGtinDto.setExternalReferenceId(scan.getId());
            productGtinDto = productGtinService.create(productGtinDto);
        }
        ShelfDto shelfDto = shelfService.readByExternalReferenceId(scan.getExternalReferenceId());
        ShelfLayerDto shelfLayerDto = shelfLayerService.readByShelfIdAndExternalReferenceIdAndLevel(shelfDto.getId(), scan.getExternalReferenceId(), Integer.parseInt(scan.getAdditionalInfo()));

        BarcodeDto barcodeDto = BarcodeDto.builder()
                .positionX(scan.getPositionX())
                .positionY(scan.getPositionY())
                .positionZ(scan.getPositionZ())
                .productGtinId(productGtinDto.getId())
                .shelfLayerId(shelfLayerDto.getId())
                .build();

        if(barcodeService.existsByShelfLayerIdAndProductGtinId(shelfLayerDto.getId(), productGtinDto.getId())){

            log.info(String.format("new scan of barcode with the ID %s", scan.getId()));
            BarcodeDto barcode = barcodeService.readByShelfLayerAndProductGtin(shelfLayerDto.getId(), productGtinDto.getId());
            barcodeDto.setId(barcode.getId());

            barcodeService.update(barcode.getId(), barcodeDto);
            log.info(String.format("new scan of barcode with the ID %s", barcode.getId()));
        } else {

            log.info("create new barcode from ubica scan");
            barcodeService.create(barcodeDto);
        }
    }
}
