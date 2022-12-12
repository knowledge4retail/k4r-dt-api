package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.StoreCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreCharacteristicRepository extends JpaRepository<StoreCharacteristic, Integer>, JpaSpecificationExecutor<StoreCharacteristic> {}
