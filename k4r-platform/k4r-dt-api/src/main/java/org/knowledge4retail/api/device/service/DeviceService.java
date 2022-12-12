package org.knowledge4retail.api.device.service;

import org.knowledge4retail.api.device.dto.DeviceDto;

import java.util.List;

public interface DeviceService {

    List<DeviceDto> readAll();

    DeviceDto create(DeviceDto deviceDto);

    void delete(String id);

    boolean exists(String id);
}
