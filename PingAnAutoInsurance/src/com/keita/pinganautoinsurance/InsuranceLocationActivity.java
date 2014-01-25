package com.keita.pinganautoinsurance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 
 *定位页面 使baidu API
 */
public class InsuranceLocationActivity extends Activity {

	// 定位相关
	LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListener myListener = new MyLocationListener();

	// 定位图层
	locationOverlay myLocationOverlay = null;
	// 弹出泡泡图层
	private PopupOverlay pop = null;// 弹出泡泡图层，浏览节点时使用
	private TextView popupText = null;// 泡泡view
	private View viewCache = null;

	// 地图相关，使用继承MapView的MyLocationMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	BMapManager mBMapMan = null;
	MyLocationMapView mMapView = null; // 地图View
	private MapController mMapController = null;

	// UI相关
	OnCheckedChangeListener radioButtonListener = null;
	Button requestLocButton = null;
	boolean isRequest = false;// 是否手动触发请求定位
	boolean isFirstLoc = true;// 是否首次定位
	boolean isLocationClientStop = false;
	// 定位地址
	private EditText locationAddress;
	String address = "";
	private MyApplication application = null;
	//下一步按钮
	private Button continueBtn = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init("2F64E03AC6D24CC96C4713F921D8B653D2FC8747", null);
		setContentView(R.layout.activity_insurance_location_gps);
		application = (MyApplication)this.getApplication();
		//把本Activity放入管理list中
		//application.getActivityList().add(this);
		ImageButton previous_button = null;
		View view = findViewById(R.id.top_bar);
		TextView title =(TextView) view.findViewById(R.id.top_title);
		title.setText("事故定位");
		previous_button =(ImageButton) view.findViewById(R.id.top_bar_back);
		previous_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				application.getActivityList().remove(this);
				InsuranceLocationActivity.this.finish();
			}
			
		});
		locationAddress = (EditText) findViewById(R.id.location_text);
		requestLocButton = (Button) findViewById(R.id.button1);
		continueBtn = (Button)findViewById(R.id.continue_btn);
	
		//设置监听事件
		continueBtn.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转到下一步
				Intent intent = new Intent();
				intent.putExtra("location", locationAddress.getText().toString());
				intent.setClass(InsuranceLocationActivity.this, InsurancePhotoActivity.class);
				startActivity(intent);
				InsuranceLocationActivity.this.finish();
			}
		});
		requestLocButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 手动定位请求
				requestLocClick();
			}
		});
		

		// 地图初始化
		mMapView = (MyLocationMapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		// mMapView.setTraffic(true);
		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		/*mMapView.setBuiltInZoomControls(true);
		ZoomControls zoomControls = (ZoomControls) mMapView.getChildAt(2);
		zoomControls.setPadding(0, 0, 0, 50);*/
		// 创建 弹出泡泡图层
		createPaopao();

		// 定位初始化
		mLocClient = new LocationClient(this);
		locData = new LocationData();
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(10000);
		option.setAddrType("all");
		//option.setPriority(LocationClientOption.NetWorkFirst);
		mLocClient.setLocOption(option);
		mLocClient.start();

		// 定位图层初始化
		myLocationOverlay = new locationOverlay(mMapView);
		// 设置定位数据
		myLocationOverlay.setData(locData);
		// 添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		// 修改定位数据后刷新图层生效
		mMapView.refresh();

	}

	/**
	 * 手动触发一次定位请求
	 */
	public void requestLocClick() {
		isRequest = true;
		mLocClient.requestLocation();
		Toast.makeText(InsuranceLocationActivity.this, "正在定位…",
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * 修改位置图标
	 * 
	 * @param marker
	 */
	public void modifyLocationOverlayIcon(Drawable marker) {
		// 当传入marker为null时，使用默认图标绘制
		myLocationOverlay.setMarker(marker);
		// 修改图层，需要刷新MapView生效
		mMapView.refresh();
	}

	/**
	 * 创建弹出泡泡图层
	 */
	public void createPaopao() {
		viewCache = getLayoutInflater()
				.inflate(R.layout.custom_text_view, null);
		popupText = (TextView) viewCache.findViewById(R.id.textcache);
		// 泡泡点击响应回调
		PopupClickListener popListener = new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				Log.v("click", "clickapoapo");
			}
		};
		pop = new PopupOverlay(mMapView, popListener);
		MyLocationMapView.pop = pop;
	}
	

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || isLocationClientStop)
				return;
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				Log.v("定位", "网络定位");
				address = location.getAddrStr();
				if (address != null) {
					locationAddress.setText(address);
					
				}
			}
			if(address == null){
				locationAddress.setText("获取详细地址失败,请检查网络服务是否开启");
			}
			// 如果不显示定位精度圈，将accuracy赋值为0即可
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();

			// 更新定位数据
			myLocationOverlay.setData(locData);
			// 更新图层数据执行刷新后生效
			mMapView.refresh();
			// 是手动触发请求或首次定位时，移动到定位点
			if (isRequest || isFirstLoc) {
				GeoPoint point = new  GeoPoint(
						(int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6));
				// 移动地图到定位点
				mMapController.animateTo(point);
				isRequest = false;
			}
			// 首次定位完成
			isFirstLoc = false;
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}
	
	// 继承MyLocationOverlay重写dispatchTap实现点击处理
	public class locationOverlay extends MyLocationOverlay {

		public locationOverlay(MapView mapView) {
			super(mapView);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected boolean dispatchTap() {
			// TODO Auto-generated method stub
			// 处理点击事件,弹出泡泡
			popupText.setBackgroundResource(R.drawable.popup);
			popupText.setText("我的位置");
			pop.showPopup(BMapUtil.getBitmapFromView(popupText), new GeoPoint(
					(int) (locData.latitude * 1e6),
					(int) (locData.longitude * 1e6)), 8);
			return true;
		}

	}

	@Override
	protected void onPause() {
		isLocationClientStop = true;
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		isLocationClientStop = false;
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		if (mLocClient != null)
			mLocClient.stop();
		if(mBMapMan != null)
			mBMapMan.stop();
		isLocationClientStop = true;
		mMapView.destroy();
		mBMapMan.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}



}

/**
 * 继承MapView重写onTouchEvent实现泡泡处理操作
 * 
 * 
 */
class MyLocationMapView extends MapView {
	static PopupOverlay pop = null;// 弹出泡泡图层，点击图标使用

	public MyLocationMapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyLocationMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLocationMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!super.onTouchEvent(event)) {
			// 消隐泡泡
			if (pop != null && event.getAction() == MotionEvent.ACTION_UP)
				pop.hidePop();
		}
		return true;
	}
	
}
