package org.knowledge4retail.api.device.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
public class Map2d {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(name = "timestamp")
    private Long timestamp;
    @NotBlank
    @Column(name = "frame_id")
    private String frameId;
    @NotBlank
    @Column(name = "device_id")
    private String deviceId;

    @NotNull
    @Column(name = "resolution")
    private Double resolution;
    @NotNull
    @Column(name = "width")
    private Double width;
    @NotNull
    @Column(name = "height")
    private Double height;
    @NotNull
    @Column(name = "length_unit_id")
    private Integer lengthUnitId;

    @NotNull
    @Column(name = "position_x")
    private Double positionX;
    @NotNull
    @Column(name = "position_y")
    private Double positionY;
    @NotNull
    @Column(name = "position_z")
    private Double positionZ;

    @NotNull
    @Column(name = "orientation_x")
    private Double orientationX;
    @NotNull
    @Column(name = "orientation_y")
    private Double orientationY;
    @NotNull
    @Column(name = "orientation_z")
    private Double orientationZ;
    @NotNull
    @Column(name = "orientation_w")
    private Double orientationW;

    @NotBlank
    @Column(columnDefinition = "text") //we expect to store very long strings in this field
    private String data;

    @NotNull
    @Column(name = "store_id")
    private Integer storeId;
}
