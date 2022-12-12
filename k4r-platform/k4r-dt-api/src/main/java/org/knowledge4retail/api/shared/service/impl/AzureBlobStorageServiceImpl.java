package org.knowledge4retail.api.shared.service.impl;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.specialized.BlockBlobClient;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.service.BlobStorageService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class AzureBlobStorageServiceImpl implements BlobStorageService {


    private final BlobServiceClient storageClient;


    public AzureBlobStorageServiceImpl(BlobServiceClient storageClient) {
        this.storageClient = storageClient;
    }

    @Override
    public String saveBlob(String containerName, String fileName, byte[] content) throws IOException {



            BlobContainerClient containerClient = storageClient.getBlobContainerClient(containerName);

            CreateContainerIfItDoesNotExist(containerClient);

            BlockBlobClient blobClient = containerClient.getBlobClient(fileName).getBlockBlobClient();

            InputStream dataStream = new ByteArrayInputStream(content);

            blobClient.upload(dataStream, content.length);

            dataStream.close();

            return blobClient.getBlobUrl();

    }

    @Override
    public InputStream getBlob(String containerName, String fileName) {

        BlobContainerClient containerClient = storageClient.getBlobContainerClient(containerName);

        BlockBlobClient blobClient = containerClient.getBlobClient(fileName).getBlockBlobClient();

        return blobClient.openInputStream();

    }

    private void CreateContainerIfItDoesNotExist(BlobContainerClient containerClient) {
        if(!containerClient.exists())
        {
            containerClient.create();
        }
    }
}
