package com.xbb.la.xbbemployee.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.xbb.la.modellibrary.bean.LocationBean;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.BaseActivity;
import com.xbb.la.xbbemployee.listener.LocateProcessListener;
import com.xbb.la.xbbemployee.utils.DrivingRouteOverlay;
import com.xbb.la.xbbemployee.utils.LocationUtil;
import com.xbb.la.xbbemployee.utils.OverlayManager;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/22 15:08
 * 描述：路径规划界面
 */

public class RoutePlanActivity extends BaseActivity implements LocateProcessListener, BaiduMap.OnMapClickListener,
        OnGetRoutePlanResultListener {
    private MapView route_plan_map;
    private Button arrived_btn;
    private Button navigate_btn;

    private ImageView route_plan_targetImg;
    private ImageView route_plan_selfImg;

    private LocationBean myLocation;
    private LocationBean targetLocation;

    private BaiduMap mBaiduMap;
    private int nodeIndex = -1;//节点索引,供浏览节点时使用
    private RouteLine route = null;
    private OverlayManager routeOverlay = null;
    //搜索相关
    private RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    private boolean useDefaultIcon = false;

    private int distance=0;
    private int needTime;

    private String orderId;


    @Override
    protected void initViews() {
        setContentView(R.layout.activity_route_plan);
        page_title = (TextView) findViewById(R.id.title_center_txt);
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        route_plan_targetImg = (ImageView) findViewById(R.id.route_plan_targetImg);
        route_plan_selfImg = (ImageView) findViewById(R.id.route_plan_selfImg);
        route_plan_map = (MapView) findViewById(R.id.route_plan_map);
        arrived_btn = (Button) findViewById(R.id.arrived_btn);
        navigate_btn = (Button) findViewById(R.id.navigate_btn);
    }

    @Override
    protected void initData() {
        mBaiduMap = route_plan_map.getMap();
        myLocation = SharePreferenceUtil.getInstance().getLocationInfo(this);
        targetLocation = (LocationBean) getIntent().getSerializableExtra(Constant.IntentVariable.DESTINATION_LOCATION);
        orderId=getIntent().getStringExtra(Constant.IntentVariable.ORDER_ID);
        userId=SharePreferenceUtil.getInstance().getUserId(this);
        apiRequest=new ApiRequest(this);
        initNavi();
        route_plan_map.showZoomControls(false);
//        int count = route_plan_map.getChildCount();
//        for (int i = 0; i < count; i++) {
//            View child = route_plan_map.getChildAt(i);
////             隐藏百度logo ZoomControl
//             if (child instanceof ImageView || child instanceof ZoomControls){
//                 child.setVisibility(View.GONE);
//             }
//
////            if (child instanceof ZoomControls) {
////                child.setVisibility(View.GONE);
////            }
//        }

        title_left_img.setOnClickListener(this);
        route_plan_targetImg.setOnClickListener(this);
        route_plan_selfImg.setOnClickListener(this);
        arrived_btn.setOnClickListener(this);
        navigate_btn.setOnClickListener(this);
        application.addUIListener(LocateProcessListener.class, this);
        //地图点击事件处理
        mBaiduMap.setOnMapClickListener(this);
//        mBaiduMap.setTrafficEnabled(true);
        // 初始化搜索模块，注册事件监听

        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

        searchProcess();

    }

    /**
     * 发起路线规划搜索示例
     */
    public void searchProcess() {
        //重置浏览节点的路线数据
        route = null;
        mBaiduMap.clear();
        PlanNode stNode = null;
        PlanNode enNode = null;
        // 处理搜索按钮响应
        //设置起终点信息，对于tranist search 来说，城市名无意义
        if (myLocation != null) {
            stNode = PlanNode.withLocation(new LatLng(Double.parseDouble(myLocation.getLat()), Double.parseDouble(myLocation.getLon())));
            Log.v("Tag", "myLocation:"+myLocation.getLat()+","+myLocation.getLon());
        }else
            Log.v("Tag", "myLocation is null");
        if (targetLocation != null) {
            enNode = PlanNode.withLocation(new LatLng(Double.parseDouble(targetLocation.getLat()), Double.parseDouble(targetLocation.getLon())));
            Log.v("Tag", "targetLocation:"+targetLocation.getLat()+","+targetLocation.getLon());
        }else
            Log.v("Tag", "targetLocation is null");

        // 实际使用中请对起点终点城市进行正确的设定
        if (enNode != null && stNode != null)
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
        else
            Log.v("Tag","enNode==null?"+(enNode==null)+" stNode==null?"+(stNode==null));

    }

    /**
     * 切换路线图标，刷新地图使其生效
     * 注意： 起终点图标使用中心对齐.
     */
    public void changeRouteIcon(View v) {
        if (routeOverlay == null) {
            return;
        }
        if (useDefaultIcon) {
            ((Button) v).setText("自定义起终点图标");
            Toast.makeText(this,
                    "将使用系统起终点图标",
                    Toast.LENGTH_SHORT).show();

        } else {
            ((Button) v).setText("系统起终点图标");
            Toast.makeText(this,
                    "将使用自定义起终点图标",
                    Toast.LENGTH_SHORT).show();

        }
        useDefaultIcon = !useDefaultIcon;
        routeOverlay.removeFromMap();
        routeOverlay.addToMap();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    //定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.mipmap.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.mipmap.icon_en);
            }
            return null;
        }

        @Override
        public int getLineColor() {
            return super.getLineColor();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_img:
                finish();
                break;
            case R.id.route_plan_targetImg:
                if (targetLocation != null && !StringUtil.isEmpty(targetLocation.getLat()) && !StringUtil.isEmpty(targetLocation.getLon()))
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(Double.parseDouble(targetLocation.getLat()), Double.parseDouble(targetLocation.getLon()))));
                break;
            case R.id.route_plan_selfImg:
                if (myLocation != null && !StringUtil.isEmpty(myLocation.getLat()) && !StringUtil.isEmpty(myLocation.getLon()))
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(Double.parseDouble(myLocation.getLat()), Double.parseDouble(targetLocation.getLon()))));
                break;
            case R.id.arrived_btn:
               if (apiRequest==null)
                   apiRequest=new ApiRequest(this);
                apiRequest.arrive(orderId,userId);
                break;
            case R.id.navigate_btn:
                startLocate();
