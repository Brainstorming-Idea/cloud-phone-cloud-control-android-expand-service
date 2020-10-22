package com.cloud.control.expand.service.entity;

import java.util.List;

/**
 * Author：abin
 * Date：2020/10/17
 * Description： 机型配置参数
 */
public class PhoneModelInfoEntity {

    /**
     * status : 0
     * msg : 成功
     * data : [{"sn":"RK3930C2301900163","memory":"4","android_version":"9","device_imei":"862548413621219","sys_serial_no":"YSBZCGJUYU1WFHO6","product_serial_no":"V6H8PWYNRJQ59BW7","bluetooth_mac":"41:06:58:21:5f:b2","wifi_mac":"e8:3b:28:50:76:de"}]
     */

    private int status;
    private String msg;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sn : RK3930C2301900163
         * memory : 4
         * android_version : 9
         * device_imei : 862548413621219
         * sys_serial_no : YSBZCGJUYU1WFHO6
         * product_serial_no : V6H8PWYNRJQ59BW7
         * bluetooth_mac : 41:06:58:21:5f:b2
         * wifi_mac : e8:3b:28:50:76:de
         */

        private String sn;
        private String memory;
        private String android_version;
        private String device_imei;
        private String sys_serial_no;
        private String product_serial_no;
        private String bluetooth_mac;
        private String wifi_mac;

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getMemory() {
            return memory;
        }

        public void setMemory(String memory) {
            this.memory = memory;
        }

        public String getAndroid_version() {
            return android_version;
        }

        public void setAndroid_version(String android_version) {
            this.android_version = android_version;
        }

        public String getDevice_imei() {
            return device_imei;
        }

        public void setDevice_imei(String device_imei) {
            this.device_imei = device_imei;
        }

        public String getSys_serial_no() {
            return sys_serial_no;
        }

        public void setSys_serial_no(String sys_serial_no) {
            this.sys_serial_no = sys_serial_no;
        }

        public String getProduct_serial_no() {
            return product_serial_no;
        }

        public void setProduct_serial_no(String product_serial_no) {
            this.product_serial_no = product_serial_no;
        }

        public String getBluetooth_mac() {
            return bluetooth_mac;
        }

        public void setBluetooth_mac(String bluetooth_mac) {
            this.bluetooth_mac = bluetooth_mac;
        }

        public String getWifi_mac() {
            return wifi_mac;
        }

        public void setWifi_mac(String wifi_mac) {
            this.wifi_mac = wifi_mac;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "sn='" + sn + '\'' +
                    ", memory='" + memory + '\'' +
                    ", android_version='" + android_version + '\'' +
                    ", device_imei='" + device_imei + '\'' +
                    ", sys_serial_no='" + sys_serial_no + '\'' +
                    ", product_serial_no='" + product_serial_no + '\'' +
                    ", bluetooth_mac='" + bluetooth_mac + '\'' +
                    ", wifi_mac='" + wifi_mac + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PhoneModelInfoEntity{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
