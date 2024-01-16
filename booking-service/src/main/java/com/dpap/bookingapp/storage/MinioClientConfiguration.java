package com.dpap.bookingapp.storage;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioClientConfiguration {

    private final MinioConfigProperties properties;

    public MinioClientConfiguration(MinioConfigProperties properties) {
        this.properties = properties;
    }

    @Bean
    public MinioClient minioClient() {
        return io.minio.MinioClient.builder()
                .endpoint(properties.getHost())
                .credentials(properties.getUsername(), properties.getPassword())
                .build();
    }
}
