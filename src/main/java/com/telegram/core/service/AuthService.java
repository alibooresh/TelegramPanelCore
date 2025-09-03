// AuthService.java
package com.telegram.core.service;

import com.telegram.core.config.TdlibClient;
import com.telegram.core.dto.ProxyDto;
import com.telegram.core.entity.InquiryDetail;
import com.telegram.core.session.SessionManager;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@Service
public class AuthService {

    static TdApi.Object result;
    private final TdlibClient tdlibClient;
    private final SessionManager sessionManager;

    public AuthService(TdlibClient tdlibClient, SessionManager sessionManager) {
        this.tdlibClient = tdlibClient;
        this.sessionManager = sessionManager;
    }

    public TdApi.Object sendPhoneNumber(String phoneNumber) throws InterruptedException, ExecutionException {
        Client client = tdlibClient.getClient();
        CompletableFuture<TdApi.Object> future = new CompletableFuture<>();

        client.send(new TdApi.SetAuthenticationPhoneNumber(phoneNumber, null), object -> {
            if (object.getConstructor() == TdApi.Ok.CONSTRUCTOR) {
                sessionManager.set("userId", new SessionManager.Session(phoneNumber, false));
            } else {
                System.err.println("Failed to send phone: " + object);
            }
            future.complete(object);
        });

        return future.get();
    }

    public TdApi.Object checkCode(String code) throws InterruptedException, ExecutionException {
        Client client = tdlibClient.getClient();
        CompletableFuture<TdApi.Object> future = new CompletableFuture<>();

        client.send(new TdApi.CheckAuthenticationCode(code), object -> {
            if (object.getConstructor() == TdApi.Ok.CONSTRUCTOR) {
                SessionManager.Session session = sessionManager.get("userId");
                sessionManager.set("userId", new SessionManager.Session(session.phoneNumber(), true));
            } else {
                System.err.println("Code verification failed: " + object);
            }
            future.complete(object);
        });

        return future.get();
    }

    public TdApi.Object logout() throws InterruptedException, ExecutionException {
        Client client = tdlibClient.getClient();
        CompletableFuture<TdApi.Object> future = new CompletableFuture<>();

        client.send(new TdApi.LogOut(), object -> {
            if (object.getConstructor() == TdApi.Ok.CONSTRUCTOR) {
                System.err.println("Logged out ");
            } else {
                System.err.println("Error loggin out: " + object);
            }
            future.complete(object);
        });

        return future.get();
    }
    public boolean isAuthorized(String userId) {
        return sessionManager.isLoggedIn(userId);
    }

    public TdApi.Object close() {
        Client client = tdlibClient.getClient();
        client.send(new TdApi.Close(), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                result = object;
            }
        });
        return result;
    }

    public void setProxy(ProxyDto proxyDto) {
        Client client = tdlibClient.getClient();
        TdApi.ProxyType type = new TdApi.ProxyTypeMtproto(proxyDto.getSecret());
        TdApi.AddProxy addProxy = new TdApi.AddProxy(proxyDto.getServer(), proxyDto.getPort(), true, type);
        client.send(addProxy, result -> {
            if (result instanceof TdApi.Proxy) {
                System.out.println("Proxy added successfully: " + ((TdApi.Proxy) result).id);
            } else if (result instanceof TdApi.Error) {
                System.err.println("Failed to add proxy: " + ((TdApi.Error) result).message);
            } else {
                System.out.println("Unexpected response: " + result);
            }
        });
    }
    public void disableAllProxies() throws InterruptedException {
        Client client = tdlibClient.getClient();
        CountDownLatch latch = new CountDownLatch(1);

        client.send(new TdApi.GetProxies(), object -> {
            if (object instanceof TdApi.Proxies) {
                TdApi.Proxies proxies = (TdApi.Proxies) object;

                for (TdApi.Proxy proxy : proxies.proxies) {
                    if (proxy.isEnabled) {
                        System.out.println("Disabling proxy: " + proxy.id);

                        client.send(new TdApi.DisableProxy(), res -> {
                            if (res instanceof TdApi.Ok) {
                                System.out.println("Proxy " + proxy.id + " disabled successfully.");
                            } else if (res instanceof TdApi.Error) {
                                TdApi.Error err = (TdApi.Error) res;
                                System.err.println("Failed to disable proxy " + proxy.id + ": " + err.message);
                            }
                        });
                    }
                }
            } else if (object instanceof TdApi.Error) {
                TdApi.Error error = (TdApi.Error) object;
                System.err.println("Error getting proxies: " + error.message);
            }
            latch.countDown();
        });

        latch.await();
    }



    public void setParam() {
        Client client = tdlibClient.getClient();
        TdApi.SetTdlibParameters request = new TdApi.SetTdlibParameters();
        request.databaseDirectory = "tdlib";
        request.useMessageDatabase = true;
        request.useSecretChats = true;
        request.apiId = 94575;
        request.apiHash = "a3406de8d171bb422bb6ddf3bbd800e2";
        request.systemLanguageCode = "en";
        request.deviceModel = "Desktop";
        request.applicationVersion = "1.0";
        client.send(request, new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                if (object.getConstructor() == TdApi.Ok.CONSTRUCTOR) {

                }
            }
        });
    }
}