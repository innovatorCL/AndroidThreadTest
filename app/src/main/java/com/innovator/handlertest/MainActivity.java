package com.innovator.handlertest;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ThreadLocal<Boolean> mBooleanThreadLocal = new ThreadLocal<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBooleanThreadLocal.set(true);
        Log.i("TAG","[MainThread] 的值："+mBooleanThreadLocal.get());

        new Thread("Thread#1"){
            @Override
            public void run() {
                mBooleanThreadLocal.set(false);
                Log.i("TAG","[Thread#1] 的值："+mBooleanThreadLocal.get());
            }
        }.start();


        new Thread("Thread#2"){
            @Override
            public void run() {
                Log.i("TAG","[Thread#2] 的值："+mBooleanThreadLocal.get());
            }
        }.start();




        new Thread("Thread#3"){
            @Override
            public void run() {
                Looper.prepare();
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        Log.i("TAG","子线程接收到了消息："+Thread.currentThread().getName());
                    }
                };
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.i("TAG","Runnable 运行在："+Thread.currentThread().getName());
                    }
                };

                handler.post(runnable);
                handler.sendEmptyMessage(0);
                Looper.loop();
                Looper.myLooper().quitSafely();
            }
        }.start();

    }


    public void click(View view){
        Intent i = new Intent(this,LooperActivity.class);
        startActivity(i);
    }
}
