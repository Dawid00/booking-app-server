package com.dpap.bookingapp.storage;

import com.dpap.bookingapp.images.Image;
import com.dpap.bookingapp.images.ImageRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/files")
public class ClientFiles {

    private final MinioClient minioClient;
    private final ImageRepository imageRepository;

    public ClientFiles(MinioClient minioClient, ImageRepository imageRepository) {
        this.minioClient = minioClient;
        this.imageRepository = imageRepository;
    }

    @GetMapping(value = "/images/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getPhotos(@PathVariable String name)
            throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try (InputStream stream =
                     minioClient.getObject(
                             GetObjectArgs
                                     .builder()
                                     .bucket("placeImages")
                                     .object(name + ".jpg")
                                     .build())) {
            return stream.readAllBytes();
        }
    }

    @PostMapping("/images")
    public ResponseEntity<String> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "placeId") Long placeId
    ) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload a file");
            }
            var id = UUID.randomUUID().toString();
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket("booking")
                    .object(id)
                    .contentType(file.getContentType())
                    .stream(new BufferedInputStream(file.getInputStream()), file.getSize(), 5242880)
                    .build());
            imageRepository.save(new Image(buildFilePath(id), placeId));
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to upload file");
        }
    }

    private String buildFilePath(String fileName) {
        var result = "http://localhost:9990/files/images/" + fileName;
        return result.replace(".jpg", "");
    }
}
