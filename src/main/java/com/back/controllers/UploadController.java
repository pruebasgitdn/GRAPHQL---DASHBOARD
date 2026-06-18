package com.back.controllers;


import com.back.entities.Attachment;
import com.back.entities.Task;
import com.back.entities.User;
import com.back.entities.dto.FileUploadDto;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.TasksRepository;
import com.back.repositories.UserRepository;
import com.back.security.UserDetailsImpl;
import com.back.services.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController("/file")
@RequiredArgsConstructor
public class UploadController {

    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final TasksRepository tasksRepository;
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;


    @PostMapping("/upload_file/{taskId}")
    public ResponseEntity<String> upload(
            @RequestParam("file")MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable Long taskId
            ) throws IOException
    {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File cannot be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.badRequest().body("File exceeds the maximum limit of 2MB");
        }

        FileUploadDto fileUploadResponse = s3Service.uploadFile(file);

        User userfound = userRepository.findById(
                user.getId()
        ) .orElseThrow(()->{
            throw  new ItemNotFoundException("Usuario no encontrado");
        });

        Task taskfound = tasksRepository.findById(
                taskId
        ) .orElseThrow(()->{
            throw  new ItemNotFoundException("Tarea no encontrada");
        });


        Attachment newAttachment = Attachment.builder()
                .task(taskfound)
                .user(userfound)
                .fileUrl(fileUploadResponse.getUrl())
                .mimeType(fileUploadResponse.getMimeType())
                .fileSize(fileUploadResponse.getFileSize())
                .build();

        return ResponseEntity.ok(
                "Archivo subido correctamente URL: "+fileUploadResponse.getUrl()
        );
    }

    @GetMapping("/get/{fileName}")
    public ResponseEntity<byte[]> downlaod(
            @PathVariable String fileName
    )
    {

        byte[] data = s3Service.downloadFile(fileName);

        return ResponseEntity.ok()
                .body(data);
        }


}

