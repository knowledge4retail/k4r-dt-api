package org.knowledge4retail.api.device.service;

import org.knowledge4retail.api.device.dto.DeviceImageDto;
import org.knowledge4retail.api.device.model.DeviceImage;

import java.io.InputStream;
import java.util.List;

public interface DeviceImageService {

    DeviceImage create(DeviceImageDto deviceImageDto);

    List<DeviceImageDto> readByLabelTypeAndLabelId(String labelType, String labelId);

    InputStream read(int id);
}
