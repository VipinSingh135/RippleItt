package com.rippleitt.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.adapters.FilterCategoriesAdapter;
import com.rippleitt.adapters.FilterPriceRangeAdapter;
import com.rippleitt.adapters.FilterSubCategoriesAdapter;
import com.rippleitt.callback.FilterParamSelected;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.CategoryListTemplate;
import com.rippleitt.modals.CategoryTemplate;
import com.rippleitt.modals.PriceRangeResponseTemplate;
import com.rippleitt.utils.CommonUtils;
import com.robertlevonyan.views.chip.Chip;
import com.robertlevonyan.views.chip.OnCloseClickListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by pc on password/17/2018.
 */

public class ActivityFilterSelector
        extends AppCompatActivity implements
        View.OnClickListener, FilterParamSelected {

    private ImageView mImgVwCross;
    private LinearLayout mLinlytToggleCategory;
    private LinearLayout mLinLytToggleSubCategory;
    private LinearLayout mLinLytTogglePrice;
    private LinearLayout linlytTypeToggle;
    private LinearLayout linlytTypeOptions;
    private TextView mTxtVwClearFilter, mTxtVwApplyFilter;
    private AVLoadingIndicatorView mLoaderFilter;
    private ListView mLstViwCategories, mLstVwSubCategories, mLstVwPriceRange;
    private FilterCategoriesAdapter categoriesAdapter;
    private FilterSubCategoriesAdapter subCategoriesAdapter;
    private FilterPriceRangeAdapter priceRangeAdapter;
    private LinearLayout mLinLytChipsContainer;
    private final int VIEW_PRICES = 1;
    private final int VIEW_CATEGORIES = 2;
    private final int VIEW_SUB_CATEGORIES = 3;
    private final int VIEW_TYPE = 4;
    private int CURRENT_VIEW;
    boolean isProduct = true;

    private RadioButton rdBtnProduct, rdBtnService;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filter_selector);
        initUI();
        CURRENT_VIEW = VIEW_TYPE;
//        if (!RippleittAppInstance.getInstance().getSearchPriceRangePayload().isEmpty()) {
        loadPriceRangeList();
        loadCategoryList();
        loadSubCategoryList();
        loadChips();
        int filterCount = RippleittAppInstance
                .getInstance()
                .getSearchCategoryPayload()
                .keySet().size()
                + RippleittAppInstance
                .getInstance()
                .getSearchSubCategoryPayload()
                .keySet().size()
                + RippleittAppInstance
                .getInstance()
                .getSearchPriceRangePayload()
                .keySet().size();

        mTxtVwApplyFilter.setText("Apply Filters("
                + Integer.toString(filterCount) + ")");
