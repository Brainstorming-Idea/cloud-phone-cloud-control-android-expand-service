package com.cloud.control.expand.service.module.virtuallocation;

import com.cloud.control.expand.service.base.IBaseView;
import com.cloud.control.expand.service.entity.VirtualLocationInfoEntity;

/**
 * Author：abin
 * Date：2020/10/12
 * Description：虚拟定位接口
 */
public interface VirtualLocationView extends IBaseView {

    /**
     * 显示数据
     *
     * @param virtualLocationInfoEntity
     */
    void loadData(VirtualLocationInfoEntity virtualLocationInfoEntity);

    /**
     *
     * @param title
     * @param content
     * @param leftStr
     * @param rightStr
     * @param isBack 是否返回上一级界面
     */
    void showDialog(String title, String content, String leftStr, String rightStr, boolean isBack);
}
