package com.codeper.ControlClientes.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.codeper.ControlClientes.dto.FileDownloadResponse;

public interface IFileService {
    File retrieveFile(String filePath);
    //path (ruta )donde almacena el archico
    String createFile(MultipartFile file);
    FileDownloadResponse fileDownload(String filePath);


}
