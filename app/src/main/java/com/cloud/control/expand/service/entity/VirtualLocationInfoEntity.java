package com.cloud.control.expand.service.entity;

/**
 * Author：abin
 * Date：2020/10/18
 * Description： 后台返回的位置信息
 */
public class VirtualLocationInfoEntity {

    /**
     * status : 0
     * msg : 成功
     * data : {"sn":"RK3930C2301900165","longitude":"114.02881457340357","latitude":"22.54210680289081","ip":null,"city":null,"isFirst":null}
     */

    private int status;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sn : RK3930C2301900165
         * longitude : 114.02881457340357
         * latitude : 22.54210680289081
         * ip : null
         * city : null
         * isFirst : null
         */

        private String sn;
        private String longitude;
        private String latitude;
        private Object ip;
        private Object city;
        private Object isFirst;

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public Object getIp() {
            return ip;
        }

        public void setIp(Object ip) {
            this.ip = ip;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getIsFirst() {
            return isFirst;
        }

        public void setIsFirst(Object isFirst) {
            this.isFirst = isFirst;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "sn='" + sn + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", ip=" + ip +
                    ", city=" + city +
                    ", isFirst=" + isFirst +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "VirtualLocationInfoEntity{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
