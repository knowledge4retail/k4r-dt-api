package org.knowledge4retail.api.device.repository;

import org.knowledge4retail.api.device.model.compositekeys.DeviceStatusId;
import org.knowledge4retail.api.device.model.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, DeviceStatusId>, JpaSpecificationExecutor<DeviceStatus> {
}
