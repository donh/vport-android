package com.littlesparkle.growler.core.ui.mvp;

import android.support.annotation.UiThread;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V extends BaseView> {
    private WeakReference<V> mViewRef;

    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
        onStart();
    }

    @UiThread
    public V getViewCanBeNull() {
        return mViewRef == null ? null : mViewRef.get();
    }

    @UiThread
    public V getView() {
        if (mViewRef == null) {
            throw new ViewIsNullException();
        } else {
            return mViewRef.get();
        }
    }

    @UiThread
    public abstract void onStart();

    @UiThread
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
