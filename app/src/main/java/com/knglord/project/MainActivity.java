package com.knglord.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private TextView nvuname,nvdevice,icon,price,totalcost,amountpay,tv,applycoupon,removecoupon;
    DatabaseReference mref,sref;
    public FirebaseUser xuser;
    ImageView navlogo,bill,currentdevice;
    String device,uid,person;
    EditText et;
    Button bt;
    CheckBox dd,bs,sp,snr,bbp,cp,ap;
    static int id=0;
    int cost=0,discountcost,maxdiscount;
    Boolean status = false;
    Snackbar sc;
    LinearLayout expanded;
    RelativeLayout expand;
    LinearLayout sliderDotspanel;
    private ImageView[] dots;
    ViewPager pager;

    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dots.length; i++) {
                    dots[i].setImageResource(R.drawable.inactive_dot);
                }
                dots[position].setImageResource(R.drawable.active_dot);
                Log.e("pos", String.valueOf(position));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
            //        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//        Add upper code block before start camera or file browsing or get exception or use provider-path as follows
        uploadimage();
        applycoupon = findViewById(R.id.applycoupon);
        removecoupon = findViewById(R.id.removecoupon);
        removecoupon.setVisibility(View.GONE);
        pager.setVisibility(View.GONE);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        sliderDotspanel.setVisibility(View.GONE);
        setupPagerIndidcatorDots();
        dots[0].setImageResource(R.drawable.active_dot);
        Intent intent = getIntent();
        final String item_id = intent.getStringExtra("id");
        applycoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setVisibility(View.VISIBLE);
                sliderDotspanel.setVisibility(View.VISIBLE);
                applycoupon.setText("click here to apply coupon");
                applycoupon.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                applycoupon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.e("coupon_Id:",item_id);
                        if(pager.getCurrentItem()==0) {
                            applycoupon.setText("First Order Coupon applied");
                            coupon0apply();
                            applycoupon.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                            applycoupon.setTextColor(Color.parseColor("#FAD25C"));
                            pager.setVisibility(View.GONE);
                            sliderDotspanel.setVisibility(View.GONE);
                            removecoupon.setVisibility(View.VISIBLE);
                            removecoupon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    coupon0remove();
                                    removecoupon.setVisibility(View.GONE);
                                    pager.setVisibility(View.VISIBLE);
                                    sliderDotspanel.setVisibility(View.VISIBLE);
                                    applycoupon.setText("click here to apply coupon");
                                }
                            });
                        }else if(pager.getCurrentItem()==1){
                            applycoupon.setText("Referral Coupon applied");
                            applycoupon.setTextColor(Color.parseColor("#FAD25C"));
                            applycoupon.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                            pager.setVisibility(View.GONE);
                            sliderDotspanel.setVisibility(View.GONE);
                            removecoupon.setVisibility(View.VISIBLE);
                            removecoupon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    removecoupon.setVisibility(View.GONE);
                                    pager.setVisibility(View.VISIBLE);
                                    sliderDotspanel.setVisibility(View.VISIBLE);
                                    applycoupon.setText("click here to apply coupon");
                                }
                            });
                        }
                    }
                });
            }
        });
        expand = findViewById(R.id.expand);
        expanded = findViewById(R.id.expanded);
        icon = (TextView)findViewById(R.id.icon);
        price = findViewById(R.id.price);
        totalcost = findViewById(R.id.totalcost);
        amountpay = findViewById(R.id.amountpay);
        dd = findViewById(R.id.dd);
        bs = findViewById(R.id.bs);
        sp = findViewById(R.id.sp);
        snr = findViewById(R.id.snr);
        bbp = findViewById(R.id.bbp);
        cp = findViewById(R.id.cp);
        ap = findViewById(R.id.ap);
        drawerLayout=(DrawerLayout)findViewById(R.id.dl);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View head = navigationView.getHeaderView(0);
        navlogo = head.findViewById(R.id.mkview);
        nvdevice = head.findViewById(R.id.udevice);
        nvdevice.setText(Build.MODEL);
        nvuname = head.findViewById(R.id.uname);
        xuser = FirebaseAuth.getInstance().getCurrentUser();
        mref = FirebaseDatabase.getInstance().getReference("users-order");
        sref = FirebaseDatabase.getInstance().getReference("users");
        expanded.setVisibility(View.GONE);
        if(xuser != null) {
            Glide.with(MainActivity.this).load(xuser.getPhotoUrl()).into(navlogo);
            Pattern pat = Pattern.compile("\\b\\w+@\\b");
            Matcher mat = pat.matcher(xuser.getEmail());
            while (mat.find()){
                System.out.println("Match: " + mat.group());
                nvuname.setText(mat.group());
            }
        }
        device = Build.MANUFACTURER
                + " " + Build.MODEL + " "
                +"(android "+ Build.VERSION.RELEASE+")" + " "+Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
