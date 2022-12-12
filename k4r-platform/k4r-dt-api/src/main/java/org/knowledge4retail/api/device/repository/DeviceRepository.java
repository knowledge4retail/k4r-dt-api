package org.knowledge4retail.api.device.repository;

import org.knowledge4retail.api.device.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String>, JpaSpecificationExecutor<Device> {}
