package com.cloud.control.expand.service.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.dialog.ExpandServiceDetailDialog;
import com.cloud.control.expand.service.entity.ExpandServiceRecordEntity;
import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.interfaces.MenuCallback;
import com.cloud.control.expand.service.utils.ConstantsUtils;
import com.cloud.control.expand.service.utils.ImageLoader;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;

/**
 * Author：abin
 * Date：2020/9/28
 * Description： 扩展服务列表适配器
 */
public class ExpandServiceListAdapter extends BaseQuickAdapter<ExpandServiceRecordEntity.DataBean> {

    public ExpandServiceListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.recycle_item_expand_service;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final ExpandServiceRecordEntity.DataBean item) {
        ImageView expandServiceIcon = holder.getView(R.id.iv_expand_service_icon);
        // TODO 根据后台返回数据格式处理
        ImageLoader.loadCenterCrop(mContext, ExpandServiceApplication.getInstance().getBuildConfigHost() + ConstantsUtils.IMAGE_MIDDLE_URL + item.getServiceImgUrl(), expandServiceIcon, R.drawable.ic_expand_service_default);
        holder.setText(R.id.tv_expand_service_title, item.getTypeName())
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
