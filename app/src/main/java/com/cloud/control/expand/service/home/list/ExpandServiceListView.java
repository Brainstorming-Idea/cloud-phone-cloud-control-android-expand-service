package com.cloud.control.expand.service.home.list;

import com.cloud.control.expand.service.base.IBaseView;
import com.cloud.control.expand.service.entity.ExpandServiceListEntity;
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
    void loadData(List<ExpandServiceListEntity.DataBean> listEntity);

    /**
     * 跳转页面
     *
     * @param dataBean
     * @param recordEntity
     */
    void jumpPage(ExpandServiceListEntity.DataBean dataBean, ExpandServiceRecordEntity recordEntity);

}
