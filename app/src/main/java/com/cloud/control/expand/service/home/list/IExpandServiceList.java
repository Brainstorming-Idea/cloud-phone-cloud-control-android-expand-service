package com.cloud.control.expand.service.home.list;

/**
 * Author：abin
 * Date：2020/10/17
 * Description：
 */
public interface IExpandServiceList {

    /**
     * 查看扩展是否可使用
     *
     * @param position
     */
    void examineServiceStatus(int position);

}
