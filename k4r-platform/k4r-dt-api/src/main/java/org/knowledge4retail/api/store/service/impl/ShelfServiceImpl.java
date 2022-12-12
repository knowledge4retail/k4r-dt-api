package org.knowledge4retail.api.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.ShelfConverter;
import org.knowledge4retail.api.store.dto.ShelfDto;
import org.knowledge4retail.api.store.filter.ShelfFilter;
import org.knowledge4retail.api.store.model.Shelf;
import org.knowledge4retail.api.store.repository.ShelfLayerRepository;
import org.knowledge4retail.api.store.repository.ShelfRepository;
import org.knowledge4retail.api.store.service.ShelfService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;


@Slf4j
@RequiredArgsConstructor
@Service
public class ShelfServiceImpl implements ShelfService {

    private final ShelfRepository repository;
    private final ShelfLayerRepository shelfLayerRepository;

    private final DefaultProducer producer;

    @Value("${org.knowledge4retail.api.listener.kafka.topics.shelf}")
    private String kafkaTopic;

    @Override
    public ShelfDto create(ShelfDto dto) {

        dto.setId(null);
        ShelfDto shelfDto = ShelfConverter.INSTANCE.shelfToDto(
                repository.save(ShelfConverter.INSTANCE.dtoToShelf(dto)));

        producer.publishCreate(kafkaTopic, shelfDto);
        return shelfDto;
    }

    @Override
    public List<ShelfDto> readByStoreId(Integer storeId) {

        return ShelfConverter.INSTANCE.shelvesToDtos(
                repository.findByStoreId(storeId));
    }

    @Override
    public ShelfDto read(Integer shelfId) {

        return ShelfConverter.INSTANCE.shelfToDto(
                repository.getReferenceById(shelfId));
    }

    @Override
    public List<ShelfDto> readByStoreIdAndExternalReferenceId(Integer storeId, String externalReferenceId) {

        return ShelfConverter.INSTANCE.shelvesToDtos(
                repository.findByStoreIdAndExternalReferenceId(storeId, externalReferenceId));
    }

    @Override
    public List<ShelfDto> readByShelfIdsAndStoreId(List<Integer> shelfIds, Integer storeId) {

        return ShelfConverter.INSTANCE.shelvesToDtos(repository.findByShelfIdsAndStoreId(shelfIds, storeId));
    }

    @Override
    public ShelfDto readByExternalReferenceId(String externalReferenceId) {

        return ShelfConverter.INSTANCE.shelfToDto(
                repository.findByExternalReferenceId(externalReferenceId));
    }

    @Override
    public ShelfDto update(ShelfDto dto) {

        ShelfDto oldShelf = read(dto.getId());
        ShelfDto newShelf = ShelfConverter.INSTANCE.shelfToDto(
                repository.save(ShelfConverter.INSTANCE.dtoToShelf(dto)));

        producer.publishUpdate(kafkaTopic, newShelf, oldShelf);
        return newShelf;
    }

    @Override
    public void delete(Integer shelfId) {

        Shelf shelf = repository.getReferenceById(shelfId);
        shelfLayerRepository.deleteAll(shelf.getShelfLayers());
        repository.deleteById(shelfId);
        producer.publishDelete(kafkaTopic, ShelfConverter.INSTANCE.shelfToDto(shelf));
    }

    @Override
    public boolean existsByExternalReferenceId(String externalReferenceId) {

        return repository.existsByExternalReferenceId(externalReferenceId);
    }

    @Override
    public boolean exists(Integer shelfId) {

        return repository.existsById(shelfId);
    }

    @Override
    public Map<Integer, List<Shelf>> filterShelf(Map<Object, Object> storeContext) {

        log.info("Resolver called ShelfService to get filtered shelves");
        Set<Object> storeIds = storeContext.keySet();
        Map<Integer, List<Shelf>> map = new HashMap<>();

        for (Object storeObjectId : storeIds) {

            Integer storeId = Integer.parseInt(storeObjectId.toString());
            ShelfFilter filter = (ShelfFilter) storeContext.get(storeId);
            addShelfListToMap(storeId, filter, map);
        }
        return map;
    }

    private void addShelfListToMap(Integer storeId, ShelfFilter filter, Map<Integer, List<Shelf>> map) {

        List<Shelf> shelves;
        if (filter == null) {

            shelves = repository.findByStoreId(storeId);
        } else {

            Specification<Shelf> spec = null;
            try {
                spec = getSpecification(filter, null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            shelves = repository.findAll(spec);
        }

        map.put(storeId, shelves);
    }
}
