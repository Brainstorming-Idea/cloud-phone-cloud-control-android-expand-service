package com.cloud.control.expand.service.entity;

/**
 * Author：wangwenbin
 * Date：2021/3/23
 * Description：
 */
public class RootStateEntity {

    /**
     * status : 0
     * retCode : null
     * msg : 成功
     * data : {"msg":"success","data":{"isOpen":false},"sn":"RK3930C2301900143","type":"getRoot","status":0}
     */

    private int status;
    private Object retCode;
    private String msg;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * msg : success
         * data : {"isOpen":false}
         * sn : RK3930C2301900143
         * type : getRoot
         * status : 0
         */

        private String msg;
        private DataBean data;
        private String sn;
        private String type;
        private int status;

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

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class DataBean {
            /**
             * isOpen : false
             */

            private boolean isOpen;

            public boolean isIsOpen() {
                return isOpen;
            }

            public void setIsOpen(boolean isOpen) {
                this.isOpen = isOpen;
            }

            @Override
            public String toString() {
                return "DataBean{" +
                        "isOpen=" + isOpen +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBeanX{" +
                    "msg='" + msg + '\'' +
                    ", data=" + data +
                    ", sn='" + sn + '\'' +
                    ", type='" + type + '\'' +
                    ", status=" + status +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RootStateEntity{" +
                "status=" + status +
                ", retCode=" + retCode +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
