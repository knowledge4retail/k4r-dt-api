package org.knowledge4retail.api.store.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.FacingConverter;
import org.knowledge4retail.api.store.dto.FacingDto;
import org.knowledge4retail.api.store.filter.FacingFilter;
import org.knowledge4retail.api.store.model.Facing;
import org.knowledge4retail.api.store.repository.FacingRepository;
import org.knowledge4retail.api.store.service.FacingService;
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
public class FacingServiceImpl implements FacingService {

    private final FacingRepository repository;

    private final DefaultProducer producer;
    @Value("${org.knowledge4retail.api.listener.kafka.topics.facing}")
    private String kafkaTopic;

    public FacingServiceImpl(FacingRepository repository, DefaultProducer producer) {

        this.repository = repository;
        this.producer = producer;
    }

    @Override
    public FacingDto create(FacingDto dto) {

        dto.setId(null);
        FacingDto createdDto = FacingConverter.INSTANCE.facingToDto(
                repository.save(
                        FacingConverter.INSTANCE.dtoToFacing(dto)));

        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public List<FacingDto> readByShelfLayerId(int shelfLayerId) {

        return FacingConverter.INSTANCE.facingsToDtos(
                repository.findByShelfLayerId(shelfLayerId));
    }

    @Override
    public List<FacingDto> readByProductUnitId(Integer productUnitId) {

        return FacingConverter.INSTANCE.facingsToDtos(
                repository.findByProductUnitId(productUnitId));
    }

    @Override
    public FacingDto read(Integer facingId) {

        return FacingConverter.INSTANCE.facingToDto(repository.getReferenceById(facingId));
    }

    @Override
    public int delete(int facingId) {

        FacingDto deletedDto = read(facingId);
        repository.deleteById(facingId);

        producer.publishDelete(kafkaTopic, deletedDto);
        return facingId;
    }

    @Override
    public boolean exists(Integer facingId) {

        return repository.existsById(facingId);
    }


    @Override
    public FacingDto update(int facingId, FacingDto facingDto) {

        FacingDto oldDto = FacingConverter.INSTANCE.facingToDto(this.repository.getReferenceById(facingId));
        facingDto.setId(facingId);

        Facing facing = FacingConverter.INSTANCE.dtoToFacing(facingDto);
        FacingDto createdDto = FacingConverter.INSTANCE.facingToDto(this.repository.save(facing));

        producer.publishUpdate(kafkaTopic, createdDto, oldDto);
        return createdDto;
    }

    @Override
    public Map<Integer, List<Facing>> filterFacing(Map<Object, Object> shelfLayerContext) {

        log.info("Resolver called PositionService to get filtered facings");
        Set<Object> shelfLayerIds = shelfLayerContext.keySet();
        Map<Integer, List<Facing>> map = new HashMap<>();

        for (Object shelfLayerObjectId : shelfLayerIds) {

            Integer shelfLayerId = Integer.parseInt(shelfLayerObjectId.toString());
            FacingFilter filter = (FacingFilter) shelfLayerContext.get(shelfLayerId);
            addFacingListToMap(shelfLayerId, filter, map);
        }
        return map;
    }

    private void addFacingListToMap(Integer shelfLayerId, FacingFilter filter, Map<Integer, List<Facing>> map) {

        List<Facing> facings;
        if (filter == null) {

            facings = repository.findByShelfLayerId(shelfLayerId);
        } else {

            Specification<Facing> spec = null;
            try {
                spec = getSpecification(filter, null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            facings = repository.findAll(spec);
        }

        map.put(shelfLayerId, facings);
    }
}
