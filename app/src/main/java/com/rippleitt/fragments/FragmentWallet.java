package com.rippleitt.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.activities.ActivityAddContacts;
import com.rippleitt.activities.ActivityBankAccount;
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.activities.ActivityMyProductDetails;
import com.rippleitt.activities.ProductDetailsActivity;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CategoryListTemplate;
import com.rippleitt.modals.DashboardTemplate;
import com.rippleitt.modals.DisbursalResponceTemplate;
import com.rippleitt.modals.ProductDetailsResponseTemplate;
import com.rippleitt.modals.ReferralUsers;
import com.rippleitt.modals.UserWalletResponseTemplate;
import com.rippleitt.modals.WalletObject;
import com.rippleitt.modals.WalletObjectTemplate;
import com.rippleitt.modals.WalletOrder;
import com.rippleitt.modals.WalletOrderDetail;
import com.rippleitt.modals.WalletTemplate;
import com.rippleitt.utils.CommonUtils;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by manishautomatic on 05/03/18.
 */

public class FragmentWallet extends Fragment {

    ImageView mimgvwtoggleMap;
    ImageView mimgvwInitFilter;
    TextView mtxtVwTitileFragments;
    TextView mTxtVwWalletBalance;
    LinearLayout mLnLytWalletData;
    TextView mTxtVwNoReferal, tvDisburse;

    Button mbtnAddNewContact;
    AVLoadingIndicatorView mLoaderWallet;
    float totalAmount;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        //  View view = inflater.inflate(R.layout.layout_fragment_wallet, container, false);

        View view = inflater.inflate(R.layout.layout_fragment_wallet, container, false);

