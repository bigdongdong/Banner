package com.cxd.banner_demo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cxd.banner.*;
import com.cxd.banner_demo.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout parentLayout ;
    private List<Integer> banners ;

    private Banner banner ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parentLayout = findViewById(R.id.parentLayout);

        banners = new ArrayList<>();
        banners.add(R.mipmap.beauty);
        banners.add(R.mipmap.beauty_1);
        banners.add(R.mipmap.beauty_2);
        banners.add(R.mipmap.beauty_3);
        banners.add(R.mipmap.beauty_4);
        banners.add(R.mipmap.beauty_5);
        banners.add(R.mipmap.beauty_6);
//        banners.add(0);
//        banners.add(1);
//        banners.add(2);
//        banners.add(3);
//        banners.add(4);
//        banners.add(5);
//        banners.add(6);
//        banners.add(7);

        /**
         * 设置banner底部指示点 ... 的样式
         */
        PointsOptions options = new PointsOptions.Builder()
                .count(banners.size())              //点的数量
                .marginBottom(30)                   //点距离底部的距离（px）
                .selectedColor(Color.WHITE)         //当前广告页对应的点的颜色
                .unSelectedColor(Color.GRAY)        //非当前广告页对应点的颜色
                .space(10)                          //点与点之间的间隔（px）
                .width(15)                          //点的宽高尺寸（px）,点为圆形
                .build();

        /**
         * 创建banner
         */
        banner = new Banner.Builder()
                .context(this)
                .banners(banners)             //设置banner列表数据，List<? extends Object>
                .layoutStyle(Banner.LAYOUT_STYLE_IMAGEVIEW)                //提供一个ImageView
//                .layoutStyle(Banner.LAYOUT_STYLE_RELATIVELAYOUT)         //提供RelativeLayout，可以高度自定义布局
                .playStyle(Banner.PLAY_STYLE_JUST_GO)                      //无线循环播放
//                .playStyle(Banner.PLAY_STYLE_JUST_ONCE)                  //播放一次，播放完停留在最后一张
                .stayDuration(800)                                         //每张广告页停留时间（毫秒）
                .animDuration(500)                                         //广告切换的时间（毫秒）
                .pointsOptions(options)    //设置底部的指示点
                .isDisplayPoints(true)
                .isNeedBottomCover(false)
                /**
                 * 设置广告页监听
                 *    第一个泛型对应layoutStyle:
                 *      （Banner.LAYOUT_STYLE_IMAGEVIEW 对应 ImageView）
                 *      （Banner.LAYOUT_STYLE_RELATIVELAYOUT 对应 RelativeLayout）
                 *    第二个泛型是banners传的list中的泛型
                 */
                .onSelectedListener(new OnSelectedListener<ImageView,Integer>() {
                    @Override
                    public void onSelectedListener(ImageView iv, Integer integer, final int position) {
                        Log.i("aaa", "onSelectedListener:  "+integer +"  "+position);
                        //可以使用任意框架加载布局
                        Glide.with(MainActivity.this).load(integer).into(iv);
//                        iv.setImageResource(integer);
                        //给view设置交互监听
                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //点击事件
                                Toast.makeText(MainActivity.this,position+"",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .build();

        parentLayout.addView(banner); //将banner add进准备好的父布局
    }

    /**
     * 在onStart中开启轮播，在onDestory中停止轮播，避免内存泄漏
     */

    @Override
    protected void onStart() {
        super.onStart();
        if(banner != null){
            banner.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(banner != null){
            banner.stop();
        }
    }
}

