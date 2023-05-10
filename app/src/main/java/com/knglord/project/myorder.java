package com.knglord.project;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class myorder extends AppCompatActivity{
    private TextView status,ptv,dntv,adp,reviewtxt,price;
    private TextView nvuname,nvdevice;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    DatabaseReference mref;
    FirebaseUser xuser;
    ImageView navlogo,icon;
    public LinearLayout lin,l,line,expand,ldate,lrate,ldetail;
    ProgressDialog progress;
    RelativeLayout statlin;
    private SmileRating sm;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            mref = FirebaseDatabase.getInstance().getReference("users-order").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        drawerLayout = (DrawerLayout) findViewById(R.id.dl);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        lin = findViewById(R.id.linlayout);
        price = findViewById(R.id.price);
        //drawerLayout.clearFocus();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        View head = navigationView.getHeaderView(0);
        navlogo = head.findViewById(R.id.mkview);
        nvdevice = head.findViewById(R.id.udevice);
        nvdevice.setText(Build.MODEL);
        nvuname = head.findViewById(R.id.uname);
        xuser = FirebaseAuth.getInstance().getCurrentUser();
        if(xuser != null) {
            Glide.with(myorder.this).load(xuser.getPhotoUrl()).into(navlogo);
            Pattern pat = Pattern.compile("\\b\\w+@\\b");
            Matcher mat = pat.matcher(xuser.getEmail());
            while (mat.find()){
                System.out.println("Match: " + mat.group());
                nvuname.setText(mat.group());
            }
        }
        toolbar.setTitle("My Order");
        setSupportActionBar(toolbar);
        toolbar.setCollapsible(false);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.getMenu().findItem(R.id.Admin).setVisible(false);
        view();
        FirebaseUser xuser = FirebaseAuth.getInstance().getCurrentUser();
        if(xuser != null)
            Glide.with(myorder.this).load(xuser.getPhotoUrl()).into(navlogo);
        if(xuser==null){
            navigationView.getMenu().findItem(R.id.Account).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Myorder).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Pendingorder).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Myref).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Logout).setVisible(false);
        }
        if(xuser != null)
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id= menuItem.getItemId();
                DrawerLayout drawer = findViewById(R.id.dl);
                switch (id) {
                    case R.id.Home:
                        Intent homeintent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(homeintent);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.login:
                        Intent login=new Intent(getApplicationContext(),login.class);
                        startActivity(login);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Account:
                        Intent accountintent = new Intent(getApplicationContext(), account.class);
                        startActivity(accountintent);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Myorder:
                        drawer.closeDrawers();
                        break;
                    case R.id.Pendingorder:
                        Intent pendingintent = new Intent(getApplicationContext(),pendingorder.class);
                        startActivity(pendingintent);
                        //drawer.closeDrawers();
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Helpcenter:
                        Intent helpintent = new Intent(getApplicationContext(),helpcenter.class);
                        startActivity(helpintent);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Myref:
                        Intent myrefintent = new Intent(getApplicationContext(),myref.class);
                        startActivity(myrefintent);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Aboutus:
                        Intent aboutintent = new Intent(getApplicationContext(), aboutus.class);
                        startActivity(aboutintent);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        drawer.closeDrawers();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    public void view(){
        if(mref != null) {
            progress = new ProgressDialog(myorder.this);
            progress.setTitle("Loading");
            progress.setMessage("Checking From Server");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull final DataSnapshot dataSnapshot) {
                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                        String stat = String.valueOf(ds.child("status").getValue());
                        if (Objects.equals(stat,String.valueOf(1))) {
                            final int xx= Integer.parseInt(String.valueOf(ds.child("id").getValue()));
                            Log.d("id : ", String.valueOf(ds.child("id").getValue()));
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            lp.setMargins(10, 5, 5, 5);
                            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp1.setMargins(15, 15, 15, 15);
                            LinearLayout.LayoutParams statlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            statlp.setMargins(5, 5, 5, 5);
                            l = new LinearLayout(getApplicationContext());
                            dntv = new TextView(getApplicationContext());
                            ptv = new TextView(getApplicationContext());
                            adp = new TextView(getApplicationContext());
                            status = new TextView(getApplicationContext());
                            line = new LinearLayout(getApplicationContext());
                            statlin = new RelativeLayout(getApplicationContext());
                            icon = new ImageView(getApplicationContext());
                            expand = new LinearLayout(getApplicationContext());
                            expand.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,110));
                            expand.setId(xx);
                            expand.setVisibility(View.GONE);
                            line.setBackgroundColor(Color.parseColor("#cfcfcf"));
                            LinearLayout.LayoutParams linel = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));
                            linel.setMargins(10, 15,8,15);
                            line.setLayoutParams(linel);
                            l.setPadding(20,5,10,10);
                            l.setOrientation(LinearLayout.VERTICAL);
                            l.setElevation(10);
                            l.setBackgroundResource(R.drawable.ongoing_bg);
                            l.setLayoutParams(lp1);
                            adp.setLayoutParams(lp);
                            dntv.setLayoutParams(lp);
                            ptv.setLayoutParams(lp);
                            status.setLayoutParams(lp);
                            statlin.setLayoutParams(statlp);
                            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            Log.e("dskey////", String.valueOf(ds.getKey()));
                            Log.e("dsval////", String.valueOf(ds.getValue()));
                            //Log.e("dskey////val", String.valueOf(ds.child("devicename").getValue()));
                            //Toast.makeText(getApplicationContext(),"mref"+mref,Toast.LENGTH_LONG);
                            //if (Objects.equals(ds.getKey(), xuser.getUid())) {
                            adp.setText("Other Problems - "+String.valueOf(ds.child("additionprob").getValue()));
                            //Log.e("ds", String.valueOf(ds));
                            dntv.setText("Device Name - "+String.valueOf(ds.child("devicename").getValue()));
                            ptv.setText("Selected Problems - "+String.valueOf(ds.child("problems").getValue()).replace("[", "").replace("]", ""));
                            status.setText("Fixing Status : Ongoing");
                            status.setTextColor(Color.parseColor("#48c0e8"));
                            icon.setBackgroundResource(R.drawable.ic_down);
                            icon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#48c0e8")));
                            icon.setLayoutParams(rlp);
                            l.addView(dntv);
                            l.addView(ptv);
                            l.addView(adp);
                            l.addView(line);
                            statlin.addView(status);
                            statlin.addView(icon);
                            l.addView(statlin);
                            l.addView(expand);
                            lin.addView(l);
                            icon.setId(xx+100);
                            icon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    expand=findViewById(xx);
                                    icon=findViewById(xx+100);
                                    if (expand.getVisibility() == View.GONE) {
                                        TransitionManager.beginDelayedTransition(expand, new AutoTransition());
                                        expand.setVisibility(View.VISIBLE);
                                        icon.setBackgroundResource(R.drawable.ic_up_arrow);
                                    } else {
                                        TransitionManager.beginDelayedTransition(expand, new AutoTransition());
                                        expand.setVisibility(View.GONE);
                                        icon.setBackgroundResource(R.drawable.ic_down);
                                    }
                                }
                            });
                        }
                        else if (Objects.equals(stat,String.valueOf(10))) {
                            final int xx= Integer.parseInt(String.valueOf(ds.child("id").getValue()));
                            Log.d("id : ", String.valueOf(ds.child("id").getValue()));
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            lp.setMargins(10, 5, 5, 5);
                            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp1.setMargins(15, 15, 15, 15);
                            LinearLayout.LayoutParams statlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            statlp.setMargins(5, 5, 5, 5);
                            l = new LinearLayout(getApplicationContext());
                            expand = new LinearLayout(getApplicationContext());
                            expand.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            lrate = new LinearLayout(getApplicationContext());
                            lrate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            lrate.setOrientation(LinearLayout.HORIZONTAL);
                            expand.setOrientation(LinearLayout.VERTICAL);
                            expand.setId(xx);
                            expand.setVisibility(View.GONE);
                            sm = new SmileRating(getApplicationContext());
                            sm.setLayoutParams(statlp);
                            int rating = Integer.parseInt(String.valueOf(ds.child("rating").getValue()));
                            sm.setSelectedSmile(rating);
                            reviewtxt = new TextView(getApplicationContext());
                            reviewtxt.setLayoutParams(lp);
                            reviewtxt.setText("Share your experience with us");
                            reviewtxt.setPadding(0, 10, 0, 0);
                            lrate.addView(reviewtxt);
                            lrate.addView(sm);
                            expand.addView(lrate);
                            sm.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
                                @Override
                                public void onSmileySelected(int smiley, boolean reselected) {
                                    Toast.makeText(getApplicationContext(), "Thank you for your feedback", Toast.LENGTH_SHORT).show();
                                    ds.getRef().child("rating").setValue(smiley);
                                    lin.removeAllViews();
                                }
                            });
                            ldate = new LinearLayout(getApplicationContext());
                            ldate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            //ldate.setPadding(5, 0, 5, 0);
                            ldate.setOrientation(LinearLayout.HORIZONTAL);
                            ImageView iv = new ImageView(getApplicationContext());
                            iv.setLayoutParams(new ViewGroup.LayoutParams(50, 170));
                            iv.setImageResource(R.drawable.delivery_status_layout);
                            iv.setScaleType(ImageView.ScaleType.CENTER);
                            ldate.addView(iv);
                            ldetail = new LinearLayout(getApplicationContext());
                            ldetail.setOrientation(LinearLayout.VERTICAL);
                            ldetail.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            TextView orderdate = new TextView(getApplicationContext());
                            TextView deliverydate = new TextView(getApplicationContext());
                            orderdate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            deliverydate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            orderdate.setText("Ordered : "+ds.child("orderdate").getValue());
                            orderdate.setTextColor(Color.BLACK);
                            deliverydate.setText("Delivered : "+ds.child("deliverydate").getValue());
                            deliverydate.setTextColor(Color.BLACK);
                            deliverydate.setPadding(0, 60, 0, 0);
                            ldetail.addView(orderdate);
                            ldetail.addView(deliverydate);
                            ldate.addView(ldetail);
                            expand.addView(ldate);
                            dntv = new TextView(getApplicationContext());
                            ptv = new TextView(getApplicationContext());
                            adp = new TextView(getApplicationContext());
                            status = new TextView(getApplicationContext());
                            line = new LinearLayout(getApplicationContext());
                            statlin = new RelativeLayout(getApplicationContext());
                            icon = new ImageView(getApplicationContext());
                            line.setBackgroundColor(Color.parseColor("#cfcfcf"));
                            LinearLayout.LayoutParams linel = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));
                            linel.setMargins(10, 15,8,15);
                            line.setLayoutParams(linel);
                            adp.setLayoutParams(lp);
                            dntv.setLayoutParams(lp);
                            ptv.setLayoutParams(lp);
                            status.setLayoutParams(lp);
                            statlin.setLayoutParams(statlp);
                            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            l.setPadding(20,5,10,10);
                            l.setOrientation(LinearLayout.VERTICAL);
                            l.setElevation(10);
                            l.setBackgroundResource(R.drawable.complete_bg);
                            l.setLayoutParams(lp1);
                            Log.e("dskey////", String.valueOf(ds.getKey()));
                            Log.e("dsval////", String.valueOf(ds.getValue()));
                            //Log.e("dskey////val", String.valueOf(ds.child("devicename").getValue()));
                            //Toast.makeText(getApplicationContext(),"mref"+mref,Toast.LENGTH_LONG);
                            //if (Objects.equals(ds.getKey(), xuser.getUid())) {
                            adp.setText("Other Problems - "+String.valueOf(ds.child("additionprob").getValue()));
                            //Log.e("ds", String.valueOf(ds));
                            dntv.setText("Device Name - "+String.valueOf(ds.child("devicename").getValue()));
                            ptv.setText("Selected Problems - "+String.valueOf(ds.child("problems").getValue()).replace("[", "").replace("]", ""));
                            status.setText("Fixing Status : Completed");
                            status.setTextColor(Color.parseColor("#03fc5a"));
                            icon.setBackgroundResource(R.drawable.ic_down);
                            icon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#03fc5a")));
                            icon.setLayoutParams(rlp);
                            l.addView(dntv);
                            l.addView(ptv);
                            l.addView(adp);
                            l.addView(line);
                            statlin.addView(status);
                            statlin.addView(icon);
                            l.addView(statlin);
                            l.addView(expand);
                            lin.addView(l);
                            icon.setId(xx+100);
                            icon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    expand=findViewById(xx);
                                    icon=findViewById(xx+100);
                                    if (expand.getVisibility() == View.GONE) {
                                        TransitionManager.beginDelayedTransition(expand, new AutoTransition());
                                        expand.setVisibility(View.VISIBLE);
                                        icon.setBackgroundResource(R.drawable.ic_up_arrow);
                                    } else {
                                        TransitionManager.beginDelayedTransition(expand, new AutoTransition());
                                        expand.setVisibility(View.GONE);
                                        icon.setBackgroundResource(R.drawable.ic_down);
                                    }
                                }
                                //}
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
            progress.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        overridePendingTransition(R.anim.back_transition, R.anim.background);
        this.finish();
    }
}
