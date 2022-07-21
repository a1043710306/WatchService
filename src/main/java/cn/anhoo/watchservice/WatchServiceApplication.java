package cn.anhoo.watchservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.anhoo.watchservice.mapper")
public class WatchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WatchServiceApplication.class, args);
    }

}
