package com.lzf.fileupload.service;

import com.lzf.fileupload.model.Chunk;
import com.lzf.fileupload.model.FileInfo;

import java.util.List;
import java.util.Map;

public interface FileInfoService {

    public int save(FileInfo fileInfo);

    public List<FileInfo> getAllFileInfo();

    public int deleteFile(Long id);

    public FileInfo findByIdentifierEquals(String identifier);

}
