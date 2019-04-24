package com.rippleitt.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rippleitt.R;
import com.rippleitt.activities.ActivityAddVoucher;
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.controller.RippleittAppInstance;

import com.rippleitt.webservices.FetchMyVouchersApi;
import com.wang.avi.AVLoadingIndicatorView;

public class FragmentMyVouchers extends Fragment implements View.OnClickListener {

    ImageView mimgvwtoggleMap;
    ImageView mimgvwInitFilter;
    TextView mtxtVwTitileFragments;
    ListView mlistVouchers;
//    Button mBtnAddNewVouchers;
    AVLoadingIndicatorView mLoader;
    Button mBtnAddNewVouchersCenter;
    LinearLayout mLinLytCenterControl;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_my_vouchers, container, false);
        mimgvwtoggleMap=(ImageView)((ActivityHome)getActivity()).findViewById(R.id.imgvwtoggleMap);
        mLoader = (AVLoadingIndicatorView)view.findViewById(R.id.aviLoaderHomeCenter);
        mBtnAddNewVouchersCenter=(Button)view.findViewById(R.id.btnAddVouchersCenter);
        mLinLytCenterControl=(LinearLayout)view.findViewById(R.id.linlytCenter);
        mLinLytCenterControl.setVisibility(View.GONE);
        mBtnAddNewVouchersCenter.setOnClickListener(this);
        mLoader.setVisibility(View.GONE);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mtxtVwTitileFragments=(TextView) ((ActivityHome)getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("My Vouchers");
        mlistVouchers=(ListView)view.findViewById(R.id.listVouchers);

        mimgvwInitFilter = (ImageView) ((ActivityHome) getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.VISIBLE);
        mimgvwInitFilter.setImageDrawable(getActivity()
                .getResources().getDrawable(R.drawable.ic_add_circle_outline_black_24dp));
        mimgvwInitFilter.setColorFilter(Color.argb(255, 255, 255, 255));
        mimgvwInitFilter.setOnClickListener(this);

        mlistVouchers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        return view;
    }


    @Override
    public void onResume(){
        super.onResume();
        Toast.makeText(getActivity(),"Fetching Vouchers",Toast.LENGTH_LONG).show();
        mlistVouchers.setAdapter(null);
        new FetchMyVouchersApi().fetchMyVouchersApi(getContext(),"0", mLoader);
        mLinLytCenterControl.setVisibility(View.GONE);
//        mBtnAddNewVouchers.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        if(view==mBtnAddNewVouchersCenter || view== mimgvwInitFilter){
            startActivity(new Intent(getActivity(), ActivityAddVoucher.class));
        }
    }


//    private void volleyFetchProductDetails(){
//
//        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance
//                .getInstance().Vouchers_DETAILS ,
//                new com.android.volley.Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("", response.toString());
//                        Gson gson = new Gson();
//                        MyVouchersDetailsPayload payload =
//                                (MyVouchersDetailsPayload)gson.fromJson(response,MyVouchersDetailsPayload.class);
//                        if(payload.getResponse_code().equalsIgnoreCase("1")){
//                            RippleittAppInstance
//                                    .getInstance()
//                                    .setMY_CURRENT_Vouchers(payload.getData());
//                            startActivity(new Intent(getActivity(), ActivityMyProductDetails.class));
//                        }
//
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(),"could not fetch sub category, please try again",Toast.LENGTH_LONG).show();
//                VolleyLog.d("", "Error: " + error.getMessage());
//                Log.d("", ""+error.getMessage()+","+error.toString());
//                CommonUtils.dismissProgress();
//            }
//        }){
//            @Override
//            protected Map<String,String> getParams(){
//
//                Map<String, String> params = new HashMap<>();
//                params.put("token", PreferenceHandler.readString(getActivity(),
//                        PreferenceHandler.AUTH_TOKEN, ""));
//                params.put("Vouchers_id",RippleittAppInstance
//                        .getInstance().getCURRENT_SELECTED_Vouchers_ID());
//
//                return params;
//            }
//        };
//        sr.setShouldCache(false);
//        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
//    }


    @Override
    public void onPause(){
        super.onPause();
        RippleittAppInstance.getInstance().getRequestQueue().cancelAll("rippleitt");
    }

}
