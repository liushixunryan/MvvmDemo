package com.xql.mvvmdemo;

import com.xql.basic.activity.BasicActivity;
import com.xql.mvvmdemo.databinding.ActivityMainBinding;
import com.xql.mvvmdemo.vm.MainVM;

public class MainActivity extends BasicActivity<ActivityMainBinding, MainVM> {

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }


}