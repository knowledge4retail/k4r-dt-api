package org.knowledge4retail.api.scan.repository;

import org.knowledge4retail.api.scan.model.Scan;
import org.knowledge4retail.api.scan.model.ScanKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScanRepository extends JpaRepository<Scan, ScanKey>, JpaSpecificationExecutor<Scan> {

    Boolean existsByEntityTypeAndId(String entityType, String Id);

    List<Scan> findByEntityTypeAndId(String entityType, String Id);
}