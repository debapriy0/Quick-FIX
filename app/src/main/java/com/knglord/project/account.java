package com.knglord.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class account extends AppCompatActivity{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    public ImageView profile,navlogo;
    TextView nvdevice,nvuname;
    private Button bt;
    public EditText uname,number,email,address;
    public DatabaseReference mref;
    String userid;
    public String emailval,nameval,addval,numberval;
    private static String TAG ;
    FirebaseUser xuser;
    Integer count =0,z=0;
    Boolean status = false;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        xuser = FirebaseAuth.getInstance().getCurrentUser();
        //mail = xuser.getEmail();
        mref = FirebaseDatabase.getInstance().getReference("users");
        bt=findViewById(R.id.btnupdate);
        uname=findViewById(R.id.editname);
        number=findViewById(R.id.editnumber);
        email=findViewById(R.id.editemail);
        address=findViewById(R.id.editaddress);
        //email.setText(xuser.getEmail());
        if(xuser!=null){
        userid = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        uploadimage();
        Glide.with(account.this).load(xuser.getPhotoUrl()).into(profile);
        if(xuser.getPhotoUrl()==null)
            Glide.with(account.this).load(R.drawable.default_female).into(profile);
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.dl);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View head = navigationView.getHeaderView(0);
        navlogo = head.findViewById(R.id.mkview);
        nvdevice = head.findViewById(R.id.udevice);
        nvuname = head.findViewById(R.id.uname);
        if(xuser != null)
            Glide.with(account.this).load(xuser.getPhotoUrl()).into(navlogo);
        //drawerLayout.clearFocus();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitle("My Account");
        setSupportActionBar(toolbar);
        toolbar.setCollapsible(false);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.getMenu().findItem(R.id.Admin).setVisible(false);
        nvdevice.setText(Build.MODEL);
        if(xuser==null){
            navigationView.getMenu().findItem(R.id.Account).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Myorder).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Pendingorder).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Myref).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Logout).setVisible(false);
        }
        if(xuser != null) {
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
            Pattern pat = Pattern.compile("\\b\\w+@\\b");
            Matcher mat = pat.matcher(xuser.getEmail());
            while (mat.find()){
                System.out.println("Match: " + mat.group());
                nvuname.setText(mat.group());
            }
        }
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
                        drawer.closeDrawers();
                        break;
                    case R.id.Myorder:
                        Intent myorderintent = new Intent(account.this,myorder.class);
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
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(0);
            }
        });
        show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    //Select profile picture
    void uploadimage() {
        profile=findViewById(R.id.profilepic);
        profile.setOnClickListener(new View.OnClickListener() {
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
    }
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(account.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(), account.this.getApplicationContext().getPackageName() + ".provider", f));
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
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),bitmapOptions);
                    profile.setImageBitmap(bitmap);
                    String path = f.getAbsolutePath();
                    Toast.makeText(getApplicationContext(),"Image captured",Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),path,Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),String.valueOf(f),Toast.LENGTH_LONG).show();
                    uploadpic(bitmap);
                    /*String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
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
            else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                profile.setImageBitmap(thumbnail);
                Toast.makeText(getApplicationContext(),"Image selected", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),picturePath, Toast.LENGTH_LONG).show();
                uploadpic(thumbnail);
            }

        }
    }
    //Uploading new data to the account

    private void show(){
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if(count==0)
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Log.e("dskey////", String.valueOf(ds.getKey()));
                    //Log.e("uid////",mref.getKey());
                    if (Objects.equals(ds.getKey(), xuser.getUid())) {
                        uname.setText(ds.child("name").getValue(String.class));
                        email.setText(ds.child("email").getValue(String.class));
                        number.setText(ds.child("number").getValue(String.class));
                        address.setText(ds.child("add").getValue(String.class));
                        Log.e("pass : ", String.valueOf(ds.child("pass").getValue()));
                        break;
                        //profile.setImageBitmap((Bitmap) ds.child("propic").getValue());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void uploadpic(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference().child("profilepic").child(userid+"JPEG");
        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getdownloadurl(reference);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getdownloadurl(StorageReference reference){
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        //Log.d(TAG, String.valueOf(user));
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
                        user.updateProfile(request)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                                        Glide.with(account.this).load(xuser.getPhotoUrl()).into(profile);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
    private void update(Integer x){
        z=x;
        addval= address.getText().toString();
        numberval= number.getText().toString();
        nameval= uname.getText().toString();
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if(z==0)
                for(DataSnapshot ds1 : dataSnapshot.getChildren()){
//                    Log.d(TAG, "key " + ds1.getKey());
                    if (Objects.equals(ds1.getKey(), xuser.getUid())) {
//                        Log.d(TAG, "name " + xuser.getEmail());
                        ds1.getRef().child("add").setValue(addval);
                        ds1.getRef().child("name").setValue(nameval);
                        ds1.getRef().child("number").setValue(numberval);
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.account_update_toast, (ViewGroup) findViewById(R.id.success_toast));
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 150);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                        count = 1;
                        z++;
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //Do your work.
                    selectImage();
                } else {
                    Toast.makeText(this, "Until you grant the permission, You won't be able to upload images", Toast.LENGTH_LONG).show();
                }
            }
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