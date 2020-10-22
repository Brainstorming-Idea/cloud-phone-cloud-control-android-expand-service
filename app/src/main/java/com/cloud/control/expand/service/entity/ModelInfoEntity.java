package com.cloud.control.expand.service.entity;

/**
 * Author：abin
 * Date：2020/10/17
 * Description： 手机型号对应品牌信息
 */
public class ModelInfoEntity {
    private String brand;
    private String model;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "ModelInfoEntity{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}
