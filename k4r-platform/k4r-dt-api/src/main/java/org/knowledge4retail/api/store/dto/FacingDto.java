package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowledge4retail.api.shared.dto.BasicDto;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacingDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;
    @Schema(description = "FK")
    private Integer shelfLayerId;
    private Integer layerRelativePosition;
    private Integer noOfItemsWidth;
    private Integer noOfItemsDepth;
    private Integer noOfItemsHeight;
    private Integer minStock;
    private Integer stock;
    private Integer misplacedStock;
    private Integer productUnitId;
    private String externalReferenceId;
}
