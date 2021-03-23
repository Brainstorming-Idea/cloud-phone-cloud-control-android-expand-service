package com.cloud.control.expand.service.home.list;

import com.cloud.control.expand.service.base.IBaseView;
import com.cloud.control.expand.service.entity.ExpandServiceRecordEntity;

import java.util.List;

/**
 * Author：abin
 * Date：2020/9/27
 * Description：扩展服务列表接口
 */
public interface ExpandServiceListView extends IBaseView {
    /**
     * 显示数据
     *
     * @param listEntity
     */
    void loadData(List<ExpandServiceRecordEntity.DataBean> listEntity);

    /**
     * 隐藏列表视图
     */
    void hideListView();

    /**
     * 跳转界面
     *
     * @param dataBean
     */
    void jumpPage(ExpandServiceRecordEntity.DataBean dataBean);

    /**
     * 获取root按钮状态
     *
     * @param state
     */
    void getRootState(boolean state);

}
