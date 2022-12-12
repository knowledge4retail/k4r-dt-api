package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class StoreFilter {

    private FilterField id;
    private FilterField storeName;
    private FilterField storeNumber;
    private FilterField addressCountry;
    private FilterField addressState;
    private FilterField addressCity;
    private FilterField addressPostcode;
    private FilterField addressStreet;
    private FilterField addressStreetNumber;
    private FilterField addressAdditional;
    private FilterField longitude;
    private FilterField latitude;
    private FilterField cadPlanId;
    private FilterField externalReferenceId;
}
