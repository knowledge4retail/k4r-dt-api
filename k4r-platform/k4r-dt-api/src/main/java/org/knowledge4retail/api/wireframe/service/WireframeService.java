package org.knowledge4retail.api.wireframe.service;

import org.knowledge4retail.api.wireframe.dto.WireframeDto;
import org.knowledge4retail.api.wireframe.model.Wireframe;

import java.util.List;

public interface WireframeService {

    Wireframe create(WireframeDto WireframeDto);
    List<Wireframe> readAll();
    byte[] getWireframeFile(String blobUrl);
    Wireframe readById(String gTIN);
}
