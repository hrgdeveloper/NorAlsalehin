package com.developer.hrg.noralsalehin.InsideChanel;


import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.developer.hrg.noralsalehin.Helps.Config;
import com.developer.hrg.noralsalehin.Helps.TouchImageView;
import com.developer.hrg.noralsalehin.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class InsideVideoFragment extends Fragment {
    private static final String FILE = "file";
    private static final String TEXT = "text";

    private static final String VIDEOPOSITION = "position";

    private String  file;
    private String text;
    FrameLayout frameLayout ;
    VideoView videoView   ;
    ImageView iv_back;
    TextView tv_text ;
    Toolbar toolbar ;
    boolean hide = false ;
    RelativeLayout relativeLayout_top ;
    MediaController mediaController ;
    Integer last_post = 0 ;


    public static InsideVideoFragment newInstance(String file, String text) {
        InsideVideoFragment fragment = new InsideVideoFragment();
        Bundle args = new Bundle();
        args.putString(FILE, file);
        args.putString(TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            file = getArguments().getString(FILE);
            text = getArguments().getString(TEXT);
        }
    }


    public InsideVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(VIDEOPOSITION,videoView.getCurrentPosition());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_inside_video, container, false);


        videoView=(VideoView) view.findViewById(R.id.videoView);
        tv_text=(TextView)view.findViewById(R.id.tv_inside_video_bottom);
        iv_back=(ImageView)view.findViewById(R.id.iv_back_insideVideo);
        relativeLayout_top=(RelativeLayout)view.findViewById(R.id.relative_inside_video_top);
        mediaController=new MediaController(getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (text!=null) {
            tv_text.setText(text);
        }else {
            tv_text.setVisibility(View.GONE);
        }
        if (savedInstanceState==null) {

        }else {
            last_post=savedInstanceState.getInt(VIDEOPOSITION,0);
        }



        mediaController.setAnchorView(videoView);
        videoView.setVideoURI(Uri.fromFile(getFile(Config.Folders.VIDEOS,file)));
        videoView.setMediaController(mediaController);
        videoView.seekTo(last_post);
        videoView.start();

//        mediaController.show();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                ((InsideActivity)getActivity()).onBackPressed();
            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!hide) {
                    tv_text.setVisibility(View.INVISIBLE);
                    tv_text.animate().translationY(view.getHeight());
                    relativeLayout_top.setVisibility(View.INVISIBLE);
                    relativeLayout_top.animate().translationX(view.getHeight());
                    mediaController.show();
                }else {
                    tv_text.setVisibility(View.VISIBLE);
                    relativeLayout_top.setVisibility(View.VISIBLE);
                    tv_text.animate().translationY(0);
                    relativeLayout_top.animate().translationX(0);
                    mediaController.hide();

                }
                hide=!hide;
                return false;
            }
        });

 }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity()!=null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }


        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    ((InsideActivity)getActivity()).onBackPressed();

                    return true;
                }
                return false;
            }
        });

    }

    public File getFile (String folderName, String filename) {
        File file = new File(Environment.getExternalStorageDirectory()+"/NoorAlSalehin/"+folderName,filename);
        return file ;
    }


}
