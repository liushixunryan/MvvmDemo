package com.xql.loading;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * 标题：提示窗
 * 描述：通用提示窗
 * 作者：ryanliu
 * 时间：2021-09-10
 * XML: dialog_tip
 */
public class TipDialog extends Dialog implements View.OnClickListener {

    private TextView contentTv, submitTv, cancelTv;
    private View lineView;
    private SubmitListener listener;
    private CancelListener cancelListener;

    /**
     *初始化提示内容、确认、取消组件，并设置点击监听
     */
    public TipDialog(Context context) {
        super(context);
        //设置对话框背景透明
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setContentView(R.layout.dialog_tip);
            contentTv = findViewById(R.id.content);
            submitTv = findViewById(R.id.submit);
            cancelTv = findViewById(R.id.cancel);
            lineView = findViewById(R.id.line_view);
            cancelTv.setOnClickListener(this);
            submitTv.setOnClickListener(this);
            setCanceledOnTouchOutside(false);
            setCancelable(false);
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
    }

    /**
     * 设置提示信息
     * @param message 提示信息
     */
    public TipDialog setMessage(String message) {
        contentTv.setText(message);
        return this;
    }

    /**
     * 设置确认按钮信息
     * @return
     */
    public TipDialog setsubmitText(String message) {
        submitTv.setText(message);
        return this;
    }

    /**
     * 是否显示取消按钮
     * @param isShow
     * @return
     */
    public TipDialog isShowCancel(boolean isShow) {
        cancelTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
        lineView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 确认按钮点击监听
     * @param l
     * @return
     */
    public TipDialog setSubmitListener(SubmitListener l) {
        this.listener = l;
        return this;
    }

    /**
     * 取消按钮点击监听
     * @param cancelListener
     */
    public void setCancelListener(CancelListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    /**
     * 显示提示窗
     */
    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 隐藏提示窗
     */
    @Override
    public void dismiss() {
        Log.e("sansuiban", "dismiss");
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消、确认点击事件
     * @param v 组件
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            dismiss();
            if (cancelListener != null) {
                cancelListener.onTipDialogCancel();
            }
        } else if (v.getId() == R.id.submit) {
            dismiss();

            if (listener != null)
                listener.onTipDialogSubmit();

        }
    }

    /**
     * 确认按钮回调接口
     */
    public interface SubmitListener {
        void onTipDialogSubmit();
    }

    /**
     * 取消按钮回调接口
     */
    public interface CancelListener {
        void onTipDialogCancel();
    }
}
