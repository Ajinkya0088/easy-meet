package com.nerds.easymeet;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private View view;
    private EditText mPasswordField, mEmailField;
    private CardView mLogInButton;
    private LinearLayout mProgressBarLayout;
    private TextView logInButtonTV;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences.Editor editor;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark));
        view = inflater.inflate(R.layout.fragment_login, container, false);
        initializeViews();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        mLogInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!validateForm()) {
            return;
        }

        logInButtonTV.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(
                mEmailField.getText().toString(), mPasswordField.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(
                                    LoginFragment.this.getContext(),
                                    LoginFragment.this.getString(R.string.succesfully_logged_in),
                                    Toast.LENGTH_LONG
                            ).show();
                            editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                            editor.putString(Constants.USER_EMAIL_ID, mEmailField.getText().toString());
                            editor.apply();
                            startActivity(new Intent(getContext(), MainActivity.class));
                            Objects.requireNonNull(getActivity()).finish();
                        } else {
                            Toast.makeText(
                                    LoginFragment.this.getContext(),
                                    LoginFragment.this.getString(R.string.login_failed),
                                    Toast.LENGTH_LONG
                            ).show();
                            mProgressBarLayout.setVisibility(View.GONE);
                            logInButtonTV.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void initializeViews() {
        mEmailField = view.findViewById(R.id.email_id);
        mPasswordField = view.findViewById(R.id.password);
        mProgressBarLayout = view.findViewById(R.id.button_progress_bar_layout);
        mLogInButton = view.findViewById(R.id.log_in_cv);
        logInButtonTV = view.findViewById(R.id.log_in_tv);
    }
}
