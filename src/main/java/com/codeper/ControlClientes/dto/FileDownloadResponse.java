package com.codeper.ControlClientes.dto;

import org.springframework.core.io.Resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class FileDownloadResponse {
    private Resource resource;
    private String contentType;
    private String fileName;
}
