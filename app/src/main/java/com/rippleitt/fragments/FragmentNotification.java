package com.rippleitt.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.activities.ActivityListingFaq;
import com.rippleitt.activities.ActivityMyProductDetails;
import com.rippleitt.activities.ProductDetailsActivity;
import com.rippleitt.adapters.MessagesListAdapter;
import com.rippleitt.adapters.NotificationListAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.MyListingDetailsPayload;
import com.rippleitt.modals.NotificationTemplate;
import com.rippleitt.modals.NotificationsResponseTemplate;
import com.rippleitt.modals.ProductDetailsResponseTemplate;
import com.rippleitt.modals.SystemBroadcastTemplate;
import com.rippleitt.utils.CommonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manishautomatic on 05/03/18.
 */

public class FragmentNotification extends Fragment {

    // ListView mlistVwNotifications;
    NotificationListAdapter notificationListAdapter;
    ImageView mimgvwtoggleMap;
    ImageView mimgvwInitFilter;
    TextView mtxtVwTitileFragments;
    private TextView mtxtVwNoNotifications;
    private AVLoadingIndicatorView mLoaderNotifications;
    private LinearLayout mLinLytNotificationContainer;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_notification, container, false);
        // mlistVwNotifications=(ListView)view.findViewById(R.id.listVwNotifications);
        work(view);
        return view;
    }

    public void work(View view) {
        mtxtVwNoNotifications = (TextView) view.findViewById(R.id.txtvwNoNotifications);
        mtxtVwNoNotifications.setVisibility(View.GONE);
        mLoaderNotifications = (AVLoadingIndicatorView) view.findViewById(R.id.aviLoaderMyNotifications);
        mimgvwtoggleMap = (ImageView) ((ActivityHome) getActivity()).findViewById(R.id.imgvwtoggleMap);
        mimgvwtoggleMap.setVisibility(View.GONE);
        mimgvwInitFilter = (ImageView) ((ActivityHome) getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.GONE);
        mtxtVwTitileFragments = (TextView) ((ActivityHome) getActivity()).findViewById(R.id.txtVwTitileFragments);
        mLinLytNotificationContainer = (LinearLayout) view.findViewById(R.id.lstVwNotificationContainer);
        mtxtVwTitileFragments.setText("Notifications");
    }

    @Override
    public void onResume() {
        super.onResume();
        volleyFetchUserNotification();
        ;
    }

    private void volleyFetchUserNotification() {
        mLoaderNotifications.setVisibility(View.VISIBLE);
        mtxtVwNoNotifications.setVisibility(View.GONE);
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCH_USER_NOTIFICATION,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            NotificationsResponseTemplate notifications = new NotificationsResponseTemplate();
                            notifications = (NotificationsResponseTemplate) new Gson().fromJson(response, NotificationsResponseTemplate.class);
                            if (notifications.getResponse_code().equalsIgnoreCase("1")) {
                                RippleittAppInstance.getInstance().setUSER_NOTIFICATIONS(notifications.getData());
                                updateList(notifications.getData(), notifications.getSystem_broadcasts());
                                volleyUpdateReadStatus();
                            } else {
                                mtxtVwNoNotifications.setVisibility(View.VISIBLE);
                                mLoaderNotifications.setVisibility(View.GONE);
                            }
                            mLoaderNotifications.setVisibility(View.GONE);
                        } catch (Exception e) {
                            mtxtVwNoNotifications.setVisibility(View.VISIBLE);
                            mLoaderNotifications.setVisibility(View.GONE);
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

    private void updateList(NotificationTemplate[] notifications, SystemBroadcastTemplate[] systemBroadcasts) {
        mLinLytNotificationContainer.removeAllViews();
        if (notifications.length == 0 && systemBroadcasts.length == 0) {
            mtxtVwNoNotifications.setVisibility(View.VISIBLE);
        } else {
            mtxtVwNoNotifications.setVisibility(View.GONE);
        }

        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View heaerBroadcast = LayoutInflater.from(getActivity()).inflate(R.layout.layout_notification_header, null);
        TextView mtxtVwTitle = (TextView) heaerBroadcast.findViewById(R.id.txtvwHeaderTitle);
        mtxtVwTitle.setText("Broadcasts");
        mLinLytNotificationContainer.addView(heaerBroadcast);
        // now we populate the admin broadcasts...!
        for (SystemBroadcastTemplate systemBroadcast : systemBroadcasts) {
            View currentBroadCastView = LayoutInflater.from(getActivity()).inflate(R.layout.notification_listing_view, null);
            TextView txtvwNotificationsText = (TextView) currentBroadCastView.findViewById(R.id.txtVwUserNameNotification);
            TextView txtvwTimestamp = (TextView) currentBroadCastView.findViewById(R.id.txtvwTimeStamp);
            txtvwNotificationsText.setText(systemBroadcast.getMessage());
            txtvwTimestamp.setText(systemBroadcast.getTimestamp());
            mLinLytNotificationContainer.addView(currentBroadCastView);
        }
        View heaerNotification = LayoutInflater.from(getActivity()).inflate(R.layout.layout_notification_header, null);
        TextView mtxtVwTitleNotirication = (TextView) heaerNotification.findViewById(R.id.txtvwHeaderTitle);

        FrameLayout blank = new FrameLayout(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40);
        blank.setLayoutParams(lp);
        blank.setBackgroundColor(Color.WHITE);
        mLinLytNotificationContainer.addView(blank);
        mtxtVwTitleNotirication.setText("Notifications");
        mLinLytNotificationContainer.addView(heaerNotification);

        // now we populate the system broadcasts...!
        for (final NotificationTemplate systemBroadcast : notifications) {
            View currentBroadCastView = LayoutInflater.from(getActivity()).inflate(R.layout.notification_listing_view, null);
            TextView txtvwNotificationsText = (TextView) currentBroadCastView.findViewById(R.id.txtVwUserNameNotification);
            TextView txtvwTimestamp = (TextView) currentBroadCastView.findViewById(R.id.txtvwTimeStamp);
            txtvwNotificationsText.setText(systemBroadcast.getMessage());
            txtvwTimestamp.setText(systemBroadcast.getTime());
            currentBroadCastView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (systemBroadcast.getType().equalsIgnoreCase("1")) {
                        // make offer...
                        String listingID = systemBroadcast.getListing_id();
                        volleyFetchProductDetails(listingID);
                        //startActivity(new Intent(getActivity(),ActivityMyProductDetails.class));
                    }
                    if (systemBroadcast.getType().equalsIgnoreCase("business")) {
                        // offer accepted..
                        ActivityHome homeInstance = (ActivityHome) getActivity();
                        homeInstance.loadMyOrders();
                    }
                    if (systemBroadcast.getType().equalsIgnoreCase("password")) {
                        // received question...
                        RippleittAppInstance.getInstance().setListingFaqMode(1);
                        RippleittAppInstance
                                .getInstance()
                                .getSELECTED_LISTING_DETAIL_OBJECT().setListing_id(systemBroadcast.getListing_id());
                        startActivity(new Intent(getActivity(), ActivityListingFaq.class));
                    }
                    if (systemBroadcast.getType().equalsIgnoreCase("mail")) {
                        // received answer
                        RippleittAppInstance.getInstance().setListingFaqMode(0);
                        RippleittAppInstance
                                .getInstance()
                                .getSELECTED_LISTING_DETAIL_OBJECT().setListing_id(systemBroadcast.getListing_id());
                        startActivity(new Intent(getActivity(), ActivityListingFaq.class));
                    }
                    if (systemBroadcast.getType().equalsIgnoreCase("user")) {
                        // order confirmed
                        ActivityHome homeInstance = (ActivityHome) getActivity();
                        homeInstance.loadMyOrders();
                    }
                    if (systemBroadcast.getType().equalsIgnoreCase("6")) {
                        // simillar product
                        RippleittAppInstance.getInstance().setCURRENT_SELECTED_LISTING_ID(systemBroadcast.getListing_id());
                        volleyFetchItemDetails(systemBroadcast.getListing_id());

                    }
                    if (systemBroadcast.getType().equalsIgnoreCase("7")) {
                        // listing approved...
                        ActivityHome homeInstance = (ActivityHome) getActivity();
                        homeInstance.loadMyListings();
                    }
                    if (systemBroadcast.getType().equalsIgnoreCase("8")) {
                        // listing approved...
                        ActivityHome homeInstance = (ActivityHome) getActivity();
                        homeInstance.loadMyOrders();
                    }
//                    if (systemBroadcast.getType().equalsIgnoreCase("9")) {
//                        // listing approved...
//                        ActivityHome homeInstance = (ActivityHome) getActivity();
//                        homeInstance.loadMyDisputes();
//                    }
//                    if (systemBroadcast.getType().equalsIgnoreCase("10")) {
//                        // listing approved...
//                        ActivityHome homeInstance = (ActivityHome) getActivity();
//                        homeInstance.loadMyDisputes();
//                    }
                    if (systemBroadcast.getType().equalsIgnoreCase("11")) {
                        // listing approved...
                        ActivityHome homeInstance = (ActivityHome) getActivity();
                        homeInstance.loadMyDisputes();
                    }
                    if (systemBroadcast.getType().equalsIgnoreCase("12")) {
                        // listing approved...
                        ActivityHome homeInstance = (ActivityHome) getActivity();
                        homeInstance.loadMyDisputes();
                    }
                    if (systemBroadcast.getType().equalsIgnoreCase("13")) {
                        // listing approved...
                        ActivityHome homeInstance = (ActivityHome) getActivity();
                        homeInstance.loadMyDisputes();
                    }
                    if (systemBroadcast.getType().equalsIgnoreCase("14")) {
                        // listing approved...
                        ActivityHome homeInstance = (ActivityHome) getActivity();
                        homeInstance.loadMyOrders();
                    }
                    if (systemBroadcast.getType().equalsIgnoreCase("15")) {
                        // listing approved...
                        ActivityHome homeInstance = (ActivityHome) getActivity();
                        homeInstance.loadMyOrders();
                    }if (systemBroadcast.getType().equalsIgnoreCase("21")) {
                        // listing approved...
                        ActivityHome homeInstance = (ActivityHome) getActivity();
                        homeInstance.loadMyUnlockedVouchers();
                    }
                }
            });
            mLinLytNotificationContainer.addView(currentBroadCastView);
        }
    }


    // get user listing details...
    private void loadMyListing(int index) {
        RippleittAppInstance
                .getInstance()
                .setCURRENT_SELECTED_LISTING_ID(RippleittAppInstance
                        .getInstance().getUSER_NOTIFICATIONS()[index].getListing_id());
        Toast.makeText(getActivity(), "Getting listing details...", Toast.LENGTH_SHORT).show();

        String listingID = RippleittAppInstance
                .getInstance().getUSER_NOTIFICATIONS()[index].getListing_id();

        //startActivity(new Intent(getActivity(),ActivityMyProductDetails.class));
        volleyFetchProductDetails(listingID);
    }

    private void volleyFetchProductDetails(final String _ID) {
        final ProgressDialog mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Getting listing details...");
        mDialog.setCancelable(false);
        mDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance
                .getInstance().LISTING_DETAILS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        MyListingDetailsPayload payload =
                                (MyListingDetailsPayload) gson.fromJson(response, MyListingDetailsPayload.class);
                        if (payload.getResponse_code().equalsIgnoreCase("1")) {
                            RippleittAppInstance
                                    .getInstance()
                                    .setMY_CURRENT_LISTING(payload.getData());
                            startActivity(new Intent(getActivity(), ActivityMyProductDetails.class));
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                Toast.makeText(getActivity(), "could not fetch listing details, please try again", Toast.LENGTH_LONG).show();
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
                params.put("listing_id", _ID);

                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }

    private void volleyFetchItemDetails(final String listingID) {
        final ProgressDialog mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Getting listing details...");
        mDialog.setCancelable(false);
        mDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.BASE_URL
                        + RippleittAppInstance.FETCHING_PRODUCT_DETAILS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();
                        try {
                            Gson g = new Gson();
                            ProductDetailsResponseTemplate productDetails =
                                    (ProductDetailsResponseTemplate) g.fromJson(response, ProductDetailsResponseTemplate.class);
                            RippleittAppInstance.getInstance().setSELECTED_LISTING_DETAIL_OBJECT(productDetails.getData());
                            //shouldTriggerRecent=false;
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


    // mark the notifications as read..
    private void volleyUpdateReadStatus() {
        mLoaderNotifications.setVisibility(View.VISIBLE);
        //mtxtVwNoNotifications.setVisibility(View.GONE);
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.MARK_AS_READ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            NotificationsResponseTemplate notifications = new NotificationsResponseTemplate();
                            notifications = (NotificationsResponseTemplate) new Gson().fromJson(response, NotificationsResponseTemplate.class);
                            if (notifications.getResponse_code().equalsIgnoreCase("1")) {
                                //RippleittAppInstance.getInstance().setUSER_NOTIFICATIONS(notifications.getData());
                                //updateList(notifications.getData(),notifications.getSystem_broadcasts());
                            } else {
                                //mtxtVwNoNotifications.setVisibility(View.VISIBLE);
                                //mLoaderNotifications.setVisibility(View.GONE);
                            }
                            mLoaderNotifications.setVisibility(View.GONE);
                        } catch (Exception e) {
                            mtxtVwNoNotifications.setVisibility(View.VISIBLE);
                            mLoaderNotifications.setVisibility(View.GONE);
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


}
