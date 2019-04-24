package com.rippleitt.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.activities.ActivityBankAccount;
import com.rippleitt.activities.ActivityChangePassword;
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.activities.ActivityUpdatePaymentMethod;
import com.rippleitt.activities.ActivityUpdateProfile;
import com.rippleitt.commonUtilities.PreferenceHandler;

/**
 * Created by manishautomatic on 05/03/18.
 */

public class FragmentSettings extends Fragment implements View.OnClickListener{

    ImageView mimgvwtoggleMap;
    ImageView mimgvwInitFilter;
    TextView mtxtVwTitileFragments;
    TextView tvAccount;
    RelativeLayout mrelUpdateProfile;
    RelativeLayout mrelChangePass;
    RelativeLayout mrelUpdatePaymentMethod;
    RelativeLayout mrelUpdateAccountDetails;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_settings, container, false);
        mimgvwtoggleMap=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwtoggleMap);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        mtxtVwTitileFragments=(TextView) ((ActivityHome)getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("Settings");

        mrelUpdateProfile=(RelativeLayout)view.findViewById(R.id.relUpdateProfile);
        mrelChangePass=(RelativeLayout)view.findViewById(R.id.relChangePass);
        mrelUpdatePaymentMethod=(RelativeLayout)view.findViewById(R.id.relUpdatePayment);
        mrelUpdateAccountDetails=(RelativeLayout)view.findViewById(R.id.relUpdateAccount);
        tvAccount=(TextView) view.findViewById(R.id.tvAccount);
        mrelUpdateProfile.setOnClickListener(this);
        mrelUpdateAccountDetails.setOnClickListener(this);
        mrelChangePass.setOnClickListener(this);
        mrelUpdatePaymentMethod.setOnClickListener(this);


        return view;

    }

    @Override
    public void onClick(View view) {
        if(view==mrelUpdatePaymentMethod){
            startActivity(new Intent(getActivity(),ActivityUpdatePaymentMethod.class));
        }else if (view==mrelUpdateProfile){
            startActivity(new Intent(getActivity(), ActivityUpdateProfile.class));
        }else if (view==mrelChangePass){
            startActivity(new Intent(getActivity(), ActivityChangePassword.class));
        }else if (view==mrelUpdateAccountDetails){
            startActivity(new Intent(getActivity(), ActivityBankAccount.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String account_num= PreferenceHandler.readString(getActivity(), PreferenceHandler.ACCOUNT_NUMBER,"");

        if (account_num==null || account_num.equals("")){
            tvAccount.setText("Add Bank Account Details");
        }else{
            tvAccount.setText("Update Bank Account Details");
        }
    }
}
