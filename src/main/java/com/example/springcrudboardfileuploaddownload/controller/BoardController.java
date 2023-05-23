package com.example.springcrudboardfileuploaddownload.controller;

import com.example.springcrudboardfileuploaddownload.dto.BoardReqDto;
import com.example.springcrudboardfileuploaddownload.dto.BoardReadResDto;
import com.example.springcrudboardfileuploaddownload.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    /*글 작성(파일 업로드)*/
    @PostMapping("/boards")
    public void createPost(@RequestPart(value = "requestDto") BoardReqDto reqDto,
                           @RequestPart(value = "files") List<MultipartFile> files) throws IOException {
        boardService.createPost(reqDto, files);
    }

    /*글 읽기(파일 링크)*/
    @GetMapping("/boards")
    public BoardReadResDto readPost(@RequestParam Long boardId) {
        return boardService.readPost(boardId);
    }

    /*글 삭제*/
    @DeleteMapping("/boards")
    public void deletePost(@RequestParam Long boardId) {
        boardService.deletePost(boardId);
    }

    /*글 업데이트*/
    @PutMapping("/boards")
    public void updatePost(@RequestParam Long boardId, @RequestBody BoardReqDto reqDto) {
        boardService.updatePost(boardId, reqDto);
    }

    /*파일 링크 클릭 시 파일 저장 링크*/
    @GetMapping("/files/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileName") String fileName,
                                          HttpServletRequest request) throws IOException {
        /*프로젝트 루트 경로*/
        String rootDir = System.getProperty("user.dir");

        /*file의 path를 저장 -> 클릭 시 파일로 이동*/
        Path filePath = Path.of(rootDir + "/media/" + fileName);

        /*파일의 패스를 uri로 변경하고 resource로 저장.*/
        Resource resource = new UrlResource(filePath.toUri());

        /*컨텐츠 타입을 가지고 온다.*/
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}