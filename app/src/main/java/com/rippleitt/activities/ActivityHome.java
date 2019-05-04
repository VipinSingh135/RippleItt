package com.rippleitt.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.rippleitt.R;
import com.rippleitt.adapters.SliderListAdapter;
import com.rippleitt.commonUtilities.CommonUtils;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.NewMessageReceived;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.fragments.FragmentAddressBook;
import com.rippleitt.fragments.FragmentBuyerDashboard;
import com.rippleitt.fragments.FragmentHome;
import com.rippleitt.fragments.FragmentManageListing;
import com.rippleitt.fragments.FragmentMessages;
import com.rippleitt.fragments.FragmentMyBids;
import com.rippleitt.fragments.FragmentMyDisbursals;
import com.rippleitt.fragments.FragmentMyDisputes;
import com.rippleitt.fragments.FragmentMyOrders;
import com.rippleitt.fragments.FragmentMyReferrals;
import com.rippleitt.fragments.FragmentMyVouchers;
import com.rippleitt.fragments.FragmentNotification;
import com.rippleitt.fragments.FragmentProfile;
import com.rippleitt.fragments.FragmentRecomendationsForMe;
import com.rippleitt.fragments.FragmentSettings;
import com.rippleitt.fragments.FragmentUnlockedVouchers;
import com.rippleitt.fragments.FragmentWallet;
import com.rippleitt.fragments.FragmentWishlist;
import com.rippleitt.modals.FbAuthTemplate;
import com.rippleitt.modals.NotificationsResponseTemplate;
import com.rippleitt.modals.SliderItems;
import com.squareup.picasso.Picasso;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, NewMessageReceived {

    // some random commit.....

    private ListView mLstVwSliderOptions;
    private LinearLayout mlytFooterLogout;
    private SliderListAdapter sliderAdapter;
    private FragmentManager mFragmentManager;
    private ImageView imgvwtoggleMap, imgvwToggleSearchBar;
    private final String TAG_FRAGMENT_HOME = "frag_home";
    private final String TAG_FRAGMENT_BUYER_DASHBOARD = "frag_buyer_dashboard";
    private final String TAG_FRAGMENT_SELLER_DASHBOARD = "frag_seller_dashboard";
    private final String TAG_FRAGMENT_MANAGE_LISTINGS = "frag_manage_listing";
    private final String TAG_FRAGMENT_MESSAGES = "frag_messages";
    private final String TAG_FRAGMENT_NOTIFICATIONS = "frag_notifications";
    private final String TAG_FRAGMENT_PROFILE = "frag_profile";
    private final String TAG_FRAGMENT_SETTINGS = "frag_settings";
    private final String TAG_FRAGMENT_MY_BIDS = "frag_my_bids";
    private final String TAG_FRAGMENT_WALLET = "frag_wallet";
    private final String TAG_FRAGMENT_WISHLIST = "frag_wishlist";
    private final String TAG_FRAGMENT_ADDRESS_BOOK = "frag_address_book";
    private final String TAG_FRAGMENT_MY_REFERRALS = "frag_my_referrals";
    private final String TAG_FRAGMENT_UNLOCKED_VOUCHERS = "frag_unlocked_vouchers";
    private final String TAG_FRAGMENT_RECOMENDATIONS_FOR_ME = "frag_receomendations_for_me";
    private final String TAG_FRAGMENT_VOUCHERS = "frag_my_vouchers";
    private final String TAG_FRAGMENT_MY_ORDERS = "frag_my_orders";
    private final String TAG_FRAGMENT_MY_DISPUTES = "frag_my_disputes";
    private final String TAG_FRAGMENT_MY_DISBURSALS = "frag_my_disbursals";
    private final String TAG_FRAGMENT_INVITE_FRIENDS = "frag_invite_friends";
    private LinearLayout mLinLytLogoutContainer;
    private TextView mTxtvwUserName, mTxtVwUserEmail;
    private CircleImageView mImgVwUserProfilePic;
    private int triggerMode = 1; //1 -- filter, 2-- add 1
    private FragmentHome fragmentHomeInstance;
    private FragmentAddressBook fragmentAddressBook;
    private FragmentNotification fragNotification;
    private ImageView mImgVwMapToggle;
    private ImageView mImgVwAddListing;
    private RelativeLayout relGuide;
    private final int START_ACTION_ADD_LISTING = 75;
    ArrayList<SliderItems> items;
    boolean isBusiness=false;
    private TextView tvFaq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFragmentManager = getSupportFragmentManager();
        items = new ArrayList<>();
        getSupportActionBar().setTitle("");

        initDrawable(toolbar);
        initUI();
        onclick();
        preloadHomeFragment();
        volleyFetchUserNotification();
        loadUserData();

        if (isBusiness){
            prepareList();
        }else{
            prepareListSingle();
        }


        if (getIntent().getExtras()!=null) {
            boolean isMessage = getIntent().getExtras().getBoolean("isMessage", false);
            if (isBusiness) {
                if (isMessage) {
                    SliderItems item = items.get(11);
                    item.setMessage(true);
                    items.set(11, item);
                }
            } else {
                if (isMessage) {
                    SliderItems item = items.get(10);
                    item.setMessage(true);
                    items.set(10, item);
                }
            }
        }

        sliderAdapter.notifyAdapter(items);

        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(mReciever, new IntentFilter(RippleittAppInstance.broadcastMessage));
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getBaseContext()).unregisterReceiver(mReciever);
    }

    private BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Boolean isMessage = intent.getBooleanExtra("isMessage", false);
            if (isBusiness) {
                if (isMessage) {
                    SliderItems item = items.get(11);
                    item.setMessage(true);
                    items.set(11, item);
                }
            }
            else{
                if (isMessage) {
                    SliderItems item = items.get(10);
                    item.setMessage(true);
                    items.set(10, item);
                }
            }
            sliderAdapter.notifyAdapter(items);
        }
    };

    public void invokeNotificationUpdate() {
        volleyFetchUserNotification();
    }

    public int getTriggerMode() {
        return triggerMode;
    }

    public void setTriggerMode(int triggerMode) {
        this.triggerMode = triggerMode;
    }

    private void initUI() {
        mLstVwSliderOptions = (ListView) findViewById(R.id.lst_menu_items);
        mlytFooterLogout = (LinearLayout) findViewById(R.id.lytFooterLogout);
        mImgVwUserProfilePic = (CircleImageView) findViewById(R.id.imgVwUserProfilePic);
        relGuide = (RelativeLayout) findViewById(R.id.relGuide);
        tvFaq = (TextView) findViewById(R.id.tvFaq);
        mTxtvwUserName = (TextView) findViewById(R.id.txtvwUserName);
        mTxtVwUserEmail = (TextView) findViewById(R.id.txtvwUserEmail);
        imgvwToggleSearchBar = (ImageView) findViewById(R.id.imgvwInitSearch);
        mImgVwAddListing = (ImageView) findViewById(R.id.imgvwAddListing);
        mImgVwAddListing.setOnClickListener(this);
        imgvwToggleSearchBar.setOnClickListener(this);
        mImgVwMapToggle = (ImageView) findViewById(R.id.imgvwtoggleMap);
        mImgVwMapToggle.setOnClickListener(this);

        relGuide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                relGuide.setVisibility(View.GONE);
                return true;
            }
        });

    }

    private void loadUserData() {
        try {
            SharedPreferences sharedPreferences = ActivityHome.this
                    .getSharedPreferences("preferences", Context.MODE_PRIVATE);
            mTxtVwUserEmail.setText(sharedPreferences.getString("email", ""));

            if (sharedPreferences.getString("image", "").equalsIgnoreCase("")
                    ||
                    sharedPreferences.getString("image", "").equalsIgnoreCase("null")
            ) {
                mImgVwUserProfilePic.setImageResource(R.drawable.default_profile_icon);
            } else {
                Picasso.with(ActivityHome.this)
                        .load(RippleittAppInstance
                                .formatPicPath(sharedPreferences.getString("image", "")))
                        .error(R.drawable.default_profile_icon)
                        .placeholder(R.drawable.default_profile_icon)
                        .into(mImgVwUserProfilePic);
            }
            if(PreferenceHandler.readString(ActivityHome.this, PreferenceHandler.USER_TYPE,"1")
                    .equalsIgnoreCase("2")){
                isBusiness=true;
                mTxtvwUserName.setText(PreferenceHandler.readString(ActivityHome.this, PreferenceHandler.BUSINESS_NAME, ""));

//                mTxtvwUserName.setText(sharedPreferences.getString("user_name", ""));
            }else{
                isBusiness=false;

                mTxtvwUserName.setText(sharedPreferences.getString("user_name", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void prepareList() {
//        String[] titles = {"Home",
//                "Dashboard",
//                "My Orders",
//                "My Listings",
//                "My Offers",
//                "My Referrals",
//                "My Disputes",
//                "Recommendations",
//                "Address Book",
//                "Wallet",
//                "My Disbursals",
//                "Wishlist",
//                "Messages",
//                "Notification",
//                "Settings"};
//        Integer[] icons = {R.drawable.dashboard,
//                R.drawable.dashboard,
//                R.drawable.listing,
//                R.drawable.listing,
//                R.drawable.listing,
//                R.drawable.listing,
//                R.drawable.listing,
//                R.drawable.listing,
//
//                R.drawable.dashboard,
//                R.drawable.wallet,
//                R.drawable.wallet,
//                R.drawable.wishlist,
//                R.drawable.messages,
//                R.drawable.notification,
//                R.drawable.settings};
//        /*
//        String[] titles ={"Home","Dashboard", "My Listings","My Bids",
//                "My Referrals","Recomendations for me","My Orders",
//                "Wallet","Address Book","Wishlist","Messages",
//                "Notification","Profile","Settings"};
//        Integer[] icons={R.drawable.dashboard,R.drawable.dashboard,R.drawable.listing,
//                R.drawable.listing, R.drawable.listing, R.drawable.listing, R.drawable.listing,
//                R.drawable.wallet,R.drawable.wallet,R.drawable.dashboard,
//                R.drawable.messages,R.drawable.notification,
//                R.drawable.profile,R.drawable.settings};
//            */
//
//        for (int index = 0; index < titles.length; index++) {
//            SliderItems item = new SliderItems();
//            item.setSliderOptionName(titles[index]);
//            item.setSliderDrawableResource(icons[index]);
//            item.setMessage(false);
//            items.add(item);
//        }

//        sliderAdapter = new SliderListAdapter(ActivityHome.this, items);
//        mLstVwSliderOptions.setAdapter(sliderAdapter);
//
//        mLstVwSliderOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                sliderAdapter.updateSelectionIndex(i);
////                sliderAdapter.notifyDataSetChanged();
//                closeDrawer();
//                switchFragment(i);
//                if (i == 12) {
//                    SliderItems item = items.get(12);
//                    item.setMessage(false);
//                    items.set(12, item);
//                }
//                sliderAdapter.notifyAdapter(items);
//            }
//        });
//
//    }

    private void prepareList() {
        String[] titles = {"Home",
                "Dashboard",
                "My Orders",
                "My Listings",
                "My Offers",
                "My Referrals",
                "My Disputes",
                "My Vouchers",
                "Unlocked Vouchers",
                "Recommendations",
                "Address Book",
                "Wallet",
                "My Disbursals",
                "Wishlist",
                "Messages",
                "Notification",
                "Settings",
                "Invite"
        };
        Integer[] icons = {R.drawable.dashboard,
                R.drawable.dashboard,
                R.drawable.listing,
                R.drawable.listing,
                R.drawable.listing,
                R.drawable.listing,
                R.drawable.listing,
                R.drawable.listing,
                R.drawable.listing,
                R.drawable.listing,
                R.drawable.dashboard,
                R.drawable.wallet,
                R.drawable.wallet,
                R.drawable.wishlist,
                R.drawable.messages,
                R.drawable.notification,
                R.drawable.settings,
                R.drawable.wishlist
        };
        /*
        String[] titles ={"Home","Dashboard", "My Listings","My Bids",
                "My Referrals","Recomendations for me","My Orders",
                "Wallet","Address Book","Wishlist","Messages",
                "Notification","Profile","Settings"};
        Integer[] icons={R.drawable.dashboard,R.drawable.dashboard,R.drawable.listing,
                R.drawable.listing, R.drawable.listing, R.drawable.listing, R.drawable.listing,
                R.drawable.wallet,R.drawable.wallet,R.drawable.dashboard,
                R.drawable.messages,R.drawable.notification,
                R.drawable.profile,R.drawable.settings};
            */

        for (int index = 0; index < titles.length; index++) {
            SliderItems item = new SliderItems();
            item.setSliderOptionName(titles[index]);
            item.setSliderDrawableResource(icons[index]);
            item.setMessage(false);
            items.add(item);
        }

        sliderAdapter = new SliderListAdapter(ActivityHome.this, items);
        mLstVwSliderOptions.setAdapter(sliderAdapter);

        mLstVwSliderOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sliderAdapter.updateSelectionIndex(i);
//                sliderAdapter.notifyDataSetChanged();
                closeDrawer();
                switchFragment(i);
                if (i == 14) {
                    SliderItems item = items.get(14);
                    item.setMessage(false);
                    items.set(14, item);
                }
                sliderAdapter.notifyAdapter(items);
            }
        });

    }
    private void prepareListSingle() {
        String[] titles = {"Home",
                "Dashboard",
                "My Orders",
                "My Listings",
                "My Offers",
                "My Referrals",
                "My Disputes",
                "Unlocked Vouchers",
                "Recommendations",
                "Address Book",
                "Wallet",
                "My Disbursals",
                "Wishlist",
                "Messages",
                "Notification",
                "Settings",
                "Invite"};
        Integer[] icons = {R.drawable.dashboard,
                R.drawable.dashboard,
                R.drawable.listing,
                R.drawable.listing,
                R.drawable.listing,
                R.drawable.listing,
                R.drawable.listing,
                R.drawable.listing,
                R.drawable.listing,
                R.drawable.dashboard,
                R.drawable.wallet,
                R.drawable.wallet,
                R.drawable.wishlist,
                R.drawable.messages,
                R.drawable.notification,
                R.drawable.settings,
                R.drawable.wishlist
        };
        /*
        String[] titles ={"Home","Dashboard", "My Listings","My Bids",
                "My Referrals","Recomendations for me","My Orders",
                "Wallet","Address Book","Wishlist","Messages",
                "Notification","Profile","Settings"};
        Integer[] icons={R.drawable.dashboard,R.drawable.dashboard,R.drawable.listing,
                R.drawable.listing, R.drawable.listing, R.drawable.listing, R.drawable.listing,
                R.drawable.wallet,R.drawable.wallet,R.drawable.dashboard,
                R.drawable.messages,R.drawable.notification,
                R.drawable.profile,R.drawable.settings};
            */

        for (int index = 0; index < titles.length; index++) {
            SliderItems item = new SliderItems();
            item.setSliderOptionName(titles[index]);
            item.setSliderDrawableResource(icons[index]);
            item.setMessage(false);
            items.add(item);
        }

        sliderAdapter = new SliderListAdapter(ActivityHome.this, items);
        mLstVwSliderOptions.setAdapter(sliderAdapter);

        mLstVwSliderOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sliderAdapter.updateSelectionIndex(i);
//                sliderAdapter.notifyDataSetChanged();
                closeDrawer();
                switchFragment(i);
                if (i == 13) {
                    SliderItems item = items.get(13);
                    item.setMessage(false);
                    items.set(13, item);
                }
                sliderAdapter.notifyAdapter(items);
            }
        });

    }

    private void initDrawable(Toolbar toolbar) {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        //CommonUtils.keyboardHide(this,drawer);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onclick() {
        mlytFooterLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHome.this);
                builder.setMessage("Are you sure you want to logout ?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                SharedPreferences sharedPreferences = getSharedPreferences(RippleittAppInstance.PREFERENCES, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                try {
                                    LoginManager.getInstance().logOut();
                                } catch (Exception e) {

                                }
                                Intent intent = new Intent(ActivityHome.this, ActivityLogin.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        tvFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ActivityHome.this, TermsConditionsActivity.class);
                i.putExtra("isFAQ",true);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void preloadHomeFragment() {
        FragmentHome fragmentHome = new FragmentHome();
        fragmentHomeInstance = fragmentHome;
        mFragmentManager.beginTransaction()
                .add(R.id.frmlytFragmentContainer, fragmentHome, TAG_FRAGMENT_HOME)
                .commit();
    }


    // defines which fragment to be loaded in the activity
    // based on the position of the listview containing the slider
    // menu option selected.

    private void switchFragment(int index) {

        if (index != 0) {
            ImageView mimgvwtoggleSearch = (ImageView) findViewById(R.id.imgvwInitSearch);
            mimgvwtoggleSearch.setVisibility(View.INVISIBLE);
        }
        if (index == 0) {
            ImageView mimgvwtoggleSearch = (ImageView) findViewById(R.id.imgvwInitSearch);
            mimgvwtoggleSearch.setVisibility(View.VISIBLE);
        }

        switch (index) {
            case 0: // the home fragment....
                mImgVwAddListing.setVisibility(View.VISIBLE);
                triggerMode = 1;
                FragmentHome fragHome = (FragmentHome) mFragmentManager
                        .findFragmentByTag(TAG_FRAGMENT_HOME);
                if (fragHome != null) {
                    fragmentHomeInstance = fragHome;
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.frmlytFragmentContainer, fragHome)
                            .commit();
                    fragHome.reloadRecentSearch();

                } else {
                    fragHome = new FragmentHome();
                    fragmentHomeInstance = fragHome;
                    mFragmentManager.beginTransaction()
                            .replace(R.id.frmlytFragmentContainer, fragHome, TAG_FRAGMENT_HOME)
                            .commit();
                    RippleittAppInstance.getInstance().setShouldResetHomeFragment(true);
                    RippleittAppInstance
                            .getInstance()
                            .setTriggerSearch(true);
                }
                break;
            case 1:// buyer dashboard
                mImgVwAddListing.setVisibility(View.GONE);
                FragmentBuyerDashboard fragBuyerDashboard = (FragmentBuyerDashboard) mFragmentManager
                        .findFragmentByTag(TAG_FRAGMENT_BUYER_DASHBOARD);
                if (fragBuyerDashboard != null) {
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.frmlytFragmentContainer, fragBuyerDashboard)
                            .commit();
                } else {
                    fragBuyerDashboard = new FragmentBuyerDashboard();
                    mFragmentManager.beginTransaction()
                            .replace(R.id.frmlytFragmentContainer, fragBuyerDashboard, TAG_FRAGMENT_BUYER_DASHBOARD)
                            .commit();
                }
                break;
            case 2:// manage listing
                mImgVwAddListing.setVisibility(View.GONE);
                FragmentMyOrders fragMyOrders = (FragmentMyOrders) mFragmentManager
                        .findFragmentByTag(TAG_FRAGMENT_MY_ORDERS);
                if (fragMyOrders != null) {
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.frmlytFragmentContainer, fragMyOrders)
                            .commit();
                } else {
                    fragMyOrders = new FragmentMyOrders();
                    mFragmentManager.beginTransaction()
                            .replace(R.id.frmlytFragmentContainer,
                                    fragMyOrders, TAG_FRAGMENT_MY_ORDERS)
                            .commit();
                }

                break;

            case 3:// manage listing
                mImgVwAddListing.setVisibility(View.GONE);
                FragmentManageListing fragManageListing = (FragmentManageListing) mFragmentManager
                        .findFragmentByTag(TAG_FRAGMENT_MANAGE_LISTINGS);
                if (fragManageListing != null) {
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.frmlytFragmentContainer, fragManageListing)
                            .commit();
                } else {
                    fragManageListing = new FragmentManageListing();
                    mFragmentManager.beginTransaction()
                            .replace(R.id.frmlytFragmentContainer,
                                    fragManageListing, TAG_FRAGMENT_MANAGE_LISTINGS)
                            .commit();
                }

                break;
            case 4:// manage listing
                mImgVwAddListing.setVisibility(View.GONE);
                FragmentMyBids fragmentMyBids = (FragmentMyBids) mFragmentManager
                        .findFragmentByTag(TAG_FRAGMENT_MY_BIDS);
                if (fragmentMyBids != null) {
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.frmlytFragmentContainer, fragmentMyBids)
                            .commit();
                } else {
                    fragmentMyBids = new FragmentMyBids();
                    mFragmentManager.beginTransaction()
                            .replace(R.id.frmlytFragmentContainer,
                                    fragmentMyBids, TAG_FRAGMENT_MY_BIDS)
                            .commit();
                }

                break;
            case 5:// manage listing
                mImgVwAddListing.setVisibility(View.GONE);
                FragmentMyReferrals fragMyReferals = (FragmentMyReferrals) mFragmentManager
                        .findFragmentByTag(TAG_FRAGMENT_MY_REFERRALS);
                if (fragMyReferals != null) {
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.frmlytFragmentContainer, fragMyReferals)
                            .commit();
                } else {
                    fragMyReferals = new FragmentMyReferrals();
                    mFragmentManager.beginTransaction()
                            .replace(R.id.frmlytFragmentContainer,
                                    fragMyReferals, TAG_FRAGMENT_MY_REFERRALS)
                            .commit();
                }

                break;
            case 6:// manage listing
                mImgVwAddListing.setVisibility(View.GONE);
                FragmentMyDisputes fragMyDisputes = (FragmentMyDisputes) mFragmentManager
                        .findFragmentByTag(TAG_FRAGMENT_MY_DISPUTES);
                if (fragMyDisputes != null) {
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.frmlytFragmentContainer, fragMyDisputes)
                            .commit();
                } else {
                    fragMyDisputes = new FragmentMyDisputes();
                    mFragmentManager.beginTransaction()
                            .replace(R.id.frmlytFragmentContainer,
                                    fragMyDisputes, TAG_FRAGMENT_MY_DISPUTES)
                            .commit();
                }

                break;

            case 7:// wallet
