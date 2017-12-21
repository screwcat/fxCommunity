package com.taiji.fxsqjw.views.rest;

import com.taiji.fxsqjw.views.utils.StringUtil;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lightkin on 17-8-6.
 */

public class JojtApiUtils {
    //public static final String BASEURL="http://ingraces.ticp.net:8081/JBox-1.0/";
    //public static final String BASEURL = "http://10.82.133.190:8080/service_center/";
    //public static final String BASEURL="http://10.82.181.19:8730/";
    //public static final String BASEURL="http://192.168.20.70:19107/";
    public static final String BASEURL = "http://10.82.181.71:8730/";

    public static <T> T createService(final Class<T> serviceClass) {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .build();
        return adapter.create(serviceClass);
    }

    public static void userLogin(String userId, String passwd, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.userLogin(userId, passwd)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.print("123");
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(Map map) {
                        if (map == null || map.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(map);
                        }
                    }
                });
    }

    public static void updateCurrentLocation(String userId, double lat, double lon, long captureTime, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.updateCurrentLocation(userId, lat, lon, captureTime).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(String result) {
                        if (StringUtil.isEmpty(result)) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(result);
                        }
                    }
                });
    }

    public static void checkingSignMap(String userId, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.checkingSignMap(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.print("123");
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(List list) {
                        if (list == null || list.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(list);
                        }
                    }
                });
    }

    public static void checkingSignList(String userId, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.checkingSignList(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(List list) {
                        if (list == null || list.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(list);
                        }
                    }
                });
    }

    public static void getStressObject(int currentPage, final String searchContent, String currentUserId, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.stressObjectQuery(currentPage, searchContent, currentUserId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(Map map) {
                if (map == null || map.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(map);
                }
            }
        });
    }

    public static void personMessageAdd(String userId, String mldzid, String credential_type, String credential_code, String phone, String work, String unit, String location, String is_rent, String liver_type, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.personMessage(userId, mldzid, credential_type, credential_code, phone, work, unit, location, is_rent, liver_type).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(Map map) {
                if (map == null || map.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(map);
                }
            }
        });
    }

    public static void entrustLiveAdd(String userId, String person_id, String lodge_reason, String lodge_type, String begin_time, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.entrustLive(userId, person_id, lodge_reason, lodge_type, begin_time).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(Map map) {
                if (map == null || map.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(map);
                }
            }
        });
    }

    public static void shackLiveAdd(String userId, String person_id, String census_register_type, String shack_time, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.shackLive(userId, person_id, census_register_type, shack_time).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(Map map) {
                if (map == null || map.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(map);
                }
            }
        });
    }

    public static void houseMessageAdd(String userId, String mldzid, String house_character, String house_type, String house_use, String is_rent, String owner_credential_type, String address, String room_number, String square, String owner_credential_code, String owner_phone, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.houseMessage(userId, mldzid, house_character, house_type, house_use, is_rent, owner_credential_type, address, room_number, square, owner_credential_code, owner_phone).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(Map map) {
                if (map == null || map.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(map);
                }
            }
        });
    }

    public static void hireHouseAdd(String userId, String house_id, String credential_type, String room_num, String rent_square, String rent_time, String money, String relation, String credential_code, String phone, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.hireHouse(userId, house_id, credential_type, room_num, rent_square, rent_time, money, relation, credential_code, phone).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(Map map) {
                if (map == null || map.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(map);
                }
            }
        });
    }

    public static void getInstruct(int currentPage, String searchContent, String userId, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.getInstruct(currentPage, searchContent, userId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(Map map) {
                if (map == null || map.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(map);
                }
            }
        });
    }


    public static void getWaitProcess(int currentPage, String currentUser, String searchContent, String signingStatus, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.getWaitProcess(currentPage, currentUser, searchContent, signingStatus).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(Map map) {
                if (map == null || map.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(map);
                }
            }
        });
    }

    public static void waitProcessDetail(String noticeId, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.waitProcessDetail(noticeId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(Map map) {
                if (map == null || map.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(map);
                }
            }
        });
    }

    public static void waitProcessSign(String userId, String noticeId, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.waitProcessSign(userId, noticeId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(Map map) {
                if (map == null || map.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(map);
                }
            }
        });
    }

    public static void threeLevelFireDetail(String unitCode, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.threeLevelFireDetail(unitCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.print("123");
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(Map map) {
                        if (map == null || map.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(map);
                        }
                    }
                });

    }

    public static void threeLevelFireDetailInfo(String id, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.threeLevelFireDetailInfo(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.print("123");
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(Map map) {
                        if (map == null || map.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(map);
                        }
                    }
                });

    }

    public static void hiddenDangerDealResult(String id, String dealProject, String dealResult, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.hiddenDangerDealResult(id, dealProject, dealResult)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.print("123");
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(Map map) {
                        if (map == null || map.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(map);
                        }
                    }
                });
    }

    public static void stressObjectWrite(String userId, String objectId, String describe, String xsjz, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.stressObjectWrite(userId, objectId, describe, xsjz)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.print("123");
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(Map map) {
                        if (map == null || map.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(map);
                        }
                    }
                });
    }

    public static void instructQueryDetail(String uuid, String receiverId, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.instructQueryDetail(uuid, receiverId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.print("Error");
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(Map map) {
                        if (map == null || map.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(map);
                        }
                    }
                });
    }

    public static void instructSign(String uuid, String reportContent, String status, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.instructSign(uuid, reportContent, status).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(Map map) {
                if (map == null || map.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(map);
                }
            }
        });
    }

    public static void instructDispatch(String noticeTitle, String noticeContent, String senderId, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.instructDispatch(noticeTitle, noticeContent, senderId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(Map map) {
                if (map == null || map.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(map);
                }
            }
        });
    }

    public static void threeLevelFireQuery(String currentPage, String userId, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.threeLevelFireQuery(currentPage, userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.print("123");
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(Map map) {
                        if (map == null || map.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(map);
                        }
                    }
                });

    }

    //校园安全开始
    public static void campusSecurityDetail(String unitCode, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.campusSecurityDetail(unitCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.print("123");
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(Map map) {
                        if (map == null || map.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(map);
                        }
                    }
                });

    }

    public static void campusSecurityDetailInfo(String id, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.campusSecurityDetailInfo(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.print("123");
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(Map map) {
                        if (map == null || map.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(map);
                        }
                    }
                });

    }

    public static void campusSecurityDealResult(String id, String dealProject, String dealResult, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.campusSecurityDealResult(id, dealProject, dealResult)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.print("123");
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(Map map) {
                        if (map == null || map.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            System.out.print("135");
                            apiCallback.onSuccess(map);
                            System.out.print("246");
                        }
                    }
                });
    }

    public static void campusSecurityQuery(String unitCode, String userId, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.campusSecurityQuery(unitCode, userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.print("123");
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(Map map) {
                        if (map == null || map.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(map);
                        }
                    }
                });

    }

    //校园安全结束
    public static void instructDispatcherQuery(String policeNum, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.instructDispatcherQuery(policeNum).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(Map map) {
                if (map == null || map.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(map);
                }
            }
        });
    }

    public static void addressSearchTask(String dzmc, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.addressSearchTask(dzmc).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(List list) {
                if (list == null || list.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(list);
                }
            }
        });
    }

    public static void waitProcessNum(String receiverId, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.waitProcessNum(receiverId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.print("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.print("Error");
                apiCallback.onError(); // 失败
            }

            @Override
            public void onNext(Map map) {
                if (map == null || map.size() == 0) {
                    apiCallback.onError(); // 失败
                } else {
                    apiCallback.onSuccess(map);
                }
            }
        });
    }

    public static void threeLevelFireQueryFrame(String curPage, String searchContent, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.threeLevelFireQueryFrame(curPage, searchContent)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.print("123");
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(Map map) {
                        if (map == null || map.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(map);
                        }
                    }
                });

    }

    public static void campusSecurityQueryFrame(String curPage, String searchContent, final ApiCallBack apiCallback) {
        RestApi apiService = createService(RestApi.class);
        apiService.campusSecurityQueryFrame(curPage, searchContent)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.print("123");
                        apiCallback.onError(); // 失败
                    }

                    @Override
                    public void onNext(Map map) {
                        if (map == null || map.size() == 0) {
                            apiCallback.onError(); // 失败
                        } else {
                            apiCallback.onSuccess(map);
                        }
                    }
                });

    }

    public interface ApiCallBack {
        void onSuccess(Object obj);

        void onError();
    }

}
