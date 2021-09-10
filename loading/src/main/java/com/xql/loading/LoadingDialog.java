package com.xql.loading;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wang.avi.AVLoadingIndicatorView;

/**
 * 标题：等待窗
 * 描述：通用等待提示窗
 * 作者：ryanliu
 * 时间：2021-09-10
 * XML: dialog_loadding
 */
public class LoadingDialog extends Dialog implements View.OnClickListener {
    private AVLoadingIndicatorView avi;

    private TextView tv_text, tv_cancel;

    private CancelListener listener;

    /**
     * 初始化提示信息、取消按钮和等待动画
     *
     * @param context
     */
    public LoadingDialog(Context context) {
        super(context);
        //设置对话框背景透明
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setContentView(R.layout.dialog_loadding);
            tv_text = findViewById(R.id.tv_text);
            tv_cancel = findViewById(R.id.cancel);
            avi = findViewById(R.id.avi);
            tv_cancel.setOnClickListener(this);
            setCanceledOnTouchOutside(false);
            //            setCancelable(true);
        }
    }

    /**
     * 拦截触碰事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 设置取消按钮点击监听
     */
    public void setListener(CancelListener listener) {
        tv_cancel.setVisibility(View.VISIBLE);
        this.listener = listener;
    }

    /**
     * 为加载进度个对话框设置不同的提示消息
     *
     * @param message 给用户展示的提示信息
     * @return build模式设计，可以链式调用
     */
    public LoadingDialog setMessage(String message) {
        tv_text.setText(message);
        return this;
    }

    /**
     * 显示等待窗，并开始等待动画
     */
    @Override
    public void show() {
        startAnim();
        super.show();
    }

    /**
     * 隐藏等待窗，并停止等待动画
     */
    @Override
    public void dismiss() {
        stopAnim();
        super.dismiss();
    }

    /**
     * 开始等待动画
     */
    void startAnim() {
        avi.show();
        avi.smoothToShow();
    }

    /**
     * 停止等待动画
     */
    void stopAnim() {
        avi.hide();
        // or avi.smoothToHide();
    }

    /**
     * 取消按钮点击事件
     *
     * @param v 组件
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            dismiss();
            if (this.listener != null) {
                this.listener.onCancel();
                tv_text.setText("正在加载");
                tv_cancel.setVisibility(View.GONE);
                this.listener = null;
            }
        }
    }

    /**
     * 取消按钮监听接口
     */
    public interface CancelListener {
        void onCancel();
    }
}
