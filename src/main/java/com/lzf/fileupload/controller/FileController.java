package com.lzf.fileupload.controller;

import com.lzf.fileupload.model.BaseResponse;
import com.lzf.fileupload.model.Chunk;
import com.lzf.fileupload.model.FileInfo;
import com.lzf.fileupload.model.UploadResult;
import com.lzf.fileupload.service.ChunkService;
import com.lzf.fileupload.service.FileInfoService;
import com.lzf.fileupload.utils.FileInfoUtils;
import com.lzf.fileupload.utils.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/uploader")
public class FileController {

    Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${prop.upload-folder}")
    private String uploadFolder;

    @Autowired
    private FileInfoService fileInfoService;

    @Autowired
    private ChunkService chunkService;

    @PostMapping("/single")
    public void singleUpload(Chunk chunk) {
        // 获取传来的文件
        MultipartFile file = chunk.getUpfile();
        // 获取文件名
        String filename = chunk.getFilename();
        try {
            // 获取文件的内容
            byte[] bytes = file.getBytes();
            // SINGLE_UPLOADER是我定义的一个路径常量，这里的意思是，如果不存在该目录，则去创建
            String SINGLE_FOLDER = "D:\\temp\\file";
            if (!Files.isWritable(Paths.get(SINGLE_FOLDER))) {
                Files.createDirectories(Paths.get(SINGLE_FOLDER));
            }
            // 获取上传文件的路径
            Path path = Paths.get(SINGLE_FOLDER, filename);
            // 将字节写入该文件
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/uploadChunk")
    public UploadResult checkFile(Chunk chunk, HttpServletResponse response) {
        logger.info("验证分片请求");
        UploadResult result = new UploadResult();

        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);

        String file = uploadFolder + "/" + chunk.getIdentifier();
        //如果上传文件相同只是文件名不同，则新增一条file记录
        if (Files.isWritable(Paths.get(file))) {

            FileInfo fileInfo = fileInfoService.findByIdentifierEquals(chunk.getIdentifier());
            if(fileInfo!=null) {
                //文件不出错的情况
                result.setSkipUpload(true);
                result.setLocation(file);
                response.setStatus(HttpServletResponse.SC_OK);
                result.setMessage("上传成功（秒传）");
                return result;
            }
        }

        List<Integer> chunkNumber = chunkService.selectChunkNumber(chunk.getIdentifier());
        if (chunkNumber != null && chunkNumber.size() > 0) {
            result.setSkipUpload(false);
            result.setUploadedChunks(chunkNumber);
            response.setStatus(HttpServletResponse.SC_OK);
            result.setMessage("已上传部分块，可继续上传");
            return result;
        }

        result.setMessage("正常分块上传");
        return result;
    }

    @PostMapping("/uploadChunk")
    public BaseResponse uploadChunk(Chunk chunk) {
        logger.info("上传分片请求");
        System.out.println(chunk);
        MultipartFile file = chunk.getUpfile();

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(FileInfoUtils.generatePath(uploadFolder, chunk));
            Files.write(path, bytes);
            chunkService.save(chunk);
            return new BaseResponse(200, "上传分片" + chunk.getChunkNumber() + "成功");
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResponse(415, "上传出错");
        }

    }

    @PostMapping("/mergeFile")
    public BaseResponse mergeFile(@RequestBody Map<String, String> request) {
        logger.info("合并分片请求");
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilename(request.get("name"));
        fileInfo.setIdentifier(request.get("uniqueIdentifier"));
        fileInfo.setTotalSize(Long.valueOf(request.get("size")));
        fileInfo.setRefProjectId(request.get("projectId"));

        String fileName = fileInfo.getFilename();
        String file = uploadFolder + "/" + fileInfo.getIdentifier() + "/" + fileName;
        String folder = uploadFolder + "/" + fileInfo.getIdentifier();
        String mergeResult = FileInfoUtils.merge(file, folder, fileName);
        fileInfo.setLocation(file);

        if ("200".equals(mergeResult)) {
            fileInfoService.save(fileInfo);
            return new BaseResponse(200, "合并成功");
        }

        if("300".equals(mergeResult)) {
            FileInfo fileInfo1 = fileInfoService.findByIdentifierEquals(fileInfo.getIdentifier());
            if(!fileInfo.getFilename().equals(fileInfo1.getFilename())){
                fileInfoService.save(fileInfo);
            }
        }

        return new BaseResponse(415, "文件合并出错！请重试");
    }

    @RequestMapping(value = "/selectFileList", method = RequestMethod.POST)
    public BaseResponse selectFileList(@RequestBody Object object) {
        return new BaseResponse(200, "查询成功", fileInfoService.getAllFileInfo());
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(HttpServletRequest req, HttpServletResponse resp) {
        String location = req.getParameter("location");
        String fileName = req.getParameter("filename");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        OutputStream fos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(location));
            fos = resp.getOutputStream();
            bos = new BufferedOutputStream(fos);
            ServletUtils.setFileDownloadHeader(req, resp, fileName);
            int byteRead = 0;
            byte[] buffer = new byte[8192];
            while ((byteRead = bis.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, byteRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.flush();
                bis.close();
                fos.close();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    public BaseResponse deleteFile(@RequestBody FileInfo fileInfo ){
        int result = fileInfoService.deleteFile(fileInfo.getId());
        return new BaseResponse(200,"已删除！");
    }


}
