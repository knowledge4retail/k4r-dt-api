package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StoreObjectDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;

    @NotNull
    @Schema(required = true, description = "notNull")
    private String type;

    private String description;

    private OffsetDateTime locationTimestamp;

    private Double positionX;

    private Double positionY;

    private Double positionZ;

    private Double orientationX;

    private Double orientationY;

    private Double orientationZ;

    private Double orientationW;

    @NotNull
    @Schema(required = true, description = "notNull")
    private Integer width;

    @NotNull
    @Schema(required = true, description = "notNull")
    private Integer height;

    @NotNull
    @Schema(required = true, description = "notNull")
    private Integer depth;

    @NotNull
    @Schema(required = true, description = "notNull")
    private Integer lengthUnitId;

    @Schema(description = "FK")
    private Integer storeId;
}
