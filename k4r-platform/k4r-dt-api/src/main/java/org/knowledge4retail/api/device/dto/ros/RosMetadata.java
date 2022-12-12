package org.knowledge4retail.api.device.dto.ros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RosMetadata {

    @NotNull
    private Long timestamp;

    @NotBlank
    private String frameId;

    @NotBlank
    private String deviceId;
}
