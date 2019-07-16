package com.uowfyp.masterhouse;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new HomeActivity()).commit();
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fm = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm = new HomeActivity();
                    break;
                case R.id.navigation_dashboard:
                    fm = new favouriteActivity();
                    break;
                case R.id.navigation_Profile:
                    fm = new ProfileActivity();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    fm).commit();
            return true;
        }
    };
}
