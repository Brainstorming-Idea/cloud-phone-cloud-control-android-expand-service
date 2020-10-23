package com.cloud.control.expand.service.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.dialog.LoadDialog;
import com.cloud.control.expand.service.log.KLog;
import com.cloud.control.expand.service.widget.EmptyLayout;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author：abin
 * Date：2020/9/27
 * Description：基础的Activity
 */
public abstract class BaseActivity<T extends IBasePresenter> extends RxAppCompatActivity implements IBaseView, EmptyLayout.OnRetryListener {

    /**
     * 把 EmptyLayout 放在基类统一处理，@Nullable 表明 View 可以为 null，详细可看 ButterKnife
     */
    @Nullable
    @BindView(R.id.empty_layout)
    protected EmptyLayout mEmptyLayout;

    /**
     * 把 Presenter 提取到基类需要配合基类的 initInjector() 进行注入，如果继承这个基类则必定要提供一个 Presenter 注入方法，
     * 该APP所有 Presenter 都是在 Module 提供注入实现，也可以选择提供另外不带 Presenter 的基类
     */
    @Inject
    protected T mPresenter;

    /**
     * 全局的toast
     */
    private Toast toast;

    /**
     * 只要继承基类BaseActivity的子类都可以调用使用
     */
    public Context mContext;
    /**
     * 加载框
     */
    private LoadDialog mLoadDialog;
    /**
     * 最后一次dialog显示时间
     */
    private long lastDialogShowTime;

    /**
     * 弱引用
     */
    protected WeakReference<BaseActivity<T>> weakReference;

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    @LayoutRes
    protected abstract int attachLayoutRes();

    /**
     * Dagger 注入
     */
    protected abstract void initInjector();

    /**
     * 初始化视图控件
     */
    protected abstract void initViews();

    /**
     * 更新视图控件
     */
    protected abstract void updateViews(boolean isRefresh);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weakReference = new WeakReference<>(this);
        mContext = weakReference.get();
        setContentView(attachLayoutRes());
        ButterKnife.bind(this);
        initInjector();
        initViews();
        updateViews(false);
    }

    @Override
    public void showLoading() {
        showProgressDialog();
        //隐藏无网络、无数据界面，重新加载异常界面未隐藏
        if (mEmptyLayout != null) {
            mEmptyLayout.hide();
        }
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    @Override
    public void showNetError() {
        if (mEmptyLayout != null) {
            mEmptyLayout.setEmptyIcon(R.drawable.ic_not_network);
            mEmptyLayout.setEmptyMessage("无法连接到网络");
            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_NO_NET);
            mEmptyLayout.setRetryListener(this);
        }
    }

    @Override
    public void showNoData(String message) {
        if (mEmptyLayout != null) {
            mEmptyLayout.setEmptyIcon(R.drawable.ic_no_expand_service);
            mEmptyLayout.setEmptyMessage(message);
            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_NO_DATA);
            mEmptyLayout.setRetryListener(this);
        }
    }

    @Override
    public void onRetry() {
        updateViews(false);
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    /**
     * 初始化 Toolbar
     *
     * @param toolbar
     * @param homeAsUpEnabled
     * @param title
     */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setToolbarTitleCenter(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    /***
     * 设置Toolbar标题居中
     */
    public void setToolbarTitleCenter(Toolbar toolbar) {
        String title = "title";
        final CharSequence originalTitle = toolbar.getTitle();
        toolbar.setTitle(title);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (title.equals(textView.getText())) {
                    textView.setGravity(Gravity.CENTER);
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER;
                    textView.setLayoutParams(params);
                }
            }
            toolbar.setTitle(originalTitle);
        }
    }

    /**
     * 全局的toast样式
     *
     * @param message
     */
    public void toastMessage(String message) {
        try {
            //LayoutInflater的作用：对于一个没有被载入或者想要动态载入的界面，都需要LayoutInflater.inflate()来载入，LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
            LayoutInflater inflater = getLayoutInflater();//调用Activity的getLayoutInflater()
            View view = inflater.inflate(R.layout.layout_common_toast, null); //加載layout下的布局
            TextView toastContent = view.findViewById(R.id.tv_toast_content);
            toastContent.setText(message); //toast的标题
            if (toast == null) {
                toast = new Toast(this);
            }
            toast.setGravity(Gravity.CENTER, 0, 0);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
            toast.setDuration(Toast.LENGTH_SHORT);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
            toast.setView(view); //添加视图文件
            toast.show();
        } catch (Exception e) {

        }
    }

    /**
     * 显示加载框
     */
    public void showProgressDialog() {
        if (mLoadDialog == null) {
            mLoadDialog = new LoadDialog(this);
        }
        try {
            if (!mLoadDialog.isShowing()) {
                lastDialogShowTime = SystemClock.currentThreadTimeMillis();
                try {
                    mLoadDialog.show();
                } catch (Exception e) {
                    KLog.d("mLoadDialog.show Exception == " + e.toString());
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 关闭加载框
     */
    public void hideProgressDialog() {
        try {
            if (mLoadDialog != null && mLoadDialog.isShowing()) {
                //防止dialog消失过快造成视觉卡顿效果，最小要200mms后消息，如果delayMillis<0,其实和0是一样的效果
                long current = SystemClock.currentThreadTimeMillis();
                if (lastDialogShowTime != 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!isDestroyed()) {
                                mLoadDialog.dismiss();
                            }
                        }
                    }, 200 - (current - lastDialogShowTime));
                }
            } else {
                if (mLoadDialog != null) {
                    mLoadDialog.dismiss();
                }
            }
        } catch (Exception e) {

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toast = null;
        mLoadDialog = null;
    }

}