//        Log.e("user////", xuser.getEmail());
        et = findViewById(R.id.mydevice);
        et.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_END = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (et.getRight() - et.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                        et.setText(device);
                        return true;
                    }
                }
                return false;
            }
        });
        //nvuname.setText();
        //drawerLayout.clearFocus();
        bt = findViewById(R.id.placereq_btn);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("id : ", String.valueOf(id));
                String deviceinfo =et.getText().toString();
                Drawable billimg = bill.getDrawable();
                Drawable currentimg = currentdevice.getDrawable();
                Date date = new Date();
                CharSequence s  = DateFormat.format("EEEE, MMMM d yyyy ", date.getTime());
                Log.d("date// ", String.valueOf(s));
                List<String> result = new ArrayList<>();
                if(xuser == null)
                {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.login_first_toast, (ViewGroup) findViewById(R.id.login_toast));
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0, 150);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                    Intent intent =new Intent(MainActivity.this,login.class);
                    startActivity(intent);
                }
                else if(deviceinfo.isEmpty()){
                    //Toast.makeText(getApplicationContext(),"Please provide required details",Toast.LENGTH_SHORT).show();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.unsuccess_order_toast, (ViewGroup) findViewById(R.id.unsuccess_toast));
                    Toast toast = new Toast(getApplicationContext());
                    tv = layout.findViewById(R.id.textmsgg);
                    tv.setText("Please provide name of the device");
                    toast.setGravity(Gravity.BOTTOM, 0, 150);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
//                else if(billimg==null){
//                    //Toast.makeText(getApplicationContext(),"Please provide required details",Toast.LENGTH_SHORT).show();
//                    LayoutInflater inflater = getLayoutInflater();
//                    View layout = inflater.inflate(R.layout.unsuccess_order_toast, (ViewGroup) findViewById(R.id.unsuccess_toast));
//                    Toast toast = new Toast(getApplicationContext());
//                    tv = layout.findViewById(R.id.textmsgg);
//                    tv.setText("Please upload bill");
//                    toast.setGravity(Gravity.BOTTOM, 0, 150);
//                    toast.setDuration(Toast.LENGTH_LONG);
//                    toast.setView(layout);
//                    toast.show();
//                }
//                else if(currentimg==null){
//                    //Toast.makeText(getApplicationContext(),"Please provide required details",Toast.LENGTH_SHORT).show();
//                    LayoutInflater inflater = getLayoutInflater();
//                    View layout = inflater.inflate(R.layout.unsuccess_order_toast, (ViewGroup) findViewById(R.id.unsuccess_toast));
//                    Toast toast = new Toast(getApplicationContext());
//                    tv = layout.findViewById(R.id.textmsgg);
//                    tv.setText("Please upload bill");
//                    toast.setGravity(Gravity.BOTTOM, 0, 150);
//                    toast.setDuration(Toast.LENGTH_LONG);
//                    toast.setView(layout);
//                    toast.show();
//                }
                else if(!dd.isChecked() && !bs.isChecked() && !sp.isChecked() && !bbp.isChecked() && !cp.isChecked() && !snr.isChecked() && !ap.isChecked()){
                    //Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.unsuccess_order_toast, (ViewGroup) findViewById(R.id.unsuccess_toast));
                    Toast toast = new Toast(getApplicationContext());
                    tv = layout.findViewById(R.id.textmsgg);
                    tv.setText("please select at least one problem");
                    toast.setGravity(Gravity.BOTTOM, 0, 150);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
                else {
                    if(dd.isChecked()) {
                        result.add(String.valueOf(dd.getText()));
                    }
                    if(bs.isChecked()) {
                        result.add(String.valueOf(bs.getText()));
                    }
                    if(sp.isChecked()) {
                        result.add(String.valueOf(sp.getText()));
                    }
                    if(bbp.isChecked()) {
                        result.add(String.valueOf(bbp.getText()));
                    }
                    if(cp.isChecked()) {
                        result.add(String.valueOf(cp.getText()));
                    }
                    if(snr.isChecked()) {
                        result.add(String.valueOf(snr.getText()));
                    }
                    if(ap.isChecked()) {
                        result.add(String.valueOf(ap.getText()));
                    }
                    EditText adprob = findViewById(R.id.addprob);
                    String addprob = adprob.getText().toString();
                    uid = xuser.getUid();
                    person = xuser.getEmail();
                    long currentTime = Calendar.getInstance().getTimeInMillis();
                    id= (int) (id+currentTime);
                    Log.e("id--", String.valueOf(id));
                    //Log.e("uid ",uid);
                    demo d = new demo(person,deviceinfo,result,addprob,id,0,-1, (String) s,"",cost);
                    mref.child(uid).child(mref.push().getKey()).setValue(d);
                    sc = Snackbar.make(drawerLayout,"Oreder placed successfuly, check the pending status for more info",Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sc.dismiss();
                                    cost = 0;
                                    price.setText("\u20B9"+String.valueOf(cost));
                                }
                            });
                    sc.show();
                    sendNewOrderNotification();
                }
                id++;
            }
        });
        checkcost();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("Quick FIX");
        setSupportActionBar(toolbar);
        toolbar.setCollapsible(false);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.getMenu().findItem(R.id.Admin).setVisible(false);
        if(xuser==null){
            navigationView.getMenu().findItem(R.id.Account).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Myorder).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Pendingorder).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Myref).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Logout).setVisible(false);
        }
        if(xuser != null) {
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id= menuItem.getItemId();
                DrawerLayout drawer = findViewById(R.id.dl);
                switch (id) {
                    case R.id.Home:
                        drawer.closeDrawers();
                        break;
                    case R.id.login:
                        //drawer.closeDrawer(GravityCompat.START,false);
                        Intent login=new Intent(MainActivity.this,login.class);
                        startActivity(login);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Account:
                        Intent accountintent = new Intent(MainActivity.this, account.class);
                        startActivity(accountintent);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Myorder:
                        Intent myorderintent = new Intent(MainActivity.this,myorder.class);
                        startActivity(myorderintent);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Pendingorder:
                        Intent pendingintent = new Intent(getApplicationContext(),pendingorder.class);
                        startActivity(pendingintent);
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
        //TextView pricedetail = new TextView(this);
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expanded.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(expanded);
                    expanded.animate().setDuration(700).start();
                    expanded.setVisibility(View.VISIBLE);
                    icon.setBackgroundResource(R.drawable.ic_up_arrow);
                } else {
                    TransitionManager.beginDelayedTransition(expanded);
                    expanded.animate().setDuration(700).start();
                    expanded.setVisibility(View.GONE);
                    icon.setBackgroundResource(R.drawable.ic_expand);
                }
            }
        });
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("token--", "getInstanceId failed", task.getException());
                            return;
                        }
                        final String token = task.getResult().getToken();
                        Log.d("msg--", token);
                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                        if(sref != null && xuser!=null) {
                            sref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if (Objects.equals(ds.getKey(), xuser.getUid())) {
                                            //sref.child(xuser.getUid()).child("token").setValue(token);
                                            //Log.e("user", String.valueOf(ds.getRef().child("token").setValue(token)));
                                            ds.getRef().child("token").setValue(token);
                                            final String SUBSCRIBE_TO = token;
                                            Log.e("token----", SUBSCRIBE_TO);
                                            FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
            });
    }

    private void uploadimage() {
        bill=(ImageView)findViewById(R.id.billingimage);
        currentdevice=(ImageView)findViewById(R.id.deviceimage);
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int PERMISSION_ALL = 1;
                String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                for (String permission : PERMISSIONS) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(PERMISSIONS, PERMISSION_ALL);
                        status = false;
                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                        status = true;
                    }
                }
                if(status) {
                    selectImage();
                }
            }
        });
        currentdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int PERMISSION_ALL = 2;
                String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                for (String permission : PERMISSIONS) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(PERMISSIONS, PERMISSION_ALL);
                        status = false;
                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                        status = true;
                    }
                }
                if(status) {
                    selectImage1();
                }
            }
        });
    }
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(), MainActivity.this.getApplicationContext().getPackageName() + ".provider", f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void selectImage1() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f1 = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(), MainActivity.this.getApplicationContext().getPackageName() + ".provider", f1));
                    startActivityForResult(intent, 3);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 4);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    bill.setImageBitmap(bitmap);

                    /*String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile,outFile1 = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 99, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == 3) {
                File f1 = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f1.listFiles()) {
                    if (temp.getName().equals("temp1.jpg")) {
                        f1 = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap1;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap1 = BitmapFactory.decodeFile(f1.getAbsolutePath(),
                            bitmapOptions);
                    currentdevice.setImageBitmap(bitmap1);
                    /*String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f1.delete();
                    OutputStream outFile1 = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile1= new FileOutputStream(file);
                        bitmap1.compress(Bitmap.CompressFormat.JPEG, 99, outFile1);
                        outFile1.flush();
                        outFile1.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                bill.setImageBitmap(thumbnail);

            }
            else if (requestCode == 4) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                currentdevice.setImageBitmap(thumbnail);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    Toast.makeText(this, "Until you grant the permission, You won't be able to upload images", Toast.LENGTH_LONG).show();
                }
            break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    selectImage1();
                } else {
                    Toast.makeText(this, "Until you grant the permission, You won't be able to upload images", Toast.LENGTH_LONG).show();
                }
            break;
        }
    }

    public void checkcost(){
        cost=0;
        dd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dd.isChecked()) {
                    cost = cost + 500;
                    price.setText("\u20B9"+String.valueOf(cost));
                    totalcost.setText("Total cost : \u20B9"+String.valueOf(cost));
                    amountpay.setText("Amout payable : \u20B9"+String.valueOf(cost));
                }else {
                    cost = cost - 500;
                    price.setText("\u20B9"+String.valueOf(cost));
                    totalcost.setText("Total cost : \u20B9"+String.valueOf(cost));
                    amountpay.setText("Amout payable : \u20B9"+String.valueOf(cost));
                }
            }
        });
        bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bs.isChecked()) {
                    cost = cost + 1500;
                    price.setText("\u20B9"+String.valueOf(cost));
                    totalcost.setText("Total cost : \u20B9"+String.valueOf(cost));
                    amountpay.setText("Amout payable : \u20B9"+String.valueOf(cost));
                }else {
                    cost = cost - 1500;
                    price.setText("\u20B9"+String.valueOf(cost));
                    totalcost.setText("Total cost : \u20B9"+String.valueOf(cost));
                    amountpay.setText("Amout payable : \u20B9"+String.valueOf(cost));
                }
            }
        });
        sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.isChecked()) {
                    cost = cost + 500;
                    price.setText("\u20B9"+String.valueOf(cost));
                    totalcost.setText("Total cost : \u20B9"+String.valueOf(cost));
                    amountpay.setText("Amout payable : \u20B9"+String.valueOf(cost));
                }else {
                    cost = cost - 500;
                    price.setText("\u20B9"+String.valueOf(cost));
                    totalcost.setText("Total cost : \u20B9"+String.valueOf(cost));
                    amountpay.setText("Amout payable : \u20B9"+String.valueOf(cost));
                }
            }
        });
        bbp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bbp.isChecked()) {
                    cost = cost + 1000;
                    price.setText("\u20B9"+String.valueOf(cost));
                    totalcost.setText("Total cost : \u20B9"+String.valueOf(cost));
                    amountpay.setText("Amout payable : \u20B9"+String.valueOf(cost));
                }else {
                    cost = cost - 1000;
                    price.setText("\u20B9"+String.valueOf(cost));
                    totalcost.setText("Total cost : \u20B9"+String.valueOf(cost));
                    amountpay.setText("Amout payable : \u20B9"+String.valueOf(cost));
                }
            }
        });
        cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cp.isChecked()) {
                    cost = cost + 300;
                    price.setText("\u20B9"+String.valueOf(cost));
                    totalcost.setText("Total cost : \u20B9"+String.valueOf(cost));
                    amountpay.setText("Amout payable : \u20B9"+String.valueOf(cost));
                }else {
                    cost = cost - 300;
                    price.setText("\u20B9"+String.valueOf(cost));
                    totalcost.setText("Total cost : \u20B9"+String.valueOf(cost));
                    amountpay.setText("Amout payable : \u20B9"+String.valueOf(cost));
                }
            }
        });
        snr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(snr.isChecked()) {
                    cost = cost + 500;
                    price.setText("\u20B9"+String.valueOf(cost));
                    totalcost.setText("Total cost : \u20B9"+String.valueOf(cost));
                    amountpay.setText("Amout payable : \u20B9"+String.valueOf(cost));
                }else {
                    cost = cost - 500;
                    price.setText("\u20B9"+String.valueOf(cost));
                    totalcost.setText("Total cost : \u20B9"+String.valueOf(cost));
                    amountpay.setText("Amout payable : \u20B9"+String.valueOf(cost));
                }
            }
        });
        ap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ap.isChecked()) {
                    cost = cost + 300;
                    price.setText("\u20B9"+String.valueOf(cost));
                    totalcost.setText("Total cost : \u20B9"+String.valueOf(cost));
                    amountpay.setText("Amout payable : \u20B9"+String.valueOf(cost));
                }else {
                    cost = cost - 300;
                    price.setText("\u20B9"+String.valueOf(cost));
                    totalcost.setText("Total cost : \u20B9"+String.valueOf(cost));
                    amountpay.setText("Amout payable : \u20B9"+String.valueOf(cost));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void coupon0apply(){
        maxdiscount=1000;
        discountcost=(cost*50)/100;
        if(discountcost>maxdiscount) {
            cost = cost - discountcost + (discountcost - maxdiscount);
            amountpay.setText("Amout payable : \u20B9"+cost);
            price.setText("\u20B9"+cost);
        }
        else {
            cost = cost - discountcost;
            amountpay.setText("Amout payable : \u20B9"+cost);
            price.setText("\u20B9"+cost);
        }
    }

    public void coupon0remove(){
        cost = cost + discountcost;
        amountpay.setText("Amout payable : \u20B9"+cost);
        price.setText("\u20B9"+cost);
    }
    static class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FragmentViewPager.newInstance(R.drawable.coupon,0);
                case 1:
                    return FragmentViewPager.newInstance(R.drawable.couponreff,1);
//                case 2:
//                    return FragmentViewPager.newInstance(R.drawable);
                default:
                    return FragmentViewPager.newInstance(R.drawable.coupon,0);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private void setupPagerIndidcatorDots() {
        dots = new ImageView[2];
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.inactive_dot);
            //ivArrayDotsPager[i].setAlpha(0.4f);
//            dots[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    view.setAlpha(1);
//                }
//            });
            sliderDotspanel.addView(dots[i]);
            sliderDotspanel.bringToFront();
        }
    }

    public void sendNewOrderNotification(){
        final String FCM_API = "https://fcm.googleapis.com/fcm/send";
        final String serverKey = "key=" + "AAAAN6sk20I:APA91bFeWAW023u1794hvn_hnhtNWgpPJRPvc-Am859TeBW42cj98c2Q0Z9xdXnU3wTMoo5x2v66p_tPNsqcfb9GhtWkoNbp2FmdhZVACliqCcgFTxUtro5VQUlJusFMzxF2QSMZbn-P";
        final String contentType = "application/json";
        final String TAG = "NOTIFICATION TAG";
        String NOTIFICATION_TITLE;
        String NOTIFICATION_MESSAGE;
        String topic ="cNh_sAmeRROIa9VtYzI3JU:APA91bEkr2xRSt1NBJx86g4T65NYFnmzhVuVdNav4eAo3Gt2aGNYaKGdNMFKOrl0UIBCCUqfwspQdA854jkV4UuG8aE7Vbd5HO6Zp8O-hLOwdnxsRxsUJ3DlH1sEieBeLA3EL5SAH-Sg";
        String topic1 ="cdJ1i6y1RC2EmICc7Fo7VD:APA91bHS5jhTKNAeK5y31NijgMxEn8QhcTFFTatOIALAowUVw_ZM5RoL1InSUW0Gx35p6Vuc4liTOg1R9v8orPHDjENhik2hy3-qtRalXoXZ-47HUN1xgXyFeao0rL4jeLalwa3rZT_d";
        String topic2 ="d9yGQwRaQbO_D7c_9pEr7Z:APA91bHx_gT6_ipFn_-zx2xVwHawKzTOZJ3jZXOywre3avcj6HPAnC9TJRpuu9LUzWLpM27VoaTdXV_eSdrOQaasV3JRjCHifm6V2E01aPiTQ1GoAnRl6Gu08XFibk1Qb5uSJJ_h_Igd";

        Log.e("---token----", String.valueOf(topic));
        NOTIFICATION_TITLE = "Quick-fix";
        NOTIFICATION_MESSAGE = "Hey ADMIN, seems someone needs you";
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", topic);
            Log.e("done", "sourav");
//            notification.put("to", topic1);
//            Log.e("done", "me");
//            notification.put("to", topic2);
//            Log.e("done", "dhruboda");
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
                        Toast.makeText(MainActivity.this, "Request error", Toast.LENGTH_LONG).show();
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
}