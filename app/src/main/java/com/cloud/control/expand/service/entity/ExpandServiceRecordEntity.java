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
     * data : [{"typeId":1,"typeName":"IP代理","dueTimeStr":"2020-10-25T06:48:13.000+0000","mobileName":"mxw0tJvF","status":1},{"typeId":2,"typeName":"虚拟定位","dueTimeStr":"2020-10-16T06:48:13.000+0000","mobileName":"mxw0tJvF","status":1},{"typeId":3,"typeName":"一键新机","dueTimeStr":null,"mobileName":"mxw0tJvF","status":2}]
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
         * dueTimeStr : 2020-10-25T06:48:13.000+0000
         * mobileName : mxw0tJvF
         * status : 1
         */

        private int typeId;
        private String typeName;
        private String dueTimeStr;
        private String mobileName;
        private int status;

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

        @Override
        public String toString() {
            return "DataBean{" +
                    "typeId=" + typeId +
                    ", typeName='" + typeName + '\'' +
                    ", dueTimeStr='" + dueTimeStr + '\'' +
                    ", mobileName='" + mobileName + '\'' +
                    ", status=" + status +
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
