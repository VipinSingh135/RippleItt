package com.rippleitt.controller;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mobiprobe.Mobiprobe;
import com.rippleitt.activities.ActivityHome;
import com.rippleitt.activities.ActivityVerifyEmail;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.modals.CategoryTemplate;
import com.rippleitt.modals.ContactSharingTemplate;
import com.rippleitt.modals.ListingOwnerTemplate;
import com.rippleitt.modals.ListingSyncTemplate;
import com.rippleitt.modals.ListingTemplate;
import com.rippleitt.modals.NotificationTemplate;
import com.rippleitt.modals.PriceRangeTemplate;
import com.rippleitt.modals.SelfListingTemplate;
import com.rippleitt.modals.UserProfileTemplate;
import com.rippleitt.modals.VoucherTemplate;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by manishautomatic on 05/03/18.
 */

public class RippleittAppInstance extends Application {

    ///==============================url===================================

    /*
    public  static String BASE_URL="http://mobiprobe.com/rippleitt/testapi_temp/";
    public  static String IMAGE_BASE_URL="http://mobiprobe.com/rippleitt/listing_pics/";
    public  static String IMAGE_PATH_PREFIX="http://mobiprobe.com/rippleitt";
    */

    //    public  static String BASE_URL="http://rippleitt.com.au/api/";
//    public static String BASE_URL = "https://rippleitt.com.au/api_temp/";
    public static String BASE_URL = "https://rippleitt.com.au/api_dev/";
    public static String FCM_URL = "https://fcm.googleapis.com/fcm/";
    public static String IMAGE_BASE_URL = "https://rippleitt.com.au/listing_pics/";
    public static String IMAGE_PATH_PREFIX = "https://rippleitt.com.au";
    //public  static String IMAGE_PATH_PREFIX="http://rippleitt.co m.au/rippleitt";


    /*
    public  static String BASE_URL="http://acuratechglobal.com/rippleitt/api/";
    public  static String IMAGE_BASE_URL="http://acuratechglobal.com/rippleitt/listing_pics/";
    public  static String IMAGE_PATH_PREFIX="http://acuratechglobal.com/rippleitt";
    */

    public static String FCM_SERVER_KEY = "key= AAAAZFQbHxk:APA91bH647fsHDThaueZ64UYg0riFMaNcgQveuf7tf5zXtneKGD0HDgitecfqJR5e3v1JE51kT4nm8bngW68ZkJdLnR80_qgZmOQbXwuqe92Q9VQzlYBBabFlHpxd68DI-Inl56G_Jn-";


