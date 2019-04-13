package com.nerds.easymeet.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nerds.easymeet.fragments.LoginFragment;
import com.nerds.easymeet.R;
import com.nerds.easymeet.fragments.SignUpFragment;
import com.nerds.easymeet.fragments.WelcomeFragment;

public class LauncherActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        firebaseAuth = FirebaseAuth.getInstance();
        changeFragment(new WelcomeFragment());
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        new Handler().postDelayed(() -> {
            if (currentUser != null) {
                runOnUiThread(() -> {
                    startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                    finish();
                });
            } else {
                runOnUiThread(this::showNoAccountFoundAlert);
            }
        }, 2000);


    }

    private void showNoAccountFoundAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.no_account_alert_layout, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        view.findViewById(R.id.sign_up_cv).setOnClickListener(v -> {
            alertDialog.dismiss();
            changeFragment(new SignUpFragment());
        });
        view.findViewById(R.id.log_in_cv).setOnClickListener(v -> {
            alertDialog.dismiss();
            changeFragment(new LoginFragment());
        });
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
