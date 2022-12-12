package org.knowledge4retail.api.store.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name="store_object")
public class StoreObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "location_timestamp")
    private OffsetDateTime locationTimestamp;

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

    @NotNull
    @Column(name = "width")
    private Double width;

    @NotNull
    @Column(name = "height")
    private Double height;

    @NotNull
    @Column(name = "depth")
    private Double depth;

    @NotNull
    @Column(name = "length_unit_id")
    private Integer lengthUnitId;

    @Column(name = "store_id")
    private Integer storeId;
}
