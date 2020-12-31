package com.cloud.control.expand.service.module.switchproxy;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.adapter.SwitchProxyCityListAdapter;
import com.cloud.control.expand.service.base.BaseActivity;
import com.cloud.control.expand.service.dialog.CommonHintDialog;
import com.cloud.control.expand.service.entity.CityListEntity;
import com.cloud.control.expand.service.entity.ResponseEntity;
import com.cloud.control.expand.service.entity.SelectCityStatusEntity;
import com.cloud.control.expand.service.entity.SwitchProxyTypeEntity;
import com.cloud.control.expand.service.injector.components.DaggerSwitchProxyComponent;
import com.cloud.control.expand.service.injector.modules.SwitchProxyModule;
import com.cloud.control.expand.service.interfaces.MenuCallback;
import com.cloud.control.expand.service.utils.NoFastClickUtils;
import com.cloud.control.expand.service.widget.RecyclerViewSpacesItemDecoration;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.helper.RecyclerViewHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author：abin
 * Date：2020/9/29
 * Description：切换IP
 */
public class SwitchProxyActivity extends BaseActivity<SwitchProxyPresenter> implements SwitchProxyView, SwitchProxyCityListAdapter.ICityData {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_switch_proxy_city_list)
    RecyclerView mRvSwitchProxyCity;
    @Inject
    BaseQuickAdapter mSwitchProxyCityListAdapter;
    @BindView(R.id.tv_current_ip)
    TextView tvCurrentIp;
    @BindView(R.id.tv_random_switch)
    TextView tvRandomSwitch;
    @BindView(R.id.tv_assign_switch)
    TextView tvAssignSwitch;
    @BindView(R.id.tv_select_region)
    TextView tvSelectRegion;
    @BindView(R.id.tv_close_proxy)
    TextView tvCloseProxy;
    @BindView(R.id.tv_start_proxy)
    TextView tvStartProxy;

    private List<SelectCityStatusEntity> selectCityStatusEntityList;
    private int mIpChangeType = -1; //切换方式，默认未选中 0:全国随机切换ip;1:指定城市切换ip
    private List<String> mSelectCity = new ArrayList<>(); //最后选中的城市，用来传参

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_switch_proxy;
    }

    @Override
    protected void initInjector() {
        DaggerSwitchProxyComponent.builder()
                .switchProxyModule(new SwitchProxyModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar, false, "");
        selectCityStatusEntityList = new ArrayList<>();
        RecyclerViewHelper.initRecyclerViewG(this, mRvSwitchProxyCity, mSwitchProxyCityListAdapter, 3);
        setRecyclerViewSpacesItemDecoration(15, 15, 15, 15);
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData();
    }

    @Override
    public void loadData(ResponseEntity ip, SwitchProxyTypeEntity ipChangeType, CityListEntity cityListEntity) {
        //显示IP
        if (ip != null) {
            if (!TextUtils.isEmpty((String) ip.getData()))
                tvCurrentIp.setText((String) ip.getData());
        }
        //显示切换方式
        if (ipChangeType != null) {
            if (ipChangeType.getData() == 0) {
                mIpChangeType = 0;
                tvRandomSwitch.setSelected(true);
                tvAssignSwitch.setSelected(false);
                tvRandomSwitch.setSelected(true);
                tvRandomSwitch.setTextColor(getResources().getColor(R.color.c_ffffff));
                tvAssignSwitch.setTextColor(getResources().getColor(R.color.c_999999));
                tvSelectRegion.setVisibility(View.GONE);
                mRvSwitchProxyCity.setVisibility(View.GONE);
            } else if (ipChangeType.getData() == 1) {
                mIpChangeType = 1;
                tvRandomSwitch.setSelected(false);
                tvAssignSwitch.setSelected(true);
                tvAssignSwitch.setSelected(true);
                tvAssignSwitch.setTextColor(getResources().getColor(R.color.c_ffffff));
                tvRandomSwitch.setTextColor(getResources().getColor(R.color.c_999999));
                tvSelectRegion.setVisibility(View.VISIBLE);
                mRvSwitchProxyCity.setVisibility(View.VISIBLE);
            } else {
                tvRandomSwitch.setEnabled(false);
                tvAssignSwitch.setEnabled(false);
                mIpChangeType = -1;
            }
        }
        //显示城市列表
        if (cityListEntity != null) {
            selectCityStatusEntityList.clear();
            mSelectCity.clear();
            for (int i = 0; i < cityListEntity.getData().getCityList().size(); i++) {
                SelectCityStatusEntity entity = new SelectCityStatusEntity();
                entity.setCity(cityListEntity.getData().getCityList().get(i));
                entity.setStatus(false);
                selectCityStatusEntityList.add(entity);
                for (int j = 0; j < cityListEntity.getData().getSelectedCity().size(); j++) {
                    //替换选中状态的城市
                    if (cityListEntity.getData().getCityList().get(i).equals(cityListEntity.getData().getSelectedCity().get(j))) {
                        SelectCityStatusEntity replaceEntity = new SelectCityStatusEntity();
                        replaceEntity.setCity(cityListEntity.getData().getCityList().get(i));
                        replaceEntity.setStatus(true);
                        //每个item城市的选中状态
                        selectCityStatusEntityList.set(i, replaceEntity);
                        //上次选中的城市
                        mSelectCity.add(cityListEntity.getData().getCityList().get(i));
                    }
                }
            }
            mSwitchProxyCityListAdapter.updateItems(selectCityStatusEntityList);
        }
    }

    @Override
    public void toast(String message) {
        toastMessage(message);
    }

    @Override
    public void dialog(String title, String content, String leftStr, String rightStr) {
        showExpireDialog(title, content, leftStr, rightStr, new MenuCallback() {
            @Override
            public void onLeftButtonClick(Object value) {

            }

            @Override
            public void onRightButtonClick(Object value) {
                finish();
            }
        });
    }

    /**
     * 设置列表间距
     *
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    private void setRecyclerViewSpacesItemDecoration(int top, int bottom, int left, int right) {
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_SPACE, top);//top间距
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_SPACE, bottom);//底部间距
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.LEFT_SPACE, left);//左间距
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_SPACE, right);//右间距
        mRvSwitchProxyCity.addItemDecoration(new RecyclerViewSpacesItemDecoration(3, stringIntegerHashMap, true));
    }

    @OnClick({R.id.tv_start_proxy, R.id.tv_close_proxy, R.id.iv_switch_proxy_back, R.id.tv_random_switch, R.id.tv_assign_switch})
    public void onClick(View view) {
        if (NoFastClickUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_start_proxy:
                if (mIpChangeType == -1) {
                    toastMessage("请先设置IP切换方式");
                    return;
                }
                //指定城市，判断是否选中城市
                if (mIpChangeType == 1) {
                    if (mSelectCity.size() <= 0) {
                        toastMessage("请先选择地区");
                        return;
                    }
                }
                String[] selectArray = new String[mSelectCity.size()];
                for (int i = 0; i < mSelectCity.size(); i++) {
                    selectArray[i] = mSelectCity.get(i);
                }
                mPresenter.startProxy(selectArray, mIpChangeType);
                break;
            case R.id.tv_close_proxy:
                CommonHintDialog.show(mContext, "关闭IP代理", "确认关闭IP代理吗？", "取消", "确认", new MenuCallback() {
                    @Override
                    public void onLeftButtonClick(Object value) {

                    }

                    @Override
                    public void onRightButtonClick(Object value) {
                        mPresenter.closeProxy();
                    }
                });
                break;

            case R.id.tv_random_switch:
                mIpChangeType = 0;
                tvRandomSwitch.setSelected(true);
                tvAssignSwitch.setSelected(false);
                tvRandomSwitch.setTextColor(getResources().getColor(R.color.c_ffffff));
                tvAssignSwitch.setTextColor(getResources().getColor(R.color.c_999999));
                tvSelectRegion.setVisibility(View.GONE);
                mRvSwitchProxyCity.setVisibility(View.GONE);
                break;

            case R.id.tv_assign_switch:
                mIpChangeType = 1;
                tvRandomSwitch.setSelected(false);
                tvAssignSwitch.setSelected(true);
                tvAssignSwitch.setTextColor(getResources().getColor(R.color.c_ffffff));
                tvRandomSwitch.setTextColor(getResources().getColor(R.color.c_999999));
                tvSelectRegion.setVisibility(View.VISIBLE);
                mRvSwitchProxyCity.setVisibility(View.VISIBLE);
                mPresenter.refreshCityList();
                break;

            case R.id.iv_switch_proxy_back:
                finish();
                break;
        }
    }

    @Override
    public void cityStatus(SelectCityStatusEntity entity) {
        //点击按钮状态更新
        for (int i = 0; i < selectCityStatusEntityList.size(); i++) {
            if (entity.getCity().equals(selectCityStatusEntityList.get(i).getCity())) {
                SelectCityStatusEntity replaceEntity = new SelectCityStatusEntity();
                replaceEntity.setCity(selectCityStatusEntityList.get(i).getCity());
                replaceEntity.setStatus(entity.isStatus());
                selectCityStatusEntityList.set(i, replaceEntity);
            }
        }
        mSelectCity.clear();
        //新的集合用来添加选中城市
        for (int j = 0; j < selectCityStatusEntityList.size(); j++) {
            if (selectCityStatusEntityList.get(j).isStatus()) {
                mSelectCity.add(selectCityStatusEntityList.get(j).getCity());
            }
        }
    }
}
