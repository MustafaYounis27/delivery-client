package com.client.metyassara.ui.home_page.HomeFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.client.metyassara.R;

//this Fragment has FrameLayout to replace other Fragments
public class OrderFragment extends Fragment
{

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

        //HomeFragment is the first fragment will appear
        requireActivity ()
                .getSupportFragmentManager ()
                .beginTransaction ()
                .add ( R.id.order_container,new HomeFragment () )
                .commit ();
    }

}
