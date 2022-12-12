package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowledge4retail.api.shared.dto.BasicDto;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BarcodeDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;

    @Schema(description = "FK")
    private Integer productGtinId;

    @Schema(description = "FK")
    private Integer shelfLayerId;

    private Double positionX;

    private Double positionY;

    private Double positionZ;

    private Double orientationX;

    private Double orientationY;

    private Double orientationZ;

    private Double orientationW;

    private Integer lengthUnitId;
}
