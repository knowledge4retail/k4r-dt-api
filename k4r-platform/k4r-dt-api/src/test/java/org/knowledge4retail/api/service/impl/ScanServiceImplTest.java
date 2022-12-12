package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.product.service.ProductGtinService;
import org.knowledge4retail.api.scan.dto.ScanDto;
import org.knowledge4retail.api.scan.repository.ScanRepository;
import org.knowledge4retail.api.scan.service.impl.ScanServiceImpl;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.service.BarcodeService;
import org.knowledge4retail.api.store.service.ShelfLayerService;
import org.knowledge4retail.api.store.service.ShelfService;
import org.knowledge4retail.api.store.service.StoreService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ScanServiceImplTest {

    private ScanServiceImpl service;
    @Mock
    private ScanRepository scanRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private StoreService storeService;
    @Mock
    private ShelfService shelfService;
    @Mock
    private ShelfLayerService shelfLayerService;
    @Mock
    private BarcodeService barcodeService;
    @Mock
    private ProductGtinService productGtinService;

    @BeforeEach
    public void setup() {

        service = new ScanServiceImpl(scanRepository, producer, storeService, shelfService, shelfLayerService, barcodeService, productGtinService);
    }

    @Test
    public void createSendsMessageToKafka(){

        ScanDto scanDto = new ScanDto();
        scanDto.setEntityType("store");
        scanDto.setId("1234");
        scanDto.setTimestamp("2021-05-27T06:59:22");

        service.create(scanDto);
        verify(producer).publishCreate(any(), any());
    }
}
