package com.lzf.fileupload.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "t_chunk_info")
public class Chunk implements Serializable {

    private static final long serialVersionUID = 7073871700302406420L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 当前文件块，从1开始
     */
    private Integer chunkNumber;
    /**
     * 分块数量
     */
    private Long chunkSize;
    /**
     * 当前分块大小
     */
    @Column(name = "current_chunkSize")
    private Long currentChunkSize;
    /**
     * 总大小
     */
    private Long totalSize;
    /**
     * 文件标识
     */
    private String identifier;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 相对路径
     */
    private String relativePath;
    /**
     * 总块数
     */
    private Integer totalChunks;
    /**
     * 文件类型
     */
    private String type;

    /**
     * 要上传的文件
     */
    @Transient
    private transient MultipartFile upfile;
}
