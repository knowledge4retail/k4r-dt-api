package org.knowledge4retail.api.device.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.knowledge4retail.api.device.model.compositekeys.DeviceStatusId;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "device_status")
@IdClass(DeviceStatusId.class)
public class DeviceStatus {

    @Id
    @NotEmpty
    @Column(name = "device_id")
    String deviceId;

    @Id
    @NotNull
    @Column(name = "timestamp")
    Long timestamp;

    @Column(name = "frame_id")
    String frameId;

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
}
