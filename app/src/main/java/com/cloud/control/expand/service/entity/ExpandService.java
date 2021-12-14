package com.cloud.control.expand.service.entity;

/**
 * @author wangyou
 * @desc: 扩展服务枚举类
 * @date :2021/2/26
 */
public enum ExpandService {
    /*1-IP代理；2-虚拟定位；3-一键新机；4-虚拟行为场景；5-多窗口推流；6-文字识别；7-日志调试；8-ROOT模式；9-多路直播；10-虚拟相机；11-克隆；12-高级ADB；13-备份还原；14-同屏互动*/
    IP_PROXY(1),VIRTUAL_LOCATION(2),CHANGE_MACHINE(3),VIRTUAL_SCENE(4),MUL_WINDOW(5),OCR(6),LOG_DEBUG(7),ROOT_PATTERN(8),MULTI_CHANNEL_LIVE(9),VIRTUAL_CAMERA(10),CLONED(11),SENIOR_ADB(12),BACKUP_RESTORE(13),SCREEN_INTERACTION(14);

    private int typeId;

    ExpandService(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
    public static ExpandService getExpandService(int typeId){
        for (ExpandService service : ExpandService.values()){
            if (service.getTypeId() == typeId){
                return service;
            }
        }
        return null;
    }
}
