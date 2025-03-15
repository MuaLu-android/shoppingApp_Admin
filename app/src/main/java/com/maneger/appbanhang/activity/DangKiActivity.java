package com.maneger.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maneger.appbanhang.R;
import com.maneger.appbanhang.retrofit.ApiBanHang;
import com.maneger.appbanhang.retrofit.RetrofitClient;
import com.maneger.appbanhang.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {
    EditText email, pass, repass, username, mobile;
    Button btndangki;
    ApiBanHang apiBanHang;
    FirebaseAuth firebaseAuth;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ki2);
        intView();
        intControll();
    }

    private void intControll() {
        btndangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangki();
            }
        });
    }

    private void dangki() {
        String tr_email = email.getText().toString().trim();
        String tr_pass = pass.getText().toString().trim();
        String tr_repass = repass.getText().toString().trim();
        String tr_username = username.getText().toString().trim();
        String tr_mobile = mobile.getText().toString().trim();
        if (TextUtils.isEmpty(tr_email)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhâp email", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(tr_pass)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhâp pass", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(tr_repass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhâp repass", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(tr_mobile)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhâp Mobile", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(tr_username)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhâp Username", Toast.LENGTH_SHORT).show();
        }else{
            if(tr_pass.equals(tr_repass)){
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(tr_email, tr_pass)
                                .addOnCompleteListener(DangKiActivity.this, new OnCompleteListener<AuthResult>(){
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            if (user != null){
                                                postData(tr_email, tr_pass, tr_username, tr_mobile, user.getUid());
                                            }
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
            }else {
                Toast.makeText(getApplicationContext(), "Pass chưa khớp", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void postData(String tr_email, String tr_pass, String tr_username, String tr_mobile, String uid) {
        //post data
        compositeDisposable.add(apiBanHang.dangki(tr_email, tr_pass, tr_username, tr_mobile, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_SHORT).show();
                                Utils.user_current.setEmail(tr_email);
                                Utils.user_current.setPass(tr_pass);
                                Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }


    private void intView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
        mobile = findViewById(R.id.mobile);
        username = findViewById(R.id.username);
        btndangki = findViewById(R.id.btndangki);
    }


    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}