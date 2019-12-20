package com.cxd.banner;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

//底部指示点
public class PointsView extends LinearLayout {
    private  PointsOptions options ;
    private Context context ;
    private List<ImageView> views ;
    private ImageView lastIV ;

    public PointsView(Context context , PointsOptions options) {
        super(context);
        this.context = context ;
        this.options = options ;

        views = new ArrayList<>();

        this.setLayoutParams(new LayoutParams(-2,-2));
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER_HORIZONTAL);

        for(int i = 0 ; i < options.count ; i++){
            ImageView iv = generatePointView() ;
            views.add(iv);
            this.addView(iv);
        }
    }

    private ImageView generatePointView(){
        ImageView iv = new ImageView(context);
        LayoutParams params = new LayoutParams(options.width,options.width);
        params.setMargins(options.space/2,0,options.space/2,0);
        iv.setLayoutParams(params);

        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(options.width);
        gd.setColor(options.unSelectedColor);
        iv.setBackground(gd);

        return iv ;
    }

    public void setSelected(int position){
        ImageView iv = views.get(position);
        GradientDrawable gd ;

        if(lastIV == null){
            lastIV = iv ;
            gd = new GradientDrawable();
            gd.setCornerRadius(options.width);
            gd.setColor(options.selectedColor);
            iv.setBackground(gd);
            return;
        }else{
            gd = new GradientDrawable();
            gd.setCornerRadius(options.width);
            gd.setColor(options.unSelectedColor);
            lastIV.setBackground(gd);

            gd = new GradientDrawable();
            gd.setCornerRadius(options.width);
            gd.setColor(options.selectedColor);
            iv.setBackground(gd);
            lastIV = iv ;
        }
    }
}
