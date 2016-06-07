package com.cattsoft.wow.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cattsoft.wow.R;
import com.cattsoft.wow.activity.AmendPasswordActivity;
import com.cattsoft.wow.activity.MainActivity;
import com.cattsoft.wow.service.PalpitateService;


public class LeftSlidingFragment extends Fragment {

    private Button changePassword;
    private Button logout;
    private Button about;
    private Context context;
    View view;


    @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_left_sliding,container,false);
        changePassword=(Button)view.findViewById(R.id.change_password_btn);
        logout=(Button)view.findViewById(R.id.logout_btn);
        about=(Button)view.findViewById(R.id.about_btn);
        changePassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AmendPasswordActivity.class);
                getActivity().startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentService=new Intent(getActivity(), PalpitateService.class);
                getActivity().stopService(intentService);//停止心跳服务

                Intent intent=new Intent(getActivity(),MainActivity.class);
                intent.putExtra("isLogout","Y");
                getActivity().startActivity(intent);
            }
        });
        return view;
    }



}
