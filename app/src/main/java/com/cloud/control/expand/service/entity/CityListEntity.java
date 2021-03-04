package com.cloud.control.expand.service.entity;

import java.util.List;

/**
 * Author：abin
 * Date：2020/10/15
 * Description： 城市列表实体类
 */
public class CityListEntity {


    /**
     * status : 0
     * retCode : null
     * msg : 获取城市列表成功
     * data : {"selectedCity":["包头"],"longCityList":["合肥","淮安","吉安","荆门","连云港","厦门"],"cityList":["白山","包头","滨州","池州","福州","广州","黄山","江门","南京","莆田","荣昌县","三明","泰州","唐山","铜陵","温州","新余","宣城","烟台","漳州"]}
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
        private List<String> selectedCity;
        private List<String> longCityList;
        private List<String> cityList;

        public List<String> getSelectedCity() {
            return selectedCity;
        }

        public void setSelectedCity(List<String> selectedCity) {
            this.selectedCity = selectedCity;
        }

        public List<String> getLongCityList() {
            return longCityList;
        }

        public void setLongCityList(List<String> longCityList) {
            this.longCityList = longCityList;
        }

        public List<String> getCityList() {
            return cityList;
        }

        public void setCityList(List<String> cityList) {
            this.cityList = cityList;
        }
    }
}
