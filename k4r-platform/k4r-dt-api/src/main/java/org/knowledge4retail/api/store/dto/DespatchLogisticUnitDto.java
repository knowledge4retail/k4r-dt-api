package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;

import java.time.OffsetDateTime;

@Data
public class DespatchLogisticUnitDto implements BasicDto {

    private Integer id;
    @Schema(description = "FK")
    private Integer parentId;
    @Schema(description = "FK")
    private Integer despatchAdviceId;
    private String packageTypeCode;
    private String logisticUnitId;
    private OffsetDateTime estimatedDelivery;
}
