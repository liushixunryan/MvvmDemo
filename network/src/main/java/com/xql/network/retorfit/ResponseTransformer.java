package com.xql.network.retorfit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

/**
 * @ClassName: ResponseTransformer
 * @Description: 解析第一层bean
 * @CreateDate: 2021/9/10 11:20
 * @UpdateUser: RyanLiu
 */


public class ResponseTransformer<T> implements ObservableTransformer<BaseResult<T>, T> {

    @Override
    public ObservableSource<T> apply(Observable<BaseResult<T>> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .concatMap(tResponseResult -> {
                    if (tResponseResult.getData() != null) {
                        if (tResponseResult.getResult().toString().equals("true")) {
                            return createData(tResponseResult.getData());
                        } else {
                            return Observable.error(new Exception(tResponseResult.getNotice()));
                        }
                    } else {
                        if (tResponseResult.getResult()) {
                            return createData(tResponseResult.getData());
                        } else {
                            return Observable.error(new Exception(tResponseResult.getNotice()));
                        }
                    }
                });
    }

    private ObservableSource<? extends T> createData(T data) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(data);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
