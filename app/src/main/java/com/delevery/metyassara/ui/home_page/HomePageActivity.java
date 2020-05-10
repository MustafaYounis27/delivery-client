package com.delevery.metyassara.ui.home_page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.delevery.metyassara.R;
import com.google.android.material.navigation.NavigationView;

public class HomePageActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawer ();

    }

    private void setupDrawer()
    {
        DrawerLayout drawerLayout = findViewById ( R.id.drawer_layout );

        NavController navController = Navigation.findNavController ( this, R.id.nav_host_fragment );
        NavigationView navView = findViewById ( R.id.nav_view );
        NavigationUI.setupWithNavController ( navView, navController );

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder ( navController.getGraph () )
                        .setDrawerLayout ( drawerLayout )
                        .build ();
    }
}
