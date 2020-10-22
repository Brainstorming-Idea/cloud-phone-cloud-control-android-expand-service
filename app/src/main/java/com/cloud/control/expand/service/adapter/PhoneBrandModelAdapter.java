package com.cloud.control.expand.service.adapter;

import android.content.Context;
import android.view.View;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.entity.ItemBrandModelEntity;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;

/**
 * Author：abin
 * Date：2020/10/13
 * Description： 手机品牌型号适配器
 */
public class PhoneBrandModelAdapter extends BaseQuickAdapter<ItemBrandModelEntity> {

    private IRefreshData mIRefreshData;

    public void setIRefreshData(IRefreshData iRefreshData) {
        mIRefreshData = iRefreshData;
    }

    public PhoneBrandModelAdapter(Context context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.recycle_item_phone_brand_model;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final ItemBrandModelEntity item) {
        holder.setText(R.id.tv_brand_model, item.getData());
        //是否选中
        if (item.isCheck()) {
            holder.getView(R.id.iv_check).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.iv_check).setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIRefreshData.update(item);
            }
        });
    }

    public interface IRefreshData {
        void update(ItemBrandModelEntity item);
    }

}
