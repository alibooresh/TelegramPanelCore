package com.telegram.core.service;

import com.telegram.core.config.TdlibClient;
import com.telegram.core.session.SessionManager;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final TdlibClient tdlibClient;
    private final SessionManager sessionManager;
    static TdApi.Object result;


    public MessageService(TdlibClient tdlibClient, SessionManager sessionManager) {
        this.tdlibClient = tdlibClient;
        this.sessionManager = sessionManager;
    }


    public TdApi.Object getMessages(Long chatId, long[] messageIds) {
        TdApi.User user = null;
        Client client = tdlibClient.getClient();
        client.send(new TdApi.GetMessages(chatId, messageIds),
                new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        result = object;
                    }
                });
        return result;
    }

    public TdApi.Object getMessageProperties(Long chatId, Long messageId) {
        TdApi.User user = null;
        Client client = tdlibClient.getClient();
        client.send(new TdApi.GetMessageProperties(chatId, messageId),
                new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        result = object;
                    }
                });
        return result;
    }

    public TdApi.Object getMessageReadDate(Long chatId, Long messageId) {
        TdApi.User user = null;
        Client client = tdlibClient.getClient();
        client.send(new TdApi.GetMessageReadDate(chatId, messageId),
                new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        result = object;
                    }
                });
        return result;
    }

    public TdApi.Object getDatabaseStatistics() {
        TdApi.User user = null;
        Client client = tdlibClient.getClient();
        client.send(new TdApi.GetDatabaseStatistics(),
                new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        result = object;
                    }
                });
        return result;
    }

}
