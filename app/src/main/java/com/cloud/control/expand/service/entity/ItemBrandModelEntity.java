package com.cloud.control.expand.service.entity;

/**
 * Author：abin
 * Date：2020/10/17
 * Description： 机型数据
 */
public class ItemBrandModelEntity {
    private String data;
    private int type; //弹框类型  1 品牌  2机型
    private boolean check; //是否选中

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "ItemBrandModelEntity{" +
                "data='" + data + '\'' +
                ", type=" + type +
                ", check=" + check +
                '}';
    }
}
