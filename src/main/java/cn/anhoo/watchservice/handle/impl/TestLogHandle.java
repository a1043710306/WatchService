package cn.anhoo.watchservice.handle.impl;

import cn.anhoo.watchservice.WatchServiceApplication;
import cn.anhoo.watchservice.handle.LogsHandle;
import cn.anhoo.watchservice.mapper.bean.Test;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class TestLogHandle implements LogsHandle<Test> {
    @Override
    public Test decode(String str) {
        Test test=new Test();
        test.setTestId(System.nanoTime()+"");
        test.setContent(StringEscapeUtils.unescapeJava(str));
        test.setCreateAt(new Date());
        try {
            test.setSource( InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return test;
    }

    @Override
    public List<Test> decodeBatch(List<String> logs) {
        List<Test> tests=new ArrayList<>();
        for(String log:logs){
            tests.add(decode(log));
        }
        return tests;
    }


}
