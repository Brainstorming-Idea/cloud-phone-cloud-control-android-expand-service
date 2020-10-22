package com.cloud.control.expand.service.entity;

import java.util.List;

/**
 * Author：abin
 * Date：2020/10/17
 * Description： 一键新机参数
 */
public class UpdatePhoneConfigEntity {

    private List<MobileVosBean> mobileVos;

    public List<MobileVosBean> getMobileVos() {
        return mobileVos;
    }

    public void setMobileVos(List<MobileVosBean> mobileVos) {
        this.mobileVos = mobileVos;
    }

    public static class MobileVosBean {
        /**
         * android_version : 8.1.0
         * bluetooth_mac : e6:20:4a:e4:50:53
         * device_imei : 862548419424575
         * memory : 6
         * mobileType : 小米8
         * product_serial_no : HNI303YBGB3MC6UJ
         * sn : RK3930C2301900163
         * sys_serial_no : 0K9L9ZS8RKEUD2TT
         * typeId : 3
         * wifi_mac : 53:03:5f:f9:36:45
         */

        private String android_version;
        private String bluetooth_mac;
        private String device_imei;
        private String memory;
        private String mobileType;
        private String product_serial_no;
        private String sn;
        private String sys_serial_no;
        private int typeId;
        private String wifi_mac;

        public String getAndroid_version() {
            return android_version;
        }

        public void setAndroid_version(String android_version) {
            this.android_version = android_version;
        }

        public String getBluetooth_mac() {
            return bluetooth_mac;
        }

        public void setBluetooth_mac(String bluetooth_mac) {
            this.bluetooth_mac = bluetooth_mac;
        }

        public String getDevice_imei() {
            return device_imei;
        }

        public void setDevice_imei(String device_imei) {
            this.device_imei = device_imei;
        }

        public String getMemory() {
            return memory;
        }

        public void setMemory(String memory) {
            this.memory = memory;
        }

        public String getMobileType() {
            return mobileType;
        }

        public void setMobileType(String mobileType) {
            this.mobileType = mobileType;
        }

        public String getProduct_serial_no() {
            return product_serial_no;
        }

        public void setProduct_serial_no(String product_serial_no) {
            this.product_serial_no = product_serial_no;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getSys_serial_no() {
            return sys_serial_no;
        }

        public void setSys_serial_no(String sys_serial_no) {
            this.sys_serial_no = sys_serial_no;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getWifi_mac() {
            return wifi_mac;
        }

        public void setWifi_mac(String wifi_mac) {
            this.wifi_mac = wifi_mac;
        }

        @Override
        public String toString() {
            return "MobileVosBean{" +
                    "android_version='" + android_version + '\'' +
                    ", bluetooth_mac='" + bluetooth_mac + '\'' +
                    ", device_imei='" + device_imei + '\'' +
                    ", memory='" + memory + '\'' +
                    ", mobileType='" + mobileType + '\'' +
                    ", product_serial_no='" + product_serial_no + '\'' +
                    ", sn='" + sn + '\'' +
                    ", sys_serial_no='" + sys_serial_no + '\'' +
                    ", typeId=" + typeId +
                    ", wifi_mac='" + wifi_mac + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UpdatePhoneConfigEntity{" +
                "mobileVos=" + mobileVos +
                '}';
    }
}
