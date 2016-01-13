package com.xbb.la.xbbemployee.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRouteGuideManager.OnNavigationListener;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.BaseActivity;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/17 16:49
 * 描述：导航页面
 */

public class DestinationGuideActivity extends BaseActivity {
    private BNRoutePlanNode mBNRoutePlanNode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		createHandler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        }
        View view = BNRouteGuideManager.getInstance().onCreate(this, new OnNavigationListener() {

            @Override
            public void onNaviGuideEnd() {
                showToast(getString(R.string.navigate_end));
                finish();
            }

            @Override
            public void notifyOtherAction(int actionType, int arg1, int arg2, Object obj) {

            }
        });

        if (view != null) {
            setContentView(view);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mBNRoutePlanNode = (BNRoutePlanNode) bundle.getSerializable(Constant.IntentAction.ROUTE_PLAN_NODE);
            }
        }

    }


    @Override
    protected void onResume() {
        BNRouteGuideManager.getInstance().onResume();
        super.onResume();

//		hd.sendEmptyMessageDelayed(MSG_SHOW, 5000);
    }

    protected void onPause() {
        super.onPause();
        BNRouteGuideManager.getInstance().onPause();
    }

    ;

    @Override
    protected void onDestroy() {
        BNRouteGuideManager.getInstance().onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        BNRouteGuideManager.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        BNRouteGuideManager.getInstance().onBackPressed(false);
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        BNRouteGuideManager.getInstance().onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }
}
