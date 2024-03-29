package com.uowfyp.masterhouse;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class UserPost extends Fragment {

    User user = new User();
    MissionPost missionPost = new MissionPost();
    RecyclerView postList;
    ArrayList<MissionPost> list;
    DatabaseReference postReff;
    ProgressBar loading;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_detail, container, false);

        postList = (RecyclerView)rootView.findViewById(R.id.postList);
        postList.setLayoutManager(new LinearLayoutManager(getActivity()));
        loading = (ProgressBar)rootView.findViewById(R.id.loading3);
        postList.setHasFixedSize(true);

        auth = FirebaseAuth.getInstance();

        postReff = FirebaseDatabase.getInstance().getReference();

        if (postReff.child("Posts")!= null){
            list = new ArrayList<MissionPost>();
            postReff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DataSnapshot dspost = dataSnapshot.child("Posts");
                    DataSnapshot dsuserpost = dataSnapshot.child("Users").child(auth.getCurrentUser().getUid()).child("posts");
                    DataSnapshot userdata = dataSnapshot.child("Users");

                    if (dataSnapshot.exists()) {
                        for (final DataSnapshot dsnapuserpost : dsuserpost.getChildren()) {
                            String postKey = dsnapuserpost.getKey();

                            for (final DataSnapshot dsnappost : dspost.getChildren()) {

                                if (dsnappost.getKey().equals(postKey)) {
                                    missionPost = dsnappost.getValue(MissionPost.class);
                                    missionPost.setKey(dsnapuserpost.getKey().toString());

                                    for (DataSnapshot dsnap3: userdata.getChildren()){

                                        if (dsnappost.child("uid").getValue().toString().equals(dsnap3.getKey())){
                                            missionPost.setUsername(dsnap3.child("firstName").getValue().toString());
                                        }
                                        if (userdata.child(auth.getCurrentUser().getUid()).child("likes").hasChild(dsnappost.getKey())){
                                            missionPost.setIslike(true);
                                        }
                                    }
                                    list.add(missionPost);
                                }
                            }
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) { // onclick function to pass the key to the single view activity
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();

            missionPost = list.get(position);
            Intent intent = new Intent(getActivity(), PostDetailActivity.class);
            intent.putExtra("postKey", missionPost.getKey()); // get the missionPost key from Firebase database and pass the key
            startActivity(intent);
        }
    };
}
