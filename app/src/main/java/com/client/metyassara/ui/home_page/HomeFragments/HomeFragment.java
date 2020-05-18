package com.client.metyassara.ui.home_page.HomeFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.client.metyassara.R;

public class HomeFragment extends Fragment
{
    private View homeFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeFragment=inflater.inflate ( R.layout.fragment_home,null );
        return homeFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated ( savedInstanceState );

        initViews();
    }

    private void initViews()
    {
        //to visible image icon on screen
        final ImageView userSetting = requireActivity ().findViewById ( R.id.user_setting );
        userSetting.setVisibility ( View.VISIBLE );

        //when click orderNow will replace RestaurantFragment instead of HomeFragment
        TextView orderNow = homeFragment.findViewById ( R.id.order_now );
        orderNow.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                loadFragment();

                userSetting.setVisibility ( View.GONE );
            }
        } );
    }

    private void loadFragment()
    {
        requireActivity ()
                .getSupportFragmentManager ()
                .beginTransaction ()
                .replace ( R.id.order_container,new RestaurantsFragment () )
                .addToBackStack ( null )
                .commit ();
    }
}
