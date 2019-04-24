package com.rippleitt.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rippleitt.R;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.CustomTextWatcher;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.webservices.BuyNowApi;
import com.rippleitt.webservices.MakeOfferApi;
import com.sdsmdg.tastytoast.TastyToast;

public class SetYourPriceActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout mrelSetPriceBack;
    EditText medittxtAddPrice;
    Button mbtnSetPrice;
    EditText mEdtxtAddQty;
    ImageView imgProduct;
    TextView mtxtVwTitle, tvTotalAmount, tvTotalProductAmount, tvProductAmount, tvAdditionalFee;
    TextView tvVoucherText, tvVoucherAmount,tvName;
    private TextView mTxtVwListingTerms;
    private CheckBox mChkBxTerms;
    boolean isBuyNow = false;
    String listingPrice = "0";
    String voucher_id = "";
    View view3,view2;
    int voucherType = 0, voucherAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_your_price);
        init();
    }

    public void init() {
        mChkBxTerms = (CheckBox) findViewById(R.id.chkbxTerms);
        mTxtVwListingTerms = (TextView) findViewById(R.id.txtvwListingPolicyLauncher);
        mTxtVwListingTerms.setText(Html.fromHtml("<u>Rippleitt Terms & Conditions</u>"));
        mTxtVwListingTerms.setOnClickListener(this);
        mtxtVwTitle = (TextView) findViewById(R.id.txtvwSetPrice);
        imgProduct = (ImageView) findViewById(R.id.imgProduct);
        tvName = (TextView) findViewById(R.id.tvName);
        view3 = (View) findViewById(R.id.view3);
        view2 = (View) findViewById(R.id.view2);
        tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);
        tvVoucherText = (TextView) findViewById(R.id.tvVoucherText);
        tvVoucherAmount = (TextView) findViewById(R.id.tvVoucherAmount);
        tvAdditionalFee = (TextView) findViewById(R.id.tvAdditionalFee);
        tvProductAmount = (TextView) findViewById(R.id.tvProductAmount);
        tvTotalProductAmount = (TextView) findViewById(R.id.tvTotalProductAmount);
        mrelSetPriceBack = (RelativeLayout) findViewById(R.id.relSetPriceBack);
        mbtnSetPrice = (Button) findViewById(R.id.btnSetPrice);
        medittxtAddPrice = (EditText) findViewById(R.id.edittxtAddPrice);
        mEdtxtAddQty = (EditText) findViewById(R.id.edittxtAddQty);
        mrelSetPriceBack.setOnClickListener(this);
        mbtnSetPrice.setOnClickListener(this);
        medittxtAddPrice.addTextChangedListener(new CustomTextWatcher(
                medittxtAddPrice));

        tvVoucherAmount.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        tvVoucherText.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isBuyNow = bundle.getBoolean("isBuyNow", false);
            listingPrice = bundle.getString("price", "0");
            if (bundle.getString("voucher_id") != null) {
                voucher_id = bundle.getString("voucher_id");
//                tvTotalAmount= bundle.getString("voucher_id");

                voucherAmount = Integer.parseInt(bundle.getString("voucher_price"));
                voucherType = Integer.parseInt(bundle.getString("voucher_type"));

            }
        }

        tvTotalAmount.setText("Grand Total:- $0");
        tvProductAmount.setText("Listing Price:- $0");
        tvTotalProductAmount.setText("Total Cost:- $0");
        tvAdditionalFee.setText("Processing Fee:- $0");
        tvTotalAmount.setVisibility(View.GONE);
        tvAdditionalFee.setVisibility(View.GONE);
        tvProductAmount.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        tvTotalProductAmount.setVisibility(View.GONE);

        tvName.setText(RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getListing_name());
        Glide.with(getBaseContext())
                .load(RippleittAppInstance.formatPicPath(RippleittAppInstance.getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getListing_photos()[0].getPhoto_path()))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imgProduct);

        if (RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getListing_type().equalsIgnoreCase("2") && isBuyNow) {
            mEdtxtAddQty.setVisibility(View.GONE);
            mEdtxtAddQty.setText("1");
            tvProductAmount.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
            tvTotalProductAmount.setVisibility(View.VISIBLE);
            if (voucher_id != null && voucher_id.length() > 0) {
                tvVoucherAmount.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                tvVoucherText.setVisibility(View.VISIBLE);
                tvVoucherAmount.setText("Effective Cost:- $" + String.format("%.2f", calculateVoucherFee(String.valueOf(voucherAmount), voucherType, "1")));
            }
//                            tvAdditionalFee.setVisibility(View.VISIBLE);
//                            tvTotalAmount.setText("Grand Total:- $" + String.valueOf(String.format("%.2f", calculate())));
            tvProductAmount.setText("Listing Price:- $" + String.format("%.2f", Float.parseFloat(listingPrice)));
            tvTotalProductAmount.setText("Total Cost:- $" + String.format("%.2f", calculate("1")));

        }else if (RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getListing_type().equalsIgnoreCase("2")){
            mEdtxtAddQty.setText("1");
            mEdtxtAddQty.setVisibility(View.GONE);
        }else {
            mEdtxtAddQty.setVisibility(View.VISIBLE);
        }

        String existingBid = RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getMy_bid_amount();
        String existingQuantity = RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getMy_bid_quantity();
        if (!existingBid.equalsIgnoreCase("0")) {
            medittxtAddPrice.setText(existingBid);
            mEdtxtAddQty.setText(RippleittAppInstance.getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT().getMy_bid_quantity());
            mtxtVwTitle.setText("Update Offer");
            mbtnSetPrice.setText("Update Offer");
        } else {
            mtxtVwTitle.setText("Make an Offer");
            mbtnSetPrice.setText("Make Offer");
        }

        if (isBuyNow) {
            medittxtAddPrice.setVisibility(View.GONE);
            mtxtVwTitle.setText("Avail Now");
            mEdtxtAddQty.setHint("Quantity*");
            mbtnSetPrice.setText("Submit");
//            mEdtxtAddQty.setText(null);
//            tvTotalAmount.setVisibility(View.VISIBLE);
        } else {
            tvTotalAmount.setVisibility(View.GONE);
            tvAdditionalFee.setVisibility(View.GONE);
            tvProductAmount.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            tvTotalProductAmount.setVisibility(View.GONE);
        }

        if (existingQuantity != null || existingQuantity.equalsIgnoreCase("")) {

        } else {
            mEdtxtAddQty.setText(existingQuantity);
        }

        mEdtxtAddQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isBuyNow) {
                    if (s.length() > 0) {

//                            tvTotalAmount.setVisibility(View.VISIBLE);
                        tvProductAmount.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.VISIBLE);
                        tvTotalProductAmount.setVisibility(View.VISIBLE);
                        if (voucher_id != null && voucher_id.length() > 0) {
                            tvVoucherAmount.setVisibility(View.VISIBLE);
                            view3.setVisibility(View.VISIBLE);
                            tvVoucherText.setVisibility(View.VISIBLE);
                            tvVoucherAmount.setText("Effective Cost:- $" + String.format("%.2f", calculateVoucherFee(String.valueOf(voucherAmount), voucherType, mEdtxtAddQty.getText().toString().trim())));
                        }
//                            tvAdditionalFee.setVisibility(View.VISIBLE);
//                            tvTotalAmount.setText("Grand Total:- $" + String.valueOf(String.format("%.2f", calculate())));
                        tvProductAmount.setText("Listing Price:- $" + String.format("%.2f", Float.parseFloat(listingPrice)));
                        tvTotalProductAmount.setText("Total Cost:- $" + String.format("%.2f", calculate(mEdtxtAddQty.getText().toString().trim())));

//                            tvAdditionalFee.setText("Processing Fee:- $" + "12.00");
//                        }
                    } else {
                        tvTotalAmount.setVisibility(View.GONE);
                        tvAdditionalFee.setVisibility(View.GONE);
                        tvProductAmount.setVisibility(View.GONE);
                        view2.setVisibility(View.GONE);
                        tvTotalProductAmount.setVisibility(View.GONE);
                        tvTotalAmount.setText("Grand Total:- $0");
                        tvProductAmount.setText("Listing Price:- $0");
                        tvTotalProductAmount.setText("Total Cost:- $0");
                        tvAdditionalFee.setText("Processing Fee:- $0");
                        if (voucher_id != null && voucher_id.length() > 0) {
                            tvVoucherAmount.setVisibility(View.GONE);
                            view3.setVisibility(View.GONE);
                            tvVoucherText.setVisibility(View.GONE);
                        }
                    }
                } else {
                    tvTotalAmount.setVisibility(View.GONE);
                    tvAdditionalFee.setVisibility(View.GONE);
                    tvProductAmount.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    tvTotalProductAmount.setVisibility(View.GONE);
                    if (voucher_id != null && voucher_id.length() > 0) {
                        tvVoucherAmount.setVisibility(View.GONE);
                        view3.setVisibility(View.GONE);
                        tvVoucherText.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == mTxtVwListingTerms) {
//            String url = "http://rippleitt.com/listing_policies.php";
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
            Intent i = new Intent(SetYourPriceActivity.this, TermsConditionsActivity.class);
            startActivity(i);
        }

        if (view == mrelSetPriceBack) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
        if (view == mbtnSetPrice) {
            if (validate()) {
                if (isBuyNow) {
                    if (Double.parseDouble(mEdtxtAddQty.getText().toString())
                            >
                            Double.parseDouble(RippleittAppInstance
                                    .getInstance()
                                    .getSELECTED_LISTING_DETAIL_OBJECT()
                                    .getQuantity())
                    ) {
                        TastyToast.makeText(getBaseContext(), "Not enough quantity in stock!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        showConfirmation();
                    }
                } else {
//                    if (RippleittAppInstance.getInstance()
//                            .getSELECTED_LISTING_DETAIL_OBJECT()
//                            .getListing_type().equalsIgnoreCase("1")) {
                        new MakeOfferApi().makeOfferApi(this, medittxtAddPrice.getText().toString(),
                                mEdtxtAddQty.getText().toString().trim());
//                    } else {
//                        new MakeOfferApi().makeOfferApi(this, medittxtAddPrice.getText().toString(),
//                                "1");
//                    }
                }
            }
        }
    }

    private void showConfirmation() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        builder.setMessage("Confirm your purchases?");


        builder.setTitle(null)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

//                        if (RippleittAppInstance.getInstance()
//                                .getSELECTED_LISTING_DETAIL_OBJECT()
//                                .getListing_type().equalsIgnoreCase("1")) {

                            new BuyNowApi().buyNowApi(SetYourPriceActivity.this, mEdtxtAddQty.getText().toString().trim(), voucher_id);
//                        } else {
//                            new BuyNowApi().buyNowApi(SetYourPriceActivity.this, "1", voucher_id);
//                        }
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //addAttachments();
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.setCancelable(true);
        alert.show();
    }

    private double calculate(String qty) {
        double quantity = Float.parseFloat(qty);
        double price = Float.parseFloat(listingPrice);
        double total = quantity * price;
//        String str= String.valueOf(total+10.0f);
        return total;
    }

    private double calculateVoucherFee(String voucherAmount, int voucherType, String qty) {
//        if (mEdtxtAddQty.getText().toString().trim()==null)
        double quantity = Float.parseFloat(qty);
        double voucherPrice = Float.parseFloat(voucherAmount);
        double totalPrice = Float.parseFloat(listingPrice);
        double total = 0;

        if (voucherType == 1) {
            total = (totalPrice * quantity);
            if (total > voucherPrice) {
                total = (totalPrice * quantity) - voucherPrice;
            } else {
                tvVoucherAmount.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                tvVoucherText.setVisibility(View.GONE);
            }
        } else if (voucherType == 2) {
            double percent = (totalPrice * voucherPrice) / 100;
            total = (totalPrice - percent) * quantity;
        }
        return total;
    }

    private boolean validate() {

        if (mEdtxtAddQty.getText().toString().equalsIgnoreCase("")) {
            mEdtxtAddQty.setError("Please provide quantity");
            // TastyToast.makeText(this,"please provide quantity",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
            return false;
        }

        if (mEdtxtAddQty.getText().toString().equalsIgnoreCase("0")) {
            mEdtxtAddQty.setError("Please provide valid quantity");
            // TastyToast.makeText(this,"please provide quantity",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
            return false;
        }

        float parsedAmount = 0;

        if (Double.parseDouble(mEdtxtAddQty.getText().toString().trim())
                >
                Double.parseDouble(RippleittAppInstance
                        .getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT()
                        .getQuantity())
        ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Not enough quantity in stock!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }

        if (!isBuyNow) {
            if (medittxtAddPrice.getText().toString().equalsIgnoreCase("")) {
                medittxtAddPrice.setError("Please provide price");
                // TastyToast.makeText(this,"please provide bid amount",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                return false;
            }

            try {
                parsedAmount = Float.parseFloat(medittxtAddPrice.getText().toString().trim());
            } catch (Exception e) {

            }

        } else {
            try {
                parsedAmount = Float.parseFloat(RippleittAppInstance
                        .getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT()
                        .getListing_price());
            } catch (Exception e) {

            }
        }
        if (parsedAmount < 1) {
            medittxtAddPrice.setError("Price can not be less than $1");
            // TastyToast.makeText(this,"please provide bid amount",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
            return false;
        }


        if (!mChkBxTerms.isChecked()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please accept the Rippleitt Listing Policies.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            //mChkBxTerms.setError("Please acept listing terms");
            // TastyToast.makeText(this,"please acept listing terms",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
            return false;
        }

        if (voucher_id != null && voucher_id.length() > 0) {
            double quantity = Float.parseFloat(mEdtxtAddQty.getText().toString().trim());
            double voucherPrice = (double) voucherAmount;
            double totalPrice = Float.parseFloat(listingPrice);
            double total = 0;

            if (voucherType == 1) {
                total = (totalPrice * quantity);
                if (!(total > voucherPrice)) {
                    voucher_id = "";
                    return false;
                }
            }

        }


        return true;
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

}
