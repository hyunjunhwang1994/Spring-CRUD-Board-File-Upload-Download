package com.example.springcrudboardfileuploaddownload.service;

import com.example.springcrudboardfileuploaddownload.dto.BoardReqDto;
import com.example.springcrudboardfileuploaddownload.dto.BoardReadResDto;
import com.example.springcrudboardfileuploaddownload.dto.FileFormat;
import com.example.springcrudboardfileuploaddownload.entity.Board;
import com.example.springcrudboardfileuploaddownload.entity.File;
import com.example.springcrudboardfileuploaddownload.repository.BoardRepository;
import com.example.springcrudboardfileuploaddownload.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    /*게시글 생성*/
    @Transactional
    public void createPost(BoardReqDto reqDto, List<MultipartFile> files) throws IOException {

        /*게시글 entity 생성*/
        Board board = new Board(reqDto);
        boardRepository.save(board);

        /*지원하지 않는 확장자 파일 제거*/
        List<MultipartFile> validatedFiles = filesValidation(files);

        /*걸러진 파일들 업로드*/
        filesUpload(validatedFiles, board.getId());


        /*유효성 검증을 끝낸 파일들을 하나씩 꺼냄.*/
        for (MultipartFile validatedFile : validatedFiles) {

            /*File Entity 생성 후 저장*/
            File file = new File(validatedFile, board);
            fileRepository.save(file);

        }
    }

    /*게시글 읽기*/
    public BoardReadResDto readPost(Long boardId) {

        /*board*/
        Optional<Board> optionalBoard = boardRepository.findById(boardId);

        if (optionalBoard.isEmpty()) {
            throw new IllegalArgumentException("해당 글이 없습니다.");
        }

        Board board = optionalBoard.get();
        /*File*/
        List<File> fileList = fileRepository.findAllByBoardId(boardId);
        List<FileFormat> fileFormatList = new ArrayList<>();

        /*파일이 존재한다면*/
        if (fileList != null) {
            for (File file : fileList) {
                FileFormat fileFormat = new FileFormat(file);
                fileFormatList.add(fileFormat);
            }
        }

        BoardReadResDto responseDto = new BoardReadResDto(board.getTitle(), board.getContents(), fileFormatList);
        return responseDto;
    }


    /*게시글 삭제*/
    @Transactional
    public void deletePost(Long boardId) {

        /*해당 boardId를 가지고 있는 file 먼저 삭제*/
        fileRepository.deleteAllByBoardId(boardId);

        /*board 삭제*/
        boardRepository.deleteById(boardId);
    }

    /*게시글 업데이트*/
    @Transactional
    public void updatePost(Long boardId, BoardReqDto reqDto) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);

        if (optionalBoard.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 글입니다.");
        }

        Board board = optionalBoard.get();
        board.updateBoard(reqDto);
    }

    /*파일의 유효성 검증*/
    private List<MultipartFile> filesValidation(List<MultipartFile> files) throws IOException {
        /*접근 거부 파일 확장자명*/
        String[] accessDeniedFileExtension = {"exe", "zip"};
        /*접근 거부 파일 컨텐츠 타입*/
        String[] accessDeniedFileContentType = {"application/x-msdos-program", "application/zip"};


        ArrayList<MultipartFile> validatedFiles = new ArrayList<>();


        for (MultipartFile file : files) {
            /*원본 파일 이름*/
            String originalFileName = file.getOriginalFilename();
            /*파일의 확장자명*/
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            /*파일의 컨텐츠타입*/
            String fileContentType = file.getContentType();

            /*accessDeniedFileExtension, accessDeniedFileContentType -> 업로드 불가*/
            if (Arrays.asList(accessDeniedFileExtension).contains(fileExtension) ||
                    Arrays.asList(accessDeniedFileContentType).contains(fileContentType)) {
                log.warn(fileExtension + "(" + fileContentType + ") 파일은 지원하지 않는 확장자입니다.");
            } else {/*업로드 가능*/
                validatedFiles.add(file);
            }


        }
        return validatedFiles;
    }

    /*파일 업로드 메소드*/
    private void filesUpload(List<MultipartFile> files, Long boardId) throws IOException {

        /*프로젝트 루트 경로*/
        String rootDir = System.getProperty("user.dir");

        for (MultipartFile file : files) {
            /*업로드 경로*/
            java.io.File uploadPath = new java.io.File(rootDir + "/media/" + boardId + "_" + file.getOriginalFilename());
            /*업로드*/
            file.transferTo(uploadPath);
        }
    }

}