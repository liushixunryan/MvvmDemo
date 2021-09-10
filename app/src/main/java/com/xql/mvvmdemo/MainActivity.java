package com.xql.mvvmdemo;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import com.xql.annotation.BindPath;
import com.xql.arouter.ARouter;
import com.xql.basic.activity.BasicActivity;
import com.xql.mvvmdemo.databinding.ActivityMainBinding;
import com.xql.mvvmdemo.vm.MainVM;

@BindPath(key = "main/main")
public class MainActivity extends BasicActivity<ActivityMainBinding, MainVM> {

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        //点击事件
        mBinding.setOnClickLister(MainActivity.this);
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        if (view.getId() == R.id.top) {
            Log.e(TAG, "OnSingleClickListener: ");
            ARouter.getInstance().jumpActivity("login/login", null);
        }
    }


}