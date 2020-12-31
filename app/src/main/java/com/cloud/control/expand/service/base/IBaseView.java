package com.cloud.control.expand.service.base;

import com.trello.rxlifecycle.LifecycleTransformer;

/**
 * Author：abin
 * Date：2020/9/27
 * Description：基础 BaseView 接口
 */
public interface IBaseView {

    /**
     * 显示加载动画
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示网络错误，modify 对网络异常在 BaseActivity 和 BaseFragment 统一处理
     */
    void showNetError();

    /**
     * 显示没有数据
     */
    void showNoData(String message);

    /**
     * 提示消息
     *
     * @param message
     */
    void toast(String message);

    /**
     * 弹框提示
     */
    void dialog(String title, String content, String leftStr, String rightStr);

    /**
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */
    <T> LifecycleTransformer<T> bindToLife();

}
