package cn.anhoo.watchservice.handle;

import cn.anhoo.watchservice.event.FileEvent;
import cn.anhoo.watchservice.handle.impl.SimpleLogsHandle;
import cn.anhoo.watchservice.handle.impl.TestLogHandle;
import cn.anhoo.watchservice.mapper.TestMapper;
import cn.anhoo.watchservice.mapper.bean.Test;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableAsync
@AllArgsConstructor
@Slf4j
public class SelectMapperHandle {
    final SimpleLogsHandle simpleLogsHandle;

    final TestLogHandle testLogHandle;

    final TestMapper testMapper;

    public void doDb(List<String> data,FileEvent fileEvent){
        LogsHandle logsHandle=selectLogsHandle(fileEvent);
        List<Test> testList=logsHandle.decodeBatch(data);
        selectMapper(fileEvent,testList);
    }

    /***
     * 选择日志处理器
     * @return
     */
    private LogsHandle selectLogsHandle(FileEvent fileEvent){
        String fileName=fileEvent.getFilePath().getFileName().toString();
        switch(fileName){
            case "":
                return null;
            case "1.txt":
                return testLogHandle;
            default:
                log.warn("no select appropriate log handler. So use the default stringHandler");
                return simpleLogsHandle;
        }
    }
    @Async
    protected void selectMapper(FileEvent fileEvent,List batch){
        String fileName=fileEvent.getFilePath().getFileName().toString();
        switch(fileName){
            case "1.txt":
                for (Test t:(List<Test>)batch){
                    testMapper.insert(t);
                }
                break;
        }
    }

}
