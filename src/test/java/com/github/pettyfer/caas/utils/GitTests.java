package com.github.pettyfer.caas.utils;

import cn.hutool.core.util.StrUtil;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class GitTests {

    private static final String PROJECT_NAME = "auto-build-examples";

    private static final String WORKSPACE_HOME = "D:/CAAS/workspace/";

    private static final String GIT_URL = "git@gitlab.ggjs.sinobest.cn:liuyang03813/auto-build-examples.git";

    @Test
    @SneakyThrows
    public void gitCloneTest() {
        File file = new File(WORKSPACE_HOME + PROJECT_NAME);
        // 清理目录
        if (file.exists()) {
            deleteFolder(file);
        }

        SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host hc, Session session) {
                session.setConfig("StrictHostKeyChecking","no");
                super.configure(hc, session);
            }

            @Override
            @SneakyThrows
            protected JSch createDefaultJSch(FS fs) throws JSchException {
                String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
                        "MIICXAIBAAKBgQCLYa4rKWX/oS7O5p2JnGr/uiXhyu72GWaJChWlKr12EnKdKhXU\n" +
                        "Am3RbF2kHltTaJCp3tN95aBemb+U0EhE96sGO1mtxfK7MfWteBbGBAH2vTvlzvRv\n" +
                        "gyL0khUZ9nwz3RPMaWvwidEbB6Wm0A6j/BfXpDFYPahj+0O/pzMwXjrM5QIDAQAB\n" +
                        "AoGAcXkt3a/PfYVQ4Vr5CK6Soe1/p8qaLztQseLG6nfYVUcBjphTuw3bMDNkLRgV\n" +
                        "y9sGY3WogweuPzY2tcW4AIYN8kUH+CRGG5U0nHvJNkxSvp2EGc51hAt68pxcZ25V\n" +
                        "voxzUBGlfdpR+x9qAYXa/HtcPc9YQxp28BqzconlDdE1l8ECQQDjRq3nOOq4u8MC\n" +
                        "rHFuq+SUUVjE3XRqj03ree6IOx1SV2qJKVwoIO9/kmqz6U6x+oKzbKQPgiTw29Bd\n" +
                        "Z9Y4oBeRAkEAnP8/+IdPjZ8vXI7gcuGWxZuGhpj++kbwdmjeLbNPnl2ILxC8LXKi\n" +
                        "BvCVZcT8l1EI5ymOhFJ7RvC7FXWLkMf+FQJBAMepQgFNiTb9DFZ+86/MJqT9ycQ4\n" +
                        "4Jr0hfmGRr07YYkC7r7MrP873+rsz/x5y/6jGstRd9d/97/4+9Xy8MkSK2ECQG4w\n" +
                        "JAjdhzNBz27qBJP8yuJEZuWfCg5Erc6diZqPqEjhIozN9RT8yMWQI3r7B8F4KYR8\n" +
                        "wT8T2NwQZ24HQv5Jr4UCQBYWlVxbdXTqVSx45DMCKRz5gZrGmfgVdgI0bNQqEE3z\n" +
                        "Y49gTXGi7BBAtJO9znYqGfcE/FKwYoy6ur/m/YseDgs=\n" +
                        "-----END RSA PRIVATE KEY-----\n";
                JSch defaultJSch = new JSch();
                defaultJSch.addIdentity("caas", privateKey.getBytes(StandardCharsets.UTF_8), null, null);
                return defaultJSch;

            }
        };

        TransportConfigCallback callback = transport -> {
            SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(sshSessionFactory);
        };

        Git git = Git.cloneRepository()
                .setURI(GIT_URL)
                .setBranch("master")
                .setDirectory(file)
                .setTransportConfigCallback(callback)
                .call();
        git.getRepository().getDirectory().getParentFile();
    }


    public static void deleteFolder(File file){
        if(file.isFile() || file.list().length==0){
            file.delete();
        }else{
            File[] files = file.listFiles();
            for(int i=0;i<files.length;i++){
                deleteFolder(files[i]);
                files[i].delete();
            }
        }
    }


}
