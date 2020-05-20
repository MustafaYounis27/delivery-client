package com.client.metyassara.ui.home_page.HomeFragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.client.metyassara.R;

public class HomeFragment extends Fragment
{
    private View homeFragment;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ImageView userSetting;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeFragment=inflater.inflate ( R.layout.fragment_home,null );
        return homeFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated ( savedInstanceState );
        IntialSharedPreferences();
        initViews();
        GoToVerficationOrNo();
    }

    private void GoToVerficationOrNo() {
        // هنا جبت اللي متسيف وعملت اتشك لو جاي من الماب يعملي ريبلاس ببفراجمن بتاع verfication
        String GoToVerficationFragmentOrno= preferences.getString("go to vericatio",null);
       if(GoToVerficationFragmentOrno!=null&&GoToVerficationFragmentOrno.equals("yes")){
           // واول لما دخل عشان يعمب ريبلاس خليته سيف داتا تانيه غير اللي بتشك بيها عشان مايفضلش متسيف ةيعمل ريبلاس ويبقي ده اول فراجمنت
           editor.putString("go to vericatio", "no");
           editor.commit();
           userSetting.setVisibility ( View.GONE );
           VerificationOrderFragment verificationOrderFragment=new VerificationOrderFragment();
           FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
           FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
           fragmentTransaction.replace(R.id.order_container, verificationOrderFragment);
           fragmentTransaction.commit();
       }
    }

    private void initViews()
    {
        //to visible image icon on screen
         userSetting = requireActivity ().findViewById ( R.id.user_setting );
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

    private void IntialSharedPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
    }
}
