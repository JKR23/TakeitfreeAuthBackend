package com.takeitfree.auth.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AzureBlobStorageService {
    public String uploadFile(MultipartFile file)throws IOException;
}
