# Banner
###  自动轮播广告控件，支持交互和自定义界面，可自动轮播，手指触摸停止，手指拿开恢复轮播

<br>
<img  width = "700" src = "https://github.com/bigdongdong/Banner/blob/master/gif/preview.gif"></img>

# 项目配置

```
  allprojects {
      repositories {
          ...
          maven { url 'https://jitpack.io' }  //添加jitpack仓库
      }
  }
  
  dependencies {
	  implementation 'com.github.bigdongdong:Banner:4.1' //添加依赖
  }
```

# 使用方式
```java
private List<String> banners ;


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
//        .layoutStyle(Banner.LAYOUT_STYLE_RELATIVELAYOUT)         //提供RelativeLayout，可以高度自定义布局
        .playStyle(Banner.PLAY_STYLE_JUST_GO)                      //无限循环播放
//        .playStyle(Banner.PLAY_STYLE_JUST_ONCE)                  //播放一次，播放完停留在最后一张
        .stayDuration(800)                                         //每张广告页停留时间（毫秒）
        .animDuration(500)                                         //广告切换的时间（毫秒）
        .isDisplayPoints(true)                                     //是否显示底部指示点，默认显示
        .pointsOptions(options)                                    //设置底部的指示点
	.isNeedBottomCover(false)				   //是否需要底部遮罩层，默认true
        /**
         * 设置广告页监听
         *    第一个泛型对应layoutStyle:
         *      （Banner.LAYOUT_STYLE_IMAGEVIEW 对应 ImageView）
         *      （Banner.LAYOUT_STYLE_RELATIVELAYOUT 对应 RelativeLayout）
         *    第二个泛型是banners传的list中的泛型
         */
        .onSelectedListener(new OnSelectedListener<ImageView,String>() {
            @Override
            public void onSelectedListener(ImageView iv, String url, int position) {
                //可以使用任意框架加载布局
                Glide.with(MainActivity.this).load(url).into(iv);
                //给view设置交互监听
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        })
        .build();

parentLayout.addView(banner); //将banner add进准备好的父布局
        
        
/**
 * 在onStart中开启轮播，在onStop中停止轮播
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
```


