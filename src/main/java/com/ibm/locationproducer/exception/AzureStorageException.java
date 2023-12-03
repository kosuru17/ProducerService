package com.ibm.locationproducer.exception;

import com.azure.core.exception.AzureException;


public class AzureStorageException extends AzureException {
    public AzureStorageException(String message) {
        super(message);
    }
}
