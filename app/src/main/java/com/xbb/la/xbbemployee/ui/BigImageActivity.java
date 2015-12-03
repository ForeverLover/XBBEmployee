package com.xbb.la.xbbemployee.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.adapter.BigImageAdpater;
import com.xbb.la.xbbemployee.config.BaseActivity;
import com.xbb.la.xbbemployee.widget.MyPhotoViewPager;

import java.util.ArrayList;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/27 17:17
 * 描述：大图浏览界面
 */

public class BigImageActivity  extends BaseActivity {
    private MyPhotoViewPager big_img_pager;
    private LinearLayout big_img_group;
    private ArrayList<View> viewlist;
    private ImageView[] points;
    private ArrayList<String> img_urls;
    private BigImageAdpater adapter;
    private static final String ISLOCKED_ARG = "isLocked";
    private ImageView point;
    private ImageView big_back;
    private int selectIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigimg);
        initWidget(savedInstanceState);

    }


    private void initWidget(Bundle savedInstanceState) {
        big_img_pager = (MyPhotoViewPager) findViewById(R.id.big_img_pager);
        big_img_group = (LinearLayout) findViewById(R.id.big_img_group);
        big_back = (ImageView) findViewById(R.id.big_back);

        img_urls = getIntent().getStringArrayListExtra("img_pics");
        selectIndex = getIntent().getIntExtra("pos", 0);
        if (img_urls != null && !img_urls.isEmpty()) {
            setPoint();
            adapter = new BigImageAdpater(this, img_urls);
            big_img_pager.setAdapter(adapter);
            big_img_pager.setOnPageChangeListener(pageListener);
            big_img_pager.setCurrentItem(selectIndex);
            if (savedInstanceState != null) {
                boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG,
                        false);
                ((MyPhotoViewPager) big_img_pager).setLocked(isLocked);
            }
        } else {
            showToast("图片下载不正常");
            finish();
        }
        big_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < points.length; i++) {
                points[arg0].setImageResource(R.mipmap.dot);
                if (arg0 != i) {
                    points[i].setImageResource(R.mipmap.dot_normal);
                }
            }
        }
    };

    private void setPoint() {
        // TODO Auto-generated method stub
        big_img_group.removeAllViews();
        points = new ImageView[img_urls.size()];
        for (int i = 0; i < img_urls.size(); i++) {
            point = new ImageView(this);
            point.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            point.setPadding(10, 0, 10, 0);
            points[i] = point;
            if (i == selectIndex) {
                // textViews[i].setBackgroundResource(R.drawable.radio_sel);
                points[i].setImageResource(R.mipmap.dot);
            } else {
                // textViews[i].setBackgroundResource(R.drawable.radio);
                points[i].setImageResource(R.mipmap.dot_normal);
            }
            big_img_group.addView(point);
        }
        if (points.length <= 1) {
            big_img_group.setVisibility(View.GONE);

        }
    }

}
