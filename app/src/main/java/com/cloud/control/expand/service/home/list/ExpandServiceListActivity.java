package com.cloud.control.expand.service.home.list;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.base.BaseActivity;
import com.cloud.control.expand.service.dialog.CommonHintDialog;
import com.cloud.control.expand.service.entity.ExpandServiceRecordEntity;
import com.cloud.control.expand.service.injector.components.DaggerExpandServiceListComponent;
import com.cloud.control.expand.service.injector.modules.ExpandServiceListModule;
import com.cloud.control.expand.service.interfaces.MenuCallback;
import com.cloud.control.expand.service.module.changemachine.ChangeMachineActivity;
import com.cloud.control.expand.service.module.switchproxy.SwitchProxyActivity;
import com.cloud.control.expand.service.module.virtuallocation.VirtualLocationActivity;
import com.cloud.control.expand.service.utils.NoFastClickUtils;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;

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
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData();
    }

    @Override
    public void loadData(List<ExpandServiceRecordEntity.DataBean> listEntity) {
        mExpandServiceMainListAdapter.updateItems(listEntity);
    }

    @Override
    public void jumpPage(ExpandServiceRecordEntity.DataBean dataBean) {
        if (dataBean.getStatus() == 1) {
            if (dataBean.getTypeName().equals("IP代理")) {
                startActivity(new Intent(mContext, SwitchProxyActivity.class));
            } else if (dataBean.getTypeName().equals("虚拟定位")) {
                startActivity(new Intent(mContext, VirtualLocationActivity.class));
            } else if (dataBean.getTypeName().equals("一键新机")) {
                startActivity(new Intent(mContext, ChangeMachineActivity.class));
            }
        } else {
            showOperationLimitedDialog(dataBean.getMobileName(), dataBean.getTypeName());
        }
    }

    /**
     * 操作受限弹框
     *
     * @param cardNick
     * @param serviceName
     */
    private void showOperationLimitedDialog(String cardNick, String serviceName) {
        String richText = "<font color='#1677FF'>" + cardNick + "</font>" + "未购买" + serviceName + "服务，请购买后再使用。";
        CommonHintDialog.show(mContext, "操作受限", richText, "", "确认", new MenuCallback() {
            @Override
            public void onLeftButtonClick(Object value) {

            }

            @Override
            public void onRightButtonClick(Object value) {

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
