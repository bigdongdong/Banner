package com.cxd.moudle;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Banner extends RelativeLayout {
    private final String TAG = "Banner_TAG";

    public static int PLAY_STYLE_JUST_GO = 1;//无限顺延
    public static int PLAY_STYLE_JUST_ONCE = 2;//只播放一遍，播放完停留在最后一个
    //    public static int PLAY_STYLE_GO_AND_BACK = 3;//往返

    public static int LAYOUT_STYLE_IMAGEVIEW = 1;//布局是imageView
    public static int LAYOUT_STYLE_RELATIVELAYOUT = 2 ; //布局是relativelayout

    private Activity context ;
    private Builder builder ;

    private ViewPager viewPager ;
    private BannerAdapter adapter ;
    private LayoutParams params ;

    private Timer timer ;
    private TimerTask timerTask ;

    private CountDownTimer recoveryCDT ;//恢复自动播放的线程
    private int currentPosition = 0 ;

    private Handler handler ;
    private int what = 1; //动态控制该值来实现自动与停播的功能

    private boolean isRunning = false ;
    private Banner(Activity context , final Builder builder) {
        super(context);

        this.context = context ;
        this.builder = builder ;

        /* 初始化viewpager */
        params = new LayoutParams(-1,-1);
        viewPager = new ViewPager(context);
        viewPager.setLayoutParams(params);
        this.setLayoutParams(params);

        /* 设置viewpager切换动画速率 */
        try {
            Class aClass = ViewPager.class;
            Field sInterpolator = aClass.getDeclaredField("sInterpolator");
            sInterpolator.setAccessible(true);
            Scroller scroller = new Scroller(context, (Interpolator) sInterpolator.get(viewPager)) {
                public void startScroll(int startX, int startY, int dx, int dy, int duration) {
                    //最后一个参数即viewpager自动滑动的时间
                    super.startScroll(startX, startY, dx, dy, builder.animDuration);
                }
            };
            Field mScroller = aClass.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, scroller);
        } catch (Exception e) {
            new RuntimeException("给viewpager设置动画时间时出错："+e.getMessage());
        }

        /* 将viewpager添加入rootview */
        adapter = new BannerAdapter();
        viewPager.setAdapter(adapter);
        this.addView(viewPager);

       /*设置底部的点点点指示栏*/
        if(builder.isDisplayPoints == true && builder.banners.size()>1){
            /* 初始化pointsview */
            params = new LayoutParams(-2,-2);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.setMargins(0,0,0,builder.pointsOptions.marginBottom);
            final PointsView pv = new PointsView(context , builder.pointsOptions);
            pv.setLayoutParams(params);
            pv.setSelected(0);
            this.addView(pv);
            if(builder.isNeedBottomCover == true){
                //添加一个渐变层
                View view = new View(context);
                params = new LayoutParams(-1,2 * builder.pointsOptions.marginBottom
                        +builder.pointsOptions.width);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                view.setLayoutParams(params);
                GradientDrawable gd = new GradientDrawable();
                gd.setColors(new int[]{Color.parseColor("#00000000"),
                        Color.parseColor("#50111111")});
                view.setBackground(gd);
                this.addView(view);
            }
            /* 添加联动 以及position的更新 */
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    if(pv != null){
                        pv.setSelected(i % builder.banners.size());
                    }

                    currentPosition = i ;
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }else{
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    currentPosition = i ;
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }

        /* 设置触摸停止轮播，抬起启用恢复线程 */
        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP
                        || event.getAction() == MotionEvent.ACTION_CANCEL){
                    //启动恢复倒计时
                    recoveryCDT.start();
                }else{
                    what = -1 ; //不是抬起就不给滑动viewpager
                }
                return false;
            }
        });

        /* 设置自动播放的timer */
        setAutoPlayTimer();

        /* 设置手指触摸后，恢复滑动的倒计时线程 */
        setRecoveryTimer();

        /* 接受viewpager动作的handler */
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1){
                    viewPager.setCurrentItem(currentPosition++);
                }
            }
        };
    }

    /* 初始化轮播timer */
    private void setAutoPlayTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //发送handler，下滑viewpager
                handler.sendEmptyMessage(what);
            }
        };
    }

    /* 初始化恢复线程 */
    private void setRecoveryTimer(){
        recoveryCDT = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                what = 1 ;
            }
        };
    }

    /* 启动轮播 */
    public void start(){
        if(timer != null && isRunning == false){
            timer.schedule(timerTask,100,builder.stayDuration);
            Log.i(TAG, "start:stayDuration "+builder.stayDuration);
            isRunning = true ;
        }
    }

    /* 停止轮播 */
    public void stop(){
        if(timerTask != null){
            timerTask.cancel();
        }
        if(timer != null){
            timer.cancel();
        }
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
        }
    }

    public static class Builder{
        private Activity context ;
        private List<? extends Object> banners ;
        private OnSelectedListener onSelectedListener ;
        private int layoutStyle = LAYOUT_STYLE_IMAGEVIEW; //内容布局
        private int playStyle = Banner.PLAY_STYLE_JUST_GO;
        private boolean isDisplayPoints = true ;
        private int stayDuration ;//毫秒
        private int animDuration ;//毫秒
        private PointsOptions pointsOptions ; //设置点导航的样式
        private boolean isNeedBottomCover = true ; //是否需要底部渐变遮罩层，方便点显示更清晰

        public Builder context(Activity context){
            this.context = context ;
            return this;
        }
        public Builder banners(List<? extends Object> banners){
            this.banners = banners ;
            return this;
        }
        public Builder layoutStyle(int layoutStyle){
            this.layoutStyle = layoutStyle ;
            return this;
        }
        public Builder onSelectedListener(OnSelectedListener onSelectedListener){
            this.onSelectedListener = onSelectedListener ;
            return this;
        }
        public Builder playStyle(int playStyle){
            this.playStyle = playStyle ;
            return this;
        }
        public Builder isDisplayPoints(boolean isDisplayPoints){
            this.isDisplayPoints = isDisplayPoints ;
            return this;
        }
        public Builder animDuration(int animDuration){
            this.animDuration = animDuration ;
            return this;
        }
        public Builder stayDuration(int stayDuration){
            this.stayDuration = stayDuration ;
            return this;
        }
        public Builder pointsOptions(PointsOptions pointsOptions){
            this.pointsOptions = pointsOptions ;
            return this;
        }
        public Builder isNeedBottomCover(boolean isNeedBottomCover){
            this.isNeedBottomCover = isNeedBottomCover ;
            return this;
        }
        public Banner build(){
            return new Banner(context,this);
        }
    }

    private class BannerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if(builder.playStyle == PLAY_STYLE_JUST_ONCE){
                return builder.banners.size();
            }
            if(builder.playStyle == PLAY_STYLE_JUST_GO){
                return Integer.MAX_VALUE;
            }
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutParams rparams = new LayoutParams(-1,-1);

            //根据style返回对应view类型
            if(builder.layoutStyle == LAYOUT_STYLE_RELATIVELAYOUT){ //relativelayout
                RelativeLayout relativeLayout = new RelativeLayout(context);
                relativeLayout.setLayoutParams(rparams);
                int p = position % builder.banners.size();
                builder.onSelectedListener.onSelectedListener(relativeLayout,builder.banners.get(p),p);
                container.addView(relativeLayout);
                return relativeLayout;
            }else if(builder.layoutStyle == LAYOUT_STYLE_IMAGEVIEW){//imageview
                ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(rparams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                int p = position % builder.banners.size();
                builder.onSelectedListener.onSelectedListener(imageView,builder.banners.get(p),p);
                container.addView(imageView);
                return imageView;
            }

            return null ;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
