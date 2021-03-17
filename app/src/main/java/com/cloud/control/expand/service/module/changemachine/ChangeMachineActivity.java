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
import com.cloud.control.expand.service.entity.ItemBrandModelEntity;
import com.cloud.control.expand.service.entity.PhoneBrandModelEntity;
import com.cloud.control.expand.service.entity.PhoneModelInfoEntity;
import com.cloud.control.expand.service.entity.UpdatePhoneConfigEntity;
import com.cloud.control.expand.service.home.ExpandServiceApplication;
import com.cloud.control.expand.service.injector.components.DaggerChangeMachineComponent;
import com.cloud.control.expand.service.injector.modules.ChangeMachineModule;
import com.cloud.control.expand.service.interfaces.MenuCallback;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.utils.BigDecimalUtil;
import com.cloud.control.expand.service.utils.EditTextUtils;
import com.cloud.control.expand.service.utils.RegexUtils;
import com.cloud.control.expand.service.utils.StringUtils;

import java.math.BigDecimal;
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
    @BindView(R.id.et_device_name)
    EditText etDeviceName;
    @BindView(R.id.iv_device_name_delete)
    ImageView ivDeviceNameDelete;
    @BindView(R.id.et_imei)
    EditText etImei;
    @BindView(R.id.iv_imei_delete)
    ImageView ivImeiDelete;
    @BindView(R.id.et_usable)
    EditText etUsable;
    @BindView(R.id.iv_usable_delete)
    ImageView ivUsableDelete;
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
    private PhoneModelInfoEntity.DataBean phoneModelInfoEntity;

    private boolean deviceNameFocus = false; //焦点状态
    private boolean imeiFocus = false;
    private boolean usableFocus = false;
    private boolean bluetoothMacFocus = false;
    private boolean wifiMacFocus = false;
    private BigDecimal romTotalSize;  //总空间
    private BigDecimal romUsableSize; //可用空间

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
    public void loadData(PhoneBrandModelEntity brandModelEntity, PhoneModelInfoEntity.DataBean dataBean) {
        if (brandModelEntity != null) {
            mDefaultBrandModelEntity = brandModelEntity;
        }
        if (dataBean != null) {
            phoneModelInfoEntity = dataBean;

            romTotalSize = BigDecimalUtil.decimalFormat(Float.parseFloat(dataBean.getDeviceRomTotalSize()) / (1024 * 1024 * 1024));
            romUsableSize = BigDecimalUtil.decimalFormat(Float.parseFloat(dataBean.getDeviceRomUsableSize()) / (1024 * 1024 * 1024));

            KLog.e("romTotalSize " + romTotalSize + ", romUsableSize " + romUsableSize);

            //品牌
            if (!TextUtils.isEmpty(dataBean.getBrandName()))
                tvPhoneBrand.setText(dataBean.getBrandName());
            //型号
            if (!TextUtils.isEmpty(dataBean.getMobileModel()))
                tvPhoneModel.setText(dataBean.getMobileModel());
            //机器名
            if (!TextUtils.isEmpty(dataBean.getDeviceName())) {
                if (dataBean.getDeviceName().equals("null")) {
                    etDeviceName.setText("");
                } else {
                    etDeviceName.setText(dataBean.getDeviceName());
                }
            }
            //IMEI
            if (!TextUtils.isEmpty(dataBean.getDeviceImei()))
                etImei.setText(dataBean.getDeviceImei());

            //可用空间
            if (!TextUtils.isEmpty("" + romUsableSize)) {
                etUsable.setText("" + romUsableSize);
            }
            //蓝牙MAC
            if (!TextUtils.isEmpty(dataBean.getDeviceBtMac()))
                etBluetoothMac.setText(dataBean.getDeviceBtMac());
            //机器MAC
            if (!TextUtils.isEmpty(dataBean.getDeviceMac()))
                etWifiMac.setText(dataBean.getDeviceMac());
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

    @OnClick({R.id.tv_change_machine_start, R.id.rl_phone_brand, R.id.rl_phone_model, R.id.iv_change_machine_back, R.id.iv_device_name_delete, R.id.iv_imei_delete, R.id.iv_usable_delete, R.id.iv_bluetooth_mac_delete, R.id.iv_wifi_mac_delete, R.id.tv_change_machine_reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_change_machine_start:
                if(verifyEditText()) {
                    showStartChangeMachineDialog();
                }
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
            case R.id.iv_device_name_delete:
                etDeviceName.setText("");
                break;
            case R.id.iv_imei_delete:
                etImei.setText("");
                break;
            case R.id.iv_usable_delete:
                etUsable.setText("");
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
        //机器名
        etDeviceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //输入框没有数据隐藏删除按钮
                if (!TextUtils.isEmpty(etDeviceName.getText().toString())) {
                    ivDeviceNameDelete.setVisibility(View.VISIBLE);
                } else {
                    ivDeviceNameDelete.setVisibility(View.GONE);
                }
                //输入框失去焦点隐藏删除按钮
                if (!deviceNameFocus) {
                    ivDeviceNameDelete.setVisibility(View.GONE);
                }
            }
        });
//        etDeviceName.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                deviceNameFocus = hasFocus;
//                //失去焦点
//                if (!hasFocus) {
//                    if (TextUtils.isEmpty(etDeviceName.getText().toString())) {
//                        if (!TextUtils.isEmpty(phoneModelInfoEntity.getDeviceName())) {
//                            if (phoneModelInfoEntity.getDeviceName().equals("null")) {
//                                etDeviceName.setText("");
//                            } else {
//                                etDeviceName.setText(phoneModelInfoEntity.getDeviceName());
//                            }
//                        }
//                    }
//                    ivDeviceNameDelete.setVisibility(View.GONE);
//                } else {
//                    if (!TextUtils.isEmpty(etDeviceName.getText().toString())) {
//                        ivDeviceNameDelete.setVisibility(View.VISIBLE);
//                    } else {
//                        ivDeviceNameDelete.setVisibility(View.GONE);
//                    }
//                }
//            }
//        });

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
//        etImei.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                imeiFocus = hasFocus;
//                if (!hasFocus) {
//                    if (!TextUtils.isEmpty(etImei.getText().toString()) && RegexUtils.checkNumbersAndLetter(etImei.getText().toString()) && etImei.getText().toString().length() != 15)
//                        etImei.setText(StringUtils.getRandomImei());
//                    ivImeiDelete.setVisibility(View.GONE);
//                } else {
//                    if (!TextUtils.isEmpty(etImei.getText().toString())) {
//                        ivImeiDelete.setVisibility(View.VISIBLE);
//                    } else {
//                        ivImeiDelete.setVisibility(View.GONE);
//                    }
//                }
//            }
//        });
        EditTextUtils.setEditTextNumberAndLetter(etImei);

        //可用空间
        etUsable.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(etUsable.getText().toString())) {
                    ivUsableDelete.setVisibility(View.VISIBLE);
                } else {
                    ivUsableDelete.setVisibility(View.GONE);
                }
                if (!usableFocus) {
                    ivUsableDelete.setVisibility(View.GONE);
                }
            }
        });
