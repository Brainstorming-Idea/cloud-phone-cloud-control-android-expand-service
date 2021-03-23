package com.cloud.control.expand.service.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.dialog.CommonHintDialog;
import com.cloud.control.expand.service.dialog.ExpandServiceDetailDialog;
import com.cloud.control.expand.service.entity.ExpandServiceRecordEntity;
import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.interfaces.MenuCallback;
import com.cloud.control.expand.service.utils.ConstantsUtils;
import com.cloud.control.expand.service.utils.ExtendedServicesType;
import com.cloud.control.expand.service.utils.ImageLoader;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;

/**
 * Author：abin
 * Date：2020/9/28
 * Description： 扩展服务列表适配器
 */
public class ExpandServiceListAdapter extends BaseQuickAdapter<ExpandServiceRecordEntity.DataBean> {

    private boolean isOpen;
    private IRootOnClickListener iRootOnClickListener;

    public ExpandServiceListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.recycle_item_expand_service;
    }

    public void setRootState(boolean isOpen) {
        this.isOpen = isOpen;
        notifyDataSetChanged();
    }

    public void setIRootOnClickListener(IRootOnClickListener iRootOnClickListener) {
        this.iRootOnClickListener = iRootOnClickListener;
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
        final ImageView ivRoot = holder.getView(R.id.iv_root);
        //显示root按钮
        if (item.getTypeId() == ExtendedServicesType.ROOT_PATTERN.getKey()) {
            if (isOpen) {
                ivRoot.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_button_on));
            } else {
                ivRoot.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_button_off));
            }
        } else { //显示跳转按钮
            ivRoot.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_expand_service_right));
        }
        ivRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getTypeId() == ExtendedServicesType.ROOT_PATTERN.getKey()) {
                    String operationHint = isOpen ? "关闭" : "开启";
                    CommonHintDialog.show(mContext, operationHint + "ROOT", operationHint + "后可能会有部分应用无法使用，重启无法使用的应用即可解决", "取消", "确认", new MenuCallback() {
                        @Override
                        public void onLeftButtonClick(Object value) {

                        }

                        @Override
                        public void onRightButtonClick(Object value) {
                            if (isOpen) {
                                isOpen = false;
                                ivRoot.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_button_off));
                            } else {
                                isOpen = true;
                                ivRoot.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_button_on));
                            }
                            iRootOnClickListener.onClick(isOpen);
                        }
                    });
                } else {
                    //TODO 后续其他扩展服务按钮处理
                }
            }
        });
    }

    public interface IRootOnClickListener {
        void onClick(boolean isOpen);
    }
}
