package com.developer.hrg.noralsalehin.Main;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer.hrg.noralsalehin.Helps.Config;
import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.Models.UnRead;
import com.developer.hrg.noralsalehin.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GetChanelsAdapter extends RecyclerView.Adapter<GetChanelsAdapter.Holder> {

    Context context ;
    ArrayList<Chanel> chanels;
    ArrayList<UnRead> unReads ;
    MyClickListener myClickListener;
        public GetChanelsAdapter(Context context , ArrayList<Chanel> chanels , ArrayList<UnRead> unReads) {
            this.context=context;
            this.chanels=chanels;
            this.unReads=unReads;
        }
 public interface MyClickListener{
     public void chanel_clicked(int position, View view) ;



 }

 public void setMyClickListener(MyClickListener myClickListener) {
     this.myClickListener=myClickListener;
 }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.custom_chanel,parent,false);
        Holder holder = new Holder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String time = " " ;
        Chanel chanel = chanels.get(position);
        UnRead unRead = unReads.get(position);
        Log.e("databasecheckDate" ,chanel.getUpdated_at()==null ? "null" : chanel.getUpdated_at());
        Log.e("databasecheckType" , chanel.getType()+""==null ? "null" : chanel.getType()+"");
   if (chanel.getUpdated_at()!=null) {
       Date date = null;
       try {
           date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(chanel.getUpdated_at());
           time = new SimpleDateFormat("H:mm").format(date);
       } catch (ParseException e) {
           e.printStackTrace();
       }
   }else {
       time = "";
   }
      if (unRead.getCount()>0 || unReads==null)
      {
          holder.tv_count.setVisibility(View.VISIBLE);
          holder.tv_count.setText(unRead.getCount()+" ");

      }else {
          holder.tv_count.setVisibility(View.INVISIBLE);
      }


        holder.tv_name.setText(chanel.getName());
        holder.tv_admin_name.setText(chanel.getUsername() + " : ");
        holder.tv_time.setText(time);
        // inja 3 ta halat dare ya ham maseeage khalie ham type ke yani hanooz payami vase kanal ersal nashode
        // ya message khalie ke yani ye file bedone matn ersal shode
        // ya matn dashte akharan message ke matno neshon midim
        if (chanel.getLast_message()==null && chanel.getType()==null) {
            holder.tv_last.setText("بدون پیام ...");
        }else if (chanel.getLast_message()==null) {
            holder.tv_last.setText(chanel.getStringFromType());
        }else {
            holder.tv_last.setText(chanel.getLast_message());
        }
        Glide.with(context).load(Config.CHANEL_THUMB_BASE+chanel.getThumb()).into(holder.iv_profile);


    }

    @Override
    public int getItemCount() {
        return chanels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_name , tv_admin_name , tv_last , tv_count , tv_time  ;
        ImageView iv_profile  ;
        public Holder(View itemView) {
            super(itemView);
            tv_name=(TextView)itemView.findViewById(R.id.tv_custom_chanel_name);
            tv_admin_name=(TextView)itemView.findViewById(R.id.tv_custom_chanel_admin);
            iv_profile=(ImageView)itemView.findViewById(R.id.iv_custom_chanel_photo);
            tv_last=(TextView)itemView.findViewById(R.id.tv_custom_chanel_last);
            tv_count=(TextView)itemView.findViewById(R.id.tv_custom_chanel_count);
            tv_time=(TextView)itemView.findViewById(R.id.tv_custom_chanel_time);
               itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       myClickListener.chanel_clicked(getAdapterPosition(),view);
                   }
               });

        }
    }


}
