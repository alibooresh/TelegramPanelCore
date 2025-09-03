
// SessionManager.java
package com.telegram.core.session;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public void set(String userId, Session session) {
        sessions.put(userId, session);
    }

    public Session get(String userId) {
        return sessions.get(userId);
    }

    public boolean isLoggedIn(String userId) {
        Session session = sessions.get(userId);
        return session != null && session.isAuthorized();
    }

    public void clear(String userId) {
        sessions.remove(userId);
    }

    public record Session(String phoneNumber, boolean authorized) {
        public boolean isAuthorized() {
            return authorized;
        }
    }
}