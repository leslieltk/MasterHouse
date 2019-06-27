package com.uowfyp.masterhouse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserApplied extends Fragment {

    User user;
    Post post = new Post();
    Post post2 = new Post();
    RecyclerView postList;
    ArrayList<Post> list;
    DatabaseReference postReff;
    ProgressBar loading;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_detail, container, false);

        postList = (RecyclerView)rootView.findViewById(R.id.postList);
        postList.setLayoutManager(new LinearLayoutManager(getActivity()));
        loading = (ProgressBar)rootView.findViewById(R.id.loading2);
        user = new User();
        postList.setHasFixedSize(true);

        auth = FirebaseAuth.getInstance();

        postReff = FirebaseDatabase.getInstance().getReference();
        if (postReff.child("Posts")!= null){
            list = new ArrayList<Post>();
            postReff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DataSnapshot dspost = dataSnapshot.child("Posts");
                    DataSnapshot dslike = dataSnapshot.child("Users").child(auth.getCurrentUser().getUid()).child("applied");
                    DataSnapshot userdata = dataSnapshot.child("Users");

                    if (dataSnapshot.exists()) {
                        for (final DataSnapshot dsnap : dslike.getChildren()) {
                            String postKey = dsnap.getKey();
                            for (final DataSnapshot dsnap2 : dspost.getChildren()) {
                                if (dsnap2.getKey().equals(postKey)) {
                                    post2 = dsnap2.getValue(Post.class);
                                    post2.setKey(dsnap.getKey().toString());
                                    for (DataSnapshot dsnap3: userdata.getChildren()){
                                        if (dsnap2.child("uid").getValue().toString().equals(dsnap3.getKey())){
                                            post2.setUsername(dsnap3.child("firstName").getValue().toString());
                                        }
                                    }
                                    list.add(post2);
                                }
                            }
                        }
//                        loading.setVisibility(View.GONE);
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) { // onclick function to pass the key to the single view activity
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();

            post = list.get(position);
            Intent intent = new Intent(getActivity(), PostDetailActivity.class);
            intent.putExtra("key",post.getKey()); // get the post key from Firebase database and pass the key
            startActivity(intent);
        }
    };
}
