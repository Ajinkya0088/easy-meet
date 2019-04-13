package com.nerds.easymeet.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nerds.easymeet.data.Constants;
import com.nerds.easymeet.R;
import com.nerds.easymeet.data.User;
import com.nerds.easymeet.activities.MainActivity;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    private View view;
    private EditText mPasswordField, mEmailField, mUserNameField;
    private CardView mSignUpButton;
    private LinearLayout mProgressBarLayout;
    private TextView signUpButtonTV;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPref;
    private String email, password, name;
    private String userId;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark));
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
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

        name = mUserNameField.getText().toString();
        email = mEmailField.getText().toString();
        password = mPasswordField.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(
                                SignUpFragment.this.getContext(),
                                SignUpFragment.this.getString(R.string.account_created),
                                Toast.LENGTH_LONG
                        ).show();
                        User user = new User("", name, email);
                        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                                .add(user)
                                .addOnSuccessListener(documentReference -> {
                                    userId = documentReference.getId();
                                    FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                                            .document(userId)
                                            .update("id", userId);
                                    SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
                                    sharedPrefEditor.putString(Constants.USER_ID, userId);
                                    sharedPrefEditor.putString(Constants.USER_EMAIL_ID, email);
                                    sharedPrefEditor.putString(Constants.USER_NAME, name);
                                    sharedPrefEditor.apply();
                                    startActivity(new Intent(SignUpFragment.this.getContext(), MainActivity.class));
                                    Objects.requireNonNull(SignUpFragment.this.getActivity()).finish();
                                });

                    } else {
                        Toast.makeText(
                                SignUpFragment.this.getContext(),
                                SignUpFragment.this.getString(R.string.account_creation_failed),
                                Toast.LENGTH_LONG
                        ).show();
                        mProgressBarLayout.setVisibility(View.GONE);
                        signUpButtonTV.setVisibility(View.VISIBLE);
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
