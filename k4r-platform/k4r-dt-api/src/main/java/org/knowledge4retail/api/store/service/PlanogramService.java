package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.PlanogramDto;
import org.knowledge4retail.api.store.model.Planogram;

import java.util.List;

public interface PlanogramService {

    Planogram create(PlanogramDto planogramDto);
    List<Planogram> readAll(Integer storeId);
    byte[] getPlanogramFile(String blobUrl);
    Planogram readById(Integer planogramId);
}
