package com.telegram.core.config;

import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;

public class TdApiHandler {

    public static class LogMessageHandler implements Client.LogMessageHandler {
        @Override
        public void onLogMessage(int verbosityLevel, String message) {
            if (verbosityLevel == 0) {
                System.err.println("FATAL: " + message);
            } else {
                System.out.println("LOG: " + message);
            }
        }
    }

    public static class UpdateHandler implements Client.ResultHandler {
        @Override
        public void onResult(TdApi.Object object) {
            // Simple logging, can be extended for real-time updates
            System.out.println("Update received: " + object);
        }
    }
}
