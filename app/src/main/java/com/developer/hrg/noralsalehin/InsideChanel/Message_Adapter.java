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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by hamid on 6/27/2018.
 */

public class Message_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Message> messages;
    ClickListener clickListener;

    public Message_Adapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
        setHasStableIds(true);
    }

    public interface ClickListener {
        public void picture_imageClicked(int position, View view, CircularProgressBar circularProgressBar, ImageView iv_download);

        public void picture_likeClicked(int position, View view);

        public void picture_commentClicked(int position, View view);

        public void simple_likeClicked(int position, View view);

        public void simple_commentClicked(int position, View view);

        public void video_imageClicked(int position, View view, CircularProgressBar circularProgressBar, ImageView iv_download );

        public void video_likeClicked(int position, View view);

        public void video_commentClicked(int position, View view);

    }

    public void setCliclListener(ClickListener cliclListener) {
        this.clickListener = cliclListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_simple, parent, false);
            return new SimpleHolder(view);
        } else if (viewType == 2) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_picture, parent, false);
  // 2500ms = 2,5s
            // circularProgressBar.setProgressWithAnimation(65, animationDuration);
            return new ImageHolder(view);
        } else if (viewType == 3) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_video, parent, false);

            return new VideoHolder(view);
        } else {
            throw new RuntimeException("The type has to be ONE or TWO");
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Message message = messages.get(position);
        String time = "00:00";
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(message.getUpdated_at());
            time = new SimpleDateFormat("H:mm").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (holder instanceof SimpleHolder) {
            ((SimpleHolder) holder).tv_text.setText(message.getMessage());
            ((SimpleHolder) holder).tv_time.setText(time);

            if (message.getLiked() == 0) {
                ((SimpleHolder) holder).iv_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unlike));
            } else {
                ((SimpleHolder) holder).iv_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like));
            }
        } else if (holder instanceof ImageHolder) {

            if (isFileExists(Config.Folders.IMAGES, message.getUrl()) && getFile(Config.Folders.IMAGES, message.getUrl()).length()==message.getLenth()) {
                ((ImageHolder) holder).circularProgressBar.setVisibility(View.GONE);
                ((ImageHolder) holder).iv_download.setVisibility(View.GONE);
                ((ImageHolder) holder).view_fake_white.setVisibility(View.GONE);
                ((ImageHolder) holder).iv_picture.setImageURI(Uri.fromFile(getFile(Config.Folders.IMAGES, message.getUrl())));
//                Glide.with(context).load(getFile(Config.Folders.IMAGES, message.getUrl())).apply(new RequestOptions()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//
//                ).into(new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                        ((ImageHolder) holder).iv_picture.setImageDrawable(resource);
//                    }
//                });
//                ((ImageHolder) holder).iv_picture.setImageURI(Uri.fromFile(getFile(Config.Folders.IMAGES, message.getUrl())));
            } else {
                ((ImageHolder) holder).view_fake_white.setVisibility(View.VISIBLE);
                ((ImageHolder) holder).iv_download.setVisibility(View.VISIBLE);
                ((ImageHolder) holder).circularProgressBar.setVisibility(View.VISIBLE);
                Glide.with(context).load(Config.MESSAGE_THUMB_ADDRESS + message.getThumb()).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.white))
                ).into(((ImageHolder) holder).iv_picture);

                if (message.getDl_state()==0) {
                    ((ImageHolder) holder).circularProgressBar.setProgress(0);
                    ((ImageHolder) holder).iv_download.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.download));
                }else {

                    ((ImageHolder) holder).iv_download.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.pause));
                }


            }

            ((ImageHolder)holder).tv_time.setText(time);
            if (message.getMessage() != null) {
                ((ImageHolder) holder).tv_text.setVisibility(View.VISIBLE);
                ((ImageHolder) holder).tv_text.setText(message.getMessage());
            } else {
                ((ImageHolder) holder).tv_text.setVisibility(View.GONE);
            }
            if (message.getLiked() == 0) {
                ((ImageHolder) holder).iv_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unlike));
            } else {
                ((ImageHolder) holder).iv_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like));
            }

        } else if (holder instanceof VideoHolder) {

            ((VideoHolder) holder).tv_time.setText(time);
            if (message.getMessage() != null) {
                ((VideoHolder) holder).tv_text.setVisibility(View.VISIBLE);
                ((VideoHolder) holder).tv_text.setText(message.getMessage());
            } else {
                ((VideoHolder) holder).tv_text.setVisibility(View.GONE);
            }
            if (message.getLiked() == 0) {
                ((VideoHolder) holder).iv_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unlike));
            } else {
                ((VideoHolder) holder).iv_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like));
            }

            Glide.with(context).load(Config.VIDEO_PIC_ADDRES + message.getThumb()).apply(new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)

            ).into(((VideoHolder) holder).iv_picture);


            if (isFileExists(Config.Folders.VIDEOS, message.getUrl()) && getFile(Config.Folders.VIDEOS, message.getUrl()).length()==message.getLenth()) {
                ((VideoHolder) holder).circularProgressBar.setVisibility(View.INVISIBLE);
                ((VideoHolder) holder).iv_download.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.play));
            } else {


                ((VideoHolder) holder).iv_download.setVisibility(View.VISIBLE);
                ((VideoHolder) holder).circularProgressBar.setVisibility(View.VISIBLE);


                if (message.getDl_state()==0) {
                   ((VideoHolder) holder).circularProgressBar.setProgress(0);
                    ((VideoHolder) holder).iv_download.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.download));
                }else {
                    ((VideoHolder) holder).iv_download.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.pause));
                }
            }


            String lenth = readableFileSize(message.getLenth());
            ((VideoHolder) holder).tv_video_time.setText(message.getTime() + " | " + lenth);
        }

    }


    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    public void backup() {
        //        switch (holder.getItemViewType()) {
//            case 1:
//                SimpleHolder simpleHolder = (SimpleHolder) holder;
//                simpleHolder.tv_text.setText(message.getMessage());
//                simpleHolder.tv_time.setText(time);
//                if (message.getLiked() == 0) {
//                    simpleHolder.iv_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unlike));
//                } else {
//                    simpleHolder.iv_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like));
//                }
//
//                break;
//            case 2:
//
//
//                ImageHolder imageHolder = (ImageHolder) holder;
//                //in method esme folder va esme aks ro migire o check mikone bebine mojode ya na
//                if (isFileExists(Config.Folders.IMAGES, message.getUrl())) {
//                    imageHolder.circularProgressBar.setVisibility(View.GONE);
//                    imageHolder.iv_download.setVisibility(View.GONE);
//                    Glide.with(context).load(getFile(Config.Folders.IMAGES, message.getUrl())).apply(new RequestOptions()
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//
//                    ).into(new SimpleTarget<Drawable>() {
//                        @Override
//                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                            ((ImageHolder) holder).iv_picture.setImageDrawable(resource);
//                        }
//                    });
//                    imageHolder.iv_picture.setImageURI(Uri.fromFile(getFile(Config.Folders.IMAGES, message.getUrl())));
//                } else {
//                    imageHolder.iv_download.setVisibility(View.VISIBLE);
//                    imageHolder.circularProgressBar.setVisibility(View.VISIBLE);
//                    Glide.with(context).load(Config.MESSAGE_THUMB_ADDRESS + message.getThumb()).apply(new RequestOptions()
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .placeholder(ContextCompat.getDrawable(context, R.drawable.white))
//                    ).into(imageHolder.iv_picture);
//                }
//
//                imageHolder.tv_time.setText(time);
//                if (message.getMessage() != null) {
//                    imageHolder.tv_text.setVisibility(View.VISIBLE);
//                    imageHolder.tv_text.setText(message.getMessage());
//                } else {
//                    imageHolder.tv_text.setVisibility(View.GONE);
//                }
//                if (message.getLiked() == 0) {
//                    imageHolder.iv_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unlike));
//                } else {
//                    imageHolder.iv_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like));
//                }
//
//                break;
//            case 3:
//                VideoHolder videoHolder = (VideoHolder) holder;
//                videoHolder.tv_time.setText(time);
//                if (message.getMessage() != null) {
//                    videoHolder.tv_text.setVisibility(View.VISIBLE);
//                    videoHolder.tv_text.setText(message.getMessage());
//                } else {
//                    videoHolder.tv_text.setVisibility(View.GONE);
//                }
//                if (message.getLiked() == 0) {
//                    videoHolder.iv_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unlike));
//                } else {
//                    videoHolder.iv_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like));
//                }
//
//                Glide.with(context).load(Config.VIDEO_PIC_ADDRES + message.getThumb()).apply(new RequestOptions()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//
//                ).into(videoHolder.iv_picture);
//
//
//                if (isFileExists(Config.Folders.VIDEOS, message.getUrl())) {
//                    videoHolder.circularProgressBar.setVisibility(View.INVISIBLE);
//                    videoHolder.iv_download.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.play));
//                } else {
//                    videoHolder.iv_download.setVisibility(View.VISIBLE);
//                    videoHolder.circularProgressBar.setVisibility(View.VISIBLE);
//                    videoHolder.iv_download.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.download));
//                }
//
//                String lenth = message.getLenth() < 1024 ? message.getLenth() + " kb" : message.getLenth() / 1024 + " mb";
//                videoHolder.tv_video_time.setText(message.getTime() + " | " + lenth);
//
//
//                break;
//
//
//            default:
//                break;


        //    }
    }

    public boolean isFileExists(String folderName, String filename) {
        if (filename == null) {
            filename = "null";
        }
        File file = new File(Environment.getExternalStorageDirectory() + "/NoorAlSalehin/" + folderName, filename);
        return file.exists();
    }

    public File getFile(String folderName, String filename) {
        File file = new File(Environment.getExternalStorageDirectory() + "/NoorAlSalehin/" + folderName, filename);
        return file;
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = messages.get(position).getType();
        if (type == 1) {
            return 1;
        } else if (type == 2) {
            return 2;
        } else if (type == 3) {
            return 3;
        } else {
            return 0;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class SimpleHolder extends RecyclerView.ViewHolder {
        TextView tv_text, tv_time;

        ImageView iv_like, iv_comment;

        public SimpleHolder(View itemView) {
            super(itemView);
            tv_text = (TextView) itemView.findViewById(R.id.tv_custom_simple_text);
            iv_like = (ImageView) itemView.findViewById(R.id.iv_simple_like);
            iv_comment = (ImageView) itemView.findViewById(R.id.iv_simple_comment);
            tv_time = (TextView) itemView.findViewById(R.id.tv_simple_time);
            iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.simple_likeClicked(getAdapterPosition(), view);
                }
            });
            iv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.simple_commentClicked(getAdapterPosition(), view);
                }
            });
        }
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        ImageView iv_picture, iv_download, iv_like, iv_comment;
        View view_fake_white ;
        TextView tv_text, tv_time;

        CircularProgressBar circularProgressBar;

        public ImageHolder(View itemView) {
            super(itemView);
            iv_download = (ImageView) itemView.findViewById(R.id.iv_picture_download);
            iv_picture = (ImageView) itemView.findViewById(R.id.iv_picture_image);
            view_fake_white=(View)itemView.findViewById(R.id.view_picture_fake_white);
            circularProgressBar = (CircularProgressBar) itemView.findViewById(R.id.cp_picturee);
            tv_text = (TextView) itemView.findViewById(R.id.tv_picture_text);
            iv_comment = (ImageView) itemView.findViewById(R.id.iv_picture_comment);
            tv_time = (TextView) itemView.findViewById(R.id.tv_picture_time);
            iv_picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.picture_imageClicked(getAdapterPosition(), view, circularProgressBar, iv_download);
                }
            });
            iv_like = (ImageView) itemView.findViewById(R.id.iv_picture_like);
            iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.picture_likeClicked(getAdapterPosition(), view);
                }
            });
            iv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clickListener.picture_commentClicked(getAdapterPosition(), view);
                }
            });
        }
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        //  ImageView iv_picture   , iv_like , iv_comment;
        ImageView iv_picture, iv_download, iv_like, iv_comment;
        TextView tv_text, tv_time, tv_video_time;
        //  CircleProgressView circleProgressView ;
        View view_fake_video ;
        CircularProgressBar circularProgressBar;

        public VideoHolder(View itemView) {
            super(itemView);
            iv_download = (ImageView) itemView.findViewById(R.id.iv_video_download_playy);
            iv_picture = (ImageView) itemView.findViewById(R.id.iv_video_image);
            //    circleProgressView=(CircleProgressView)itemView.findViewById(R.id.cp_video_download);
            circularProgressBar = (CircularProgressBar) itemView.findViewById(R.id.cp_video);
            view_fake_video=(View)itemView.findViewById(R.id.view_video_fake_white);
            tv_text = (TextView) itemView.findViewById(R.id.tv_video_text);
            iv_comment = (ImageView) itemView.findViewById(R.id.iv_video_comment);
            tv_time = (TextView) itemView.findViewById(R.id.tv_video_time);
            tv_video_time = (TextView) itemView.findViewById(R.id.tv_video_video_time);
//            iv_picture.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
            view_fake_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.video_imageClicked(getAdapterPosition(), view, circularProgressBar, iv_download);
                }
            });

            iv_like = (ImageView) itemView.findViewById(R.id.iv_video_like);
            iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.video_likeClicked(getAdapterPosition(), view);
                }
            });
            iv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clickListener.video_commentClicked(getAdapterPosition(), view);
                }
            });

        }
    }


}
