package org.knowledge4retail.api.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;

@Data
public class ProductGtinDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;
    private String gtin;
    @Schema(description = "FK")
    private Integer productUnitId;
    private String gtinType;
    private Boolean mainGtinFlag;
    private String externalReferenceId;
}
