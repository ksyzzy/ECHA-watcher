package dev.armacki.echawatcher.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class HashService {

    private final Logger logger = LoggerFactory.getLogger(HashService.class);

    public String generateChecksum(String filePath) throws IOException {
        byte[] hash;
        try {
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            hash = MessageDigest.getInstance("MD5").digest(data);
        } catch (InvalidPathException | OutOfMemoryError ex) {
            logger.error("File does not exist or file size exceeds 2GB", ex);
            return null;
        } catch (NoSuchAlgorithmException ex) {
            logger.error("Algorithm MD5 not found", ex);
            return null;
        }

        return new BigInteger(1, hash).toString(16);
    }
}

