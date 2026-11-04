package com.codeper.ControlClientes.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.codeper.ControlClientes.dto.FileDownloadResponse;
import jakarta.annotation.PostConstruct;

@Service 
public class FileService implements IFileService {

    @Value("${storage.location}")
    private String filePath;
    //OBJETO QUE JESTIONA LA RUTA DE ALMACENAMIENTO DE ARCHIVOS
    private Path fileStorageLocation;
    
    @PostConstruct 
    public void init() {
        try {
             this.fileStorageLocation = Path.of(filePath).toAbsolutePath().normalize();
             Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el directorio de almacenamiento de archivos.", e);
        } 
    }

    @Override
    public File retrieveFile(String filePath) {
        try {
            Path fileSavedPath=fileStorageLocation.resolve(filePath).normalize();
            if(!fileSavedPath.startsWith(fileStorageLocation)){
                throw new RuntimeException("Ruta de archivo no válida: " + filePath);
            }
            File file=fileSavedPath.toFile();
            return file;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo recuperar el archivo. Por favor, intente nuevamente.", e);
        }
    }

    @Override
    public String createFile(MultipartFile file) {
        String fileName=generateUniqueFileName(file.getOriginalFilename());
        try {
            Path targetLocation=fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo almacenar el archivo. Por favor, intente nuevamente.", e);
        }
    }

    @Override
    public FileDownloadResponse fileDownload(String filePath) {
        try {
            File file=retrieveFile(filePath);
            Path path=Path.of(file.getAbsolutePath());
            Resource resource=new UrlResource(path.toUri());
            if(!resource.exists()){
                throw new RuntimeException("Archivo no encontrado: " + filePath);
            }
            String contentType="";
            try {
                contentType=Files.probeContentType(path);
                
            } catch (Exception e) {
                contentType="application/octet-stream";
            }
            return FileDownloadResponse.builder()
                    .fileName(file.getName())
                    .contentType(contentType)
                     .resource(resource)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo descargar el archivo. Por favor, intente nuevamente.", e);
        }
    }
    //nombre con el que guardare mi archivo en el servidor, para evitar que se sobreescriba un archivo con el mismo nombre
    private String generateUniqueFileName(String originalFileName) {
        if(originalFileName==null){
            originalFileName="file";
        }
        String timestamp= new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String uuid=UUID.randomUUID().toString().substring(0,8);

        return timestamp+"_"+uuid+"_"+originalFileName;
    }

}
