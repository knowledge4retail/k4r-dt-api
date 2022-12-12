package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StoreDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;

    @NotBlank
    @Schema(required = true, description = "notBlank")
    private String storeName;

    private String storeNumber;

    private String addressCountry;

    private String addressState;

    private String addressCity;

    private String addressPostcode;

    private String addressStreet;

    private String addressStreetNumber;

    private String addressAdditional;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String cadPlanId;

    private String externalReferenceId;
}
