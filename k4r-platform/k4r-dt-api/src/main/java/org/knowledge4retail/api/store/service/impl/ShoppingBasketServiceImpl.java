package org.knowledge4retail.api.store.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.customer.service.CustomerService;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.ShoppingBasketPositionConverter;
import org.knowledge4retail.api.store.dto.ShoppingBasketPositionDto;
import org.knowledge4retail.api.store.exception.ShoppingBasketPositionNotFoundException;
import org.knowledge4retail.api.store.exception.ShoppingBasketPositionPatchException;
import org.knowledge4retail.api.store.model.ShoppingBasketPosition;
import org.knowledge4retail.api.store.repository.ShoppingBasketRepository;
import org.knowledge4retail.api.store.service.ShoppingBasketService;
import org.knowledge4retail.api.store.service.StoreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class ShoppingBasketServiceImpl implements ShoppingBasketService {

    private final ShoppingBasketRepository repository;

    private final DefaultProducer producer;

    @Value("${org.knowledge4retail.api.listener.kafka.topics.basketposition}")
    private String kafkaTopic;

    public ShoppingBasketServiceImpl(ShoppingBasketRepository repository, DefaultProducer producer, CustomerService customerService, ProductService productService, StoreService storeService) {

        this.repository = repository;
        this.producer = producer;
    }

    @Override
    @Transactional
    public ShoppingBasketPositionDto create(ShoppingBasketPositionDto dto) {

        dto.setId(null);
        ShoppingBasketPositionDto createdDto = ShoppingBasketPositionConverter.INSTANCE.positionToDto(
                repository.save(ShoppingBasketPositionConverter.INSTANCE.dtoToPosition(dto)));

        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public List<ShoppingBasketPositionDto> readByStoreIdAndCustomerId(Integer storeId, Integer customerId) {

        List<ShoppingBasketPosition> res = repository.findByStoreIdAndCustomerId(storeId, customerId);
        return ShoppingBasketPositionConverter.INSTANCE.positionsToDtos(res);
    }

    @Override
    public ShoppingBasketPositionDto read(Integer positionId) {

        return ShoppingBasketPositionConverter.INSTANCE.positionToDto(repository.getReferenceById(positionId));
    }

    @Override
    public void deleteByStoreIdAndCustomerId(Integer storeId, Integer customerId) {

        List<ShoppingBasketPosition> toDelete = repository.findByStoreIdAndCustomerId(storeId, customerId);
        if (toDelete.isEmpty()) {

            throw new ShoppingBasketPositionNotFoundException();
        }
        repository.deleteAll(toDelete);
        for (ShoppingBasketPosition position: toDelete) {

            producer.publishDelete(
                    kafkaTopic, ShoppingBasketPositionConverter.INSTANCE.positionToDto(position));
        }
    }

    @Override
    public void delete(Integer positionId) {

        ShoppingBasketPositionDto shoppingBasketPositionDto = read(positionId);

        repository.deleteById(positionId);
        producer.publishDelete(kafkaTopic, shoppingBasketPositionDto);
    }

    @Override
    public boolean exists(Integer positionId) {

        return repository.existsById(positionId);
    }

    @Override
    public ShoppingBasketPositionDto update(Integer positionId, JsonPatch patch) {

        try {
            ShoppingBasketPosition oldPosition = repository.findById(positionId).orElseThrow(ShoppingBasketPositionNotFoundException::new);

            ShoppingBasketPositionDto oldPositionDto = ShoppingBasketPositionConverter.INSTANCE.positionToDto(oldPosition);

            ShoppingBasketPosition newPosition = applyPatch(oldPosition, patch);
            repository.save(newPosition);
            ShoppingBasketPositionDto newPositionDto = ShoppingBasketPositionConverter.INSTANCE.positionToDto(newPosition);

            producer.publishUpdate(kafkaTopic, newPositionDto, oldPositionDto);

            return newPositionDto;
        } catch (JsonPatchException | JsonProcessingException e) {

            log.error(String.format("error patching shopping basket with payload %1$s", patch.toString()));
            throw new ShoppingBasketPositionPatchException();
        }
    }

    public ShoppingBasketPosition applyPatch(ShoppingBasketPosition position, JsonPatch patch) throws JsonPatchException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(position, JsonNode.class));

        return objectMapper.treeToValue(patched, ShoppingBasketPosition.class);
    }
}
