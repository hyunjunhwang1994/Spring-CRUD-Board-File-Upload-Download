package com.example.springcrudboardfileuploaddownload.dto;

import com.example.springcrudboardfileuploaddownload.entity.File;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileFormat {
    private String fileName;
    private String fileExtension;
    private String fileUri;

    public FileFormat(File file) {
        this.fileName = file.getFileName();
        this.fileExtension = file.getFileExtension();
        this.fileUri = file.getFileUri();
    }
}
