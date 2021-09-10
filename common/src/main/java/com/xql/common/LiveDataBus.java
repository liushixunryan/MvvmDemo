package com.xql.common;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LiveDataBus {
    private static LiveDataBus liveDataBus = new LiveDataBus();
    //有一个管理所有liveData对象的容器
    private Map<String, BusMutableLiveData<Object>> bus;

    private LiveDataBus(){
        bus = new HashMap<>();
    }

    public static LiveDataBus getInstance(){
        return liveDataBus;
    }

    /**
     * 存和取一体
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public synchronized<T> BusMutableLiveData<T> with(String key, Class<T> clazz){
        if (!bus.containsKey(key)){
            bus.put(key,new BusMutableLiveData<>());
        }
        return (BusMutableLiveData<T>) bus.get(key);
    }

    public class BusMutableLiveData<T> extends MutableLiveData<T> {
        //false : 需要粘性事件   true : 不需要粘性事件
        private boolean isViscosity = false;

        public void observe(@NonNull LifecycleOwner owner, boolean isViscosity, @NonNull Observer<T> observer) {
            this.isViscosity = isViscosity;
            observe(owner,observer);
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            super.observe(owner, observer);
            if (isViscosity){
                //通过反射  获取到mVersion   获取到mLastVersion   将mVersion的值给mLastVersion
                try {
                    hook(observer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * hook技术的实现方法  拦截onChange方法的执行
         */
        private void hook(Observer<? super T> observer) throws Exception{
            //获取到liveData的类对象
            Class<LiveData> liveDataClass = LiveData.class;
            //根据类对象获取到mversion的反射对象
            Field mVersionField = liveDataClass.getDeclaredField("mVersion");
            //打开权限
            mVersionField.setAccessible(true);
            //获取到mObservers的反射对象
            Field mObserversField = liveDataClass.getDeclaredField("mObservers");
            //打开权限
            mObserversField.setAccessible(true);
            //从当前的LiveData对象中获取mObservers这个成员变量在当前对象中的值
            Object mObservers = mObserversField.get(this);
            //获取到mObservers这个map的get方法
            Method get = mObservers.getClass().getDeclaredMethod("get",Object.class);
            //打开权限
            get.setAccessible(true);
            //执行get方法
            Object invokeEntry = get.invoke(mObservers,observer);
            //定义一个空对象  LifecycleBoundObserver
            Object observerWrapper = null;
            if (invokeEntry != null && invokeEntry instanceof Map.Entry){
                observerWrapper = ((Map.Entry)invokeEntry).getValue();
            }
            if (observerWrapper == null){
                throw new NullPointerException("observerWrapper不能为空");
            }

            //得到observerWrapper的类对象
            Class<?> aClass = observerWrapper.getClass().getSuperclass();
            //获取mLastVersion的发射对象
            Field mLastVersionField = aClass.getDeclaredField("mLastVersion");
            //打开权限
            mLastVersionField.setAccessible(true);
            //得到mversion的值
            Object o = mVersionField.get(this);
            //把它的值赋值给mLastVersion
            mLastVersionField.set(observerWrapper,o);
        }
    }
}
