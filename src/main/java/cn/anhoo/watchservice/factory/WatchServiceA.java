package cn.anhoo.watchservice.factory;

import cn.anhoo.watchservice.event.FileEvent;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class WatchServiceFactory {
    final ThreadPoolExecutor threadPoolExecutor;
    final Queue<FileEvent> fileEventQueue;

    public   WatchServiceFactory watchServiceFactory() throws IOException {
        WatchService watchService=FileSystems.getDefault().newWatchService();
    }

    public WatchServiceFactory(ThreadPoolExecutor threadPoolExecutor, Queue<FileEvent> fileEventQueue) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.fileEventQueue = fileEventQueue;
    }
}
