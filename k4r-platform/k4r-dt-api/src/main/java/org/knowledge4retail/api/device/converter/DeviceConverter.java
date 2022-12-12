package org.knowledge4retail.api.device.converter;

import org.knowledge4retail.api.device.dto.DeviceDto;
import org.knowledge4retail.api.device.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceConverter {

    DeviceConverter INSTANCE = Mappers.getMapper(DeviceConverter.class);

    Device dtoToDevice(DeviceDto deviceDto);

    DeviceDto deviceToDto(Device device);

    List<DeviceDto> devicesToDtos(List<Device> devices);
}
