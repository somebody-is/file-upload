package com.lzf.fileupload;

import com.lzf.fileupload.dao.ChunkDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class FileUploadApplicationTests {

    @Autowired
    private ChunkDao chunkDao;


    @Test
    void contextLoads() {
        List<Integer> list = chunkDao.selectChunkNumbersByIdentifier("acd59f43d37f1072df23fec661c807fc");
        System.out.println(list);
    }

}
