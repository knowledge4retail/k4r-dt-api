package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;

@Data
public class ItemDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;
    private Integer positionInFacingX;
    private Integer positionInFacingY;
    private Integer positionInFacingZ;
    @Schema(description = "FK")
    private Integer facingId;
    private String externalReferenceId;
    private Integer productUnitId;
}
