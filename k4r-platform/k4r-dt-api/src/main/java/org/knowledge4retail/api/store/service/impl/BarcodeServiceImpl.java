package org.knowledge4retail.api.store.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.BarcodeConverter;
import org.knowledge4retail.api.store.dto.BarcodeDto;
import org.knowledge4retail.api.store.filter.BarcodeFilter;
import org.knowledge4retail.api.store.model.Barcode;
import org.knowledge4retail.api.store.repository.BarcodeRepository;
import org.knowledge4retail.api.store.service.BarcodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Service
public class BarcodeServiceImpl implements BarcodeService {

    private final BarcodeRepository repository;

    private final DefaultProducer producer;
    @Value("${org.knowledge4retail.api.listener.kafka.topics.barcode}")
    private String kafkaTopic;

    public BarcodeServiceImpl(BarcodeRepository repository, DefaultProducer producer) {

        this.repository = repository;
        this.producer = producer;
    }

    @Override
    public BarcodeDto create(BarcodeDto dto) {

        dto.setId(null);
        BarcodeDto createdDto = BarcodeConverter.INSTANCE.barcodeToDto(repository.save(BarcodeConverter.INSTANCE.dtoToBarcode(dto)));

        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public List<BarcodeDto> readByShelfLayerId(Integer shelfLayerId) {

        return BarcodeConverter.INSTANCE.barcodesToDtos(repository.findByShelfLayerId(shelfLayerId));
    }

    @Override
    public BarcodeDto read(Integer barcodeId) {

        return BarcodeConverter.INSTANCE.barcodeToDto(repository.getReferenceById(barcodeId));
    }

    @Override
    public void delete(Integer barcodeId) {

        BarcodeDto deletedDto = read(barcodeId);
        repository.deleteById(barcodeId);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    @Override
    public boolean exists(Integer barcodeId) {

        return repository.existsById(barcodeId);
    }


    @Override
    public BarcodeDto update(Integer barcodeId, BarcodeDto dto) {

        Barcode barcode = this.repository.getReferenceById(barcodeId);
        BarcodeDto oldDto = BarcodeConverter.INSTANCE.barcodeToDto(barcode);

        BarcodeDto updatedDto = BarcodeConverter.INSTANCE.barcodeToDto(this.repository.save(barcode));

        producer.publishUpdate(kafkaTopic, updatedDto, oldDto);
        return updatedDto;
    }

    @Override
    public List<BarcodeDto> readBarcodeByProductGtinId(Integer productGtinId) {

        return BarcodeConverter.INSTANCE.barcodesToDtos(repository.findByProductGtinId(productGtinId));
    }

    @Override
    public Map<Integer, List<Barcode>> filterBarcode(Map<Object, Object> shelfLayerContext) {

        log.info("Resolver called PositionService to get filtered barcodes");
        Set<Object> shelfLayerIds = shelfLayerContext.keySet();
        Map<Integer, List<Barcode>> map = new HashMap<>();

        for (Object shelfLayerObjectId : shelfLayerIds) {

            Integer shelfLayerId = Integer.parseInt(shelfLayerObjectId.toString());
            BarcodeFilter filter = (BarcodeFilter) shelfLayerContext.get(shelfLayerId);
            addBarcodeListToMap(shelfLayerId, filter, map);
        }
        return map;
    }

    @Override
    public BarcodeDto readByShelfLayerAndProductGtin(Integer shelfLayerId, Integer productGtinId) {

        return BarcodeConverter.INSTANCE.barcodeToDto(repository.findByShelfLayerIdAndProductGtinId(shelfLayerId, productGtinId));
    }

    @Override
    public Boolean existsByShelfLayerIdAndProductGtinId(Integer shelfLayerId, Integer productGtinId) {

        return repository.existsByShelfLayerIdAndProductGtinId(shelfLayerId, productGtinId);
    }

    private void addBarcodeListToMap(Integer shelfLayerId, BarcodeFilter filter, Map<Integer, List<Barcode>> map) {

        List<Barcode> barcodes;
        if (filter == null) {

            barcodes = repository.findByShelfLayerId(shelfLayerId);
        } else {

            Specification<Barcode> spec = null;
            try {
                spec = getSpecification(filter, null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            barcodes = repository.findAll(spec);
        }

        map.put(shelfLayerId, barcodes);
    }
}
