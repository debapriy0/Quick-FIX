package com.knglord.project;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registration extends AppCompatActivity {
    EditText name,phone,email,Password,confirmPassword;
    Button register;
    private FirebaseAuth mAuth;
    public String userId;
    public DatabaseReference mref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        if(Build.VERSION.SDK_INT >= 21){
            Window w =this.getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.setStatusBarColor(this.getResources().getColor(R.color.splash));
        }
        mAuth = FirebaseAuth.getInstance();
        name=(EditText)findViewById(R.id.txtName);
        phone=(EditText)findViewById(R.id.txtPhone);
        email=(EditText)findViewById(R.id.txtuser);
        Password=(EditText)findViewById(R.id.txtPass);
        confirmPassword=(EditText)findViewById(R.id.txtConfirmPass);
        register=(Button)findViewById(R.id.btnRegister);
        mref = FirebaseDatabase.getInstance().getReference("users");
        reg();
    }
    void reg()
    {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String s1 = name.getText().toString();
                final String s2 = phone.getText().toString();
                final String s3 = email.getText().toString();
                final String s4= Password.getText().toString();
                String s5= confirmPassword.getText().toString();
                if (TextUtils.isEmpty(s1)) {
                    Toast.makeText(registration.this, "Please enter profile name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(s2)) {
                    Toast.makeText(registration.this, "Please enter phone number", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(s3)) {
                    Toast.makeText(registration.this, "Please enter email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(s4)) {
                    Toast.makeText(registration.this, "Please enter password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(s5)) {
                    Toast.makeText(registration.this, "Please enter confirm password", Toast.LENGTH_LONG).show();
                    return;
                }
                    if (s4.equals(s5)) {

                        mAuth.createUserWithEmailAndPassword(s3, s4).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (!task.isSuccessful())
                                {
                                    try
                                    {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException weakPassword)
                                    {
                                        Toast.makeText(registration.this, "password not strong", Toast.LENGTH_LONG).show();
                                    } catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                    {
                                        Toast.makeText(registration.this,malformedEmail.getMessage(), Toast.LENGTH_LONG).show();
                                    } catch (FirebaseAuthUserCollisionException existEmail)
                                    {
                                        Toast.makeText(registration.this, "Email already exist", Toast.LENGTH_LONG).show();
                                    }
                                    catch (Exception e)
                                    {
                                        Toast.makeText(registration.this,e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                                    if (TextUtils.isEmpty(userId)) {
                                        createuser(s1,s2,s3,s4);
                                    }
                                    Intent intent = new Intent(getApplicationContext(),login.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                                }
                            }
                            void createuser(String s1, String s2, String s3, String s4) {
                                if (TextUtils.isEmpty(userId)) {
                                    userId = mref.push().getKey();
                                }
                                FirebaseUser xuser = FirebaseAuth.getInstance().getCurrentUser();
                                xuser.getUid();
                                //Log.e("xuser  ",xuser.getUid());
                                user nuser = new user(s1,s2,s3,s4,"","");
                                mref.child(xuser.getUid()).setValue(nuser);
                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "password did not match!", Toast.LENGTH_LONG).show();
                    }
                }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),login.class));
        overridePendingTransition(R.anim.back_transition, R.anim.background);
        this.finish();
    }
}
