package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.Store;
import org.knowledge4retail.api.store.model.StoreAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer>, JpaSpecificationExecutor<Store> {

    boolean existsByExternalReferenceId(@Param("externalReferenceId") String externalReferenceId);

    Store findByExternalReferenceId(String externalReferenceId);

    @Query("""
            select new org.knowledge4retail.api.store.model.StoreAggregate(
                s,
                (select count(*) from Shelf sh where sh.storeId = s.id) as shelfCount,
                (select count(*) from ShelfLayer shl where shl.shelfId in (select id from Shelf sh2 where sh2.storeId = s.id)) as shelfLayerCount,
                (select count(*) from Barcode b where b.shelfLayerId in (select id from ShelfLayer shl2 where shl2.shelfId in (select id from Shelf sh3 where sh3.storeId = s.id))) as barcodeCount,
                (select count(*) from Product pr where pr.id in (select productId from ProductInGroup pig where pig.productGroupId in (select id from ProductGroup pg where pg.storeId = s.id))) as productCount)
            from Store s
           """)
    List<StoreAggregate> getAllAggregates();
}
