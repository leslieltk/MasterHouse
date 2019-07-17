package com.uowfyp.masterhouse;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.readmoreoption.ReadMoreOption;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.viewHolder> {
    private static final String TAG = "HomeActivity";
    ArrayList<MissionPost> list;
    MissionPost missionPost = new MissionPost();
    Context mcontext;
    View.OnClickListener mOnItemClickListener;
    DatabaseReference dreff;

    public RecycleViewAdapter( ArrayList<MissionPost> list) {
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
        viewHolder.postSubTitle.setText((list.get(position).getDate()));
        viewHolder.tvPrice.setText("$ " +(list.get(position).getPrice())+ " " +(list.get(position).getPriceType()));

        ReadMoreOption readMoreOption = new ReadMoreOption.Builder(mcontext)    // readmore function to show less on default
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


//        if(list.get(position).islike == true){
//            viewHolder.btnlike.setBackgroundResource(R.drawable.ic_favorite_red_600_24dp);
//        }
//        viewHolder.btnlike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


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
        TextView postTitle, postDesc, postSubTitle, postUsername, tvPrice;
        Button btnlike;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            postUsername = (TextView)itemView.findViewById(R.id.postUsername);
            postTitle = (TextView) itemView.findViewById(R.id.postTitle);
            postDesc = (TextView) itemView.findViewById(R.id.postDesc);
            postSubTitle = (TextView)itemView.findViewById(R.id.postDate);
            parentLayout = (CardView)itemView.findViewById(R.id.parent_layout);
            tvPrice = (TextView)itemView.findViewById(R.id.tvCardPrice);
//            btnlike = (Button)itemView.findViewById(R.id.btnslike);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);

        }
    }

}

