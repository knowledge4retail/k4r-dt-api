package org.knowledge4retail.api.position.dto;

import lombok.Builder;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;
import org.knowledge4retail.api.store.dto.BarcodeDto;
import org.knowledge4retail.api.store.dto.FacingDto;
import org.knowledge4retail.api.store.dto.ShelfDto;
import org.knowledge4retail.api.store.dto.ShelfLayerDto;

@Data
@Builder
public class PositionDto implements BasicDto {

    private BarcodeDto barcodeDto;

    private FacingDto facingDto;

    private ShelfLayerDto shelfLayerDto;

    private ShelfDto shelfDto;

    private String gtin;
}
