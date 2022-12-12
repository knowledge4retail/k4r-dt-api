package org.knowledge4retail.api.device.dto;

import lombok.Data;
import org.knowledge4retail.api.device.dto.ros.RosMetadata;
import org.knowledge4retail.api.device.dto.ros.Pose;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class DeviceStatusDto implements BasicDto {

    @Valid
    RosMetadata rosMetadata;


    @Valid
    @NotNull
    Pose pose;

}
