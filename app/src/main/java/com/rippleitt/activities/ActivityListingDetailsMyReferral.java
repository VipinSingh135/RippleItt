package com.rippleitt.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.modals.ProductDetailsResponseTemplate;
import com.rippleitt.modals.ReferralEntity;
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

public class ActivityListingDetailsMyReferral   extends AppCompatActivity implements View.OnClickListener, WishlistSelectCallback {

    //RelativeLayout mrelShareProfileDetails;
    // RelativeLayout mrelViewProfileDetails;
    RelativeLayout mrelProductDetailsback;
    ImageView mimgVwProductImage;
    ImageView mimgVwAddToWishlist;
    TextView mtxtVwProductName;
    TextView mtxtVwProductDetail;
    TextView mtxtVwUserNameDetails;
    TextView mtxtVwAddrDetails;
    TextView mtxtVwProductPrice;
    TextView mTxtVwReferAmount;
    TextView mTxtVwMyBid;
    Button mbtnReferAFriend;

    PagerBullet vwPagerProductImages;
    CircleImageView mimgVwprofileImageDetails;
    CustomViewPagerAdapter customViewPagerAdapter;
    private final int REFRESH_DATA=78;
    ProgressDialog pDliaog;

    private ListingBidsAdapter bidsAdapter;
    private RelativeLayout  mRelLytOtherSellerWrapper;
    private LinearLayout mLinLytBidsContainer, mLinLytOtherSellersContainer;
    private RelativeLayout mRelLytDownArrowBids, mRelLytDownArrowSellers;
    private TextView  mTxtVwSellerCount;
    private LinearLayout mLinLytRootView;
    private TextView mTxtVwShowMore,tvShare;
    private TextView mTxtVWAvailableQty, mTxtVwPaymentMode, mTxtVwDeliveryType;
    private ImageView mImgVwRatingStarOne,mImgVwRatingStarTwo,
            mImgVwRatingStarThree,mImgVwRatingStarFour,mImgVwRatingStarFive;
    private RelativeLayout mRelLtyServiceAreContainer;
    private RelativeLayout mRelLtyServiceAreaIcon;
    private TextView mTxtVwShippingMode,txtVwAddress;
    private TextView mtxtVwServiceZip,mTxtVwNoRating;
    private RelativeLayout mRelLytFaqBox;
    private RelativeLayout relProductPriceDetails;
    LinearLayout linProductQtyDetails,linProductShippingDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_detail_my_referal);
        init();
        work();
    }

    public void init(){
        mTxtVwNoRating=(TextView)findViewById(R.id.txtvwNoRating);
        mTxtVwNoRating.setVisibility(View.GONE);
        mTxtVwNoRating.setPaintFlags(mTxtVwNoRating.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mTxtVwNoRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtxtVwUserNameDetails.performClick();
            }
        });
        mRelLytFaqBox=(RelativeLayout)findViewById(R.id.relCommentsBox);
        mRelLytFaqBox.setOnClickListener(this);
        mRelLtyServiceAreContainer=(RelativeLayout)findViewById(R.id.relServiceAreas);
        mRelLtyServiceAreaIcon=(RelativeLayout)findViewById(R.id.relServiceAreaIcon);
        mTxtVwShippingMode=(TextView)findViewById(R.id.txtvwDeliveryMode);
        mtxtVwServiceZip=(TextView)findViewById(R.id.txtvwServiceCodes);
        txtVwAddress=(TextView)findViewById(R.id.txtVwAddress);
        mTxtVwPaymentMode=(TextView)findViewById(R.id.txtvwPaymentMode);
        mRelLtyServiceAreContainer.setOnClickListener(this);

        relProductPriceDetails =  findViewById(R.id.relProductPriceDetails);
        linProductShippingDetails =  findViewById(R.id.linProductShippingDetails);
        linProductQtyDetails =  findViewById(R.id.linProductQtyDetails);
        tvShare = (TextView) findViewById(R.id.tvShare);

        pDliaog=new ProgressDialog(this);
        pDliaog.setCancelable(false);
