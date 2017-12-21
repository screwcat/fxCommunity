package com.taiji.fxsqjw.views.utils;


import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * @version :
 */
public class CheckUpdateUtils {


    /**
     * 检查更新
     */
    @SuppressWarnings("unused")
    public static void checkUpdate(String appCode, String curVersion,final CheckCallBack updateCallback) {
     ApiService apiService=   ServiceFactory.createServiceFrom(ApiService.class);
        apiService.getUpdateInfo()//测试使用
                //apiService.getUpdateInfo(appCode, curVersion)//开发过程中可能使用的
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateAppInfo>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.print("123");
                    }

                    @Override
                    public void onNext(UpdateAppInfo updateAppInfo) {
                        if (updateAppInfo.error_code == 0 || updateAppInfo.data == null ||
                                updateAppInfo.data.updateurl == null) {
                            updateCallback.onError(); // 失败
                        } else {
                            updateCallback.onSuccess(updateAppInfo);
                        }
                    }
                });
    }


    public interface CheckCallBack{
        void onSuccess(UpdateAppInfo updateInfo);
        void onError();
    }


}
