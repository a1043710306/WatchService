package cn.anhoo.watchservice.event;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

/**
 *  文件状态变更事件
 */
public class FileEvent {
    //文件指针
    private RandomAccessFile randomAccessFile;
    //上次文件读取位置
    private long lastOffset=0;
    //事件状态
    private WatchEvent.Kind<Path> standardWatchEventKinds;

    private String filePath;


    public RandomAccessFile getRandomAccessFile() {
        return randomAccessFile;
    }

    public void setRandomAccessFile(RandomAccessFile randomAccessFile) {
        this.randomAccessFile = randomAccessFile;
    }

    /**
     * 读取完成设置最后的文件指针位置
     * @throws IOException
     */
    public void readComplete() throws IOException {
        lastOffset=randomAccessFile.getFilePointer();
    }

    /**
     * 回滚文件指针
     * @throws IOException
     */
    public void rollbackPointer()  {
        try {
            randomAccessFile.seek(lastOffset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WatchEvent.Kind<Path> getStandardWatchEventKinds() {
        return standardWatchEventKinds;
    }

    public void setStandardWatchEventKinds(WatchEvent.Kind<Path> standardWatchEventKinds) {
        this.standardWatchEventKinds = standardWatchEventKinds;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
