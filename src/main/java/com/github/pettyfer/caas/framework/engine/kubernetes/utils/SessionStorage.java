package com.github.pettyfer.caas.framework.engine.kubernetes.utils;

import com.github.pettyfer.caas.framework.engine.kubernetes.session.TerminalSession;
import lombok.SneakyThrows;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Pettyfer
 */
public class SessionStorage {

    private final Map<String, TerminalSession> sessionStore = new ConcurrentHashMap<>();

    private static volatile SessionStorage storage;

    /**
     * 双端检查锁创建实例
     *
     * @return SessionStorage
     */
    public static synchronized SessionStorage getInstance() {
        if (storage == null) {
            synchronized (SessionStorage.class) {
                if (storage == null) {
                    storage = new SessionStorage();
                }
            }
        }
        return storage;
    }

    public void addSession(TerminalSession session) {
        if (session != null) {
            String id = session.getSession().getId();
            sessionStore.put(id, session);
        }
    }

    @SneakyThrows
    public synchronized void removeSession(String sessionId) {
        TerminalSession terminalSession = sessionStore.get(sessionId);
        terminalSession.getExecWatch().getInput().write("exit\n".getBytes());
        sessionStore.remove(sessionId);
    }

    public TerminalSession getSession(String sessionId) {
        return sessionStore.get(sessionId);
    }

    public List<TerminalSession> getAllSession() {
        TerminalSession[] terminalSessions = new TerminalSession[]{};
        return Arrays.asList(sessionStore.values().toArray(terminalSessions));
    }

    public void sendMessage(String sessionId, String text) throws IOException {
        TerminalSession terminalSession = sessionStore.get(sessionId);
        if (terminalSession != null && terminalSession.getSession() != null) {
            Session session = terminalSession.getSession();
            if (session.isOpen()) {
                session.getBasicRemote().sendText(text);
            }
        }
    }

    public void groupMessage(String message) {
        for (TerminalSession item : sessionStore.values()) {
            try {
                item.getSession().getBasicRemote().sendText(message);
            } catch (IOException ignored) {
            }
        }
    }

}
