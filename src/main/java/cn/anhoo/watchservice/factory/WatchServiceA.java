package cn.anhoo.watchservice.factory;

import cn.anhoo.watchservice.event.EventFileRead;
import cn.anhoo.watchservice.event.FileEvent;
import com.sun.nio.file.SensitivityWatchEventModifier;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.*;
import java.nio.file.*;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.sun.jmx.mbeanserver.Util.cast;

@AllArgsConstructor
public class WatchServiceA {
    final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    final Queue<FileEvent> fileEventQueue;
    final String dir;
    /**
     * key 是文件名
     * value  事件对象
     */
    final Map<String,FileEvent> pathFileEventMap;

    private WatchService watchServiceFactory(String dir) throws IOException {
        WatchService watchService=FileSystems.getDefault().newWatchService();
        //配置监听事件
        Paths.get(dir).register(watchService,
                new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY}, SensitivityWatchEventModifier.HIGH);
        return watchService;
    }



    private FileEvent selectFileEvent(WatchEvent event) throws IOException {
        Path  path=cast(event.context());
        FileEvent fileEvent=pathFileEventMap.get(path.toString());
        if(fileEvent!=null && event.kind()==StandardWatchEventKinds.ENTRY_MODIFY){
            fileEvent.setStandardWatchEventKinds(StandardWatchEventKinds.ENTRY_MODIFY);
            fileEvent.setStandardWatchEventKinds(null);
            return fileEvent;
        }

        if(event.kind()==StandardWatchEventKinds.ENTRY_DELETE){
            //文件删除  删除文件句柄
            fileEvent.setStandardWatchEventKinds(StandardWatchEventKinds.ENTRY_DELETE);
            FileEvent fileEvent1=pathFileEventMap.remove(path.getFileName().toString());
            if(fileEvent1.getRandomAccessFile()!=null){
                fileEvent1.getRandomAccessFile().close();
            }
            return null;
        }
        if(event.kind()==StandardWatchEventKinds.ENTRY_CREATE){
            //文件重新创建  重新打开句柄
            fileEvent.setStandardWatchEventKinds(StandardWatchEventKinds.ENTRY_CREATE);
            fileEvent.setRandomAccessFile(new RandomAccessFile(path.toString(),"r"));
            fileEvent.setFilePath(path);
            pathFileEventMap.put(path.getFileName().toString(),fileEvent);
            fileEvent.rollbackPointer();
            // 创建文件操作不读取文件  有可能数据暂时还未写入
        }
        return null;
    }


    private void  doMonitor(WatchService watchService)throws Exception{
        while(true){
            WatchKey key=watchService.poll(3, TimeUnit.SECONDS);
            if(key == null)
                continue;
            for(WatchEvent watchEvent:key.pollEvents()){
                FileEvent fileEvent=selectFileEvent(watchEvent);
                if(fileEvent!=null && fileEvent.getStandardWatchEventKinds()!=null){
                    fileEventQueue.add(fileEvent);
                }
            }
            key.reset();
        }

    }

    public void start(){
        threadPoolTaskExecutor.submit(()->{
            try {
                doMonitor(watchServiceFactory(dir));
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String args[]) throws FileNotFoundException {
//        Queue<FileEvent> queue=new LinkedBlockingQueue<>();
//        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(4,20,200,TimeUnit.SECONDS,new LinkedBlockingQueue<>());
//        String filePath="E:\\1.txt";
//        FileEvent fileEvent=new FileEvent();
//        Map<String,FileEvent> fileEventMap=new ConcurrentHashMap<>();
//        fileEvent.setRandomAccessFile(new RandomAccessFile(filePath,"r"));
//        fileEvent.setFilePath(filePath);
//        fileEventMap.put(new File(filePath).getName(),fileEvent);
//
//        WatchServiceA watchServiceA=new WatchServiceA(threadPoolExecutor,queue,"E:\\",fileEventMap);
//        watchServiceA.start();
//        while(true){
//            FileEvent fileEvent1= queue.poll();
//            if(fileEvent1 !=null){
//                if(fileEvent1.getStandardWatchEventKinds()==StandardWatchEventKinds.ENTRY_MODIFY){
//                    String str=null;
//                    while ((str= EventFileRead.readLine(fileEvent1))!=null){
//                        System.out.println(str);
//                    }
//
//                }
//            }
//        }
    }



}
