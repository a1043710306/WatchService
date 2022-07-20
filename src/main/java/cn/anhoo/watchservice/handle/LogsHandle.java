package cn.anhoo.watchservice.handle;

/***
 * 日志解码器
 * @param <T>
 */
public interface LogsHandle<T> {
    T decode(String str);
}
