package com.xql.mvvmdemo.vm;


import androidx.lifecycle.MutableLiveData;

import com.xql.basic.viewmodel.BasicViewModel;

/**
 * @ClassName: MainVM
 * @Description: java类作用描述
 * @CreateDate: 2021/9/10 10:32
 * @UpdateUser: RyanLiu
 */

public class MainVM extends BasicViewModel {
    public MutableLiveData<String> getTomorrow(String constellationname) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
//        RetrofitManager.newInstance().creat(APIService.class).getTomorrow(constellationname).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<TomorrowBean>() {
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//                Log.e("sansuiban", "aaaaa: " + d);
//            }
//
//            @Override
//            public void onNext(@NonNull TomorrowBean tomorrowBean) {
//                liveData.postValue(tomorrowBean);
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                ToastUtils.showLong("网络错误");
//            }
//
//            @Override
//            public void onComplete() {
//                Log.e("sansuiban", "bbbb: ");
//            }
//        });
        return liveData;
    }
}
