package com.cloud.control.expand.service.home.list;

import com.cloud.control.expand.service.entity.ExpandServiceListEntity;

/**
 * Author：abin
 * Date：2020/10/15
 * Description：
 */
public interface IExpandServiceExpired {
    /**
     * 查看服务是否到期
     *
     * @param dataBean
     */
    void lookExpiredStatus(ExpandServiceListEntity.DataBean dataBean);
}
