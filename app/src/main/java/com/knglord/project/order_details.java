package com.knglord.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.messaging.FirebaseMessaging;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class order_details extends AppCompatActivity {
    DatabaseReference mref,sref;
    FirebaseUser xuser;
    private TextView stat,ptv,dntv,adp,id,user;
    LinearLayout lin,l;
    Button bt,bt1,bt2;
    String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Intent intent = getIntent();
        final String st = intent.getStringExtra("uid");
        //xuser = st;
        lin = findViewById(R.id.lin);
        user = findViewById(R.id.user);
        mref = FirebaseDatabase.getInstance().getReference("users-order").child(st);
        sref = FirebaseDatabase.getInstance().getReference("users");
        Log.e("sref-", String.valueOf(sref));
        Log.e("mref-", String.valueOf(mref));
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lin.removeAllViews();
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(5, 10, 5, 5);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp1.setMargins(15, 15, 15, 15);
                    l = new LinearLayout(getApplicationContext());
                    l.setPadding(10,5,10,5);
                    l.setOrientation(LinearLayout.HORIZONTAL);
                    l.setElevation(10);
                    l.setLayoutParams(lp1);
                    dntv = new TextView(getApplicationContext());
                    ptv = new TextView(getApplicationContext());
                    adp = new TextView(getApplicationContext());
                    id = new TextView(getApplicationContext());
                    bt = new Button(getApplicationContext());
                    bt1 = new Button(getApplicationContext());
                    bt2 = new Button(getApplicationContext());
                    stat = new TextView(getApplicationContext());
                    adp.setLayoutParams(lp);
                    dntv.setLayoutParams(lp);
                    ptv.setLayoutParams(lp);
                    id.setLayoutParams(lp);
                    bt.setLayoutParams(lp);
                    bt1.setLayoutParams(lp);
                    bt2.setLayoutParams(lp);
                    stat.setLayoutParams(lp);
                    //Log.e("snapshot", String.valueOf(ds.getChildren()));
//                    Log.e("dskey////", String.valueOf(ds.getKey()));
//                    Log.e("dsval////", String.valueOf(ds.getValue()));
                    //Log.e("dskey////val", String.valueOf(ds.child("devicename").getValue()));
                    //Toast.makeText(getApplicationContext(),"mref"+mref,Toast.LENGTH_LONG);
                    //if (Objects.equals(ds.getKey(), xuser.getUid())) {
                    adp.setText(String.valueOf("Other Problem - "+ds.child("additionprob").getValue()));
                    //Log.e("ds", String.valueOf(ds));
                    dntv.setText("Device - "+String.valueOf(ds.child("devicename").getValue()));
                    ptv.setText("Problems - "+String.valueOf(ds.child("problems").getValue()));
                    id.setText("Order ID - "+String.valueOf(ds.child("id").getValue()));
                    stat.setText("Status - "+String.valueOf(ds.child("status").getValue()));
                    String st = String.valueOf(ds.child("status").getValue());
                    if(Objects.equals(st,String.valueOf(1))) {
                        stat.setTextColor(Color.parseColor("#3eb0ed"));
                        stat.setTypeface(stat.getTypeface(), Typeface.BOLD);
                    }
                    else if(Objects.equals(st,String.valueOf(0))) {
                        stat.setTextColor(Color.parseColor("#ede13e"));
                        stat.setTypeface(stat.getTypeface(), Typeface.BOLD);
                    }
                    else if(Objects.equals(st,String.valueOf(10))) {
                        stat.setTextColor(Color.parseColor("#48d62f"));
                        stat.setTypeface(stat.getTypeface(), Typeface.BOLD);
                    }
                    bt1.setText("HOLD");
                    bt1.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                    bt.setText("APPROVE");
                    bt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#34b7eb")));
                    bt2.setText("DONE");
                    bt2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    lin.addView(id);
                    lin.addView(stat);
                    lin.addView(dntv);
                    lin.addView(ptv);
                    lin.addView(adp);
                    l.addView(bt1);
                    l.addView(bt);
                    l.addView(bt2);
                    lin.addView(l);
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(" : ", String.valueOf(ds.child("status").getValue()));
                            ds.getRef().child("status").setValue(1);
                            sendApprovednotification();
                        }
                    });
                    bt1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(" : ", String.valueOf(ds.child("status").getValue()));
                            ds.getRef().child("status").setValue(0);
                        }
                    });
                    bt2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(" : ", String.valueOf(ds.child("status").getValue()));
                            ds.getRef().child("status").setValue(10);
                            Date date = new Date();
                            CharSequence s  = DateFormat.format("EEEE, MMMM d yyyy ", date.getTime());
                            ds.getRef().child("deliverydate").setValue(s);
                            //sendFCMPush("done");
                            sendDonenotification();
                        }
                    });
                    LinearLayout l = new LinearLayout(getApplicationContext());
                    l.setMinimumHeight(3);
                    l.setBackgroundColor(Color.parseColor("#a8a9b3"));
                    lin.addView(l);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        sref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot ds : dataSnapshot.getChildren()){
                    String stat = st;
                    if (Objects.equals(ds.getKey(), stat)) {
                        topic = String.valueOf(ds.child("token").getValue());
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void sendDonenotification(){
        final String FCM_API = "https://fcm.googleapis.com/fcm/send";
        final String serverKey = "key=" + "AAAAN6sk20I:APA91bFeWAW023u1794hvn_hnhtNWgpPJRPvc-Am859TeBW42cj98c2Q0Z9xdXnU3wTMoo5x2v66p_tPNsqcfb9GhtWkoNbp2FmdhZVACliqCcgFTxUtro5VQUlJusFMzxF2QSMZbn-P";
        final String contentType = "application/json";
        final String TAG = "NOTIFICATION TAG";
        String NOTIFICATION_TITLE;
        String NOTIFICATION_MESSAGE;
        Log.e("---token----", topic);
        NOTIFICATION_TITLE = "Quick-fix";
        NOTIFICATION_MESSAGE = "Your product has been delivered, You'll receive at your door any time";
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", topic);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(order_details.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void sendApprovednotification(){
        final String FCM_API = "https://fcm.googleapis.com/fcm/send";
        final String serverKey = "key=" + "AAAAN6sk20I:APA91bFeWAW023u1794hvn_hnhtNWgpPJRPvc-Am859TeBW42cj98c2Q0Z9xdXnU3wTMoo5x2v66p_tPNsqcfb9GhtWkoNbp2FmdhZVACliqCcgFTxUtro5VQUlJusFMzxF2QSMZbn-P";
        final String contentType = "application/json";
        final String TAG = "NOTIFICATION TAG";
        String NOTIFICATION_TITLE;
        String NOTIFICATION_MESSAGE;
        Log.e("---token----", topic);
        NOTIFICATION_TITLE = "Quick-fix";
        NOTIFICATION_MESSAGE = "Your product has been approved, please check myorder for more details";
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", topic);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(order_details.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),admin_view.class));
        overridePendingTransition(R.anim.back_transition, R.anim.background);
        this.finish();
    }
}
