package org.knowledge4retail.api.store.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_number")
    private String storeNumber;

    @Column(name = "address_country")
    private String addressCountry;

    @Column(name = "address_state")
    private String addressState;

    @Column(name = "address_city")
    private String addressCity;

    @Column(name = "address_postcode")
    private String addressPostcode;

    @Column(name = "address_street")
    private String addressStreet;

    @Column(name = "address_street_number")
    private String addressStreetNumber;

    @Column(name = "address_additional")
    private String addressAdditional;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "cad_plan_id")
    private String cadPlanId;

    @Column(name = "external_reference_id")
    private String externalReferenceId;
}
