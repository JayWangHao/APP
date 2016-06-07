package com.cattsoft.framework.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cattsoft.framework.R;
import com.cattsoft.framework.models.SysUser;
import com.cattsoft.framework.view.LabelText;

/**
 * 个人信息
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment {

    private static final String ARG_SYSUSER = "sysUser";

    private SysUser mSysUser;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PersonalFragment.
     */
    public static PersonalFragment newInstance(SysUser sysUser) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SYSUSER, sysUser);
        fragment.setArguments(args);
        return fragment;
    }

    public PersonalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = this.getArguments();
            mSysUser = (SysUser) bundle.getSerializable(ARG_SYSUSER);
        }
        mSysUser = SysUser.parse(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_personal, container, false);

        TextView mHeadName = (TextView) rootView.findViewById(R.id.tv_personal_name);
//        LabelText mPersonalName = (LabelText) rootView.findViewById(R.id.lt_personal_name);
//        LabelText mLoginName = (LabelText) rootView.findViewById(R.id.lt_personal_login_name);
//        LabelText mOrgName = (LabelText) rootView.findViewById(R.id.lt_personal_org_name);
//        LabelText mTelNbr = (LabelText) rootView.findViewById(R.id.lt_personal_telnbr);
//        LabelText mMailbox = (LabelText) rootView.findViewById(R.id.lt_personal_mailbox);


        TextView mPersonalName = (TextView) rootView.findViewById(R.id.lt_personal_name);
        TextView mLoginName = (TextView) rootView.findViewById(R.id.lt_personal_login_name);
        TextView mOrgName = (TextView) rootView.findViewById(R.id.lt_personal_org_name);
        TextView mTelNbr = (TextView) rootView.findViewById(R.id.lt_personal_telnbr);
        TextView mMailbox = (TextView) rootView.findViewById(R.id.lt_personal_mailbox);

//       // mHeadName.setText(mSysUser.getName());
//        mPersonalName.setValue(mSysUser.getName());
//        mLoginName.setValue(mSysUser.getLoginName());
//        mOrgName.setValue(mSysUser.getOrgName());
//        mTelNbr.setValue(mSysUser.getTelNbr());
//        mMailbox.setValue(mSysUser.getMailBox());

        mHeadName.setText(mSysUser.getName());
        mPersonalName.setText(mSysUser.getName());
        mLoginName.setText(mSysUser.getLoginName());
        mOrgName.setText(mSysUser.getOrgName());
        mTelNbr.setText(mSysUser.getTelNbr());
        mMailbox.setText(mSysUser.getMailBox());
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}