package cn.anhoo.watchservice.mapper.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("test")
@Data
public class Test {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String content;
    private String source;
    @JsonFormat(locale = "zh",timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    private String testId;
}
