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
         * mobileId : 25
         * sn : RK3930C2301900152
         * typeId : 3
         * id : 17
         * brandId : 1
         * brandName : vivo
         * deviceName : V1829A
         * deviceMac : 65:03:41:5a:c4:6c
         * deviceBtMac : 31:f7:71:e8:e2:84
         * deviceImei : 862548412417972
         * deviceRomUsableSize : 38201458688
         */

        private int mobileId;
        private String sn;
        private int typeId;
        private int id;
        private int brandId;
        private String brandName;
        private String deviceName;
        private String deviceMac;
        private String deviceBtMac;
        private String deviceImei;
        private String deviceRomUsableSize;

        public int getMobileId() {
            return mobileId;
        }

        public void setMobileId(int mobileId) {
            this.mobileId = mobileId;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getBrandId() {
            return brandId;
        }

        public void setBrandId(int brandId) {
            this.brandId = brandId;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceMac() {
            return deviceMac;
        }

        public void setDeviceMac(String deviceMac) {
            this.deviceMac = deviceMac;
        }

        public String getDeviceBtMac() {
            return deviceBtMac;
        }

        public void setDeviceBtMac(String deviceBtMac) {
            this.deviceBtMac = deviceBtMac;
        }

        public String getDeviceImei() {
            return deviceImei;
        }

        public void setDeviceImei(String deviceImei) {
            this.deviceImei = deviceImei;
        }

        public String getDeviceRomUsableSize() {
            return deviceRomUsableSize;
        }

        public void setDeviceRomUsableSize(String deviceRomUsableSize) {
            this.deviceRomUsableSize = deviceRomUsableSize;
        }

        @Override
        public String toString() {
            return "MobileVosBean{" +
                    "mobileId=" + mobileId +
                    ", sn='" + sn + '\'' +
                    ", typeId=" + typeId +
                    ", id=" + id +
                    ", brandId=" + brandId +
                    ", brandName='" + brandName + '\'' +
                    ", deviceName='" + deviceName + '\'' +
                    ", deviceMac='" + deviceMac + '\'' +
                    ", deviceBtMac='" + deviceBtMac + '\'' +
                    ", deviceImei='" + deviceImei + '\'' +
                    ", deviceRomUsableSize='" + deviceRomUsableSize + '\'' +
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