    public static String PREFERENCES = "preferences";
    public static final String broadcastMessage = "broadCastMessage";
    public static String currentActivity = null;
    public static String TOKEN_ID = "";
    public static String PRODUCT_ID = "";
    public static String WISHLIST_STATUS = "";
    public static String STATUS_FILTER = "";
    public static String FILTER_CATEGORY_ID = "";
    public static String FILTER_SUBCAT_ID = "";
    public static String SIGNUP = "signup.php";
    public static String LOGIN = "login.php";
    public static String SEND_PUSH = "send";
    public static String ADD_NEW_PRODUCT = "addlisting.php";
    public static String FETCHING_PRODUCT_DETAILS = "listingdetails.php";
    public static String FETCH_LISTING_DETAILS = BASE_URL + "fetchlisting.php";
    public static String LISTING_DETAILS = BASE_URL + "listingdetails.php";
    public static String REMOVE_CONTACT = BASE_URL + "removecontact.php";
    public static String CRITERIA_SEARCH = "searchresult.php";
    public static String FETCH_CATEGORY_DETAILS = "fetchcategory.php";
    public static String FETCH_subCATEGORY_DETAILS = "fetchsubcategory.php";
    public static String ADD_WISHLIST_DETAILS = "addtowishlist.php";
    public static String REMOVE_WISHLIST_DETAILS = "removefromwishlist.php";
    public static String FETCH_WISHLIST_DETAILS = "fetchuserwishlist.php";
    public static String FETCH_ADDRESSBOOK_LIST = "fetchaddressbook.php";
    public static String FETCH_MY_BIDS = "fetchmybids.php";
    public static String MAKE_OFFER = "makeoffer.php";
    public static String ADD_VOUCHER = "add_voucher.php";
    public static String EDIT_VOUCHER = "edit_voucher.php";
    public static String FETCH_ACCOUNT_DETAILS = BASE_URL + "getbankinfo.php";
    public static String FETCH_ACCOUNT_DETAILS_SUBMIT = BASE_URL + "updatebankinfo.php";
    public static String POST_DISBURSE = BASE_URL + "submitdisbursalrequest.php";
    public static String BUY_NOW =  "buynow.php";
    public static String ACCEPT_OFFER = "acceptlistingoffer.php";
    public static String ADD_CONTACT = "addcontact.php";
    public static String FEATCH_USER_LISTING = "fetchuserlisting.php";
    public static String FETCH_VOUCHER_LISTING = "get_vouchers.php";
    public static String SEARCH_CONTACTS_LISTING = "searchuser.php";
    public static String DELETE_PRODUCT_LISTING = "deletelisting.php";
    public static String DELETE_VOUCHER = "delete_voucher.php";
    public static String EDIT_PRODUCT_LISTING = "editlisting.php";
    private final String FETCH_CATEGORIES = "fetchcategory.php";
    private final String FETCH_SUBCATEGORIES = "fetchsubcategory.php";
    private final String SUBMIT_LISTING = "addlisting.php";
    private final String DELETE_PHOTO = "deletephoto.php";
    public static String ADD_VOUCHER_LISTING = "add_voucher_listing.php";
    public static String EDIT_VOUCHER_LISTING = "edit_voucher_listing.php";
    public static String REMOVE_VOUCHER_LISTING = "remove_voucher_listing.php";
    public static final String FETCH_REFERAALS = BASE_URL + "fetchuserrefferal.php";
    public static final String FETCH_MY_RECOMENDATIONS = BASE_URL + "recommendedtouser.php";
    private final String EDIT_LISTING = BASE_URL + "editlisting.php";
    public static final String FORGOT_PASSWORD = BASE_URL + "forgot_password.php";
    public final static String ACCEPT_BID = BASE_URL + "acceptlistingoffer.php";
    private final String EDIT_PROFILE = BASE_URL + "editprofile.php";
    private final String CHANGE_PASSWORD = BASE_URL + "updatepassword.php";
    public static final String ADD_EXTERNAL_CONTACT = BASE_URL + "addnonrippleittuser.php";
    public static final String DELETE_EXTERNAL_CONTACT = BASE_URL + "removeexternalcontact.php";
    public static final String OTP_VALIDATION = BASE_URL + "forgotpass_otp_validation.php";
    public static final String FETCH_USER_ORDERS = BASE_URL + "fetchuserorders.php";
    public static final String FETCH_USER_DISPUTES = BASE_URL + "fetchuserdisputes.php";
    public static final String UPDATE_LISTING_PRIMARY_IMAGE = BASE_URL + "updateprimaryimage.php";
    public static final String SUBMIT_REVIEW = BASE_URL + "submitreview.php";
    public static final String UPDATE_FIREBASE_TOKEN = BASE_URL + "refreshfcmtoken.php";
    public static final String GET_USER_DETAILS = BASE_URL + "getuserdetail.php";
    public static final String PERFORM_FB_LOGIN = BASE_URL + "registerviafb.php";
    public static final String GET_CARD_TOKEN = BASE_URL + "getcardtoken.php";
    public static final String UPDATE_CARD_ID = BASE_URL + "updatecardid.php";
    public static final String FETCH_USER_NOTIFICATION = BASE_URL + "fetchusernotification.php";
    public static final String MARK_AS_READ = BASE_URL + "readnotification.php";
    public static final String FETCH_USER_DASHBOARD = BASE_URL + "dashboard.php";
    public static final String FETCH_USER_WALLET = BASE_URL + "fetchuserwallet.php";
    public static final String FETCH_MY_DISBURSALS = BASE_URL + "fetchdisbursals.php";
    public static final String VERIFY_USER_CREDENTIALS = BASE_URL + "authenticateuser.php";
    //markordercomplete.php
    public static final String MARK_ORDER_COMPLATE = BASE_URL + "markordercomplete.php";
    public static final String MAKE_ORDER_PAYMENT = BASE_URL + "makeorderpayment.php";
    public static final String RELEASE_ORDER_PAYMENT = BASE_URL + "releaseorderpayment.php";
    public static final String REQUEST_ORDER_PAYMENT = BASE_URL + "requestorderpayment.php";
    public static final String RESEND_OTP = BASE_URL + "resendverificationotp.php";
    public static final String UPDATE_MOBILE_NUMBER = BASE_URL + "updatemobilenumber.php";
    public static final String FETCH_LISTING_FAQ = BASE_URL + "fetchlistingfaq.php";
    //ActivityPostListingFaq
    public static final String POST_LISTING_FAQ = BASE_URL + "postlistingfaq.php";
    public static final String POST_FAQ_RESPONSE = BASE_URL + "postfaqresponse.php";
    public static final String FETCH_PRICE_RANGES = BASE_URL + "fetchpriceranges.php";
    public static final String SHARE_LISTING = BASE_URL + "sharelisting.php";

