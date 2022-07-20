package cn.anhoo.watchservice.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "risk")
@Data
public class RiskConfig {
    private List<String> logFiles;


    public List<Path> getLogFilesPaths(){
        List<Path> pathsList=new ArrayList<>();
        for(String s:getLogFiles()){
            Path path=Paths.get(s);
            pathsList.add(path);
        }
        return pathsList;
    }


}
