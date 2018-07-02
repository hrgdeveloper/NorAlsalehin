package com.developer.hrg.noralsalehin.InsideChanel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.developer.hrg.noralsalehin.Helps.Config;
import com.developer.hrg.noralsalehin.Models.Message;
import com.developer.hrg.noralsalehin.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by hamid on 6/27/2018.
 */

public class Message_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context ;
    ArrayList<Message> messages ;
    ClickListener clickListener ;
    public Message_Adapter(Context context,ArrayList<Message> messages) {
           this.context=context;
        this.messages=messages;

    }
  public interface ClickListener {
        public void imageClicked(int position,View view,CircularProgressBar circularProgressBar);

    }

    public void setCliclListener(ClickListener cliclListener) {
        this.clickListener=cliclListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==1) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_simple,parent,false);
            return new SimpleHolder(view);
        }else if (viewType==2) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_picture,parent,false);
            CircularProgressBar circularProgressBar = (CircularProgressBar)view.findViewById(R.id.cp_picture);
            circularProgressBar.setColor(ContextCompat.getColor(context,android.R.color.holo_blue_dark));
            circularProgressBar.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark));
            circularProgressBar.setProgressBarWidth(10);
            circularProgressBar.setBackgroundProgressBarWidth(5);
        //    int animationDuration = 2500; // 2500ms = 2,5s
       //     circularProgressBar.setProgressWithAnimation(65, animationDuration);
            return new ImageHolder(view);
        }else  {
            throw new RuntimeException("The type has to be ONE or TWO");
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
           switch (holder.getItemViewType()) {
               case 1 :
                   SimpleHolder  simpleHolder = (SimpleHolder)holder;
                   simpleHolder.tv_text.setText(message.getMessage());
                   break;
               case 2 :
                   ImageHolder imageHolder = (ImageHolder)holder;
                   //in method esme folder va esme aks ro migire o check mikone bebine mojode ya na
                    if (isFileExists(Config.Folders.IMAGES ,message.getUrl())) {
                        imageHolder.circularProgressBar.setVisibility(View.GONE);
                        imageHolder.iv_download.setVisibility(View.GONE);
                        Glide.with(context).load(getFile(Config.Folders.IMAGES,message.getUrl())).apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)

                        ) .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                ((ImageHolder) holder).iv_picture.setImageDrawable(resource);
                            }
                        });
                    //    imageHolder.iv_picture.setImageURI(Uri.fromFile(getFile(Config.Folders.IMAGES,message.getUrl())));
                    }else {
                        imageHolder.iv_download.setVisibility(View.VISIBLE);
                        imageHolder.circularProgressBar.setVisibility(View.VISIBLE);
                        Glide.with(context).load(Config.MESSAGE_THUMB_ADDRESS+message.getThumb()).apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(ContextCompat.getDrawable(context,R.drawable.white))
                        ).into(imageHolder.iv_picture);
                    }




                   if (message.getMessage()!=null) {
                       imageHolder.tv_text.setVisibility(View.VISIBLE);
                       imageHolder.tv_text.setText(message.getMessage());
                   }else {
                       imageHolder.tv_text.setVisibility(View.GONE);
                   }

                   break;
               default:
                   break;


           }
    }

    public boolean  isFileExists (String folderName, String filename) {
        if (filename==null) {
            filename = "null";
        }
        File file = new File(Environment.getExternalStorageDirectory()+"/NoorAlSalehin/"+folderName,filename);
        return file.exists();
    }
    public File  getFile (String folderName, String filename) {
        File file = new File(Environment.getExternalStorageDirectory()+"/NoorAlSalehin/"+folderName,filename);
        return file ;
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
       int type = messages.get(position).getType();
        if (type==1) {
            return 1;
        }else if (type==2) {
            return 2 ;
        }else {
            return -1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class SimpleHolder extends RecyclerView.ViewHolder {
   TextView tv_text ;
        public SimpleHolder(View itemView) {
            super(itemView);
            tv_text=(TextView)itemView.findViewById(R.id.tv_custom_simple_text);
        }
    }
    class ImageHolder extends RecyclerView.ViewHolder {
      ImageView iv_picture , iv_download ;
        TextView tv_text ;
        TextView tv_percent;
        CircularProgressBar circularProgressBar ;
        public ImageHolder(View itemView) {
            super(itemView);
            iv_download=(ImageView)itemView.findViewById(R.id.iv_picture_download);
            iv_picture=(ImageView)itemView.findViewById(R.id.iv_custom_image);
             circularProgressBar=(CircularProgressBar)itemView.findViewById(R.id.cp_picture);
            tv_text=(TextView)itemView.findViewById(R.id.tv_custom_picture_text);
            iv_picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.imageClicked(getAdapterPosition(),view,circularProgressBar);
                }
            });
        }
    }



}