 //ActivityPostListingFaq
    public static final String POST_ORDER_DISPUTE = BASE_URL + "filedispute.php";
    public static final String POST_DISPUTE_COMMENT = BASE_URL + "submitdisputemessage.php";
    public static final String FETCH_LISTING_DISPUTE = BASE_URL + "fetchdisputemessages.php";;

    public static String FETCH_UNLOCKED_VOUCHERS = "get_unlocked_vouchers.php";


    public static final String REFER_LISTING = BASE_URL + "refer.php";
    private boolean triggerSearch = true;
    private String emailToVerify = "";
    private boolean shouldResetHomeFragment = false;
    private ListingTemplate[] CURRENT_USER_LISTINGS;
    private NotificationTemplate[] USER_NOTIFICATIONS;
    private String CURRENT_SELECTED_LISTING_ID = "";
    private int countCurrentPage = -1;
    private int productCount = 0, serviceCount = 0;
    private int countMaxPages = 0;
    private int countTotalSearchResult = 0;
    private int addCardMode = 0; // 0 -> while login, 1 ---> while making offer, publishing listing.
    private boolean triggerCriteriaSearch = false;
    private ListingTemplate SELECTED_LISTING_DETAIL_OBJECT = new ListingTemplate();
    private UserProfileTemplate userTemplate = new UserProfileTemplate();
    private SelfListingTemplate MY_CURRENT_LISTING;
    private ImageLoader mImageLoader;
    private final int MY_SOCKET_TIMEOUT_MS = 30000;
    private ListingOwnerTemplate currentChatPartner;
    private static RippleittAppInstance instance;
    private LinkedHashMap<String, String> categories = new LinkedHashMap<>();
    private LinkedHashMap<String, String> subCategories = new LinkedHashMap<>();
    private LinkedHashMap<String, String> priceRanges = new LinkedHashMap<>();
    private HashMap<String, String> listingImages = new HashMap<>();
    ;
    private int CURRENT_SELECTED_IMAGE_POSITION = 0;
    private ListingTemplate CURRENT_LISTING_OBJECT;
    private ListingSyncTemplate CURRENT_ADDED_LISTING_OBJECT;
    private VoucherTemplate CURRENT_VOUCHER_OBJECT;
    private LinkedHashMap<String, String> currentListingExistingPics = new LinkedHashMap<String, String>();
    private ArrayList<ContactSharingTemplate> userCurrentAddressBook = new ArrayList<>();
    private LinkedHashMap<String, CategoryTemplate> currentLoadedCategories = new LinkedHashMap<>();
    private LinkedHashMap<String, CategoryTemplate> currentLoadedSubCategories = new LinkedHashMap<>();
    private LinkedHashMap<String, PriceRangeTemplate> currentLoadedPriceRanges = new LinkedHashMap<>();
    private LinkedHashMap<String, String> searchCategories = new LinkedHashMap<>();
    private LinkedHashMap<String, String> searchSubCategories = new LinkedHashMap<>();
    private LinkedHashMap<String, String> searchPriceRanges = new LinkedHashMap<>();
    private LinkedHashMap<String, String> searchCategoryPayload = new LinkedHashMap<>();
    private LinkedHashMap<String, String> searchSubCategoryPayload = new LinkedHashMap<>();
    private LinkedHashMap<String, String> searchPriceRangePayload = new LinkedHashMap<>();
    private LinkedHashMap<String, String> serviceZIPCodes = new LinkedHashMap<>();
    private LinkedHashMap<String, String> serviceZIPCodesExisting = new LinkedHashMap<>();
    private LinkedHashMap<String, String> serviceZIPCodesNew = new LinkedHashMap<>();
    private LinkedHashMap<String, String> serviceZIPCodesDeleted = new LinkedHashMap<>();
    public static JSONObject userObject;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private ArrayList<ListingTemplate> currentFilteredProducts = new ArrayList<>();
    private ArrayList<ListingTemplate> currentFilteredServices = new ArrayList<>();
    private ArrayList<ListingTemplate> currentSearchResults = new ArrayList<>();
    private String mobileNUmberToVerify = "";
    private String accountEmail = "";
    private int fullScreenImageMode = 1;
    private int currentTappedImageindex = -1;
    private String emailVerified = "0", mobileVerified = "0";
    private int verificationMode = 0; // 1== email, business==mobile
    private int listingFaqMode = 0; // 0 - public, 1=self
//    private int disputeRiseMode = 0; // 0= message, 1= rise dispute
    private String disputeID = "", disputeTitle="", disputeStatus="";
    private String currentQuestionId = "";
    private boolean disputeMode = false;
    private boolean isProductFilter = true;


