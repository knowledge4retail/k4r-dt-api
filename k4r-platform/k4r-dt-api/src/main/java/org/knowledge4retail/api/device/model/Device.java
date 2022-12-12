package org.knowledge4retail.api.device.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "device")
public class Device {

    @Id
    @Column(name = "id")
    private String id;
    @NotNull
    @Column(name = "store_id")
    private Integer storeId;
    @Column(name = "device_type")
    private String deviceType;
    @Column(name = "description")
    private String description;
}
