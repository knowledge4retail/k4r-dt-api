package org.knowledge4retail.api.customer.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class CustomerFilter {

    private FilterField id;
    private FilterField anonymisedName;
}
