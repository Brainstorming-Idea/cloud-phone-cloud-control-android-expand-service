package com.cloud.control.expand.service.entity;

import java.util.List;

/**
 * Author：abin
 * Date：2020/10/17
 * Description： 品牌型号实体类
 */
public class PhoneBrandModelEntity {

    /**
     * status : 0
     * msg : 成功
     * data : [{"id":1,"brandName":"vivo","model":[{"id":15,"brandId":1,"mobileModel":"VIVO X7Plus","createTime":"2020-05-25T19:11:38","updateTime":null},{"id":16,"brandId":1,"mobileModel":"VIVO Z5x","createTime":"2020-05-25T19:11:40","updateTime":null},{"id":17,"brandId":1,"mobileModel":"VIVO Y85A","createTime":"2020-05-25T19:11:42","updateTime":null},{"id":18,"brandId":1,"mobileModel":"VIVO S5","createTime":"2020-05-25T19:11:44","updateTime":null}]},{"id":2,"brandName":"小米","model":[{"id":2,"brandId":2,"mobileModel":"Redmi Note 5","createTime":"2020-05-14T14:50:12","updateTime":null},{"id":6,"brandId":2,"mobileModel":"小米8","createTime":"2020-05-14T14:50:18","updateTime":null},{"id":10,"brandId":2,"mobileModel":"红米 8A","createTime":"2020-05-25T19:09:45","updateTime":null},{"id":11,"brandId":2,"mobileModel":"小米CC9","createTime":"2020-05-25T19:09:47","updateTime":null}]},{"id":3,"brandName":"oppo","model":[{"id":3,"brandId":3,"mobileModel":"OPPO A83","createTime":"2020-05-14T14:50:13","updateTime":null},{"id":12,"brandId":3,"mobileModel":"OPPO Reno2","createTime":"2020-05-25T19:11:32","updateTime":null},{"id":13,"brandId":3,"mobileModel":"OPPO K5","createTime":"2020-05-25T19:11:34","updateTime":null},{"id":14,"brandId":3,"mobileModel":"OPPO A79","createTime":"2020-05-25T19:11:36","updateTime":null}]},{"id":4,"brandName":"华为","model":[{"id":4,"brandId":4,"mobileModel":"荣耀10","createTime":"2020-05-14T14:50:15","updateTime":null},{"id":7,"brandId":4,"mobileModel":"荣耀20","createTime":"2020-05-25T19:08:48","updateTime":null},{"id":8,"brandId":4,"mobileModel":"Nova 6","createTime":"2020-05-25T19:08:54","updateTime":null},{"id":9,"brandId":4,"mobileModel":"麦芒5","createTime":"2020-05-25T19:08:52","updateTime":null}]},{"id":5,"brandName":"三星","model":[{"id":19,"brandId":5,"mobileModel":"Galaxy C7 Pro","createTime":"2020-05-25T19:11:45","updateTime":null},{"id":20,"brandId":5,"mobileModel":"Galaxy S10","createTime":"2020-05-25T19:11:47","updateTime":null},{"id":21,"brandId":5,"mobileModel":"Galaxy S9","createTime":"2020-05-25T19:11:49","updateTime":null},{"id":22,"brandId":5,"mobileModel":"Galaxy A60","createTime":"2020-05-25T19:11:50","updateTime":null}]}]
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
         * id : 1
         * brandName : vivo
         * model : [{"id":15,"brandId":1,"mobileModel":"VIVO X7Plus","createTime":"2020-05-25T19:11:38","updateTime":null},{"id":16,"brandId":1,"mobileModel":"VIVO Z5x","createTime":"2020-05-25T19:11:40","updateTime":null},{"id":17,"brandId":1,"mobileModel":"VIVO Y85A","createTime":"2020-05-25T19:11:42","updateTime":null},{"id":18,"brandId":1,"mobileModel":"VIVO S5","createTime":"2020-05-25T19:11:44","updateTime":null}]
         */

        private int id;
        private String brandName;
        private List<ModelBean> model;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public List<ModelBean> getModel() {
            return model;
        }

        public void setModel(List<ModelBean> model) {
            this.model = model;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", brandName='" + brandName + '\'' +
                    ", model=" + model +
                    '}';
        }

        public static class ModelBean {
            /**
             * id : 15
             * brandId : 1
             * mobileModel : VIVO X7Plus
             * createTime : 2020-05-25T19:11:38
             * updateTime : null
             */

            private int id;
            private int brandId;
            private String mobileModel;
            private String createTime;
            private Object updateTime;

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

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public Object getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(Object updateTime) {
                this.updateTime = updateTime;
            }

            @Override
            public String toString() {
                return "ModelBean{" +
                        "id=" + id +
                        ", brandId=" + brandId +
                        ", mobileModel='" + mobileModel + '\'' +
                        ", createTime='" + createTime + '\'' +
                        ", updateTime=" + updateTime +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "PhoneBrandModelEntity{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
