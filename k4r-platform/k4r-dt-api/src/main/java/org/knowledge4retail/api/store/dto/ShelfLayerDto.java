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
public class ShelfLayerDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;

    private Integer shelfId;

    private Integer level;

    private String type;

    private Double positionZ;

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

    private String externalReferenceId;
}
