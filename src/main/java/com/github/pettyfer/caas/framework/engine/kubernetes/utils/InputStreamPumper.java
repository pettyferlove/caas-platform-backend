package com.github.pettyfer.caas.framework.engine.kubernetes.utils;

import io.fabric8.kubernetes.client.Callback;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Pettyfer
 */
@Slf4j
public class InputStreamPumper implements Runnable, Closeable {

    final InputStream in;
    final Callback<byte[]> callback;
    final Runnable onClose;
    final AtomicBoolean closed = new AtomicBoolean(false);

    volatile boolean keepReading = true;
    Thread thread;

    public InputStreamPumper(InputStream in, Callback<byte[]> callback) {
        this(in, callback, null);
    }

    public InputStreamPumper(InputStream in, Callback<byte[]> callback, Runnable onClose) {
        this.in = in;
        this.callback = callback;
        this.onClose = onClose;
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
        } catch (IOException e) {
            if (!keepReading) {
                return;
            }
            if (!thread.isInterrupted()) {
                log.debug("Error while pumping stream.", e);
            } else {
                log.debug("Interrupted while pumping stream.");
            }
        } finally {
            close();
        }
    }

    @Override
    public synchronized void close() {
        keepReading = false;
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }

        if (closed.compareAndSet(false, true) && onClose != null) {
            onClose.run();
        }
    }
}