    public static String getPostFaqResponse() {
        return POST_FAQ_RESPONSE;
    }


    public LinkedHashMap<String, String> getSearchPriceRanges() {
        return searchPriceRanges;
    }

    public void setSearchPriceRanges(LinkedHashMap<String, String> searchPriceRanges) {
        this.searchPriceRanges = searchPriceRanges;
    }

    public LinkedHashMap<String, String> getSearchPriceRangePayload() {
        return searchPriceRangePayload;
    }

    public void setSearchPriceRangePayload(LinkedHashMap<String, String> searchPriceRangePayload) {
        this.searchPriceRangePayload = searchPriceRangePayload;
    }


    public static String getPostOrderDispute() {
        return POST_ORDER_DISPUTE;
    }

    public static String getPostDisputeComment() {
        return POST_DISPUTE_COMMENT;
    }

    public boolean isShouldResetHomeFragment() {
        return shouldResetHomeFragment;
    }

    public void setShouldResetHomeFragment(boolean shouldResetHomeFragment) {
        this.shouldResetHomeFragment = shouldResetHomeFragment;
    }

    public static String getFetchPriceRanges() {
        return FETCH_PRICE_RANGES;
    }


    public static String getShareListing() {
        return SHARE_LISTING;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //e00266e1
        Mobiprobe.activate(this, "e00266e1");
        instance = this;
        mContext = getApplicationContext();
        FirebaseMessaging.getInstance().subscribeToTopic("admin");

        initVolley();
    }


    public String getCurrentQuestionId() {
        return currentQuestionId;
    }

    public void setCurrentQuestionId(String currentQuestionId) {
        this.currentQuestionId = currentQuestionId;
    }

    public int getListingFaqMode() {
        return listingFaqMode;
    }

    public void setListingFaqMode(int disputeRiseMode) {
        this.listingFaqMode = listingFaqMode;
    }

    public boolean isDisputeMode() {
        return disputeMode;
    }

    public void setDisputeMode(boolean disputeMode) {
        this.disputeMode = disputeMode;
    }

    public String getDisputeID() {
        return disputeID;
    }

    public void setDisputeID(String disputeRiseMode) {
        this.disputeID = disputeRiseMode;
    }

    public String getDisputeTitle() {
        return disputeTitle;
    }

    public void setDisputeTitle(String disputeTitle) {
        this.disputeTitle = disputeTitle;
    }

    public String getDisputeStatus() {
        return disputeStatus;
    }

    public void setDisputeStatus(String disputeStatus) {
        this.disputeStatus = disputeStatus;
    }

    public static String getPostListingFaq() {
        return POST_LISTING_FAQ;
    }

    public LinkedHashMap<String, String> getServiceZIPCodesExisting() {
        return serviceZIPCodesExisting;
    }

    public void setServiceZIPCodesExisting(LinkedHashMap<String, String> serviceZIPCodesExisting) {
        this.serviceZIPCodesExisting = serviceZIPCodesExisting;
    }

    public LinkedHashMap<String, String> getServiceZIPCodesNew() {
        return serviceZIPCodesNew;
    }

    public void setServiceZIPCodesNew(LinkedHashMap<String, String> serviceZIPCodesNew) {
        this.serviceZIPCodesNew = serviceZIPCodesNew;
    }

    public LinkedHashMap<String, String> getServiceZIPCodesDeleted() {
        return serviceZIPCodesDeleted;
    }

    public void setServiceZIPCodesDeleted(LinkedHashMap<String, String> serviceZIPCodesDeleted) {
        this.serviceZIPCodesDeleted = serviceZIPCodesDeleted;
    }


    public LinkedHashMap<String, PriceRangeTemplate> getCurrentLoadedPriceRanges() {
        return currentLoadedPriceRanges;
    }

    public void setCurrentLoadedPriceRanges(LinkedHashMap<String, PriceRangeTemplate> currentLoadedPriceRanges) {
        this.currentLoadedPriceRanges = currentLoadedPriceRanges;
    }

    public LinkedHashMap<String, String> getServiceZIPCodes() {
        return serviceZIPCodes;
    }

