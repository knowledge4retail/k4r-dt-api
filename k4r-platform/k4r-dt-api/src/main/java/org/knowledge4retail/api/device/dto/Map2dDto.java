package org.knowledge4retail.api.device.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowledge4retail.api.device.dto.ros.RosMetadata;
import org.knowledge4retail.api.device.dto.ros.Pose;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Map2dDto {

    @Schema(description = "PK")
    private Integer id;

    @NotNull
    @Schema(required = true, description = "notNull")
    private RosMetadata rosMetadata;

    @NotNull
    @Schema(required = true, description = "notNull")
    private Double resolution;

    @NotNull
    @Schema(required = true, description = "notNull")
    private Double width;

    @NotNull
    @Schema(required = true, description = "notNull")
    private Double height;

    @NotNull
    @Schema(required = true, description = "notNull")
    private Integer lengthUnitId;

    @NotNull
    @Schema(required = true, description = "notNull")
    private Pose pose;

    @NotNull
    @Schema(description = "notNull")
    private int[] data;

    @Schema(required = true, description = "FK.notNull")
    private Integer storeId;
}
