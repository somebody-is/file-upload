package com.lzf.fileupload.service.impl;

import com.lzf.fileupload.dao.ChunkDao;
import com.lzf.fileupload.model.Chunk;
import com.lzf.fileupload.service.ChunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChunkServiceImpl implements ChunkService {

    @Autowired
    private ChunkDao chunkDao;

    @Override
    public int save(Chunk chunk) {
        try{
            chunkDao.save(chunk);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean checkChunk(String identifier, Integer chunkNumber) {
        return false;
    }

    @Override
    public List<Integer> selectChunkNumber(String identifier) {
        return chunkDao.selectChunkNumbersByIdentifier(identifier);
    }

}
