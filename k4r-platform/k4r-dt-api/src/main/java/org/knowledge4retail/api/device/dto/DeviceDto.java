package org.knowledge4retail.api.device.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;

@Data
public class DeviceDto implements BasicDto {

    @Schema(description = "PK")
    private String id;
    @Schema(description = "FK", required = true)
    private Integer storeId;
    private String deviceType;
    private String description;
}
