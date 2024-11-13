package com.utpolis.api.microservicio.ejemplo.servicio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class ArchivoServicio {

    @Value("${archivo.ruta}")
    private String RUTA;

    public void guardar(MultipartFile inputFile) throws IOException { // [1] Genuine Coder, 2024, File Upload and Download with Spring Boot | Efficient File Manager REST API | Multipart Download https://www.youtube.com/watch?v=wW0nVc2NlhA
        if(inputFile.isEmpty()) {
            throw new NullPointerException("El archivo está vacío");
        }
        crearRuta();
        String fileSeparator = File.separator;
        File outputFile = new File(RUTA + fileSeparator + inputFile.getOriginalFilename());
        if(!Objects.equals(outputFile.getParent(), RUTA)) {
            throw new SecurityException("Ruta de archivo inválida. " + outputFile.getParent() + " no es una ruta válida");
        }
        Files.copy(inputFile.getInputStream(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private void crearRuta() {
        File directorio = new File(RUTA);
        if(!new File(RUTA).exists()) directorio.mkdirs();
    }

}
