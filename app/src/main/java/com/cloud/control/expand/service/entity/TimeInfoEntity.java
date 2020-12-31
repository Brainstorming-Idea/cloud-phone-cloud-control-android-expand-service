package com.cloud.control.expand.service.entity;

/**
 * Author：wangwenbin
 * Date：2020/12/30
 * Description：
 */
public class TimeInfoEntity {

    /**
     * status : 0
     * msg : 获取成功
     * data : {"zone":"Asia/Shanghai","timezone":5400,"time":1609125092340}
     * retCode : null
     */

    private int status;
    private String msg;
    private DataBean data;
    private Object retCode;

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

    public Object getRetCode() {
        return retCode;
    }

    public void setRetCode(Object retCode) {
        this.retCode = retCode;
    }

    public static class DataBean {
        /**
         * zone : Asia/Shanghai
         * timezone : 5400
         * time : 1609125092340
         */

        private String zone;
        private int timezone;
        private long time;

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public int getTimezone() {
            return timezone;
        }

        public void setTimezone(int timezone) {
            this.timezone = timezone;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
