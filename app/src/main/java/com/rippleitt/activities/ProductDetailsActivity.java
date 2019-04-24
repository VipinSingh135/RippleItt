package com.rippleitt.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.adapters.BuyerBidsAdapter;
import com.rippleitt.adapters.CustomViewPagerAdapter;
import com.rippleitt.adapters.ListingBidsAdapter;
import com.rippleitt.callback.WishlistSelectCallback;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.BidTemplate;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.modals.OtherProductsTemplate;
import com.rippleitt.modals.ProductDetailsResponseTemplate;
import com.rippleitt.utils.CommonUtils;
import com.rippleitt.webservices.AddToWishListApi;
import com.rippleitt.webservices.GetProductDetailsApi;
import com.rippleitt.webservices.RemoveProductWishlist;
import com.robohorse.pagerbullet.PagerBullet;
import com.squareup.picasso.Picasso;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener, WishlistSelectCallback {

    //RelativeLayout mrelShareProfileDetails;
    // RelativeLayout mrelViewProfileDetails;
    RelativeLayout mrelProductDetailsback;
    RelativeLayout relLytFAQ;
    ImageView mimgVwProductImage;
    ImageView mimgVwAddToWishlist;
    TextView mtxtVwProductName;
    TextView mtxtVwProductDetail;
    TextView mtxtVwUserNameDetails;
    TextView mtxtVwAddrDetails;
    TextView mtxtVwProductPrice;
    TextView mTxtVwReferAmount;
    TextView txtVwVoucher;
    TextView mTxtVwMyBid;
    Button mbtnReferAFriend;
    Button mbtnSetYourPrice;
    Button mbtnSellOnReppliett;
    PagerBullet vwPagerProductImages;
    CircleImageView mimgVwprofileImageDetails;
    CustomViewPagerAdapter customViewPagerAdapter;
    private final int REFRESH_DATA = 78;
    ProgressDialog pDliaog;
    private ScrollView mScrlViewParent;
    private ListingBidsAdapter bidsAdapter;
    private RelativeLayout relOtherBuyers, mRelLytOtherSellerWrapper;
    private LinearLayout mLinLytBidsContainer, mLinLytOtherSellersContainer;
    private RelativeLayout mRelLytDownArrowBids, mRelLytDownArrowSellers;
    private TextView mTxtVwBidCount, mTxtVwSellerCount;
    private LinearLayout mLinLytRootView;
    private TextView mTxtVwShowMore;
    private TextView mTxtVWAvailableQty, mTxtVwPaymentMode;
    private Button mBtnFaq, btnBuyNow;
    // rating stars...
    private ImageView mImgVwRatingStarOne, mImgVwRatingStarTwo,
            mImgVwRatingStarThree, mImgVwRatingStarFour, mImgVwRatingStarFive;
    private RelativeLayout mRelLtyServiceAreContainer, mRelLtyServiceAreaIcon;
    private TextView mtxtVwServiceZip, mTxtVwShippingMode, mTxtVwNoRating;
    private RelativeLayout relProductPriceDetails;
    LinearLayout linProductQtyDetails, linProductShippingDetails;
    FrameLayout mFrmLytBadge;
    TextView mTxtvwBadgeStatus;
    View viewServiceCodesDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        init();
        work();
    }

    public void init() {

        mTxtVwNoRating = (TextView) findViewById(R.id.txtvwNoRating);
        mTxtVwNoRating.setVisibility(View.GONE);
        mTxtVwNoRating.setPaintFlags(mTxtVwNoRating.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mTxtVwNoRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtxtVwUserNameDetails.performClick();
            }
        });
        relLytFAQ = (RelativeLayout) findViewById(R.id.relCommentsBox);
        relLytFAQ.setOnClickListener(this);
        mRelLtyServiceAreContainer = (RelativeLayout) findViewById(R.id.relServiceAreas);
        mRelLtyServiceAreaIcon = (RelativeLayout) findViewById(R.id.relServiceAreaIcon);
        mTxtVwShippingMode = (TextView) findViewById(R.id.txtvwDeliveryMode);
        mtxtVwServiceZip = (TextView) findViewById(R.id.txtvwServiceCodes);
        txtVwVoucher = (TextView) findViewById(R.id.txtVwVoucher);
        mTxtVwPaymentMode = (TextView) findViewById(R.id.txtvwPaymentMode);
        mFrmLytBadge = (FrameLayout) findViewById(R.id.frmlytOrderFlagColor);
        mTxtvwBadgeStatus = (TextView) findViewById(R.id.txtvwLabelOrderStatus);
        viewServiceCodesDivider = findViewById(R.id.viewServiceCodesDivider);
        mRelLtyServiceAreContainer.setOnClickListener(this);

        mScrlViewParent = (ScrollView) findViewById(R.id.scrlVwParent);
        pDliaog = new ProgressDialog(this);
        pDliaog.setCancelable(false);
        pDliaog.setMessage("Updating details...");
        mBtnFaq = (Button) findViewById(R.id.btnFAQ);
        btnBuyNow = (Button) findViewById(R.id.btnBuyNow);
        btnBuyNow.setOnClickListener(this);
        mBtnFaq.setOnClickListener(this);
        mBtnFaq.setVisibility(View.GONE);
        RippleittAppInstance.getInstance().setFullScreenImageMode(1);
        mTxtVwShowMore = (TextView) findViewById(R.id.txtvwShowMore);
        mTxtVwShowMore.setPaintFlags(mTxtVwShowMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mTxtVwShowMore.setOnClickListener(this);
        // new GetProductDetailsApi().getProductDetailsApi(this, RippleittAppInstance.PRODUCT_ID);
        vwPagerProductImages = (PagerBullet) findViewById(R.id.vwPagerProductImages);
        mLinLytBidsContainer = (LinearLayout) findViewById(R.id.linLytListingBids);
        mLinLytOtherSellersContainer = (LinearLayout) findViewById(R.id.linlytOtherSellers);
        mRelLytDownArrowBids = (RelativeLayout) findViewById(R.id.relOtherBuyersIcon);
        mRelLytDownArrowSellers = (RelativeLayout) findViewById(R.id.relOtherSellersIcon);
        mRelLytOtherSellerWrapper = (RelativeLayout) findViewById(R.id.relOtherSellers);
        relProductPriceDetails = findViewById(R.id.relProductPriceDetails);
        linProductShippingDetails = findViewById(R.id.linProductShippingDetails);
        linProductQtyDetails = findViewById(R.id.linProductQtyDetails);
        relOtherBuyers = findViewById(R.id.relOtherBuyers);
        viewServiceCodesDivider = findViewById(R.id.viewServiceCodesDivider);

        mTxtVWAvailableQty = (TextView) findViewById(R.id.txtvwAvailableQty);
        //   mrelViewProfileDetails=(RelativeLayout)findViewById(R.id.relViewProfileDetails);
        mrelProductDetailsback = (RelativeLayout) findViewById(R.id.relProductDetailsback);
        mImgVwRatingStarOne = (ImageView) findViewById(R.id.imgvwRatingStarOne);
        mImgVwRatingStarTwo = (ImageView) findViewById(R.id.imgvwRatingStarTwo);
        mImgVwRatingStarThree = (ImageView) findViewById(R.id.imgvwRatingStarThree);
        mImgVwRatingStarFour = (ImageView) findViewById(R.id.imgvwRatingStarFour);
        mImgVwRatingStarFive = (ImageView) findViewById(R.id.imgvwRatingStarFive);
        mTxtVwBidCount = (TextView) findViewById(R.id.txtvwBidCount);
        mTxtVwSellerCount = (TextView) findViewById(R.id.txtvwSellerCount);
        mTxtVwBidCount.setOnClickListener(this);
        mTxtVwSellerCount.setOnClickListener(this);
        mtxtVwProductName = (TextView) findViewById(R.id.txtVwProductName);
        mtxtVwAddrDetails = (TextView) findViewById(R.id.txtVwAddrDetails);
        mtxtVwProductDetail = (TextView) findViewById(R.id.txtVwProductDetail);
        mtxtVwProductPrice = (TextView) findViewById(R.id.txtVwProductPrice);
        mTxtVwReferAmount = (TextView) findViewById(R.id.txtVwProductReferAmount);
        mtxtVwUserNameDetails = (TextView) findViewById(R.id.txtVwUserNameDetails);
        mimgVwAddToWishlist = (ImageView) findViewById(R.id.imgVwAddToWishlist);
        mbtnReferAFriend = (Button) findViewById(R.id.btnReferAFriend);
        mbtnSetYourPrice = (Button) findViewById(R.id.btnSetYourPrice);
        mbtnSellOnReppliett = (Button) findViewById(R.id.btnSellOnReppliett);
        mimgVwprofileImageDetails = (CircleImageView) findViewById(R.id.imgVwprofileImageDetails);


        // mrelViewProfileDetails.setOnClickListener(this);
        mbtnReferAFriend.setOnClickListener(this);
        mbtnSetYourPrice.setOnClickListener(this);
        mrelProductDetailsback.setOnClickListener(this);
        mbtnSellOnReppliett.setOnClickListener(this);
        mimgVwAddToWishlist.setOnClickListener(this);
        relOtherBuyers.setOnClickListener(this);
        mRelLytOtherSellerWrapper.setOnClickListener(this);
        configViewPagerClickListener();
        try {
            populateRatingStars();
        } catch (Exception e) {

        }
        txtVwVoucher.setVisibility(View.GONE);
    }

    private void populateBids() {

        mLinLytBidsContainer.removeAllViews();
        mLinLytBidsContainer.setVisibility(View.GONE);
        LayoutInflater inflater = LayoutInflater.from(ProductDetailsActivity.this);
        for (final BidTemplate currentBid : RippleittAppInstance
                .getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getBids()) {
            // now we populate other bids...
            View view = LayoutInflater.from(this).inflate(R.layout.listing_bids_view, null);
            CircleImageView userPic = (CircleImageView) view.findViewById(R.id.imgVwBidBuyerPic);
            TextView txtvwUserName = (TextView) view.findViewById(R.id.txtVwBidBuyerName);
            TextView txtvwBidAmount = (TextView) view.findViewById(R.id.txtvwBidAmount);
            TextView txtvwMinutes = (TextView) view.findViewById(R.id.txtvwMinutes);
            // RelativeLayout bidderDetails = (RelativeLayout)view.findViewById(R.id.relLytBidderDetails);
            try {
                Picasso.with(ProductDetailsActivity.this)
                        .load(RippleittAppInstance
                                .formatPicPath(currentBid.getUserinformation().getImage()))
                        .into(userPic);
            } catch (Exception e) {
                userPic.setImageResource(R.drawable.default_profile_icon);
            }


            txtvwUserName.setText(currentBid.getUserinformation().getFullName());
            txtvwMinutes.setVisibility(View.GONE);
//            if (Integer.parseInt(currentBid.getMinutes()) < 1440) {
//
//                txtvwMinutes.setTextColor(Color.parseColor("#FFAFAFAF"));
//                txtvwMinutes.setText(com.rippleitt.commonUtilities.CommonUtils.minutesToHours(Integer.parseInt(currentBid.getMinutes())));
//
//            } else {
//                txtvwMinutes.setTextColor(Color.parseColor("#b74242"));
//                txtvwMinutes.setText("Expired");
//
//            }
            txtvwBidAmount.setText("$" + currentBid.getBidprice() + " ("
                    + currentBid.getQuantity()
                    + " Qty" + ")");
            if (currentBid.getIs_my_bid().equalsIgnoreCase("1")) {
                txtvwUserName.setText("Your Bid");
                txtvwUserName.setTextColor(Color.parseColor("#65b488"));
                txtvwBidAmount.setTextColor(Color.parseColor("#65b488"));
            }


            mLinLytBidsContainer.addView(view);
        }
    }

    private void configViewPagerClickListener() {
    }

    public void work() {
        if (RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getPayment_mode().equalsIgnoreCase("2")) {
            mTxtVwPaymentMode.setText("Credit Card");
        } else {
            mTxtVwPaymentMode.setText("Cash");
        }


        if (RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getIs_new().equals("1")) {
            mFrmLytBadge.setBackgroundColor(Color.parseColor("#2f4fb9"));
            mTxtvwBadgeStatus.setText(" New ");
        } else {
            mFrmLytBadge.setBackgroundColor(Color.parseColor("#d2742d"));
            mTxtvwBadgeStatus.setText(" Used ");
        }

        if (RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getListing_type().equalsIgnoreCase("1")) {
            mFrmLytBadge.setVisibility(View.VISIBLE);
        } else {
            mFrmLytBadge.setVisibility(View.GONE);
        }


        if (RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
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
            //populateZip();
        }

        customViewPagerAdapter = new CustomViewPagerAdapter(ProductDetailsActivity.this);
        vwPagerProductImages = (PagerBullet) findViewById(R.id.vwPagerProductImages);
        vwPagerProductImages.setIndicatorTintColorScheme(Color.WHITE, Color.parseColor("#289b98"));
        vwPagerProductImages.setAdapter(customViewPagerAdapter);

        //===========setBuyNow================
        if (RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getDirect_buy().equalsIgnoreCase("1")) {
            btnBuyNow.setVisibility(View.VISIBLE);
            mbtnSetYourPrice.setVisibility(View.GONE);
        } else if (RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getDirect_buy().equalsIgnoreCase("2")) {
            btnBuyNow.setVisibility(View.VISIBLE);
            mbtnSetYourPrice.setVisibility(View.VISIBLE);
        } else {
            btnBuyNow.setVisibility(View.GONE);
            mbtnSetYourPrice.setVisibility(View.VISIBLE);
        }

        //===========setProductImage================
        if (RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT()
                .getMy_bid_amount().equalsIgnoreCase("0")) {

        } else {
            mbtnSetYourPrice.setText("Update Offer");
        }
        try {
            Picasso.with(this)
                    .load(RippleittAppInstance
                            .formatPicPath(RippleittAppInstance
                                    .getInstance()
                                    .getSELECTED_LISTING_DETAIL_OBJECT()
                                    .getUserinformation().getImage()))
                    .error(R.drawable.default_profile_icon)
                    .placeholder(R.drawable.default_profile_icon)
                    .into(mimgVwprofileImageDetails)
            ;

        } catch (Exception e) {
            mimgVwprofileImageDetails.setImageResource(R.drawable.default_profile_icon);
        }

        ///===============setProductName and description========
        mTxtVWAvailableQty.setText(RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getQuantity() + " ");
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
        mtxtVwProductPrice.setText("$" + RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getListing_price());
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

        int bidCount = 0;
        bidCount = RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getBids().length;
        mTxtVwBidCount.setText("Current Offers (" + Integer.toString(bidCount) + ")");
        mTxtVwSellerCount.setText("Other Listings by this Seller (" +
                RippleittAppInstance
                        .getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getOtherproducts().length + ")");

        if (RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getIs_self_listing().equalsIgnoreCase("1")) {
            mbtnSetYourPrice.setVisibility(View.GONE);
            btnBuyNow.setVisibility(View.GONE);
            mBtnFaq.setVisibility(View.GONE);
            relLytFAQ.setVisibility(View.GONE);
            //mtxtVwUserNameDetails.setTextColor(Color.parseColor("#000000"));
        } else {
            //mtxtVwUserNameDetails.setPaintFlags(mtxtVwUserNameDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            //mtxtVwUserNameDetails.setOnClickListener(this);
        }
        mtxtVwUserNameDetails.setPaintFlags(mtxtVwUserNameDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mtxtVwUserNameDetails.setOnClickListener(this);
        if (mtxtVwProductDetail.getText().toString().length() > 180) {
            mTxtVwShowMore.setVisibility(View.VISIBLE);
        } else {
            mTxtVwShowMore.setVisibility(View.GONE);
        }

        if (RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getListing_type().equals("2")) {
//            relProductPriceDetails.setVisibility(View.GONE);
            linProductQtyDetails.setVisibility(View.GONE);
            viewServiceCodesDivider.setVisibility(View.GONE);
            linProductShippingDetails.setVisibility(View.GONE);
//            relOtherBuyers.setVisibility(View.GONE);
//            btnBuyNow.setVisibility(View.GONE);
//            mbtnSetYourPrice.setVisibility(View.GONE);
            populateBids();
        } else {
            relProductPriceDetails.setVisibility(View.VISIBLE);
            linProductQtyDetails.setVisibility(View.VISIBLE);
            linProductShippingDetails.setVisibility(View.VISIBLE);
            viewServiceCodesDivider.setVisibility(View.VISIBLE);
            relOtherBuyers.setVisibility(View.VISIBLE);
            populateBids();
        }
        populateOtherproducts();

        if (RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT().getHas_voucher().equals("1") &&
                RippleittAppInstance.getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_details() != null) {
            txtVwVoucher.setVisibility(View.VISIBLE);
            if (RippleittAppInstance.getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_details().getType()!= null) {
                if (RippleittAppInstance.getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_details().getType().equals("1")) {
                    txtVwVoucher.setText("$" + RippleittAppInstance.getInstance()
                            .getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_details().getAmount() + " Off");
                } else {
                    txtVwVoucher.setText(RippleittAppInstance.getInstance()
                            .getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_details().getAmount() + "% Off");
                }
            }
        }else{
            txtVwVoucher.setVisibility(View.GONE);

        }
    }

    private void showOptionsDialog() {
        final String[] animals = {"Invite a Friend", "Share with Address Book", "Cancel"};
        ListAdapter adapter = new ArrayAdapter(
                getApplicationContext(), R.layout.custom_dialog_row, animals) {

            ViewHolder holder;


            class ViewHolder {

                TextView title;
            }

            public View getView(int position, View convertView,
                                ViewGroup parent) {
                final LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);

                if (convertView == null) {
                    convertView = inflater.inflate(
                            R.layout.custom_dialog_row, null);

                    holder = new ViewHolder();
                    holder.title = (TextView) convertView
                            .findViewById(R.id.txtvwRowItem);
                    convertView.setTag(holder);
                } else {
                    // view already defined, retrieve view holder
                    holder = (ViewHolder) convertView.getTag();
                }


                holder.title.setText(animals[position]);

                return convertView;
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ProductDetailsActivity.this,
                R.style.AppCompatAlertDialogStyle));


        // add a list


        builder.setAdapter(adapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int item) {
                        switch (item) {
                            case 0:
                                startActivity(new Intent(ProductDetailsActivity.this, ReferFriendActivity.class));
                                break;
                            case 1:

                                startActivity(new Intent(ProductDetailsActivity.this, AddressBookActivity.class));
                                break;
                            case 2:
                                dialog.dismiss();
                                break;

                        }
                        dialog.dismiss();
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void populateZip() {
        String[] zipArray = RippleittAppInstance
                .getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getPostal_codes();
        String parsedZip = "";
        for (String currentZip : zipArray) {
            parsedZip = parsedZip + " " + currentZip + ",";
        }
        parsedZip = parsedZip.replaceAll(",$", "");
        mtxtVwServiceZip.setText(parsedZip);
        mtxtVwServiceZip.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {

        if (view == relLytFAQ) {
            if (RippleittAppInstance
                    .getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT().getIs_self_listing().equalsIgnoreCase("1")) {
                RippleittAppInstance.getInstance().setListingFaqMode(1);// user is watching public listing...
            } else {
                RippleittAppInstance.getInstance().setListingFaqMode(0);// user is watching public listing...
            }

            startActivity(new Intent(ProductDetailsActivity.this, ActivityListingFaq.class));
        }

        if (view == mRelLtyServiceAreContainer) {
            if (mtxtVwServiceZip.getVisibility() == View.VISIBLE) {
                mtxtVwServiceZip.setVisibility(View.GONE);
                mRelLtyServiceAreaIcon.setBackgroundResource(R.drawable.arrow_down);
            } else {
                mtxtVwServiceZip.setVisibility(View.VISIBLE);
                mRelLtyServiceAreaIcon.setBackgroundResource(R.drawable.arrow_up);
            }
        }

        if (mBtnFaq == view) {
            //RippleittAppInstance.getInstance().setListingFaqMode(0);// user is watching public listing...
            //startActivity(new Intent(Pro ductDetailsActivity.this, ActivityListingFaq.class));
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
        if ((relOtherBuyers == view) || (mTxtVwBidCount == view)) {
            if (mRelLytDownArrowBids.getBackground().getConstantState()
                    == getResources().getDrawable(R.drawable.arrow_down).getConstantState()) {
                mRelLytDownArrowBids.setBackgroundResource(R.drawable.arrow_up);
                mLinLytBidsContainer.setVisibility(View.VISIBLE);
            } else {
                mRelLytDownArrowBids.setBackgroundResource(R.drawable.arrow_down);
                mLinLytBidsContainer.setVisibility(View.GONE);
            }
        }
        if (mRelLytOtherSellerWrapper == view || (mTxtVwSellerCount == view)) {
            if (mRelLytDownArrowSellers.getBackground().getConstantState()
                    == getResources().getDrawable(R.drawable.arrow_down).getConstantState()) {
                mRelLytDownArrowSellers.setBackgroundResource(R.drawable.arrow_up);
                mLinLytOtherSellersContainer.setVisibility(View.VISIBLE);
            } else {
                mRelLytDownArrowSellers.setBackgroundResource(R.drawable.arrow_down);
                mLinLytOtherSellersContainer.setVisibility(View.GONE);
            }
        }

        //==========View_Seller_profile
        if (view == mtxtVwUserNameDetails) {

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
            startActivity(new Intent(ProductDetailsActivity.this, ProfileActivity.class));
        }
        //============Refer_A_friend
        if (view == mbtnReferAFriend) {
            showOptionsDialog();
            //startActivity(new Intent(ProductDetailsActivity.this,AddressBookActivity.class));
            //startActivity(new Intent(ProductDetailsActivity.this,ReferFriendActivity.class));
        }
        //===========Set_Your Price
        if (view == mbtnSetYourPrice) {

            Intent i = new Intent(ProductDetailsActivity.this, SetYourPriceActivity.class);
            startActivityForResult(i, REFRESH_DATA);
        }
        //===========Buy Now
        if (view == btnBuyNow) {

            Intent i = new Intent(ProductDetailsActivity.this, SetYourPriceActivity.class);
            i.putExtra("isBuyNow", true);
            i.putExtra("price", RippleittAppInstance.getInstance().getSELECTED_LISTING_DETAIL_OBJECT().getListing_price());

            if (RippleittAppInstance.getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT().getHas_referral().equals("1") &&
                    RippleittAppInstance.getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_details() != null) {

                i.putExtra("voucher_id", RippleittAppInstance.getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_details().getVoucherId());
                i.putExtra("voucher_price", RippleittAppInstance.getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_details().getAmount());
                i.putExtra("voucher_type", RippleittAppInstance.getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT().getVoucher_details().getType());

            }
            startActivityForResult(i, REFRESH_DATA);
        }
        //===========BackButton
        if (view == mrelProductDetailsback) {
            finish();
        }

        if (view == mbtnSellOnReppliett) {
            startActivity(new Intent(ProductDetailsActivity.this, AddNewProduct.class));
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

    private void populateOtherproducts() {
        mLinLytOtherSellersContainer.removeAllViews();
        mLinLytOtherSellersContainer.setVisibility(View.GONE);
        LayoutInflater inflater = LayoutInflater.from(ProductDetailsActivity.this);
        for (final OtherProductsTemplate otherprduct : RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getOtherproducts()) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_other_product, null);
            CircleImageView userPic = (CircleImageView) view.findViewById(R.id.imgVwBidBuyerPic);
            TextView txtvwListingName = (TextView) view.findViewById(R.id.txtvwListingTitle);
            TextView txtvwListingDetails = (TextView) view.findViewById(R.id.txtVwDetails);
            txtvwListingDetails.setPaintFlags(txtvwListingDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            TextView mTxtVwListingPrice = (TextView) view.findViewById(R.id.txtVwPrice);
            try {
                Picasso.with(ProductDetailsActivity.this)
                        .load(RippleittAppInstance
                                .formatPicPath(otherprduct.getListing_photos()[0].getPhoto_path()))
                        .into(userPic);
            } catch (Exception e) {
                userPic.setImageResource(R.drawable.default_profile_icon);
            }

            txtvwListingName.setText(otherprduct.getListing_name());
            mTxtVwListingPrice.setText("$" + otherprduct.getPrice());
            txtvwListingDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    volleyFetchProductDetails(otherprduct.getListing_id());
                }
            });
            if (otherprduct.getListing_type().equals("1")) {
                mTxtVwListingPrice.setVisibility(View.VISIBLE);
            } else {
//                mTxtVwListingPrice.setVisibility(View.INVISIBLE);
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

        if (requestCode == REFRESH_DATA) {
            // this is multi contact selection callback...
            if (resultCode == Activity.RESULT_OK) {
                volleyFetchProductDetails(RippleittAppInstance.getInstance().getCURRENT_SELECTED_LISTING_ID());
            } else {
                // Toast.makeText(ActivityHome.this,"No contacts were imported",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void volleyFetchProductDetails(final String listing_id) {
        pDliaog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.BASE_URL
                        + RippleittAppInstance.FETCHING_PRODUCT_DETAILS,
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
                Toast.makeText(ProductDetailsActivity.this,
                        "could not update listing information",
                        Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("",   "" + error.getMessage() + "," + error.toString());
                CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", PreferenceHandler.readString(ProductDetailsActivity.this,
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
                .getUserinformation()
                .getRating();
        mImgVwRatingStarOne.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarTwo.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
        mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        if (rating.equalsIgnoreCase("0") || rating.equalsIgnoreCase("0.0")) {
            mImgVwRatingStarOne.setVisibility(View.INVISIBLE);
            mImgVwRatingStarTwo.setVisibility(View.INVISIBLE);
            mImgVwRatingStarThree.setVisibility(View.INVISIBLE);
            mImgVwRatingStarFour.setVisibility(View.INVISIBLE);
            mImgVwRatingStarFive.setVisibility(View.INVISIBLE);
            mTxtVwNoRating.setVisibility(View.VISIBLE);
        }
        if (rating.equalsIgnoreCase("1")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equalsIgnoreCase("1.user")) {
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
        if (rating.equalsIgnoreCase("2.user")) {
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
        if (rating.equalsIgnoreCase("3.user")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.half_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equalsIgnoreCase("mail")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }
        if (rating.equalsIgnoreCase("mail.user")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.half_rating);
        }
        if (rating.equalsIgnoreCase("user")) {
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.full_rating);
        }
    }

}
