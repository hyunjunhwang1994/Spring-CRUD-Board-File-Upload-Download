package com.example.springcrudboardfileuploaddownload.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardReadResDto {

    private String title;
    private String contents;
    private List<FileFormat> fileFormat;

    public BoardReadResDto(String title, String contents, List<FileFormat> fileFormat) {
        this.title = title;
        this.contents = contents;
        this.fileFormat = fileFormat;
    }
}
