package com.back.services;


import com.back.config.EnvConfig;
import com.back.entities.dto.FileUploadDto;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    //private final S3Client s3Client;

//    @Value("${aws.s3.bucket}")
//    private String bucketName;

    private final S3Client s3Client = S3Client.builder()
            .region(Region.of(EnvConfig.get("aws_region")))
            .credentialsProvider(
                    StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(
                                    EnvConfig.get("aws_access_key"),
                                    EnvConfig.get("aws_secret_key")
                            )
                    )
            )
            .build();


    private String bucketUrl = "https://hablamelo.s3.us-east-2.amazonaws.com/";



    public String returnUrl(String fileName){

        return bucketUrl + fileName;

    }


    public FileUploadDto uploadFile(MultipartFile file) throws IOException{
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(EnvConfig.get("aws_s3_bucket"))
                        .key(UUID.randomUUID()
                                +
                                "-"
                                +
                                file.getOriginalFilename()
                        )
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes()));


        FileUploadDto response = FileUploadDto.builder()
                .url(returnUrl(file.getName()))
                .fileName(file.getName())
                .fileSize(file.getSize())
                .mimeType(file.getContentType())
                .build();

        return  response;


    }

    public byte[] downloadFile(String key){
        ResponseBytes<GetObjectResponse> objectAsBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder()
                        .bucket(EnvConfig.get("aws_s3_bucket"))
                        .key(key)
                .build());

        return  objectAsBytes.asByteArray();
    }}





