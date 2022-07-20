package cn.anhoo.watchservice.config;

import cn.anhoo.watchservice.event.FileEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class FileEventQueueConfig {
    @Bean
    public Queue<FileEvent> fileEventQueue(){
        return new LinkedBlockingQueue<>();
    }
}
