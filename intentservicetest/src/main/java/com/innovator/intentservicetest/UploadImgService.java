package com.innovator.intentservicetest;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class UploadImgService extends IntentService {

    private static final String ACTION_UPLOAD_IMG = "com.zhy.blogcodes.intentservice.action.UPLOAD_IMAGE";
    public static final String EXTRA_IMG_PATH = "com.zhy.blogcodes.intentservice.extra.IMG_PATH";

    public static void startUploadImg(Context context, String path) {

        Intent intent = new Intent(context, UploadImgService.class);
        intent.setAction(ACTION_UPLOAD_IMG);
        intent.putExtra(EXTRA_IMG_PATH, path);
        context.startService(intent);
    }


    public UploadImgService() {
        super("UploadImgService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_IMG.equals(action)) {
                final String path = intent.getStringExtra(EXTRA_IMG_PATH);
                handleUploadImg(path);
            }
        }
    }

    private void handleUploadImg(String path) {
        try {
            //模拟上传耗时
            Thread.sleep(3000);

            Intent intent = new Intent(MainActivity.UPLOAD_RESULT);
            intent.putExtra(EXTRA_IMG_PATH, path);
            sendBroadcast(intent);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG","onCreate");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("TAG","onBind");
        return null;
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e("TAG","onSatrtCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Log.e("TAG","onSatrt");
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG","onDestroy");
    }

}
