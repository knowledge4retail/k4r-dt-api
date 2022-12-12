package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.Barcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarcodeRepository extends JpaRepository<Barcode, Integer>, JpaSpecificationExecutor<Barcode> {

    List<Barcode> findByShelfLayerId(Integer shelfLayerId);

    Barcode findByShelfLayerIdAndProductGtinId(Integer shelfLayerId, Integer productGtinId);

    Boolean existsByShelfLayerIdAndProductGtinId(Integer shelfLayerId, Integer productGtinId);

    @Query("select p from Barcode p where p.productGtinId = (:productGtinId)")
    List<Barcode> findByProductGtinId(Integer productGtinId);
}
