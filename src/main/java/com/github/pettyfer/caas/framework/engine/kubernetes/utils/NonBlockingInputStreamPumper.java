package com.github.pettyfer.caas.framework.engine.kubernetes.utils;

import io.fabric8.kubernetes.client.Callback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Pettyfer
 */
@Slf4j
public class NonBlockingInputStreamPumper extends InputStreamPumper {

    public NonBlockingInputStreamPumper(InputStream in, Callback<byte[]> callback) {
        this(in, callback, null);
    }

    public NonBlockingInputStreamPumper(InputStream in, Callback<byte[]> callback, Runnable onClose) {
        super(in, callback, onClose);
    }

    @Override
    public void run() {
        synchronized (this) {
            thread = Thread.currentThread();
        }
        byte[] buffer = new byte[1024];
        try {
            while (keepReading && !Thread.currentThread().isInterrupted()) {
                while (in.available() > 0 && keepReading && !Thread.currentThread().isInterrupted()) {
                    int length = in.read(buffer);
                    if (length < 0) {
                        throw new IOException("EOF has been reached!");
                    }
                    byte[] actual = new byte[length];
                    System.arraycopy(buffer, 0, actual, 0, length);
                    callback.call(actual);
                }
                Thread.sleep(50); // Pause to avoid tight loop if external proc is too slow
            }
        } catch (IOException ignored) {
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }

}
