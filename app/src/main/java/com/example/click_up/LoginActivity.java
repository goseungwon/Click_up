package com.example.click_up;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edtLoginID, edtLoginPassword;
    private Button btnMainRegister, btnMainLogin;
    private long time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edtLoginID = findViewById(R.id.edtLoginID);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);

        findViewById(R.id.btnMainRegister).setOnClickListener(onClickListener);
        findViewById(R.id.btnMainLogin).setOnClickListener(onClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnMainRegister:
                    startActivity(RegisterActivity.class);
                    break;

                case R.id.btnMainLogin:
                    login();
                    break;
            }
        }
    };

    private void login() {
        String userID = edtLoginID.getText().toString().trim();
        String userPassword = edtLoginPassword.getText().toString().trim();

        if (userID.length() > 0 && userPassword.length() > 0) {
            mAuth.signInWithEmailAndPassword(userID, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                startToast("???????????? ?????????????????????.");
                                startActivity(MainActivity.class);
                            } else {
                                if (task.getException() != null) {
                                    startToast("????????? ?????? ??????????????? ???????????? ????????????.");
                                    updateUI(null);
                                }
                            }
                        }
                    });
        } else {
            startToast("????????? ?????? ??????????????? ????????? ?????????.");
        }
    }

    private void updateUI(FirebaseUser user) {
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "???????????? ????????? ?????? ??? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() - time < 2000) {
            moveTaskToBack(true);
            finishAndRemoveTask();
        }
    }
}


