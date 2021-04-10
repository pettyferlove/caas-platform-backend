package com.github.pettyfer.caas.utils;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandTests {

    public static final String SCRIPT_DIR = System.getProperty("user.dir");

    @Test
    @SneakyThrows
    public void testCommand() {
        Map<String, String> env = System.getenv();
        String mavenHome = env.get("MAVEN_HOME");
        String command = mavenHome + "/bin/mvn.cmd clean package";
        String[] commandSplit = command.split(" ");
        List<String> lcommand = new ArrayList<String>();
        for (int i = 0; i < commandSplit.length; i++) {
            lcommand.add(commandSplit[i]);
        }

        System.out.println(SCRIPT_DIR);
        ProcessBuilder processBuilder = new ProcessBuilder(lcommand);
        //processBuilder.directory(new File(SCRIPT_DIR));
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        Process p = processBuilder.start();
        InputStream is = p.getInputStream();
        BufferedReader bs = new BufferedReader(new InputStreamReader(is));

        p.waitFor();
        if (p.exitValue() != 0) {
            //说明命令执行失败
            //可以进入到错误处理步骤中
        }
        String line = null;
        while ((line = bs.readLine()) != null) {
            System.out.println(line);
        }
    }

    @Test
    @SneakyThrows
    public void testSqlCommand() {
        Map<String, String> env = System.getenv();
        String home = env.get("JAVA_HOME");
        System.out.println(home);

        String command = home + "/bin/java.exe -jar commands/build-sql-1.0.3.jar -d C:/Users/Pettyfer/Desktop/auto-build-sql-examples -f c125eed7 -g C:/Users/Pettyfer/Desktop";
        String[] commandSplit = command.split(" ");
        List<String> lcommand = new ArrayList<String>();
        for (int i = 0; i < commandSplit.length; i++) {
            lcommand.add(commandSplit[i]);
        }
        ProcessBuilder processBuilder = new ProcessBuilder(lcommand);
        /*Map<String, String> environment = processBuilder.environment();
        environment.clear();
        environment.put("LANG", "en_US.UTF-8");*/
        //processBuilder.directory(new File(SCRIPT_DIR));
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        Process process = processBuilder.start();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))){
            while (process.isAlive()) {
                while (bufferedReader.ready()) {
                    String s = bufferedReader.readLine();
                    System.out.println(s);
                }
            }
        }

        int status = process.waitFor();

        processBuilder.redirectErrorStream(true);
//将输出流重定向到日志文件
        File log = new File("log");
        processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
    }

}