    public void setServiceZIPCodes(LinkedHashMap<String, String> serviceZIPCodes) {
        this.serviceZIPCodes = serviceZIPCodes;
    }

    public static String getResendOtp() {
        return RESEND_OTP;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(String mobileVerified) {
        this.mobileVerified = mobileVerified;
    }


    public String getMobileNUmberToVerify() {
        return mobileNUmberToVerify;
    }

    public void setMobileNUmberToVerify(String mobileNUmberToVerify) {
        this.mobileNUmberToVerify = mobileNUmberToVerify;
    }

    public static String getUpdateMobileNumber() {
        return UPDATE_MOBILE_NUMBER;
    }

    public static String getVerifyUserCredentials() {
        return VERIFY_USER_CREDENTIALS;
    }

    public String getEmailToVerify() {
        return emailToVerify;
    }

    public void setEmailToVerify(String emailToVerify) {
        this.emailToVerify = emailToVerify;
    }

    public int getAddCardMode() {
        return addCardMode;
    }

    public void setAddCardMode(int addCardMode) {
        this.addCardMode = addCardMode;
    }

    public NotificationTemplate[] getUSER_NOTIFICATIONS() {
        return USER_NOTIFICATIONS;
    }

    public void setUSER_NOTIFICATIONS(NotificationTemplate[] USER_NOTIFICATIONS) {
        this.USER_NOTIFICATIONS = USER_NOTIFICATIONS;
    }

    public int getCurrentTappedImageindex() {
        return currentTappedImageindex;
    }

    public void setCurrentTappedImageindex(int currentTappedImageindex) {
        this.currentTappedImageindex = currentTappedImageindex;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public int getFullScreenImageMode() {
        return fullScreenImageMode;
    }

    public void setFullScreenImageMode(int fullScreenImageMode) {
        this.fullScreenImageMode = fullScreenImageMode;
    }

    public ListingOwnerTemplate getCurrentChatPartner() {
        return currentChatPartner;
    }

    public void setCurrentChatPartner(ListingOwnerTemplate currentChatPartner) {
        this.currentChatPartner = currentChatPartner;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public ArrayList<ListingTemplate> getCurrentFilteredProducts() {
        return currentFilteredProducts;
    }

    public void setCurrentFilteredProducts(ArrayList<ListingTemplate> currentFilteredProducts) {
        this.currentFilteredProducts = currentFilteredProducts;
    }

    public ArrayList<ListingTemplate> getCurrentFilteredServices() {
        return currentFilteredServices;
    }

    public void setCurrentFilteredServices(ArrayList<ListingTemplate> currentFilteredServices) {
        this.currentFilteredServices = currentFilteredServices;
    }


    public int getVerificationMode() {
        return verificationMode;
    }

    public void setVerificationMode(int verificationMode) {
        this.verificationMode = verificationMode;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(int serviceCount) {
        this.serviceCount = serviceCount;
    }

    public boolean isTriggerSearch() {
        return triggerSearch;
    }

    public void setTriggerSearch(boolean triggerSearch) {
        this.triggerSearch = triggerSearch;
    }

    public static String getSIGNUP() {
        return BASE_URL + SIGNUP;
    }

    public int getCountTotalSearchResult() {
        return countTotalSearchResult;
    }

    public void setCountTotalSearchResult(int countTotalSearchResult) {
        this.countTotalSearchResult = countTotalSearchResult;
    }

    public int getCountCurrentPage() {
        return countCurrentPage;
    }

    public void setCountCurrentPage(int countCurrentPage) {
        this.countCurrentPage = countCurrentPage;
    }

    public ListingTemplate getSELECTED_LISTING_DETAIL_OBJECT() {
        return SELECTED_LISTING_DETAIL_OBJECT;
    }

    public void setSELECTED_LISTING_DETAIL_OBJECT(ListingTemplate SELECTED_LISTING_DETAIL_OBJECT) {
        this.SELECTED_LISTING_DETAIL_OBJECT = SELECTED_LISTING_DETAIL_OBJECT;
    }

    public UserProfileTemplate getUserTemplate() {
        return userTemplate;
    }

    public void setUserTemplate(UserProfileTemplate userTemplate) {
        this.userTemplate = userTemplate;
    }

    public int getCountMaxPages() {
        return countMaxPages;
    }

    public void setCountMaxPages(int countMaxPages) {
        this.countMaxPages = countMaxPages;
    }

    public ArrayList<ListingTemplate> getCurrentSearchResults() {
        return currentSearchResults;
    }

    public void setCurrentSearchResults(ArrayList<ListingTemplate> currentSearchResults) {
        this.currentSearchResults = currentSearchResults;
    }

    public SelfListingTemplate getMY_CURRENT_LISTING() {
        return MY_CURRENT_LISTING;
    }

    public void setMY_CURRENT_LISTING(SelfListingTemplate MY_CURRENT_LISTING) {
        this.MY_CURRENT_LISTING = MY_CURRENT_LISTING;
    }

    public String getCHANGE_PASSWORD() {
        return CHANGE_PASSWORD;
    }

    public boolean isTriggerCriteriaSearch() {
        return triggerCriteriaSearch;
    }

    public void setTriggerCriteriaSearch(boolean triggerCriteriaSearch) {
        this.triggerCriteriaSearch = triggerCriteriaSearch;
    }

    public LinkedHashMap<String, String> getSearchCategoryPayload() {
        return searchCategoryPayload;
    }

    public void setSearchCategoryPayload(LinkedHashMap<String, String> searchPayload) {
        this.searchCategoryPayload = searchPayload;
    }

    public LinkedHashMap<String, String> getSearchSubCategoryPayload() {
        return searchSubCategoryPayload;
    }

    public void getSearchSubCategoryPayload(LinkedHashMap<String, String> searchPayload) {
        this.searchSubCategoryPayload = searchPayload;
    }

    public String getEDIT_PROFILE() {
        return EDIT_PROFILE;
    }

    public String getCURRENT_SELECTED_LISTING_ID() {
        return CURRENT_SELECTED_LISTING_ID;
    }

    public void setCURRENT_SELECTED_LISTING_ID(String CURRENT_SELECTED_LISTING_ID) {
        this.CURRENT_SELECTED_LISTING_ID = CURRENT_SELECTED_LISTING_ID;
    }

    public ArrayList<ContactSharingTemplate> getUserCurrentAddressBook() {
        return userCurrentAddressBook;
    }

    public void setUserCurrentAddressBook(ArrayList<ContactSharingTemplate> userCurrentAddressBook) {
        this.userCurrentAddressBook = userCurrentAddressBook;
    }

    public ListingTemplate[] getCURRENT_USER_LISTING() {
        return CURRENT_USER_LISTINGS;
    }

    public void setCURRENT_USER_LISTING(ListingTemplate[] CURRENT_USER_LISTINGS) {
        this.CURRENT_USER_LISTINGS = CURRENT_USER_LISTINGS;
    }

    public LinkedHashMap<String, CategoryTemplate> getCurrentLoadedCategories() {
        return currentLoadedCategories;
    }

    public void setCurrentLoadedCategories(LinkedHashMap<String, CategoryTemplate> currentLoadedCategories) {
        this.currentLoadedCategories = currentLoadedCategories;
    }

    public LinkedHashMap<String, String> getSearchCategories() {
        return searchCategories;
    }

    public void setSearchCategories(LinkedHashMap<String, String> searchCategories) {
        this.searchCategories = searchCategories;
    }

    public LinkedHashMap<String, String> getSearchSubCategories() {
        return searchSubCategories;
    }

    public void setSearchSubCategories(LinkedHashMap<String, String> searchSubCategories) {
        this.searchSubCategories = searchSubCategories;
    }

    public LinkedHashMap<String, CategoryTemplate> getCurrentLoadedSubCategories() {
        return currentLoadedSubCategories;
    }

    public void setCurrentLoadedSubCategories(LinkedHashMap<String, CategoryTemplate> currentLoadedSubCategories) {
        this.currentLoadedSubCategories = currentLoadedSubCategories;
    }

    public String getEDIT_LISTING() {
        return EDIT_LISTING;
    }

    public HashMap<String, String> getCurrentListingExistingPics() {
        return currentListingExistingPics;
    }

    public void setCurrentListingExistingPics(LinkedHashMap<String, String> currentListingExistingPics) {
        this.currentListingExistingPics = currentListingExistingPics;
    }

    public ListingTemplate getCURRENT_LISTING_OBJECT() {
        return CURRENT_LISTING_OBJECT;
    }

    public void setCURRENT_LISTING_OBJECT(ListingTemplate CURRENT_LISTING_OBJECT) {
        this.CURRENT_LISTING_OBJECT = CURRENT_LISTING_OBJECT;
    }

    public String getSUBMIT_LISTING() {
        return BASE_URL + SUBMIT_LISTING;
    }

    public String getDELETE_PHOTO() {
        return BASE_URL + DELETE_PHOTO;
    }

    public HashMap<String, String> getListingImages() {
        return listingImages;
    }

    public void setListingImages(HashMap<String, String> listingImages) {
        this.listingImages = listingImages;
    }

    //=================hash map for product details==============
    public static HashMap<String, String> getHashMapProductDetails() {
        return hashMapProductDetails;
    }

    public static void setHashMapProductDetails(HashMap<String, String> hashMapProductDetails) {
        RippleittAppInstance.hashMapProductDetails = hashMapProductDetails;
    }

    public static HashMap<String, String> hashMapProductDetails = new HashMap<>();

    public String getFETCH_CATEGORIES() {
        return BASE_URL + FETCH_CATEGORIES;
    }

    public String getFETCH_SUBCATEGORIES() {
        return BASE_URL + FETCH_SUBCATEGORIES;
    }

    public static ArrayList<HashMap<String, String>> getArrayImages() {
        return arrayImages;
    }

    public static void setArrayImages(ArrayList<HashMap<String, String>> arrayImages) {
        RippleittAppInstance.arrayImages = arrayImages;
    }

    public static ArrayList<HashMap<String, String>> arrayImages = new ArrayList<>();

    public static ArrayList<HashMap<String, String>> getArraybids() {
        return arraybids;
    }

    public static void setArraybids(ArrayList<HashMap<String, String>> arraybids) {
        RippleittAppInstance.arraybids = arraybids;
    }

    public int getCURRENT_SELECTED_IMAGE_POSITION() {
        return CURRENT_SELECTED_IMAGE_POSITION;
    }

    public void setCURRENT_SELECTED_IMAGE_POSITION(int CURRENT_SELECTED_IMAGE_POSITION) {
        this.CURRENT_SELECTED_IMAGE_POSITION = CURRENT_SELECTED_IMAGE_POSITION;
    }

    public void setCategories(CategoryTemplate[] categories_) {
        this.categories.clear();
        this.currentLoadedCategories.clear();
        categories.put("Category*", "");
        for (int index = 0; index < categories_.length; index++) {
            categories.put(categories_[index].getName(), categories_[index].getId());
            currentLoadedCategories.put(categories_[index].getId(), categories_[index]);
        }
    }

    public LinkedHashMap<String, String> getPriceRanges() {
        return priceRanges;
    }

    public void setPriceRanges(PriceRangeTemplate[] priceRangeData) {
        this.priceRanges.clear();
        this.currentLoadedPriceRanges.clear();
        for (int index = 0; index < priceRangeData.length; index++) {
            priceRanges.put(priceRangeData[index].getTitle(),
                    priceRangeData[index].getRange_id());
            currentLoadedPriceRanges.put(priceRangeData[index].getRange_id(), priceRangeData[index]);
        }

    }

    public void setSubCategories(CategoryTemplate[] subCategories_) {
        if (subCategories_ == null) return;
        this.subCategories.clear();
        this.currentLoadedSubCategories.clear();
        subCategories.put("Sub Category*", "");
        for (int index = 0; index < subCategories_.length; index++) {
            subCategories.put(subCategories_[index].getName(), subCategories_[index].getId());
            currentLoadedSubCategories.put(subCategories_[index].getId(), subCategories_[index]);
        }


    }

    public ArrayList<String> getCategories() {
        return new ArrayList<String>(categories.keySet());
    }

    public ArrayList<String> getSubCategories() {
        return new ArrayList<String>(subCategories.keySet());
    }

    public String getCategoryId(String categoryName) {
        return this.categories.get(categoryName);
    }

    public String getSubCategoryId(String subcategoryName) {
        return this.subCategories.get(subcategoryName);
    }

    public static ArrayList<HashMap<String, String>> arraybids = new ArrayList<>();

    public static RippleittAppInstance getInstance() {
        return instance;
    }

    public static String formatPicPath(String unformatedPath) {
        if (unformatedPath != null) {
            return IMAGE_PATH_PREFIX + unformatedPath.replaceAll("../../", "/");
        } else {
            return "";
        }

    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    private void initVolley() {
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public static String getBuyNow() {
        return BUY_NOW;
    }

    public void segregateResults(List<ListingTemplate> currentResultsBatch) {
        currentSearchResults.addAll(currentResultsBatch);

        for (ListingTemplate obj : currentResultsBatch) {
            if (obj.getListing_type().equalsIgnoreCase("1")) {
                currentFilteredProducts.add(obj);
            } else {
                currentFilteredServices.add(obj);
            }
        }

    }

    public VoucherTemplate getCURRENT_VOUCHER_OBJECT() {
        return CURRENT_VOUCHER_OBJECT;
    }

    public void setCURRENT_VOUCHER_OBJECT(VoucherTemplate CURRENT_VOUCHER_OBJECT) {
        this.CURRENT_VOUCHER_OBJECT = CURRENT_VOUCHER_OBJECT;
    }

    public boolean isProductFilter() {
        return isProductFilter;
    }

    public void setProductFilter(boolean productFilter) {
        isProductFilter = productFilter;
    }

    public ListingSyncTemplate getCURRENT_ADDED_LISTING_OBJECT() {
        return CURRENT_ADDED_LISTING_OBJECT;
    }

    public void setCURRENT_ADDED_LISTING_OBJECT(ListingSyncTemplate CURRENT_ADDED_LISTING_OBJECT) {
        this.CURRENT_ADDED_LISTING_OBJECT = CURRENT_ADDED_LISTING_OBJECT;
    }


//    try {
//        JSONObject object = new JSONObject(response);
//        String status = object.getString("response_code");
//        String msg = object.getString("response_message");
//
//        Log.e("Status", status + "");
//        if (status.equalsIgnoreCase("1")) {
//
//            JSONObject userDetailsObj = object.getJSONObject("userinformation");
//
//            String userName = userDetailsObj.getString("fname") + " " + userDetailsObj.getString("lname");
//            String userTokenId = userDetailsObj.getString("token");
//            String email = userDetailsObj.getString("email");
//            String mobilenumber = userDetailsObj.getString("mobilenumber");
//            String longitude = userDetailsObj.getString("longitude");
//            String latitude = userDetailsObj.getString("latitude");
//            String address1 = userDetailsObj.getString("address1");
//            String address2 = userDetailsObj.getString("address2");
//            String gender = userDetailsObj.getString("gender");
//            String image = userDetailsObj.getString("image");
//            String referalcode = userDetailsObj.getString("referalcode");
//            String user_id = userDetailsObj.getString("user_id");
//            String user_type = userDetailsObj.getString("user_type");
//            String abn_number = userDetailsObj.getString("abn_number");
//            String account_number = userDetailsObj.getString("bank_account_number");
//            String business_name = userDetailsObj.getString("business_name");
//            PreferenceHandler.writeString(getBaseContext(), PreferenceHandler.USER_TYPE, user_type);
//            PreferenceHandler.writeString(getBaseContext(), PreferenceHandler.ABN_NUMBER, abn_number);
//            PreferenceHandler.writeString(getBaseContext(), PreferenceHandler.BUSINESS_NAME, business_name);
//            PreferenceHandler.writeString(getBaseContext(), PreferenceHandler.ACCOUNT_NUMBER, account_number);
//
//            String email_verificationFlag = userDetailsObj.getString("email_verified");
//            String mobile_verificationFlag = userDetailsObj.getString("phone_verified");
//
//            RippleittAppInstance.TOKEN_ID = userTokenId;
//            RippleittAppInstance.userObject = userDetailsObj;
//            SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("user_id", user_id);
//            editor.commit();
//
//            PreferenceHandler.writeString(ActivityVerifyEmail.this, PreferenceHandler.INIT_MODE, PreferenceHandler.LOGIN);
//            RippleittAppInstance.getInstance().setEmailVerified(email_verificationFlag);
//            RippleittAppInstance.getInstance().setMobileVerified(mobile_verificationFlag);
//
//            if (email_verificationFlag.equalsIgnoreCase("1") &&
//                    (mobile_verificationFlag.equalsIgnoreCase("1"))) {
//
//                editor.putString("userTokenId", userTokenId);
//                editor.putString("user_name", userName);
//                editor.putString("email", email);
//                editor.putString("image", image);
//                editor.putString("address1", address1);
//                editor.putString("address2", address2);
//                editor.putString("mobilenumber", mobilenumber);
//                editor.putString("user_id", user_id);
//                editor.putString("fname", userDetailsObj.getString("fname"));
//                editor.putString("lname", userDetailsObj.getString("lname"));
//                editor.putString("abn_no", userDetailsObj.getString("abn_number"));
//                editor.putString("business_name", userDetailsObj.getString("business_name"));
//                editor.commit();
//                TastyToast.makeText(getBaseContext(), msg, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
//                startActivity(new Intent(getBaseContext(), ActivityHome.class));
//            }
//            finish();
//
//        } else {
//            TastyToast.makeText(getBaseContext(), msg, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
//        }
//    } catch (
//    JSONException e) {
//        e.printStackTrace();
//    }


}
