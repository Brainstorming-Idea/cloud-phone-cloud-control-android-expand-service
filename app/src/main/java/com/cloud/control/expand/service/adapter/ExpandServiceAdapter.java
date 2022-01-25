package com.cloud.control.expand.service.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.dialog.CommonHintDialog;
import com.cloud.control.expand.service.dialog.ExpandServiceDetailDialog;
import com.cloud.control.expand.service.entity.ExpandServiceRecordEntity;
import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.home.list.ExpandServiceListPresenter;
import com.cloud.control.expand.service.interfaces.MenuCallback;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.utils.ConstantsUtils;
import com.cloud.control.expand.service.utils.ExtendedServicesType;
import com.cloud.control.expand.service.utils.ImageLoader;
import com.cloud.control.expand.service.utils.NoFastClickUtils;

/**
 * @author wangyou
 * @desc:
 * @date :2022/1/25
 */
public class ExpandServiceAdapter extends RecyclerView.Adapter<ExpandServiceAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private Context mContext;
    private ExpandServiceListPresenter presenter;
    private boolean isOpen;

    public ExpandServiceAdapter(Context context, ExpandServiceListPresenter presenter) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.presenter = presenter;
    }

    public void setRootState(boolean isOpen,int position) {
        this.isOpen = isOpen;
        this.notifyItemChanged(position);
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycle_item_expand_service,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View itemView = holder.itemView;
        ExpandServiceRecordEntity.DataBean item = presenter.exListData.get(position);
        ImageView exIcon = itemView.findViewById(R.id.iv_expand_service_icon);
        ImageView exDetailIcon = itemView.findViewById(R.id.iv_expand_service_detail);
        ImageLoader.loadCenterCrop(mContext, ExpandServiceApplication.getInstance().getBuildConfigHost() + ConstantsUtils.IMAGE_MIDDLE_URL + item.getServiceImgUrl(), exIcon, R.drawable.ic_expand_service_default);
        TextView tvExName = itemView.findViewById(R.id.tv_expand_service_title);
        TextView tvDesc = itemView.findViewById(R.id.tv_expand_service_describe);
        tvExName.setText(item.getTypeName());
        tvDesc.setText(item.getServiceExplain());
        exDetailIcon.setOnClickListener(v -> {
            //服务详情弹框
            ExpandServiceDetailDialog.show(mContext, item, "关闭", new MenuCallback() {
                @Override
                public void onLeftButtonClick(Object value) {

                }

                @Override
                public void onRightButtonClick(Object value) {

                }
            });
        });
        final ImageView ivRoot = itemView.findViewById(R.id.iv_root);
        //显示root按钮
        if (item.getTypeId() == ExtendedServicesType.ROOT_PATTERN.getKey()) {
            if (isOpen) {
                ivRoot.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_button_on));
                KLog.d("root open");
            } else {
                ivRoot.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_button_off));
                KLog.d("root close");
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
                            presenter.setRootState(isOpen);
                        }
                    });
                } else {
                    //TODO 后续其他扩展服务按钮处理
                }
            }
        });

        itemView.setOnClickListener(v -> {
            if (NoFastClickUtils.isFastClick()) {
                return;
            }
            presenter.examineServiceStatus(presenter.exListData.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return presenter.exListData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}