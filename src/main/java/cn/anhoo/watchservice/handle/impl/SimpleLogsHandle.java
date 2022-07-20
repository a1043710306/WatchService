package cn.anhoo.watchservice.handle.impl;

import cn.anhoo.watchservice.handle.LogsHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleLogsHandle implements LogsHandle<String> {
    @Override
    public String decode(String str) {
        log.info("decode string {}",str);
        return str;
    }
}
