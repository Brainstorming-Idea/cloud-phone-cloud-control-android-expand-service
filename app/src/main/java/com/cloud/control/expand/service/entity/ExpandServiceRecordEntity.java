package com.cloud.control.expand.service.entity;

import java.util.List;

/**
 * Author：abin
 * Date：2020/10/14
 * Description： 扩展服务记录实体类
 */
public class ExpandServiceRecordEntity {


    /**
     * status : 0
     * msg :
     * data : [{"typeId":1,"typeName":"IP代理","dueTimeStr":"2020-11-30T07:49:35.000+0000","mobileName":"rsiZ30GO","status":1,"currentTime":"2020-11-10T09:00:44.380+0000","serviceImgUrl":"group1/M00/00/9D/rBABDF-fe0mAQL1oAACSH2XZjmw246.png","serviceExplain":"IP"},{"typeId":2,"typeName":"虚拟定位","dueTimeStr":"2020-11-06T07:49:35.000+0000","mobileName":"rsiZ30GO","status":1,"currentTime":"2020-11-10T09:00:44.382+0000","serviceImgUrl":"group1/M00/00/9D/rBABDF-fe1-AP-IYAABMJZu-x0k301.png","serviceExplain":"虚拟"},{"typeId":3,"typeName":"一键新机","dueTimeStr":null,"mobileName":"rsiZ30GO","status":2,"currentTime":"2020-11-10T09:00:44.387+0000","serviceImgUrl":"group1/M00/00/9D/rBABDV-fe5OAe1Q4AAB-MnF2D-g837.png","serviceExplain":"一键新机"}]
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
         * typeId : 1
         * typeName : IP代理
         * dueTimeStr : 2020-11-30T07:49:35.000+0000
         * mobileName : rsiZ30GO
         * status : 1
         * currentTime : 2020-11-10T09:00:44.380+0000
         * serviceImgUrl : group1/M00/00/9D/rBABDF-fe0mAQL1oAACSH2XZjmw246.png
         * serviceExplain : IP
         */

        private int typeId;
        private String typeName;
        private String dueTimeStr;
        private String mobileName;
        private int status;
        private String currentTime;
        private String serviceImgUrl;
        private String serviceExplain;

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getDueTimeStr() {
            return dueTimeStr;
        }

        public void setDueTimeStr(String dueTimeStr) {
            this.dueTimeStr = dueTimeStr;
        }

        public String getMobileName() {
            return mobileName;
        }

        public void setMobileName(String mobileName) {
            this.mobileName = mobileName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }

        public String getServiceImgUrl() {
            return serviceImgUrl;
        }

        public void setServiceImgUrl(String serviceImgUrl) {
            this.serviceImgUrl = serviceImgUrl;
        }

        public String getServiceExplain() {
            return serviceExplain;
        }

        public void setServiceExplain(String serviceExplain) {
            this.serviceExplain = serviceExplain;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "typeId=" + typeId +
                    ", typeName='" + typeName + '\'' +
                    ", dueTimeStr='" + dueTimeStr + '\'' +
                    ", mobileName='" + mobileName + '\'' +
                    ", status=" + status +
                    ", currentTime='" + currentTime + '\'' +
                    ", serviceImgUrl='" + serviceImgUrl + '\'' +
                    ", serviceExplain='" + serviceExplain + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ExpandServiceRecordEntity{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
