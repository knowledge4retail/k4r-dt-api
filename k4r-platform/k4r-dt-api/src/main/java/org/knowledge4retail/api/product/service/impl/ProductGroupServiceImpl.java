package org.knowledge4retail.api.product.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.converter.ProductGroupConverter;
import org.knowledge4retail.api.product.dto.ProductGroupDto;
import org.knowledge4retail.api.product.exception.ProductInGroupAssignmentException;
import org.knowledge4retail.api.product.model.Product;
import org.knowledge4retail.api.product.model.ProductGroup;
import org.knowledge4retail.api.product.repository.ProductGroupRepository;
import org.knowledge4retail.api.product.service.ProductGroupService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.service.StoreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductGroupServiceImpl implements ProductGroupService {

    private final ProductGroupRepository repository;
    private final DefaultProducer producer;
    @Value("${org.knowledge4retail.api.listener.kafka.topics.productgroup}")
    private String kafkaTopic;

    public ProductGroupServiceImpl(ProductGroupRepository repository, DefaultProducer producer, StoreService storeService) {

        this.repository = repository;
        this.producer = producer;
    }

    @Override
    public ProductGroupDto create(ProductGroupDto dto) {

        try {
            ProductGroupDto createdDto = ProductGroupConverter.INSTANCE.productGroupToDto(
                    repository.save(
                            ProductGroupConverter.INSTANCE.dtoToProductGroup(dto)));
            producer.publishCreate(kafkaTopic, createdDto);
            return createdDto;
        } catch (DataIntegrityViolationException e) {
            String message = String.format("Group name already exists for this store or duplicate products in group detected. Please refer to exception details. Payload: %1$s", dto.toString());
            log.warn(message);
            throw new ProductInGroupAssignmentException(message);
        }
    }

    @Override
    public List<ProductGroupDto> readByStoreId(Integer storeId) {

        return ProductGroupConverter.INSTANCE.productGroupsToDtos(
                repository.findByStoreId(storeId));
    }

    @Override
    public ProductGroupDto read(Integer productGroupId) {

        return ProductGroupConverter.INSTANCE.productGroupToDto(
                repository.getReferenceById(productGroupId));
    }

    @Override
    public void delete(Integer productGroupId) {

        ProductGroupDto deletedDto = read(productGroupId);
        repository.deleteById((productGroupId));
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    @Override
    public ProductGroupDto updateAddProductToGroup(Integer productGroupId, String productId) {

        ProductGroup group = repository.getReferenceById(productGroupId);
        ProductGroupDto oldDto = ProductGroupConverter.INSTANCE.productGroupToDto(group);

        if (group.getProducts().stream().anyMatch(p -> p.getId().equals(productId))) {
            throw new ProductInGroupAssignmentException(String.format("product %1$s already in group %2$s", productId, productGroupId));
        }

        Product p = new Product();
        p.setId(productId);
        group.getProducts().add(p);

        ProductGroupDto updatedDto = ProductGroupConverter.INSTANCE.productGroupToDto(
                repository.save(group));

        producer.publishUpdate(kafkaTopic, updatedDto, oldDto);
        return updatedDto;
    }

    @Override
    public ProductGroupDto updateRemoveProductFromGroup(Integer productGroupId, String productId) {

        ProductGroup group = repository.getReferenceById(productGroupId);
        ProductGroupDto oldDto = ProductGroupConverter.INSTANCE.productGroupToDto(group);

        if (group.getProducts().stream().noneMatch(p -> p.getId().equals(productId))) {
            throw new ProductInGroupAssignmentException(String.format("product %1$s not in group %2$s", productId, productGroupId));
        }

        group.getProducts().removeIf(p -> p.getId().equals(productId));

        ProductGroupDto updatedDto = ProductGroupConverter.INSTANCE.productGroupToDto(
                repository.save(group));

        producer.publishUpdate(kafkaTopic, updatedDto, oldDto);
        return updatedDto;
    }

    @Override
    public boolean exists(Integer productGroupId) {

        if (productGroupId == null) {
            return false;
        }
        return repository.existsById(productGroupId);
    }

}
