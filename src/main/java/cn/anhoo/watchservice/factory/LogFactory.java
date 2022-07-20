package cn.anhoo.watchservice.factory;

import cn.anhoo.watchservice.event.FileEvent;
import cn.anhoo.watchservice.utils.RiskConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@AllArgsConstructor
@Slf4j
public class LogFactory {
    final RiskConfig riskConfig;
    final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 文件事件队列  日志更新事件   日志文件创建事件   日志文件删除事件
     */
    final Queue<FileEvent> fileEventQueue;

    /***
     * 构造日志监视器 然后交由spring管理
     * @return
     * @throws FileNotFoundException
     */
    @Bean
   public List<WatchServiceA> init() throws FileNotFoundException {
        Map<String, FileEvent> fileEventMap=new ConcurrentHashMap<>();
        List<String> dirs=new ArrayList<>();
        List<Path> pathList=riskConfig.getLogFilesPaths();

        List<WatchServiceA> watchServiceAList=new ArrayList<>();
        for(Path p:pathList){
            dirs.add(p.getParent().toString());
            File file=p.toFile();
            if(file.isFile()){
                FileEvent fileEvent=new FileEvent();
                fileEvent.setFilePath(p);
                fileEvent.setRandomAccessFile(new RandomAccessFile(p.toString(),"r"));
                fileEventMap.put(p.getFileName().toString(),fileEvent);
            }
        }
        for(String dir:dirs){
            WatchServiceA watchService=new WatchServiceA(threadPoolTaskExecutor,fileEventQueue,dir,fileEventMap);
            watchServiceAList.add(watchService);
            watchService.start();
        }
        log.info("load logs dir {}",dirs);
        log.info("Load log file controller number {}",watchServiceAList.size());

        return watchServiceAList;
    }
}
