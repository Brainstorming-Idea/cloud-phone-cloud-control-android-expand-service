package com.cloud.control.expand.service.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.entity.ExpandServiceListEntity;
import com.cloud.control.expand.service.interfaces.MenuCallback;


/**
 * Author：abin
 * Date：2020/10/13
 * Description：扩展服务到期弹框
 */
public class ExpandServiceDetailDialog extends Dialog implements View.OnClickListener {

    private MenuCallback callback;

    public ExpandServiceDetailDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
        setContentView(R.layout.dialog_expand_service_detail);
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
        findViewById(R.id.btn_dialog_close).setOnClickListener(this);
        findViewById(R.id.rl_dialog_detail).setOnClickListener(this);
        findViewById(R.id.tv_dialog_detail_title).setOnClickListener(this);
        //设置TextView可滑动
        ((TextView) findViewById(R.id.tv_dialog_detail_content)).setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void setMenuCallback(MenuCallback callback) {
        this.callback = callback;
    }

    /**
     * 两个按钮的样式
     *
     * @param dataBean
     * @param rightStr
     */
    private void setStyle(ExpandServiceListEntity.DataBean dataBean, String rightStr) {
        if (!TextUtils.isEmpty(dataBean.getBase64())) {
            byte[] decodedString = Base64.decode(dataBean.getBase64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ((ImageView) findViewById(R.id.iv_dialog_detail_icon)).setImageBitmap(decodedByte);
        }
        ((TextView) findViewById(R.id.tv_dialog_detail_content)).setText(dataBean.getServiceExplain());
        ((TextView) findViewById(R.id.tv_dialog_detail_title)).setText(dataBean.getServiceName());
        ((Button) findViewById(R.id.btn_dialog_close)).setText(rightStr);
    }

    /**
     * 显示俩个按钮
     *
     * @param context
     * @param dataBean
     * @param rightStr
     * @param callback
     */
    public static void show(Context context, ExpandServiceListEntity.DataBean dataBean, String rightStr,
                            MenuCallback callback) {
        ExpandServiceDetailDialog dialog = new ExpandServiceDetailDialog(context,
                R.style.dialog_style);
        dialog.setMenuCallback(callback);
        dialog.setStyle(dataBean, rightStr);
        dialog.show();
        dialog.initDialog(context, dialog);
    }


    /**
     * 初始化dialog的大小 背景
     *
     * @param context
     * @param dialog
     */
    private void initDialog(Context context, ExpandServiceDetailDialog dialog) {
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
            case R.id.btn_dialog_close:
                dismiss();
                callback.onRightButtonClick(null);
                break;
        }
    }
}
