package xj.love.hj.demo.fine.uploader.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;

/**
 * 文件分片工具类
 *
 * @author xiaojia
 * @since 1.0
 */
public final class FileChunkUtils {

    private FileChunkUtils() {
    }

    /**
     * 获取分片文件列表
     *
     * @param directory 所存放文件目录
     */
    public static List<File> getChunkFiles(File directory) {
        File[] chunkFiles = directory.listFiles(new ChunkFilesFilter());
        return Arrays.asList(chunkFiles)
                .stream()
                .sorted(Comparator.comparing(f -> Integer.parseInt(f.getName())))
                .collect(Collectors.toList());
    }

    /**
     * 合并所有分片文件
     *
     * @param outputFile 合并到的文件
     * @param chunkFile 分片文件
     */
    public static File mergeChunkFiles(File outputFile, File chunkFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputFile, true);
        try {
            FileInputStream fis = new FileInputStream(chunkFile);

            try {
                IOUtils.copy(fis, fos);
            } finally {
                IOUtils.closeQuietly(fis);
            }
        } finally {
            IOUtils.closeQuietly(fos);
        }
        return outputFile;
    }

    /**
     * 删除所有分片文件
     *
     * @param directory 所存放文件目录
     */
    public static void deleteChunkFiles(File directory) {
        List<File> chunkFiles = getChunkFiles(directory);
        chunkFiles.forEach(File::delete);
    }

    /**
     * 分片文件过滤器
     */
    private static class ChunkFilesFilter implements FilenameFilter {

        ChunkFilesFilter() {
        }

        @Override
        public boolean accept(File file, String s) {
            return s.matches("^\\d+$");
        }
    }
}
