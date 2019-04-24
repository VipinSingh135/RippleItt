package com.rippleitt.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rippleitt.R;
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.adapters.ReviewListAdapter;
import com.rippleitt.controller.RippleittAppInstance;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by manishautomatic on 05/03/18.
 */

public class FragmentProfile extends Fragment {

    ReviewListAdapter reviewListAdapter;
    ListView mlistVwReviewsProfile;
    ImageView mimgvwtoggleMap;
    TextView mtxtVwTitileFragments;
    private CircleImageView mCrclImgVwUserProfilePic;
    private TextView mTxtVwUserName;
    private TextView mTxtEmail;
    ImageView mimgvwInitFilter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_profile, container, false);
        initUI(view);

        work();
        loadUI();
        return view;
    }

    public void work(){
        reviewListAdapter=new ReviewListAdapter(getActivity(),null);
        mlistVwReviewsProfile.setAdapter(reviewListAdapter);

        mimgvwtoggleMap=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwtoggleMap);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        mtxtVwTitileFragments=(TextView) ((ActivityHome)getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("Profile");

    }


    private void initUI(View view){
        mlistVwReviewsProfile=(ListView)view.findViewById(R.id.listVwReviewsProfile);
        mlistVwReviewsProfile.setVisibility(View.INVISIBLE);
        mCrclImgVwUserProfilePic=(CircleImageView) view.findViewById(R.id.imgVwprofileImage);
        mTxtVwUserName=(TextView)view.findViewById(R.id.txtVwUserNameProfile);
        mTxtEmail=(TextView)view.findViewById(R.id.txtVwUserEmailProfile);
    }


    private void loadUI(){
        try{
            SharedPreferences sharedPreferences=getActivity()
                    .getSharedPreferences("preferences", Context.MODE_PRIVATE);
            mTxtVwUserName.setText(sharedPreferences.getString("user_name",""));
            mTxtEmail.setText(sharedPreferences.getString("email",""));

            if(sharedPreferences.getString("image","").equalsIgnoreCase("")
                    ||
                    sharedPreferences.getString("image","").equalsIgnoreCase("null")
                    ){
                mCrclImgVwUserProfilePic.setImageResource(R.drawable.default_profile_icon);
            }else{
                Picasso.with(getActivity())
                        .load(RippleittAppInstance
                                .formatPicPath(sharedPreferences.getString("image","")))
                        .into(mCrclImgVwUserProfilePic);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
