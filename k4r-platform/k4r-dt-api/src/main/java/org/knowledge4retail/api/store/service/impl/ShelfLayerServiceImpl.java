package org.knowledge4retail.api.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.ShelfLayerConverter;
import org.knowledge4retail.api.store.dto.ShelfLayerDto;
import org.knowledge4retail.api.store.filter.ShelfLayerFilter;
import org.knowledge4retail.api.store.model.ShelfLayer;
import org.knowledge4retail.api.store.repository.ShelfLayerRepository;
import org.knowledge4retail.api.store.service.ShelfLayerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShelfLayerServiceImpl implements ShelfLayerService {

    private final ShelfLayerRepository repository;
    private final DefaultProducer producer;

    @Value("${org.knowledge4retail.api.listener.kafka.topics.shelflayer}")
    private String kafkaTopic;

    @Override
    public ShelfLayerDto create(ShelfLayerDto dto) {

        dto.setId(null);
        ShelfLayerDto shelfLayerDto = ShelfLayerConverter.INSTANCE.shelfLayerToDto(
                repository.save(ShelfLayerConverter.INSTANCE.dtoToShelfLayer(dto)));

        producer.publishCreate(kafkaTopic, shelfLayerDto);
        return shelfLayerDto;
    }

    @Override
    public List<ShelfLayerDto> readByShelfId(Integer shelfId) {

        return ShelfLayerConverter.INSTANCE.shelfLayersToDtos(
                repository.findByShelfId(shelfId));
    }

    @Override
    public ShelfLayerDto read(Integer shelfLayerId) {

        return ShelfLayerConverter.INSTANCE.shelfLayerToDto(
                repository.getReferenceById(shelfLayerId));
    }

    @Override
    public List<ShelfLayerDto> readByShelfIdAndExternalReferenceId(Integer shelfId, String externalReferenceId) {

        ShelfLayerDto shelfLayerDto = ShelfLayerConverter.INSTANCE.shelfLayerToDto(
                repository.findByShelfIdAndExternalReferenceId(shelfId, externalReferenceId));
        List<ShelfLayerDto> shelfLayerDtos = new ArrayList<>();
        shelfLayerDtos.add(shelfLayerDto);

        return shelfLayerDtos;
    }

    @Override
    public ShelfLayerDto readOneShelfLayerByShelfIdAndExternalReferenceId(Integer shelfId, String externalReferenceId) {

        return ShelfLayerConverter.INSTANCE.shelfLayerToDto(
                repository.findByShelfIdAndExternalReferenceId(shelfId, externalReferenceId));
    }

    @Override
    public List<ShelfLayerDto> readByShelfLayerIds(List<Integer> shelfLayerIds) {

        return ShelfLayerConverter.INSTANCE.shelfLayersToDtos(repository.findByShelfLayerIds(shelfLayerIds));
    }

    @Override
    public ShelfLayerDto readByShelfIdAndExternalReferenceIdAndLevel(Integer shelfId, String externalReferenceId, Integer level) {

        return ShelfLayerConverter.INSTANCE.shelfLayerToDto(
                repository.findByShelfIdAndExternalReferenceIdAndLevel(shelfId, externalReferenceId, level));
    }

    @Override
    public void delete(Integer shelfLayerId) {

        ShelfLayerDto shelfLayerDto = read(shelfLayerId);
        repository.deleteById(shelfLayerId);
        producer.publishDelete(kafkaTopic, shelfLayerDto);
    }

    @Override
    public boolean exists(Integer shelfLayerId) {

        return repository.existsById(shelfLayerId);
    }

    @Override
    public boolean existsByExternalReferenceId(String externalReferenceId) {

        return repository.existsByExternalReferenceId(externalReferenceId);
    }

    @Override
    public boolean existsByExternalReferenceIdAndLevel(String externalReferenceId, Integer level) {

        return repository.existsByExternalReferenceIdAndLevel(externalReferenceId, level);
    }

    @Override
    public Map<Integer, List<ShelfLayer>> filterShelfLayer(Map<Object, Object> shelfContext) {

        log.info("Resolver called ShelfLayerService to get filtered shelfLayers");
        Set<Object> shelfIds = shelfContext.keySet();
        Map<Integer, List<ShelfLayer>> map = new HashMap<>();

        for (Object shelfObjectId : shelfIds) {

            Integer shelfId = Integer.parseInt(shelfObjectId.toString());
            ShelfLayerFilter filter = (ShelfLayerFilter) shelfContext.get(shelfId);
            addShelfLayerListToMap(shelfId, filter, map);
        }
        return map;
    }

    @Override
    @Transactional
    public ShelfLayerDto update(ShelfLayerDto shelfLayerDto) {

        ShelfLayerDto oldShelfLayer = read(shelfLayerDto.getId());
        ShelfLayerDto newShelfLayer = ShelfLayerConverter.INSTANCE.shelfLayerToDto(
                repository.save(ShelfLayerConverter.INSTANCE.dtoToShelfLayer(shelfLayerDto)));

        producer.publishUpdate(kafkaTopic, newShelfLayer, oldShelfLayer);
        return newShelfLayer;
    }

    private void addShelfLayerListToMap(Integer storeId, ShelfLayerFilter filter, Map<Integer, List<ShelfLayer>> map) {

        List<ShelfLayer> shelfLayers;
        if (filter == null) {

            shelfLayers = repository.findByShelfId(storeId);
        } else {

            Specification<ShelfLayer> spec = null;
            try {
                spec = getSpecification(filter, null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            shelfLayers = repository.findAll(spec);
        }

        map.put(storeId, shelfLayers);
    }
}
