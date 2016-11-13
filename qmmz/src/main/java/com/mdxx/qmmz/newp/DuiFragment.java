package com.mdxx.qmmz.newp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mdxx.qmmz.R;
import com.mdxx.qmmz.Reward;
import com.mdxx.qmmz.common.ToastUtils;
import com.mdxx.qmmz.common.ViewUtil;
import com.mdxx.qmmz.utils.InterfaceTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DuiFragment extends Fragment implements OnClickListener {
    private PullToRefreshListView pull_reward;
    private ListView mListView;
    private RewardAdapter adapter;
    private NMainActivity activity;
    private TextView text_invitenum;
    private TextView text_friend7;
    private TextView text_hongbao;
    private int page = 1;
    private final String rewardurl = InterfaceTool.ULR + "user/friendreward";
    private ArrayList<Reward> rewardlist = new ArrayList<Reward>();
    private ImageView noDataImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (NMainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_dui, container, false);
        initview(view);
//		getreward(true);
        return view;
    }

    private void initview(View v) {
        v.findViewById(R.id.duihuan_layout).setOnClickListener(this);
        noDataImage = (ImageView) v.findViewById(R.id.imageView2);
        pull_reward = (PullToRefreshListView) v.findViewById(R.id.pull_reward);
        mListView = pull_reward.getRefreshableView();
        mListView.setDividerHeight(0);
        adapter = new RewardAdapter();
        mListView.setAdapter(adapter);
        pull_reward.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                getreward(true);
            }

        });
        pull_reward
                .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        page++;
                        getreward(false);
                    }
                });

        View view = activity.getLayoutInflater().inflate(R.layout.invitation_head, null);
        text_invitenum = (TextView) view.findViewById(R.id.text_invitenum);
        text_friend7 = (TextView) view.findViewById(R.id.text_friend7);
        text_hongbao = (TextView) view.findViewById(R.id.text_hongbao);
        mListView.addHeaderView(view);

        noDataImage.setVisibility(View.VISIBLE);
        pull_reward.setVisibility(View.GONE);
    }

    private void getreward(final boolean isclear) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", activity.getuseid());
        params.put("page", page + "");
        InterfaceTool.Networkrequest(activity, activity.queue, null, rewardurl,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject arg0) {
                        try {
                            String code = arg0.getString("code");
                            if (code.equals("1")) {
                                String hongbao = arg0.getString("hongbao");
                                String friend7 = arg0.getString("friend7");
                                String invitenum = arg0.getString("invitenum");
                                text_friend7.setText(friend7);
                                text_hongbao.setText(hongbao);
                                text_invitenum.setText(invitenum);
                                JSONArray data = arg0.getJSONArray("data");
                                if (isclear) {
                                    rewardlist.clear();
                                }
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) data
                                            .get(i);
                                    String time = jsonObject.getString("time");
                                    String money = jsonObject
                                            .getString("money");
                                    String remark = jsonObject
                                            .getString("remark");
                                    Reward reward = new Reward(remark, time,
                                            money);
                                    rewardlist.add(reward);
                                }
                                adapter.notifyDataSetChanged();
                                if (rewardlist.size() > 0) {
                                    noDataImage.setVisibility(View.GONE);
                                    pull_reward.setVisibility(View.VISIBLE);
                                }
                            } else {
                                activity.Toastshow("错误代码" + code);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        pull_reward.onRefreshComplete();
                    }
                }, params);
    }

    class RewardAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return rewardlist.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Viewholder holder;
            if (convertView == null) {
                holder = new Viewholder();
                convertView = activity.getLayoutInflater().inflate(
                        R.layout.linnear_reward, null);
                holder.image_head = (ImageView) convertView
                        .findViewById(R.id.image_head);
                holder.text_time = (TextView) convertView
                        .findViewById(R.id.text_time);
                holder.text_remark = (TextView) convertView
                        .findViewById(R.id.text_remark);
                holder.text_money = (TextView) convertView
                        .findViewById(R.id.text_money);
                convertView.setTag(holder);
            } else {
                holder = (Viewholder) convertView.getTag();
            }
            Reward reward = rewardlist.get(position);
            activity.imageLoader.displayImage(activity.getimageurl(), holder.image_head,
                    activity.options, null);
            holder.text_time.setText(reward.getTime());
            holder.text_remark.setText(reward.getRemark());
            holder.text_money.setText("+" + reward.getMoney() + "元");
            return convertView;
        }

    }

    class Viewholder {
        ImageView image_head;
        TextView text_time;
        TextView text_remark;
        TextView text_money;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.duihuan_layout:
                MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
                builder.title("请输入手机号和兑换的积分数");
                builder.negativeText("取消");
                builder.positiveText("确定");
                builder.autoDismiss(false);
                builder.customView(R.layout.dialog_dui, false);
                builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View cv = dialog.getCustomView();
                        EditText phoneEditText = (EditText) cv.findViewById(R.id.phoneEditText);
                        EditText scoreEditText = (EditText) cv.findViewById(R.id.scoreEditText);
                        String phone = phoneEditText.getText().toString();
                        String score = scoreEditText.getText().toString();
                        // 检查手机号
                        if (!isMobileNO(phone)) {
                            ToastUtils.showToast(getActivity().getApplicationContext(), "请输入正确的手机号");
                            return;
                        }
                        // 检查积分值
                        if (!isGoodScore(score)) {
                            ToastUtils.showToast(getActivity().getApplicationContext(), "积分值必须是正整数");
                            return;
                        }
                        dialog.dismiss();
                        duiScore(phone, score);
                    }
                });
                builder.show();
                break;
        }
    }

    private boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    private boolean isGoodScore(String mobiles) {
        Pattern p = Pattern.compile("^[0-9]*[1-9][0-9]*$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    private void duiScore(String phone, String score) {
        // 兑换
        ToastUtils.showToast(getActivity().getApplicationContext(), phone + " : " + score);
    }
}
