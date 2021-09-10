package com.xql.basic.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.xql.basic.viewmodel.BasicViewModel;
import com.xql.loading.LoadingDialog;
import com.xql.loading.TipDialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: BasicActivity
 * @Description: 通用Activity
 * @CreateDate: 2021/9/10 10:19
 * @UpdateUser: RyanLiu
 */

public abstract class BasicActivity<B extends ViewDataBinding, VM extends BasicViewModel> extends AppCompatActivity {
    protected final String TAG = "sansuiban";
    // 等待窗对象
    private LoadingDialog loadingDialog;
    // 提示窗对象
    private TipDialog tipDialog;
    //databinding
    public B mBinding;
    //viewmodel
    public VM mViewModel;
    //全局context
    public Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //activityi管理
        ActivityCollector.addActivity(this);
        //初始化布局
        mBinding = DataBindingUtil.setContentView(this, layoutId());
        mBinding.setLifecycleOwner(this);
        //初始化dialog
        initDialog();
        //创建我们的ViewModel。
        createViewModel();
        //设置数据
        initData();
    }

    /**
     * 绑定viewmodel
     */
    public void createViewModel() {
        if (mViewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
                Log.i(TAG, "createViewModel: " + modelClass.toString());
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BasicViewModel.class;
            }
            mViewModel = (VM) new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(modelClass);
        }
    }

    /**
     * 初始化各种Dialog
     */
    private void initDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }

        if (tipDialog == null) {
            tipDialog = new TipDialog(this);
        }
    }

    /**
     * 显示等待Dialog
     */
    public void showLoading() {
        if (loadingDialog != null && !loadingDialog.isShowing())
            loadingDialog.show();
    }

    /**
     * 显示等待Dialog，可自定义显示内容
     */
    public LoadingDialog showLoading(String msg) {
        if (loadingDialog != null && !loadingDialog.isShowing())
            loadingDialog.setMessage(msg).show();
        return loadingDialog;
    }

    /**
     * 显示提示类型Dialog
     */
    public TipDialog showTipDialog(String msg, boolean isShowCancel, TipDialog.SubmitListener l) {

        if (tipDialog != null && !tipDialog.isShowing()) {

            tipDialog.setMessage(msg).isShowCancel(isShowCancel).setSubmitListener(l).show();
        } else if (tipDialog != null && tipDialog.isShowing()) {
            tipDialog.setMessage(msg);
        }
        return tipDialog;
    }

    /**
     * 隐藏等待Dialog
     */
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 隐藏提示类型Dialog
     */
    public void hideTipDialog() {
        if (tipDialog != null && tipDialog.isShowing()) {
            tipDialog.dismiss();
        }
    }

    /**
     * 初始化布局
     *
     * @return 布局id
     */
    protected abstract int layoutId();

    /**
     * 设置数据
     */
    protected abstract void initData();


    //活动管理器
    public static class ActivityCollector {

        public static List<Activity> activitys = new ArrayList<Activity>();

        /**
         * 向List中添加一个活动
         *
         * @param activity 活动
         */
        public static void addActivity(Activity activity) {

            activitys.add(activity);
        }

        /**
         * 从List中移除活动
         *
         * @param activity 活动
         */
        public static void removeActivity(Activity activity) {

            activitys.remove(activity);
        }

        /**
         * 将List中存储的活动全部销毁掉
         */
        public static void finishAll() {

            for (Activity activity : activitys) {

                if (!activity.isFinishing()) {

                    activity.finish();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity管理
        ActivityCollector.removeActivity(this);
    }
}
