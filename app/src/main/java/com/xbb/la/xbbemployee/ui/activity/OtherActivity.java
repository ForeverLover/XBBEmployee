package com.xbb.la.xbbemployee.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.TitleActivity;

/**
 * 项目:SellerPlatform
 * 作者：Hi-Templar
 * 创建时间：2015/12/17 17:32
 * 描述：$TODO
 */
public class OtherActivity extends TitleActivity {
    @ViewInject(R.id.other_tel_tv)
    private TextView other_tel_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(R.string.menu_other_text);
    }

    @OnClick({R.id.other_feedback_layout, R.id.other_link_layout})
    private void otherAction(View v) {
        switch (v.getId()) {
            case R.id.other_feedback_layout:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.other_link_layout:
                startActivity(ContactUsActivity.class);
                break;
        }
    }


}
