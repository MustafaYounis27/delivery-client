package com.delevery.metyassara.ui.login.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.delevery.metyassara.R;
import com.delevery.metyassara.ui.home_page.HomePageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//
public class LoginFragment extends Fragment {
    // 1- define loginFragment View
    private View loginFragment;
    private TextView signUp;
    private EditText email, password;
    private ProgressDialog dialog;
    private Button login_Button;

    //2- override On Create view to inflate LoginFragment View

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(getActivity(), HomePageActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView
            (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //3- assign LoginFragment view with view that return From inflate object that need (View Layout , Root)
        // to attach with layout
        loginFragment = inflater.inflate(R.layout.login_fragment, null);
        return loginFragment;
    }

    //4- override On activity Created to inflate view in LoginActivity

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 5-initialize Fragment Views
        initViews();
    }

    private void initViews() {
        email = loginFragment.findViewById(R.id.email_field_login);
        password = loginFragment.findViewById(R.id.password_field_login);
        login_Button = loginFragment.findViewById(R.id.login);

        signUp = loginFragment.findViewById(R.id.signup_btn);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SignUpFragment()).addToBackStack(null).commit();

            }
        });

        //intial dialog
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("plese wait...");
        dialog.setTitle("Login");
        dialog.setCancelable(false);

        //login button
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String email_text = email.getText().toString();
        String password_text = password.getText().toString();

        //check if empty or no
        if (email_text.isEmpty()) {
            Toast.makeText(getContext(), "please enter email", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }
        if (password_text.isEmpty()) {
            Toast.makeText(getContext(), "please enter password", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return;
        }
        //her i make sure is not empty then i will make login auth
        dialog.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email_text, password_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Intent intent = new Intent(getActivity(), HomePageActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
