package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.StoreObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreObjectRepository extends JpaRepository<StoreObject, Integer>, JpaSpecificationExecutor<StoreObject> {

    List<StoreObject> findByStoreId(Integer storeId);

    List<StoreObject> findByStoreIdAndType(Integer storeId, String type);
}
