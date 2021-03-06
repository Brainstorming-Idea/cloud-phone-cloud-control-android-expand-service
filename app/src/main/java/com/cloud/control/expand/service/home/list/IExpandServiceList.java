package com.cloud.control.expand.service.home.list;

import com.cloud.control.expand.service.entity.ExpandServiceRecordEntity;

/**
 * Author：abin
 * Date：2020/10/17
 * Description：
 */
public interface IExpandServiceList {

    /**
     * 查看扩展是否可使用
     *
     * @param dataBean
     */
    void examineServiceStatus(ExpandServiceRecordEntity.DataBean dataBean);

}
