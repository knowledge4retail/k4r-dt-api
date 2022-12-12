package org.knowledge4retail.api.shared.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureStorageBlobClientConfig {

    @Value("${azure.blobstorage.connection-string}")
    String connectionString;

    @Bean
    public BlobServiceClient getClient() {

        return new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
    }
}
