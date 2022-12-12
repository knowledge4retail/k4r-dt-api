package org.knowledge4retail.api.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowledge4retail.api.shared.dto.BasicDto;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StoreAggregateDto implements BasicDto {
    private Integer id;
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
    private Long shelfCount;
    private Long shelfLayerCount;
    private Long barcodeCount;
    private Long productCount;
}
