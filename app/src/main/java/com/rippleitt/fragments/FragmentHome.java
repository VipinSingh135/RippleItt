package com.rippleitt.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;
import com.mugen.attachers.BaseAttacher;
import com.rippleitt.R;
import com.rippleitt.activities.ActivityFilterSelector;
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.activities.ProductDetailsActivity;
import com.rippleitt.adapters.HomeProductsAdapter;
import com.rippleitt.callback.EndlessRecyclerViewScrollListener;
import com.rippleitt.callback.HomeProductTapped;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.modals.ProductDetailsResponseTemplate;
import com.rippleitt.modals.SearchResultsPayload;
import com.rippleitt.utils.CommonUtils;
import com.sdsmdg.tastytoast.TastyToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by manishautomatic on 05/03/18. bug fixing demo
 */

public class FragmentHome extends Fragment implements
        HomeProductTapped, View.OnClickListener,
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //private ImageView mimgvwtoggleMap;
    private ImageView mimgvwInitFilter;
    private ImageView mimgVwClearSearch;
    private TextView mtxtVwTitileFragments;
    private AVLoadingIndicatorView maviLoaderHomeCenter, maviLoaderHomeBottom;
    private EditText medtxtQuickSearch;
    private int status_filter = 1;
    private RelativeLayout mRelLytSearchBox;
    private ImageView mimgvwtoggleSearch;
    private HomeProductsAdapter homeProductsAdapter;
    private final int RESULT_MODE_PRODUCT = 1;
    private final int RESULT_MODE_SERVICES = 2;
    private int CURRENT_RESULT_MODE = 1;
    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView recyclerView;
    private boolean shouldTriggerRecent = true;
    private LinearLayout mLinLytProducts, mLinLytServices;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private BaseAttacher attacher;
    private MapView mMapView;
    private GoogleMap mGglMpInstance;
    private RoundedImageView mRndImgVwInfoWindowProductPic;
    private TextView mtxtVwIwTitle, mtxtVwIwDescription, infwnOwnerName,
            infwnPrice, infwnReward;
    private CircleImageView infwOwnerPic;
    private ImageView mImgVwHideInfoWindow;
    private LinearLayout mLinLytInfoWindowContainer;
    private LinearLayout mLinLytInfoClickHandler;
    private String listing_id_to_load = "";
    private LinearLayout mLinlytTabContainer;
    private TextView mTxtVwNoResults;
    private int CURRENT_SEARCH_MODE = 0;
    private final int SEARCH_MODE_RECENT = 1;
    private final int SEARCH_MODE_CRITERIA = 2;


    //private MapView mMapView;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("frag_home", "on-create-called");
        resetSearchCounters();
        View view = inflater.inflate(R.layout.layout_fragment_home, container, false);

        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        maviLoaderHomeCenter = (AVLoadingIndicatorView) view.findViewById(R.id.aviLoaderHomeCenter);
        maviLoaderHomeBottom = (AVLoadingIndicatorView) view.findViewById(R.id.aviLoaderHomeBottom);
        mLinlytTabContainer = (LinearLayout) view.findViewById(R.id.linlytTabContainer);
        maviLoaderHomeCenter.setVisibility(View.GONE);
        maviLoaderHomeBottom.setVisibility(View.GONE);
        medtxtQuickSearch = (EditText) view.findViewById(R.id.edtxtQuickSearch);
        mimgVwClearSearch = (ImageView) view.findViewById(R.id.imgVwClearSearch);
        mRelLytSearchBox = (RelativeLayout) view.findViewById(R.id.rellytSearchBox);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewHomeProducts);
        mLinLytProducts = (LinearLayout) view.findViewById(R.id.linlytProducts);
        mLinLytServices = (LinearLayout) view.findViewById(R.id.linlytServices);
        infwOwnerPic = (CircleImageView) view.findViewById(R.id.imgVwprofileImageHome);
        //** init info window containers ***//
        mTxtVwNoResults = (TextView) view.findViewById(R.id.txtvwNoResults);
        mTxtVwNoResults.setVisibility(View.GONE);
        mLinLytInfoWindowContainer = (LinearLayout) view.findViewById(R.id.linlytInfoWindow);
        mRndImgVwInfoWindowProductPic = (RoundedImageView) view.findViewById(R.id.infoWindowProducPic);
        mtxtVwIwTitle = (TextView) view.findViewById(R.id.txtVwProductWishlistTitle);
        mtxtVwIwDescription = (TextView) view.findViewById(R.id.txtVwProductWishlistDetail);
        infwnOwnerName = (TextView) view.findViewById(R.id.txtVwUserNameHome);
        infwnPrice = (TextView) view.findViewById(R.id.txtVwProductWishlistPrice);
        infwnReward = (TextView) view.findViewById(R.id.txtVwProductReferAmount);
        mImgVwHideInfoWindow = (ImageView) view.findViewById(R.id.imgvwHideInfo);
        mLinLytInfoClickHandler = (LinearLayout) view.findViewById(R.id.linlytInfoWindowClickHandler);
        mLinLytInfoClickHandler.setOnClickListener(this);
        mImgVwHideInfoWindow.setOnClickListener(this);
        //** end  of init info window containers ***//
        mLinLytServices.setOnClickListener(this);
        mLinLytProducts.setOnClickListener(this);
        mMapView = (MapView) view.findViewById(R.id.mpvwGoogleMapInstance);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        //================fetching list of all products=============

        mimgvwInitFilter = (ImageView) ((ActivityHome) getActivity()).findViewById(R.id.imgvwInitFilter);
        mimgvwInitFilter.setVisibility(View.VISIBLE);
        if (((ActivityHome) getActivity()).getTriggerMode() == 1) {
            mimgvwInitFilter.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.filter));
        }
        mtxtVwTitileFragments = (TextView) ((ActivityHome) getActivity()).findViewById(R.id.txtVwTitileFragments);
        mtxtVwTitileFragments.setText("Rippleitt");
        work();
        prepareRecycleView();
        connectBaseAttacher();
        return view;
    }


    private void prepareRecycleView() {
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
//        MapsInitializer.initialize(getActivity());

        // recyclerView.addOnScrollListener(scrollListener);
        recyclerView.setItemAnimator(null);

    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("frag_home", "on-resume-called");
        RippleittAppInstance.getInstance().setShouldResetHomeFragment(false);
        initSearch();

       /* if(mMapView!=null)
            mMapView.onResume();*/

    }


    public void reloadRecentSearch() {
        mRelLytSearchBox.setVisibility(View.GONE);
        medtxtQuickSearch.setText("");
        resetSearchCounters();
        resetFilters();
        performVolleyRecentSearch();
        mLinLytProducts.performClick();
    }


    public void initSearch() {
        if (shouldTriggerRecent) {
            Log.e("shold-trigger-recent", "yes");
        } else {
            Log.e("shold-trigger-recent", "no");
        }
        if (shouldInitSearch()) {
            Log.e("shold-init-search", "yes");
        } else {
            Log.e("shold-init-search", "ni");
        }
        if (RippleittAppInstance.getInstance().isTriggerSearch()) {
            Log.e("is-trigger-search", "yes");
        } else {
            Log.e("is-trigger-search", "no");
        }

        if (shouldTriggerRecent && shouldInitSearch() &&
                RippleittAppInstance.getInstance().isTriggerSearch()) {
            performVolleyRecentSearch();
        } else {
            RippleittAppInstance
                    .getInstance()
                    .setTriggerSearch(true);
            shouldTriggerRecent = true;
        }
    }

    public void toggleSearchBoxVisibility() {
        if (mRelLytSearchBox.getVisibility() == View.VISIBLE) {
            mRelLytSearchBox.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(medtxtQuickSearch.getWindowToken(), 0);
        } else {
            mRelLytSearchBox.setVisibility(View.VISIBLE);
            medtxtQuickSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(medtxtQuickSearch, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void toggleMapView() {
        /*if(mMapView.getVisibility()==View.VISIBLE){
            mMapView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            mMapView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }*/
    }

    public void work() {

        // mimgvwtoggleMap.setOnClickListener(this);
        ///===================search=====================
        medtxtQuickSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (v.getText().toString().trim().equalsIgnoreCase("")) {
                        TastyToast.makeText(getActivity(), "Enter text to search",
                                TastyToast.LENGTH_SHORT, TastyToast.INFO);
                    } else {
                        // reset the previous results.....
                        InputMethodManager imm = (InputMethodManager) getActivity()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(medtxtQuickSearch.getWindowToken(), 0);
                        resetSearchCounters();
                        performVolleyCriteriaSearch();
                    }
                    return true;
                }
                return false;
            }
        });

        //////////=================filter================

        mimgvwInitFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((ActivityHome) getActivity()).getTriggerMode() == 1) {
                    RippleittAppInstance
                            .getInstance()
                            .setTriggerSearch(false);
                    Intent i = new Intent(getActivity(), ActivityFilterSelector.class);
                    startActivityForResult(i, status_filter);
                }

                // new SearchApi().searchApi(getContext(), "0", "", "13474949", "14880621", maviLoaderHome);
            }
        });
        //================clear button============
        mimgVwClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                medtxtQuickSearch.setText("");
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                resetSearchCounters();
                homeProductsAdapter = null;
                performVolleyCriteriaSearch();

                // new FetchListingApi().fetchListingApi(getActivity(), "0", maviLoaderHome);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mimgvwtoggleSearch = (ImageView) ((ActivityHome) getActivity())
                .findViewById(R.id.imgvwInitSearch);
        mimgvwtoggleSearch.setVisibility(View.VISIBLE);
        ((ActivityHome) getActivity()).showBellIcon();

        if (RippleittAppInstance.getInstance().isShouldResetHomeFragment()) {
            RippleittAppInstance.getInstance().setShouldResetHomeFragment(false);
            reloadRecentSearch();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null)
            mMapView.onPause();
        mimgvwtoggleSearch = (ImageView) ((ActivityHome) getActivity()).findViewById(R.id.imgvwInitSearch);
        //mimgvwtoggleSearch.setVisibility(View.INVISIBLE);
        RippleittAppInstance.getInstance().getRequestQueue().cancelAll("rippleitt");
        /* mMapView.onPause();*/
    }

    private void resetSearchCounters() {
        RippleittAppInstance.getInstance().setProductCount(0);
        RippleittAppInstance.getInstance().setServiceCount(0);
        RippleittAppInstance.getInstance().setCountCurrentPage(-1);
        RippleittAppInstance.getInstance().setCountMaxPages(0);
        RippleittAppInstance.getInstance().setCountTotalSearchResult(0);
        RippleittAppInstance.getInstance().getCurrentSearchResults().clear();
        RippleittAppInstance.getInstance().getCurrentFilteredProducts().clear();
        RippleittAppInstance.getInstance().getCurrentFilteredServices().clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == status_filter) {

            if (resultCode == Activity.RESULT_OK) {

                if (RippleittAppInstance.getInstance().isTriggerCriteriaSearch()) {
                    resetSearchCounters();
                    performVolleyCriteriaSearch();
                } else {
                    if (CURRENT_RESULT_MODE == RESULT_MODE_SERVICES) {
                        shouldTriggerRecent = true;
                        resetSearchCounters();
                        mLinLytServices.performClick();
                    } else {
                        resetSearchCounters();
                        performVolleyRecentSearch();
                    }
                }

            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                if (RippleittAppInstance.getInstance().isTriggerCriteriaSearch()) {
                    resetSearchCounters();
                    performVolleyCriteriaSearch();
                    recyclerView.setVisibility(View.GONE);
                    CURRENT_RESULT_MODE = RESULT_MODE_SERVICES;
                    homeProductsAdapter = null;
                    mLinLytServices.setBackgroundColor(Color.parseColor("#349b99"));
                    mLinLytProducts.setBackgroundColor(Color.parseColor("#aaaaaa"));
                } else {
                    if (CURRENT_RESULT_MODE == RESULT_MODE_SERVICES) {
                        shouldTriggerRecent = true;
                        resetSearchCounters();
                        mLinLytServices.performClick();
                    } else {
                        resetSearchCounters();
                        performVolleyRecentSearch();
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //resetSearchCounters();
                //performVolleyRecentSearch();;
            }
        }

    }

    private void resetFilters() {

        RippleittAppInstance.getInstance().getSearchCategoryPayload().clear();
        RippleittAppInstance.getInstance().getSearchSubCategoryPayload().clear();
        RippleittAppInstance.getInstance().getSearchPriceRangePayload().clear();
        for (String key : RippleittAppInstance
                .getInstance()
                .getCurrentLoadedCategories().keySet()) {
            RippleittAppInstance
                    .getInstance()
                    .getCurrentLoadedCategories().get(key).setIsTicked(0);
        }

        for (String key : RippleittAppInstance
                .getInstance()
                .getCurrentLoadedSubCategories().keySet()) {
            RippleittAppInstance
                    .getInstance()
                    .getCurrentLoadedSubCategories().get(key).setIsTicked(0);
        }
        for (String key : RippleittAppInstance
                .getInstance()
                .getCurrentLoadedPriceRanges().keySet()) {
            RippleittAppInstance
                    .getInstance()
                    .getCurrentLoadedPriceRanges().get(key).setIsTicked(0);
        }


    }

    private void performVolleyRecentSearch() {
        CURRENT_SEARCH_MODE = SEARCH_MODE_RECENT;
        Log.e("frag_home", "on-init-volley-recent");
        RippleittAppInstance.getInstance()
                .setProductCount(RippleittAppInstance
                        .getInstance().getCurrentFilteredProducts().size());
        RippleittAppInstance.getInstance()
                .setServiceCount(RippleittAppInstance
                        .getInstance().getCurrentFilteredServices().size());

        if (RippleittAppInstance.getInstance().getCountCurrentPage() == -1) {
            maviLoaderHomeCenter.setVisibility(View.VISIBLE);
        } else {
            maviLoaderHomeBottom.setVisibility(View.VISIBLE);
        }
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCH_LISTING_DETAILS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("frag_home", "on-response-volley-recent");
                        maviLoaderHomeCenter.setVisibility(View.GONE);
                        maviLoaderHomeBottom.setVisibility(View.GONE);
                        //Log.d("", response.toString());
                        try {


                            Gson gson = new Gson();
                            SearchResultsPayload searchResults =
                                    (SearchResultsPayload) gson.fromJson(response, SearchResultsPayload.class);
                            if (searchResults.getResponse_code().equalsIgnoreCase("1")) {
                                Log.e("frag_home", "current_page" + Integer.toString(searchResults.getCurrentpage()));
                                Log.e("frag_home", "max_page" + Integer.toString(searchResults.getTotalpages()));
                                Log.e("frag_home", "total_results" + Integer.toString(searchResults.getTotalcount()));

                                if (searchResults.getTotalcount() == 0) {

                                    mTxtVwNoResults.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);

                                    return;
                                } else {
                                    mTxtVwNoResults.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                                RippleittAppInstance
                                        .getInstance()
                                        .setCountCurrentPage(searchResults.getCurrentpage());
                                RippleittAppInstance
                                        .getInstance()
                                        .setCountMaxPages(searchResults.getTotalpages());
                                RippleittAppInstance
                                        .getInstance()
                                        .setCountTotalSearchResult(searchResults.getTotalcount());
                                // now add the search results into the Globals Array...
                                RippleittAppInstance
                                        .getInstance()
                                        .segregateResults(Arrays.asList(searchResults.getData()));

                                populateData();
                                if (attacher != null) attacher.setLoadMoreEnabled(true);
                            } else {
                                Toast.makeText(getActivity(),
                                        "could not fetch results, please try again",
                                        Toast.LENGTH_LONG).show();
                            }


                        } catch (Exception e) {

                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        error.getMessage(),
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
                params.put("page", Integer.toString(RippleittAppInstance
                        .getInstance()
                        .getCountCurrentPage() + 1));
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");


    }

    private void populateData() {
        recyclerView.setVisibility(View.VISIBLE);
        if (homeProductsAdapter == null) {
            if (CURRENT_RESULT_MODE == RESULT_MODE_PRODUCT) {
                homeProductsAdapter = new HomeProductsAdapter(getActivity(),
                        this, RippleittAppInstance.getInstance()
                        .getCurrentFilteredProducts());
            } else {
                homeProductsAdapter = new HomeProductsAdapter(getActivity(),
                        this, RippleittAppInstance.getInstance()
                        .getCurrentFilteredServices());
            }
            prepareRecycleView();
            recyclerView.setAdapter(homeProductsAdapter);
            connectBaseAttacher();
        } else {
            // homeProductsAdapter.notifyDataSetChanged();
            if (CURRENT_RESULT_MODE == RESULT_MODE_PRODUCT) {
                if (RippleittAppInstance.getInstance().getCurrentFilteredProducts()
                        .size() == 0
                        && RippleittAppInstance.getInstance().getCurrentFilteredServices()
                        .size() != 0) {
                    CURRENT_RESULT_MODE = RESULT_MODE_SERVICES;
                    mLinLytServices.performClick();
                }
                homeProductsAdapter.notifyItemRangeInserted(RippleittAppInstance.getInstance().getProductCount(),
                        RippleittAppInstance.getInstance().getCurrentFilteredProducts()
                                .size() - RippleittAppInstance.getInstance().getProductCount());
            } else {
                if (RippleittAppInstance.getInstance().getCurrentFilteredProducts()
                        .size() != 0
                        && RippleittAppInstance.getInstance().getCurrentFilteredServices()
                        .size() == 0) {
                    CURRENT_RESULT_MODE = RESULT_MODE_PRODUCT;
                    mLinLytProducts.performClick();
                }


                homeProductsAdapter.notifyItemRangeInserted(RippleittAppInstance.getInstance().getServiceCount(),
                        RippleittAppInstance.getInstance().getCurrentFilteredServices()
                                .size() - RippleittAppInstance.getInstance().getServiceCount());
            }
        }

        if (CURRENT_RESULT_MODE == RESULT_MODE_SERVICES) {
            if (CURRENT_SEARCH_MODE == SEARCH_MODE_CRITERIA) {
                if (shouldInitSearch())
                    performVolleyCriteriaSearch();
            } else {
                if (shouldInitSearch())
                    performVolleyRecentSearch();
            }
        }

        // drawPinsOnMap();
    }

    @Override
    public void itemSelected(int position) {
        String listing_id = "";
        if (CURRENT_RESULT_MODE == RESULT_MODE_PRODUCT) {
            listing_id = RippleittAppInstance.getInstance()
                    .getCurrentFilteredProducts().get(position).getListing_id();
        } else {
            listing_id = RippleittAppInstance.getInstance()
                    .getCurrentFilteredServices().get(position).getListing_id();
        }
        RippleittAppInstance.getInstance().setCURRENT_SELECTED_LISTING_ID(listing_id);
        volleyFetchItemDetails(listing_id);
    }

    private void volleyFetchItemDetails(final String listingID) {
        maviLoaderHomeCenter.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.BASE_URL
                        + RippleittAppInstance.FETCHING_PRODUCT_DETAILS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        maviLoaderHomeCenter.setVisibility(View.GONE);
                        maviLoaderHomeBottom.setVisibility(View.GONE);
                        try {
                            Gson g = new Gson();
                            ProductDetailsResponseTemplate productDetails =
                                    (ProductDetailsResponseTemplate) g.fromJson(response, ProductDetailsResponseTemplate.class);
                            RippleittAppInstance.getInstance().setSELECTED_LISTING_DETAIL_OBJECT(productDetails.getData());
                            shouldTriggerRecent = false;
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

    private void performVolleyCriteriaSearch() {
        CURRENT_SEARCH_MODE = SEARCH_MODE_CRITERIA;
        Log.e("frag_home", "init_criteria_search");
        RippleittAppInstance.getInstance()
                .setProductCount(RippleittAppInstance
                        .getInstance().getCurrentFilteredProducts().size());
        RippleittAppInstance.getInstance()
                .setServiceCount(RippleittAppInstance
                        .getInstance().getCurrentFilteredServices().size());

        if (RippleittAppInstance.getInstance().getCountCurrentPage() == -1) {
            maviLoaderHomeCenter.setVisibility(View.VISIBLE);
        } else {
            maviLoaderHomeBottom.setVisibility(View.VISIBLE);
        }
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.BASE_URL + RippleittAppInstance.CRITERIA_SEARCH,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("frag_home", "on-response_criteria");
                        Log.e("packer", response);
                        maviLoaderHomeCenter.setVisibility(View.GONE);
                        maviLoaderHomeBottom.setVisibility(View.GONE);
                        shouldTriggerRecent = false;
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        try {
                            SearchResultsPayload searchResults =
                                    (SearchResultsPayload) gson.fromJson(response, SearchResultsPayload.class);
                            if (searchResults.getResponse_code().equalsIgnoreCase("1")) {

                                if (searchResults.getTotalcount() == 0) {

                                    mTxtVwNoResults.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);

                                    return;
                                } else {
                                    mTxtVwNoResults.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                                Log.e("frag_home", "critera-current_page" + Integer.toString(searchResults.getCurrentpage()));
                                Log.e("frag_home", "critera-max_page" + Integer.toString(searchResults.getTotalpages()));
                                Log.e("frag_home", "critera-total_results" + Integer.toString(searchResults.getTotalcount()));

                                RippleittAppInstance
                                        .getInstance()
                                        .setCountCurrentPage(searchResults.getCurrentpage());
                                RippleittAppInstance
                                        .getInstance()
                                        .setCountMaxPages(searchResults.getTotalpages());
                                RippleittAppInstance
                                        .getInstance()
                                        .setCountTotalSearchResult(searchResults.getTotalcount());
                                // now add the search results into the Globals Array...
                                RippleittAppInstance
                                        .getInstance()
                                        .segregateResults(Arrays.asList(searchResults.getData()));
                                populateData();
                                if (attacher != null) attacher.setLoadMoreEnabled(true);
                            } else {
                                Toast.makeText(getActivity(),
                                        "could not fetch results, please try again",
                                        Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
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
                params.put("page", Integer.toString(RippleittAppInstance
                        .getInstance()
                        .getCountCurrentPage() + 1));
                String cat_packet = "";
                if (RippleittAppInstance.getInstance().getSearchCategoryPayload().isEmpty()) {
                    cat_packet = "0";
                } else {
                    for (String key : RippleittAppInstance.getInstance()
                            .getSearchCategoryPayload().keySet()) {
                        cat_packet = cat_packet + key + "|";
                    }
                }
                String sub_cat_packet = "";
                if (RippleittAppInstance.getInstance().getSearchSubCategoryPayload().isEmpty()) {
                    sub_cat_packet = "0";
                } else {
                    for (String key : RippleittAppInstance.getInstance()
                            .getSearchSubCategoryPayload().keySet()) {
                        sub_cat_packet = sub_cat_packet + key + "|";
                    }
                }
                String price_range_packet = "";
                if (RippleittAppInstance.getInstance().getSearchPriceRangePayload().isEmpty()) {
                    price_range_packet = "0";
                } else {
                    for (String key : RippleittAppInstance.getInstance()
                            .getSearchPriceRangePayload().keySet()) {
                        price_range_packet = price_range_packet + key + "|";
                    }
                }
                cat_packet = cat_packet.replaceAll("\\|$", "");
                sub_cat_packet = sub_cat_packet.replaceAll("\\|$", "");
                price_range_packet = price_range_packet.replaceAll("\\|$", "");
                params.put("category_id", cat_packet);
                params.put("sub_category_id", sub_cat_packet);
                params.put("keyword", medtxtQuickSearch.getText().toString());
                params.put("price", price_range_packet);
                params.put("page", Integer.toString(RippleittAppInstance
                        .getInstance()
                        .getCountCurrentPage() + 1));
                System.out.print(params);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr,
                "rippleitt");

    }

    @Override
    public void onClick(View view) {
        if (view == mLinLytInfoClickHandler) {
            if (!listing_id_to_load.equalsIgnoreCase(""))
                volleyFetchItemDetails(listing_id_to_load);
        }
        if (view == mImgVwHideInfoWindow) {
            mLinLytInfoWindowContainer.setVisibility(View.GONE);
        }


        if (view == mLinLytProducts) {
            mLinLytProducts.setBackgroundColor(Color.parseColor("#349b99"));
            mLinLytServices.setBackgroundColor(Color.parseColor("#aaaaaa"));
            filterResults(RESULT_MODE_PRODUCT);
        }
        if (view == mLinLytServices) {
            recyclerView.setVisibility(View.GONE);
            CURRENT_RESULT_MODE = RESULT_MODE_SERVICES;
            homeProductsAdapter = null;
            mLinLytServices.setBackgroundColor(Color.parseColor("#349b99"));
            mLinLytProducts.setBackgroundColor(Color.parseColor("#aaaaaa"));
            if (shouldTriggerRecent) {
                performVolleyRecentSearch();
            } else {
                performVolleyCriteriaSearch();
            }
            //filterResults(RESULT_MODE_SERVICES);
        }
    }

    private void filterResults(int mode) {
        CURRENT_RESULT_MODE = mode;
        toggleDataView();
    }

    private void toggleDataView() {
        if (CURRENT_RESULT_MODE == RESULT_MODE_PRODUCT) {

            if (RippleittAppInstance.getInstance()
                    .getCurrentFilteredProducts().size() == 0) {
                mTxtVwNoResults.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                mTxtVwNoResults.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            homeProductsAdapter = new HomeProductsAdapter(getActivity(),
                    this, RippleittAppInstance.getInstance()
                    .getCurrentFilteredProducts());
        } else {

            if (RippleittAppInstance.getInstance()
                    .getCurrentFilteredServices().size() == 0) {
                mTxtVwNoResults.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                mTxtVwNoResults.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }


            homeProductsAdapter = new HomeProductsAdapter(getActivity(),
                    this, RippleittAppInstance.getInstance()
                    .getCurrentFilteredServices());
        }
        prepareRecycleView();
        recyclerView.setAdapter(homeProductsAdapter);
        connectBaseAttacher();


    }

    private void connectBaseAttacher() {
        //mCollectionView can be a ListView, GridView, RecyclerView or any instance of AbsListView!
        attacher = Mugen.with(recyclerView, new MugenCallbacks() {
            @Override
            public void onLoadMore() {

                Log.e("frag_home", "load-more");
                if (shouldTriggerRecent && shouldInitSearch()) {
                    performVolleyRecentSearch();
                    attacher.setLoadMoreEnabled(false);
                } else if (shouldInitSearch()) {
                    performVolleyCriteriaSearch();
                    attacher.setLoadMoreEnabled(false);
                }
            }

            @Override
            public boolean isLoading() {
                /* Return true if a load operation is ongoing. This will
                 * be used as an optimization to prevent further triggers
                 * if the user scrolls up and scrolls back down before
                 * the load operation finished.
                 *
                 * If there is no load operation ongoing, return false
                 */
                return false;
            }

            @Override
            public boolean hasLoadedAllItems() {
                /*
                 * If every item has been loaded from the data store, i.e., no more items are
                 * left to fetched, you can start returning true here to prevent any more
                 * triggers of the load more method as a form of optimization.
                 *
                 * This is useful when say, the data is being fetched from the network
                 */
                return false;
            }
        }).start();

        /* Use this to dynamically turn infinite scroll on or off. It is enabled by default */
        attacher.setLoadMoreEnabled(true);

        /* Use this to change when the onLoadMore() function is called.
         * By default, it is called when the scroll reaches business items from the bottom */
        attacher.setLoadMoreOffset(1);

        /*
         * mugen uses an internal OnScrollListener to detect and trigger load events.
         * If you need to listen to scroll events yourself, you can set this and
         * mugen will automatically forward all scroll events to the listener.
         */
        // attacher.setOnScrollListener(listener);
    }

    /**
     * if the current loaded page is less than the max page,
     * init I/O, else don't
     *
     * @return
     */

    private boolean shouldInitSearch() {
        if ((RippleittAppInstance.getInstance()
                .getCountCurrentPage() <= RippleittAppInstance
                .getInstance().getCountMaxPages())) {
            Log.e("frag_home", "should-load-true");
            return true;
        }

        Log.e("frag_home", "should_load-false");
        return false;
    }

    @Override
    public void onDestroy() {
        if (mMapView != null)
            mMapView.onDestroy();
        super.onDestroy();
    }

    private void setUpMapIfNeeded(View inflatedView) {


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mGglMpInstance = googleMap;

            boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                    .getString(R.string.style_json)));
            mGglMpInstance.getUiSettings().setZoomControlsEnabled(true);
            mGglMpInstance.getUiSettings().setZoomGesturesEnabled(true);
            mGglMpInstance.setOnMarkerClickListener(this);

            if (!success) {
                Log.e("g-maps", "Style parsing failed.");
            }

        }
    }

    private void drawPinsOnMap() {

        try {
            if (mGglMpInstance != null) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (ListingTemplate currentListing : RippleittAppInstance
                        .getInstance()
                        .getCurrentSearchResults()) {

                    if (currentListing.getLocation() != null) {

                        try {
                            LatLng snowqualmie = new LatLng(Double.parseDouble(currentListing.getLocation().getLatitude()),
                                    Double.parseDouble(currentListing.getLocation().getLatitude()));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(snowqualmie)
                                    .title(currentListing.getProductname())
                                    .snippet(currentListing.getListing_description())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            Marker m = mGglMpInstance.addMarker(markerOptions);

                            m.setTag(currentListing);
                            builder.include(m.getPosition());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    //m.showInfoWindow();
                }

                LatLngBounds bounds = builder.build();
                int padding = 10; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mGglMpInstance.moveCamera(cu);
                mGglMpInstance.animateCamera(cu);
            }
        } catch (Exception e) {

        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        ListingTemplate currentListing = (ListingTemplate) marker.getTag();
        listing_id_to_load = currentListing.getListing_id();
        if (currentListing != null && currentListing instanceof ListingTemplate) {
            mLinLytInfoWindowContainer.setVisibility(View.VISIBLE);
            //  mtxtVwIwTitle, mtxtVwIwDescription, infwnOwnerName,
            //  infwnPrice,infwnReward;
            mtxtVwIwTitle.setText(currentListing.getProductname());
            mtxtVwIwDescription.setText(currentListing.getListing_description());
            infwnOwnerName.setText(currentListing.getUserinformation().getFullName());
            infwnPrice.setText("$" + currentListing.getListing_price());
            infwnReward.setText("Earn: " + currentListing.getRefer_amount());

            if ((currentListing
                    .getListing_photos().length != 0)) {
                try {
                    if (currentListing
                            .getListing_photos()[0].getPhoto_path().equalsIgnoreCase("null")
                            || currentListing
                            .getListing_photos()[0].getPhoto_path().equalsIgnoreCase("")) {
                        mRndImgVwInfoWindowProductPic.setImageResource(R.drawable.default_profile_icon);
                    } else {
                        Glide.with(getActivity())
                                .load(RippleittAppInstance.formatPicPath(
                                        currentListing
                                                .getListing_photos()[0].getPhoto_path()))
                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .into(mRndImgVwInfoWindowProductPic);
                    }

                } catch (Exception e) {
                    mRndImgVwInfoWindowProductPic.setImageResource(R.drawable.default_profile_icon);
                }
            }

            try {
                Glide.with(getActivity())
                        .load(RippleittAppInstance.formatPicPath(
                                currentListing
                                        .getUserinformation().getImage()))
                        .fitCenter()
                        .into(infwOwnerPic);
            } catch (Exception e) {
                infwOwnerPic.setImageResource(R.drawable.default_profile_icon);
            }

        }

        return true;
    }


}