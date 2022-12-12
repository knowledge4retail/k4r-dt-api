package org.knowledge4retail.api.device.converter;

import org.knowledge4retail.api.device.dto.Map2dDto;
import org.knowledge4retail.api.device.dto.Map2dMessageDto;
import org.knowledge4retail.api.device.model.Map2d;

public interface Map2dConverter {
    Map2d dtoToModel(Map2dDto map2dDto);

    Map2dDto modelToDto(Map2d map2dEntity);

    Map2dMessageDto dtoToMessageDto(Map2dDto dto);
}
