package org.knowledge4retail.api.store.model;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="barcode")
public class Barcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "product_gtin_id")
    private Integer productGtinId;

    @Column(name = "shelf_layer_id")
    private Integer shelfLayerId;

    @Column(name = "position_x")
    private Double positionX;

    @Column(name = "position_y")
    private Double positionY;

    @Column(name = "position_z")
    private Double positionZ;

    @Column(name = "orientation_x")
    private Double orientationX;

    @Column(name = "orientation_y")
    private Double orientationY;

    @Column(name = "orientation_z")
    private Double orientationZ;

    @Column(name = "orientation_w")
    private Double orientationW;

    @Column(name = "length_unit_id")
    private Integer lengthUnitId;
}
