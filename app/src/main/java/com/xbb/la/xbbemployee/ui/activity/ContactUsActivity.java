package com.xbb.la.xbbemployee.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.BaseActivity;

/**
 * 项目:SellerPlatform
 * 作者：Hi-Templar
 * 创建时间：2015/12/17 17:32
 * 描述：$TODO
 */
public class ContactUsActivity extends BaseActivity {
    @ViewInject(R.id.tel_call_tv)
    private TextView tell_call_tv;

    private String tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
    }

    @Override
    public void initData() {
        super.initData();
        tel="028-655770";
    }

    @Override
    public void initView() {
        super.initView();
        tell_call_tv.setText(getString(R.string.call_info_label).concat(tel));
    }

    @OnClick({R.id.tel_cancel_tv, R.id.tel_call_tv})
    private void contact(View v) {
        switch (v.getId()) {
            case R.id.tel_call_tv:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.tel_cancel_tv:
                finish();
                break;
        }
    }
}
