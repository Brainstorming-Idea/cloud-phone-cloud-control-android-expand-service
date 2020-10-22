package com.cloud.control.expand.service.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.adapter.PhoneBrandModelAdapter;
import com.cloud.control.expand.service.entity.ItemBrandModelEntity;
import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：abin
 * Date：2020/10/13
 * Description：手机品牌型号选择框
 */
public class PhoneBrandModelDialog extends DialogFragment implements View.OnClickListener, OnRecyclerViewItemClickListener, PhoneBrandModelAdapter.IRefreshData {

    private RecyclerView mRvPhoneBrandModel;
    private PhoneBrandModelAdapter mPhoneBrandModelAdapter;
    private OnMenuClickListener onMenuClickListener;
    private List<ItemBrandModelEntity> mItemList = new ArrayList<>();

    public PhoneBrandModelDialog setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
        return this;
    }

    public PhoneBrandModelDialog setItemData(List<ItemBrandModelEntity> itemData) {
        mItemList = itemData;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.DialogBottom);
        dialog.setContentView(R.layout.dialog_phone_brand_model);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        mRvPhoneBrandModel = dialog.findViewById(R.id.rv_phone_brand_model);
        mPhoneBrandModelAdapter = new PhoneBrandModelAdapter(getActivity());
        mPhoneBrandModelAdapter.setIRefreshData(this);
        RecyclerViewHelper.initRecyclerViewV(getActivity(), mRvPhoneBrandModel, mPhoneBrandModelAdapter);
        dialog.findViewById(R.id.tv_dialog_cancel).setOnClickListener(this);
        mPhoneBrandModelAdapter.setOnItemClickListener(this);
        mPhoneBrandModelAdapter.updateItems(mItemList);
        setRecyclerViewHeight(mItemList.size());
        return dialog;
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }

    @Override
    public void onItemClick(View view, int position) {
        this.dismiss();
    }

    @Override
    public void update(ItemBrandModelEntity item) {
        onMenuClickListener.onMenuClick(item);
        this.dismiss();
    }

    public interface OnMenuClickListener {
        void onMenuClick(ItemBrandModelEntity item);
    }

    /**
     * 动态设置RecyclerView的高度
     *
     * @param itemCount
     */
    private void setRecyclerViewHeight(int itemCount) {
        ViewGroup.LayoutParams layoutParams = mRvPhoneBrandModel.getLayoutParams();
        if (itemCount > 5) {
            layoutParams.height = dip2px(getActivity(), 50 * 5);
        } else {
            layoutParams.height = dip2px(getActivity(), 50 * itemCount);
        }
        mRvPhoneBrandModel.setLayoutParams(layoutParams);
    }

    /**
     * dp和px互转
     *
     * @param context
     * @param dpValue
     * @return
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