//                mImgVwAddListing.setVisibility(View.VISIBLE);
                if (isBusiness) {
                    mImgVwAddListing.setVisibility(View.GONE);
                    triggerMode = 3;
                    FragmentMyVouchers fragmentMyVouchers = (FragmentMyVouchers) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_VOUCHERS);
                    if (fragmentMyVouchers != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragmentMyVouchers)
                                .commit();
                    } else {
                        fragmentMyVouchers = new FragmentMyVouchers();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragmentMyVouchers, TAG_FRAGMENT_VOUCHERS)
                                .commit();
                    }
                }else{
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentUnlockedVouchers fragUnlockedVouchers = (FragmentUnlockedVouchers) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_UNLOCKED_VOUCHERS);
                    if (fragUnlockedVouchers != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragUnlockedVouchers)
                                .commit();
                    } else {
                        fragUnlockedVouchers = new FragmentUnlockedVouchers();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragUnlockedVouchers, TAG_FRAGMENT_UNLOCKED_VOUCHERS)
                                .commit();
                    }
                }
                break;
            case 8:// Unlocked Vouchers
                if (isBusiness) {
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentUnlockedVouchers fragUnlockedVouchers = (FragmentUnlockedVouchers) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_UNLOCKED_VOUCHERS);
                    if (fragUnlockedVouchers != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragUnlockedVouchers)
                                .commit();
                    } else {
                        fragUnlockedVouchers = new FragmentUnlockedVouchers();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragUnlockedVouchers, TAG_FRAGMENT_UNLOCKED_VOUCHERS)
                                .commit();
                    }
                }else{
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentRecomendationsForMe fragRecomendationsFrMe = (FragmentRecomendationsForMe) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_RECOMENDATIONS_FOR_ME);
                    if (fragRecomendationsFrMe != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragRecomendationsFrMe)
                                .commit();
                    } else {
                        fragRecomendationsFrMe = new FragmentRecomendationsForMe();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragRecomendationsFrMe, TAG_FRAGMENT_RECOMENDATIONS_FOR_ME)
                                .commit();
                    }

                }
                break;
            case 9:// wallet
                if (isBusiness) {
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentRecomendationsForMe fragRecomendationsFrMe = (FragmentRecomendationsForMe) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_RECOMENDATIONS_FOR_ME);
                    if (fragRecomendationsFrMe != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragRecomendationsFrMe)
                                .commit();
                    } else {
                        fragRecomendationsFrMe = new FragmentRecomendationsForMe();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragRecomendationsFrMe, TAG_FRAGMENT_RECOMENDATIONS_FOR_ME)
                                .commit();
                    }
                }else{
                    mImgVwAddListing.setVisibility(View.GONE);
                    triggerMode = 2;
                    fragmentAddressBook = (FragmentAddressBook) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_ADDRESS_BOOK);
                    if (fragmentAddressBook != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragmentAddressBook)
                                .commit();
                    } else {
                        fragmentAddressBook = new FragmentAddressBook();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragmentAddressBook, TAG_FRAGMENT_ADDRESS_BOOK)
                                .commit();
                    }
                }
                break;
            case 10:// address book
                if (isBusiness) {
                    mImgVwAddListing.setVisibility(View.GONE);
                    triggerMode = 2;
                    fragmentAddressBook = (FragmentAddressBook) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_ADDRESS_BOOK);
                    if (fragmentAddressBook != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragmentAddressBook)
                                .commit();
                    } else {
                        fragmentAddressBook = new FragmentAddressBook();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragmentAddressBook, TAG_FRAGMENT_ADDRESS_BOOK)
                                .commit();
                    }
                }else{
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentWallet fragWallet = (FragmentWallet) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_WALLET);
                    if (fragWallet != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragWallet)
                                .commit();
                    } else {
                        fragWallet = new FragmentWallet();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragWallet, TAG_FRAGMENT_WALLET)
                                .commit();
                    }

                }
                break;
            case 11:// Wallet
                if (isBusiness) {
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentWallet fragWallet = (FragmentWallet) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_WALLET);
                    if (fragWallet != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragWallet)
                                .commit();
                    } else {
                        fragWallet = new FragmentWallet();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragWallet, TAG_FRAGMENT_WALLET)
                                .commit();
                    }
                }else{
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentMyDisbursals fragDisbursals = (FragmentMyDisbursals) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_MY_DISBURSALS);
                    if (fragDisbursals != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragDisbursals)
                                .commit();
                    } else {
                        fragDisbursals = new FragmentMyDisbursals();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragDisbursals, TAG_FRAGMENT_MY_DISBURSALS)
                                .commit();
                    }
                }
                break;
            case 12:// Wallet
                if (isBusiness) {
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentMyDisbursals fragDisbursals = (FragmentMyDisbursals) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_MY_DISBURSALS);
                    if (fragDisbursals != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragDisbursals)
                                .commit();
                    } else {
                        fragDisbursals = new FragmentMyDisbursals();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragDisbursals, TAG_FRAGMENT_MY_DISBURSALS)
                                .commit();
                    }
                }else{
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentWishlist fragWishlist = (FragmentWishlist) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_WISHLIST);
                    if (fragWishlist != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragWishlist)
                                .commit();
                    } else {
                        fragWishlist = new FragmentWishlist();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragWishlist, TAG_FRAGMENT_WISHLIST)
                                .commit();
                    }

                }
                break;
            case 13:// messages
                if (isBusiness) {
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentWishlist fragWishlist = (FragmentWishlist) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_WISHLIST);
                    if (fragWishlist != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragWishlist)
                                .commit();
                    } else {
                        fragWishlist = new FragmentWishlist();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragWishlist, TAG_FRAGMENT_WISHLIST)
                                .commit();
                    }

                }else{
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentMessages fragMessages = (FragmentMessages) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_MESSAGES);
                    if (fragMessages != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragMessages)
                                .commit();
                    } else {
                        fragMessages = new FragmentMessages();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragMessages, TAG_FRAGMENT_MESSAGES)
                                .commit();
                    }
                }
                break;
            case 14:// notification
                if (isBusiness) {
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentMessages fragMessages = (FragmentMessages) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_MESSAGES);
                    if (fragMessages != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragMessages)
                                .commit();
                    } else {
                        fragMessages = new FragmentMessages();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragMessages, TAG_FRAGMENT_MESSAGES)
                                .commit();
                    }
                }else{
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentNotification fragNotification_ = (FragmentNotification) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_NOTIFICATIONS);
                    if (fragNotification_ != null) {
                        fragNotification = fragNotification_;
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragNotification_)
                                .commit();
                    } else {
                        fragNotification = new FragmentNotification();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragNotification, TAG_FRAGMENT_NOTIFICATIONS)
                                .commit();
                    }
                }


                break;
            case 15:// profile
                if (isBusiness) {
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentNotification fragNotification_ = (FragmentNotification) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_NOTIFICATIONS);
                    if (fragNotification_ != null) {
                        fragNotification = fragNotification_;
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragNotification_)
                                .commit();
                    } else {
                        fragNotification = new FragmentNotification();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragNotification, TAG_FRAGMENT_NOTIFICATIONS)
                                .commit();
                    }
                }else{
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentSettings fragmentSettings = (FragmentSettings) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_SETTINGS);
                    if (fragmentSettings != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragmentSettings)
                                .commit();
                    } else {
                        fragmentSettings = new FragmentSettings();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragmentSettings, TAG_FRAGMENT_SETTINGS)
                                .commit();
                    }
                }

                /*

                    FragmentHome fragHome = (FragmentHome) mFragmentManager
                        .findFragmentByTag(TAG_FRAGMENT_HOME);
                if(fragHome!=null){
                    fragmentHomeInstance=fragHome;
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.frmlytFragmentContainer,fragHome)
                            .commit();
                    fragHome.initSearch();
                }else{
                    fragHome = new FragmentHome();
                    fragmentHomeInstance=fragHome;
                    mFragmentManager.beginTransaction()
                            .replace(R.id.frmlytFragmentContainer, fragHome, TAG_FRAGMENT_HOME)
                            .commit();
                }

                 */


                break;
            case 16:// settings
                if (isBusiness) {
                    mImgVwAddListing.setVisibility(View.GONE);
                    FragmentSettings fragmentSettings = (FragmentSettings) mFragmentManager
                            .findFragmentByTag(TAG_FRAGMENT_SETTINGS);
                    if (fragmentSettings != null) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.frmlytFragmentContainer, fragmentSettings)
                                .commit();
                    } else {
                        fragmentSettings = new FragmentSettings();
                        mFragmentManager.beginTransaction()
                                .replace(R.id.frmlytFragmentContainer,
                                        fragmentSettings, TAG_FRAGMENT_SETTINGS)
                                .commit();
                    }
                }else{
                    mImgVwAddListing.setVisibility(View.GONE);
                    startActivity(new Intent(ActivityHome.this, ReferFriendActivity.class));
                }
                break;
            case 17:// settings
                if (isBusiness) {
                    mImgVwAddListing.setVisibility(View.GONE);
                    startActivity(new Intent(ActivityHome.this, ReferFriendActivity.class));
                }else{

                }
                break;
        }
    }

    @Override
    public void onClick(View view) {

        if (view == mImgVwAddListing) {
            RippleittAppInstance.getInstance().setCURRENT_LISTING_OBJECT(null);
            Intent intent = new Intent(ActivityHome.this, ActivityAddListingStep1.class);
            startActivityForResult(intent, START_ACTION_ADD_LISTING);
        }
        if (view == mlytFooterLogout) {
            SharedPreferences preferences = getSharedPreferences("preferences",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(ActivityHome.this, ActivityLogin.class));
            finish();
        }
        if (view == imgvwToggleSearchBar) {
            if (fragmentHomeInstance != null)
                fragmentHomeInstance.toggleSearchBoxVisibility();
        }
        if (view == mImgVwMapToggle) {
            if (isBusiness) {
                mImgVwMapToggle.setImageResource(R.drawable.ic_notifications_black_24dp);
                switchFragment(15); // load the notificaton fragment...
                sliderAdapter.updateSelectionIndex(15);
                sliderAdapter.notifyDataSetChanged();
                //
            }else{
                mImgVwMapToggle.setImageResource(R.drawable.ic_notifications_black_24dp);
                switchFragment(14); // load the notificaton fragment...
                sliderAdapter.updateSelectionIndex(14);
                sliderAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 89) {
            // this is multi contact selection callback...
            if (resultCode == Activity.RESULT_OK) {
                if (fragmentAddressBook != null) {
                    List<ContactResult> results = MultiContactPicker.obtainResult(data);
                    fragmentAddressBook.readContacts(results);
                }
            } else {
                Toast.makeText(ActivityHome.this, "No contacts were imported", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == START_ACTION_ADD_LISTING) {
            // launch the my listings fragment...
            if (resultCode == RESULT_CANCELED) {
                mImgVwAddListing.setVisibility(View.VISIBLE);
            } else {
                sliderAdapter.updateSelectionIndex(2);
                sliderAdapter.notifyDataSetChanged();
                closeDrawer();
                switchFragment(3);
            }

        }

    }


    public void loadMyOrders() {
        switchFragment(2);
        sliderAdapter.updateSelectionIndex(2);
        sliderAdapter.notifyDataSetChanged();
    }

    public void loadMyDisputes() {
        switchFragment(6);
        sliderAdapter.updateSelectionIndex(6);
        sliderAdapter.notifyDataSetChanged();
    }


    public void loadMyUnlockedVouchers() {
        if (isBusiness) {
            switchFragment(8);
            sliderAdapter.updateSelectionIndex(8);
            sliderAdapter.notifyDataSetChanged();
        }else{
            switchFragment(7);
            sliderAdapter.updateSelectionIndex(7);
            sliderAdapter.notifyDataSetChanged();
        }
    }


    public void loadMyDisbursals() {
        if (isBusiness) {
            switchFragment(12);
            sliderAdapter.updateSelectionIndex(12);
            sliderAdapter.notifyDataSetChanged();
        }else{

            switchFragment(11);
            sliderAdapter.updateSelectionIndex(11);
            sliderAdapter.notifyDataSetChanged();
        }
    }

    public void loadMyListings() {
        switchFragment(3);
        sliderAdapter.updateSelectionIndex(3);
        sliderAdapter.notifyDataSetChanged();

    }


    // FETCH USER NOTIFICATIONS...

    private void volleyFetchUserNotification() {

        StringRequest sr = new StringRequest(Request.Method.POST,
                RippleittAppInstance.FETCH_USER_NOTIFICATION,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            NotificationsResponseTemplate notifications = new NotificationsResponseTemplate();
                            notifications = (NotificationsResponseTemplate) new Gson().fromJson(response, NotificationsResponseTemplate.class);
                            if (notifications.getFlag().equalsIgnoreCase("1")) {
                                mImgVwMapToggle.setImageResource(R.drawable.ic_notifications_active_black_24dp);
                            } else {
                                mImgVwMapToggle.setImageResource(R.drawable.ic_notifications_black_24dp);
                            }
                        } catch (Exception e) {

                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityHome.this,
                        "could not fetch your 1 book, please try again", Toast.LENGTH_LONG).show();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("", "" + error.getMessage() + "," + error.toString());
                com.rippleitt.utils.CommonUtils.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String token = PreferenceHandler.readString(ActivityHome.this, PreferenceHandler.AUTH_TOKEN, "");
                params.put("token", token);
                return params;
            }
        };
        sr.setShouldCache(false);
        RippleittAppInstance.getInstance().addToRequestQueue(sr, "rippleitt");
    }

    public void showBellIcon() {
        mImgVwMapToggle.setVisibility(View.VISIBLE);
        volleyFetchUserNotification();
    }

    @Override
    public void onMessageReceive() {
        if (isBusiness) {
            SliderItems item = items.get(14);
            item.setMessage(true);
            items.set(14, item);
            sliderAdapter.notifyAdapter(items);
        }else{
            SliderItems item = items.get(13);
            item.setMessage(true);
            items.set(13, item);
            sliderAdapter.notifyAdapter(items);
        }
    }
}