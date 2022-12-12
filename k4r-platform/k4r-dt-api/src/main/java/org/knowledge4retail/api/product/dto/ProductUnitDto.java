package org.knowledge4retail.api.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotNull;

@Data
public class ProductUnitDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;
    @Schema(description = "FK")
    private String productId;
    private String unitCode;
    private Integer numeratorBaseUnit;
    private Integer denominatorBaseUnit;
    @NotNull
    @Schema(required = true, description = "notNull")
    private Double length;
    @NotNull
    @Schema(required = true, description = "notNull")
    private Double width;
    @NotNull
    @Schema(required = true, description = "notNull")
    private Double height;
    @Schema(description = "FK")
    private Integer dimensionUnit;
    private Double volume;
    @Schema(description = "FK")
    private Integer volumeUnit;
    private Double netWeight;
    private Double grossWeight;
    @Schema(description = "FK")
    private Integer weightUnit;
    private Integer maxStackSize;
}
