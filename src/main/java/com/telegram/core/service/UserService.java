package com.telegram.core.service;

import com.telegram.core.config.TdlibClient;
import com.telegram.core.entity.InquiryDetail;
import com.telegram.core.session.SessionManager;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final TdlibClient tdlibClient;
    private final SessionManager sessionManager;
    static TdApi.Object result;
    static TdApi.User user;
    static InquiryDetail inquiryDetail;
    private static boolean isFound = false;


    public UserService(TdlibClient tdlibClient, SessionManager sessionManager) {
        this.tdlibClient = tdlibClient;
        this.sessionManager = sessionManager;
    }

    public TdApi.User getMe() {
        Client client = tdlibClient.getClient();
        CompletableFuture<TdApi.User> future = new CompletableFuture<>();

        client.send(new TdApi.GetMe(), object -> {
            if (object instanceof TdApi.User) {
                future.complete((TdApi.User) object);
            } else {
                future.completeExceptionally(new RuntimeException("Failed: " + object));
            }
        });

        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException("Error getting user", e);
        }
    }

    public TdApi.User getUserByNumber(String number) {
        TdApi.User user = null;
        Client client = tdlibClient.getClient();
        client.send(new TdApi.GetContacts(), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                TdApi.Users users = (TdApi.Users) object;
                for (long userId : users.userIds) {
                    client.send(new TdApi.GetUser(userId), new Client.ResultHandler() {
                        @Override
                        public void onResult(TdApi.Object object) {
                            TdApi.User user = (TdApi.User) object;
                            if (user.phoneNumber.equals(number)) {
                                result = user;
                                isFound = true;
                            }
                        }
                    });
                    if (isFound) {
                        break;
                    }
                }
            }
        });
        user = (TdApi.User) result;
        return user;
    }


    public TdApi.Object getUserFullInfo(Long userId) {
        TdApi.User user = null;
        Client client = tdlibClient.getClient();
        client.send(new TdApi.GetUserFullInfo(userId),
                new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        result = object;
                    }
                });
        return result;
    }

    public TdApi.Object getUserLink() {
        TdApi.User user = null;
        Client client = tdlibClient.getClient();
        client.send(new TdApi.GetUserFullInfo(),
                new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        result = object;
                    }
                });
        return result;
    }

    public TdApi.Object getContacts() {
        TdApi.User user = null;
        Client client = tdlibClient.getClient();
        client.send(new TdApi.GetUserFullInfo(),
                new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        result = object;
                    }
                });
        return result;
    }

    public TdApi.Object getApplicationConfig() {
        TdApi.User user = null;
        Client client = tdlibClient.getClient();
        client.send(new TdApi.GetApplicationConfig(),
                new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        result = object;
                    }
                });
        return result;
    }

    public TdApi.Object getActiveSessions() {
        TdApi.User user = null;
        Client client = tdlibClient.getClient();
        client.send(new TdApi.GetActiveSessions(),
                new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        result = object;
                    }
                });
        return result;
    }

    public TdApi.Object getTimeZones() {
        TdApi.User user = null;
        Client client = tdlibClient.getClient();
        client.send(new TdApi.GetTimeZones(),
                new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        result = object;
                    }
                });
        return result;
    }

    public TdApi.Object getAccountTtl() {
        TdApi.User user = null;
        Client client = tdlibClient.getClient();
        client.send(new TdApi.GetAccountTtl(),
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

    public CompletableFuture<InquiryDetail> getUserByPhone1(String phone) {
        CompletableFuture<InquiryDetail> future = new CompletableFuture<>();
        Client client = tdlibClient.getClient();
        client.send(new TdApi.SearchUserByPhoneNumber(phone, false),
                object -> {
                    if (object != null) {
                        InquiryDetail result = mapToPhoneInquiryDto(phone, object);
                        future.complete(result);
                    } else {
                        future.completeExceptionally(new RuntimeException("No user found"));
                    }
                });

        return future;
    }

    public InquiryDetail getUserByPhone(String phone) {
        inquiryDetail = null;
        Client client = tdlibClient.getClient();
        client.send(new TdApi.SearchUserByPhoneNumber(phone, false),
                new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        if (object != null) {
                            inquiryDetail = mapToPhoneInquiryDto(phone, object);
                        }
                    }
                });

        return inquiryDetail;
    }

    public InquiryDetail addContact(String phone) {
        InquiryDetail phoneInquiryDto = new InquiryDetail();
        Client client = tdlibClient.getClient();
        client.send(new TdApi.SearchUserByPhoneNumber(phone, false),
                new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        result = object;
                    }
                });
        if (result != null) {
            phoneInquiryDto = mapToPhoneInquiryDto(phone, result);
            result = null;
        }
        return phoneInquiryDto;
    }

    public List<InquiryDetail> getUserListByPhoneList(List<String> phoneList) {
        List<InquiryDetail> inquiryDetails = new ArrayList<>();
        Client client = tdlibClient.getClient();
        phoneList.forEach(number -> {
            client.send(new TdApi.SearchUserByPhoneNumber(number, false),
                    new Client.ResultHandler() {
                        @Override
                        public void onResult(TdApi.Object object) {
                            if (object != null) {
                                inquiryDetails.add(mapToPhoneInquiryDto(number, object));
                            }
                        }
                    });
        });
        return inquiryDetails;
    }

    public List<InquiryDetail> getUserListByPhoneList1(List<String> phoneList) {
        Client client = tdlibClient.getClient();
        List<CompletableFuture<InquiryDetail>> futures = new ArrayList<>();

        for (String number : phoneList) {
            CompletableFuture<InquiryDetail> future = new CompletableFuture<>();
            client.send(new TdApi.SearchUserByPhoneNumber(number, false), object -> {
                if (object != null) {
                    future.complete(mapToPhoneInquiryDto(number, object));
                } else {
                    future.complete(null); // یا future.completeExceptionally(new RuntimeException(...));
                }
            });
            futures.add(future);
        }


        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public InquiryDetail getUserInfoByPhone(String number) throws ExecutionException, InterruptedException {
        Client client = tdlibClient.getClient();
        CompletableFuture<InquiryDetail> future = new CompletableFuture<>();
        client.send(new TdApi.SearchUserByPhoneNumber(number, false), object -> {
            if (object != null) {
                future.complete(mapToPhoneInquiryDto(number, object));
            } else {
                future.complete(null); // یا future.completeExceptionally(new RuntimeException(...));
            }
        });
        return future.get();
    }


    private InquiryDetail mapToPhoneInquiryDto(String phone, TdApi.Object object) {
        InquiryDetail result = new InquiryDetail();
        result.setPhoneNumber(phone);
        if (object instanceof TdApi.User) {
            TdApi.User user = (TdApi.User) object;
            result.setUserId(user.id);
            result.setFirstName(user.firstName);
            result.setLastName(user.lastName);
            if (user.usernames != null) {
                result.setUsernames(user.usernames.editableUsername);
            }
//            if (user.status != null) {
//                TdApi.UserStatusOffline lastStatus = (TdApi.UserStatusOffline) user.status;
//                Date date = new Date(lastStatus.wasOnline);
//                result.setStatus(date.toString());
//            }
        } else if (object instanceof TdApi.Error) {
            result.setInquiryStatus(((TdApi.Error) object).message);
        }
        return result;
    }

    public List<InquiryDetail> importAndUpdateUsers(List<InquiryDetail> inquiryDetailsBatch) throws InterruptedException {
        Client client = tdlibClient.getClient();


        TdApi.Contact[] contacts = inquiryDetailsBatch.stream()
                .map(detail -> new TdApi.Contact(
                        detail.getPhoneNumber(), // phoneNumber
                        "",                      // firstName
                        "",                      // lastName
                        "",                      // vCard
                        0                        // userId
                ))
                .toArray(TdApi.Contact[]::new);

        CountDownLatch latch = new CountDownLatch(1);

        client.send(new TdApi.ImportContacts(contacts), object -> {
            if (object instanceof TdApi.ImportedContacts) {
                TdApi.ImportedContacts imported = (TdApi.ImportedContacts) object;

                for (int i = 0; i < imported.userIds.length; i++) {
                    long userId = imported.userIds[i];

                    InquiryDetail detail = inquiryDetailsBatch.get(i);
                    detail.setUserId(userId);

                    if (userId == 0) {
                        detail.setStatus("NOT_FOUND");
                    } else {
                        detail.setStatus("FOUND");
                    }
                }
            } else {
                System.err.println("Failed to import contacts: " + object);
            }
            latch.countDown();
        });

        latch.await();

        return inquiryDetailsBatch;
    }


    public List<InquiryDetail> importAndGetUsers(List<String> phoneList) throws InterruptedException {
        List<InquiryDetail> inquiryDetails = new ArrayList<>();
        Client client = tdlibClient.getClient();


        TdApi.Contact[] contacts = phoneList.stream()
                .map(phone -> new TdApi.Contact(
                        phone,          // phoneNumber
                        "",             // firstName (optional)
                        "",             // lastName (optional)
                        "",             // vCard (optional)
                        0               // userId (0 چون هنوز نمی‌دونیم)
                ))
                .toArray(TdApi.Contact[]::new);

        CountDownLatch latch = new CountDownLatch(1);

        // 2. فراخوانی importContacts
        client.send(new TdApi.ImportContacts(contacts), object -> {
            if (object instanceof TdApi.ImportedContacts) {
                TdApi.ImportedContacts imported = (TdApi.ImportedContacts) object;

                for (int i = 0; i < imported.userIds.length; i++) {
                    long userId = imported.userIds[i];
                    String phone = phoneList.get(i);

                    InquiryDetail detail = new InquiryDetail();
                    detail.setPhoneNumber(phone);
                    detail.setUserId(userId);

                    inquiryDetails.add(detail);
                }
            } else {
                System.err.println("Failed to import contacts: " + object);
            }
            latch.countDown();
        });

        latch.await();

        return inquiryDetails;
    }

}

