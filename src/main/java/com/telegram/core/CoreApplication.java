package com.telegram.core;

import com.telegram.core.config.TdlibClient;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoreApplication {
    private static TdlibClient tdlibClient;

    public CoreApplication(TdlibClient tdlibClient) {
        CoreApplication.tdlibClient = tdlibClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
        Client client = tdlibClient.getClient();
        TdApi.SetTdlibParameters request = new TdApi.SetTdlibParameters();
        request.databaseDirectory = "tdlib";
        request.useMessageDatabase = true;
        request.useSecretChats = true;
        request.apiId = 17545990;
        request.apiHash = "e981c00b0db88ebe6335c09acbfc6bd4";
        request.systemLanguageCode = "en";
        request.deviceModel = "Desktop";
        request.applicationVersion = "1.0";
        client.send(request, new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                if (object.getConstructor() == TdApi.Ok.CONSTRUCTOR) {
                    System.out.println(object);
                }
            }
        });
    }

}
