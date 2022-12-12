package org.knowledge4retail.api.product.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class ProductUnitFilter {

    private FilterField id;
    private FilterField productId;
    private FilterField unitCode;
    private FilterField numeratorBaseUnit;
    private FilterField denominatorBaseUnit;
    private FilterField length;
    private FilterField width;
    private FilterField height;
    private FilterField dimensionUnit;
    private FilterField volume;
    private FilterField volumeUnit;
    private FilterField netWeight;
    private FilterField grossWeight;
    private FilterField weightUnit;
    private FilterField maxStackSize;
}
