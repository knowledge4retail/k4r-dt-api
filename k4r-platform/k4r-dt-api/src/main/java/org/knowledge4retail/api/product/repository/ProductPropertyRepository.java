package org.knowledge4retail.api.product.repository;

import org.knowledge4retail.api.product.model.ProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPropertyRepository extends JpaRepository<ProductProperty, Integer>, JpaSpecificationExecutor<ProductProperty> {

    @Query("select case when count(p)> 0 then true else false end from ProductProperty p where characteristicId = (:characteristicId) and productId = (:productId) and  storeId = (:storeId)")
    boolean existsByProductIdAndCharacteristicIdAndStoreId(@Param("storeId") Integer storeId, @Param("productId") String productId, @Param("characteristicId") Integer characteristicId);

    @Query("select case when count(p)> 0 then true else false end from ProductProperty p where characteristicId = (:characteristicId) and productId = (:productId) and  storeId is null")
    boolean existsByProductIdAndCharacteristicIdAndStoreIdNull(@Param("productId") String productId, @Param("characteristicId") Integer characteristicId);

    @Query("select p from ProductProperty p where p.productId = (:productId) and  p.storeId = (:storeId)")
    List<ProductProperty> getAllByProductIdAndStoreId(@Param("storeId") Integer storeId, @Param("productId") String productId);

    @Query("select p from ProductProperty p where p.productId = (:productId) and  p.storeId is null")
    List<ProductProperty> getAllByProductIdAndStoreIdNull(@Param("productId") String productId);

    @Query("select p from ProductProperty p where p.productId = (:productId) and  p.storeId = (:storeId) and p.characteristicId = (:characteristicId)")
    Optional<ProductProperty> getProperty(@Param("storeId") Integer storeId, @Param("productId") String productId, @Param("characteristicId") Integer characteristicId);

    @Query("select p from ProductProperty p where p.productId = (:productId) and  p.storeId is null and p.characteristicId = (:characteristicId)")
    Optional<ProductProperty> getPropertyWithStoreIdNull(@Param("productId") String productId, @Param("characteristicId") Integer characteristicId);

    @Modifying
    @Query("delete from ProductProperty p where p.productId = (:productId) and  p.storeId = (:storeId) and p.characteristicId = (:characteristicId)")
    void deleteProperty(@Param("storeId") Integer storeId, @Param("productId") String productId, @Param("characteristicId") Integer characteristicId);

    @Modifying
    @Query("delete from ProductProperty p where p.productId = (:productId) and  p.storeId is null and p.characteristicId = (:characteristicId)")
    void deletePropertyWithStoreIdNull(@Param("productId") String productId, @Param("characteristicId") Integer characteristicId);

    @Query("select p from ProductProperty p where p.characteristicId = (:characteristicId) and p.productId = (:productId) and  p.storeId = (:storeId)")
    ProductProperty getOneByProductIdAndCharacteristicIdAndStoreId(@Param("productId") String productId, @Param("characteristicId") Integer characteristicId, @Param("storeId") Integer storeId);

    @Query("select p from ProductProperty p where p.characteristicId = (:characteristicId) and p.productId = (:productId) and  p.storeId is null")
    ProductProperty getOneByProductIdAndCharacteristicIdAndStoreIdNull(@Param("productId") String productId, @Param("characteristicId") Integer characteristicId);
}

