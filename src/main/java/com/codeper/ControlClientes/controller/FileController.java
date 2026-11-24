package com.codeper.ControlClientes.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeper.ControlClientes.dto.FileDownloadResponse;
import com.codeper.ControlClientes.service.IFileService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/files")
@RequiredArgsConstructor 
public class FileController {
    private final IFileService fileService;
    
    //Dice: “responde a peticiones GET en /api/files/algo”. Lo que va en algo se
    //guarda en la variable fileName. El :.+ evita que Spring corte la extensión (.jpg, .png).
    @GetMapping("/{fileName:.+}")
    //<Resource> significa que no devuelves JSON, sino un archivo
    public ResponseEntity<Resource> ver(@PathVariable String fileName) {
        FileDownloadResponse r = fileService.fileDownload(fileName);

        String contentType = (r.getContentType() == null || r.getContentType().isBlank())
                ? "application/octet-stream"
                : r.getContentType();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + r.getFileName() + "\"")
                .body(r.getResource());
    }


}
