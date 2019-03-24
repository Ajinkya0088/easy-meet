package com.nerds.easymeet;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    View view;
    EditText mPasswordField, mEmailField, mUserNameField;
    CardView mSignUpButton;
    LinearLayout mProgressBarLayout;
    TextView signUpButtonTV;
    private FirebaseAuth firebaseAuth;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark));
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initializeViews();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        mSignUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!validateForm()) {
            return;
        }

        signUpButtonTV.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(
                mEmailField.getText().toString(), mPasswordField.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(
                                    SignUpFragment.this.getContext(),
                                    SignUpFragment.this.getString(R.string.account_created),
                                    Toast.LENGTH_LONG
                            ).show();


                            startActivity(new Intent(SignUpFragment.this.getContext(), MainActivity.class));
                            SignUpFragment.this.getActivity().finish();
                        } else {
                            Toast.makeText(
                                    SignUpFragment.this.getContext(),
                                    SignUpFragment.this.getString(R.string.account_creation_failed),
                                    Toast.LENGTH_LONG
                            ).show();
                            mProgressBarLayout.setVisibility(View.GONE);
                            signUpButtonTV.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String userName = mUserNameField.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            mUserNameField.setError("Required.");
            valid = false;
        } else {
            mUserNameField.setError(null);
        }

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
        mUserNameField = view.findViewById(R.id.user_name);
        mProgressBarLayout = view.findViewById(R.id.button_progress_bar_layout);
        mSignUpButton = view.findViewById(R.id.sign_up_cv);
        signUpButtonTV = view.findViewById(R.id.signup_button_tv);
    }
}
