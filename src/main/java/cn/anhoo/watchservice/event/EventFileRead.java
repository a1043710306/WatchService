package cn.anhoo.watchservice.event;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

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

        }catch (EOFException e){
            fileEvent.rollbackPointer();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
