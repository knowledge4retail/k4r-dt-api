package org.knowledge4retail.api.product.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.converter.MaterialGroupConverter;
import org.knowledge4retail.api.product.dto.MaterialGroupDto;
import org.knowledge4retail.api.product.model.MaterialGroup;
import org.knowledge4retail.api.product.repository.MaterialGroupRepository;
import org.knowledge4retail.api.product.service.MaterialGroupService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MaterialGroupServiceImpl implements MaterialGroupService {

    private final MaterialGroupRepository materialGroupRepository;
    private final DefaultProducer producer;
    @Value("${org.knowledge4retail.api.listener.kafka.topics.materialgroup}")
    private String kafkaTopic;

    public MaterialGroupServiceImpl(MaterialGroupRepository materialGroupRepository, DefaultProducer producer) {

        this.materialGroupRepository = materialGroupRepository;
        this.producer = producer;
    }

    @Override
    public List<MaterialGroupDto> readAll() {

        return MaterialGroupConverter.INSTANCE.materialGroupsToDtos(materialGroupRepository.findAll());
    }

    @Override
    public MaterialGroupDto read(Integer id) {

        MaterialGroup materialGroup = materialGroupRepository.getReferenceById(id);
        return MaterialGroupConverter.INSTANCE.materialGroupToDto(materialGroup);
    }

    @Override
    public MaterialGroupDto create(MaterialGroupDto materialGroupDto) {

        materialGroupDto.setId(null);
        MaterialGroup materialToSave = MaterialGroupConverter.INSTANCE.dtoToMaterialGroup(materialGroupDto);
        MaterialGroup savedMaterial = materialGroupRepository.save(materialToSave);

        MaterialGroupDto createdDto = MaterialGroupConverter.INSTANCE.materialGroupToDto(savedMaterial);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public MaterialGroupDto update(Integer id, MaterialGroupDto materialGroupDto) {

        MaterialGroupDto oldDto = read(id);
        materialGroupDto.setId(id);

        MaterialGroup materialGroup = MaterialGroupConverter.INSTANCE.dtoToMaterialGroup(materialGroupDto);
        MaterialGroupDto createdDto = MaterialGroupConverter.INSTANCE.materialGroupToDto(materialGroupRepository.save(materialGroup));

        producer.publishUpdate(kafkaTopic, createdDto, oldDto);
        return createdDto;
    }

    @Override
    public void delete(Integer id) {

        MaterialGroupDto deletedDto = MaterialGroupConverter.INSTANCE.materialGroupToDto(materialGroupRepository.getReferenceById(id));
        materialGroupRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    @Override
    public boolean exists(Integer id) {

        return materialGroupRepository.existsById(id);
    }
}
