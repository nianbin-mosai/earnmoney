package com.wyu.earnmoney.test.ads.yow;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wyu.earnmoney.R;
import com.yow.PointListener;
import com.yow.YoManage;

public class YowMainActivity extends Activity {

	private static String TAG = "MainActivity";

	private TextView pointsView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yowmain);
		pointsView = (TextView) findViewById(R.id.points);
		// 初始化积分墙
		YoManage.getInstance(this, "cc9f189330cdb7e6cbaed28babbd820c", "ch").init();

		// 设置积分奖励回调,如果不需要回调可以不用设置
		YoManage.setGivePointListener(pointListener);

	}

	PointListener pointListener = new PointListener() {

		//奖励用户 虚拟币
		@Override
		public void givePointResult(int points, int totalPoints) {

			Log.d(TAG, "givePointResult points=" + points + "#totalPoints=" + totalPoints);
			// points 奖励积分数
			// totalPoints 当前总积分数
			Toast.makeText(YowMainActivity.this, "完成任务获得积分：" + points + "#当前总积分=" + totalPoints, Toast.LENGTH_LONG).show();
		}


		//获取用户“虚拟币”
		@Override
		public void getPointResult(int points) {
			Log.d(TAG, "getPointResult points=" + points);
			// points 当前用户虚拟币数
			Toast.makeText(YowMainActivity.this, "当前积分数：" + points, Toast.LENGTH_LONG).show();
			pointsView.setText("当前积分数：" + points);

		}

		//扣除玩家“虚拟币”
		@Override
		public void consumePointResult(int points, int status) {
			Log.d(TAG, "consumePointResult points=" + points + "#status=" + status);
			// points 要扣除的 虚拟币
			// status 0：成功 1:失败 2:用户虚拟币余额不足
			Toast.makeText(YowMainActivity.this, "消费：" + points + "积分#status=" + status, Toast.LENGTH_LONG).show();

		}

		//手动奖励用户虚拟币
		@Override
		public void rewardPointResult(String unitName, int totalPoints, int status) {
			Log.d(TAG, "rewardPointResult unitName=" + unitName + "#totalPoints=" + totalPoints + "#status=" + status);
			// unitName 当前积分名称
			// totalPoints 当前用户总积分数
			// status 0:奖励成功 ,1:奖励失败
			Toast.makeText(YowMainActivity.this, "奖励：" + unitName + "#totalPoints=" + totalPoints + "#status=" + status, Toast.LENGTH_LONG).show();

		}
	};

	public void openOffer(View view) {
		// 显示积分墙
		String userId = "123"; // 传入自己的标用户标识
		YoManage.showOffer(this, userId);
	}

	public void getPoint(View view) {
		// 获取积分
		YoManage.getPoints(pointListener);
	}

	public void consumeOffer(View view) {
		int points = 10;
		// 消费积分
		YoManage.consumePoints(points, pointListener);

	}

	public void rewardOffer(View view) {
		int points = 10;
		// 奖励积分
		YoManage.rewardPoints(points, pointListener);

	}

}

