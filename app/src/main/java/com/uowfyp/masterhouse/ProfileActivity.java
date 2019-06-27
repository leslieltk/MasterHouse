package com.uowfyp.masterhouse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends Fragment {

    private static final String TAG = "Profile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";

    TextView tvEmail, tvName, tvDate;
    Button btnlogout;
    ProgressBar loading;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference dbreff, likereff, postReff;
    private ViewPager mViewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        Fragment UserLike = new UserLike();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(R.id.viewpager, UserLike).commit();

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        auth = FirebaseAuth.getInstance();
        tvEmail = (TextView) rootView.findViewById(R.id.tvemail);
        tvName = (TextView) rootView.findViewById(R.id.tvname);
        btnlogout = (Button) rootView.findViewById(R.id.btnlogout);
        loading = (ProgressBar) rootView.findViewById(R.id.loading2);
        tvDate = (TextView) rootView.findViewById(R.id.tvdate);

        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.result_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        FirebaseUser authuser = FirebaseAuth.getInstance().getCurrentUser();
        if (authuser != null) {
            loading.setVisibility(View.VISIBLE);
            String email = authuser.getEmail();
            String uid = authuser.getUid();
            dbreff = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            dbreff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String firstName = dataSnapshot.child("firstName").getValue().toString();
                    String lastName = dataSnapshot.child("lastName").getValue().toString();
                    firstName = firstName.toUpperCase();
                    lastName = lastName.toUpperCase();
                    tvName.setText(firstName + " " + lastName);
                    tvName.setTextSize(20);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            boolean emailVerified = authuser.isEmailVerified();
            tvEmail.setText(email);
            loading.setVisibility(View.GONE);

        }


        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        return rootView;

    }

    private void setupViewPager(ViewPager viewPager) {


        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new UserLike(), "Your Like");
        adapter.addFragment(new HomeActivity(), "Your Post");
        adapter.addFragment(new UserApplied(), "Your Applied");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
