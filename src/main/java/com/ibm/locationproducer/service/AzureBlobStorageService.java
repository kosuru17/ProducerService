package com.ibm.locationproducer.service;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.ibm.locationproducer.exception.AzureStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


@Service
public class AzureBlobStorageService {


    @Value("${azure.storage.account-name}")
    private String storageAccountName;

    @Value("${azure.storage.account-key}")
    private String storageAccountKey;

    @Value("${azure.myBlob.url}")
    private String azureUrl;

    @Value("${azure.storage.container-name}")
    private String containerName;


        public Flux<String> readJsonFilesFromContainer (){

            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(String.format(azureUrl, storageAccountName, storageAccountKey))
                    .buildClient();

            return Flux.defer(() -> {
                    BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
                    return Flux.fromIterable(containerClient.listBlobs()).flatMap(blobItem -> {
                                String blobName = blobItem.getName();
                                return readJsonFileFromBlob(containerClient, blobName);
                            })
                            .subscribeOn(Schedulers.boundedElastic())
                            .onErrorResume(e->Mono.error(new AzureStorageException("Error while reading data from Azure blob storage.")));

            });


        }

        public Mono<String> readJsonFileFromBlob (BlobContainerClient containerClient, String blobName) {
            String expression = "Select * from BlobStorage ";
            return Mono.fromCallable(() -> containerClient.getBlobClient(blobName).openInputStream())
                    .map(inputStream -> {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            StringBuilder jsonContent = new StringBuilder();
                            String line;
                            while (true) {
                                try {
                                    if (!((line = reader.readLine()) != null)) break;
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                jsonContent.append(line);
                            }
                          System.out.println(jsonContent.toString());
                        return jsonContent.toString();
                        });

        }
}


