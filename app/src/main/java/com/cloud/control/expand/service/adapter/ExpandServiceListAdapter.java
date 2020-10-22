package com.cloud.control.expand.service.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.dialog.ExpandServiceDetailDialog;
import com.cloud.control.expand.service.entity.ExpandServiceListEntity;
import com.cloud.control.expand.service.interfaces.MenuCallback;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;

/**
 * Author：abin
 * Date：2020/9/28
 * Description： 扩展服务列表适配器
 */
public class ExpandServiceListAdapter extends BaseQuickAdapter<ExpandServiceListEntity.DataBean> {

    public ExpandServiceListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.recycle_item_expand_service;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final ExpandServiceListEntity.DataBean item) {
        ImageView expandServiceIcon = holder.getView(R.id.iv_expand_service_icon);
        // TODO 根据后台返回数据格式处理
        //ImageLoader.loadCenterCrop(mContext, item.getBase64(), expandServiceIcon, R.drawable.ic_change_machine);
        //加载图片base64数据
        if (!TextUtils.isEmpty(item.getBase64())) {
            byte[] decodedString = Base64.decode(item.getBase64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            expandServiceIcon.setImageBitmap(decodedByte);
        }
        holder.setText(R.id.tv_expand_service_title, item.getServiceName())
                .setText(R.id.tv_expand_service_describe, item.getServiceExplain());
        holder.getView(R.id.iv_expand_service_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //服务详情弹框
                ExpandServiceDetailDialog.show(mContext, item, "关闭", new MenuCallback() {
                    @Override
                    public void onLeftButtonClick(Object value) {

                    }

                    @Override
                    public void onRightButtonClick(Object value) {

                    }
                });
            }
        });
    }
}
