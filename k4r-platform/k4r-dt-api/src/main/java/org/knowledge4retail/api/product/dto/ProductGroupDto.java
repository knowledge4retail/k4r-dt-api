package org.knowledge4retail.api.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductGroupDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;
    @NotBlank
    @Schema(required = true, description = "notBlank")
    private String name;
    @Schema(description = "FK")
    private Integer storeId;

    private List<ProductDto> products;
}