        work();
        mimgvwtoggleMap = (ImageView) ((ActivityHome) getActivity()).findViewById(R.id.imgvwtoggleMap);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter = (ImageView) ((ActivityHome) getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        mtxtVwTitileFragments = (TextView) ((ActivityHome) getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("Wallet");

        initUI(view);

        return view;
    }

    public void work() {

    }

    private void initUI(View view) {
        mTxtVwNoReferal = (TextView) view.findViewById(R.id.txtvwNoWallet);
        mTxtVwNoReferal.setVisibility(View.GONE);
        mTxtVwWalletBalance = (TextView) view.findViewById(R.id.txtvwWalletBalance);
        tvDisburse = (TextView) view.findViewById(R.id.tvDisburse);
        mLnLytWalletData = (LinearLayout) view.findViewById(R.id.walletDataContainer);
        mLoaderWallet = (AVLoadingIndicatorView) view.findViewById(R.id.aviLoaderWallet);

        tvDisburse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDisburseDialog();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchVolleyWalletData();
    }

    private void fetchVolleyWalletData() {
        mLoaderWallet.setVisibility(View.VISIBLE);

        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCH_USER_WALLET,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (isVisible()) {
                            mLoaderWallet.setVisibility(View.GONE);
                            try {
                                UserWalletResponseTemplate wallet = new UserWalletResponseTemplate();
                                wallet = (UserWalletResponseTemplate) new Gson().fromJson(response, UserWalletResponseTemplate.class);

                                if (wallet.getResponse_code().equalsIgnoreCase("1")) {
                                    totalAmount = Float.parseFloat(wallet.getWallet_balance());
                                    mTxtVwWalletBalance.setText("$" + String.format("%.2f", totalAmount));
                                    if (wallet.getData().length == 0) {
                                        mTxtVwWalletBalance.setVisibility(View.VISIBLE);
                                        mTxtVwNoReferal.setVisibility(View.VISIBLE);
                                    }

                                    populateWallet(wallet.getData());
                                } else {
                                    Toast.makeText(getActivity(),
                                            "could not update your wallet, please try again",
                                            Toast.LENGTH_LONG).show();

                                }

//                                if (wallet.getResponse_code().equalsIgnoreCase("1")) {
//                                    totalAmount = Float.parseFloat(wallet.getWallet_balance());
//                                    mTxtVwWalletBalance.setText("$" + String.format("%.2f",totalAmount));
//                                    if (wallet.getData().length == 0 && wallet.getOrders().length==0) {
//                                        mTxtVwWalletBalance.setVisibility(View.VISIBLE);
//                                        mTxtVwNoReferal.setVisibility(View.VISIBLE);
//                                    }
//
//                                    populateReferralChain(wallet.getData(), wallet.getOrders());
//                                } else {
//                                    Toast.makeText(getActivity(),
//                                            "could not update your wallet, please try again",
//                                            Toast.LENGTH_LONG).show();
//
//                                }

                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "could not update your wallet, please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "could not fetch your address book, please try again", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                com.rippleitt.utils.CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String token = PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, "");
                params.put("token", token);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");

    }

//    private void populateReferralChain(WalletObject[] data, WalletOrder[] order) {
//        mLnLytWalletData.removeAllViews();
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//
//        Map<Date, WalletTemplate> walletHashMap =new TreeMap<>();
//
//        for (WalletObject object : data) {
//            WalletTemplate template = new WalletTemplate();
//            template.setData(object);
//
//            String strDate = object.getDate();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = null;
//            try {
//                date = dateFormat.parse(strDate);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            System.out.println(date);
//
//            walletHashMap.put(date, template);
//        }
//
//        for (WalletOrder object : order) {
//            WalletTemplate template = new WalletTemplate();
//
//            String strDate = object.getDate();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = null;
//            try {
//                date = dateFormat.parse(strDate);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            System.out.println(date);
//
//            if (walletHashMap.containsKey(date)) {
//                template = walletHashMap.get(date);
//            }
////            for (WalletObject objectData : data) {
////                if (object.getDate().equals(objectData.getDate())){
////                    walletHashMap.remove(date);
////                    template.setData(objectData);
////                }
////            }
//            template.setOrders(object);
//            walletHashMap.put(date, template);
//        }
//
//
//        Map<Date, WalletTemplate> walletMap =new TreeMap<>(new Comparator<Date>() {
//            @Override
//            public int compare(Date d1, Date d2) {
//                if (d1.after(d2)){
//                    return -1;
//                }else if (d1 == d2){
//                    return 0;
//                }else {
//                    return 1;
//                }
////                return d1.after(d2) ? -1 : 1;
//            }
//        });
//        walletMap.putAll(walletHashMap);
//
//        for (WalletTemplate walletTemplate : walletMap.values()) {
//
//            WalletObject walletObject = walletTemplate.getData();
//            WalletOrder walletOrder = walletTemplate.getOrders();
//            if (walletObject != null) {
//                View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.layour_wallet_row_date, null);
//                TextView txtvwDisbursalDate = (TextView) titleView.findViewById(R.id.txtvwDisbursalDate);
//                txtvwDisbursalDate.setText(walletObject.getDate());
//                mLnLytWalletData.addView(titleView);
//            } else if (walletOrder != null) {
//                View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.layour_wallet_row_date, null);
//                TextView txtvwDisbursalDate = (TextView) titleView.findViewById(R.id.txtvwDisbursalDate);
//                txtvwDisbursalDate.setText(walletOrder.getDate());
//                mLnLytWalletData.addView(titleView);
//            }
//            if (walletObject != null) {
//
//                // now add the children...
//                for (final ReferralUsers user : walletObject.getUsers()) {
//                    View childView = LayoutInflater.from(getActivity()).inflate(R.layout.row_referral_chain_row, null);
//                    CircleImageView profilePic = (CircleImageView) childView.findViewById(R.id.imgvwRefreeImage);
//                    try {
//                        Picasso.with(getActivity())
//                                .load(RippleittAppInstance.formatPicPath(
//                                        user.getListing_image()))
//                                .placeholder(R.drawable.default_profile_icon)
//                                .error(R.drawable.default_profile_icon)
//                                .into(profilePic);
//                    } catch (Exception e) {
//                        profilePic.setImageResource(R.drawable.default_profile_icon);
//                    }
//                    TextView txtvwName = (TextView) childView.findViewById(R.id.txtvwReferredTo);
//                    TextView txtvwAmount = (TextView) childView.findViewById(R.id.txtvwRewardEarned);
//                    TextView tvType = (TextView) childView.findViewById(R.id.tvType);
//                    FrameLayout frmlytOrderFlagColor = childView.findViewById(R.id.frmlytOrderFlagColor);
//                    frmlytOrderFlagColor.setBackgroundColor(Color.parseColor("#65b488"));
//                    tvType.setText("Refferal");
//                    txtvwName.setText(user.getListing_name());
//                    txtvwAmount.setText("$" + String.format("%.2f",Float.parseFloat(user.getAmount())));
//                    childView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            volleyFetchItemDetails(user.getListing_id());
//                        }
//                    });
//                    mLnLytWalletData.addView(childView);
//                }
//            }
//            if (walletOrder != null) {
//
//                // now add the children...
//                for (final WalletOrderDetail detail : walletOrder.getDetails()) {
//                    View childView = LayoutInflater.from(getActivity()).inflate(R.layout.row_referral_chain_row, null);
//                    CircleImageView profilePic = (CircleImageView) childView.findViewById(R.id.imgvwRefreeImage);
//                    try {
//                        Picasso.with(getActivity())
//                                .load(RippleittAppInstance.formatPicPath(
//                                        detail.getListingImage()))
//                                .placeholder(R.drawable.default_profile_icon)
//                                .error(R.drawable.default_profile_icon)
//                                .into(profilePic);
//                    } catch (Exception e) {
//                        profilePic.setImageResource(R.drawable.default_profile_icon);
//                    }
//                    TextView txtvwName = (TextView) childView.findViewById(R.id.txtvwReferredTo);
//                    TextView txtvwAmount = (TextView) childView.findViewById(R.id.txtvwRewardEarned);
//                    TextView tvType = (TextView) childView.findViewById(R.id.tvType);
//                    FrameLayout frmlytOrderFlagColor = childView.findViewById(R.id.frmlytOrderFlagColor);
//                    frmlytOrderFlagColor.setBackgroundColor(Color.parseColor("#77a9ff"));
//                    tvType.setText("Sell");
//                    txtvwName.setText(detail.getListingName());
//                    txtvwAmount.setText("$" + String.format("%.2f",Float.parseFloat(detail.getAmount())));
//                    childView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            volleyFetchItemDetails(detail.getListingId());
//                        }
//                    });
//                    mLnLytWalletData.addView(childView);
//                }
//            }
//
////        for(WalletObject walletObject :data){
//
////                }
////
////            }
//        }
//    }

    private void populateWallet(WalletObjectTemplate[] data) {
        mLnLytWalletData.removeAllViews();
//        LayoutInflater inflater = LayoutInflater.from(getActivity());


        for (WalletObjectTemplate walletTemplate : data) {

            if (walletTemplate != null) {

                // now add the children...
                View childView = LayoutInflater.from(getActivity()).inflate(R.layout.row_referral_chain_row, null);

                TextView txtvwReferredTo = (TextView) childView.findViewById(R.id.txtvwReferredTo);
                TextView txtvwDate = (TextView) childView.findViewById(R.id.txtvwDate);
                TextView txtvwAmount = (TextView) childView.findViewById(R.id.txtvwRewardEarned);
                TextView tvType = (TextView) childView.findViewById(R.id.tvType);
                FrameLayout frmlytOrderFlagColor = childView.findViewById(R.id.frmlytOrderFlagColor);
                txtvwReferredTo.setText(walletTemplate.getNarration());
                txtvwDate.setText(walletTemplate.getTimestamp());
//                    txtvwDate.setText(walletTemplate.getTimestamp().split(" ")[0]);

                if (walletTemplate.getCredit()!=null && !walletTemplate.getCredit().equals("") && !walletTemplate.getCredit().equalsIgnoreCase("0") ) {
                    txtvwAmount.setText("$" + String.format("%.2f", Float.parseFloat(walletTemplate.getCredit())));
                    frmlytOrderFlagColor.setBackgroundColor(Color.parseColor("#77a9ff"));
                    tvType.setText("Credit");

                }
                if (walletTemplate.getDebit()!=null && !walletTemplate.getDebit().equals("") && !walletTemplate.getDebit().equalsIgnoreCase("0") ) {
                    txtvwAmount.setText("$" + String.format("%.2f", Float.parseFloat(walletTemplate.getDebit())));
                    frmlytOrderFlagColor.setBackgroundColor(Color.parseColor("#65b488"));
                    tvType.setText("Debit");

                }
//                childView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        volleyFetchItemDetails(user.getListing_id());
//                    }
//                });

                mLnLytWalletData.addView(childView);
            }


        }
    }


    private void volleyFetchItemDetails(final String listingID) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(true);
        pDialog.setMessage("Getting Listing details...");
        pDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.BASE_URL
                        + RippleittAppInstance.FETCHING_PRODUCT_DETAILS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        try {
                            Gson g = new Gson();
                            ProductDetailsResponseTemplate productDetails =
                                    (ProductDetailsResponseTemplate) g.fromJson(response, ProductDetailsResponseTemplate.class);
                            RippleittAppInstance.getInstance().setSELECTED_LISTING_DETAIL_OBJECT(productDetails.getData());

                            startActivity(new Intent(getActivity(), ProductDetailsActivity.class));
                            RippleittAppInstance.getInstance()
                                    .setCURRENT_SELECTED_LISTING_ID(RippleittAppInstance
                                            .getInstance()
                                            .getSELECTED_LISTING_DETAIL_OBJECT().getListing_id());
                        } catch (Exception e) {
                            Toast.makeText(getActivity(),
                                    "could not fetch listing details",
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "could not fetch categories, please try again",
                        Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(getActivity(),
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("listing_id", listingID);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }

    private void showDisburseDialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_disburse_amount);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        @Bind(R.id.btnYes)
//        Button btnYes;
        Button btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        TextView tvTotalAmount = (TextView) dialog.findViewById(R.id.tvTotalAmount);
        final EditText edAmount = (EditText) dialog.findViewById(R.id.edAmount);

        tvTotalAmount.setText("Available Amount: $" + totalAmount);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                GeneralFunctions.apiRegisterDevice(userDetails.getUserId(),false);
                if (TextUtils.isEmpty(edAmount.getText())) {
                    Toast.makeText(getContext(), "Please add amount to be disbursed", Toast.LENGTH_SHORT).show();

                } else if (Float.parseFloat(edAmount.getText().toString()) > totalAmount) {
                    Toast.makeText(getContext(), "Insufficient Amount", Toast.LENGTH_SHORT).show();

                } else if (Float.parseFloat(edAmount.getText().toString()) < 100.00) {
                    Toast.makeText(getContext(), "Disbursal amount cannot be less than $100", Toast.LENGTH_SHORT).show();

                } else {
                    volleyCallDisburse(edAmount.getText().toString());
                }
            }
        });
        dialog.show();
    }

    private void volleyCallDisburse(final String s) {
        final ProgressDialog mPDialog;
        mPDialog = new ProgressDialog(getContext());
        mPDialog.setMessage("Submitting your disbursal");
        mPDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.POST_DISBURSE,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mPDialog.dismiss();

                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        DisbursalResponceTemplate response_ = gson.fromJson(response, DisbursalResponceTemplate.class);
                        if (response_.getResponse_code() == 1) {
                            dialog.dismiss();

                            if (response_.getBank_account() == 1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Your disbursal request is submitted successfully")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things\
                                                ActivityHome homeInstance = (ActivityHome) getActivity();
                                                homeInstance.loadMyDisbursals();
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                startActivity(new Intent(getActivity(), ActivityBankAccount.class));

                            }
                        } else {
                            if (response_.getBank_account() == 1) {
                                Toast.makeText(getContext(), response_.getResponse_message(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            } else {
                                startActivity(new Intent(getActivity(), ActivityBankAccount.class));

                            }
                            // show response_message in alert...
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPDialog.dismiss();
                Toast.makeText(getContext(), "could not submit your response, please try again", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(getContext(),
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("amount", s);

                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "account_details");


    }
}
