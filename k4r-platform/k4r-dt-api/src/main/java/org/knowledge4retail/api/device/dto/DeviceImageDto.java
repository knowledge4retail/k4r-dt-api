package org.knowledge4retail.api.device.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowledge4retail.api.device.dto.ros.RosMetadata;
import org.knowledge4retail.api.device.dto.ros.Pose;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceImageDto {

    private Integer id;

    @Valid
    private RosMetadata rosMetadata;

    @Valid
    @JsonProperty("device_pose")
    @Schema(required = true, description = "notNull")
    private Pose devicePose;

    @Valid
    @JsonProperty("camera_pose")
    @Schema(required = true, description = "notNull")
    private Pose cameraPose;

    @JsonProperty("label_name")
    @NotEmpty
    @Schema(required = true, description = "notEmpty")
    private String labelName;

    @JsonProperty("label_id")
    @NotEmpty
    @Schema(required = true, description = "notEmpty")
    private String labelId;

    private String imageName;

    private byte[] image;
}
