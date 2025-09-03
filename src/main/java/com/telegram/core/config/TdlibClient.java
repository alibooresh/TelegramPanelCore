// TdlibClient.java
package com.telegram.core.config;

import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TdlibClient {
    private Client client;

    @PostConstruct
    public void init() {
        Client.setLogMessageHandler(0, new TdApiHandler.LogMessageHandler());
        try {
            Client.execute(new TdApi.SetLogVerbosityLevel(0));
            Client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("tdlib.log", 1 << 27, false)));
        } catch (Client.ExecutionException e) {
            throw new RuntimeException("TDLib log setup failed", e);
        }
        client = Client.create(new TdApiHandler.UpdateHandler(), null, null);
    }

    public Client getClient() {
        return client;
    }
}
