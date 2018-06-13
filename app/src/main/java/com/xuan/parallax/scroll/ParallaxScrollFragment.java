package com.xuan.parallax.scroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

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
public class ParallaxScrollFragment extends Fragment implements LayoutInflater.Factory2{
    public static final String layout_Id="layout_Id";

    private CompatViewInflater mCompatViewInflater;
    //存放所以需要位移的控件，然后获取位移属性去滑动的时候移动
    private List<View> mParallaxScrollViews=new ArrayList<>();

    private int[] parallaxScrollStyleable={
            R.attr.translationXIn,
            R.attr.translationXOut,
            R.attr.translationYIn,
            R.attr.translationYOut,
    };

    public static ParallaxScrollFragment getInstance(Bundle bundle){
        synchronized (bundle){
            ParallaxScrollFragment fragment=new ParallaxScrollFragment();
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId=getArguments().getInt(layout_Id);

        //解析view属性
        //这里传inflater 是单例模式，导致所有创建的view都是fragment的inflater创建的,会报错
        //我们只需要fragment里面的view由自己创建 activity里面的view依旧由activity创建
        //需要克隆一个 inflater
        LayoutInflater mInflater =inflater.cloneInContext(getActivity());
        LayoutInflaterCompat.setFactory2(mInflater,this);

        return mInflater.inflate(layoutId,container,false);
    }

    @Override//会循环子控件调用
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = createView(parent,name,context,attrs);
        if(view!=null){
            Log.i("TAG","自身拦截创建");
            //开始循环解析每个view的属性
            analysisAttrs(view,context,attrs);
        }
        return view;
    }

    private void analysisAttrs(View view, Context context, AttributeSet attrs) {
        TypedArray array=context.obtainStyledAttributes(attrs,parallaxScrollStyleable);

        //有parallaxScrollStyleable这个属性
        if (array != null && array.getIndexCount()!=0) {
//            float translationXIn=array.getFloat(0,0f);
//            float translationXout=array.getFloat(1,0f);
//            float translationYIn=array.getFloat(2,0f);
//            float translationYout=array.getFloat(3,0f);

            ParallaxTag tag=new ParallaxTag();
            for (int i = 0; i < array.getIndexCount(); i++) {
                int attr=array.getIndex(i);

                switch (attr) {
                    case 0:
                        tag.translationXIn=array.getFloat(attr,0f);
                        break;
                    case 1:
                        tag.translationXOut=array.getFloat(attr,0f);
                        break;
                    case 2:
                        tag.translationYIn=array.getFloat(attr,0f);
                        break;
                    case 3:
                        tag.translationYOut=array.getFloat(attr,0f);
                        break;
                }
            }

            Log.i("TAG",tag.toString());
            //给当前view设置一个标记存储起来
            view.setTag(R.id.parallax_tag,tag);
            mParallaxScrollViews.add(view);
        }

        array.recycle();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;//会判断null 调用 上面的方法 不需要理会
    }

    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mCompatViewInflater == null) {
            mCompatViewInflater = new CompatViewInflater();
        }

        // We only want the View to inherit it's context if we're running pre-v21
        final boolean inheritContext = isPre21 && true
                && shouldInheritContext((ViewParent) parent);

        return mCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true /* Read read app:theme as a fallback at all times for legacy reasons */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (!(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    public List<View> getParallaxScrollViews( ) {
        return mParallaxScrollViews;
    }
}
