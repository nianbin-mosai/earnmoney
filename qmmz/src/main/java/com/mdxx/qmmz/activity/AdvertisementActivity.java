package com.mdxx.qmmz.activity;

import net.youmi.android.offers.OffersManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.datouniao.AdPublisher.AppConfig;
import com.datouniao.AdPublisher.AppConnect;
import com.fingermobi.vj.activity.QdiActivity;
import com.fingermobi.vj.listener.IVJAPI;
import com.fingermobi.vj.listener.IVJAppidStatus;
import com.mdxx.qmmz.BannerBean;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.utils.InterfaceTool;
import com.newqm.pointwall.QSdkManager;
import com.qm.lo.inter.QEarnNotifier;
import com.yow.YoManage;

public class AdvertisementActivity extends BaseActivity implements
		OnClickListener, QEarnNotifier {
	private RelativeLayout meLayout;
//	private ImageView banner_image2;
	private final String banner_url = InterfaceTool.ULR + "banner/index";
	private BannerBean bannerBean;
	private AppConnect instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_advertisement);
		meLayout = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.activity_advertisement, null);
		setContentView(meLayout);
		initview();
//		getBannerImage();
	}

	public void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.layout_youmi).setOnClickListener(this);
		findViewById(R.id.layout_datouniao).setOnClickListener(this);
		findViewById(R.id.layout_qumi).setOnClickListener(this);
		findViewById(R.id.layout_juyou).setOnClickListener(this);
		findViewById(R.id.layout_qingsongzhuan).setOnClickListener(this);
		AppConfig config = new AppConfig();//大头鸟
		config.setCtx(this);
		config.setClientUserID(getuseid());
		instance = AppConnect.getInstance(config);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.layout_youmi:
			OffersManager.getInstance(AdvertisementActivity.this)
					.setCustomUserId(getuseid());
			OffersManager.getInstance(AdvertisementActivity.this).setUsingServerCallBack(true);
			OffersManager.getInstance(AdvertisementActivity.this).showOffersWall();
			break;
		case R.id.layout_datouniao:
			instance.ShowAdsOffers();
			break;
		case R.id.layout_qumi:
			QSdkManager.getsdkInstance().showOffers(this);
			break;
		case R.id.layout_juyou:
			YoManage.showOffer(this,getuseid());
			break;
		case R.id.layout_qingsongzhuan:
			IVJAPI ivjapi = new IVJAPI();
			ivjapi.setAppid("ae7304f92345fc939e9edfb27c1d7305");
			ivjapi.setGameid(getuseid());
			ivjapi.init(AdvertisementActivity.this, new IVJAppidStatus() {
				
				@Override
				public void appidStatus(int status) {
					switch (status) {
					case IVJAPI.VJ_ERROR:
						// 失败
						break;
					case IVJAPI.VJ_APPID_INVALID:
						// 广告位关闭							
						break;
					case IVJAPI.VJ_SUCCESS:
						// 广告位打开							
						startActivity(new Intent(AdvertisementActivity.this,
								QdiActivity.class));
						break;
					case IVJAPI.VJ_CLOSE:
						// 界面被关闭						
						break;
					case IVJAPI.VJ_ON_PRESENT:
						// 界面被展示出来						
						break;
					}
				}
			});
			break;
		}
	}

//	private void getBannerImage() {
//		Map map = new HashMap<String, String>();
//		map.put("type", "2");
//		InterfaceTool.Networkrequest(this, queue, m_pDialog, banner_url,
//				new Response.Listener<JSONObject>() {
//
//					@Override
//					public void onResponse(JSONObject response) {
//						closewaite();
//						try {
//							int code = response.getInt("code");
//							if (code == 1) {
//								JSONArray data = response.getJSONArray("data");
//								if (data != null) {
//									JSONObject jsonObject = data
//											.getJSONObject(0);
//									bannerBean = new BannerBean();
//									bannerBean.setUrl(jsonObject
//											.getString("url"));
//									bannerBean.setImageurl(jsonObject
//											.getString("imageurl"));
//									imageLoader.displayImage(
//											bannerBean.getImageurl(),
//											banner_image2, options, null);
//								}
//							} else {
//								Toastshow(response.getString("msg"));
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//				}, map);
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (instance != null) {
			instance.close();
		}
	}

	@Override
	public void earnedPoints(float arg0, float arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getPoints(float arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getPointsFailed(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
