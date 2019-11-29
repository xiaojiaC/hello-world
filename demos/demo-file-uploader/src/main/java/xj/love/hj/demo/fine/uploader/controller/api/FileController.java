package xj.love.hj.demo.fine.uploader.controller.api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xj.love.hj.demo.fine.uploader.configuration.props.FileUploaderProperties;
import xj.love.hj.demo.fine.uploader.constant.Constants;
import xj.love.hj.demo.fine.uploader.util.FileChunkUtils;
import xj.love.hj.demo.fine.uploader.util.Response;

/**
 * 文件资源控制器
 *
 * @author xiaojia
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX + "/files")
public class FileController {

    @Autowired
    private FileUploaderProperties fileUploaderProperties;

    @PostMapping("upload")
    public Response<?> uploadFileChunk(@RequestParam("md5") String fileMd5,
            @RequestParam("index") int chunkIndex,
            @RequestParam("file") MultipartFile chunkFile) {
        String fileSaveDir = fileUploaderProperties.getDir() + File.separator + fileMd5;
        // 创建文件存储目录
        File file = new File(fileSaveDir);
        if (!file.exists()) {
            file.mkdir();
        }

        // 保存分片文件数据
        File chunkSaveFile = new File(fileSaveDir + File.separator + chunkIndex);
        try {
            FileUtils.copyInputStreamToFile(chunkFile.getInputStream(), chunkSaveFile);
            log.info("File saved successfully");
            return Response.success();
        } catch (IOException e) {
            log.error("File saved failed", e);
            return Response.failed();
        }
    }

    @PostMapping("merge")
    public Response<?> mergeFileChunks(@RequestParam("md5") String fileMd5,
            @RequestParam("name") String fileName,
            @RequestParam("size") long fileSize) {
        String fileSaveDir = fileUploaderProperties.getDir() + File.separator + fileMd5;
        File file = new File(fileSaveDir);
        // 读取所有分片文件
        List<File> chunkFiles = FileChunkUtils.getChunkFiles(file);

        File outputFile = new File(fileSaveDir + File.separator + fileName);
        try {
            // 创建输出文件
            boolean result = outputFile.createNewFile();
            if (result) {
                // 合并分片文件
                for (File chunkFile : chunkFiles) {
                    FileChunkUtils.mergeChunkFiles(outputFile, chunkFile);
                }

                if (fileSize != outputFile.length()) {
                    return Response.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), String.format(
                            "Merge file size check failed, the expectation is %s, the result is %s",
                            fileSize,
                            outputFile.length()));
                }
                log.info("File merge completed");
            }
        } catch (Exception e) {
            log.error("File merge failed", e);
            return Response.failed();
        }

        FileChunkUtils.deleteChunkFiles(file);
        return Response.success();
    }
}
