package com.knglord.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class myref extends AppCompatActivity{
    TextView nvdevice,nvuname,sharecode;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    public ImageView navlogo;
    Button claim;
    LinearLayout pop;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myref);
        drawerLayout = (DrawerLayout) findViewById(R.id.dl);
        sharecode = findViewById(R.id.sharecode);
        claim = findViewById(R.id.claim);
        pop = findViewById(R.id.pop);
        pop.setVisibility(View.INVISIBLE);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //drawerLayout.clearFocus();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitle("Share and Discount");
        setSupportActionBar(toolbar);
        toolbar.setCollapsible(false);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.getMenu().findItem(R.id.Admin).setVisible(false);
        FirebaseUser xuser = FirebaseAuth.getInstance().getCurrentUser();
        if(xuser==null){
            navigationView.getMenu().findItem(R.id.Account).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Myorder).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Pendingorder).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Myref).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Logout).setVisible(false);
        }
        if(xuser != null) {
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
            View head = navigationView.getHeaderView(0);
            navlogo = head.findViewById(R.id.mkview);
            nvdevice = head.findViewById(R.id.udevice);
            nvuname = head.findViewById(R.id.uname);
            nvdevice.setText(Build.MODEL);
            Glide.with(myref.this).load(xuser.getPhotoUrl()).into(navlogo);
            Pattern pat = Pattern.compile("\\b\\w+@\\b");
            Matcher mat = pat.matcher(xuser.getEmail());
            while (mat.find()){
                System.out.println("Match: " + mat.group());
                nvuname.setText(mat.group());
            }
        }
        sharecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareapp();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id= menuItem.getItemId();
                DrawerLayout drawer = findViewById(R.id.dl);
                switch (id) {
                    case R.id.Home:
                        Intent homeintent = new Intent(getApplicationContext(),MainActivity.class);
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
                        Intent myorderintent = new Intent(getApplicationContext(),myorder.class);
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
                        drawer.closeDrawers();
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

        claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.setVisibility(View.VISIBLE);
                pop.animate().alpha(1).setDuration(1000);
                claim.animate().alpha(0).setDuration(1000);
                claim.setVisibility(View.INVISIBLE);
                //pop.animate().translationY(100).setDuration(600).setStartDelay(300);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        if (item.getItemId() == R.id.share) {
            try {
                shareAPK();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refer_menu, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        overridePendingTransition(R.anim.back_transition, R.anim.background);
        this.finish();
    }

    private  void shareAPK() throws PackageManager.NameNotFoundException {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ApplicationInfo packageinfo = getApplicationContext().getPackageManager().getApplicationInfo(getPackageName(), 0);
        File file = new File(packageinfo.publicSourceDir);
        try {
            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            tempFile = new File(tempFile.getPath() + "/" + "Quick-FIX" + ".apk");
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            InputStream in = new FileInputStream(file);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/vnd.android.package-archive");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            startActivity(Intent.createChooser(intent, "Share App Via"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void shareapp(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My App Name");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Hey check out Quick-FIX at: https://projectandroapps.000webhostapp.com/");
        startActivity(Intent.createChooser(sharingIntent, "Share app via"));
    }
}
