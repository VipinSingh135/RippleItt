package com.rippleitt.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rippleitt.R;
import com.rippleitt.adapters.RestictedDaysAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.callback.ItemClickListener;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.VoucherTemplate;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivityAddVoucher extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, ItemClickListener {

    private EditText medittxtVoucherName;
    private EditText medittxtPrice;
    private EditText medittxtQuantity;
    private EditText edittxtAddSellerPrice;
    private TextView tvExpiryDate, txtvwAddVoucher, tvRestrictedDays;
    private RelativeLayout mrelAddProduct_back;
    private LinearLayout linRefferAmount,linBuyerAmount;
    private LinearLayout linlytGive, linlytGiveGet;
    private TextView tvGive, tvGiveGet;
    private ImageView imgGive, imgGiveGet;
    private LinearLayout linlytPercent, linlytDollar, linlytFree;
    private TextView tvPercent, tvDollar, tvFree;
    private ImageView imgPercent, imgDollar, imgFree;

//    private Spinner mspinnerAmountType;

    private Button mbtnAddVoucher;

    private CheckBox mChkBxTC;
    private TextView mTxtVwTc;
//    String spinnerCategoryItem[] = {"%", "$"};

    //    ArrayAdapter<String> spinnerCategoryArr;
//
//    private RadioGroup mRdGrpType;
    private ProgressDialog mProgressDialog;

    /// objects for location management....

    int amountType = 2;
    int voucherType = 1;

    List<String> restictedDaysList;
    List<String> selectedDays;
    RestictedDaysAdapter restictedDaysAdapter;
    RecyclerView recyclerView;
    VoucherTemplate template;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voucher);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        init();

    }


    @Override
    public void onPause() {
        super.onPause();
        RippleittAppInstance.getInstance().setCURRENT_VOUCHER_OBJECT(null);
        mProgressDialog.dismiss();
    }

    public void init() {

        mChkBxTC = (CheckBox) findViewById(R.id.chkbxTerms);
        tvExpiryDate = (TextView) findViewById(R.id.tvExpiryDate);
        tvRestrictedDays = (TextView) findViewById(R.id.tvRestrictedDays);
        tvRestrictedDays = (TextView) findViewById(R.id.tvRestrictedDays);
        txtvwAddVoucher = (TextView) findViewById(R.id.txtvwAddVoucher);

        linlytGive = findViewById(R.id.linlytGive);
        linlytGiveGet = findViewById(R.id.linlytGiveGet);
        linRefferAmount = findViewById(R.id.linRefferAmount);
        linBuyerAmount = findViewById(R.id.linBuyerAmount);
        tvGive = findViewById(R.id.tvGive);
        tvGiveGet = findViewById(R.id.tvGiveGet);
        imgGive = findViewById(R.id.imgGive);
        imgGiveGet = findViewById(R.id.imgGiveGet);

        linlytPercent = findViewById(R.id.linlytPercent);
        linlytDollar = findViewById(R.id.linlytDollar);
        linlytFree = findViewById(R.id.linlytFree);
        tvPercent = findViewById(R.id.tvPercent);
        tvDollar = findViewById(R.id.tvDollar);
        tvFree = findViewById(R.id.tvFree);
        imgPercent = findViewById(R.id.imgPercent);
        imgDollar = findViewById(R.id.imgDollar);
        imgFree = findViewById(R.id.imgFree);

        mTxtVwTc = (TextView) findViewById(R.id.txtvwTCLauncher);
        mTxtVwTc.setText(Html.fromHtml("<u>Rippleitt Terms & Conditions</u>"));
        mTxtVwTc.setOnClickListener(this);

        medittxtPrice = (EditText) findViewById(R.id.edittxtAddProductPrice);
        medittxtVoucherName = (EditText) findViewById(R.id.edittxtAddProductName);
        medittxtQuantity = (EditText) findViewById(R.id.edtxtQuantity);
        edittxtAddSellerPrice = (EditText) findViewById(R.id.edittxtAddSellerPrice);
        mrelAddProduct_back = (RelativeLayout) findViewById(R.id.relAddProduct_back);
//        mspinnerAmountType = (Spinner) findViewById(R.id.spnrType);
        mbtnAddVoucher = (Button) findViewById(R.id.btnAddVoucher);
//        mRdGrpType = (RadioGroup) findViewById(R.id.radioGroupProductType);
//        mRdGrpType.setOnCheckedChangeListener(this);
        mrelAddProduct_back.setOnClickListener(this);
        mbtnAddVoucher.setOnClickListener(this);
        tvExpiryDate.setOnClickListener(this);
        tvRestrictedDays.setOnClickListener(this);
        linlytDollar.setOnClickListener(this);
        linlytPercent.setOnClickListener(this);
        linlytFree.setOnClickListener(this);
        linlytGiveGet.setOnClickListener(this);
        linlytGive.setOnClickListener(this);

        work();

    }

    public void work() {
        //==================spinner_category===============
//        spinnerCategoryArr = new ArrayAdapter<String>
//                (this, android.R.layout.simple_spinner_item, spinnerCategoryItem);
//        spinnerCategoryArr.setDropDownViewResource
//                (android.R.layout.simple_spinner_dropdown_item);
//        mspinnerAmountType.setAdapter(spinnerCategoryArr);

        restictedDaysList = new ArrayList<>();
        restictedDaysList.add("Sunday");
        restictedDaysList.add("Monday");
        restictedDaysList.add("Tuesday");
        restictedDaysList.add("Wednesday");
        restictedDaysList.add("Thursday");
        restictedDaysList.add("Friday");
        restictedDaysList.add("Saturday");

        selectedDays = new ArrayList<>();
        linRefferAmount.setVisibility(View.GONE);

        template = RippleittAppInstance.getInstance().getCURRENT_VOUCHER_OBJECT();
        if (template != null) {

            txtvwAddVoucher.setText("Edit Voucher");
            mbtnAddVoucher.setText("Edit");

            medittxtVoucherName.setText(template.getName());
            if (template.getExpiry() != null) {
                tvExpiryDate.setText(template.getExpiry().split(" ")[0]);
            }

            if (template.getMode().equals("1")) {
//                mRdGrpType.check(R.id.rdBtnGive);
                linlytGive.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
                linlytGiveGet.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
                imgGive.setImageResource(R.drawable.white_cicle);
                imgGiveGet.setImageResource(R.drawable.light_blue_circle);
                voucherType = 1;
                tvGive.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
                tvGiveGet.setTextColor(getResources().getColor(R.color.grey2));
                linRefferAmount.setVisibility(View.GONE);

            } else {
//                mRdGrpType.check(R.id.rdBtnGiveGet);
                linlytGiveGet.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
                linlytGive.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
                imgGiveGet.setImageResource(R.drawable.white_cicle);
                imgGive.setImageResource(R.drawable.light_blue_circle);
                voucherType = 2;
                tvGiveGet.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
                tvGive.setTextColor(getResources().getColor(R.color.grey2));
                linRefferAmount.setVisibility(View.VISIBLE);

            }

            if (template.getType().equals("1")) {
//                mspinnerAmountType.setSelection(1);
                linlytDollar.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
                linlytPercent.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
                linlytFree.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
                imgDollar.setImageResource(R.drawable.white_cicle);
                imgPercent.setImageResource(R.drawable.light_blue_circle);
                imgFree.setImageResource(R.drawable.light_blue_circle);
                amountType = 1;
                tvDollar.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
                tvPercent.setTextColor(getResources().getColor(R.color.grey2));
                tvFree.setTextColor(getResources().getColor(R.color.grey2));
                linBuyerAmount.setVisibility(View.VISIBLE);
                linRefferAmount.setVisibility(View.VISIBLE);

            } else if (template.getType().equals("2")) {
//                mspinnerAmountType.setSelection(1);
                linlytPercent.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
                linlytDollar.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
                linlytFree.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
                imgDollar.setImageResource(R.drawable.white_cicle);
                imgPercent.setImageResource(R.drawable.light_blue_circle);
                imgFree.setImageResource(R.drawable.light_blue_circle);
                amountType = 2;
                tvDollar.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
                tvPercent.setTextColor(getResources().getColor(R.color.grey2));
                tvFree.setTextColor(getResources().getColor(R.color.grey2));
                linBuyerAmount.setVisibility(View.VISIBLE);
                linRefferAmount.setVisibility(View.VISIBLE);

            } else if (template.getType().equals("3")) {
                linlytFree.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
                linlytPercent.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
                linlytDollar.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
                imgFree.setImageResource(R.drawable.white_cicle);
                imgPercent.setImageResource(R.drawable.light_blue_circle);
                imgDollar.setImageResource(R.drawable.light_blue_circle);
                amountType = 3;
                tvFree.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
                tvDollar.setTextColor(getResources().getColor(R.color.grey2));
                tvPercent.setTextColor(getResources().getColor(R.color.grey2));
                linBuyerAmount.setVisibility(View.GONE);
                linRefferAmount.setVisibility(View.GONE);
//                mspinnerAmountType.setSelection(0);
            }

            medittxtPrice.setText(template.getAmount());
            edittxtAddSellerPrice.setText(template.getGet_amount());

            medittxtQuantity.setText(template.getQuantity());
//            medittxtQuantity.setFocusable(false);
//            medittxtQuantity.setClickable(false);
            if (template.getRestrictedDays() != null) {
                tvRestrictedDays.setText(template.getRestrictedDays());

                if (template.getRestrictedDays() != null) {
                    String str[] = template.getRestrictedDays().split(",");
                    if (str.length > 0) {
                        for (String obj : str) {
                            selectedDays.add(obj.trim());
                        }
                    }
                }
            }

            medittxtPrice.setFocusable(false);
            medittxtPrice.setClickable(false);
//            mspinnerAmountType.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {

        if (mTxtVwTc == view) {
            Intent i = new Intent(ActivityAddVoucher.this, TermsConditionsActivity.class);
            startActivity(i);
        }
        if (linlytGive == view) {
            linlytGive.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytGiveGet.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            imgGive.setImageResource(R.drawable.white_cicle);
            imgGiveGet.setImageResource(R.drawable.light_blue_circle);
            voucherType = 1;
            tvGive.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvGiveGet.setTextColor(getResources().getColor(R.color.grey2));
            linRefferAmount.setVisibility(View.GONE);
            if (amountType==3){
                linBuyerAmount.setVisibility(View.GONE);
                linRefferAmount.setVisibility(View.GONE);
                edittxtAddSellerPrice.setText("10");
                medittxtPrice.setText("10");
            }
        }
        if (linlytGiveGet == view) {
            linlytGiveGet.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytGive.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            imgGiveGet.setImageResource(R.drawable.white_cicle);
            imgGive.setImageResource(R.drawable.light_blue_circle);
            voucherType = 2;
            tvGiveGet.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvGive.setTextColor(getResources().getColor(R.color.grey2));
            linRefferAmount.setVisibility(View.VISIBLE);
            if (amountType==3){
                linBuyerAmount.setVisibility(View.GONE);
                linRefferAmount.setVisibility(View.GONE);
                edittxtAddSellerPrice.setText("10");
                medittxtPrice.setText("10");
            }
        }

        if (linlytDollar == view) {

            linlytDollar.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytPercent.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            linlytFree.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            imgDollar.setImageResource(R.drawable.white_cicle);
            imgPercent.setImageResource(R.drawable.light_blue_circle);
            imgFree.setImageResource(R.drawable.light_blue_circle);
            amountType = 1;
            tvDollar.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvPercent.setTextColor(getResources().getColor(R.color.grey2));
            tvFree.setTextColor(getResources().getColor(R.color.grey2));
            linBuyerAmount.setVisibility(View.VISIBLE);
            linRefferAmount.setVisibility(View.VISIBLE);
            edittxtAddSellerPrice.setText("");
            medittxtPrice.setText("");
            if (voucherType==1){
                linRefferAmount.setVisibility(View.GONE);
            }
        }
        if (linlytPercent == view) {
            linlytPercent.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytDollar.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            linlytFree.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            imgPercent.setImageResource(R.drawable.white_cicle);
            imgDollar.setImageResource(R.drawable.light_blue_circle);
            imgFree.setImageResource(R.drawable.light_blue_circle);
            amountType = 2;
            tvPercent.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvDollar.setTextColor(getResources().getColor(R.color.grey2));
            tvFree.setTextColor(getResources().getColor(R.color.grey2));
            linBuyerAmount.setVisibility(View.VISIBLE);
            linRefferAmount.setVisibility(View.VISIBLE);
            edittxtAddSellerPrice.setText("");
            medittxtPrice.setText("");
            if (voucherType==1){
                linRefferAmount.setVisibility(View.GONE);
            }
        }
        if (linlytFree == view) {
            linlytFree.setBackground(getResources().getDrawable(R.drawable.tab_green_centre));
            linlytPercent.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            linlytDollar.setBackground(getResources().getDrawable(R.drawable.tab_green_centre_outline));
            imgFree.setImageResource(R.drawable.white_cicle);
            imgPercent.setImageResource(R.drawable.light_blue_circle);
            imgDollar.setImageResource(R.drawable.light_blue_circle);
            amountType = 3;
            tvFree.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            tvDollar.setTextColor(getResources().getColor(R.color.grey2));
            tvPercent.setTextColor(getResources().getColor(R.color.grey2));
            linBuyerAmount.setVisibility(View.GONE);
            linRefferAmount.setVisibility(View.GONE);
            edittxtAddSellerPrice.setText("10");
            medittxtPrice.setText("10");
        }
        if (view == mrelAddProduct_back) {
            finish();
        }

        if (view == tvExpiryDate) {
            showDatePickerDialog();
        }

        if (view == tvRestrictedDays) {
            showRestictedDaysDialog(ActivityAddVoucher.this, "Select Days");
        }


        if (view == mbtnAddVoucher) {
            if (validate()) {
                if (template != null && template.getVoucherId() != null) {
                    editVoucherApi();
                } else
                    addVoucherApi();
            }
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.getTime().getYear() + 1900;
        int month = calendar.getTime().getMonth();
        int date = calendar.getTime().getDate();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, date);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();

    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean validate() {

        if (medittxtVoucherName.getText().toString().trim().equalsIgnoreCase("")) {

            medittxtVoucherName.setError("Please enter voucher name");

            // Toast.makeText(ActivityAddVoucher.this, "Please enter product name",Toast.LENGTH_LONG).show();
            return false;
        }

//        if (mspinnerAmountType
//                .getSelectedItem().toString()
//                .equalsIgnoreCase("")) {
//            Toast.makeText(ActivityAddVoucher.this, "Please select Amount type", Toast.LENGTH_LONG).show();
//
//            return false;
//        }


        if (medittxtPrice.getText().toString().trim().equalsIgnoreCase("")) {
            medittxtPrice.setError("Please enter voucher price");

            // Toast.makeText(ActivityAddVoucher.this, "Please enter listing description    ",Toast.LENGTH_LONG).show();
            return false;
        }

//        if (mRdGrpType.is()<0)

        if (!mChkBxTC.isChecked()) {
            com.rippleitt.utils.CommonUtils
                    .showSingleButtonAlert(ActivityAddVoucher.this,
                            "Please accept the Rippleitt Listing Policies");

            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        RippleittAppInstance.getInstance().getListingImages().clear();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

//    @Override
//    public void onCheckedChanged(RadioGroup radioGroup, int i) {
//
//        if (i == R.id.rdBtnGive) {
//            voucherType = 1;
//        } else if (i == R.id.rdBtnGiveGet) {
//            voucherType = 2;
//        }
//    }

    public void addVoucherApi() {
        final ArrayList<HashMap<String, String>> arry_Details = new ArrayList<>();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Submitting your offer");
        mProgressDialog.show();

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL + RippleittAppInstance.ADD_VOUCHER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressDialog.dismiss();
                try {

                    //  maviLoaderHome.hide();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("response_code");
                    String msg = object.getString("response_message");

                    if (status.equalsIgnoreCase("1")) {
//                        if(card_flag.equalsIgnoreCase("1")){
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("voucher_id", object.getString("voucher_id"));
                        returnIntent.putExtra("amount_type", amountType);
                        setResult(Activity.RESULT_OK, returnIntent);
                        TastyToast.makeText(ActivityAddVoucher.this, "Congratulations! Your Voucher has been added", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        finish();
                    } else {
                        TastyToast.makeText(ActivityAddVoucher.this, msg, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // sharedPreferences=getSharedPreferences(Global_constants.pref, Context.MODE_PRIVATE);;
                params.put("token", PreferenceHandler.readString(ActivityAddVoucher.this,
                        PreferenceHandler.AUTH_TOKEN, ""));

                params.put("name", medittxtVoucherName.getText().toString());
                params.put("quantity", medittxtQuantity.getText().toString());
                params.put("amount", medittxtPrice.getText().toString());
                params.put("restricted_days", tvRestrictedDays.getText().toString());

//                if (mspinnerAmountType.getSelectedItemPosition() == 0) {
//                    amountType = 2;
//                } else {
//                    amountType = 1;
//                }

                params.put("type", String.valueOf(amountType));
                params.put("mode", String.valueOf(voucherType));

                if (voucherType == 1) {
                    params.put("get_amount", medittxtPrice.getText().toString());
                } else {
                    params.put("get_amount", edittxtAddSellerPrice.getText().toString());
                }

                if (tvExpiryDate.getText().toString() != null && tvExpiryDate.getText().toString().length() > 0) {
                    params.put("expiry_date", tvExpiryDate.getText().toString());
                    params.put("has_expiry", String.valueOf(1));
                } else {
                    params.put("expiry_date", "");
                    params.put("has_expiry", String.valueOf(0));
                }
//                params.put("mode", String.valueOf(voucherType));

                return params;
            }
        };
        myRqst.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(myRqst);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
        tvExpiryDate.setText(date);
    }

    public void showRestictedDaysDialog(final Context context, String title) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_resticted_days);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView textView = (TextView) dialog.findViewById(R.id.tVDialogTitle);
        textView.setText(title);
        Button btnDone = (Button) dialog.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvRestrictedDays.setText(selectedDays.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
                dialog.dismiss();
            }
        });

        restictedDaysAdapter = new RestictedDaysAdapter(getBaseContext(), restictedDaysList, selectedDays, this);
        recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(restictedDaysAdapter);

        dialog.show();
    }

    @Override
    public void onItemClick(int pos) {
        if (selectedDays.contains(restictedDaysList.get(pos))) {
            selectedDays.remove(restictedDaysList.get(pos));
            restictedDaysAdapter.notifyItemChanged(pos);
        } else {
            selectedDays.add(restictedDaysList.get(pos));
            restictedDaysAdapter.notifyItemChanged(pos);
        }
    }


    public void editVoucherApi() {
        final ArrayList<HashMap<String, String>> arry_Details = new ArrayList<>();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Submitting your offer");
        mProgressDialog.show();

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final StringRequest myRqst = new StringRequest(Request.Method.POST, RippleittAppInstance.BASE_URL + RippleittAppInstance.EDIT_VOUCHER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressDialog.dismiss();
                try {

                    //  maviLoaderHome.hide();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("response_code");
                    String msg = object.getString("response_message");

                    if (status.equalsIgnoreCase("1")) {
//                        if(card_flag.equalsIgnoreCase("1")){
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("voucher_id", object.getString("voucher_id"));
                        setResult(Activity.RESULT_OK, returnIntent);
                        TastyToast.makeText(ActivityAddVoucher.this, "Congratulations! Your Voucher has been added", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        finish();
                    } else {
                        TastyToast.makeText(ActivityAddVoucher.this, msg, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // sharedPreferences=getSharedPreferences(Global_constants.pref, Context.MODE_PRIVATE);;
                params.put("token", PreferenceHandler.readString(ActivityAddVoucher.this,
                        PreferenceHandler.AUTH_TOKEN, ""));

                params.put("name", medittxtVoucherName.getText().toString());
                params.put("quantity", medittxtQuantity.getText().toString());
                params.put("amount", medittxtPrice.getText().toString());
                params.put("restricted_days", tvRestrictedDays.getText().toString());

//                if (mspinnerAmountType.getSelectedItemPosition() == 0) {
//                    amountType = 2;
//                } else {
//                    amountType = 1;
//                }

                params.put("type", String.valueOf(amountType));
                params.put("mode", String.valueOf(voucherType));

                if (voucherType == 1) {
                    params.put("get_amount", medittxtPrice.getText().toString());
                } else {
                    params.put("get_amount", edittxtAddSellerPrice.getText().toString());
                }

                if (tvExpiryDate.getText().toString() != null && tvExpiryDate.getText().toString().length() > 0) {
                    params.put("expiry_date", tvExpiryDate.getText().toString());
                    params.put("has_expiry", String.valueOf(1));
                } else {
                    params.put("expiry_date", "");
                    params.put("has_expiry", String.valueOf(0));
                }
//                params.put("mode", String.valueOf(voucherType));
                params.put("voucher_id", String.valueOf(template.getVoucherId()));

                return params;
            }
        };
        myRqst.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(myRqst);

    }


}
