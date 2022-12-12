package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ShoppingBasketPositionDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;

    @Schema(required = true, description = "FK.notNull")
    private Integer storeId;

    @NotNull
    @Schema(required = true, description = "FK.notNull")
    private Integer customerId;

    @NotBlank
    @Schema(required = true, description = "FK.notBlank")
    private String productId;

    private BigDecimal sellingPrice;

    @NotNull
    @Schema(required = true, description = "notNull")
    private Integer quantity;

    private String currency;
}
