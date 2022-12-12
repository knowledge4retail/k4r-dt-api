package org.knowledge4retail.api.product.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "product_property")
public class ProductProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @NotBlank
    @Column(name = "product_id")
    private String productId;
    @NotNull
    @Column(name = "characteristic_id")
    private Integer characteristicId;
    @Column(name = "store_id")
    private Integer storeId;
    @NotBlank
    @Column(name = "value_low")
    private String valueLow;
    @Column(name = "value_high")
    private String valueHigh;
}
