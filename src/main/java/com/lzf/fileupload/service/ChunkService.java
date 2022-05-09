package com.lzf.fileupload.service;

import com.lzf.fileupload.model.Chunk;

import java.util.List;

public interface ChunkService {

    public int save(Chunk chunk);

    public boolean checkChunk(String identifier,Integer chunkNumber);

    public List<Integer>selectChunkNumber(String identifier);

    public int deleteByIdentifier(String identifier);

}
