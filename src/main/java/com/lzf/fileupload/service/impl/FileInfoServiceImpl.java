package com.lzf.fileupload.service.impl;

import com.lzf.fileupload.dao.FileInfoDao;
import com.lzf.fileupload.model.FileInfo;
import com.lzf.fileupload.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileInfoServiceImpl implements FileInfoService {

    @Autowired
    private FileInfoDao fileInfoDao;

    @Override
    public int save(FileInfo fileInfo) {
        try{
            fileInfoDao.save(fileInfo);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<FileInfo> getAllFileInfo() {
        return fileInfoDao.findFileInfosByDelFlagEquals(0);
    }

    @Override
    public int deleteFile(Long id) {
        try{
            FileInfo fileInfo = fileInfoDao.findById(id).get();
            fileInfo.setDelFlag(1);
            fileInfoDao.save(fileInfo);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public FileInfo findByIdentifierEquals(String identifier) {
        return fileInfoDao.findByIdentifierEquals(identifier);
    }
}
