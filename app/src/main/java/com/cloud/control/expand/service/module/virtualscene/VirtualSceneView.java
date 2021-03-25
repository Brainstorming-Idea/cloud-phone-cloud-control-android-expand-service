package com.cloud.control.expand.service.module.virtualscene;

import com.cloud.control.expand.service.base.IBaseView;

import io.reactivex.disposables.Disposable;

/**
 * @author wangyou
 * @desc: P -> V
 * @date :2021/3/3
 */
public interface VirtualSceneView extends IBaseView {

    /**
     * 加载虚拟中心位置描述
     * @param loc
     */
    void loadCenterLocDes(String loc);

    /**
     * 设置当前城市
     * @param city
     */
    void setCenterCity(String city);

    /**
     * 中心坐标
     * @param cCoord
     */
    void loadCenterLocation(double[] cCoord);

    /**
     * 更新启动状态
     */
    void updateStatus();

    void addDisposable(Disposable disposable);
}
