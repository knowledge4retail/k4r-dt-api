package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ShelfDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;

    private Double positionX;

    private Double positionY;

    private Double positionZ;

    private Double orientationX;

    private Double orientationY;

    private Double orientationZ;

    private Double orientationW;

    @NotNull
    @Schema(required = true, description = "notNull")
    private Double width;

    @NotNull
    @Schema(required = true, description = "notNull")
    private Double height;

    @NotNull
    @Schema(required = true, description = "notNull")
    private Double depth;

    @NotNull
    @Schema(required = true, description = "notNull")
    private Integer lengthUnitId;

    @Schema(description = "FK")
    private Integer storeId;

    @Schema(description = "FK")
    private Integer productGroupId;

    private String cadPlanId;

    private Integer blockId;

    private Integer runningNumber;

    private String externalReferenceId;
}
