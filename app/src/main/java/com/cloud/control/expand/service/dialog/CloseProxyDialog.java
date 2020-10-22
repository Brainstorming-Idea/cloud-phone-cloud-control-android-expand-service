package com.cloud.control.expand.service.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.interfaces.MenuCallback;


/**
 * Author：abin
 * Date：2020/10/13
 * Description：关闭代理弹框
 */
public class CloseProxyDialog extends Dialog implements View.OnClickListener {

    private MenuCallback callback;

    public CloseProxyDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
        setContentView(R.layout.dialog_close_proxy);
        getWindow().setGravity(Gravity.CENTER);
        initView();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

        }
        return false;
    }

    private void initView() {
        findViewById(R.id.btn_dialog_right).setOnClickListener(this);
        findViewById(R.id.btn_dialog_left).setOnClickListener(this);
        findViewById(R.id.rl_dialog).setOnClickListener(this);
    }

    private void setMenuCallback(MenuCallback callback) {
        this.callback = callback;
    }

    /**
     * 两个按钮的样式
     *
     * @param content
     * @param leftStr
     * @param rightStr
     */
    private void setStyle(String content, String leftStr, String rightStr) {
        if (TextUtils.isEmpty(content)) {
            findViewById(R.id.tv_dialog_content).setVisibility(View.GONE);
        } else {
            findViewById(R.id.tv_dialog_content).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tv_dialog_content)).setText(content);
        }
        findViewById(R.id.tv_vertical_line).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_dialog_left).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.btn_dialog_left)).setText(leftStr);
        ((Button) findViewById(R.id.btn_dialog_right)).setText(rightStr);
        if (TextUtils.isEmpty(leftStr)) {
            findViewById(R.id.tv_vertical_line).setVisibility(View.GONE);
            findViewById(R.id.btn_dialog_left).setVisibility(View.GONE);
        } else {
            findViewById(R.id.tv_vertical_line).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_dialog_left).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示俩个按钮
     *
     * @param context
     * @param title
     * @param content
     * @param leftStr
     * @param rightStr
     * @param callback
     */
    public static void show(Context context, String content, String leftStr, String rightStr,
                            MenuCallback callback) {
        CloseProxyDialog dialog = new CloseProxyDialog(context,
                R.style.dialog_style);
        dialog.setMenuCallback(callback);
        dialog.setStyle(content, leftStr, rightStr);
        dialog.show();
        dialog.initDialog(context, dialog);
    }


    /**
     * 初始化dialog的大小 背景
     *
     * @param context
     * @param dialog
     */
    private void initDialog(Context context, CloseProxyDialog dialog) {
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = (int) (size.x * 0.9);
        int height = size.y;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = width;
        lp.height = height;
        dialog.getWindow().setAttributes(lp);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_left:
                dismiss();
                callback.onLeftButtonClick(null);
                break;
            case R.id.tv_dialog_title:
                break;
            case R.id.btn_dialog_right:
                dismiss();
                callback.onRightButtonClick(null);
                break;
            default:
                dismiss();
                break;
        }
    }
}
