package com.credix.pinpaddriverwithandroidusage;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pixplicity.sharp.Sharp;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyPresentation extends Presentation {
    private ImageView img;
    private VideoView vid;
    private YouTubePlayerView you_tube_player;
    private YouTubePlayer player;
    private YouTubePlayer youTubePlayer;

    public MyPresentation(Context outerContext,
                          Display display) {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        img = findViewById(R.id.display_image);
        vid = findViewById(R.id.display_vid);

        you_tube_player = findViewById(R.id.you_tube_player);
        boolean result = checkExternalImage();
        if (!result) {

        }

    }

    private boolean loadImage(File imgFile){
        try {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {

//            File imgFile = new File(imagePath);

                String mime = Files.probeContentType(Paths.get(imgFile.getAbsolutePath()));
                if (mime.contains("jpeg") || mime.contains("jpg") || mime.contains("png") || mime.contains("bmp")) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    if (myBitmap == null) return false;
                    img.setImageBitmap(myBitmap);

                    return true;
                } else if (mime.contains("svg")) {
                    FileInputStream stream = new FileInputStream(imgFile.getAbsoluteFile());

                    Sharp.loadInputStream(stream).into(img);
                    stream.close();
                    return true;
                }
                return false;


        }else{
            FileInputStream stream = new FileInputStream(imgFile.getAbsoluteFile());
            Sharp.loadInputStream(stream).into(img);
            stream.close();
        }
        }catch(Exception e){
            return false;
        }
        return false;

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (youTubePlayer != null) {
            youTubePlayer.removeListener(you_tube_listener);
            youTubePlayer.pause();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkExternalImage() {

//        playVideo();

        try {
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                // Both Read and write operations available

                File externalDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                boolean result;
                if (externalDir.list().length > 0){

                    result = loadImage(externalDir.listFiles()[0]);
                    if(result)
                        return  true;
                }

                 externalDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS);
                result = loadImage(externalDir.listFiles()[0]);
                if(result)
                    return  true;

           //   playVideo();

            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return  false;
    }

    private void playVideo() {

        img.setVisibility(View.GONE);

        you_tube_player.setVisibility(View.VISIBLE);

        you_tube_player.addYouTubePlayerListener(you_tube_listener);

    }

    AbstractYouTubePlayerListener  you_tube_listener = new AbstractYouTubePlayerListener() {
        @Override
        public void onReady(@NonNull YouTubePlayer _youTubePlayer) {
            String videoId = "orJSJGHjBLI";
            youTubePlayer = _youTubePlayer;
            youTubePlayer.loadVideo(videoId, 0);
            youTubePlayer.setVolume(0);
            youTubePlayer.mute();
            youTubePlayer.play();
        }
    };
}
