package com.cloud.control.expand.service.home.list;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.base.BaseActivity;
import com.cloud.control.expand.service.dialog.CommonHintDialog;
import com.cloud.control.expand.service.entity.ExpandService;
import com.cloud.control.expand.service.entity.ExpandServiceRecordEntity;
import com.cloud.control.expand.service.injector.components.DaggerExpandServiceListComponent;
import com.cloud.control.expand.service.injector.modules.ExpandServiceListModule;
import com.cloud.control.expand.service.interfaces.MenuCallback;
import com.cloud.control.expand.service.module.changemachine.ChangeMachineActivity;
import com.cloud.control.expand.service.module.switchproxy.SwitchProxyActivity;
import com.cloud.control.expand.service.module.virtuallocation.VirtualLocationActivity;
import com.cloud.control.expand.service.module.virtualscene.VirtualSceneActivity;
import com.cloud.control.expand.service.utils.DateUtils;
import com.cloud.control.expand.service.utils.NoFastClickUtils;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Author：abin
 * Date：2020/9/29
 * Description：扩展服务列表
 */
public class ExpandServiceListActivity extends BaseActivity<ExpandServiceListPresenter> implements ExpandServiceListView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_expand_service_list)
    RecyclerView mRvExpandServiceList;
    @Inject
    BaseQuickAdapter mExpandServiceMainListAdapter;
    List<ExpandServiceRecordEntity.DataBean> mListEntity;
    private long firstPressedTime; //退出应用按下时间

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInjector() {
        DaggerExpandServiceListComponent.builder()
                .expandServiceListModule(new ExpandServiceListModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar, false, "扩展服务");
        RecyclerViewHelper.initRecyclerViewV(this, mRvExpandServiceList, mExpandServiceMainListAdapter);
        mExpandServiceMainListAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (NoFastClickUtils.isFastClick()) {
                    return;
                }
                mPresenter.examineServiceStatus(position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mPresenter != null){
            mPresenter.getData();
        }
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData();
    }

    @Override
    public void refreshData(List<ExpandServiceRecordEntity.DataBean> listEntity) {
        mListEntity = listEntity;
    }

    @Override
    public void loadData(List<ExpandServiceRecordEntity.DataBean> listEntity) {
        mExpandServiceMainListAdapter.updateItems(listEntity);
        if(listEntity != null && listEntity.size() > 0){
            if(mRvExpandServiceList != null) {
                mRvExpandServiceList.setVisibility(View.VISIBLE);
            }
        }else{
            if(mRvExpandServiceList != null) {
                mRvExpandServiceList.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void hideListView() {
        if(mRvExpandServiceList != null){
            mRvExpandServiceList.setVisibility(View.GONE);
        }
    }

    @Override
    public void jumpPage(ExpandServiceRecordEntity.DataBean dataBean) {
        ExpandService targetService = ExpandService.getExpandService(dataBean.getTypeId());
        switch (targetService){
            case IP_PROXY:
                startActivity(new Intent(mContext, SwitchProxyActivity.class));
                break;
            case VIRTUAL_LOCATION:
                startActivity(new Intent(mContext, VirtualLocationActivity.class));
                break;
            case CHANGE_MACHINE:
                startActivity(new Intent(mContext, ChangeMachineActivity.class));
                break;
            case VIRTUAL_SCENE:
                startActivity(new Intent(mContext, VirtualSceneActivity.class));
                break;
            case OCR:
                toastMessage(getString(R.string.ocr_desc));
                break;
            case MUL_WINDOW:
                //TODO
                break;
            case LOG_DEBUG:
                //TODO
                break;
            default:
                toastMessage("暂未开放");
        }
    }

    @Override
    public void toast(String message) {

    }

    @Override
    public void dialog(String title, String content, String leftStr, String rightStr) {
        showExpireDialog(title, content, leftStr, rightStr, new MenuCallback() {
            @Override
            public void onLeftButtonClick(Object value) {

            }

            @Override
            public void onRightButtonClick(Object value) {
                if(mListEntity != null && mListEntity.size() > 0){
                    mExpandServiceMainListAdapter.updateItems(mListEntity);
                    if(mRvExpandServiceList != null) {
                        mRvExpandServiceList.setVisibility(View.VISIBLE);
                    }
                }else{
                    if(mRvExpandServiceList != null) {
                        mRvExpandServiceList.setVisibility(View.GONE);
                    }
                    showNoData("还没有可用扩展服务哦~");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            super.onBackPressed();
        } else {
            toastMessage("再按一次退出");
            firstPressedTime = System.currentTimeMillis();
        }
    }

}
