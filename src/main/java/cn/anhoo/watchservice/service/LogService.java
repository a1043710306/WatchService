package cn.anhoo.watchservice.service;

import cn.anhoo.watchservice.event.EventFileRead;
import cn.anhoo.watchservice.event.FileEvent;
import cn.anhoo.watchservice.handle.LogsHandle;
import cn.anhoo.watchservice.handle.impl.SimpleLogsHandle;
import cn.anhoo.watchservice.utils.RiskConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Queue;

@Service
@AllArgsConstructor
@EnableScheduling
@Slf4j
public class LogService {
    final Queue<FileEvent> fileEventQueue;
    final SimpleLogsHandle simpleLogsHandle;

    @Scheduled(fixedDelay = 200,initialDelay = 2000)
    public void doProcess(){
        FileEvent fileEvent=null;
        while ((fileEvent=fileEventQueue.poll())!=null){
            LogsHandle logsHandle=selectLogsHandle(fileEvent);
            List<String> logList= EventFileRead.getLoadLogs(fileEvent);
            for(String str:logList){
              log.info((String) logsHandle.decode(str));
            }
        }
    }

    /***
     * 选择日志处理器
     * @return
     */
    private LogsHandle selectLogsHandle(FileEvent  fileEvent){
        String fileName=fileEvent.getFilePath().getFileName().toString();
        switch(fileName){
            case "":
                return null;
            default:
                return simpleLogsHandle;
        }
    }
}
