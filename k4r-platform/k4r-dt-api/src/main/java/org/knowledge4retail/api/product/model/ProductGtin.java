package org.knowledge4retail.api.product.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "product_gtin")
public class ProductGtin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "gtin")
    private String gtin;
    @Column(name = "product_unit_id")
    private Integer productUnitId;
    @Column(name = "gtin_type")
    private String gtinType;
    @Column(name = "main_gtin_flag")
    private Boolean mainGtinFlag;
    @Column(name = "external_reference_id")
    private String externalReferenceId;
}
