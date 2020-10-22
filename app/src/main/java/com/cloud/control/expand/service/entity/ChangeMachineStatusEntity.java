package com.cloud.control.expand.service.entity;

import java.util.List;

/**
 * Author：abin
 * Date：2020/10/17
 * Description： 是否改机
 */
public class ChangeMachineStatusEntity {

    /**
     * status : 0
     * msg : 成功
     * data : [{"id":7,"mobileType":"小米8","sn":"RK3930C2301900163"}]
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
         * id : 7
         * mobileType : 小米8
         * sn : RK3930C2301900163
         */

        private int id;
        private String mobileType;
        private String sn;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMobileType() {
            return mobileType;
        }

        public void setMobileType(String mobileType) {
            this.mobileType = mobileType;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }
    }

    @Override
    public String toString() {
        return "ChangeMachineStatusEntity{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
