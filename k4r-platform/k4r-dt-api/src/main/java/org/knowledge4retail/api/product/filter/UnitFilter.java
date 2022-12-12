package org.knowledge4retail.api.product.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class UnitFilter {

    private FilterField id;
    private FilterField name;
    private FilterField type;
    private FilterField symbol;
}
