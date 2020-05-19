package com.client.metyassara.ui.home_page.HomeFragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.client.metyassara.R;

//this Fragment has FrameLayout to replace other Fragments
public class OrderFragment extends Fragment

{

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate ( R.layout.fragment_order, null );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {

        super.onActivityCreated ( savedInstanceState );
        IntialSharedPreferences();
        String InOrderRNno = preferences.getString("in order", "");
        if(InOrderRNno.equals("in order")){
            OrderTrackingFragment orderTrackingFragment=new OrderTrackingFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.order_container, orderTrackingFragment);
            fragmentTransaction.commit();
        }
        //HomeFragment is the first fragment will appear
      else {
          requireActivity ()
                    .getSupportFragmentManager ()
                    .beginTransaction ()
                    .add ( R.id.order_container,new HomeFragment () )
                    .commit ();
        }
    }
    private void IntialSharedPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
    }
}
