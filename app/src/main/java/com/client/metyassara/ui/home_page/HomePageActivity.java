package com.client.metyassara.ui.home_page;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.client.metyassara.R;
import com.client.metyassara.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomePageActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupDrawer();
    }

    private void setupDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationView navView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navView, navController);
        navView.getMenu().findItem(R.id.facebook)
                .setOnMenuItemClickListener
                        (new MenuItem.OnMenuItemClickListener() {
                             @Override
                             public boolean onMenuItemClick(MenuItem menuItem) {
                                 String YourPageURL = "https://www.facebook.com/groups/2276404602661722";
                                 Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                                 startActivity(browserIntent);
                                 drawerLayout.closeDrawer(GravityCompat.START);
                                 return false;
                             }
                         }
                        );

        navView.getMenu().findItem(R.id.logout)
                .setOnMenuItemClickListener
                        (new MenuItem.OnMenuItemClickListener() {
                             @Override
                             public boolean onMenuItemClick(MenuItem menuItem) {
                                 FirebaseAuth.getInstance().signOut();
                                 startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                 finish();
                                 return false;
                             }
                         }
                        );
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(drawerLayout)
                        .build();
    }

    // onClick with imageView to open drawerLayout
    public void openDrawer(View view) {
        drawerLayout.open();
    }
}
