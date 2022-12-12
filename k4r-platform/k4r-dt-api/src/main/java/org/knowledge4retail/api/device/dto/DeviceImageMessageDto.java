package org.knowledge4retail.api.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowledge4retail.api.device.dto.ros.RosMetadata;
import org.knowledge4retail.api.device.dto.ros.Pose;
import org.knowledge4retail.api.shared.dto.BasicDto;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceImageMessageDto implements BasicDto {

    private Integer id;
    private RosMetadata rosMetadata;
    private Pose devicePose;
    private Pose cameraPose;
    private String labelName;
    private String labelId;
    private String imageName;
}
