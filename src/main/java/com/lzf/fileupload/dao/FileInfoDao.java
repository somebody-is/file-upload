package com.lzf.fileupload.dao;

import com.lzf.fileupload.model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileInfoDao extends JpaRepository<FileInfo,Long> {

    public FileInfo findByIdentifierEquals(String identifier);

    public List<FileInfo> findFileInfosByDelFlagEquals(int i);

}