//                LocationBean locationBean=new LocationBean();
//                locationBean.setLat("30.663456");
//                locationBean.setLon("104.072227");
//                startActivity(new Intent(this,ArrivedActivity.class).putExtra("targetLocation",locationBean));
                break;
        }
    }


    private void initNavi() {
//        BaiduNaviManager.getInstance().setNativeLibraryPath(Constant.Path.PATH_NAVI);
        BaiduNaviManager.getInstance().init(this, Constant.Path.PATH_NAVI, "BDNavi",
                new BaiduNaviManager.NaviInitListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                        /*if (0 == status)
                            Log.v("BD", "key vertify succeed");
                        else
                            Log.v("BD", "key vertify failded");*/

                    }

                    public void initSuccess() {
                    }

                    public void initStart() {
                    }

                    public void initFailed() {
                    }
                }, new BNOuterTTSPlayerCallback() {

                    @Override
                    public void stopTTS() {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void resumeTTS() {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void releaseTTSPlayer() {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public int playTTSText(String speech, int bPreempt) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public void phoneHangUp() {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void phoneCalling() {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void pauseTTS() {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void initTTSPlayer() {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public int getTTSState() {
                        // TODO Auto-generated method stub
                        return 0;
                    }
                });

    }

    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType) {
        LocationBean locationBean = SharePreferenceUtil.getInstance().getLocationInfo(this);
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        if (locationBean != null)
            sNode = LocationUtil.getInstance().getBNRoutePlanNode(LocationUtil.getInstance().getBDLocation(locationBean));
        if (targetLocation != null)
            eNode = LocationUtil.getInstance().getBNRoutePlanNode(LocationUtil.getInstance().getBDLocation(targetLocation));
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
        }
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            dismissLoading();
            Intent intent = new Intent(RoutePlanActivity.this, DestinationGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.IntentAction.ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        @Override
        public void onRoutePlanFailed() {

        }
    }
    @Override
    public void onLocateStart() {

    }

    @Override
    public void onLocateFailed() {

    }

    @Override
    public void onSucceed(LocationBean locationBean) {
        myLocation = SharePreferenceUtil.getInstance().getLocationInfo(this);
        if (BaiduNaviManager.isNaviInited()) {
            routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09_MC);
        }
    }

    @Override
    protected void onPause() {
        route_plan_map.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        route_plan_map.onResume();
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        mSearch.destroy();
        super.onDestroy();
        application.removeUIListener(LocateProcessListener.class, this);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            if (distance != 0)
                distance = 0;
            if (needTime != 0)
                needTime = 0;
            nodeIndex = -1;
            route = drivingRouteResult.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
            routeOverlay = overlay;
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(drivingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();

            List<DrivingRouteLine> routeLines=drivingRouteResult.getRouteLines();
            List<DrivingRouteLine.DrivingStep> steps=routeLines.get(0).getAllStep();
            // 分为N步  
           for(int i=0; i<steps.size(); i++){
               String instructions=steps.get(i).getInstructions();
                int direction=steps.get(i).getDirection();
                int distance=steps.get(i).getDistance();
                this.distance+=distance;// 叠加每一个step的distance  
                String entraceInstructions=steps.get(i).getEntranceInstructions();
               String title=steps.get(i).getEntrance().getTitle();
                }
            needTime=distance/550;
        }

        page_title.setText(StringUtil.getHourMinute(needTime)+" "+StringUtil.distanceFormatter(this.distance));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onPrepare(int taskId) {
        super.onPrepare(taskId);
        showLoading();
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        switch (taskId){
            case Task.ARRIVE:
                localBroadcastManager.sendBroadcast(new Intent(Constant.IntentAction.STOP_LOCATION_UPLOAD).putExtra(Constant.IntentVariable.ORDER_ID,orderId));
                localBroadcastManager.sendBroadcast(new Intent(Constant.IntentAction.CHANGE_STATE));
                startActivity(new Intent(this, TransactionActivity.class).putExtra(Constant.IntentVariable.ORDER_ID, orderId));
                finish();
                break;
        }
    }

    @Override
    public void onEnd(int taskId) {
        super.onEnd(taskId);
        dismissLoading();
    }
}
