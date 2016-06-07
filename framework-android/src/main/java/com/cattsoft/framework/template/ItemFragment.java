package com.cattsoft.framework.template;

import com.cattsoft.framework.R;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


public class ItemFragment extends Fragment {

    private TextView mTextView;
    private String title;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contextView = inflater.inflate(R.layout.fragment_item, container, false);
        contextView.setBackgroundResource(R.drawable.viewpager_fragment_one_bg);
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//				 LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		params.leftMargin = ViewUtil.dip2px(getActivity(), 10);
//		params.rightMargin = ViewUtil.dip2px(getActivity(), 10);
//		contextView.setLayoutParams(params);
        Bundle mBundle = getArguments();
        title = mBundle.getString("arg");

		mTextView = (TextView) contextView.findViewById(R.id.textview);



		
		mTextView.setText(title);
		
		return contextView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here
          //  mTextView.setText(title);
        }
//        else{
//            // fragment is no longer visible
//        }
    }

}