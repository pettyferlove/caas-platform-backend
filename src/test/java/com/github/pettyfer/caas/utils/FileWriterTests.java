package com.github.pettyfer.caas.utils;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;

public class FileWriterTests {

    @Test
    @SneakyThrows
    public void fileWriterTest(){
        String str = "FROM pettyfer/apline-open-jre8:latest\n" +
                "RUN mkdir -p /home\n" +
                "WORKDIR /home\n" +
                "EXPOSE 8881\n" +
                "COPY ./target/auto-build-examples-0.0.1-SNAPSHOT.jar app.jar\n" +
                "ENTRYPOINT java -jar -Dfile.encoding=UTF-8 -Xmn64m -Xms256m -Xmx256m app.jar";
        File txt=new File("D:/Dockerfile");
        if(!txt.exists()){
            txt.createNewFile();
        }
        byte bytes[]=new byte[512];
        bytes=str.getBytes();
        int b=bytes.length;   //是字节的长度，不是字符串的长度
        FileOutputStream fos=new FileOutputStream(txt);
        fos.write(bytes,0,b);
        fos.close();
    }

}
