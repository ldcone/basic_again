package com.example.learn_recyclerview;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;

public class fragment_main extends AppCompatActivity implements SurfaceHolder.Callback {
    private Button btn_1;
    private Button btn_start;
    private Button btn_stop;
    Thread thread;
    boolean isThread = false;
    private Button btn_dialog;

    private Camera camera;
    private MediaRecorder mediaRecorder;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private boolean recording = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        TedPermission.with(this)
                .setPermissionListener(permission)
                .setRationaleMessage("녹화를 위하여 권한을 허용해주세요.")
                .setDeniedMessage("권한이 거부되었습니다.")
                .setPermissions(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .check();
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_dialog = (Button) findViewById(R.id.btn_recording);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                MainActivity fragment_main2 = new MainActivity();
                transaction.replace(R.id.frame,fragment_main2);
                transaction.commit();
            }
        });
//        //스레드 시작
//        btn_start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("whattheheck","notworking?");
//                isThread = true;
//                thread = new Thread(){
//                    public void run(){
//                        while(isThread){
//                            try {
//                                Log.d("whattheheck","notworking?");
//                                sleep(5000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            Log.d("working rigth","working");
//                            handler.sendEmptyMessage(0);
//                        }
//                    }
//                };
//                thread.start();
//
//            }
//        });
//        // 스레드 종료
//        btn_stop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isThread = false;
//
//            }
//        });

        //백그라운드 음악 시작
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("clicked","clicked");
                startService(new Intent(getApplicationContext(),music_service.class));
            }
        });

        // 백그라운드 음악 멈추기
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(),music_service.class));
            }
        });

//        btn_dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("clicked","clicked");
//                AlertDialog.Builder ad = new AlertDialog.Builder(fragment_main.this);
//                ad.setIcon(R.mipmap.ic_launcher);
//                ad.setTitle("title");
//                ad.setMessage("end application");
//
//                final EditText et = new EditText(fragment_main.this);
//                ad.setView(et);
//
//                ad.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String st = et.getText().toString();
//                        Toast.makeText(getApplicationContext(),st,Toast.LENGTH_SHORT).show();
//                        dialogInterface.dismiss();
//                    }
//                });
//
//                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                ad.show();
//
//            }
//        });
        btn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recording){
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    camera.lock();
                    recording =false;
                }else{
                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            Toast.makeText(fragment_main.this,"녹화가 시작되었습니다.",Toast.LENGTH_SHORT).show();
                            try {
                                Log.d("working?","working");
                                File file = new File(getFilesDir(), "test.mp4") ;
                                mediaRecorder = new MediaRecorder();
                                camera.unlock();
                                mediaRecorder.setCamera(camera);
                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                                mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                                mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
                                mediaRecorder.setOrientationHint(90);
                                mediaRecorder.setOutputFile(file);
                                mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                                recording = true;
                            }catch (Exception e){

                                e.printStackTrace();
                                mediaRecorder.release();
                            }
                        }
                    });
                }
            }
        });




    }

    PermissionListener permission = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(fragment_main.this,"권한 허가 ",Toast.LENGTH_SHORT).show();
            camera = Camera.open();
            camera.setDisplayOrientation(90);
            surfaceView = (SurfaceView) findViewById(R.id.sv);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(fragment_main.this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(fragment_main.this,"권한 거부 ",Toast.LENGTH_SHORT).show();

        }
    };
    private  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Toast.makeText(getApplicationContext(),"alertthis",Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

    }

    private void refreshCamera(Camera camera) {
        if(surfaceHolder.getSurface() == null){
            return;
        }
        try{
            camera.stopPreview();
        }catch (Exception e){
            e.printStackTrace();
        }
        setCamera(camera);
    }

    private void setCamera(Camera camera) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        refreshCamera(camera);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}
