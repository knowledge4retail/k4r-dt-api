package org.knowledge4retail.api.product.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.converter.ProductGtinConverter;
import org.knowledge4retail.api.product.dto.ProductGtinDto;
import org.knowledge4retail.api.product.model.ProductGtin;
import org.knowledge4retail.api.product.repository.ProductGtinRepository;
import org.knowledge4retail.api.product.service.ProductGtinService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductGtinServiceImpl implements ProductGtinService {

    @Value("${org.knowledge4retail.api.listener.kafka.topics.productgtin}")
    private String kafkaTopic;

    private final ProductGtinRepository productGtinRepository;
    private final DefaultProducer producer;

    @Override
    public List<ProductGtinDto> readAll() {

        return ProductGtinConverter.INSTANCE.productGtinsToDtos(productGtinRepository.findAll());
    }

    @Override
    public ProductGtinDto read(Integer id) {

        ProductGtin productGtin = productGtinRepository.getReferenceById(id);
        return ProductGtinConverter.INSTANCE.productGtinToDto(productGtin);
    }

    @Override
    public ProductGtinDto readByGtin(String gtin) {

        ProductGtin productGtin = productGtinRepository.getByGtin(gtin);
        return ProductGtinConverter.INSTANCE.productGtinToDto(productGtin);
    }

    @Override
    public ProductGtinDto readByExternalReferenceId(String externalReferenceId) {

        ProductGtin productGtin = productGtinRepository.getByExternalReferenceId(externalReferenceId);
        return ProductGtinConverter.INSTANCE.productGtinToDto(productGtin);
    }

    @Override
    public ProductGtinDto create(ProductGtinDto productGtinDto) {

        productGtinDto.setId(null);
        ProductGtin gtinToSave = ProductGtinConverter.INSTANCE.dtoToProductGtin(productGtinDto);
        ProductGtin savedGtin = productGtinRepository.save(gtinToSave);

        ProductGtinDto createdDto = ProductGtinConverter.INSTANCE.productGtinToDto(savedGtin);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public ProductGtinDto update(Integer id, ProductGtinDto productGtinDto) {

        ProductGtinDto oldDto = read(id);
        productGtinDto.setId(id);

        ProductGtin productGtin = ProductGtinConverter.INSTANCE.dtoToProductGtin(productGtinDto);
        ProductGtinDto createdDto = ProductGtinConverter.INSTANCE.productGtinToDto(productGtinRepository.save(productGtin));

        producer.publishUpdate(kafkaTopic, createdDto, oldDto);
        return createdDto;
    }

    public void delete(Integer id) {

        ProductGtinDto deletedDto = ProductGtinConverter.INSTANCE.productGtinToDto(productGtinRepository.getReferenceById(id));
        productGtinRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    public boolean exists(Integer id) {

        return productGtinRepository.existsById(id);
    }

    public boolean existsByGtin(String gtin) {

        return productGtinRepository.existsByGtin(gtin);
    }

    public boolean existsByExternalReferenceId(String externalReferenceId) {

        return productGtinRepository.existsByExternalReferenceId(externalReferenceId);
    }
}
