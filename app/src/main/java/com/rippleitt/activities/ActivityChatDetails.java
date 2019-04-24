package com.rippleitt.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.rippleitt.R;
import com.rippleitt.adapters.ChatListAdapter;
import com.rippleitt.commonUtilities.PreferenceHandler;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ChatMessageTemplate;
import com.rippleitt.webservices.SendPushNotificationApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pc on mail/27/2018.
 */

public class ActivityChatDetails extends AppCompatActivity
                    implements View.OnClickListener {

    private ImageView mImgVwBack;
    private TextView mtxtVwChatTitle;
    private CircleImageView mImgVwParticipantPic;
    private TextView mTxtVwTitle;
    private ImageView mImgVwDispatchMessage;
    private ProgressDialog mProgressDialog;
    private ArrayList<ChatMessageTemplate>chatLog=new ArrayList<ChatMessageTemplate>();
    private ChatListAdapter chatsAdapter;
    private ListView mLstVwChats;
    private EditText mEdtxtMessage;
    private DatabaseReference myRef, chatListReference;
    String device_token;


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_chat_details);
        initUI();
        configureFirebaseChat();
    }


    @Override
    public void onClick(View view) {
        if(view==mImgVwBack){
            finish();
        }if(view==mImgVwDispatchMessage){

//            if(mEdtxtMessage.getText().toString().matches(".*\\d+.*")){
//                com.rippleitt.utils.CommonUtils.showSingleButtonAlert(ActivityChatDetails.this,
//                                "Message cannot contain numeric values");
//                return;
//            }

            if(mEdtxtMessage.getText().toString().trim().equalsIgnoreCase("")){
                com.rippleitt.utils.CommonUtils.showSingleButtonAlert(ActivityChatDetails.this,
                        "Please provide message");
                return;
            }
            dispatchMessage();


        }
    }


    private void initUI(){
        mEdtxtMessage=(EditText)findViewById(R.id.edtxtMessage);
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("Updating conversation...");
        mProgressDialog.setCancelable(false);
        mImgVwBack=(ImageView)findViewById(R.id.imgBack);
        mImgVwDispatchMessage=(ImageView)findViewById(R.id.imgvwSendBtn);
        mLstVwChats=(ListView)findViewById(R.id.lstVwChatThread);
        mImgVwDispatchMessage.setOnClickListener(this);
       mtxtVwChatTitle=(TextView)findViewById(R.id.txtvwCounterParty);
        mImgVwParticipantPic=(CircleImageView) findViewById(R.id.imgVwChatListUserPic);
        mImgVwBack.setOnClickListener(this);
        Picasso.with(ActivityChatDetails.this)
                .load(RippleittAppInstance.formatPicPath(
                        RippleittAppInstance.getInstance()
                                .getCurrentChatPartner().getImage()))
                .placeholder(R.drawable.default_profile_icon)
                .error(R.drawable.default_profile_icon)
                .into(mImgVwParticipantPic);
        mtxtVwChatTitle.setText(RippleittAppInstance.getInstance().getCurrentChatPartner().getFullName());
        if (RippleittAppInstance.getInstance().getCurrentChatPartner().getFcm_token()!=null)
        device_token= RippleittAppInstance.getInstance().getCurrentChatPartner().getFcm_token();
    }


    private void configureFirebaseChat(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String selfId=PreferenceHandler
                .readString(ActivityChatDetails.this,
                        PreferenceHandler.USER_ID,"");
        String partnerID=RippleittAppInstance.getInstance().getCurrentChatPartner().getUser_id();
        String chat_path="";
        if(Integer.parseInt(selfId)<Integer.parseInt(partnerID)){
            chat_path=selfId+"_"+partnerID;
        }else{
            chat_path=partnerID+"_"+selfId;
        }

        // create a reference in the chatList table..

        try{

            String chatListPath = "chatList/"+selfId+"/"+partnerID;
            chatListReference = database.getReference(chatListPath);
            if (chatListReference==null) {
                HashMap<String, Long> object = new HashMap();
                object.put("timestamp", System.currentTimeMillis());
                chatListReference.setValue(object);
            }

        }catch (Exception e){

        }

        String dataStorePath = "message/"+ chat_path;
         myRef = database.getReference(dataStorePath);
        if(myRef!=null){

            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        HashMap<String,Object> packet=(HashMap) dataSnapshot.getValue();
                        ChatMessageTemplate chatObject = new ChatMessageTemplate();
                        chatObject.setText((String)packet.get("text"));
                        chatObject.setTimestamp((long)packet.get("timestamp"));
                        chatObject.setUserID((String)packet.get("userID"));
                        chatLog.add(chatObject);

                    if(chatsAdapter==null){
                        populateChatList();
                    }else{
                        chatsAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    // update the chat list...
    private void populateChatList(){
        chatsAdapter = new ChatListAdapter(ActivityChatDetails.this, chatLog);
        mLstVwChats.setAdapter(chatsAdapter);
        if (chatLog!=null && chatLog.size()!=0)
        mLstVwChats.smoothScrollToPosition(chatLog.size()-1);
    }


    private void dispatchMessage(){
        if(!mEdtxtMessage.getText()
                .toString()
                .equalsIgnoreCase("")){
            // send the message
            if(myRef!=null){
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                ChatMessageTemplate chatObject = new ChatMessageTemplate();
                chatObject.setText(mEdtxtMessage.getText().toString());
                chatObject.setUserID(PreferenceHandler.readString(ActivityChatDetails.this,
                        PreferenceHandler.USER_ID,""));
                chatObject.setTimestamp(ServerValue.TIMESTAMP);
                myRef.push().setValue(chatObject);
                String selfId=PreferenceHandler
                        .readString(ActivityChatDetails.this,
                                PreferenceHandler.USER_ID,"");
                String partnerID=RippleittAppInstance.getInstance().getCurrentChatPartner().getUser_id();
                try{

                    String chatListPath = "chatList/"+selfId+"/"+partnerID;
                    chatListReference = database.getReference(chatListPath);
                    HashMap<String,Long> object= new HashMap();
                    object.put("timestamp",System.currentTimeMillis());
                    chatListReference.setValue(object);

                    String chatListPathFriend = "chatList/"+partnerID+"/"+selfId;
                    object.put("timestamp",System.currentTimeMillis());
                    database.getReference(chatListPathFriend).setValue(object);

//                    database.getReference("chatList/"+selfId).orderByChild(partnerID+"/timestamp");

                }catch (Exception e){
                    Log.d( "dispatchMessage: ",e.getMessage());
                }
                new SendPushNotificationApi().SendPushNotificationApi(ActivityChatDetails.this,mEdtxtMessage.getText().toString(),device_token);
                mEdtxtMessage.setText("");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RippleittAppInstance.currentActivity = "chat";
    }

    @Override
    protected void onPause() {
        super.onPause();
        RippleittAppInstance.currentActivity = null;
    }
}
