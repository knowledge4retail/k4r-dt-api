package org.knowledge4retail.api.product.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class ProductDescriptionFilter {

    private FilterField id;
    private FilterField productId;
    private FilterField description;
    private FilterField isoLanguageCode;
}
