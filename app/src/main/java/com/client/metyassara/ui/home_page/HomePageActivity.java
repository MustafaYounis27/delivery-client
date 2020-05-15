package com.client.metyassara.ui.home_page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.client.metyassara.R;
import com.google.android.material.navigation.NavigationView;

public class HomePageActivity extends AppCompatActivity
{

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawer ();

    }

    private void setupDrawer()
    {
        drawerLayout = findViewById ( R.id.drawer_layout );

        NavController navController = Navigation.findNavController ( this, R.id.nav_host_fragment );
        NavigationView navView = findViewById ( R.id.nav_view );
        NavigationUI.setupWithNavController ( navView, navController );

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder ( navController.getGraph () )
                        .setDrawerLayout ( drawerLayout )
                        .build ();
    }

    // onClick with imageView to open drawerLayout
    public void openDrawer(View view)
    {
        drawerLayout.open ();
    }
}
