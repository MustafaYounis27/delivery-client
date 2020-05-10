package com.delevery.metyassara.ui.login.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.delevery.metyassara.R;

public class SignUpFragment extends Fragment {
    // 1- define loginFragment View
    private View signupFragment;
    
    //2- override On Create view to inflate signupFragment View
    @Nullable
    @Override
    public View onCreateView
    (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //3- assign signupFragment view with view that return From inflate object that need (View Layout , Root)
        // to attach with layout
        signupFragment = inflater.inflate(R.layout.login_fragment,null);
        return signupFragment;
    }
    //4- override On activity Created to inflate view in LoginActivity
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
    }
}
