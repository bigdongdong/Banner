package com.cxd.banner;

import android.view.View;

/**
 *
 * @param <V>  RelativeLayout or ImageView
 * @param <T>
 */
public interface OnSelectedListener<V extends View,T>{

    /**
     *
     * @param view  如果isHighCustomizationRequired为true，则提供RelativeLayout，否则提供ImageView
     * @param t
     * @param position
     */
    void onSelectedListener(V view, T t, int position);
}