package com.xql.basic.application;

import android.app.Application;
import androidx.multidex.MultiDex;
import com.xql.arouter.ARouter;

/**
 * @ClassName: BasicApplication
 * @Description: 全局Application
 * @CreateDate: 2021/9/10 9:15
 * @UpdateUser: RyanLiu
 */

public class BasicApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * Arouter
         */
        //arouter注册
        ARouter.getInstance().init(this);
        /**
         *MultiDex
         */
        // Dex文件超出限制
        MultiDex.install(this);
    }
}
