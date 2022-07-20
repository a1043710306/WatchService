package cn.anhoo.watchservice.event;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class EventFileRead {
    public  static String readLine(FileEvent fileEvent)  {
        StringBuffer stringBuffer=new StringBuffer();

        try{
            RandomAccessFile randomAccessFile=fileEvent.getRandomAccessFile();
            int b=0;
            while ((b=randomAccessFile.read())!='\n'){
                if(b==-1){
                    break;
                }
                stringBuffer.append((char) b);
            }
            if(b=='\n'){
                fileEvent.readComplete();
                return stringBuffer.toString();
            }
            fileEvent.rollbackPointer();
        }catch (EOFException e){
            fileEvent.rollbackPointer();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getLoadLogs(FileEvent fileEvent){
        List<String> logs=new ArrayList<>();
        String s=null;
        while ((s=readLine(fileEvent))!=null){
            logs.add(s);
        }
        return logs;
    }
}
