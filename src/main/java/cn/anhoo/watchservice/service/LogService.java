package cn.anhoo.watchservice.service;

import cn.anhoo.watchservice.event.EventFileRead;
import cn.anhoo.watchservice.event.FileEvent;
import cn.anhoo.watchservice.handle.LogsHandle;
import cn.anhoo.watchservice.handle.SelectMapperHandle;
import cn.anhoo.watchservice.handle.impl.SimpleLogsHandle;
import cn.anhoo.watchservice.handle.impl.TestLogHandle;
import cn.anhoo.watchservice.mapper.bean.Test;
import cn.anhoo.watchservice.utils.RiskConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@AllArgsConstructor
@EnableScheduling
@Slf4j
public class LogService {
    final LinkedBlockingQueue<FileEvent> fileEventQueue;
    final SelectMapperHandle selectMapperHandle;

    @Scheduled(fixedDelay = 200,initialDelay = 2000)
    public void doProcess(){
        int size=fileEventQueue.size();
        List<FileEvent>fileEvents=new ArrayList<>(size);
        fileEventQueue.drainTo(fileEvents,size);
        for(FileEvent fileEvent:fileEvents){
            List<String> logs=EventFileRead.getLoadLogs(fileEvent);
            selectMapperHandle.doDb(logs,fileEvent);
        }
        fileEvents.clear();
    }


}
