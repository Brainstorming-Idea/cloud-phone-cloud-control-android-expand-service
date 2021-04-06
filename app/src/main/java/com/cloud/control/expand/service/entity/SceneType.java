package com.cloud.control.expand.service.entity;

/**
 * @author wangyou
 * @desc:
 * @date :2021/3/4
 */
public enum SceneType {
    /*静坐；步行，跑步，驾驶*/
    SIT(0, "静坐"),WALK(1, "步行"),RUN(2, "跑步"),DRIVE(3, "驾驶");
    private int value;
    private String desc;

    SceneType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static SceneType getVirtualScene(int value){
        for (SceneType sceneType : SceneType.values()){
            if (value == sceneType.getValue()){
                return sceneType;
            }
        }
        return null;
    }
}