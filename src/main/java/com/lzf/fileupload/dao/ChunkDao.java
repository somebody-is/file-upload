package com.lzf.fileupload.dao;

import com.lzf.fileupload.model.Chunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ChunkDao extends JpaRepository<Chunk,Long> {

    @Query(value = "select chunk_number from t_chunk_info where identifier=?1",nativeQuery = true)
    public List<Integer> selectChunkNumbersByIdentifier(String identifier);

}
