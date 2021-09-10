package com.xql.arouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

/**
 * @ClassName: ARouter
 * @Description: 全局ARouter
 * @CreateDate: 2021/9/10 9:18
 * @UpdateUser: RyanLiu
 */
public class ARouter {
    private Context context;
    private static ARouter aRouter = new ARouter();
    //路由表  存储所有Activity类对象
    private Map<String, Class<? extends Activity>> map;

    private ARouter() {
        map = new HashMap<>();
    }

    public static ARouter getInstance() {
        return aRouter;
    }

    /**
     * 将类对象添加进路由表的方法
     *
     * @param key
     * @param clazz
     */
    public void addActivity(String key, Class<? extends Activity> clazz) {
        if (key != null && clazz != null && !map.containsKey(key)) {
            map.put(key, clazz);
        }
    }

    /**
     * 将类对象添加进路由表的方法
     *
     * @param key
     */
    public Class<? extends Activity> getActivity(String key) {
        if (key != null && map.containsKey(key)) {
            return map.get(key);
        }
        return null;
    }

    public void init(Context context) {
        //调用生成的工具类的方法
        this.context = context;
        List<String> className = getClassName("com.xql.util");
        for (String s : className) {
            try {
                Class<?> aClass = Class.forName(s);
                Object o = aClass.newInstance();
                if (o instanceof IRouter) {
                    IRouter iRouter = (IRouter) o;
                    iRouter.putActivity();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过包名获取这个包下面的所有的类名
     * @param packageName
     * @return
     */
    private List<String> getClassName(String packageName) {
        //创建一个class对象的集合
        List<String> classList = new ArrayList<>();
        try {
            //把当前应有的apk储存路径给dexfile
            DexFile df = new DexFile(context.getPackageCodePath());
            Enumeration<String> entries = df.entries();
            while (entries.hasMoreElements()) {
                String className = entries.nextElement();
                if (className.contains(packageName)) {
                    classList.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classList;
    }

    /**
     *跳转窗体的方法
     * @param key
     * @param bundle
     */
    public void jumpActivity(String key, Bundle bundle) {
        Class<? extends Activity> aClass = map.get(key);
        if (aClass == null) {
            return;
        }
        Intent intent = new Intent(context, aClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