//        }

        if(RippleittAppInstance.getInstance().isProductFilter()){
            rdBtnProduct.setChecked(true);
            rdBtnService.setChecked(false);
        }else{
            rdBtnProduct.setChecked(false);
            rdBtnService.setChecked(true);
        }


        rdBtnProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isProduct = true;
                    resetCategory();
                    resetSubCategory();
                    RippleittAppInstance.getInstance().setProductFilter(true);
                    rdBtnService.setChecked(false);
                    RippleittAppInstance.getInstance().getSearchCategories().clear();
                }
            }
        });
        rdBtnService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isProduct = false;
                    resetCategory();
                    resetSubCategory();
                    RippleittAppInstance.getInstance().setProductFilter(false);
                    rdBtnProduct.setChecked(false);
                    RippleittAppInstance.getInstance().getSearchCategories().clear();
                }
            }
        });

        //        loadChips();


    }

    @Override
    public void onClick(View view) {
        if (view == mImgVwCross) {
            Intent returnIntent = new Intent();
            RippleittAppInstance.getInstance().setProductFilter(true);
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
            //resetFilters()
        }
        if (view == mLinLytTogglePrice) {
            CURRENT_VIEW = VIEW_PRICES;
            mLstViwCategories.setVisibility(View.GONE);
            mLstVwPriceRange.setVisibility(View.VISIBLE);
            linlytTypeOptions.setVisibility(View.GONE);
            mLstVwSubCategories.setVisibility(View.GONE);
            mLstViwCategories.setVisibility(View.GONE);
            mLinlytToggleCategory.setBackgroundColor(getResources()
                    .getColor(R.color.colorHeaderBackground));
            mLinLytToggleSubCategory.setBackgroundColor(getResources()
                    .getColor(R.color.colorHeaderBackground));
            mLinLytTogglePrice.setBackgroundColor(getResources()
                    .getColor(R.color.header_dark));
            linlytTypeToggle.setBackgroundColor(getResources()
                    .getColor(R.color.colorHeaderBackground));
            fetchPriceRanges();

        }

        if (view == linlytTypeToggle) {
            CURRENT_VIEW = VIEW_TYPE;
            mLstViwCategories.setVisibility(View.GONE);
            mLstVwPriceRange.setVisibility(View.GONE);
            linlytTypeOptions.setVisibility(View.VISIBLE);
            mLstVwSubCategories.setVisibility(View.GONE);
            mLstViwCategories.setVisibility(View.GONE);
            mLinlytToggleCategory.setBackgroundColor(getResources()
                    .getColor(R.color.colorHeaderBackground));
            mLinLytToggleSubCategory.setBackgroundColor(getResources()
                    .getColor(R.color.colorHeaderBackground));
            mLinLytTogglePrice.setBackgroundColor(getResources()
                    .getColor(R.color.colorHeaderBackground));
            linlytTypeToggle.setBackgroundColor(getResources()
                    .getColor(R.color.header_dark));
        }

        if (view == mLinlytToggleCategory) {
            CURRENT_VIEW = VIEW_CATEGORIES;

            mLstViwCategories.setVisibility(View.VISIBLE);
            linlytTypeOptions.setVisibility(View.GONE);
            mLstVwSubCategories.setVisibility(View.GONE);
            mLstVwPriceRange.setVisibility(View.GONE);
            mLinlytToggleCategory.setBackgroundColor(getResources()
                    .getColor(R.color.header_dark));
            mLinLytToggleSubCategory.setBackgroundColor(getResources()
                    .getColor(R.color.colorHeaderBackground));
            mLinLytTogglePrice.setBackgroundColor(getResources()
                    .getColor(R.color.colorHeaderBackground));
            linlytTypeToggle.setBackgroundColor(getResources()
                    .getColor(R.color.colorHeaderBackground));
            if (RippleittAppInstance.getInstance()
                    .getSearchCategoryPayload()
                    .isEmpty()) {
                //fetchCategories();
            }
            fetchCategories();

        }
        if (view == mLinLytToggleSubCategory) {
            CURRENT_VIEW = VIEW_SUB_CATEGORIES;
            if (RippleittAppInstance.getInstance().getSearchCategoryPayload().isEmpty()) {
                resetSubCategory();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please select category.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return;
            }


//            mLstVwPriceRange.setVisibility(View.GONE);
            //           mLstViwCategories.setVisibility(View.GONE);
            //          mLstVwSubCategories.setVisibility(View.VISIBLE);
            linlytTypeOptions.setVisibility(View.GONE);
            mLinlytToggleCategory.setBackgroundColor(getResources()
                    .getColor(R.color.colorHeaderBackground));
            mLinLytToggleSubCategory.setBackgroundColor(getResources()
                    .getColor(R.color.header_dark));
            mLinLytTogglePrice.setBackgroundColor(getResources()
                    .getColor(R.color.colorHeaderBackground));
            linlytTypeToggle.setBackgroundColor(getResources()
                    .getColor(R.color.colorHeaderBackground));

            fetchSubCategories();
            if (RippleittAppInstance.getInstance()
                    .getSearchSubCategoryPayload()
                    .isEmpty()) {
                //fetchSubCategories();
            }
        }
        if (view == mTxtVwClearFilter) {
            resetFilters();
            RippleittAppInstance.getInstance()
                    .setTriggerCriteriaSearch(false);
            RippleittAppInstance.getInstance().setProductFilter(true);
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        if (view == mTxtVwApplyFilter) {
            if (RippleittAppInstance.getInstance().getSearchCategoryPayload().isEmpty()
                    &&
                    RippleittAppInstance.getInstance().getSearchPriceRangePayload().isEmpty()) {
                //Please select category or price range

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please select category or price range.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return;
            } else {
                RippleittAppInstance.getInstance()
                        .setTriggerCriteriaSearch(true);
                Intent returnIntent = new Intent();
                if(isProduct) {
                    setResult(Activity.RESULT_OK, returnIntent);
                }else {
                    setResult(Activity.RESULT_FIRST_USER, returnIntent);
                }
                finish();
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void initUI() {
        mLstVwPriceRange = (ListView) findViewById(R.id.lstVwPriceRanges);
        mLinLytTogglePrice = (LinearLayout) findViewById(R.id.linlytTogglePrice);
        mLinLytTogglePrice.setOnClickListener(this);
        mImgVwCross = (ImageView) findViewById(R.id.imgVwClr);
        mLstViwCategories = (ListView) findViewById(R.id.lstVwCategories);
        mLstVwSubCategories = (ListView) findViewById(R.id.lstVwSubCategories);
        mLinlytToggleCategory = (LinearLayout) findViewById(R.id.linlytToggleCategory);
        mLinLytToggleSubCategory = (LinearLayout) findViewById(R.id.linlytToggleSubCategory);
        linlytTypeToggle = (LinearLayout) findViewById(R.id.linlytTypeToggle);
        linlytTypeOptions = (LinearLayout) findViewById(R.id.linlytTypeOptions);
        mTxtVwApplyFilter = (TextView) findViewById(R.id.txtvwFilterCount);
        mTxtVwClearFilter = (TextView) findViewById(R.id.txtvwClear);
        rdBtnService = findViewById(R.id.rdBtnService);
        rdBtnProduct = findViewById(R.id.rdBtnProduct);
        mLstVwSubCategories.setVisibility(View.GONE);
        linlytTypeOptions.setVisibility(View.VISIBLE);
        mLstVwPriceRange.setVisibility(View.GONE);
        mLstViwCategories.setVisibility(View.GONE);
        mLoaderFilter = (AVLoadingIndicatorView) findViewById(R.id.aviLoaderFilterCenter);
        mLinLytChipsContainer = (LinearLayout) findViewById(R.id.linlytChipsContainer);
        mLoaderFilter.setVisibility(View.GONE);
        mImgVwCross.setOnClickListener(this);
        mTxtVwApplyFilter.setOnClickListener(this);
        mTxtVwClearFilter.setOnClickListener(this);
        linlytTypeToggle.setOnClickListener(this);
        mLinlytToggleCategory.setOnClickListener(this);
        mLinLytToggleSubCategory.setOnClickListener(this);

    }


    private void resetSubCategory() {
        mLinLytChipsContainer.removeAllViews();
        RippleittAppInstance.getInstance().getSearchSubCategoryPayload().clear();
        for (String key : RippleittAppInstance
                .getInstance()
                .getCurrentLoadedSubCategories().keySet()) {
            RippleittAppInstance
                    .getInstance()
                    .getCurrentLoadedSubCategories().get(key).setIsTicked(0);
        }
        int filterCount = RippleittAppInstance
                .getInstance()
                .getSearchCategoryPayload()
                .keySet().size()
                + RippleittAppInstance
                .getInstance()
                .getSearchSubCategoryPayload()
                .keySet().size()
                + RippleittAppInstance
                .getInstance()
                .getSearchPriceRangePayload()
                .keySet().size();

        mTxtVwApplyFilter.setText("Apply Filters("
                + Integer.toString(filterCount) + ")");
        loadChips();

    }


    private void resetCategory() {
        mLinLytChipsContainer.removeAllViews();
        RippleittAppInstance.getInstance().getSearchCategoryPayload().clear();
        RippleittAppInstance.getInstance().getCurrentLoadedCategories().clear();
        categoriesAdapter = new FilterCategoriesAdapter(ActivityFilterSelector.this,
                1, this);
        mLstViwCategories.setAdapter(categoriesAdapter);

//        for (String key : RippleittAppInstance
//                .getInstance()
//                .getCurrentLoadedCategories().keySet()) {
//            RippleittAppInstance
//                    .getInstance()
//                    .getCurrentLoadedCategories().get(key).setIsTicked(0);
//        }
        int filterCount = RippleittAppInstance
                .getInstance()
                .getSearchCategoryPayload()
                .keySet().size()
                + RippleittAppInstance
                .getInstance()
                .getSearchSubCategoryPayload()
                .keySet().size()
                + RippleittAppInstance
                .getInstance()
                .getSearchPriceRangePayload()
                .keySet().size();

        mTxtVwApplyFilter.setText("Apply Filters("
                + Integer.toString(filterCount) + ")");
        loadChips();
    }

    private void resetFilters() {
        mLinLytChipsContainer.removeAllViews();
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

        loadCategoryList();
        loadSubCategoryList();
        loadPriceRangeList();
        mTxtVwApplyFilter.setText("Apply Filters");


    }


    private void fetchCategories() {
        mLoaderFilter.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance.getInstance().getFETCH_CATEGORIES(),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mLoaderFilter.setVisibility(View.GONE);
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        try {
                            CategoryListTemplate response_ = (CategoryListTemplate) gson.fromJson(response, CategoryListTemplate.class);
                            if (response_.getResponse_code() == 1) {
                                RippleittAppInstance
                                        .getInstance()
                                        .setCategories(response_.getData());

                                LinkedHashMap<String, String> tempCopyPayload = new LinkedHashMap<>();
                                tempCopyPayload.clear();
//                                if ()
                                for (CategoryTemplate currentCategory : RippleittAppInstance.getInstance().getCurrentLoadedCategories().values()) {
                                    if (RippleittAppInstance.getInstance().getSearchCategoryPayload().containsKey(currentCategory.getId())) {
                                        tempCopyPayload.put(currentCategory.getId(), currentCategory.getName());
                                    }
                                }

                                RippleittAppInstance.getInstance().getSearchCategoryPayload().clear();
                                ;
                                RippleittAppInstance.getInstance().getSearchCategoryPayload().putAll(tempCopyPayload);
                                tempCopyPayload.clear();
                                ;


                                for (CategoryTemplate currentCategory : RippleittAppInstance.getInstance().getCurrentLoadedCategories().values()) {
                                    if (RippleittAppInstance.getInstance().getSearchCategoryPayload().containsKey(currentCategory.getId())) {
                                        currentCategory.setIsTicked(1);
                                    } else {
                                        currentCategory.setIsTicked(0);
                                        RippleittAppInstance.getInstance().getSearchCategoryPayload().remove(currentCategory.getId());
                                    }
                                }
                                loadChips();


                                loadCategoryList();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Invalid keywords",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                        }

                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityFilterSelector.this,
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
                params.put("token", PreferenceHandler.readString(ActivityFilterSelector.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                if (isProduct) {
                    params.put("type", "1");
                } else {
                    params.put("type", "2");
                }
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_categories");
    }


    private void fetchPriceRanges() {
        mLoaderFilter.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance.getInstance().getFetchPriceRanges(),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mLoaderFilter.setVisibility(View.GONE);
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        try {
                            PriceRangeResponseTemplate response_ = (PriceRangeResponseTemplate) gson.fromJson(response, PriceRangeResponseTemplate.class);
                            if (response_.getResponse_code() == 1) {
                                RippleittAppInstance
                                        .getInstance()
                                        .setPriceRanges(response_.getData());
                                loadPriceRangeList();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Invalid keywords",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d("Error :", "onResponse: "+e.getMessage());
                        }

                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityFilterSelector.this,
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
                params.put("token", PreferenceHandler.readString(ActivityFilterSelector.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_categories");
    }

    private void loadCategoryList() {
//        if (categoriesAdapter == null) {
        categoriesAdapter = new FilterCategoriesAdapter(ActivityFilterSelector.this,
                1, this);
        mLstViwCategories.setAdapter(categoriesAdapter);
//        } else {
//            categoriesAdapter.notifyDataSetChanged();
//        }
    }


    private void loadPriceRangeList() {
//        if (priceRangeAdapter == null) {
            priceRangeAdapter = new FilterPriceRangeAdapter(ActivityFilterSelector.this, 2, this);
            mLstVwPriceRange.setAdapter(priceRangeAdapter);
//        } else {
//            priceRangeAdapter.notifyDataSetInvalidated();
//            mLstVwPriceRange.setVisibility(View.VISIBLE);
//        }
        if (CURRENT_VIEW == VIEW_PRICES) {
            mLstViwCategories.setVisibility(View.GONE);
            mLstVwSubCategories.setVisibility(View.GONE);
            linlytTypeOptions.setVisibility(View.GONE);
            mLstVwPriceRange.setVisibility(View.VISIBLE);
        }

    }


    private void fetchSubCategories() {

        if (RippleittAppInstance.getInstance().getSearchCategories().keySet().size() == 0) {
            return;
        }
        mLoaderFilter.setVisibility(View.VISIBLE);

        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance
                .getInstance().getFETCH_SUBCATEGORIES(),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mLoaderFilter.setVisibility(View.GONE);
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        CategoryListTemplate response_ = (CategoryListTemplate) gson.fromJson(response, CategoryListTemplate.class);
                        if (response_.getResponse_code() == 1) {
                            RippleittAppInstance
                                    .getInstance()
                                    .setSubCategories(response_.getData());
                            // clean up the sub categories...

                            LinkedHashMap<String, String> tempCopyPayload = new LinkedHashMap<>();
                            tempCopyPayload.clear();
                            for (CategoryTemplate currentSubCategory : RippleittAppInstance.getInstance().getCurrentLoadedSubCategories().values()) {
                                if (RippleittAppInstance.getInstance().getSearchSubCategoryPayload().containsKey(currentSubCategory.getId())) {
                                    tempCopyPayload.put(currentSubCategory.getId(), currentSubCategory.getName());
                                }
                            }

                            RippleittAppInstance.getInstance().getSearchSubCategoryPayload().clear();
                            ;
                            RippleittAppInstance.getInstance().getSearchSubCategoryPayload().putAll(tempCopyPayload);
                            tempCopyPayload.clear();
                            ;


                            for (CategoryTemplate currentSubCategory : RippleittAppInstance.getInstance().getCurrentLoadedSubCategories().values()) {
                                if (RippleittAppInstance.getInstance().getSearchSubCategoryPayload().containsKey(currentSubCategory.getId())) {
                                    currentSubCategory.setIsTicked(1);
                                } else {
                                    currentSubCategory.setIsTicked(0);
                                    RippleittAppInstance.getInstance().getSearchSubCategoryPayload().remove(currentSubCategory.getId());
                                }
                            }
                            loadChips();
                            loadSubCategoryList();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Invalid keywords",
                                    Toast.LENGTH_SHORT).show();
                        }
                        CommonUtils.dismissProgress();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityFilterSelector.this,
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
                params.put("token", PreferenceHandler
                        .readString(ActivityFilterSelector.this,
                                PreferenceHandler.AUTH_TOKEN, ""));

                String categoryPayload = "";
                for (String cat_id : RippleittAppInstance.getInstance().getSearchCategories().keySet()) {
                    categoryPayload = categoryPayload + cat_id + "|";
                }
                categoryPayload = categoryPayload.replaceAll("\\|$", "");
                params.put("category_id", categoryPayload);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_sb_categories");
    }


    @Override
    public void categorySelected(String id, int mode) {
        if (mode == 1) {
            RippleittAppInstance.getInstance().getSearchCategories().put(id, id);
            RippleittAppInstance.getInstance()
                    .getSearchCategoryPayload()
                    .put(id, RippleittAppInstance
                            .getInstance()
                            .getCurrentLoadedCategories().get(id).getName());
        } else {
            if (RippleittAppInstance.getInstance().getSearchCategories().containsKey(id)) {
                RippleittAppInstance.getInstance().getSearchCategories().remove(id);
                RippleittAppInstance.getInstance().getSearchCategoryPayload().remove(id);
            }
        }
        loadCategoryList();
        int filterCount = RippleittAppInstance
                .getInstance()
                .getSearchCategoryPayload()
                .keySet().size()
                + RippleittAppInstance
                .getInstance()
                .getSearchSubCategoryPayload()
                .keySet().size()
                + RippleittAppInstance
                .getInstance()
                .getSearchPriceRangePayload()
                .keySet().size();

        mTxtVwApplyFilter.setText("Apply Filters("
                + Integer.toString(filterCount) + ")");
        loadChips();
    }

    @Override
    public void subCategorySelected(String id, int mode) {


        if (mode == 1) {
            RippleittAppInstance.getInstance().getSearchSubCategories().put(id, id);
            RippleittAppInstance.getInstance()
                    .getSearchSubCategoryPayload()
                    .put(id, RippleittAppInstance
                            .getInstance()
                            .getCurrentLoadedSubCategories().get(id).getName());
        } else {
            if (RippleittAppInstance.getInstance().getSearchSubCategories().containsKey(id)) {
                RippleittAppInstance.getInstance().getSearchSubCategories().remove(id);
                RippleittAppInstance.getInstance().getSearchSubCategoryPayload().remove(id);
            }
        }
        int filterCount = RippleittAppInstance
                .getInstance()
                .getSearchCategoryPayload()
                .keySet().size()
                + RippleittAppInstance
                .getInstance()
                .getSearchSubCategoryPayload()
                .keySet().size()
                + RippleittAppInstance
                .getInstance()
                .getSearchPriceRangePayload()
                .keySet().size();

        mTxtVwApplyFilter.setText("Apply Filters("
                + Integer.toString(filterCount) + ")");
        // loadSubCategoryList();
        loadChips();
    }

    @Override
    public void priceRangeSelected(String id, int mode) {

        if (mode == 1) {
            RippleittAppInstance.getInstance().getSearchPriceRanges().put(id, id);
            RippleittAppInstance.getInstance()
                    .getSearchPriceRangePayload()
                    .put(id, RippleittAppInstance
                            .getInstance()
                            .getCurrentLoadedPriceRanges().get(id).getTitle());
        } else {
            if (RippleittAppInstance.getInstance().getSearchPriceRanges().containsKey(id)) {
                RippleittAppInstance.getInstance().getSearchPriceRanges().remove(id);
                RippleittAppInstance.getInstance().getSearchPriceRangePayload().remove(id);
            }
        }
        int filterCount = RippleittAppInstance
                .getInstance()
                .getSearchCategoryPayload()
                .keySet().size()
                + RippleittAppInstance
                .getInstance()
                .getSearchSubCategoryPayload()
                .keySet().size()
                + RippleittAppInstance
                .getInstance()
                .getSearchPriceRangePayload()
                .keySet().size();

        mTxtVwApplyFilter.setText("Apply Filters("
                + Integer.toString(filterCount) + ")");
        loadPriceRangeList();
        loadChips();

    }

    private void loadSubCategoryList() {


        subCategoriesAdapter = new FilterSubCategoriesAdapter(ActivityFilterSelector.this,
                1, this);
        mLstVwSubCategories.setAdapter(subCategoriesAdapter);

        if (CURRENT_VIEW == VIEW_SUB_CATEGORIES) {
            mLstVwPriceRange.setVisibility(View.GONE);
            mLstViwCategories.setVisibility(View.GONE);
            linlytTypeOptions.setVisibility(View.GONE);
            mLstVwSubCategories.setVisibility(View.VISIBLE);
        }



        /*
        if(subCategoriesAdapter==null){
            subCategoriesAdapter = new FilterSubCategoriesAdapter(ActivityFilterSelector.this,
                    1,this);
            mLstVwSubCategories.setAdapter(subCategoriesAdapter);
        }else{
            subCategoriesAdapter.notifyDataSetChanged();
        }
        */

    }


    private void loadChips() {
        mLinLytChipsContainer.removeAllViews();
        for (String id : RippleittAppInstance.getInstance().getSearchCategoryPayload().keySet()) {
            final Chip chip = new Chip(ActivityFilterSelector.this);
            chip.setChipText(RippleittAppInstance
                    .getInstance()
                    .getSearchCategoryPayload().get(id));
            chip.setClosable(true);
            LinearLayout
                    .LayoutParams params = new LinearLayout
                    .LayoutParams(LinearLayout
                    .LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            chip.setLayoutParams(params);
            chip.setOnCloseClickListener(new OnCloseClickListener() {
                @Override
                public void onCloseClick(View v) {
                    String label = chip.getChipText();
                    refreshUI(label);
                }
            });
            mLinLytChipsContainer.addView(chip);
        }

        for (String id : RippleittAppInstance.getInstance().getSearchSubCategoryPayload().keySet()) {
            final Chip chip = new Chip(ActivityFilterSelector.this);
            chip.setChipText(RippleittAppInstance
                    .getInstance()
                    .getSearchSubCategoryPayload().get(id));
            chip.setClosable(true);
            LinearLayout
                    .LayoutParams params = new LinearLayout
                    .LayoutParams(LinearLayout
                    .LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            chip.setLayoutParams(params);
            chip.setOnCloseClickListener(new OnCloseClickListener() {
                @Override
                public void onCloseClick(View v) {
                    String label = chip.getChipText();
                    refreshUI(label);
                }
            });
            mLinLytChipsContainer.addView(chip);
        }

        // load chips for price ranges...
        for (String id : RippleittAppInstance.getInstance().getSearchPriceRangePayload().keySet()) {
            final Chip chip = new Chip(ActivityFilterSelector.this);
            chip.setChipText(RippleittAppInstance
                    .getInstance()
                    .getSearchPriceRangePayload().get(id));
            chip.setClosable(true);
            LinearLayout
                    .LayoutParams params = new LinearLayout
                    .LayoutParams(LinearLayout
                    .LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            chip.setLayoutParams(params);
            chip.setOnCloseClickListener(new OnCloseClickListener() {
                @Override
                public void onCloseClick(View v) {
                    String label = chip.getChipText();
                    refreshUI(label);
                }
            });
            mLinLytChipsContainer.addView(chip);
        }
    }


    private void refreshUI(String value) {
        String key = (String) getKeyFromValue(RippleittAppInstance
                .getInstance()
                .getSearchCategoryPayload(), value);
        // if this  key is not found, lets search for this key in the subcategories...
        if (key == null) {
            key = (String) getKeyFromValue(RippleittAppInstance
                    .getInstance()
                    .getSearchSubCategoryPayload(), value);
        }
        // if key is not found in the sub categories, we lookup this in the price ranges...
        if (key == null) {
            key = (String) getKeyFromValue(RippleittAppInstance
                    .getInstance()
                    .getSearchPriceRangePayload(), value);
        }

        if (RippleittAppInstance.getInstance().getSearchCategoryPayload().containsKey(key)) {
            RippleittAppInstance.getInstance().getSearchCategoryPayload().remove(key);
            RippleittAppInstance.getInstance().getSearchCategories().remove(key);
        }
        if (RippleittAppInstance.getInstance().getSearchSubCategoryPayload().containsKey(key)) {
            RippleittAppInstance.getInstance().getSearchSubCategoryPayload().remove(key);
        }
        if (RippleittAppInstance.getInstance().getSearchPriceRangePayload().containsKey(key)) {
            RippleittAppInstance.getInstance().getSearchPriceRangePayload().remove(key);
        }

        if (CURRENT_VIEW == VIEW_SUB_CATEGORIES) {
            if (RippleittAppInstance
                    .getInstance()
                    .getCurrentLoadedSubCategories().containsKey(key)) {
                RippleittAppInstance
                        .getInstance()
                        .getCurrentLoadedSubCategories().get(key).setIsTicked(0);
            }
        }
        if (CURRENT_VIEW == VIEW_CATEGORIES) {
            if (RippleittAppInstance
                    .getInstance()
                    .getCurrentLoadedCategories().containsKey(key)) {
                RippleittAppInstance
                        .getInstance()
                        .getCurrentLoadedCategories().get(key).setIsTicked(0);
            }
        }
        if (CURRENT_VIEW == VIEW_PRICES) {
            if (RippleittAppInstance
                    .getInstance()
                    .getCurrentLoadedPriceRanges().containsKey(key)) {
                RippleittAppInstance
                        .getInstance()
                        .getCurrentLoadedPriceRanges().get(key).setIsTicked(0);
            }
        }


        int filterCount = RippleittAppInstance
                .getInstance()
                .getSearchCategoryPayload()
                .keySet().size()
                + RippleittAppInstance
                .getInstance()
                .getSearchSubCategoryPayload()
                .keySet().size()
                + RippleittAppInstance
                .getInstance()
                .getSearchPriceRangePayload()
                .keySet().size();

        mTxtVwApplyFilter.setText("Apply Filters("
                + Integer.toString(filterCount) + ")");

        loadCategoryList();
        loadSubCategoryList();
        loadPriceRangeList();
        loadChips();
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}
