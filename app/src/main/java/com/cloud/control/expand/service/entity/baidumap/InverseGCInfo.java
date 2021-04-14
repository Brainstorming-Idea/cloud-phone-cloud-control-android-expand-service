package com.cloud.control.expand.service.entity.baidumap;

/**
 * @author wangyou
 * @desc: 逆地理编码
 * @date :2021/4/7
 */
public class InverseGCInfo {
    private Integer status;
    public Result result;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result{
        private Location location;
        private String formatted_address;//结构化地址信息
        private String business;//坐标所在商圈信息，如 "人民大学,中关村,苏州街"。最多返回3个
        private AddressComponent addressComponent;//

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public AddressComponent getAddressComponent() {
            return addressComponent;
        }

        public void setAddressComponent(AddressComponent addressComponent) {
            this.addressComponent = addressComponent;
        }
    }
}