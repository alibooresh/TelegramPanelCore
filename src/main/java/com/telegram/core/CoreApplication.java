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
//        String server = "castle.ultrasam.info";
//        int port = 443;
//        boolean enable = true;
//        String secret = "1603010200010001fc030386e24c3add";
//        TdApi.ProxyType type = new TdApi.ProxyTypeMtproto(secret);
//        TdApi.AddProxy addProxy = new TdApi.AddProxy(server, port, enable, type);
//        client.send(addProxy, result -> {
//            if (result instanceof TdApi.Proxy) {
//                System.out.println("Proxy added successfully: " + ((TdApi.Proxy) result).id);
//            } else if (result instanceof TdApi.Error) {
//                System.err.println("Failed to add proxy: " + ((TdApi.Error) result).message);
//            } else {
//                System.out.println("Unexpected response: " + result);
//            }
//        });
        System.out.println("Disabling proxy: ");

        client.send(new TdApi.DisableProxy(), res -> {
            if (res instanceof TdApi.Ok) {
                System.out.println("Proxy disabled successfully.");
            } else if (res instanceof TdApi.Error) {
                TdApi.Error err = (TdApi.Error) res;
                System.err.println("Failed to disable proxy " + "proxy.id" + ": " + err.message);
            }
        });
        TdApi.SetTdlibParameters request = new TdApi.SetTdlibParameters();
        request.databaseDirectory = "tdlib";
//        request.useTestDc = true;
        request.useMessageDatabase = true;
        request.useSecretChats = true;
        request.apiId = 17545990;
        request.apiHash = "e981c00b0db88ebe6335c09acbfc6bd4";
        request.systemLanguageCode = "en";
        request.deviceModel = "Desktop";
        request.applicationVersion = "1.1";
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
