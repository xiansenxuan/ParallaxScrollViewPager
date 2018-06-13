package com.xuan.parallax.scroll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private ParallaxScrollViewPager view_pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        view_pager = findViewById(R.id.view_pager);

        //直接传递布局 就可以达到视差滚动效果 不需要外部去做各种处理逻辑
        view_pager.setLayout(
                new int[]{R.layout.fragment_page_first
                        ,R.layout.fragment_page_second
                        ,R.layout.fragment_page_third}
                , getSupportFragmentManager());


    }
}
