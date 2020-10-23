package com.cloud.control.expand.service.module.changemachine;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.base.BaseActivity;
import com.cloud.control.expand.service.dialog.CommonHintDialog;
import com.cloud.control.expand.service.dialog.PhoneBrandModelDialog;
import com.cloud.control.expand.service.entity.ChangeMachineStatusEntity;
import com.cloud.control.expand.service.entity.ItemBrandModelEntity;
import com.cloud.control.expand.service.entity.ModelInfoEntity;
import com.cloud.control.expand.service.entity.PhoneBrandModelEntity;
import com.cloud.control.expand.service.entity.PhoneModelInfoEntity;
import com.cloud.control.expand.service.entity.UpdatePhoneConfigEntity;
import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.injector.components.DaggerChangeMachineComponent;
import com.cloud.control.expand.service.injector.modules.ChangeMachineModule;
import com.cloud.control.expand.service.interfaces.MenuCallback;
import com.cloud.control.expand.service.utils.EditTextUtils;
import com.cloud.control.expand.service.utils.RegexUtils;
import com.cloud.control.expand.service.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author：abin
 * Date：2020/9/29
 * Description：一键新机
 */
public class ChangeMachineActivity extends BaseActivity<ChangeMachinePresenter> implements ChangeMachineView, PhoneBrandModelDialog.OnMenuClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_change_machine_start)
    TextView ivChangeMachineStart;
    @BindView(R.id.tv_change_machine_reset)
    TextView ivChangeMachineReset;
    @BindView(R.id.rl_phone_brand)
    RelativeLayout rlPhoneBrand;
    @BindView(R.id.tv_phone_brand)
    TextView tvPhoneBrand;
    @BindView(R.id.rl_phone_model)
    RelativeLayout rlPhoneModel;
    @BindView(R.id.tv_phone_model)
    TextView tvPhoneModel;
    @BindView(R.id.rl_phone_memory)
    RelativeLayout rlPhoneMemory;
    @BindView(R.id.tv_phone_memory)
    TextView tvPhoneMemory;
    @BindView(R.id.et_system_version)
    EditText etSystemVersion;
    @BindView(R.id.iv_system_version_delete)
    ImageView ivSystemVersionDelete;
    @BindView(R.id.et_imei)
    EditText etImei;
    @BindView(R.id.iv_imei_delete)
    ImageView ivImeiDelete;
    @BindView(R.id.et_sn)
    EditText etSn;
    @BindView(R.id.iv_sn_delete)
    ImageView ivSnDelete;
    @BindView(R.id.et_psn)
    EditText etPsn;
    @BindView(R.id.iv_psn_delete)
    ImageView ivPsnDelete;
    @BindView(R.id.et_bluetooth_mac)
    EditText etBluetoothMac;
    @BindView(R.id.iv_bluetooth_mac_delete)
    ImageView ivBluetoothMacDelete;
    @BindView(R.id.et_wifi_mac)
    EditText etWifiMac;
    @BindView(R.id.iv_wifi_mac_delete)
    ImageView ivWifiMacDelete;

    //品牌型号统一弹框
    private PhoneBrandModelDialog mPhoneBrandModelDialog;
    //进入界面默认选中品牌机型数据
    private PhoneBrandModelEntity mDefaultBrandModelEntity;
    //品牌或者型号弹框数据
    private List<ItemBrandModelEntity> brandOrModelList = new ArrayList<>();
    private static final int TYPE_BRAND = 1; //品牌
    private static final int TYPE_MODEL = 2; //型号

    private boolean systemBrandFocus = false; //焦点状态
    private boolean imeiFocus = false;
    private boolean snFocus = false;
    private boolean psnFocus = false;
    private boolean bluetoothMacFocus = false;
    private boolean wifiMacFocus = false;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_change_machine;
    }

    @Override
    protected void initInjector() {
        DaggerChangeMachineComponent.builder()
                .changeMachineModule(new ChangeMachineModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar, false, "");
        inputBoxDeal();
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData();
    }

    @Override
    public void loadData(ModelInfoEntity modelInfoEntity, ChangeMachineStatusEntity statusEntity, PhoneBrandModelEntity brandModelEntity, PhoneModelInfoEntity.DataBean dataBean) {
        if (brandModelEntity != null) {
            mDefaultBrandModelEntity = brandModelEntity;
        }
        if (modelInfoEntity != null) {
            //品牌
            if (!TextUtils.isEmpty(modelInfoEntity.getBrand()))
                tvPhoneBrand.setText(modelInfoEntity.getBrand());
            //型号
            if (!TextUtils.isEmpty(modelInfoEntity.getModel()))
                tvPhoneModel.setText(modelInfoEntity.getModel());
        }
        if (dataBean != null) {
            //内存
            if (!TextUtils.isEmpty(dataBean.getMemory()))
                tvPhoneMemory.setText(dataBean.getMemory() + "G");
            //系统版本号
            if (!TextUtils.isEmpty(dataBean.getAndroid_version()))
                etSystemVersion.setText(dataBean.getAndroid_version());
            //IMEI
            if (!TextUtils.isEmpty(dataBean.getDevice_imei()))
                etImei.setText(dataBean.getDevice_imei());
            //SN
            if (!TextUtils.isEmpty(dataBean.getSys_serial_no()))
                etSn.setText(dataBean.getSys_serial_no());
            //PSN
            if (!TextUtils.isEmpty(dataBean.getProduct_serial_no()))
                etPsn.setText(dataBean.getProduct_serial_no());
            //蓝牙MAC
            if (!TextUtils.isEmpty(dataBean.getBluetooth_mac()))
                etBluetoothMac.setText(dataBean.getBluetooth_mac());
            //WIFI-MAC
            if (!TextUtils.isEmpty(dataBean.getWifi_mac()))
                etWifiMac.setText(dataBean.getWifi_mac());
        }
    }

    @Override
    public void toast(String message) {
        toastMessage(message);
    }

    @OnClick({R.id.tv_change_machine_start, R.id.rl_phone_brand, R.id.rl_phone_model, R.id.iv_change_machine_back, R.id.iv_system_version_delete, R.id.iv_imei_delete, R.id.iv_sn_delete, R.id.iv_psn_delete, R.id.iv_bluetooth_mac_delete, R.id.iv_wifi_mac_delete, R.id.tv_change_machine_reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_change_machine_start:
                showStartChangeMachineDialog();
                break;
            case R.id.tv_change_machine_reset:
                mPresenter.refreshAllData();
                break;
            case R.id.rl_phone_brand:
                showPhoneBrandModelDialog(TYPE_BRAND);
                break;
            case R.id.rl_phone_model:
                showPhoneBrandModelDialog(TYPE_MODEL);
                break;
            case R.id.iv_system_version_delete:
                etSystemVersion.setText("");
                break;
            case R.id.iv_imei_delete:
                etImei.setText("");
                break;
            case R.id.iv_sn_delete:
                etSn.setText("");
                break;
            case R.id.iv_psn_delete:
                etPsn.setText("");
                break;
            case R.id.iv_bluetooth_mac_delete:
                etBluetoothMac.setText("");
                break;
            case R.id.iv_wifi_mac_delete:
                etWifiMac.setText("");
                break;
            case R.id.iv_change_machine_back:
                finish();
                break;
        }
    }

    /**
     * 输入框处理
     */
    private void inputBoxDeal() {
        //系统版本输入框
        etSystemVersion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //输入框没有数据隐藏删除按钮
                if (!TextUtils.isEmpty(etSystemVersion.getText().toString())) {
                    ivSystemVersionDelete.setVisibility(View.VISIBLE);
                } else {
                    ivSystemVersionDelete.setVisibility(View.GONE);
                }
                //输入框失去焦点隐藏删除按钮
                if (!systemBrandFocus) {
                    ivSystemVersionDelete.setVisibility(View.GONE);
                }
            }
        });
        etSystemVersion.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                systemBrandFocus = hasFocus;
                //失去焦点
                if (!hasFocus) {
                    //输入框不为空、字符校验不通过、后缀为.，自动生成数据
                    if (!TextUtils.isEmpty(etSystemVersion.getText().toString()) && !RegexUtils.checkNumbersAndDots(etSystemVersion.getText().toString()))
                        etSystemVersion.setText("7.1.1");
                    ivSystemVersionDelete.setVisibility(View.GONE);
                } else {
                    if (!TextUtils.isEmpty(etSystemVersion.getText().toString())) {
                        ivSystemVersionDelete.setVisibility(View.VISIBLE);
                    } else {
                        ivSystemVersionDelete.setVisibility(View.GONE);
                    }
                }
            }
        });
        EditTextUtils.setEditTextNumberAndDot(etSystemVersion);

        //IMEI
        etImei.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(etImei.getText().toString())) {
                    ivImeiDelete.setVisibility(View.VISIBLE);
                } else {
                    ivImeiDelete.setVisibility(View.GONE);
                }
                if (!imeiFocus) {
                    ivImeiDelete.setVisibility(View.GONE);
                }
            }
        });
        etImei.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                imeiFocus = hasFocus;
                if (!hasFocus) {
                    if (!TextUtils.isEmpty(etImei.getText().toString()) && RegexUtils.checkNumbersAndLetter(etImei.getText().toString()) && etImei.getText().toString().length() != 15)
                        etImei.setText(StringUtils.getRandomImei());
                    ivImeiDelete.setVisibility(View.GONE);
                } else {
                    if (!TextUtils.isEmpty(etImei.getText().toString())) {
                        ivImeiDelete.setVisibility(View.VISIBLE);
                    } else {
                        ivImeiDelete.setVisibility(View.GONE);
                    }
                }
            }
        });
        EditTextUtils.setEditTextNumberAndLetter(etImei);

        //SN
        etSn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(etSn.getText().toString())) {
                    ivSnDelete.setVisibility(View.VISIBLE);
                } else {
                    ivSnDelete.setVisibility(View.GONE);
                }
                if (!snFocus) {
                    ivSnDelete.setVisibility(View.GONE);
                }
            }
        });
        etSn.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                snFocus = hasFocus;
                if (!hasFocus) {
                    if (!TextUtils.isEmpty(etSn.getText().toString()) && RegexUtils.checkNumbersAndLetter(etSn.getText().toString()) && etSn.getText().toString().length() != 16)
                        etSn.setText(StringUtils.getRandomSysSerialNo());
                    ivSnDelete.setVisibility(View.GONE);
                } else {
                    if (!TextUtils.isEmpty(etSn.getText().toString())) {
                        ivSnDelete.setVisibility(View.VISIBLE);
                    } else {
                        ivSnDelete.setVisibility(View.GONE);
                    }
                }
            }
        });
        EditTextUtils.setEditTextNumberAndLetter(etSn);

        //PSN
        etPsn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(etPsn.getText().toString())) {
                    ivPsnDelete.setVisibility(View.VISIBLE);
                } else {
                    ivPsnDelete.setVisibility(View.GONE);
                }
                if (!psnFocus) {
                    ivPsnDelete.setVisibility(View.GONE);
                }
            }
        });
        etPsn.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                psnFocus = hasFocus;
                if (!hasFocus) {
                    if (!TextUtils.isEmpty(etPsn.getText().toString()) && RegexUtils.checkNumbersAndLetter(etPsn.getText().toString()) && etPsn.getText().toString().length() != 16)
                        etPsn.setText(StringUtils.getRandomSysSerialNo());
                    ivPsnDelete.setVisibility(View.GONE);
                } else {
                    if (!TextUtils.isEmpty(etPsn.getText().toString())) {
                        ivPsnDelete.setVisibility(View.VISIBLE);
                    } else {
                        ivPsnDelete.setVisibility(View.GONE);
                    }
                }
            }
        });
        EditTextUtils.setEditTextNumberAndLetter(etPsn);

        //蓝牙MAC
        etBluetoothMac.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(etBluetoothMac.getText().toString())) {
                    ivBluetoothMacDelete.setVisibility(View.VISIBLE);
                } else {
                    ivBluetoothMacDelete.setVisibility(View.GONE);
                }
                if (!bluetoothMacFocus) {
                    ivBluetoothMacDelete.setVisibility(View.GONE);
                }
            }
        });
        etBluetoothMac.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                bluetoothMacFocus = hasFocus;
                if (!hasFocus) {
                    if (!TextUtils.isEmpty(etBluetoothMac.getText().toString()) && !RegexUtils.checkMacFormat(etBluetoothMac.getText().toString()))
                        etBluetoothMac.setText(StringUtils.getRandomMacAddress());
                    ivBluetoothMacDelete.setVisibility(View.GONE);
                } else {
                    if (!TextUtils.isEmpty(etBluetoothMac.getText().toString())) {
                        ivBluetoothMacDelete.setVisibility(View.VISIBLE);
                    } else {
                        ivBluetoothMacDelete.setVisibility(View.GONE);
                    }
                }
            }
        });
        EditTextUtils.setEditTextNumberAndLetterAndColon(etBluetoothMac);

        //WIFI MAC
        etWifiMac.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(etWifiMac.getText().toString())) {
                    ivWifiMacDelete.setVisibility(View.VISIBLE);
                } else {
                    ivWifiMacDelete.setVisibility(View.GONE);
                }
                if (!wifiMacFocus) {
                    ivWifiMacDelete.setVisibility(View.GONE);
                }
            }
        });
        etWifiMac.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                wifiMacFocus = hasFocus;
                if (!hasFocus) {
                    ivWifiMacDelete.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(etWifiMac.getText().toString()) && !RegexUtils.checkMacFormat(etWifiMac.getText().toString()))
                        etWifiMac.setText(StringUtils.getRandomMacAddress());
                    ivWifiMacDelete.setVisibility(View.GONE);
                } else {
                    if (!TextUtils.isEmpty(etWifiMac.getText().toString())) {
                        ivWifiMacDelete.setVisibility(View.VISIBLE);
                    } else {
                        ivWifiMacDelete.setVisibility(View.GONE);
                    }
                }
            }
        });
        EditTextUtils.setEditTextNumberAndLetterAndColon(etWifiMac);
    }

    /**
     * 手机品牌型号弹框
     *
     * @param type 1 品牌  2 型号
     */
    private void showPhoneBrandModelDialog(int type) {
        if (mPhoneBrandModelDialog == null) {
            mPhoneBrandModelDialog = new PhoneBrandModelDialog();
            mPhoneBrandModelDialog.setOnMenuClickListener(this);
        }
        //显示品牌数据
        brandOrModelList.clear();
        if (type == TYPE_BRAND) {
            for (int i = 0; i < mDefaultBrandModelEntity.getData().size(); i++) {
                ItemBrandModelEntity itemBrandModelEntity = new ItemBrandModelEntity();
                itemBrandModelEntity.setData(mDefaultBrandModelEntity.getData().get(i).getBrandName());
                itemBrandModelEntity.setType(type);
                if (tvPhoneBrand.getText().toString().equals(mDefaultBrandModelEntity.getData().get(i).getBrandName())) {
                    itemBrandModelEntity.setCheck(true);
                } else {
                    itemBrandModelEntity.setCheck(false);
                }
                brandOrModelList.add(itemBrandModelEntity);
            }
        } else { //显示型号数据
            for (int i = 0; i < mDefaultBrandModelEntity.getData().size(); i++) {
                if (tvPhoneBrand.getText().toString().equals(mDefaultBrandModelEntity.getData().get(i).getBrandName())) {
                    for (int j = 0; j < mDefaultBrandModelEntity.getData().get(i).getModel().size(); j++) {
                        ItemBrandModelEntity itemBrandModelEntity = new ItemBrandModelEntity();
                        itemBrandModelEntity.setData(mDefaultBrandModelEntity.getData().get(i).getModel().get(j).getMobileModel());
                        itemBrandModelEntity.setType(type);
                        if (tvPhoneModel.getText().toString().equals(mDefaultBrandModelEntity.getData().get(i).getModel().get(j).getMobileModel())) {
                            itemBrandModelEntity.setCheck(true);
                        } else {
                            itemBrandModelEntity.setCheck(false);
                        }
                        brandOrModelList.add(itemBrandModelEntity);
                    }
                }
            }
        }
        mPhoneBrandModelDialog.setItemData(brandOrModelList);
        if (!mPhoneBrandModelDialog.isAdded() && !mPhoneBrandModelDialog.isVisible() && !mPhoneBrandModelDialog.isRemoving()) {
            mPhoneBrandModelDialog.show(getFragmentManager(), "");
        }
    }

    /**
     * 启动新机弹框
     */
    private void showStartChangeMachineDialog() {
        CommonHintDialog.show(mContext, "确认要启用新机吗？", "确认后将重启云手机，并更改云手机信息。", "取消", "确认", new MenuCallback() {
            @Override
            public void onLeftButtonClick(Object value) {

            }

            @Override
            public void onRightButtonClick(Object value) {
                changeMachine();
            }
        });
    }

    /**
     * 启动新机逻辑处理
     */
    private void changeMachine() {
        if (TextUtils.isEmpty(etSystemVersion.getText().toString())) {
            toastMessage("请输入系统版本号");
            return;
        }
        if (TextUtils.isEmpty(etImei.getText().toString())) {
            toastMessage("请输入IMEI");
            return;
        }
        if (TextUtils.isEmpty(etSn.getText().toString())) {
            toastMessage("请输入SN");
            return;
        }
        if (TextUtils.isEmpty(etPsn.getText().toString())) {
            toastMessage("请输入PSN");
            return;
        }
        if (TextUtils.isEmpty(etBluetoothMac.getText().toString())) {
            toastMessage("请输入蓝牙-MAC");
            return;
        }
        if (TextUtils.isEmpty(etWifiMac.getText().toString())) {
            toastMessage("请输入WIFI-MAC");
            return;
        }
        UpdatePhoneConfigEntity configEntity = new UpdatePhoneConfigEntity();
        List<UpdatePhoneConfigEntity.MobileVosBean> mobileVosBeanList = new ArrayList<>();
        UpdatePhoneConfigEntity.MobileVosBean mobileVosBean = new UpdatePhoneConfigEntity.MobileVosBean();
        mobileVosBean.setAndroid_version(etSystemVersion.getText().toString());
        mobileVosBean.setBluetooth_mac(etBluetoothMac.getText().toString());
        mobileVosBean.setDevice_imei(etImei.getText().toString());
        mobileVosBean.setMemory(tvPhoneMemory.getText().toString().substring(0, 1));
        mobileVosBean.setMobileType(tvPhoneModel.getText().toString());
        mobileVosBean.setProduct_serial_no(etPsn.getText().toString());
        mobileVosBean.setSn(ExpandServiceApplication.getInstance().getCardSn());
        mobileVosBean.setSys_serial_no(etSn.getText().toString());
        mobileVosBean.setTypeId(3);
        mobileVosBean.setWifi_mac(etWifiMac.getText().toString());
        mobileVosBeanList.add(mobileVosBean);
        configEntity.setMobileVos(mobileVosBeanList);
        mPresenter.startChangeMachine(configEntity);
    }

    @Override
    public void onMenuClick(ItemBrandModelEntity item) {
        if (item != null) {
            if (!TextUtils.isEmpty(item.getData())) {
                if (item.getType() == TYPE_BRAND) {
                    //更新品牌型号数据
                    tvPhoneBrand.setText(item.getData());
                    for (int i = 0; i < mDefaultBrandModelEntity.getData().size(); i++) {
                        if (tvPhoneBrand.getText().toString().equals(mDefaultBrandModelEntity.getData().get(i).getBrandName())) {
                            for (int j = 0; j < mDefaultBrandModelEntity.getData().get(i).getModel().size(); j++) {
                                if (!TextUtils.isEmpty(mDefaultBrandModelEntity.getData().get(i).getModel().get(0).getMobileModel())) {
                                    tvPhoneModel.setText(mDefaultBrandModelEntity.getData().get(i).getModel().get(0).getMobileModel());
                                }
                            }
                        }
                    }
                } else {
                    //更新型号数据
                    tvPhoneModel.setText(item.getData());
                }
                //选中机型后更新数据
                mPresenter.refreshSelectModelData(tvPhoneBrand.getText().toString(), tvPhoneModel.getText().toString());
            }
        }
    }

}
