package com.delevery.metyassara.ui.login.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.delevery.metyassara.R;
//
public class LoginFragment extends Fragment {
    // 1- define loginFragment View
    private View loginFragment;
    TextView signUp;
    
    //2- override On Create view to inflate LoginFragment View
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
        signUp = loginFragment.findViewById(R.id.signup_btn);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,new SignUpFragment()).addToBackStack(null).commit();
        
            }
        });
    }
}
