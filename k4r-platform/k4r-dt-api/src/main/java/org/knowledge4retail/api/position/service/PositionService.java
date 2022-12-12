package org.knowledge4retail.api.position.service;

import org.knowledge4retail.api.position.dto.PositionDto;
import org.knowledge4retail.api.store.dto.FacingDto;

import java.util.List;

public interface PositionService {

    FacingDto getFacingByExternalReferenceIdAndGtin(String externalReferenceId, String gtin);

    List<PositionDto> getAllPositionsByStoreIdAndGtin(Integer storeId, String gtin);
}
