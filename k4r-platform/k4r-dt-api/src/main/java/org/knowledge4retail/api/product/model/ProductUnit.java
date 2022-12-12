package org.knowledge4retail.api.product.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "product_unit")
public class ProductUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "product_id")
    private String productId;
    @Column(name = "unit_code")
    private String unitCode;
    @Column(name = "numerator_base_unit")
    private Integer numeratorBaseUnit;
    @Column(name = "denominator_base_unit")
    private Integer denominatorBaseUnit;
    @NotNull
    @Column(name = "length")
    private Double length;
    @NotNull
    @Column(name = "width")
    private Double width;
    @NotNull
    @Column(name = "height")
    private Double height;
    @Column(name = "dimension_unit")
    private Integer dimensionUnit;
    @Column(name = "volume")
    private Double volume;
    @Column(name = "volume_unit")
    private Integer volumeUnit;
    @Column(name = "net_weight")
    private Double netWeight;
    @Column(name = "gross_weight")
    private Double grossWeight;
    @Column(name = "weight_unit")
    private Integer weightUnit;
    @Column(name = "max_stack_size")
    private Integer maxStackSize;
}
