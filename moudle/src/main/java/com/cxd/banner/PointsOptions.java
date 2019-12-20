package com.cxd.banner;

/**
 * 指示点布局的参数
 */
public class PointsOptions {
    public int count ;
    public int space ;
    public int unSelectedColor ;
    public int selectedColor ;
    public int width ;
    public int marginBottom ;

    private PointsOptions(Builder builder) {
        this.count = builder.count ;
        this.space = builder.space ;
        this.unSelectedColor = builder.unSelectedColor ;
        this.selectedColor = builder.selectedColor ;
        this.width = builder.width ;
        this.marginBottom = builder.marginBottom ;
    }

    public static class Builder{
        private int count ;
        private int space ;
        private int unSelectedColor ;
        private int selectedColor ;
        private int width ;
        private int marginBottom ;

        public Builder count(int count){
            this.count = count ;
            return this ;
        }
        public Builder space(int space){
            this.space = space ;
            return this ;
        }
        public Builder unSelectedColor(int unSelectedColor){
            this.unSelectedColor = unSelectedColor ;
            return this ;
        }
        public Builder selectedColor(int selectedColor){
            this.selectedColor = selectedColor ;
            return this ;
        }
        public Builder width(int width){
            this.width = width ;
            return this ;
        }
        public Builder marginBottom(int marginBottom){
            this.marginBottom = marginBottom ;
            return this ;
        }

        public PointsOptions build(){
            return new PointsOptions(this);
        }
    }
}