//        pDliaog.setMessage("Updating details...");
        RippleittAppInstance.getInstance().setFullScreenImageMode(1);
        mTxtVWAvailableQty=(TextView)findViewById(R.id.txtvwAvailableQty);
        mTxtVwPaymentMode=(TextView)findViewById(R.id.txtvwPaymentMode);//txtvwDeliveryMode
        mTxtVwDeliveryType=(TextView)findViewById(R.id.txtvwDeliveryMode);//
        mTxtVwShowMore=(TextView)findViewById(R.id.txtvwShowMore);
        mTxtVwShowMore.setPaintFlags(mTxtVwShowMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        mTxtVwShowMore.setOnClickListener(this);
        // new GetProductDetailsApi().getProductDetailsApi(this, RippleittAppInstance.PRODUCT_ID);
        customViewPagerAdapter = new CustomViewPagerAdapter(this);
        vwPagerProductImages = (PagerBullet) findViewById(R.id.vwPagerProductImages);
        vwPagerProductImages.setIndicatorTintColorScheme(Color.WHITE, Color.parseColor("#289b98"));
        vwPagerProductImages.setAdapter(customViewPagerAdapter);
        mLinLytBidsContainer=(LinearLayout)findViewById(R.id.linLytListingBids);
        mLinLytOtherSellersContainer=(LinearLayout)findViewById(R.id.linlytOtherSellers);
               mImgVwRatingStarOne=(ImageView)findViewById(R.id.imgvwRatingStarOne);
        mImgVwRatingStarTwo=(ImageView)findViewById(R.id.imgvwRatingStarTwo);
        mImgVwRatingStarThree=(ImageView)findViewById(R.id.imgvwRatingStarThree);
        mImgVwRatingStarFour=(ImageView)findViewById(R.id.imgvwRatingStarFour);
        mImgVwRatingStarFive=(ImageView)findViewById(R.id.imgvwRatingStarFive);

        mRelLytDownArrowBids=(RelativeLayout)findViewById(R.id.relOtherBuyersIcon);
        mRelLytDownArrowSellers=(RelativeLayout)findViewById(R.id.relOtherSellersIcon);
        mRelLytOtherSellerWrapper=(RelativeLayout)findViewById(R.id.relOtherSellers);

        //   mrelViewProfileDetails=(RelativeLayout)findViewById(R.id.relViewProfileDetails);
        mrelProductDetailsback=(RelativeLayout)findViewById(R.id.relProductDetailsback);

        mTxtVwSellerCount=(TextView)findViewById(R.id.txtvwSellerCount);

        mTxtVwSellerCount.setOnClickListener(this);
        mtxtVwProductName=(TextView)findViewById(R.id.txtVwProductName);
        mtxtVwAddrDetails=(TextView)findViewById(R.id.txtVwAddrDetails);
        mtxtVwProductDetail=(TextView)findViewById(R.id.txtVwProductDetail);
        mtxtVwProductPrice=(TextView)findViewById(R.id.txtVwProductPrice);
        mTxtVwReferAmount=(TextView)findViewById(R.id.txtVwProductReferAmount);
        mtxtVwUserNameDetails=(TextView)findViewById(R.id.txtVwUserNameDetails);
        mimgVwAddToWishlist=(ImageView)findViewById(R.id.imgVwAddToWishlist);
        mbtnReferAFriend=(Button)findViewById(R.id.btnReferAFriend);
      mimgVwprofileImageDetails=(CircleImageView) findViewById(R.id.imgVwprofileImageDetails);
        mtxtVwUserNameDetails.setPaintFlags(mtxtVwUserNameDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mtxtVwUserNameDetails.setOnClickListener(this);

        // mrelViewProfileDetails.setOnClickListener(this);
        mbtnReferAFriend.setOnClickListener(this);

        mrelProductDetailsback.setOnClickListener(this);

        mimgVwAddToWishlist.setOnClickListener(this);
        tvShare.setOnClickListener(this);

        mRelLytOtherSellerWrapper.setOnClickListener(this);
        configViewPagerClickListener();
    }


    private void configViewPagerClickListener(){

    }

    public void work(){

        if(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getPayment_mode().equalsIgnoreCase("2")){
            mTxtVwPaymentMode.setText("Credit Card");
        }else{
            mTxtVwPaymentMode.setText("Cash");
        }

        mTxtVWAvailableQty.setText(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getQuantity()+" ");

        if(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getIs_self_listing().equalsIgnoreCase("1")){
            mRelLytFaqBox.setVisibility(View.GONE);
            View divider = (View)findViewById(R.id.vwDividerComment);
            divider.setVisibility(View.GONE);

        }


        if(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getShipping_type().equalsIgnoreCase("1")){ // pickup
            mTxtVwShippingMode.setText("Pick Up");
            mRelLtyServiceAreaIcon.setVisibility(View.GONE);
            mRelLtyServiceAreContainer.setVisibility(View.GONE);
            mtxtVwServiceZip.setVisibility(View.GONE);
        }else{
            mTxtVwShippingMode.setText("Shipping");
            mRelLtyServiceAreaIcon.setVisibility(View.GONE);
            mRelLtyServiceAreContainer.setVisibility(View.GONE);
            mtxtVwServiceZip.setVisibility(View.GONE);
           // populateZip();
        }


        //===========setProductImage================

        try{
            Picasso.with(this)
                    .load(RippleittAppInstance
                            .formatPicPath(RippleittAppInstance
                                    .getInstance()
                                    .getSELECTED_LISTING_DETAIL_OBJECT()
                                    .getUserinformation().getImage()))
                    .error(R.drawable.default_profile_icon)
                    .placeholder(R.drawable.default_profile_icon)
                    .into(mimgVwprofileImageDetails);

        }catch (Exception e) {
            mimgVwprofileImageDetails.setImageResource(R.drawable.default_profile_icon);
        }

        ///===============setProductName and description========
        mtxtVwProductName.setText(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getListing_name());
        mtxtVwProductDetail.setText(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getListing_description());
        mtxtVwUserNameDetails.setText(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getUserinformation().getFullName());
        mtxtVwAddrDetails.setText(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getLocation().getAddress());
        txtVwAddress.setText(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getLocation().getAddress());
        mtxtVwProductPrice.setText("$"+RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getListing_price());
        mTxtVwReferAmount.setText(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getRefer_amount());

        RippleittAppInstance.WISHLIST_STATUS="0";

        //=======set Wishlist======
        if (RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getIsaddedtowishlist()==0){
            mimgVwAddToWishlist.setBackgroundResource(R.drawable.wishlist_icon);
            RippleittAppInstance.WISHLIST_STATUS="0";
        }
        else {
            RippleittAppInstance.WISHLIST_STATUS="1";
            mimgVwAddToWishlist.setBackgroundResource(R.drawable.wishlist_red_icon);
        }

        int bidCount =0;
        bidCount=RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getBids().length;

        mTxtVwSellerCount.setText("Referred To ("+
                RippleittAppInstance
                        .getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getRecipient_array().length+")");


        if(mtxtVwProductDetail.getText().toString().length()>250){
            mTxtVwShowMore.setVisibility(View.VISIBLE);
        }else{
            mTxtVwShowMore.setVisibility(View.GONE);
        }

        if (RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getListing_type().equals("2")) {
//            relProductPriceDetails.setVisibility(View.GONE);
            linProductQtyDetails.setVisibility(View.GONE);
            linProductShippingDetails.setVisibility(View.GONE);
//            btnBuyNow.setVisibility(View.GONE);
//            mbtnSetYourPrice.setVisibility(View.GONE);
        }   else {
            relProductPriceDetails.setVisibility(View.VISIBLE);
            linProductQtyDetails.setVisibility(View.VISIBLE);
            linProductShippingDetails.setVisibility(View.VISIBLE);
        }


        populateReferrals();
        populateRatingStars();
    }

    private void populateZip(){
        String[] zipArray=RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getPostal_codes();
        String parsedZip="";
        for(String currentZip:zipArray){
            parsedZip=parsedZip+" "+currentZip+",";
        }
        parsedZip=parsedZip.replaceAll(",$","");
        mtxtVwServiceZip.setText(parsedZip);
        mtxtVwServiceZip.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {

        if(view==mRelLytFaqBox){
            if(RippleittAppInstance
                    .getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT().getIs_self_listing().equalsIgnoreCase("1")){
                RippleittAppInstance.getInstance().setListingFaqMode(1);// user is watching public listing...
            }else{
                RippleittAppInstance.getInstance().setListingFaqMode(0);// user is watching public listing...
            }

            startActivity(new Intent(ActivityListingDetailsMyReferral.this, ActivityListingFaq.class));

        }

        if (tvShare == view) {
            SharedPreferences sharedPreferences = ActivityListingDetailsMyReferral.this
                    .getSharedPreferences("preferences", Context.MODE_PRIVATE);
            String name= sharedPreferences.getString("user_name", "");

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Your Friend "+name+" shared you a product "+
                    RippleittAppInstance.getInstance()
                            .getSELECTED_LISTING_DETAIL_OBJECT().getListing_name()+" on www.rippleitt.com");
            sendIntent.setType("text/plain");
//            sendIntent.putExtra(Intent.EXTRA_STREAM, RippleittAppInstance.formatPicPath(RippleittAppInstance.getInstance()
////                    .getSELECTED_LISTING_DETAIL_OBJECT().getListing_photos()[0].getPhoto_path()));
//            sendIntent.setType("image/*");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.strSelectOption)));
        }


        if(view==mRelLtyServiceAreContainer){
            if(mtxtVwServiceZip.getVisibility()==View.VISIBLE){
                mtxtVwServiceZip.setVisibility(View.GONE);
                mRelLtyServiceAreaIcon.setBackgroundResource(R.drawable.arrow_down);
            }else{
                mtxtVwServiceZip.setVisibility(View.VISIBLE);
                mRelLtyServiceAreaIcon.setBackgroundResource(R.drawable.arrow_up);
            }
        }

        if(view==mTxtVwShowMore){
            if(mtxtVwProductDetail.getMaxLines()==2){
                mtxtVwProductDetail.setMaxLines(Integer.MAX_VALUE);
                mTxtVwShowMore.setText("See less");
            }else{
                mtxtVwProductDetail.setMaxLines(2);
                mTxtVwShowMore.setText("See More");
            }

        }

        if(mRelLytOtherSellerWrapper==view || (mTxtVwSellerCount==view)){
            if(mRelLytDownArrowSellers.getBackground().getConstantState()
                    ==getResources().getDrawable(R.drawable.arrow_down).getConstantState()){
                mRelLytDownArrowSellers.setBackgroundResource(R.drawable.arrow_up);
                mLinLytOtherSellersContainer.setVisibility(View.VISIBLE);
            }else{
                mRelLytDownArrowSellers.setBackgroundResource(R.drawable.arrow_down);
                mLinLytOtherSellersContainer.setVisibility(View.GONE);
            }
        }

        //==========View_Seller_profile
        if (view==mtxtVwUserNameDetails){

            RippleittAppInstance.getInstance()
                    .getUserTemplate().setUserName(
                    RippleittAppInstance.getInstance()
                            .getSELECTED_LISTING_DETAIL_OBJECT()
                            .getUserinformation().getFullName());

            RippleittAppInstance.getInstance()
                    .getUserTemplate().setPicPath(
                    RippleittAppInstance.getInstance()
                            .getSELECTED_LISTING_DETAIL_OBJECT()
                            .getUserinformation().getImage());
            startActivity(new Intent(ActivityListingDetailsMyReferral.this,ProfileActivity.class));
        }
        //============Refer_A_friend
        if (view==mbtnReferAFriend){
            //startActivity(new Intent(ActivityListingDetailsMyReferral.this,AddressBookActivity.class));
            //finish();
            showOptionsDialog();
            //startActivity(new Intent(ActivityListingDetailsMyReferral.this,ReferFriendActivity.class));
        }
        //===========Set_Your Price

        //===========BackButton
        if (view==mrelProductDetailsback){
            finish();
        }


        if (view==mimgVwAddToWishlist){
            //==============
            if (RippleittAppInstance.WISHLIST_STATUS.equalsIgnoreCase("0")){
                RippleittAppInstance.WISHLIST_STATUS="1";
                new AddToWishListApi().addToWishlistApi(this,
                        RippleittAppInstance.getInstance().getCURRENT_SELECTED_LISTING_ID());
            }
            else if (RippleittAppInstance.WISHLIST_STATUS.equalsIgnoreCase("1")){
                RippleittAppInstance.WISHLIST_STATUS="0";
                new RemoveProductWishlist().remoeProductWishlistApi(this,"0",
                        RippleittAppInstance.getInstance().getCURRENT_SELECTED_LISTING_ID(),this);
            }
            //=============
        }
    }

    private void populateReferrals(){
        mLinLytOtherSellersContainer.removeAllViews();
        mLinLytOtherSellersContainer.setVisibility(View.GONE);
        LayoutInflater inflater = LayoutInflater.from(ActivityListingDetailsMyReferral.this);
        for(final ReferralEntity referralEntity : RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT()
                                                  .getRecipient_array()){
            View view = LayoutInflater.from(this).inflate(R.layout.layout_referred_to_cell,null);
            CircleImageView userPic = (CircleImageView)view.findViewById(R.id.imgVwBidBuyerPic);
            TextView txtvwUserName=(TextView)view.findViewById(R.id.txtVwBidBuyerName);
            //TextView txtvwListingTitle=(TextView)view.findViewById(R.id.txtvwListingTitle);
            TextView mTxtVwInviteStatus=(TextView)view.findViewById(R.id.txtVwInvited);
            // RelativeLayout bidderDetails = (RelativeLayout)view.findViewById(R.id.relLytBidderDetails);


            try{
                Picasso.with(ActivityListingDetailsMyReferral.this)
                        .load(RippleittAppInstance
                                .formatPicPath(referralEntity.getImage()))
                        .into(userPic);
            }catch (Exception e){
                userPic.setImageResource(R.drawable.default_profile_icon);
            }

            txtvwUserName.setText(referralEntity.getFullName());
            if(referralEntity.getIs_external().equalsIgnoreCase("1")){
                mTxtVwInviteStatus.setText("Invited");
                mTxtVwInviteStatus.setVisibility(View.VISIBLE);
            }else{
                mTxtVwInviteStatus.setVisibility(View.GONE);
            }

            mLinLytOtherSellersContainer.addView(view);

        }
    }

    @Override
    public void onWishListitemSelected(String listing_id) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REFRESH_DATA){
            // this is multi contact selection callback...
            if(resultCode== Activity.RESULT_OK){
                volleyFetchProductDetails(RippleittAppInstance.getInstance().getCURRENT_SELECTED_LISTING_ID());
            }else{
                // Toast.makeText(ActivityHome.this,"No contacts were imported",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void volleyFetchProductDetails(final String listing_id){
        pDliaog.setMessage("Fetching Details...");
        pDliaog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.BASE_URL
                        +RippleittAppInstance.FETCHING_PRODUCT_DETAILS ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDliaog.dismiss();

                        Gson g = new Gson();
                        ProductDetailsResponseTemplate productDetails=
                                (ProductDetailsResponseTemplate)g.fromJson(response,ProductDetailsResponseTemplate.class);
                        RippleittAppInstance.getInstance().setSELECTED_LISTING_DETAIL_OBJECT(productDetails.getData());
                        RippleittAppInstance.getInstance()
                                .setCURRENT_SELECTED_LISTING_ID( RippleittAppInstance
                                        .getInstance()
                                        .getSELECTED_LISTING_DETAIL_OBJECT().getListing_id());
                        work();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDliaog.dismiss();
                Toast.makeText(ActivityListingDetailsMyReferral.this,
                        "could not update listing information",
                        Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", ""+error.getMessage()+","+error.toString());
                CommonUtils.dismissProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ActivityListingDetailsMyReferral.this,
                        PreferenceHandler.AUTH_TOKEN, ""));
                params.put("listing_id",listing_id);
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
                        makeTextViewResizable(tv, -1, "Show less", false);
                    } else {
                        makeTextViewResizable(tv, 3, "Show more", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }


    private void populateRatingStars(){
        String rating = RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getUserinformation()
                .getRating();
        mImgVwRatingStarOne.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarTwo.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        if(rating.equalsIgnoreCase("0") ||rating.equalsIgnoreCase("0.0")){
            mImgVwRatingStarOne.setVisibility(View.INVISIBLE);
            mImgVwRatingStarTwo.setVisibility(View.INVISIBLE);
            mImgVwRatingStarThree.setVisibility(View.INVISIBLE);
            mImgVwRatingStarFour.setVisibility(View.INVISIBLE);
            mImgVwRatingStarFive.setVisibility(View.INVISIBLE);
            mTxtVwNoRating.setVisibility(View.VISIBLE);
        }
        if(rating.equalsIgnoreCase("1")){
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }if(rating.equalsIgnoreCase("1.5")){
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.half_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        } if(rating.equalsIgnoreCase("2")){
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        } if(rating.equalsIgnoreCase("2.5")){
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.half_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        } if(rating.equalsIgnoreCase("3")){
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        } if(rating.equalsIgnoreCase("3.5")){
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.half_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        } if(rating.equalsIgnoreCase("4")){
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }  if(rating.equalsIgnoreCase("4.5")){
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.half_rating);
        } if(rating.equalsIgnoreCase("5")){
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.full_rating);
        }


    }



    private void showOptionsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityListingDetailsMyReferral.this);
        builder.setTitle("Refer a Friend");

        // add a list
        String[] animals = {"Invite a Friend", "Share with Address Book", "Cancel"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        startActivity(new Intent(ActivityListingDetailsMyReferral.this,ReferFriendActivity.class));
                        break;
                    case 1:

                        startActivity(new Intent(ActivityListingDetailsMyReferral.this,AddressBookActivity.class));
                        break;
                    case 2:
                        dialog.dismiss();
                        break;

                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
