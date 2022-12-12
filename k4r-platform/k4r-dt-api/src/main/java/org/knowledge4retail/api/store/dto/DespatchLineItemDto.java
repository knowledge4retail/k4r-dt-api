package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;


@Data
public class DespatchLineItemDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;
    @Schema(description = "FK")
    private Integer despatchLogisticUnitId;
    @Schema(description = "FK")
    private String productId;
    private String requestedProductId;
    private String lineItemNumber;
    private Integer despatchQuantity;
}
