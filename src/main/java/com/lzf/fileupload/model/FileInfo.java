package com.lzf.fileupload.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "t_file_info")
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 6969462437970901728L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //附件名称
    private String filename;

    //private String nameSearch;

    //附件MD5标识
    private String identifier;

    //附件总大小
    private Long totalSize;

    private String totalSizeName;

    //附件类型
    private String type;

    //附件存储地址
    private String location;

    //删除标志
    private int delFlag;

    //文件所属目标（项目、学生、档案等，预留字段）
    private String refProjectId;

    //上传人
    private String uploadBy;

    //上传时间
    private Date uploadTime;

    private String uploadTimeString;

}
