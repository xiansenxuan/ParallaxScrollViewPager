package com.xuan.parallax.scroll;

/**
 * com.xuan.parallax.scroll
 *
 * @author by xuan on 2018/6/13
 * @version [版本号, 2018/6/13]
 * @update by xuan on 2018/6/13
 * @descript
 */
public class ParallaxTag {
    public float translationXIn;
    public float translationXOut;
    public float translationYIn;
    public float translationYOut;

    @Override
    public String toString() {
        return "translationXIn  =  "+translationXIn+
                "     translationXOut  =  "+translationXOut+
                "     translationYIn  =  "+translationYIn+
                "     translationYOut  =  "+translationYOut;
    }
}
