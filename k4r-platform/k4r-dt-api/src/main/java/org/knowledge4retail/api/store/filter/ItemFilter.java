package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class ItemFilter {

    private FilterField id;
    private FilterField positionInFacingX;
    private FilterField positionInFacingY;
    private FilterField positionInFacingZ;
    private FilterField facingId;
    private FilterField externalReferenceId;
    private FilterField productUnitId;
}
