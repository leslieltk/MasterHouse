package com.uowfyp.masterhouse;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends Fragment {

    RecyclerView mRecyclerView;
    ArrayList<MissionPost> postlist;
    ArrayList<User> userList;
    ProgressBar loading;
    MissionPost missionPost = new MissionPost();
    MissionPost missionPost2 = new MissionPost();
    RecycleViewAdapter adapter;
    boolean isScrolling = false;
    LinearLayoutManager mManager;
    int currentItem, totoalItem, scrollOutItem;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();

            missionPost = postlist.get(position);
            Intent intent = new Intent(getActivity(), PostDetailActivity.class);
            intent.putExtra("postKey", missionPost.getKey());
            startActivity(intent);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.postList);
        loading = (ProgressBar)rootView.findViewById(R.id.loading3);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setHasFixedSize(true);

        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.homebar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle("");

        Query dbReff = FirebaseDatabase.getInstance().getReference(); // order the data by int

        if (dbReff != null) {
            loading.setVisibility(View.VISIBLE);
            postlist = new ArrayList<MissionPost>();

            dbReff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DataSnapshot postdata = dataSnapshot.child("Posts");
                    DataSnapshot userdata = dataSnapshot.child("Users");

                    if (postdata.exists()) {

                        for (DataSnapshot dsnap : postdata.getChildren()) {
                            missionPost2 = dsnap.getValue(MissionPost.class);
                            missionPost2.setKey(dsnap.getKey().toString());

                            for (DataSnapshot dsnap2: userdata.getChildren()){

                                if (dsnap.child("uid").getValue().toString().equals(dsnap2.getKey())){
                                    missionPost2.setUsername(dsnap2.child("firstName").getValue().toString());
                                }
                            }
                            postlist.add(missionPost2);
                        }
                        loading.setVisibility(View.GONE);
                        adapter = new RecycleViewAdapter(postlist);
                        mRecyclerView.setAdapter(adapter);
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
                startActivity(new Intent(getActivity(), CreateMissionActivity.class));
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
        ArrayList<MissionPost> searchList = new ArrayList<>();
        for (MissionPost object : postlist) {
            if (object.getDescription().toLowerCase().contains(keyword.toLowerCase()) || object.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                searchList.add(object);
            }
        }
        RecycleViewAdapter adapter = new RecycleViewAdapter(searchList);
        mRecyclerView.setAdapter(adapter);
    }

}
