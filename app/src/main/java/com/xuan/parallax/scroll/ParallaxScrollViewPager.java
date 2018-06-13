package com.xuan.parallax.scroll;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * com.xuan.parallax.scroll
 *
 * @author by xuan on 2018/6/13
 * @version [版本号, 2018/6/13]
 * @update by xuan on 2018/6/13
 * @descript
 */
public class ParallaxScrollViewPager extends ViewPager {
    private int[] layouts;
    private ArrayList<Fragment> fragments;

    public ParallaxScrollViewPager(@NonNull Context context) {
        this(context,null);
    }

    public ParallaxScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLayout(int[] layouts,FragmentManager fm) {
        this.layouts=layouts;
        if(layouts==null || layouts.length==0){
            return;
        }

        fragments=new ArrayList<>();

        for (int i = 0; i < layouts.length; i++) {
            Bundle bundle=new Bundle();
            bundle.putInt(ParallaxScrollFragment.layout_Id,layouts[i]);
            fragments.add(ParallaxScrollFragment.getInstance(bundle));
        }

        //设置adapter
        BaseParallaxScrollAdapter adapter=new BaseParallaxScrollAdapter(fm);
        setAdapter(adapter);
        setOffscreenPageLimit(fragments.size());

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //滚动时
                Log.i("TAG","position   " +position);
                Log.i("TAG","positionOffset   " +positionOffset);
                Log.i("TAG","positionOffsetPixels   " +positionOffsetPixels);

                //当前显示的fragment 滑动出去
                ParallaxScrollFragment outFragment= (ParallaxScrollFragment) fragments.get(position);
                List<View> mParallaxScrollViews=outFragment.getParallaxScrollViews();
                for (View view : mParallaxScrollViews) {
                    ParallaxTag tag=(ParallaxTag)view.getTag(R.id.parallax_tag);
                    view.setTranslationX(
                            (-positionOffsetPixels)*tag.translationXOut);
                    view.setTranslationY(
                            (-positionOffsetPixels)*tag.translationYOut);

                }
                //右边的fragment 滑动进来
                if(position+1<fragments.size()-1){
                    ParallaxScrollFragment inFragment= (ParallaxScrollFragment) fragments.get(position+1);
                    mParallaxScrollViews=inFragment.getParallaxScrollViews();
                    for (View view : mParallaxScrollViews) {
                        ParallaxTag tag=(ParallaxTag)view.getTag(R.id.parallax_tag);
                        view.setTranslationX(
                                (getMeasuredWidth()-positionOffsetPixels)*tag.translationXIn);
                        view.setTranslationY(
                                (getMeasuredWidth()-positionOffsetPixels)*tag.translationYIn);

                    }
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class BaseParallaxScrollAdapter extends FragmentStatePagerAdapter{

        public BaseParallaxScrollAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }
    }
}
