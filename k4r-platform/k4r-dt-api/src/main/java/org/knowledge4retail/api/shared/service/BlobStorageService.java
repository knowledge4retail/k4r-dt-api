package org.knowledge4retail.api.shared.service;

import java.io.IOException;
import java.io.InputStream;

public interface BlobStorageService {
    
    String saveBlob(String containerName, String fileName, byte[] content) throws IOException;
    InputStream getBlob(String containerName, String fileName);
}
