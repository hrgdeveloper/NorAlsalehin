package com.developer.hrg.noralsalehin.InsideChanel.comment;

/**
 * Created by hamid on 7/7/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.developer.hrg.noralsalehin.Helps.Config;
import com.developer.hrg.noralsalehin.Models.Comment;
import com.developer.hrg.noralsalehin.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comment_adapter extends RecyclerView.Adapter<Comment_adapter.Holder> {
    Context context ;
    ArrayList<Comment> comments;

    public Comment_adapter(Context context , ArrayList<Comment> comments) {
        this.context=context;
        this.comments=comments;
    }





    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_comment,parent,false);
        Holder holder = new Holder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Comment comment = comments.get(position);

        holder.tv_time.setText(comment.getCreated_at());
        holder.tv_text.setText(comment.getText());
        holder.tv_username.setText(comment.getUsername());
        Glide.with(context).load(Config.PROFILE_PIC_THUMB_ADDRESS+comment.getPic_thumb()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.profile).error(R.drawable.profile)
        ).into(holder.iv_pic);


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_username,tv_time , tv_text  ;
        CircleImageView iv_pic;
        public Holder(View itemView) {
            super(itemView);

            tv_text=(TextView)itemView.findViewById(R.id.tv_custom_comment_text);
            tv_time=(TextView)itemView.findViewById(R.id.tv_custom_comment_time);
            tv_username=(TextView)itemView.findViewById(R.id.tv_custom_comment_username);
            iv_pic=(CircleImageView) itemView.findViewById(R.id.iv_custom_comment_pic);


        }
    }


}
