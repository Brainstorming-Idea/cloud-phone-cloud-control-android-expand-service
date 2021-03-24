package com.cloud.control.expand.service.module.virtualscene;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.base.BaseActivity;
import com.cloud.control.expand.service.dialog.CommonHintDialog;
import com.cloud.control.expand.service.entity.LocationInfoEntity;
import com.cloud.control.expand.service.entity.SceneType;
import com.cloud.control.expand.service.entity.VsConfig;
import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.injector.components.DaggerVirtualSceneComponent;
import com.cloud.control.expand.service.injector.modules.VirtualSceneModule;
import com.cloud.control.expand.service.interfaces.MenuCallback;
import com.cloud.control.expand.service.module.virtualcenter.VirtualCenterActivity;
import com.cloud.control.expand.service.utils.AnimalUtils;
import com.cloud.control.expand.service.utils.ConstantsUtils;
import com.cloud.control.expand.service.utils.ServerUtils;
import com.cloud.control.expand.service.utils.SharePreferenceHelper;
import com.cloud.control.expand.service.utils.bdmap.BdMapUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 * @author wangyou
 * @desc: 虚拟场景界面
 * @date :2021/3/3
 */
public class VirtualSceneActivity extends BaseActivity<VirtualScenePresenter> implements VirtualSceneView {
    private static final String TAG = "VirtualSceneActivity";
    private List<Disposable> disposables = new ArrayList<>();
    @BindView(R.id.base_toolbar)
    Toolbar toolbar;
    @BindView(R.id.vs_grid)
    GridView vsGrid;
    @BindView(R.id.vs_radius_container)
    View radiusContainer;
    @BindView(R.id.vs_center_container)
    View centerContainer;
    @BindView(R.id.tv_center_title)
    TextView centerTitle;
    @BindView(R.id.iv_center)
    ImageView icCenter;
    @BindView(R.id.tv_center_location)
    TextView location;
    @BindView(R.id.tv_radius)
    TextView tvRadius;
    @BindView(R.id.iv_start)
    ImageView startImg;
    @BindView(R.id.tv_start)
    TextView startTv;
    @BindView(R.id.et_radius)
    EditText etRadius;
    @BindView(R.id.radius_unit)
    TextView tvRadiusUnit;
    private int sceneIndex = 0;
    private String sceneDesc = "";
    //虚拟场景的开启状态
    private boolean isStart = false;
    private VirtualSceneService.MyBinder binder;
    private boolean isBind = false;
    private int radius = 0;//输入的半径单位KM
    private double[] cCoord;//中心点坐标
    private String cLocation;//中心点位置
    private GridAdapter gridAdapter = null;
    private Intent vsIntent;
    private SharePreferenceHelper spHelper;
    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_virtual_scene;
    }

    @Override
    protected void initInjector() {
        DaggerVirtualSceneComponent.builder()
                .virtualSceneModule(new VirtualSceneModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(toolbar, false, getString(R.string.virtual_scene));
        spHelper = SharePreferenceHelper.getInstance(ExpandServiceApplication.getInstance());
        vsIntent = new Intent(this, VirtualSceneService.class);
//        if (isServiceRunning(ExpandServiceApplication.getInstance(), VirtualSceneService.class.getCanonicalName())) {
            bindService(vsIntent, vServiceConnection, Service.BIND_AUTO_CREATE);
//        }
        final List<SceneType> sceneTypes = new ArrayList<>(Arrays.asList(SceneType.values()));
        gridAdapter = new GridAdapter(this, sceneTypes);
        vsGrid.setAdapter(gridAdapter);
        gridAdapter.setSelectedPos(0);
        /*item点击事件*/
//        vsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SceneType sceneType = SceneType.getVirtualScene(position);
//                assert sceneType != null;
////                view.setSelected(true);
//                gridAdapter.setSelectedPos(position);
//                if (!sceneType.equals(SceneType.SIT)) {
//                    radiusContainer.setVisibility(View.VISIBLE);
//                    if (sceneType == SceneType.DRIVE) {
//                        etRadius.setHint(String.format(getString(R.string.vs_input_radius), "20-100KM"));
//                    } else {
//                        etRadius.setHint(String.format(getString(R.string.vs_input_radius), "1KM-5KM"));
//                    }
//                } else {
//                    radiusContainer.setVisibility(View.GONE);
//                }
//            }
//        });

        /*获取中心位置*/
        mPresenter.getCenterLoc();
    }

    @OnClick({R.id.iv_vs_back, R.id.vs_center_container, R.id.vs_start_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_vs_back:
                finish();
                break;
            case R.id.vs_center_container:
                startActivityForResult(new Intent(this, VirtualCenterActivity.class),
                        0);
                break;
            case R.id.vs_start_btn:
                if (gridAdapter != null) {
                    sceneIndex = gridAdapter.getSceneType().getValue();
                    sceneDesc = SceneType.getVirtualScene(sceneIndex).getDesc();
                } else {
                    return;
                }
                SceneType sceneType = SceneType.getVirtualScene(sceneIndex);
                String radiusStr = etRadius.getText().toString();
                if (!TextUtils.isEmpty(radiusStr)) {
                    radius = Integer.parseInt(radiusStr);
                }
                assert sceneType != null;
                switch (sceneType) {
                    case SIT:
                        break;
                    case WALK:
                    case RUN:
                        if (radius < 1 || radius > 5) {
                            toast(getString(R.string.vs_input_right_radius));
                            return;
                        }
                        break;
                    case DRIVE:
                        if (radius < 20 || radius > 100) {
                            toast(getString(R.string.vs_input_right_radius));
                            return;
                        }
                        break;
                    default:
                        return;
                }
                CommonHintDialog.show(mContext, String.format(isStart ? getString(R.string.vs_stop_scene) : getString(R.string.vs_start_scene), sceneDesc),
                        String.format(isStart ? getString(R.string.vs_stop_confirm_scene) : getString(R.string.vs_start_confirm_scene), sceneDesc), "取消", "确认",
                        new MenuCallback() {
                            @Override
                            public void onLeftButtonClick(Object value) {

                            }

                            @Override
                            public void onRightButtonClick(Object value) {
                                //请求接口将开启状态发送给后台，并检测服务过期状态
                                mPresenter.setVsStatus(4, ExpandServiceApplication.getInstance().getCardSn(), isStart ? 0 : 1);
                            }
                        });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    LocationInfoEntity infoEntity = data.getParcelableExtra("detail_address");
                    String detailAdd = infoEntity.getDetail();
                    location.setText(detailAdd);
                    /*存储坐标及位置描述*/
                    SharePreferenceHelper helper = SharePreferenceHelper.getInstance(ExpandServiceApplication.getInstance());
                    VsConfig vsConfig = helper.getObject(ConstantsUtils.SpKey.SP_VS_CONFIG,VsConfig.class);
                    if (vsConfig == null) {
                        vsConfig = new VsConfig();
                    }
                    vsConfig.setCity(detailAdd);
                    double[] newCenter = new double[]{Double.parseDouble(infoEntity.getLatitude()),Double.parseDouble(infoEntity.getLongitude())};
                    vsConfig.setCenterCoords(newCenter);
                    cCoord = newCenter;//更新中心点坐标
                    helper.putObject(ConstantsUtils.SpKey.SP_VS_CONFIG, vsConfig);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        if (!isRefresh) {
            return;
        }
        if (ServerUtils.isServiceRunning(ExpandServiceApplication.getInstance(), VirtualSceneService.class.getCanonicalName())) {
            SharePreferenceHelper helper = SharePreferenceHelper.getInstance(ExpandServiceApplication.getInstance());
            VsConfig vsConfig = helper.getObject(ConstantsUtils.SpKey.SP_VS_CONFIG, VsConfig.class);
            if (vsConfig != null) {
                isStart = vsConfig.isStart();
                location.setText(vsConfig.getCity());//显示存储的位置信息
                SceneType sceneType = SceneType.getVirtualScene(vsConfig.getSceneType());
                if (gridAdapter != null && isStart) {
                    /*确认下路线规划是否在运行，防止出现在虚拟场景运行时重启安卓卡，界面显示在运行而实际没有运行*/
                    if (binder != null && !binder.getService().getStatus()) {
                        Log.e(TAG, "服务运行状态与保存状态不一致，修改保存的状态");
                        vsConfig.setStart(false);
                        helper.putObject(ConstantsUtils.SpKey.SP_VS_CONFIG, vsConfig);
                        isStart = false;
//                    startVsService(vsConfig);
                    } else {
                        gridAdapter.setSelectedPos(vsConfig.getSceneType());
                        assert sceneType != null;
                        if (!sceneType.equals(SceneType.SIT)) {
                            radiusContainer.setVisibility(View.VISIBLE);
                            etRadius.setText(String.valueOf(vsConfig.getRadius() / 1000));
                        } else {
                            radiusContainer.setVisibility(View.GONE);
                        }
                    }
                }
            }else {
                location.setText(cLocation);
            }
        } else {
            isStart = false;
        }
        //设置GridView是否可点击
//        vsGrid.setClickable(!isStart);
//        vsGrid.setPressed(!isStart);
//        vsGrid.setEnabled(!isStart);
        //设置其他控件是否可点击
        startImg.setSelected(!isStart);
        startTv.setText(isStart ? getString(R.string.vs_stop) : getString(R.string.vs_start));
        gridAdapter.setEnable(!isStart);
        centerTitle.setEnabled(!isStart);
        location.setEnabled(!isStart);
        centerContainer.setEnabled(!isStart);
        tvRadius.setEnabled(!isStart);
        etRadius.setEnabled(!isStart);
        tvRadiusUnit.setEnabled(!isStart);
    }

    @Override
    public void loadCenterLocDes(String loc) {
        this.cLocation = loc;
    }

    @Override
    public void loadCenterLocation(double[] cCoord) {
        this.cCoord = cCoord;
    }

    @Override
    public void updateStatus() {
        //更新按钮状态及启动状态
        SharePreferenceHelper spHelper = SharePreferenceHelper.getInstance(ExpandServiceApplication.getInstance());
        if (!isStart) {
            //先获取一下终点坐标，初始起点为中心点
            double[] endPoint = mPresenter.getTerminalPoint(radius * 1000);
//            double[] endPoint = new double[]{22.197938,114.698808};//测试：百度无法计算路线的终点坐标
            if (endPoint[0] != 0 && endPoint[1] != 0) {
                //存储用户数据
                VsConfig vsConfig = new VsConfig();
                vsConfig.setCenterCoords(cCoord);
                vsConfig.setRadius(radius * 1000);
                vsConfig.setSceneType(sceneIndex);
                vsConfig.setCity(location.getText().toString());
                vsConfig.setStart(true);
                spHelper.putObject(ConstantsUtils.SpKey.SP_VS_CONFIG, vsConfig);
                startVsService(vsConfig);
            }
        } else {
            //停止路线规划
            if (isBind && binder != null && ServerUtils.isServiceRunning(ExpandServiceApplication.getInstance(), VirtualSceneService.class.getCanonicalName())){
                binder.getService().stopRoute();
            }
        }
    }

    private void startVsService(VsConfig vsConfig){
        //启动服务
        vsIntent.putExtra("scene_type", vsConfig.getSceneType());
        vsIntent.putExtra("start_loc", vsConfig.getCenterCoords());
        vsIntent.putExtra("terminal_loc", BdMapUtils.getTerminalPoint(vsConfig.getCenterCoords(), vsConfig.getRadius()));
        vsIntent.putExtra("radius", vsConfig.getRadius());
        startService(vsIntent);
    }

    private ServiceConnection vServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBind = true;
            binder = (VirtualSceneService.MyBinder) service;
            binder.getService().setCallBack(callBack);
//            isStart = binder.getService().getStatus();
            updateViews(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;

        }
    };

    /**
     * 服务的回调方法
     */
    private VirtualSceneService.CallBack callBack = new VirtualSceneService.CallBack() {
        @Override
        public void onStart() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                    updateViews(true);
                }
            });

        }

        @Override
        public void onStop() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                    updateViews(true);

                }
            });
        }

        @Override
        public void onError(String msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                    Log.e(TAG, msg);
                    toast(msg);
                    isStart = false;
                    startImg.setSelected(true);
                    startTv.setText(getString(R.string.vs_start));
                }
            });

        }
    };

    private class GridAdapter extends BaseAdapter {
        private Context context;
        private List<SceneType> sceneTypes = null;
        private int selectionPos = 0;
        private boolean isEnable = true;

        public GridAdapter(Context context, List<SceneType> sceneTypes) {
            this.context = context;
            this.sceneTypes = sceneTypes;
        }

        public void setSelectedPos(int position) {
            selectionPos = position;
            notifyDataSetChanged();
        }

        public void setEnable(boolean isEnable) {
            this.isEnable = isEnable;
            notifyDataSetChanged();
        }

        public SceneType getSceneType() {
            return SceneType.getVirtualScene(selectionPos);
        }

        @Override
        public int getCount() {
            return sceneTypes.size();
        }

        @Override
        public SceneType getItem(int position) {
            return sceneTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_vs_grid, null, false);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.view = convertView.findViewById(R.id.item_vs_container);
            viewHolder.contentView = convertView.findViewById(R.id.item_content_layout);
            viewHolder.img = convertView.findViewById(R.id.item_vs_img);
            viewHolder.desc = convertView.findViewById(R.id.item_vs_name);
            viewHolder.desc.setSelected(false);
            switch (getItem(position)) {
                case SIT:
                    viewHolder.img.setImageResource(R.drawable.sit_img);
                    viewHolder.desc.setText(context.getString(R.string.vs_sit));
                    break;
                case WALK:
                    viewHolder.img.setImageResource(R.drawable.walk_img);
                    viewHolder.desc.setText(context.getString(R.string.vs_walk));
                    break;
                case RUN:
                    viewHolder.img.setImageResource(R.drawable.run_img);
                    viewHolder.desc.setText(context.getString(R.string.vs_run));
                    break;
                case DRIVE:
                    viewHolder.img.setImageResource(R.drawable.drive_img);
                    viewHolder.desc.setText(context.getString(R.string.vs_drive));
                    break;
                default:
                    break;
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isEnable){
                        return;
                    }
                    setSelectedPos(position);
                    SceneType sceneType = SceneType.getVirtualScene(position);
                    if (!sceneType.equals(SceneType.SIT)) {
                        radiusContainer.setVisibility(View.VISIBLE);
                        if (sceneType == SceneType.DRIVE) {
                            etRadius.setHint(String.format(getString(R.string.vs_input_radius), "20-100KM"));
                        } else {
                            etRadius.setHint(String.format(getString(R.string.vs_input_radius), "1KM-5KM"));
                        }
                    } else {
                        radiusContainer.setVisibility(View.GONE);
                    }
                }
            });
            if (!isEnable && selectionPos != position) {
//                viewHolder.contentView.setClickable(false);
                viewHolder.img.setImageAlpha(127);
                viewHolder.desc.setAlpha(0.5f);
            } else {
//                viewHolder.contentView.setClickable(true);
                viewHolder.img.setImageAlpha(255);
                viewHolder.desc.setAlpha(1.0f);
            }
            if (selectionPos == position) {
                viewHolder.contentView.setSelected(true);
                viewHolder.desc.setSelected(true);
            } else {
                viewHolder.contentView.setSelected(false);
            }
            return convertView;
        }

//        /**
//         * 更新外部view
//         * @param position
//         * @param view
//         * @param virtualScene
//         */
//        public abstract void updateView(int position, VirtualScene virtualScene);

        class ViewHolder {
            View view;
            View contentView;
            ImageView img;
            TextView desc;
        }
    }

    @Override
    public void showNetError() {

    }

    @Override
    public void showNoData(String message) {

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

            }
        });
    }

    @Override
    public void addDisposable(Disposable disposable) {
        if (disposables != null) {
            disposables.add(disposable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBind && binder != null && ServerUtils.isServiceRunning(ExpandServiceApplication.getInstance(), VirtualSceneService.class.getCanonicalName())) {
            binder.getService().setCallBack(null);
            unbindService(vServiceConnection);
            isBind = false;
            binder = null;
        }
        if (disposables != null && disposables.size() > 0) {
            for (Disposable d : disposables) {
                if (!d.isDisposed()) {
                    d.dispose();
                }
            }
        }
    }
}