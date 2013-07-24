package com.keita.pinganautoinsurance;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.app.Activity;
import android.os.Bundle;

public class InsuranceLocationActivity extends Activity {
	private BMapManager mBMapMan = null;
	private MapView mMapView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init("2F64E03AC6D24CC96C4713F921D8B653D2FC8747", null);
		setContentView(R.layout.activity_insurance_location_gps);
		mMapView = (MapView)findViewById(R.id.map_view);
		mMapView.setBuiltInZoomControls(true);
		
		//设置启用内置的缩放控件   
		MapController mMapController=mMapView.getController();   
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放   
		/*GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));   
		//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)   
		mMapController.setCenter(point);//设置地图中心点   
*/		
		mMapController.setZoom(12);//设置地图zoom级别   
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);   
		LocationData locData = new LocationData();   
		//手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要使用百度经纬度坐标（bd09ll）   
		locData.latitude = 39.945;   
		locData.longitude = 116.404;   
		locData.direction = 2.0f;   
		myLocationOverlay.setData(locData);   
		mMapView.getOverlays().add(myLocationOverlay);   
		mMapView.refresh();   
		mMapView.getController().animateTo(new GeoPoint((int)(locData.latitude*1e6),   
		(int)(locData.longitude* 1e6))); 
		
	}
	@Override
	protected void onDestroy(){
	        mMapView.destroy();
	        if(mBMapMan!=null){
	                mBMapMan.destroy();
	                mBMapMan=null;
	        }
	        super.onDestroy();
	}
	@Override
	protected void onPause(){
	        mMapView.onPause();
	        if(mBMapMan!=null){
                mBMapMan.stop();
	        }
	        super.onPause();
	}
	@Override
	protected void onResume(){
	        mMapView.onResume();
	        if(mBMapMan!=null){
	                mBMapMan.start();
	        }
        super.onResume();
	}


}
