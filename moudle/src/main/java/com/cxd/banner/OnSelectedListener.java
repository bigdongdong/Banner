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
     * @param position 元素在list中的位置
     */
    void onSelectedListener(V view, T t,int position);


    /**
     * viewpager滚动到当前的pager
     * @param position
     */
    void onPageSelected(int position) ;
}