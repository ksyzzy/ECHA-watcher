package dev.armacki.echawatcher.repositories;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Getter @Setter
@Repository
public class FileSystemRepository {

    private final Logger logger = LoggerFactory.getLogger(FileSystemRepository.class);

    @Value("${documents.resource-dir}")
    private String RESOURCE_DIR;

    public boolean checkIfFileExists(String filename) {
        FileSystemResource resource;
        try {
            resource = new FileSystemResource(Paths.get(RESOURCE_DIR + "/" + filename));
        } catch (Exception ex) {
            logger.warn(String.format("Error reading file %s", filename), ex);
            return false;
        }
        return resource.exists();
    }

    public String saveFile(byte[] content, String filename) {
        Path file = Paths.get(RESOURCE_DIR + new Date().getTime() + "-" + filename);
        try {
            Files.createDirectories(file.getParent());
            Files.write(file, content);
        } catch (IOException ex) {
            logger.error(String.format("Could not write file %s", filename), ex);
            return null;
        }

        return file.toAbsolutePath().toString();
    }
}
