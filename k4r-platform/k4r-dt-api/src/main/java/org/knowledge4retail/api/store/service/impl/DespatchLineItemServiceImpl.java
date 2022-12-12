package org.knowledge4retail.api.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.DespatchLineItemConverter;
import org.knowledge4retail.api.store.dto.DespatchLineItemDto;
import org.knowledge4retail.api.store.model.DespatchLineItem;
import org.knowledge4retail.api.store.repository.DespatchLineItemRepository;
import org.knowledge4retail.api.store.service.DespatchLineItemService;
import org.knowledge4retail.api.store.service.DespatchLogisticUnitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DespatchLineItemServiceImpl implements DespatchLineItemService {

    @Value("${org.knowledge4retail.api.listener.kafka.topics.despatchlineitem}")
    private String kafkaTopic;

    private final DespatchLineItemRepository despatchLineItemRepository;
    private final DefaultProducer producer;
    private final DespatchLogisticUnitService despatchLogisticUnitService;
    private final ProductService productService;

    @Override
    public List<DespatchLineItemDto> readByDespatchLogisticUnitId(Integer despatchLogisticUnitId) {

        return DespatchLineItemConverter.INSTANCE.despatchLineItemToDtos(despatchLineItemRepository.findByDespatchLogisticUnitId(despatchLogisticUnitId));
    }

    @Override
    public DespatchLineItemDto read(Integer id) {

        DespatchLineItem despatchLineItem = despatchLineItemRepository.getReferenceById(id);
        return DespatchLineItemConverter.INSTANCE.despatchLineItemToDto(despatchLineItem);
    }

    @Override
    public DespatchLineItemDto create(DespatchLineItemDto despatchLineItemDto) {

        despatchLineItemDto.setId(null);
        DespatchLineItem despatchLineItemToSave = DespatchLineItemConverter.INSTANCE.dtoToDespatchLineItem(despatchLineItemDto);
        DespatchLineItem savedDespatchLineItem = despatchLineItemRepository.save(despatchLineItemToSave);

        DespatchLineItemDto createdDto = DespatchLineItemConverter.INSTANCE.despatchLineItemToDto(savedDespatchLineItem);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public DespatchLineItemDto update(Integer id, DespatchLineItemDto despatchLineItemDto) {

        DespatchLineItemDto oldDto = read(id);
        despatchLineItemDto.setId(id);

        DespatchLineItem despatchLineItem = DespatchLineItemConverter.INSTANCE.dtoToDespatchLineItem(despatchLineItemDto);
        DespatchLineItemDto createdDto = DespatchLineItemConverter.INSTANCE.despatchLineItemToDto(despatchLineItemRepository.save(despatchLineItem));

        producer.publishUpdate(kafkaTopic, createdDto, oldDto);
        return createdDto;
    }

    @Override
    public void delete(Integer id) {

        DespatchLineItemDto deletedDto = DespatchLineItemConverter.INSTANCE.despatchLineItemToDto(despatchLineItemRepository.getReferenceById(id));
        despatchLineItemRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    @Override
    public boolean exists(Integer id) {

        return despatchLineItemRepository.existsById(id);
    }

}
