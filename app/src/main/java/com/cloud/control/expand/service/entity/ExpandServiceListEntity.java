package com.cloud.control.expand.service.entity;

import java.util.List;

/**
 * Author：abin
 * Date：2020/9/27
 * Description：扩展服务列表实体类
 */
public class ExpandServiceListEntity {

    /**
     * status : 0
     * msg : 获取成功
     * data : [{"serviceId":16,"serviceName":"IP代理","serviceImage":"null","serviceExplain":"IP代理扩展服务","insertTime":"2020-09-02T02:31:00.000+0000","updateTime":"2020-09-02T02:31:00.000+0000","shelves":1,"only":"001","sortId":1,"link":"null","base64":"base64码"},{"serviceId":17,"serviceName":"虚拟定位","serviceImage":"null","serviceExplain":"虚拟定位扩展服务","insertTime":"2020-09-02T02:31:00.000+0000","updateTime":"2020-09-02T02:31:00.000+0000","shelves":1,"only":"002","sortId":2,"link":"null","base64":"base64码"},{"serviceId":18,"serviceName":"一键新机","serviceImage":null,"serviceExplain":"一键新机扩展服务","insertTime":"2020-09-02T02:31:01.000+0000","updateTime":"2020-09-02T02:31:01.000+0000","shelves":1,"only":"003","sortId":3,"link":"null","base64":"base64码"}]
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
         * serviceId : 16
         * serviceName : IP代理
         * serviceImage : null
         * serviceExplain : IP代理扩展服务
         * insertTime : 2020-09-02T02:31:00.000+0000
         * updateTime : 2020-09-02T02:31:00.000+0000
         * shelves : 1
         * only : 001
         * sortId : 1
         * link : null
         * base64 : base64码
         */

        private int serviceId;
        private String serviceName;
        private String serviceImage;
        private String serviceExplain;
        private String insertTime;
        private String updateTime;
        private int shelves;
        private String only;
        private int sortId;
        private String link;
        private String base64;

        public int getServiceId() {
            return serviceId;
        }

        public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceImage() {
            return serviceImage;
        }

        public void setServiceImage(String serviceImage) {
            this.serviceImage = serviceImage;
        }

        public String getServiceExplain() {
            return serviceExplain;
        }

        public void setServiceExplain(String serviceExplain) {
            this.serviceExplain = serviceExplain;
        }

        public String getInsertTime() {
            return insertTime;
        }

        public void setInsertTime(String insertTime) {
            this.insertTime = insertTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getShelves() {
            return shelves;
        }

        public void setShelves(int shelves) {
            this.shelves = shelves;
        }

        public String getOnly() {
            return only;
        }

        public void setOnly(String only) {
            this.only = only;
        }

        public int getSortId() {
            return sortId;
        }

        public void setSortId(int sortId) {
            this.sortId = sortId;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getBase64() {
            return base64;
        }

        public void setBase64(String base64) {
            this.base64 = base64;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "serviceId=" + serviceId +
                    ", serviceName='" + serviceName + '\'' +
                    ", serviceImage='" + serviceImage + '\'' +
                    ", serviceExplain='" + serviceExplain + '\'' +
                    ", insertTime='" + insertTime + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    ", shelves=" + shelves +
                    ", only='" + only + '\'' +
                    ", sortId=" + sortId +
                    ", link='" + link + '\'' +
                    /*", base64='" + base64 + '\'' +*/
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ExpandServiceMainListEntity{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
