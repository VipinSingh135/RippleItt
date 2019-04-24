package com.rippleitt.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rippleitt.R;
import com.rippleitt.adapters.ReviewListAdapter;
import com.rippleitt.controller.RippleittAppInstance;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    ReviewListAdapter reviewListAdapter;
    ListView mlistVwReviewsProfile;
    RelativeLayout mrelProfile_back;
    ImageView mImgVwProfilePic;
    TextView mTxtVwSellerName;
    TextView mTxtVwSellerEmail;
    ListView mLstViwReviewsList;
    ImageView mImgVwChatIcon;
    TextView mtxtVwNoReviewsTag,mtxtVwReviewCount, mTxtVwNoRating;
    private ImageView mImgVwRatingStarOne,mImgVwRatingStarTwo,
            mImgVwRatingStarThree,mImgVwRatingStarFour,mImgVwRatingStarFive;
    private ReviewListAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        work();
        populateReviewList();
    }

    public void init(){
        mTxtVwNoRating=(TextView)findViewById(R.id.txtvwNoRating);
        mTxtVwNoRating.setVisibility(View.INVISIBLE);
        mImgVwChatIcon=(ImageView)findViewById(R.id.imgVwChatIcon);
        mImgVwChatIcon.setOnClickListener(this);
        mtxtVwReviewCount=(TextView)findViewById(R.id.txtVwReviewProfile);
        mLstViwReviewsList=(ListView)findViewById(R.id.lstVwReviewsList);
        mtxtVwNoReviewsTag=(TextView)findViewById(R.id.txtvwNoReviews);
        mImgVwProfilePic=(ImageView)findViewById(R.id.imgVwprofileImage);
        mTxtVwSellerName=(TextView)findViewById(R.id.txtVwUserNameProfile);
        mTxtVwSellerEmail=(TextView)findViewById(R.id.txtVwUserEmailProfile);
        mlistVwReviewsProfile=(ListView)findViewById(R.id.lstVwReviewsList);
        mrelProfile_back=(RelativeLayout)findViewById(R.id.relProfile_back);
        mImgVwRatingStarOne=(ImageView)findViewById(R.id.imgvwRatingStarOne);
        mImgVwRatingStarTwo=(ImageView)findViewById(R.id.imgvwRatingStarTwo);
        mImgVwRatingStarThree=(ImageView)findViewById(R.id.imgvwRatingStarThree);
        mImgVwRatingStarFour=(ImageView)findViewById(R.id.imgvwRatingStarFour);
        mImgVwRatingStarFive=(ImageView)findViewById(R.id.imgvwRatingStarFive);
        mrelProfile_back.setOnClickListener(this);
    }

    public void work(){
      mTxtVwSellerName.setText(RippleittAppInstance.getInstance().getUserTemplate().getUserName());
     // mTxtVwSellerEmail.setText(RippleittAppInstance.getHashMapProductDetails().get("email"));

        if(RippleittAppInstance.getInstance().getUserTemplate().getPicPath()==null
                ||
                RippleittAppInstance.getInstance().getUserTemplate().getPicPath().equalsIgnoreCase("")
                ){
            mImgVwProfilePic.setImageResource(R.drawable.default_profile_icon);
        }else{
            Glide.with(this)
                    .load(RippleittAppInstance.formatPicPath(RippleittAppInstance.getInstance().getUserTemplate().getPicPath()))
                    .asBitmap()// Uri of the picture
                    .error(R.drawable.default_profile_icon)
                    .placeholder(R.drawable.default_profile_icon)
                    .into(mImgVwProfilePic);
        }

        populateRatingStars();
    }

    @Override
    public void onClick(View view) {
       if(view== mrelProfile_back){
           finish();
       }if(view==mImgVwChatIcon){
            RippleittAppInstance.getInstance().setCurrentChatPartner(
                    RippleittAppInstance.getInstance()
            .getSELECTED_LISTING_DETAIL_OBJECT().getUserinformation());
            startActivity(new Intent(ProfileActivity.this, ActivityChatDetails.class));
        }
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
        if(rating.equalsIgnoreCase("0")|| rating.equalsIgnoreCase("0.0")){
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
        }if(rating.equalsIgnoreCase("1.user")){
            mImgVwRatingStarOne.setImageResource(R.drawable.full_rating);
            mImgVwRatingStarTwo.setImageResource(R.drawable.half_rating);
            mImgVwRatingStarThree.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFour.setImageResource(R.drawable.blank_rating);
            mImgVwRatingStarFive.setImageResource(R.drawable.blank_rating);
        }  if(rating.equalsIgnoreCase("2")){
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
        } if(rating.equalsIgnoreCase("4.5")){
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

    private void populateReviewList(){
        if(RippleittAppInstance.getInstance()
                .getSELECTED_LISTING_DETAIL_OBJECT()
                .getUserinformation().getReviews()!=null
           &&
                RippleittAppInstance.getInstance()
                        .getSELECTED_LISTING_DETAIL_OBJECT()
                        .getUserinformation().getReviews().length!=0
                ){
            mtxtVwNoReviewsTag.setVisibility(View.GONE);
            mTxtVwNoRating.setVisibility(View.GONE);
            mtxtVwReviewCount.setText("Reviews ("+ Integer.toString(RippleittAppInstance
                                                .getInstance()
                                                .getSELECTED_LISTING_DETAIL_OBJECT()
                                                .getUserinformation().getReviews().length)+")");

            reviewAdapter=new ReviewListAdapter(ProfileActivity.this,RippleittAppInstance
                    .getInstance()
                    .getSELECTED_LISTING_DETAIL_OBJECT()
                    .getUserinformation().getReviews());
            mlistVwReviewsProfile.setAdapter(reviewAdapter);
        }else{
            mtxtVwReviewCount.setText("Reviews(0)");
            mtxtVwNoReviewsTag.setVisibility(View.VISIBLE);
            mTxtVwNoRating.setVisibility(View.VISIBLE);
        }
    }
}
