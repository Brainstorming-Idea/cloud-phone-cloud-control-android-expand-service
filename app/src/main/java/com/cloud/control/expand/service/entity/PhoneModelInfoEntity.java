package com.cloud.control.expand.service.entity;

/**
 * Author：abin
 * Date：2020/10/17
 * Description： 机型配置参数
 */
public class PhoneModelInfoEntity {

    /**
     * status : 0
     * retCode : null
     * msg : 成功
     * data : {"id":3,"brandId":2,"mobileModel":"MI 6","deviceModel":"MI 6","deviceRam":"6","basebrandVersion":"","density":"480","deviceRomUsableSize":"34138468352","deviceCpuMaxFreq":"1900800","androidVersion":"9","displayId":"PKQ1.190118.001","networkType":"13","deviceImsi":"460074520726247","deviceCpuCores":"8","deviceMac":"32:4d:8a:20:3e:78","deviceBoard":"msm8998","deviceBtMac":"c2:ee:8f:91:d4:cf","deviceSerialno":"","kernelVersion":"Linux localhost 4.4.153-perf+ #1 SMP PREEMPT Thu Mar 5 11:28:37 CST 2020 aarch64","callState":"0","operatorName":"中国移动","resolution":"1080x1920","deviceName":"MI 6","deviceGpuRenderer":"Adreno (TM) 540","deviceRomTotalSize":"137438953472","sdkVersion":"28","deviceGpuVendor":"Qualcomm","deviceImei":"862548413222474","deviceHardware":"qcom","deviceGpuVersion":"OpenGL ES-CM 1.1","createTime":1612668795000,"sysSerialNo":"BTEK1J84HAVRIGDJ","operatorCode":"46000","productSerialNo":"I5DTJREUJ106ORTW","deviceId":"d96e4d2d5e246","sn":"RK3930C2301900143","mobileId":27,"brandName":"小米"}
     */

    private int status;
    private Object retCode;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getRetCode() {
        return retCode;
    }

