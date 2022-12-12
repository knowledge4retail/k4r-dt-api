package org.knowledge4retail.api.device.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "device_image")
public class DeviceImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "timestamp")
    private Long timestamp;
    @NotBlank
    @Column(name = "device_id")
    private String deviceId;
    @NotBlank
    @Column(name = "frame_id")
    private String frameId;

    @NotNull
    @Column(name = "camera_position_x")
    private Double cameraPositionX;
    @NotNull
    @Column(name = "camera_position_y")
    private Double cameraPositionY;
    @NotNull
    @Column(name = "camera_position_z")
    private Double cameraPositionZ;
    @NotNull
    @Column(name = "camera_orientation_x")
    private Double cameraOrientationX;
    @NotNull
    @Column(name = "camera_orientation_y")
    private Double cameraOrientationY;
    @NotNull
    @Column(name = "camera_orientation_z")
    private Double cameraOrientationZ;
    @NotNull
    @Column(name = "camera_orientation_w")
    private Double cameraOrientationW;

    @NotNull
    @Column(name = "device_position_x")
    private Double devicePositionX;
    @NotNull
    @Column(name = "device_position_y")
    private Double devicePositionY;
    @NotNull
    @Column(name = "device_position_z")
    private Double devicePositionZ;
    @NotNull
    @Column(name = "device_orientation_x")
    private Double deviceOrientationX;
    @NotNull
    @Column(name = "device_orientation_y")
    private Double deviceOrientationY;
    @NotNull
    @Column(name = "device_orientation_z")
    private Double deviceOrientationZ;
    @NotNull
    @Column(name = "device_orientation_w")
    private Double deviceOrientationW;

    @NotEmpty
    @Column(name = "label_id")
    private String labelId;
    @NotEmpty
    @Column(name = "label_name")
    private String labelName;

    @Column(name = "blob_url")
    private String blobUrl;
}
