package com.client.metyassara.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.client.metyassara.R;
import com.client.metyassara.ui.login.Fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        //1- Load Login Fragment
        loadLoginFragment(new LoginFragment());
    }
    
    // Load Login Fragment Method to Load Login Fragment in Login activity
    // note that we user Fragment Class not LoginFragment
    //LoginFragment extend Fragment so Fragment object can be assigned to LoginFragment;
    private void loadLoginFragment(Fragment loginFragment) {
        getSupportFragmentManager()
                .beginTransaction().add(R.id.fragment_container,loginFragment)
                .disallowAddToBackStack().commit();
    
    }
    
}
