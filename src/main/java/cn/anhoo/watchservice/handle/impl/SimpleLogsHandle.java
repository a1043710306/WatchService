package cn.anhoo.watchservice.handle.impl;

import cn.anhoo.watchservice.handle.LogsHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class SimpleLogsHandle implements LogsHandle<String> {
    @Override
    public String decode(String str) {
        log.info("decode string {}",str);
        return str;
    }

    @Override
    public List<String> decodeBatch(List<String> logs) {
        List<String> stringList=new ArrayList<>();
        for(String str:logs){
            stringList.add(decode(str));
        }
        return stringList;
    }
}
