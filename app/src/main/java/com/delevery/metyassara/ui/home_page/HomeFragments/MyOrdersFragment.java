package com.delevery.metyassara.ui.home_page.HomeFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.delevery.metyassara.R;

public class MyOrdersFragment extends Fragment
{
    private View myOrdersFragment;
    private ImageView userSetting;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        myOrdersFragment=inflater.inflate ( R.layout.fragment_my_orders,null );
        return myOrdersFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        initViews();

    }

    private void initViews()
    {
        //to gone visible image icon
        userSetting=getActivity ().findViewById ( R.id.user_setting );
        userSetting.setVisibility ( View.GONE );

    }
}
