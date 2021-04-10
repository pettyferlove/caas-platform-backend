package com.github.pettyfer.caas.framework.engine.kubernetes.utils;

import io.fabric8.kubernetes.client.Callback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Pettyfer
 */
@Slf4j
public class BlockingInputStreamPumper extends InputStreamPumper {

    private Thread thread;

    public BlockingInputStreamPumper(InputStream in, Callback<byte[]> callback) {
        this(in, callback, null);
    }

    public BlockingInputStreamPumper(InputStream in, Callback<byte[]> callback, Runnable onClose) {
        super(in, callback, onClose);
    }


    @Override
    public void run() {
        synchronized (this) {
            thread = Thread.currentThread();
        }
        byte[] buffer = new byte[1024];
        try {
            int length;
            while (keepReading && !Thread.currentThread().isInterrupted() && (length = in.read(buffer)) != -1) {
                byte[] actual = new byte[length];
                System.arraycopy(buffer, 0, actual, 0, length);
                callback.call(actual);
            }
        } catch (IOException ignored) {
        } finally {
            close();
        }
    }
}