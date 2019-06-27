package com.uowfyp.masterhouse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends Fragment {
    //Fragment {

    private static final String TAG = "HomeActivity>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
    private static final String TAG2 = "HomeActivity22>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";

    RecyclerView postList;
    ArrayList<Post> list;
    ProgressBar loading;
    Post post = new Post();
    Post post2 = new Post();
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();

            post = list.get(position);
            Intent intent = new Intent(getActivity(), PostDetailActivity.class);
            intent.putExtra("key",post.getKey());
            startActivity(intent);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        postList = (RecyclerView)rootView.findViewById(R.id.postList);
        loading = (ProgressBar)rootView.findViewById(R.id.loading3);
        postList.setLayoutManager(new LinearLayoutManager(getActivity()));
        postList.setHasFixedSize(true);

        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.homebar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        getActivity().setTitle("");

        Query dbReff = FirebaseDatabase.getInstance().getReference(); // order the data by int
        final DatabaseReference dbreff2 = FirebaseDatabase.getInstance().getReference("Users");

        if (dbReff != null) {
            loading.setVisibility(View.VISIBLE);
            list = new ArrayList<Post>();
            dbReff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DataSnapshot postdata = dataSnapshot.child("Posts");
                    DataSnapshot userdata = dataSnapshot.child("Users");
                    if (postdata.exists()) {
                        for (DataSnapshot dsnap : postdata.getChildren()) {
                            post2 = dsnap.getValue(Post.class);
                            post2.setKey(dsnap.getKey().toString());
                            for (DataSnapshot dsnap2: userdata.getChildren()){
                                if (dsnap.child("uid").getValue().toString().equals(dsnap2.getKey())){
                                    post2.setUsername(dsnap2.child("firstName").getValue().toString());
                                }
                            }
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
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
//        getMenuInflater().inflate(R.menu.menu_main, menu);
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
         super.onCreateOptionsMenu(menu, inflater);
//        super.onCreateOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getActivity(), CreatePostActivity.class));
                break;
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

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


}
