package org.knowledge4retail.api.device.service;

import org.knowledge4retail.api.device.dto.Map2dDto;

public interface Map2dService {

    Map2dDto create(Integer storeId, Map2dDto map);

    Map2dDto readByStoreId(Integer storeId);

    boolean exists(Integer storeId);
    
}
