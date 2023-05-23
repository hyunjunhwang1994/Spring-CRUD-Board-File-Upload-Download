package com.example.springcrudboardfileuploaddownload.entity;

import com.example.springcrudboardfileuploaddownload.dto.BoardReqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String contents;

    public Board(BoardReqDto reqDto) {
        this.title = reqDto.getTitle();
        this.contents = reqDto.getContents();

    }
    public void updateBoard(BoardReqDto reqDto) {
        this.title = reqDto.getTitle();
        this.contents = reqDto.getContents();
    }

}
