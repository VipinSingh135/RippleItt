package com.rippleitt.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.adapters.BuyerBidsAdapter;
import com.rippleitt.adapters.CustomViewPagerAdapter;
import com.rippleitt.adapters.MyListingPagerAdapter;
import com.rippleitt.callback.AcceptBidCallback;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.BidTemplate;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.modals.MyListingDetailsPayload;
import com.rippleitt.utils.CommonUtils;
import com.rippleitt.webservices.AddToWishListApi;
import com.rippleitt.webservices.RemoveProductWishlist;
import com.robohorse.pagerbullet.PagerBullet;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityMyProductDetails extends AppCompatActivity
        implements View.OnClickListener, AcceptBidCallback {


    RelativeLayout mrelProductDetailsback;
    ImageView mimgVwProductImage;

    TextView mtxtVwProductName;
    TextView mtxtVwProductDetail;
    TextView mtxtVwUserNameDetails;
    //TextView mtxtVwAddrDetails;
    TextView mtxtVwProductPrice;
    TextView mTxtVwReferralamount;
    PagerBullet vwPagerProductImages;
    CircleImageView mimgVwprofileImageDetails;
    MyListingPagerAdapter customViewPagerAdapter;
    TextView mTxtVwBidCount;
    BuyerBidsAdapter buyerBidsAdapter;
    ImageView mImgVwBackFinish;
    LinearLayout bidsContainer;
    TextView mtxtVwAvailableQty;
    ProgressDialog mPDialog;
    private Button mBtnFaq;
    private int bidQuantity = 0;
    private RelativeLayout mRelLytCommentBox;
    private RelativeLayout relProductPriceDetails;
    LinearLayout linProductQtyDetails, linProductShippingDetails;
    private TextView mTxtVWAvailableQty, mTxtVwPaymentMode;
    private ImageView mImgVwRatingStarOne, mImgVwRatingStarTwo,
            mImgVwRatingStarThree, mImgVwRatingStarFour, mImgVwRatingStarFive;
    private RelativeLayout mRelLtyServiceAreContainer, mRelLtyServiceAreaIcon;
    private TextView mtxtVwServiceZip, mTxtVwShippingMode;
    FrameLayout mFrmLytBadge;
    TextView mTxtvwBadgeStatus,txtVwAddress, txtvwProductDetails;
    RelativeLayout relOtherBuyers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_prduct_detail);
        mPDialog = new ProgressDialog(this);
        mPDialog.setCancelable(false);
        init();
        work();
    }

    public void init() {
        mRelLtyServiceAreContainer = (RelativeLayout) findViewById(R.id.relServiceAreas);
        mRelLtyServiceAreaIcon = (RelativeLayout) findViewById(R.id.relServiceAreaIcon);
        mTxtVwShippingMode = (TextView) findViewById(R.id.txtvwDeliveryMode);
        mtxtVwServiceZip = (TextView) findViewById(R.id.txtvwServiceCodes);
        txtVwAddress = (TextView) findViewById(R.id.txtVwAddress);
        txtvwProductDetails = (TextView) findViewById(R.id.txtvwProductDetails);
        mRelLytCommentBox = (RelativeLayout) findViewById(R.id.relCommentsBox);
        mRelLytCommentBox.setOnClickListener(this);
        mRelLtyServiceAreContainer.setOnClickListener(this);
        mFrmLytBadge = (FrameLayout) findViewById(R.id.frmlytOrderFlagColor);
        mTxtvwBadgeStatus = (TextView) findViewById(R.id.txtvwLabelOrderStatus);
        mImgVwRatingStarOne = (ImageView) findViewById(R.id.imgvwRatingStarOne);
        mImgVwRatingStarTwo = (ImageView) findViewById(R.id.imgvwRatingStarTwo);
        mImgVwRatingStarThree = (ImageView) findViewById(R.id.imgvwRatingStarThree);
        mImgVwRatingStarFour = (ImageView) findViewById(R.id.imgvwRatingStarFour);
        mImgVwRatingStarFive = (ImageView) findViewById(R.id.imgvwRatingStarFive);
        mTxtVwPaymentMode = (TextView) findViewById(R.id.txtvwPaymentMode);
        relOtherBuyers = findViewById(R.id.relOtherBuyers);

        relProductPriceDetails = findViewById(R.id.relProductPriceDetails);
        linProductShippingDetails = findViewById(R.id.linProductShippingDetails);
        linProductQtyDetails = findViewById(R.id.linProductQtyDetails);

        // new GetProductDetailsApi().getProductDetailsApi(this, RippleittAppInstance.PRODUCT_ID);
        customViewPagerAdapter = new MyListingPagerAdapter(this);
        mtxtVwAvailableQty = (TextView) findViewById(R.id.txtvwAvailableQty);
        mImgVwBackFinish = (ImageView) findViewById(R.id.imgBackProductDetail);
        mImgVwBackFinish.setOnClickListener(this);
        vwPagerProductImages = (PagerBullet) findViewById(R.id.vwPagerProductImages);
        vwPagerProductImages.setIndicatorTintColorScheme(Color.WHITE, Color.parseColor("#289b98"));
        vwPagerProductImages.setAdapter(customViewPagerAdapter);
        mTxtVwBidCount = (TextView) findViewById(R.id.txtVwReviewProfile);
        mrelProductDetailsback = (RelativeLayout) findViewById(R.id.relProductDetailsback);
        mtxtVwProductName = (TextView) findViewById(R.id.txtVwProductName);
        mTxtVwReferralamount = (TextView) findViewById(R.id.txtVwProductReferAmount);
        mtxtVwProductDetail = (TextView) findViewById(R.id.txtVwProductDetail);
        mtxtVwProductPrice = (TextView) findViewById(R.id.txtVwProductPrice);
        mtxtVwUserNameDetails = (TextView) findViewById(R.id.txtVwUserNameDetails);
        mimgVwprofileImageDetails = (CircleImageView) findViewById(R.id.imgVwprofileImageDetails);
        mBtnFaq = (Button) findViewById(R.id.btnFAQ);
        //mBtnFaq.setOnClickListener(this);
        mBtnFaq.setVisibility(View.GONE);
        bidsContainer = (LinearLayout) findViewById(R.id.linlytBidsContainer);
        mrelProductDetailsback.setOnClickListener(this);
        configViewPagerClickListener();

        if (RippleittAppInstance
                .getInstance()
                .getMY_CURRENT_LISTING().getListing_type().equals("2")) {
            txtvwProductDetails.setText("Details");
        }
    }


    private void loadBids() {
        bidsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(ActivityMyProductDetails.this);
        for (final BidTemplate bid : RippleittAppInstance
                .getInstance().getMY_CURRENT_LISTING().getBids()) {
            View view = LayoutInflater.from(this).inflate(R.layout.buyer_bidview, null);
            TextView mtxtVwBidBuyerName = (TextView) view.findViewById(R.id.txtVwBidBuyerName);
            TextView mtxtBuyerBidPrice = (TextView) view.findViewById(R.id.txtvwBidAmount);
            ImageView mImgVwAcceptTick = (ImageView) view.findViewById(R.id.imgvwTickAccepted);
            CircleImageView mimgVwBidBuyerPic = (CircleImageView) view.findViewById(R.id.imgVwBidBuyerPic);
            TextView mTxtVwAcceptBid = (TextView) view.findViewById(R.id.txtvwAcceptBid);
            TextView txtvwMinutes = (TextView) view.findViewById(R.id.txtvwMinutes);
            TextView txtvwExpired = (TextView) view.findViewById(R.id.txtvwExpired);

            txtvwExpired.setVisibility(View.GONE);
            if (bid.getIs_accepted().equalsIgnoreCase("1")) {
                mImgVwAcceptTick.setVisibility(View.VISIBLE);
                mTxtVwAcceptBid.setVisibility(View.GONE);
                txtvwExpired.setVisibility(View.GONE);
            } else {
                txtvwExpired.setVisibility(View.GONE);
                mImgVwAcceptTick.setVisibility(View.GONE);
            }
            if (bid.getHideAccept().equalsIgnoreCase("1")) {
                mTxtVwAcceptBid.setVisibility(View.GONE);
                txtvwExpired.setVisibility(View.GONE);
            }
            if (bid.getIs_accepted().equalsIgnoreCase("1")) {
                mTxtVwAcceptBid.setVisibility(View.GONE);
                mImgVwAcceptTick.setVisibility(View.VISIBLE);
                txtvwExpired.setVisibility(View.GONE);
            }
            if (Integer.parseInt(bid.getMinutes()) <= 1440) {

                txtvwMinutes.setTextColor(Color.parseColor("#FFAFAFAF"));
                txtvwMinutes.setText(com.rippleitt.commonUtilities.CommonUtils.minutesToHours(Integer.parseInt(bid.getMinutes())));
                txtvwExpired.setVisibility(View.GONE);
            } else {
                txtvwMinutes.setVisibility(View.GONE);
                txtvwExpired.setTextColor(Color.parseColor("#b74242"));
                txtvwExpired.setText("Expired");
                txtvwExpired.setVisibility(View.VISIBLE);

                mTxtVwAcceptBid.setVisibility(View.GONE);
                mImgVwAcceptTick.setVisibility(View.GONE);
            }

            if (RippleittAppInstance.getInstance().getMY_CURRENT_LISTING().getIs_accepted().equalsIgnoreCase("1")) {
                //mTxtVwAcceptBid.setVisibility(View.GONE);
            }
            //==========setDetails================
            mtxtVwBidBuyerName.setText(
                    bid.getUserinformation().getFullName());
            mtxtBuyerBidPrice.setText("$" + bid.getBidprice() + " ("
                    + bid.getQuantity()
                    + " Qty" + ")");
            mTxtVwAcceptBid.setPaintFlags(mTxtVwAcceptBid.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            mTxtVwAcceptBid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptBid(bid.getBidid(), Integer.parseInt(bid.getQuantity()));
                }
            });
            try {
                Picasso.with(ActivityMyProductDetails.this)
                        .load(RippleittAppInstance.formatPicPath(
                                bid.getUserinformation().getImage()))
                        .placeholder(R.drawable.default_profile_icon)
                        .error(R.drawable.default_profile_icon)
                        .into(mimgVwBidBuyerPic);
            } catch (Exception e) {
                mimgVwBidBuyerPic.setImageResource(R.drawable.default_profile_icon);
            }

            // now we polulate the rating of the users....

            /*
            ImageView mImgVwRatingStarOne=(ImageView)view.findViewById(R.id.imgveStarOne);
            ImageView mImgVwRatingStarTwo=(ImageView)view.findViewById(R.id.imgveStarTwo);
            ImageView mImgVwRatingStarThree=(ImageView)view.findViewById(R.id.imgveStarThree);
            ImageView mImgVwRatingStarFour=(ImageView)view.findViewById(R.id.imgveStarFour);
            ImageView mImgVwRatingStarFive=(ImageView)view.findViewById(R.id.imgveStarFive);
            String rating = bid.getUserinformation().getRating();
            mImgVwRatingStarOne.setVisibility(View.INVISIBLE);
            mImgVwRatingStarTwo.setVisibility(View.INVISIBLE);
            mImgVwRatingStarThree.setVisibility(View.INVISIBLE);
            mImgVwRatingStarFour.setVisibility(View.INVISIBLE);
            mImgVwRatingStarFive.setVisibility(View.INVISIBLE);
            */

            /*
            if(rating.equalsIgnoreCase("0")){
                mImgVwRatingStarOne.setImageResource(R.drawable.blank_rating);
                mImgVwRatingStarTwo.setImageResource(R.drawable.blank_rating);
                mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
                mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
                mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
            }
            if(rating.equalsIgnoreCase("1")){
                mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
                mImgVwRatingStarTwo.setImageResource(R.drawable.blank_rating);
                mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
                mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
                mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
            } if(rating.equalsIgnoreCase("2")){
                mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
                mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
                mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
                mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
                mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
            } if(rating.equalsIgnoreCase("3")){
                mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
                mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
                mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
                mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
                mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
            } if(rating.equalsIgnoreCase("4")){
                mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
                mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
                mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
                mImgVwRatingStarFour.setImageResource(R.drawable.full_rating);
                mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
            } if(rating.equalsIgnoreCase("5")){
                mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
                mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
                mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
                mImgVwRatingStarFour.setImageResource(R.drawable.full_rating);
                mImgVwRatingStarFive.setImageResource(R.drawable.full_rating);
            }

            */


            bidsContainer.addView(view);
        }
    }

    private void configViewPagerClickListener() {

    }

    public void work() {


        if (RippleittAppInstance
                .getInstance()
                .getMY_CURRENT_LISTING()
                .getShipping_type().equalsIgnoreCase("1")) { // pickup
            mTxtVwShippingMode.setText("Pick Up");
            mRelLtyServiceAreaIcon.setVisibility(View.GONE);
            mRelLtyServiceAreContainer.setVisibility(View.GONE);
            mtxtVwServiceZip.setVisibility(View.GONE);
        } else {
            mTxtVwShippingMode.setText("Shipping");
            mRelLtyServiceAreaIcon.setVisibility(View.GONE);
            mRelLtyServiceAreContainer.setVisibility(View.GONE);
            mtxtVwServiceZip.setVisibility(View.GONE);
            // populateZip();
        }

        if (RippleittAppInstance
                .getInstance()
                .getMY_CURRENT_LISTING().getPayment_mode().equalsIgnoreCase("2")) {
            mTxtVwPaymentMode.setText("Credit Card");
        } else {
            mTxtVwPaymentMode.setText("Cash");
        }

        if (RippleittAppInstance
                .getInstance()
                .getMY_CURRENT_LISTING().getIs_new().equals("1")) {
            mFrmLytBadge.setBackgroundColor(Color.parseColor("#2f4fb9"));
            mTxtvwBadgeStatus.setText(" New ");
        } else {
            mFrmLytBadge.setBackgroundColor(Color.parseColor("#d2742d"));
            mTxtvwBadgeStatus.setText(" Used ");
        }
        if (RippleittAppInstance
                .getInstance()
                .getMY_CURRENT_LISTING().getListing_type().equalsIgnoreCase("1")) {
            mFrmLytBadge.setVisibility(View.VISIBLE);
        } else {
            mFrmLytBadge.setVisibility(View.GONE);
        }

//
        if (RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT() == null) {
            RippleittAppInstance
                    .getInstance()
                    .setSELECTED_LISTING_DETAIL_OBJECT(new ListingTemplate());
        }
        RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .setListing_id(RippleittAppInstance.getInstance().getMY_CURRENT_LISTING().getListing_id());

        RippleittAppInstance.getInstance().setFullScreenImageMode(2);
        //===========setProductImage================
        Glide.with(this)
                .load(RippleittAppInstance.formatPicPath(RippleittAppInstance
                        .getInstance().getMY_CURRENT_LISTING()
                        .getUserinformation().getImage()))
                .placeholder(R.drawable.default_profile_icon)
                .error(R.drawable.default_profile_icon)
                .into(mimgVwprofileImageDetails);

        ///===============setProductName and description========
        mtxtVwAvailableQty.setText(RippleittAppInstance
                .getInstance()
                .getMY_CURRENT_LISTING()
                .getQuantity()
        );
        mtxtVwProductName.setText(RippleittAppInstance.getInstance().getMY_CURRENT_LISTING().getListing_name());
        txtVwAddress.setText(RippleittAppInstance.getInstance().getMY_CURRENT_LISTING().getLocation().getAddress());
        mtxtVwProductDetail.setText(RippleittAppInstance.getInstance().getMY_CURRENT_LISTING().getListing_description());
        mtxtVwUserNameDetails.setText(RippleittAppInstance.getInstance()
                .getMY_CURRENT_LISTING().getUserinformation().getFname() + " "
                + RippleittAppInstance.getInstance()
                .getMY_CURRENT_LISTING().getUserinformation().getLname());
        mTxtVwReferralamount.setText("$" + RippleittAppInstance.getInstance().getMY_CURRENT_LISTING().getRefer_amount());
        mtxtVwProductPrice.setText("$" + RippleittAppInstance.getInstance().getMY_CURRENT_LISTING().getListing_price());
        mTxtVwBidCount.setText("Buyer Offers (" + Integer.toString(RippleittAppInstance
                .getInstance().getMY_CURRENT_LISTING().getBids().length) + ")");
        if (mtxtVwProductDetail.getText().toString().length() > 150) {
            makeTextViewResizable(mtxtVwProductDetail, 3, "Show more", true);
        }

        mtxtVwUserNameDetails.setPaintFlags(mtxtVwUserNameDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mtxtVwUserNameDetails.setOnClickListener(this);

        try {
            populateRatingStars();
        } catch (Exception e) {

        }

        String str = RippleittAppInstance
                .getInstance()
                .getMY_CURRENT_LISTING().getListing_type();

        if (RippleittAppInstance
                .getInstance()
                .getMY_CURRENT_LISTING().getListing_type().equals("2")) {
//            relProductPriceDetails.setVisibility(View.GONE);
            linProductQtyDetails.setVisibility(View.GONE);
            linProductShippingDetails.setVisibility(View.GONE);
//            relOtherBuyers.setVisibility(View.GONE);
            loadBids();
        } else {
            relOtherBuyers.setVisibility(View.VISIBLE);
            relProductPriceDetails.setVisibility(View.VISIBLE);
            linProductQtyDetails.setVisibility(View.VISIBLE);
            linProductShippingDetails.setVisibility(View.VISIBLE);
            loadBids();
        }
    }


    private void populateZip() {
        String[] zipArray = RippleittAppInstance
                .getInstance()
                .getMY_CURRENT_LISTING()
                .getPostal_codes();
        String parsedZip = "";
        for (String currentZip : zipArray) {
            parsedZip = parsedZip + " " + currentZip + ",";
        }
        parsedZip = parsedZip.replaceAll(",$", "");
        mtxtVwServiceZip.setText(parsedZip);
        mtxtVwServiceZip.setVisibility(View.GONE);

    }


    private void populateRatingStars() {
        String rating = RippleittAppInstance.getInstance()
                .getMY_CURRENT_LISTING()
                .getUserinformation()
                .getRating();
        mImgVwRatingStarOne.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarTwo.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        if (rating.equalsIgnoreCase("1")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equalsIgnoreCase("1.5")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.half_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equalsIgnoreCase("2")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equalsIgnoreCase("2.5")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.half_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equalsIgnoreCase("3")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equalsIgnoreCase("3.5")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.half_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equalsIgnoreCase("4")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equalsIgnoreCase("4.5")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.half_rating);
        }
        if (rating.equalsIgnoreCase("5")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.full_rating);
        }


    }


    @Override
    public void onClick(View view) {


        if (mRelLytCommentBox == view) {
            RippleittAppInstance.getInstance().setListingFaqMode(1);


            startActivity(new Intent(ActivityMyProductDetails.this, ActivityListingFaq.class));

        }

        if (view == mtxtVwUserNameDetails) {

            RippleittAppInstance.getInstance()
                    .getUserTemplate().setUserName(
                    RippleittAppInstance.getInstance()
                            .getMY_CURRENT_LISTING()
                            .getUserinformation().getFullName());

            RippleittAppInstance.getInstance()
                    .getUserTemplate().setPicPath(
                    RippleittAppInstance.getInstance()
                            .getMY_CURRENT_LISTING()
                            .getUserinformation().getImage());
            startActivity(new Intent(ActivityMyProductDetails.this, ProfileActivity.class));
        }

        //==============share_productdetAils
        if (view == mImgVwBackFinish) {
            finish();
        }
        if (view == mBtnFaq) {
            RippleittAppInstance.getInstance().setListingFaqMode(1);// user is viewing his own listing faq
            RippleittAppInstance.getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT()
                    .setListing_id(RippleittAppInstance.getInstance().getMY_CURRENT_LISTING().getListing_id());
            startActivity(new Intent(ActivityMyProductDetails.this, ActivityListingFaq.class));
        }
        //=============
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void acceptBid(String bidID, int qty) {
        if ((Integer.parseInt(RippleittAppInstance
                .getInstance()
                .getMY_CURRENT_LISTING()
                .getQuantity()) - qty) < 0) {
            promptInsufficientStock();
        } else {
            confirmAcceptance(bidID, qty);
        }

    }


    private void confirmAcceptance(final String bidID, final int bidQuantity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want accept this offer?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        volleyAcceptBid(bidID, bidQuantity);
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

    private void promptInsufficientStock() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Not enough stock to fulfil this offer!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void volleyAcceptBid(final String bid_id, final int bid_quantity) {
        mPDialog.setMessage("Submitting your request...");
        mPDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, RippleittAppInstance
                .getInstance().ACCEPT_BID,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mPDialog.dismiss();
                        Log.d("", response.toString());
                        Gson gson = new Gson();
                        MyListingDetailsPayload payload =
                                (MyListingDetailsPayload) gson.fromJson(response, MyListingDetailsPayload.class);
                        if (payload.getResponse_code().equalsIgnoreCase("1")) {
                            showConfirmation(bid_id, bid_quantity);
                        } else {
                            Toast.makeText(ActivityMyProductDetails.this,
                                    "Could not complete your request.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPDialog.dismiss();
                Toast.makeText(ActivityMyProductDetails.this,
                        "Could not complete your request, please try again", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(
                        ActivityMyProductDetails.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("bid_id", bid_id);
                params.put("listing_id", RippleittAppInstance.getInstance().getMY_CURRENT_LISTING().getListing_id());

                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "fetch_regions");
    }


    private void showConfirmation(String bidID, int qty) {
        Toast.makeText(ActivityMyProductDetails.this,
                "Offer has been accepted successfully.", Toast.LENGTH_SHORT).show();

        // now we update the bids list...
        for (BidTemplate bid : RippleittAppInstance
                .getInstance()
                .getMY_CURRENT_LISTING().getBids()) {
            if (bid.getBidid().equalsIgnoreCase(bidID)) {
                bid.setIs_accepted("1");
                bid.setHideAccept("1");
            }
            //
        }

        RippleittAppInstance
                .getInstance()
                .getMY_CURRENT_LISTING()
                .setQuantity(Integer.toString(
                        Integer
                                .parseInt(RippleittAppInstance
                                        .getInstance()
                                        .getMY_CURRENT_LISTING()
                                        .getQuantity()) - qty
                ));
        // update the quantity text view...
        mtxtVwAvailableQty.setText(RippleittAppInstance
                .getInstance()
                .getMY_CURRENT_LISTING()
                .getQuantity()
        );
        loadBids();
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


}
