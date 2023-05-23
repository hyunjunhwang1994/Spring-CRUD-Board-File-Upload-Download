package com.example.springcrudboardfileuploaddownload.repository;

import com.example.springcrudboardfileuploaddownload.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByBoardId(Long id);

    void deleteAllByBoardId(Long id);
}
