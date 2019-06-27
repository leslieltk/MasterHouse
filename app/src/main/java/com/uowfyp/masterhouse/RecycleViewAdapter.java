package com.uowfyp.masterhouse;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.devs.readmoreoption.ReadMoreOption;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.viewHolder> {
    private static final String TAG = "HomeActivity";
    ArrayList<Post> list;
    Post post = new Post();
    Context mcontext;
    View.OnClickListener mOnItemClickListener;
    DatabaseReference dreff;

    public RecycleViewAdapter( ArrayList<Post> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        mcontext = viewGroup.getContext();
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, final int position) {
        viewHolder.postUsername.setText(list.get(position).getUsername());
        viewHolder.postTitle.setText(list.get(position).getTitle());
        viewHolder.postDesc.setText(list.get(position).getDescription());
        viewHolder.postSubTitle.setText((list.get(position).getUid()));
        ReadMoreOption readMoreOption = new ReadMoreOption.Builder(mcontext)
                .moreLabel("Show More")
                .lessLabel("Show Less")
                .moreLabelColor(Color.RED)
                .lessLabelColor(Color.BLUE)
                .build();
        readMoreOption.addReadMoreTo(viewHolder.postDesc,list.get(position).getDescription());

        ReadMoreOption readMoreOption2 = new ReadMoreOption.Builder(mcontext)
                .textLength(34)
                .moreLabel("")
                .build();
        readMoreOption2.addReadMoreTo(viewHolder.postTitle,list.get(position).getTitle());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    class viewHolder extends RecyclerView.ViewHolder {
        CardView parentLayout;
        TextView postTitle, postDesc, postSubTitle, postUsername;
        Button btnApply;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            postUsername = (TextView)itemView.findViewById(R.id.postUsername);
            postTitle = (TextView) itemView.findViewById(R.id.postTitle);
            postDesc = (TextView) itemView.findViewById(R.id.postDesc);
            postSubTitle = (TextView)itemView.findViewById(R.id.postDate);
            parentLayout = (CardView)itemView.findViewById(R.id.parent_layout);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);

        }
    }

}

