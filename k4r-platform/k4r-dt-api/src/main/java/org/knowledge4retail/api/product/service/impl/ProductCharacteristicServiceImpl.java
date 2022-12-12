package org.knowledge4retail.api.product.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.converter.ProductCharacteristicConverter;
import org.knowledge4retail.api.product.dto.ProductCharacteristicDto;
import org.knowledge4retail.api.product.model.ProductCharacteristic;
import org.knowledge4retail.api.product.repository.ProductCharacteristicRepository;
import org.knowledge4retail.api.product.service.ProductCharacteristicService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductCharacteristicServiceImpl implements ProductCharacteristicService {

    private final ProductCharacteristicRepository productCharacteristicRepository;
    private final DefaultProducer producer;
    @Value("${org.knowledge4retail.api.listener.kafka.topics.productcharacteristic}")
    private String kafkaTopic;

    public ProductCharacteristicServiceImpl(ProductCharacteristicRepository productCharacteristicRepository, DefaultProducer producer){

        this.productCharacteristicRepository = productCharacteristicRepository;
        this.producer = producer;
    }

    @Override
    public List<ProductCharacteristicDto> readAll() {

        return ProductCharacteristicConverter.INSTANCE.characteristicsToDtos(productCharacteristicRepository.findAll());
    }

    @Override
    public ProductCharacteristicDto create(ProductCharacteristicDto productCharacteristicDto) {

        productCharacteristicDto.setId(null);
        ProductCharacteristic characterToSave = ProductCharacteristicConverter.INSTANCE.dtoToCharacteristic(productCharacteristicDto);
        ProductCharacteristic savedCharacter = productCharacteristicRepository.save(characterToSave);

        ProductCharacteristicDto createdDto = ProductCharacteristicConverter.INSTANCE.characteristicToDto(savedCharacter);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public void delete(Integer id) {

        ProductCharacteristicDto deletedDto = ProductCharacteristicConverter.INSTANCE.characteristicToDto(productCharacteristicRepository.getReferenceById(id));
        productCharacteristicRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    @Override
    public boolean exists(Integer id) {

        return productCharacteristicRepository.existsById(id);
    }
}
