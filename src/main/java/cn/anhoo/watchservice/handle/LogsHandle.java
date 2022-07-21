package cn.anhoo.watchservice.handle;

import java.util.List;

/***
 * 日志解码器
 * @param <T>
 */
public interface LogsHandle<T> {
    T decode(String str);
    List<T>  decodeBatch(List<String> logs);
}