//        etUsable.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                usableFocus = hasFocus;
//                if (!hasFocus) {
//                    if (!TextUtils.isEmpty(etUsable.getText().toString()))
//                        etUsable.setText("" + romUsableSize.toString());
//                    ivUsableDelete.setVisibility(View.GONE);
//                } else {
//                    if (!TextUtils.isEmpty(etUsable.getText().toString())) {
//                        ivUsableDelete.setVisibility(View.VISIBLE);
//                    } else {
//                        ivUsableDelete.setVisibility(View.GONE);
//                    }
//                }
//            }
//        });

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
//        etBluetoothMac.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                bluetoothMacFocus = hasFocus;
//                if (!hasFocus) {
//                    if (!TextUtils.isEmpty(etBluetoothMac.getText().toString()) && !RegexUtils.checkMacFormat(etBluetoothMac.getText().toString()))
//                        etBluetoothMac.setText(StringUtils.getRandomMacAddress());
//                    ivBluetoothMacDelete.setVisibility(View.GONE);
//                } else {
//                    if (!TextUtils.isEmpty(etBluetoothMac.getText().toString())) {
//                        ivBluetoothMacDelete.setVisibility(View.VISIBLE);
//                    } else {
//                        ivBluetoothMacDelete.setVisibility(View.GONE);
//                    }
//                }
//            }
//        });
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
//        etWifiMac.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                wifiMacFocus = hasFocus;
//                if (!hasFocus) {
//                    ivWifiMacDelete.setVisibility(View.GONE);
//                    if (!TextUtils.isEmpty(etWifiMac.getText().toString()) && !RegexUtils.checkMacFormat(etWifiMac.getText().toString()))
//                        etWifiMac.setText(StringUtils.getRandomMacAddress());
//                    ivWifiMacDelete.setVisibility(View.GONE);
//                } else {
//                    if (!TextUtils.isEmpty(etWifiMac.getText().toString())) {
//                        ivWifiMacDelete.setVisibility(View.VISIBLE);
//                    } else {
//                        ivWifiMacDelete.setVisibility(View.GONE);
//                    }
//                }
//            }
//        });
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
        if (mDefaultBrandModelEntity == null || mDefaultBrandModelEntity.getData() == null || mDefaultBrandModelEntity.getData().size() <= 0) {
            toastMessage("数据获取失败");
            return;
        }
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
        CommonHintDialog.show(mContext, "启用新机", "确认要启用新机吗？", "取消", "确认", new MenuCallback() {
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
     * 校验输入框数据
     */
    private boolean verifyEditText(){
        if (TextUtils.isEmpty(etDeviceName.getText().toString())) {
            toastMessage("请输入机器名");
            return false;
        }
        if (TextUtils.isEmpty(etWifiMac.getText().toString())) {
            toastMessage("请输入机器MAC地址");
            return false;
        }
        if (TextUtils.isEmpty(etBluetoothMac.getText().toString())) {
            toastMessage("请输入蓝牙MAC地址");
            return false;
        }
        if (TextUtils.isEmpty(etImei.getText().toString())) {
            toastMessage("请输入IMEI");
            return false;
        }
        if (TextUtils.isEmpty(etUsable.getText().toString())) {
            toastMessage("请输入可用空间");
            return false;
        }
        if (!RegexUtils.checkMacFormat(etWifiMac.getText().toString())) {
            toastMessage("请输入正确的机器MAC地址");
            return false;
        }
        if (!RegexUtils.checkMacFormat(etBluetoothMac.getText().toString())) {
            toastMessage("请输入正确的蓝牙MAC地址");
            return false;
        }
        if (RegexUtils.checkNumbersAndLetter(etImei.getText().toString()) && etImei.getText().toString().length() != 15) {
            toastMessage("请输入正确的IMEI");
            return false;
        }
        if (BigDecimalUtil.decimalFormat(Float.parseFloat(etUsable.getText().toString())).compareTo(romTotalSize) > -1) {
            toastMessage("可用空间不能大于总空间" + romTotalSize + "GB");
            return false;
        }
        return true;
    }

    /**
     * 启动新机逻辑处理
     */
    private void changeMachine() {
        UpdatePhoneConfigEntity configEntity = new UpdatePhoneConfigEntity();
        List<UpdatePhoneConfigEntity.MobileVosBean> mobileVosBeanList = new ArrayList<>();
        UpdatePhoneConfigEntity.MobileVosBean mobileVosBean = new UpdatePhoneConfigEntity.MobileVosBean();
        mobileVosBean.setMobileId(phoneModelInfoEntity.getMobileId());
        mobileVosBean.setSn(ExpandServiceApplication.getInstance().getCardSn());
        mobileVosBean.setTypeId(3);
        mobileVosBean.setBrandId(phoneModelInfoEntity.getBrandId());
        mobileVosBean.setId(phoneModelInfoEntity.getId());
        mobileVosBean.setBrandName(tvPhoneBrand.getText().toString());
        mobileVosBean.setDeviceName(etDeviceName.getText().toString());
        mobileVosBean.setDeviceImei(etImei.getText().toString());
        mobileVosBean.setDeviceBtMac(etBluetoothMac.getText().toString());
        mobileVosBean.setDeviceMac(etWifiMac.getText().toString());
        mobileVosBean.setDeviceRomUsableSize(String.valueOf(Float.parseFloat(etUsable.getText().toString()) * (1024 * 1024 * 1024)));
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
                mPresenter.refreshSelectModelData(tvPhoneModel.getText().toString());
            }
        }
    }

}
