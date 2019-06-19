package com.uowfyp.masterhouse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    //Fragment {

    private static final String TAG = "HomeActivity";

    RecyclerView postList;
    ArrayList<Post> list;
    ProgressBar loading;
    Post post = new Post();
    Post post2 = new Post();

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) { // onclick function to pass the key to the single view activity
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();

            post = list.get(position);
            Intent intent = new Intent(HomeActivity.this, PostDetailActivity.class);
            intent.putExtra("key",post.getKey()); // get the post key from Firebase database and pass the key
            startActivity(intent);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.activity_home, container, false);
//        postList = (RecyclerView)rootView.findViewById(R.id.postList);
//        loading = (ProgressBar)rootView.findViewById(R.id.loading3);
//        postList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        postList.setHasFixedSize(true);
//        setHasOptionsMenu(true);

        postList = (RecyclerView) findViewById(R.id.postList);
        loading = (ProgressBar) findViewById(R.id.loading3);
        postList.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        postList.setHasFixedSize(true);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Query dbReff = FirebaseDatabase.getInstance().getReference("Posts"); // order the data by int
        final DatabaseReference dbreff2 = FirebaseDatabase.getInstance().getReference("Users");

        if (dbReff != null) {
            loading.setVisibility(View.VISIBLE);
            list = new ArrayList<Post>();
            dbReff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {


                        for (final DataSnapshot dsnap : dataSnapshot.getChildren()) {
                            post2 = dsnap.getValue(Post.class);
                            post2.setKey(dsnap.getKey().toString());
                            list.add(post2);
                        }
                        loading.setVisibility(View.GONE);
                        RecycleViewAdapter adapter = new RecycleViewAdapter(list);
                        postList.setAdapter(adapter);
                        adapter.setOnItemClickListener(onClickListener);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }


    }


    private void search(String keyword) {
        ArrayList<Post> searchList = new ArrayList<>();
        for (Post object : list) {
            if (object.getDescription().toLowerCase().contains(keyword.toLowerCase()) || object.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                searchList.add(object);
            }
        }
        RecycleViewAdapter adapter = new RecycleViewAdapter(searchList);
        postList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        inflater.inflate(R.menu.menu_search, menu);  //for Fragment function
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.searchBar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                return true;
            }
        });
//         super.onCreateOptionsMenu(menu, inflater);
        super.onCreateOptionsMenu(menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(HomeActivity.this, CreatePostActivity.class));
                break;
            case R.id.menuProfile:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                    return true;
            }
            return false;
        }
    };

}
