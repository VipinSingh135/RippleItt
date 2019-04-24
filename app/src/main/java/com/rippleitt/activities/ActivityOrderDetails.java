package com.rippleitt.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.adapters.CustomViewPagerAdapter;
import com.rippleitt.adapters.ListingBidsAdapter;
import com.rippleitt.callback.WishlistSelectCallback;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.BidTemplate;
import com.rippleitt.modals.OtherProductsTemplate;
import com.rippleitt.modals.ProductDetailsResponseTemplate;
import com.rippleitt.utils.CommonUtils;
import com.rippleitt.webservices.AddToWishListApi;
import com.rippleitt.webservices.RemoveProductWishlist;
import com.robohorse.pagerbullet.PagerBullet;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pc on 4/6/2018.
 */

public class ActivityOrderDetails extends AppCompatActivity implements View.OnClickListener, WishlistSelectCallback {


    // RelativeLayout mrelViewProfileDetails;
    LinearLayout linearDiscountedPrice;
    RelativeLayout mrelProductDetailsback, relProductPriceDetails, relGatewayFee, relDiscountedFee, relServiceCost, relAmountReceived;
    ImageView mimgVwProductImage;
    ImageView mimgVwAddToWishlist;
    TextView mtxtVwProductName;
    TextView mtxtVwProductDetail;
    TextView mtxtVwUserNameDetails;
    TextView mtxtVwAddrDetails;
    TextView txtVwQuantity, txtVwServiceCost, txtVwSingleProductPrice, txtVwProductPrice, txtVwTotalPrice, mtxtVwProductPrice;
    TextView tvDollarSign, txtVwGatewayFee, txtVwDiscountedFee, tvDiscountTitle, tvProductQuantity, tvProductPrice, tvtxtVwServiceCost, tvAmountReceived, txtVwAmountReceived;
    TextView mTxtVwReferAmount;
    TextView mTxtVwMyBid;
    Button mbtnReferAFriend;
    PagerBullet vwPagerProductImages;
    CircleImageView mimgVwprofileImageDetails;
    CustomViewPagerAdapter customViewPagerAdapter;
    private final int REFRESH_DATA = 78;
    ProgressDialog pDliaog;
    private ScrollView mScrlViewParent;
    private ListingBidsAdapter bidsAdapter;
    private LinearLayout mLinLytRootView;
    private TextView mTxtVwShowMore, mTxtVwNoRating, tvNeedHelp, tvDiscountedPrice;
    private Button mBtnSubmitRating;
    private Button mBtnMakePayment;
    private ImageView mImgVwRatingStarOne, mImgVwRatingStarTwo,
            mImgVwRatingStarThree, mImgVwRatingStarFour, mImgVwRatingStarFive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        init();
        work();
    }

    public void init() {
        mTxtVwNoRating = (TextView) findViewById(R.id.txtvwNoRating);
        tvNeedHelp = (TextView) findViewById(R.id.tvNeedHelp);
        tvDiscountTitle = (TextView) findViewById(R.id.tvDiscountTitle);
        mTxtVwNoRating.setVisibility(View.GONE);
        mTxtVwNoRating.setPaintFlags(mTxtVwNoRating.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mTxtVwNoRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtxtVwUserNameDetails.performClick();
            }
        });
        mBtnSubmitRating = (Button) findViewById(R.id.btnSubmitRating);
        mBtnMakePayment = (Button) findViewById(R.id.btnMakePayment);
        mBtnMakePayment.setOnClickListener(this);
        mBtnSubmitRating.setOnClickListener(this);
        mScrlViewParent = (ScrollView) findViewById(R.id.scrlVwParent);
        pDliaog = new ProgressDialog(this);
        pDliaog.setCancelable(false);
        RippleittAppInstance.getInstance().setFullScreenImageMode(1);
        mTxtVwShowMore = (TextView) findViewById(R.id.txtvwShowMore);
        txtVwTotalPrice = (TextView) findViewById(R.id.txtVwTotalPrice);
        txtVwProductPrice = (TextView) findViewById(R.id.tvProductAmount);
        tvAmountReceived = (TextView) findViewById(R.id.tvAmountReceived);
        tvProductPrice = (TextView) findViewById(R.id.tvProductPrice);
        tvDollarSign = (TextView) findViewById(R.id.tvDollarSign);
        tvProductQuantity = (TextView) findViewById(R.id.tvProductQuantity);
        tvtxtVwServiceCost = (TextView) findViewById(R.id.tvtxtVwServiceCost);
        txtVwAmountReceived = (TextView) findViewById(R.id.txtVwAmountReceived);
        txtVwSingleProductPrice = (TextView) findViewById(R.id.txtVwSingleProductPrice);
        txtVwQuantity = (TextView) findViewById(R.id.txtVwQuantity);
        txtVwServiceCost = (TextView) findViewById(R.id.txtVwServiceCost);
        txtVwGatewayFee = (TextView) findViewById(R.id.txtVwGatewayFee);
        txtVwDiscountedFee = (TextView) findViewById(R.id.txtVwDiscountedFee);
        relAmountReceived = (RelativeLayout) findViewById(R.id.relAmountReceived);
        relServiceCost = (RelativeLayout) findViewById(R.id.relServiceCost);
        relProductPriceDetails = (RelativeLayout) findViewById(R.id.relProductPriceDetails);
        relGatewayFee = (RelativeLayout) findViewById(R.id.relGatewayFee);
        relDiscountedFee = (RelativeLayout) findViewById(R.id.relDiscountedFee);
        linearDiscountedPrice = (LinearLayout) findViewById(R.id.linearDiscountedPrice);
        tvDiscountedPrice = findViewById(R.id.tvDiscountedPrice);

        mImgVwRatingStarOne = (ImageView) findViewById(R.id.imgvwRatingStarOne);
        mImgVwRatingStarTwo = (ImageView) findViewById(R.id.imgvwRatingStarTwo);
        mImgVwRatingStarThree = (ImageView) findViewById(R.id.imgvwRatingStarThree);
        mImgVwRatingStarFour = (ImageView) findViewById(R.id.imgvwRatingStarFour);
        mImgVwRatingStarFive = (ImageView) findViewById(R.id.imgvwRatingStarFive);

        mTxtVwShowMore.setPaintFlags(mTxtVwShowMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mTxtVwShowMore.setOnClickListener(this);
        // new GetProductDetailsApi().getProductDetailsApi(this, RippleittAppInstance.PRODUCT_ID);
        vwPagerProductImages = (PagerBullet) findViewById(R.id.vwPagerProductImages);
        //   mrelViewProfileDetails=(RelativeLayout)findViewById(R.id.relViewProfileDetails);
        mrelProductDetailsback = (RelativeLayout) findViewById(R.id.relProductDetailsback);
        mtxtVwProductName = (TextView) findViewById(R.id.txtVwProductName);
        mtxtVwAddrDetails = (TextView) findViewById(R.id.txtVwAddrDetails);
        mtxtVwProductDetail = (TextView) findViewById(R.id.txtVwProductDetail);
        mtxtVwProductPrice = (TextView) findViewById(R.id.txtVwProductPrice);
        mTxtVwReferAmount = (TextView) findViewById(R.id.txtVwProductReferAmount);
        mtxtVwUserNameDetails = (TextView) findViewById(R.id.txtVwUserNameDetails);
        mimgVwAddToWishlist = (ImageView) findViewById(R.id.imgVwAddToWishlist);
        mbtnReferAFriend = (Button) findViewById(R.id.btnReferAFriend);
        mimgVwprofileImageDetails = (CircleImageView) findViewById(R.id.imgVwprofileImageDetails);
        mtxtVwUserNameDetails.setPaintFlags(mtxtVwUserNameDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mtxtVwUserNameDetails.setOnClickListener(this);

        // mrelViewProfileDetails.setOnClickListener(this);
        mbtnReferAFriend.setOnClickListener(this);
        tvNeedHelp.setOnClickListener(this);
        mrelProductDetailsback.setOnClickListener(this);
        mimgVwAddToWishlist.setOnClickListener(this);
        if (RippleittAppInstance.getInstance().isDisputeMode()) {// user is viewing self listing
            tvNeedHelp.setVisibility(View.GONE);
        } else {
            tvNeedHelp.setVisibility(View.VISIBLE);
        }
        configViewPagerClickListener();
        populateRatingStars();
    }

    private void configViewPagerClickListener() {

    }

    public void work() {

        customViewPagerAdapter = new CustomViewPagerAdapter(ActivityOrderDetails.this);
        vwPagerProductImages = (PagerBullet) findViewById(R.id.vwPagerProductImages);
        vwPagerProductImages.setIndicatorTintColorScheme(Color.WHITE, Color.parseColor("#289b98"));
        vwPagerProductImages.setAdapter(customViewPagerAdapter);
        //===========setProductImage================

        if (RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getReview_submitted().equalsIgnoreCase("1")) {
            mBtnSubmitRating.setVisibility(View.GONE);
        }

//        String str= RippleittAppInstance.getInstance()
//                .getSELECTED_LISTING_DETAIL_OBJECT()
//                .getIs_paid();
        if (RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getIs_paid().equals("2")) {
            mBtnMakePayment.setVisibility(View.GONE);
            tvNeedHelp.setVisibility(View.GONE);

//            mbtnReferAFriend.setVisibility(View.VISIBLE);
            mBtnSubmitRating.setVisibility(View.VISIBLE);
        } else if (RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getIs_paid().equals("1")) {
            tvNeedHelp.setVisibility(View.VISIBLE);

            if (RippleittAppInstance.getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT()
                    .getIs_buying().equals("1")) {

                mBtnMakePayment.setText("Release Payment");
            } else {
                mBtnMakePayment.setText("Request Payment");
            }
//            mbtnReferAFriend.setVisibility(View.GONE);
            mBtnSubmitRating.setVisibility(View.GONE);
            mBtnMakePayment.setVisibility(View.VISIBLE);
        } else if (RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getIs_paid().equals("3")) {
            mBtnMakePayment.setVisibility(View.GONE);
            tvNeedHelp.setVisibility(View.GONE);
        } else {
//            mbtnReferAFriend.setVisibility(View.GONE);
            mBtnSubmitRating.setVisibility(View.GONE);
            tvNeedHelp.setVisibility(View.VISIBLE);
        }

        ///===============setProductName and description========
        mtxtVwProductName.setText(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getListing_name());
        mtxtVwProductDetail.setText(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getListing_description());

        if (RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getIs_buying().equalsIgnoreCase("1")) {

//            mBtnMakePayment.setVisibilit y(View.GONE);
            mtxtVwUserNameDetails.setText(RippleittAppInstance
                    .getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT().getSellerarray().getFullName());
            mbtnReferAFriend.setText("Contact Seller");
            mBtnSubmitRating.setText("Write a Review");
            relAmountReceived.setVisibility(View.GONE);
            relServiceCost.setVisibility(View.GONE);
            relProductPriceDetails.setVisibility(View.GONE);
            relGatewayFee.setVisibility(View.GONE);
            relDiscountedFee.setVisibility(View.GONE);
            tvDollarSign.setText("Processing Fee: ");
            if (RippleittAppInstance
                    .getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT().getDirect_buy().equals("1")) {
                tvProductPrice.setText("Listing Price :");
            } else {
                tvProductPrice.setText("Accepted Price:");
            }
            try {
                Picasso.with(this)
                        .load(RippleittAppInstance
                                .formatPicPath(RippleittAppInstance
                                        .getInstance()
                                        .getSELECTED_LISTING_DETAIL_OBJECT()
                                        .getSellerarray().getImage()))
                        .error(R.drawable.default_profile_icon)
                        .placeholder(R.drawable.default_profile_icon)
                        .into(mimgVwprofileImageDetails);

            } catch (Exception e) {
                mimgVwprofileImageDetails.setImageResource(R.drawable.default_profile_icon);
            }


        } else {
//            mBtnMakePayment.setVisibility(View.GONE);
            mtxtVwUserNameDetails.setText(RippleittAppInstance
                    .getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT().getSellerarray().getFullName());
            mbtnReferAFriend.setText("Contact Buyer");
            mBtnSubmitRating.setText("Write a Review");
            if (RippleittAppInstance
                    .getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT().getIs_paid().equals("1")) {
                tvProductQuantity.setText("Quantity Purchased :");
            } else {
                tvProductQuantity.setText("Quantity Requested :");
            }

            if (RippleittAppInstance
                    .getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT().getDirect_buy().equals("1")) {
                tvProductPrice.setText("Listing Price :");
            } else {
                tvProductPrice.setText("Accepted Price :");
            }
//                    RippleittAppInstance
//                    .getInstance()
//                    .getSELECTED_LISTING_DETAIL_OBJECT().getDirect_buy().equals("1")){
            relProductPriceDetails.setVisibility(View.GONE);
            relGatewayFee.setVisibility(View.GONE);
            relDiscountedFee.setVisibility(View.GONE);
            relServiceCost.setVisibility(View.GONE);
            relAmountReceived.setVisibility(View.GONE);
//            relAmountReceived.setVisibility(View.VISIBLE);
//            relServiceCost.setVisibility(View.VISIBLE);
//            tvDollarSign.setText("Processing Fee: ");
//            tvtxtVwServiceCost.setText("Refferal Fee :");
//            txtVwGatewayFee.setText("Refferal Fee :");
//            txtVwGatewayFee.setText("Discounted Fee :");
//            tvDollarSign.setText("RippleIt Fee :");

            try {

                txtVwServiceCost.setText("$" + String.format("%.2f", Float.parseFloat(RippleittAppInstance
                        .getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getRipple_fee())));
                txtVwGatewayFee.setText("$" + String.format("%.2f", Float.parseFloat(RippleittAppInstance
                        .getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getGateway_fee())));
                txtVwDiscountedFee.setText("$" + String.format("%.2f", calculateDiscountedPrice(
                        RippleittAppInstance
                                .getInstance()
                                .getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_value(),
                        RippleittAppInstance
                                .getInstance()
                                .getSELECTED_LISTING_DETAIL_OBJECT().getOrder_value()
                )));

//             txtVwServiceCost.setText(RippleittAppInstance
//                    .getInstance()
//                    .getSELECTED_LISTING_DETAIL_OBJECT().getReferral_fee());

                txtVwTotalPrice.setText("$" + String.format("%.2f", Float.parseFloat(RippleittAppInstance
                        .getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getReferral_fee())));

//            txtVwProductPrice.setText("$" + String.format("%.2f", calculate()));
                Float totalCharged = Float.parseFloat(RippleittAppInstance
                        .getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getReferral_fee())
                        + Float.parseFloat(RippleittAppInstance
                        .getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getRipple_fee());
                Float amountReceived = calculate() - totalCharged;
                txtVwAmountReceived.setText("$" + String.format("%.2f", amountReceived));
            } catch (Exception e) {
                e.printStackTrace();
            }
//            }else {
//                relAmountReceived.setVisibility(View.GONE);
//                relServiceCost.setVisibility(View.GONE);
//                relProductPriceDetails.setVisibility(View.GONE);
//            }

            try {
                Picasso.with(this)
                        .load(RippleittAppInstance
                                .formatPicPath(RippleittAppInstance
                                        .getInstance()
                                        .getSELECTED_LISTING_DETAIL_OBJECT()
                                        .getSellerarray().getImage()))
                        .placeholder(R.drawable.default_profile_icon)
                        .error(R.drawable.default_profile_icon)
                        .into(mimgVwprofileImageDetails);

            } catch (Exception e) {
                mimgVwprofileImageDetails.setImageResource(R.drawable.default_profile_icon);
            }

        }


        /*
        mtxtVwAddrDetails.setText(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getLocation().getAddress());
                */
        mtxtVwProductPrice.setText("$" + RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getListing_price());
        txtVwQuantity.setText(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getQuantity());
//        txtVwServiceCost.setText("$" + String.format("%.2f", calculateServiceFee()));
        if (RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getBid_price() != null && !RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getBid_price().equals("")) {
            txtVwSingleProductPrice.setText("$" + String.format("%.2f", Float.parseFloat(RippleittAppInstance
                    .getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT().getBid_price())));
        }
//        txtVwProductPrice.setText("$" + String.format("%.2f", calculate()));
        txtVwProductPrice.setText("$" + String.format("%.2f", Float.parseFloat(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getOrder_value())));
//        txtVwTotalPrice.setText("$" + String.format("%.2f", calculate() + calculateServiceFee()));
        mTxtVwReferAmount.setText(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getRefer_amount());

        RippleittAppInstance.WISHLIST_STATUS = "0";

        //=======set Wishlist======
        if (RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getIsaddedtowishlist() == 0) {
            mimgVwAddToWishlist.setBackgroundResource(R.drawable.wishlist_icon);
            RippleittAppInstance.WISHLIST_STATUS = "0";
        } else {
            RippleittAppInstance.WISHLIST_STATUS = "1";
            mimgVwAddToWishlist.setBackgroundResource(R.drawable.wishlist_red_icon);
        }


        if (mtxtVwProductDetail.getText().toString().length() > 250) {
            mTxtVwShowMore.setVisibility(View.VISIBLE);
        } else {
            mTxtVwShowMore.setVisibility(View.GONE);
        }

        if (RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getIs_paid()
                .equalsIgnoreCase("0")) {

            if (RippleittAppInstance.getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT()
                    .getIs_buying().equalsIgnoreCase("1")) {
                mBtnMakePayment.setVisibility(View.VISIBLE);
                if (RippleittAppInstance
                        .getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT()
                        .getPayment_mode().equalsIgnoreCase("1")) {
                    mBtnMakePayment.setText("Confirm Order");

                } else {
                    mBtnMakePayment.setText("Make Payment");
                }

            } else {
                mBtnMakePayment.setVisibility(View.GONE);
                tvNeedHelp.setVisibility(View.GONE);
            }


        } else if (RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getIs_paid().equalsIgnoreCase("1"))
            if (RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getIs_buying().equalsIgnoreCase("1")) {
                if (RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getDirect_buy().equalsIgnoreCase("1")
                        && RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getPayment_mode().equalsIgnoreCase("1")) {
                    mBtnMakePayment.setVisibility(View.GONE);
                }
            } else {
                if (RippleittAppInstance
                        .getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT()
                        .getDirect_buy().equalsIgnoreCase("1") && RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getPayment_mode().equalsIgnoreCase("1")) {
                    mBtnMakePayment.setVisibility(View.VISIBLE);
                    mBtnMakePayment.setText("Confirm Order");

                } else {

                    if (mBtnMakePayment.getText().toString().equalsIgnoreCase("Request Payment")) {
                        mBtnMakePayment.setVisibility(View.VISIBLE);
                    } else
                        mBtnMakePayment.setVisibility(View.GONE);

                }
            }

        if (RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getPayment_mode().equalsIgnoreCase("1")) {
            tvNeedHelp.setVisibility(View.VISIBLE);
        }

        if (RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getIs_buying().equalsIgnoreCase("1")) {

            tvDiscountedPrice.setText("$" + String.format("%.2f",calculateDiscountedPrice(RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_value()
                    , RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getOrder_value())));

            if (RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getHas_voucher() != null &&
                    RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_details() != null &&
                    RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getHas_voucher().equals("1")) {
                linearDiscountedPrice.setVisibility(View.VISIBLE);
            } else
                linearDiscountedPrice.setVisibility(View.GONE);

        } else {

            tvDiscountTitle.setText("Amount Received: ");
            tvDiscountedPrice.setText("$" + String.format("%.2f",Float.parseFloat(RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getOrder_net())));
//            if (data[i].getHas_voucher() != null && data[i].getVoucher_details() != null && data[i].getHas_voucher().equals("1")) {
//                holder.mTxtVwProductDiscPrice.setVisibility(View.VISIBLE);
//            }else


            relProductPriceDetails.setVisibility(View.VISIBLE);
            relGatewayFee.setVisibility(View.VISIBLE);
            try {
                txtVwServiceCost.setText("$" + String.format("%.2f", Float.parseFloat(
                        RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getRipple_fee()
                )));
                txtVwGatewayFee.setText("$" + String.format("%.2f", Float.parseFloat(
                        RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getGateway_fee()
                )));
                txtVwDiscountedFee.setText("$" + String.format("%.2f", calculateDiscountedPrice(
                        RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_value(),
                        RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getOrder_value()

                )));
//                tvDiscountedPrice.setText("$" + String.valueOf(calculateDiscountedPrice(RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_value()
//                        , RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getOrder_value())));

                if (RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getHas_voucher() != null &&
                        RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_details() != null &&
                        RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getHas_voucher().equals("1")) {
                    relDiscountedFee.setVisibility(View.VISIBLE);
                } else
                    relDiscountedFee.setVisibility(View.GONE);

            } catch (Exception e) {

            }
//            tvDiscountedPrice.setVisibility(View.VISIBLE);


        }

//        if (RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getHas_voucher() != null
//                && RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_details() != null
//                && RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getHas_voucher().equals("1")) {
//
//            linearDiscountedPrice.setVisibility(View.VISIBLE);
//            tvDiscountedPrice.setText("$" + calculateVoucherFee(RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_details().getAmount(),
//                    RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getQuantity(),
//                    RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_details().getType(),
//                    RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getListing_price()));
//
//        } else {
//            linearDiscountedPrice.setVisibility(View.GONE);
//        }

    }

    private double calculateDiscountedPrice(String voucherAmount, String listingPrice) {
//        if (mEdtxtAddQty.getText().toString().trim()==null)
//        double quantity= Float.parseFloat(qty);
        double voucherPrice = Float.parseFloat(voucherAmount);
        double totalPrice = Float.parseFloat(listingPrice);
        double total = 0;

        total = totalPrice - voucherPrice;

//        return total*quantity;
        return total;
    }

    @Override
    public void onClick(View view) {

        if (view == mtxtVwUserNameDetails) {

            if (RippleittAppInstance.getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT()
                    .getIs_buying().equalsIgnoreCase("1")) {
                RippleittAppInstance.getInstance()
                        .getUserTemplate().setUserName(
                        RippleittAppInstance.getInstance()
                                .getSELECTED_LISTING_DETAIL_OBJECT()
                                .getSellerarray().getFullName());

                RippleittAppInstance.getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT()
                        .setUserinformation(RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getBuyerarray());

            } else {
                RippleittAppInstance.getInstance()
                        .getUserTemplate().setPicPath(
                        RippleittAppInstance.getInstance()
                                .getSELECTED_LISTING_DETAIL_OBJECT()
                                .getBuyerarray().getImage());
                RippleittAppInstance.getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT()
                        .setUserinformation(RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getSellerarray());
            }


            startActivity(new Intent(ActivityOrderDetails.this, ProfileActivity.class));
        }


        if (view == tvNeedHelp) {
            RippleittAppInstance.getInstance().setDisputeMode(false);
            startActivity(new Intent(ActivityOrderDetails.this, ActivityAddDispute.class));
        }


        if (view == mBtnMakePayment) {

            if (RippleittAppInstance.getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT()
                    .getIs_paid().equalsIgnoreCase("1")) {

                if (RippleittAppInstance.getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT()
                        .getIs_buying().equalsIgnoreCase("1")) {
                    String orderID = RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getOrder_id();
                    volleyReleasePayment(orderID);
                } else {
                    if (RippleittAppInstance
                            .getInstance()
                            .getSELECTED_LISTING_DETAIL_OBJECT()
                            .getDirect_buy().equalsIgnoreCase("1") && RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getPayment_mode().equalsIgnoreCase("1")) {
                        String orderID = RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getOrder_id();
                        volleyReleasePayment(orderID);
                    } else {
                        String orderID = RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getOrder_id();
                        volleyRequestPayment(orderID);
                    }
                }

            } else {

                if (RippleittAppInstance.getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT()
                        .getIs_buying().equalsIgnoreCase("1")) {
                    if (RippleittAppInstance.getInstance()
                            .getSELECTED_LISTING_DETAIL_OBJECT()
                            .getPayment_mode().equalsIgnoreCase("1")) {
                        // its a cash payment...
                        String orderID = RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getOrder_id();
                        volleyConfirmOrder(orderID);
                    } else if (RippleittAppInstance.getInstance()
                            .getSELECTED_LISTING_DETAIL_OBJECT()
                            .getPayment_mode().equalsIgnoreCase("2")) {
                        // its a card payment...
                        final String orderID = RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getOrder_id();
                        String quantity = RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getQuantity();
                        String bidAmount = RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getBid_price();
                        if (bidAmount != null && !bidAmount.equalsIgnoreCase("")) {

                            Float chargeAmount = Float.parseFloat(quantity) * Float.parseFloat(bidAmount);
                            final String strCharge = Float.toString(chargeAmount);

                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("Are you sure you want to make payment?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            volleyMakePayment(orderID);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                } else {

                }

            }
        }

        if (view == mTxtVwShowMore) {
            if (mtxtVwProductDetail.getMaxLines() == 2) {
                mtxtVwProductDetail.setMaxLines(Integer.MAX_VALUE);
                mTxtVwShowMore.setText("Show less");
            } else {
                mtxtVwProductDetail.setMaxLines(2);
                mTxtVwShowMore.setText("Show More");
            }
        }
        if (view == mBtnSubmitRating) {
            finish();
            startActivity(new Intent(ActivityOrderDetails.this, ActivitySubmitReview.class));
        }
        //==========View_Seller_profile

        //============Refer_A_friend
        if (view == mbtnReferAFriend) {


            if (RippleittAppInstance
                    .getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT()
                    .getIs_buying().equalsIgnoreCase("1")) { // contact seller...
                RippleittAppInstance.getInstance().setCurrentChatPartner(
                        RippleittAppInstance.getInstance()
                                .getSELECTED_LISTING_DETAIL_OBJECT().getSellerarray());
                startActivity(new Intent(ActivityOrderDetails.this, ActivityChatDetails.class));
            } else {
                RippleittAppInstance.getInstance().setCurrentChatPartner(
                        RippleittAppInstance.getInstance()
                                .getSELECTED_LISTING_DETAIL_OBJECT().getBuyerarray());
                startActivity(new Intent(ActivityOrderDetails.this, ActivityChatDetails.class));
            }

            // startActivity(new Intent(ActivityOrderDetails.this,AddressBookActivity.class));
            //startActivity(new Intent(ActivityOrderDetails.this,ReferFriendActivity.class));
        }
        //===========Set_Your Price

        //===========BackButton
        if (view == mrelProductDetailsback) {
            finish();
        }


        if (view == mimgVwAddToWishlist) {
            //==============
            if (RippleittAppInstance.WISHLIST_STATUS.equalsIgnoreCase("0")) {
                RippleittAppInstance.WISHLIST_STATUS = "1";
                new AddToWishListApi().addToWishlistApi(this,
                        RippleittAppInstance.getInstance().getCURRENT_SELECTED_LISTING_ID());
            } else if (RippleittAppInstance.WISHLIST_STATUS.equalsIgnoreCase("1")) {
                RippleittAppInstance.WISHLIST_STATUS = "0";
                new RemoveProductWishlist().remoeProductWishlistApi(this, "0",
                        RippleittAppInstance.getInstance().getCURRENT_SELECTED_LISTING_ID(), this);
            }
            //=============
        }
    }

    @Override
    public void onWishListitemSelected(String listing_id) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REFRESH_DATA) {
            // this is multi contact selection callback...
            if (resultCode == Activity.RESULT_OK) {
                volleyFetchProductDetails(RippleittAppInstance.getInstance().getCURRENT_SELECTED_LISTING_ID());
            } else {
                // Toast.makeText(ActivityHome.this,"No contacts were imported",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void volleyMakePayment(final String orderID) {
        pDliaog.setMessage("Making payment...");
        pDliaog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,

                RippleittAppInstance.MAKE_ORDER_PAYMENT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDliaog.dismiss();

                        Gson g = new Gson();
                        ProductDetailsResponseTemplate productDetails =
                                (ProductDetailsResponseTemplate) g.fromJson(response, ProductDetailsResponseTemplate.class);

                        if (productDetails.getResponse_code() == 1) {
                            finish();
//                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrderDetails.this);
//                            builder.setMessage("Congratulations")
//                                    .setCancelable(false)
//                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            //do things
//                                            finish();
//                                        }
//                                    });
//                            AlertDialog alert = builder.create();
//                            alert.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrderDetails.this);
                            builder.setMessage("Could not complete your request. Please try again")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things

                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDliaog.dismiss();
                Toast.makeText(ActivityOrderDetails.this,
                        "could not update listing information",
                        Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityOrderDetails.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("order_id", orderID);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }

    private void volleyReleasePayment(final String orderID) {
        pDliaog.setMessage("Releasing payment...");
        pDliaog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,

                RippleittAppInstance.RELEASE_ORDER_PAYMENT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDliaog.dismiss();

                        Gson g = new Gson();
                        ProductDetailsResponseTemplate productDetails =
                                (ProductDetailsResponseTemplate) g.fromJson(response, ProductDetailsResponseTemplate.class);

                        if (productDetails.getResponse_code() == 1) {
                            if (RippleittAppInstance.getInstance()
                                    .getSELECTED_LISTING_DETAIL_OBJECT()
                                    .getIs_buying().equalsIgnoreCase("1")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrderDetails.this);
                                builder.setMessage("Payment has been released successfully.")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrderDetails.this);
                                builder.setMessage("Order has been confirmed")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrderDetails.this);
                            builder.setMessage("Could not complete your request. Please try again")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things

                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDliaog.dismiss();
                Toast.makeText(ActivityOrderDetails.this,
                        "Could not release payment",
                        Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityOrderDetails.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("order_id", orderID);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }

    private void volleyRequestPayment(final String orderID) {
        pDliaog.setMessage("Submitting request...");
        pDliaog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,

                RippleittAppInstance.REQUEST_ORDER_PAYMENT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDliaog.dismiss();

                        Gson g = new Gson();
                        ProductDetailsResponseTemplate productDetails =
                                (ProductDetailsResponseTemplate) g.fromJson(response, ProductDetailsResponseTemplate.class);

                        if (productDetails.getResponse_code() == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrderDetails.this);
                            builder.setMessage(productDetails.getResponse_message())
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrderDetails.this);
                            builder.setMessage("Could not complete your request. Please try again")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things

                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDliaog.dismiss();
                Toast.makeText(ActivityOrderDetails.this,
                        "Could not request payment",
                        Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityOrderDetails.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("order_id", orderID);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }

    private void volleyConfirmOrder(final String orderID) {
        pDliaog.setMessage("Confirming order...");
        pDliaog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.MAKE_ORDER_PAYMENT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDliaog.dismiss();

                        Gson g = new Gson();
                        ProductDetailsResponseTemplate productDetails =
                                (ProductDetailsResponseTemplate) g.fromJson(response, ProductDetailsResponseTemplate.class);

                        if (productDetails.getResponse_code() == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrderDetails.this);
                            builder.setMessage("Order has been confirmed")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrderDetails.this);
                            builder.setMessage("Could not complete your request. Please try again")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things

                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDliaog.dismiss();
                Toast.makeText(ActivityOrderDetails.this,
                        "could not update listing information",
                        Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityOrderDetails.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("order_id", orderID);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }

    private void volleyFetchProductDetails(final String listing_id) {
        pDliaog.setMessage("Fetching details...");
        pDliaog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCHING_PRODUCT_DETAILS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDliaog.dismiss();

                        Gson g = new Gson();
                        ProductDetailsResponseTemplate productDetails =
                                (ProductDetailsResponseTemplate) g.fromJson(response, ProductDetailsResponseTemplate.class);
                        RippleittAppInstance.getInstance().setSELECTED_LISTING_DETAIL_OBJECT(productDetails.getData());
                        RippleittAppInstance.getInstance()
                                .setCURRENT_SELECTED_LISTING_ID(RippleittAppInstance
                                        .getInstance()
                                        .getSELECTED_LISTING_DETAIL_OBJECT().getListing_id());

                        mScrlViewParent.scrollTo(0, 0);
                        work();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDliaog.dismiss();
                Toast.makeText(ActivityOrderDetails.this,
                        "could not update listing information",
                        Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityOrderDetails.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("listing_id", listing_id);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "See less", false);
                    } else {
                        makeTextViewResizable(tv, 3, "See more", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    private void populateRatingStars() {
        String rating = RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getSellerarray()
                .getRating();
        mImgVwRatingStarOne.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarTwo.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        if (rating.equals("0")) {
            mImgVwRatingStarOne.setVisibility(View.GONE);
            mImgVwRatingStarTwo.setVisibility(View.GONE);
            mImgVwRatingStarThree.setVisibility(View.GONE);
            mImgVwRatingStarFour.setVisibility(View.GONE);
            mImgVwRatingStarFive.setVisibility(View.GONE);
            mTxtVwNoRating.setVisibility(View.VISIBLE);
        }
        if (rating.equals("1")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equals("1.user")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.half_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equals("2")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equals("2.5")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.half_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equals("3")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equals("3.5")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.half_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equals("4")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equals("4.5")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.half_rating);
        }
        if (rating.equals("5")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.full_rating);
        }


    }

    private float calculate() {
        float quantity = Float.parseFloat(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getQuantity());
        float price = Float.parseFloat(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getBid_price());
        float total = quantity * price;
//        String str= String.valueOf(total+10.0f);
        return total;
    }

    private float calculateServiceFee() {
        float quantity = Float.parseFloat(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getQuantity());
        float price = Float.parseFloat(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getBid_price());
        float total = quantity * price;
        float percent = (total * 12.5f) / 100;
        return percent;
    }


    private double calculateVoucherFee(String voucherAmount, String qty, String voucherType, String listingPrice) {
//        if (mEdtxtAddQty.getText().toString().trim()==null)
//        double quantity= Float.parseFloat(qty);
        double voucherPrice = Float.parseFloat(voucherAmount);
        double totalPrice = Float.parseFloat(listingPrice);
        double quantity = Float.parseFloat(qty);
        double total = 0;

        if (voucherType.equals("1")) {
            total = (totalPrice * quantity) - voucherPrice;
        } else if (voucherType.equals("2")) {
            double percent = (totalPrice * voucherPrice) / 100;
            total = (totalPrice - percent) * quantity;
        }
//        return total*quantity;
        return total;
    }

}
