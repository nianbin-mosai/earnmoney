package com.mdxx.qmmz.newp;

import com.mdxx.qmmz.R;
import com.mdxx.qmmz.R.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class ZhuanFragment extends Fragment {

	public ZhuanFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(layout.fragment_zhuan, container, false);
	}

}
