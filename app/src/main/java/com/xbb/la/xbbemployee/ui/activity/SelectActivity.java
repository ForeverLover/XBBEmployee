package com.xbb.la.xbbemployee.ui.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.xbb.la.modellibrary.utils.MLog;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.BaseActivity;
import com.xbb.la.xbbemployee.config.TitleActivity;
import com.xbb.la.xbbemployee.widget.WheelView.adapter.ArrayWheelAdapter;
import com.xbb.la.xbbemployee.widget.WheelView.widget.OnWheelChangedListener;
import com.xbb.la.xbbemployee.widget.WheelView.widget.WheelView;


/**
 * Description: 滚轮选择界面（时间，性别，价格）
 * User: Templar
 * Date: 2015-08-21
 * Time: 10:25
 * FIXME
 */
public class SelectActivity extends BaseActivity implements OnWheelChangedListener {

    @ViewInject(R.id.select_wheel1)
    private WheelView wheelView1;

    @ViewInject(R.id.select_wheel2)
    private WheelView wheelView2;

    @ViewInject(R.id.select_wheel3)
    private WheelView wheelView3;

    @ViewInject(R.id.select_cancel_btn)
    private TextView select_cancel_btn;

    @ViewInject(R.id.select_confirm_btn)
    private TextView select_ensure_btn;

    @ViewInject(R.id.title_left_img)
    private ImageView title_left_img;
    @ViewInject(R.id.title_center_txt)
    private TextView title_center_txt;

    @ViewInject(R.id.select_title_layout)
    private RelativeLayout select_title_layout;@ViewInject(R.id.select_page_layout)
    private RelativeLayout select_page_layout;

    private String title;
    private int selectedPos;
    private String[] dataList;
    private String selectItem;
    private Intent intent;
    private int currentIndex;
    private float actionBarSize;
    private ArrayWheelAdapter<String> arrayAdapter;
    private int actionBarSizeInXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
    }

    @Override
    public void initData() {
        title = getIntent().getStringExtra("title");
        intent = getIntent();
        dataList = intent.getStringArrayExtra("data");
        actionBarSizeInXml=getIntent().getIntExtra("titleHeight",0);
        selectedPos = intent.getIntExtra("index", 0);
        TypedArray actionbarSizeTypedArray = getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        actionBarSize =actionBarSizeInXml==0 ? actionbarSizeTypedArray.getDimension(0, 0):actionBarSizeInXml;


    }

    @Override
    public void initView() {
        super.initView();
        title_center_txt.setText(title);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) actionBarSize);
        select_title_layout.setLayoutParams(layoutParams);
        if (dataList != null && dataList.length > 0) {
            arrayAdapter = new ArrayWheelAdapter<String>(this, R.layout.wheelview_item, R.id.wheelview_item_text, dataList);
            wheelView1.setViewAdapter(arrayAdapter);
            selectItem = dataList[0];
            if (selectedPos >= 0 && selectedPos < dataList.length) {
                selectItem = dataList[selectedPos];
                wheelView1.setCurrentItem(selectedPos);


            }

        }
        wheelView2.setVisibility(View.GONE);
        wheelView3.setVisibility(View.GONE);
        wheelView1.setVisibleItems(3);
    }

    @Override
    public void initListener() {
        super.initListener();
        select_ensure_btn.setOnClickListener(this);
        select_cancel_btn.setOnClickListener(this);
        title_left_img.setOnClickListener(this);

        wheelView1.addChangingListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_confirm_btn:
                intent.putExtra("text", selectItem);
                intent.putExtra("currentIndex", currentIndex);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.title_left_img:
            case R.id.select_cancel_btn:
                finish();
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        selectItem = dataList[newValue];
        currentIndex = newValue;
        Log.v("Tag", "selectItem=" + selectItem);
    }
}
