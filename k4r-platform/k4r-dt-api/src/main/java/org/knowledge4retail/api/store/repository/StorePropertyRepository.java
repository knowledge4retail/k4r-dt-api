package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.StoreProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorePropertyRepository extends JpaRepository<StoreProperty, Integer>, JpaSpecificationExecutor<StoreProperty> {

    @Query("select case when count(p)> 0 then true else false end from StoreProperty p where characteristicId = (:characteristicId) and  storeId = (:storeId)")
    boolean existsByCharacteristicIdAndStoreId(@Param("storeId") Integer storeId, @Param("characteristicId") Integer characteristicId);

    @Query("select p from StoreProperty p where p.storeId = (:storeId)")
    List<StoreProperty> getAllByStore(@Param("storeId") Integer storeId);

    @Query("select p from StoreProperty p where p.storeId = (:storeId) and p.characteristicId = (:characteristicId)")
    Optional<StoreProperty> getProperty(@Param("storeId") Integer storeId, @Param("characteristicId") Integer characteristicId);

    @Modifying
    @Query("delete from StoreProperty p where p.storeId = (:storeId) and p.characteristicId = (:characteristicId)")
    void deleteProperty(@Param("storeId") Integer storeId, @Param("characteristicId") Integer characteristicId);
}

