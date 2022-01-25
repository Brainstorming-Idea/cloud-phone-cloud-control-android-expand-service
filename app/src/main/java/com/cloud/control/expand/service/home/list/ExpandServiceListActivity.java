package com.cloud.control.expand.service.home.list;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.adapter.ExpandServiceAdapter;
import com.cloud.control.expand.service.adapter.ExpandServiceListAdapter;
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
import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;

import java.util.List;

import butterknife.BindView;
import retrofit2.http.HEAD;

/**
 * Author：abin
 * Date：2020/9/29
 * Description：扩展服务列表
 */
public class ExpandServiceListActivity extends BaseActivity<ExpandServiceListPresenter> implements ExpandServiceListView, ExpandServiceListAdapter.IRootOnClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_expand_service_list)
    RecyclerView mRvExpandServiceList;
//    ExpandServiceListAdapter mExpandServiceMainListAdapter;
    ExpandServiceAdapter mExpandServiceMainListAdapter;
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
//        mExpandServiceMainListAdapter = new ExpandServiceListAdapter(mContext);
        mExpandServiceMainListAdapter = new ExpandServiceAdapter(mContext,mPresenter);
//        mExpandServiceMainListAdapter.setIRootOnClickListener(this);
        RecyclerViewHelper.initRecyclerViewV(this, mRvExpandServiceList, mExpandServiceMainListAdapter);
        Log.e("rv init hashcode","" + mRvExpandServiceList.hashCode());
//        mExpandServiceMainListAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if (NoFastClickUtils.isFastClick()) {
//                    return;
//                }
//                mPresenter.examineServiceStatus(mListEntity.get(position));
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.getData();
        }
    }

    @Override
    public void getRootState(boolean state, int position) {
        if (mExpandServiceMainListAdapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mExpandServiceMainListAdapter.setRootState(state,position);
                }
            });
        }
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        if (isRefresh) {
            mPresenter.getData();
        }
    }

    @Override
    public void loadData(List<ExpandServiceRecordEntity.DataBean> listEntity) {
        mListEntity = listEntity;
        if (!mRvExpandServiceList.isShown()){
            mRvExpandServiceList.setVisibility(View.VISIBLE);
        }
//        mRvExpandServiceList.setVisibility(View.VISIBLE);
//        mExpandServiceMainListAdapter.updateItems(listEntity);
        mExpandServiceMainListAdapter.notifyDataSetChanged();
//        if (listEntity != null && listEntity.size() > 0) {
//            if (mRvExpandServiceList != null) {
//                Log.e("wytest","list is shown:"+mRvExpandServiceList.isShown());
//                mRvExpandServiceList.setVisibility(View.VISIBLE);
//                mExpandServiceMainListAdapter.updateItems(listEntity);
//            }
//        } else {
//            Log.e("wytest","data is null"+mRvExpandServiceList.isShown());
//            if (mRvExpandServiceList != null) {
//                mRvExpandServiceList.setVisibility(View.GONE);
//            }
//        }
    }

    @Override
    public void hideListView() {
        if (mRvExpandServiceList != null) {
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
            case MULTI_CHANNEL_LIVE:
            case SENIOR_ADB:
            case BACKUP_RESTORE:
            case SCREEN_INTERACTION:
            case CLONED:
                toastMessage(getString(R.string.common_desc));
                break;
            case ROOT_PATTERN:

                break;
            default:
                toastMessage(getString(R.string.common_desc));
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
                if (mPresenter != null) {
                    mPresenter.getData();
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

    @Override
    public void onClick(boolean isOpen) {
        mPresenter.setRootState(isOpen);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
