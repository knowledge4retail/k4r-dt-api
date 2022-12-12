package org.knowledge4retail.api.product.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.converter.ProductUnitConverter;
import org.knowledge4retail.api.product.dto.ProductUnitDto;
import org.knowledge4retail.api.product.model.ProductUnit;
import org.knowledge4retail.api.product.repository.ProductUnitRepository;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.product.service.ProductUnitService;
import org.knowledge4retail.api.product.service.UnitService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductUnitServiceImpl implements ProductUnitService {

    @Value("${org.knowledge4retail.api.listener.kafka.topics.productunit}")
    private String kafkaTopic;

    private final ProductUnitRepository productUnitRepository;
    private final DefaultProducer producer;
    private final UnitService unitService;
    private final ProductService productService;

    @Override
    public List<ProductUnitDto> readAll() {

        return ProductUnitConverter.INSTANCE.productUnitToDtos(productUnitRepository.findAll());
    }

    @Override
    public ProductUnitDto read(Integer id) {

        ProductUnit productUnit = productUnitRepository.getReferenceById(id);
        return ProductUnitConverter.INSTANCE.productUnitToDto(productUnit);
    }

    @Override
    public ProductUnitDto create(ProductUnitDto productUnitDto) {

        productUnitDto.setId(null);
        ProductUnit unitToSave = ProductUnitConverter.INSTANCE.dtoToProductUnit(productUnitDto);
        ProductUnit savedUnit = productUnitRepository.save(unitToSave);

        ProductUnitDto createdDto = ProductUnitConverter.INSTANCE.productUnitToDto(savedUnit);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public ProductUnitDto update(Integer id, ProductUnitDto productUnitDto) {

        ProductUnitDto oldDto = read(id);
        productUnitDto.setId(id);

        ProductUnit productUnit = ProductUnitConverter.INSTANCE.dtoToProductUnit(productUnitDto);
        ProductUnitDto createdDto = ProductUnitConverter.INSTANCE.productUnitToDto(productUnitRepository.save(productUnit));

        producer.publishUpdate(kafkaTopic, createdDto, oldDto);
        return createdDto;
    }

    @Override
    public void delete(Integer id) {

        ProductUnitDto deletedDto = ProductUnitConverter.INSTANCE.productUnitToDto(productUnitRepository.getReferenceById(id));
        productUnitRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    @Override
    public boolean exists(Integer id) {

        return productUnitRepository.existsById(id);
    }

}
