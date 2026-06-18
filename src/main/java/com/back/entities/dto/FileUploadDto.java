package com.back.entities.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class FileUploadDto {

    private String url;
    private Long fileSize;
    private String fileName;
    private String mimeType;


}
