package org.knowledge4retail.api.device.service;

import org.knowledge4retail.api.device.dto.DeviceStatusDto;
import org.knowledge4retail.api.device.model.DeviceStatus;

import java.util.List;

public interface DeviceStatusService {

    DeviceStatus create(String robotStatus);

    List<DeviceStatusDto> readAll(String deviceId);
}
