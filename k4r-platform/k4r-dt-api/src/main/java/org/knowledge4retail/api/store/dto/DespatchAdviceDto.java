package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class DespatchAdviceDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;
    @NotNull
    @Schema(required = true, description = "FK.notNull")
    private Integer storeId;
    private OffsetDateTime shipTime;
    private OffsetDateTime creationTime;
    private String senderQualifier;
    private String senderId;
    private String documentNumber;
}