    public void setRetCode(Object retCode) {
        this.retCode = retCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * brandId : 2
         * mobileModel : MI 6
         * deviceModel : MI 6
         * deviceRam : 6
         * basebrandVersion :
         * density : 480
         * deviceRomUsableSize : 34138468352
         * deviceCpuMaxFreq : 1900800
         * androidVersion : 9
         * displayId : PKQ1.190118.001
         * networkType : 13
         * deviceImsi : 460074520726247
         * deviceCpuCores : 8
         * deviceMac : 32:4d:8a:20:3e:78
         * deviceBoard : msm8998
         * deviceBtMac : c2:ee:8f:91:d4:cf
         * deviceSerialno :
         * kernelVersion : Linux localhost 4.4.153-perf+ #1 SMP PREEMPT Thu Mar 5 11:28:37 CST 2020 aarch64
         * callState : 0
         * operatorName : 中国移动
         * resolution : 1080x1920
         * deviceName : MI 6
         * deviceGpuRenderer : Adreno (TM) 540
         * deviceRomTotalSize : 137438953472
         * sdkVersion : 28
         * deviceGpuVendor : Qualcomm
         * deviceImei : 862548413222474
         * deviceHardware : qcom
         * deviceGpuVersion : OpenGL ES-CM 1.1
         * createTime : 1612668795000
         * sysSerialNo : BTEK1J84HAVRIGDJ
         * operatorCode : 46000
         * productSerialNo : I5DTJREUJ106ORTW
         * deviceId : d96e4d2d5e246
         * sn : RK3930C2301900143
         * mobileId : 27
         * brandName : 小米
         */

        private int id;
        private int brandId;
        private String mobileModel;
        private String deviceModel;
        private String deviceRam;
        private String basebrandVersion;
        private String density;
        private String deviceRomUsableSize;
        private String deviceCpuMaxFreq;
        private String androidVersion;
        private String displayId;
        private String networkType;
        private String deviceImsi;
        private String deviceCpuCores;
        private String deviceMac;
        private String deviceBoard;
        private String deviceBtMac;
        private String deviceSerialno;
        private String kernelVersion;
        private String callState;
        private String operatorName;
        private String resolution;
        private String deviceName;
        private String deviceGpuRenderer;
        private String deviceRomTotalSize;
        private String sdkVersion;
        private String deviceGpuVendor;
        private String deviceImei;
        private String deviceHardware;
        private String deviceGpuVersion;
        private long createTime;
        private String sysSerialNo;
        private String operatorCode;
        private String productSerialNo;
        private String deviceId;
        private String sn;
        private int mobileId;
        private String brandName;

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

        public String getMobileModel() {
            return mobileModel;
        }

        public void setMobileModel(String mobileModel) {
            this.mobileModel = mobileModel;
        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getDeviceRam() {
            return deviceRam;
        }

        public void setDeviceRam(String deviceRam) {
            this.deviceRam = deviceRam;
        }

        public String getBasebrandVersion() {
            return basebrandVersion;
        }

        public void setBasebrandVersion(String basebrandVersion) {
            this.basebrandVersion = basebrandVersion;
        }

        public String getDensity() {
            return density;
        }

        public void setDensity(String density) {
            this.density = density;
        }

        public String getDeviceRomUsableSize() {
            return deviceRomUsableSize;
        }

        public void setDeviceRomUsableSize(String deviceRomUsableSize) {
            this.deviceRomUsableSize = deviceRomUsableSize;
        }

        public String getDeviceCpuMaxFreq() {
            return deviceCpuMaxFreq;
        }

        public void setDeviceCpuMaxFreq(String deviceCpuMaxFreq) {
            this.deviceCpuMaxFreq = deviceCpuMaxFreq;
        }

        public String getAndroidVersion() {
            return androidVersion;
        }

        public void setAndroidVersion(String androidVersion) {
            this.androidVersion = androidVersion;
        }

        public String getDisplayId() {
            return displayId;
        }

        public void setDisplayId(String displayId) {
            this.displayId = displayId;
        }

        public String getNetworkType() {
            return networkType;
        }

        public void setNetworkType(String networkType) {
            this.networkType = networkType;
        }

        public String getDeviceImsi() {
            return deviceImsi;
        }

        public void setDeviceImsi(String deviceImsi) {
            this.deviceImsi = deviceImsi;
        }

        public String getDeviceCpuCores() {
            return deviceCpuCores;
        }

        public void setDeviceCpuCores(String deviceCpuCores) {
            this.deviceCpuCores = deviceCpuCores;
        }

        public String getDeviceMac() {
            return deviceMac;
        }

        public void setDeviceMac(String deviceMac) {
            this.deviceMac = deviceMac;
        }

        public String getDeviceBoard() {
            return deviceBoard;
        }

        public void setDeviceBoard(String deviceBoard) {
            this.deviceBoard = deviceBoard;
        }

        public String getDeviceBtMac() {
            return deviceBtMac;
        }

        public void setDeviceBtMac(String deviceBtMac) {
            this.deviceBtMac = deviceBtMac;
        }

        public String getDeviceSerialno() {
            return deviceSerialno;
        }

        public void setDeviceSerialno(String deviceSerialno) {
            this.deviceSerialno = deviceSerialno;
        }

        public String getKernelVersion() {
            return kernelVersion;
        }

        public void setKernelVersion(String kernelVersion) {
            this.kernelVersion = kernelVersion;
        }

        public String getCallState() {
            return callState;
        }

        public void setCallState(String callState) {
            this.callState = callState;
        }

        public String getOperatorName() {
            return operatorName;
        }

        public void setOperatorName(String operatorName) {
            this.operatorName = operatorName;
        }

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceGpuRenderer() {
            return deviceGpuRenderer;
        }

        public void setDeviceGpuRenderer(String deviceGpuRenderer) {
            this.deviceGpuRenderer = deviceGpuRenderer;
        }

        public String getDeviceRomTotalSize() {
            return deviceRomTotalSize;
        }

        public void setDeviceRomTotalSize(String deviceRomTotalSize) {
            this.deviceRomTotalSize = deviceRomTotalSize;
        }

        public String getSdkVersion() {
            return sdkVersion;
        }

        public void setSdkVersion(String sdkVersion) {
            this.sdkVersion = sdkVersion;
        }

        public String getDeviceGpuVendor() {
            return deviceGpuVendor;
        }

        public void setDeviceGpuVendor(String deviceGpuVendor) {
            this.deviceGpuVendor = deviceGpuVendor;
        }

        public String getDeviceImei() {
            return deviceImei;
        }

        public void setDeviceImei(String deviceImei) {
            this.deviceImei = deviceImei;
        }

        public String getDeviceHardware() {
            return deviceHardware;
        }

        public void setDeviceHardware(String deviceHardware) {
            this.deviceHardware = deviceHardware;
        }

        public String getDeviceGpuVersion() {
            return deviceGpuVersion;
        }

        public void setDeviceGpuVersion(String deviceGpuVersion) {
            this.deviceGpuVersion = deviceGpuVersion;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getSysSerialNo() {
            return sysSerialNo;
        }

        public void setSysSerialNo(String sysSerialNo) {
            this.sysSerialNo = sysSerialNo;
        }

        public String getOperatorCode() {
            return operatorCode;
        }

        public void setOperatorCode(String operatorCode) {
            this.operatorCode = operatorCode;
        }

        public String getProductSerialNo() {
            return productSerialNo;
        }

        public void setProductSerialNo(String productSerialNo) {
            this.productSerialNo = productSerialNo;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public int getMobileId() {
            return mobileId;
        }

        public void setMobileId(int mobileId) {
            this.mobileId = mobileId;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", brandId=" + brandId +
                    ", mobileModel='" + mobileModel + '\'' +
                    ", deviceModel='" + deviceModel + '\'' +
                    ", deviceRam='" + deviceRam + '\'' +
                    ", basebrandVersion='" + basebrandVersion + '\'' +
                    ", density='" + density + '\'' +
                    ", deviceRomUsableSize='" + deviceRomUsableSize + '\'' +
                    ", deviceCpuMaxFreq='" + deviceCpuMaxFreq + '\'' +
                    ", androidVersion='" + androidVersion + '\'' +
                    ", displayId='" + displayId + '\'' +
                    ", networkType='" + networkType + '\'' +
                    ", deviceImsi='" + deviceImsi + '\'' +
                    ", deviceCpuCores='" + deviceCpuCores + '\'' +
                    ", deviceMac='" + deviceMac + '\'' +
                    ", deviceBoard='" + deviceBoard + '\'' +
                    ", deviceBtMac='" + deviceBtMac + '\'' +
                    ", deviceSerialno='" + deviceSerialno + '\'' +
                    ", kernelVersion='" + kernelVersion + '\'' +
                    ", callState='" + callState + '\'' +
                    ", operatorName='" + operatorName + '\'' +
                    ", resolution='" + resolution + '\'' +
                    ", deviceName='" + deviceName + '\'' +
                    ", deviceGpuRenderer='" + deviceGpuRenderer + '\'' +
                    ", deviceRomTotalSize='" + deviceRomTotalSize + '\'' +
                    ", sdkVersion='" + sdkVersion + '\'' +
                    ", deviceGpuVendor='" + deviceGpuVendor + '\'' +
                    ", deviceImei='" + deviceImei + '\'' +
                    ", deviceHardware='" + deviceHardware + '\'' +
                    ", deviceGpuVersion='" + deviceGpuVersion + '\'' +
                    ", createTime=" + createTime +
                    ", sysSerialNo='" + sysSerialNo + '\'' +
                    ", operatorCode='" + operatorCode + '\'' +
                    ", productSerialNo='" + productSerialNo + '\'' +
                    ", deviceId='" + deviceId + '\'' +
                    ", sn='" + sn + '\'' +
                    ", mobileId=" + mobileId +
                    ", brandName='" + brandName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PhoneModelInfoEntity{" +
                "status=" + status +
                ", retCode=" + retCode +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
