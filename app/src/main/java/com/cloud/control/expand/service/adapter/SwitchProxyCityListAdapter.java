package com.cloud.control.expand.service.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.entity.SelectCityStatusEntity;
import com.cloud.control.expand.service.log.KLog;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;

/**
 * Author：abin
 * Date：2020/10/12
 * Description： 切换IP城市列表适配器
 */
public class SwitchProxyCityListAdapter extends BaseQuickAdapter<SelectCityStatusEntity> {

    private ICityData mICityData;

    public SwitchProxyCityListAdapter setICityDataListener(ICityData cityData) {
        mICityData = cityData;
        return this;
    }

    public SwitchProxyCityListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.recycle_item_switch_proxy_city;
    }

    @Override
    protected void convert(BaseViewHolder holder, final SelectCityStatusEntity item) {
        final TextView city = (TextView) holder.getView(R.id.tv_city);
        city.setText(item.getCity());
        //选中状态
        city.setSelected(item.isStatus());
        //选中字体颜色
        if (item.isStatus()) {
            city.setTextColor(mContext.getResources().getColor(R.color.c_515ff0));
        } else {
            city.setTextColor(mContext.getResources().getColor(R.color.c_333333));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isStatus()) {
                    city.setSelected(false);
                    city.setTextColor(mContext.getResources().getColor(R.color.c_333333));
                    item.setStatus(false);
                } else {
                    city.setSelected(true);
                    city.setTextColor(mContext.getResources().getColor(R.color.c_515ff0));
                    item.setStatus(true);
                }
                //更新选中状态
                mICityData.cityStatus(item);
            }
        });
    }

    public interface ICityData {
        /**
         * 选中城市状态
         *
         * @param entity
         */
        void cityStatus(SelectCityStatusEntity entity);
    }
}
