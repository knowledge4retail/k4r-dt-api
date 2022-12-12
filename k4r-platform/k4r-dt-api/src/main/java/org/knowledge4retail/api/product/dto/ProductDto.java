package org.knowledge4retail.api.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotBlank;

@Data
public class ProductDto implements BasicDto {

    @Schema(required = true, description = "notBlank")
    @NotBlank
    private String id;
    @Schema(description = "FK")
    private Integer materialGroupId;
    private String productType;
    private String productBaseUnit;
}
