package com.innovator.handlertest;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LooperActivity extends AppCompatActivity {

    private static final String TAG = "MainThread";
    private Handler mMainHandler = null;
    private Button msgBtn = null;
    private Button btn1 = null;
    private int nClick = 0;
    ChildThread child1 = null;
    ChildThread child2 = null;


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looper);


        msgBtn = (Button) findViewById(R.id.msgBtn);
        btn1 = (Button) findViewById(R.id.button1);

        mMainHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                return false;
            }
        });

        mMainHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                Log.i(TAG, "Got an incoming message from the child thread - "
                        + (String) msg.obj);

                // 接收子线程的消息
                Log.i("TAG","收到子线程发过来的消息："+(String) msg.obj + String.valueOf(nClick));
            }

        };

        child1 = new ChildThread();
        child1.start();

        child2 = new ChildThread();
        child2.start();

        msgBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (child1.childHander != null) {

                    // 发送消息给子线程
                    Message msg = child1.childHander.obtainMessage();
//                    Message msg = new Message();
                    msg.obj = mMainHandler.getLooper().getThread().getName()
                            + " says Hello and msbBtn sending";

                    child1.childHander.sendMessage(msg);
                    Log.i(TAG, "Send a message to the child thread - "
                            + (String) msg.obj);
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (child2.childHander != null) {

                    // 发送消息给子线程
                    Message msg = child2.childHander.obtainMessage();
//                    Message msg = new Message();
                    msg.obj = mMainHandler.getLooper().getThread().getName()
                            + " says Hello and btn1 sending";
                    child2.childHander.sendMessage(msg);
                    Log.i(TAG, "Send a message to the child thread - "
                            + (String) msg.obj);
                }

            }

        });

    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Stop looping the child thread's message queue");

        if (child1.childHander != null) {
            child1.childHander.getLooper().quit();
        }
        if (child2.childHander != null) {
            child2.childHander.getLooper().quit();
        }

    }

    class ChildThread extends Thread {
        private Handler childHander = null;
        private int nClickTimes = 0;
        private static final String CHILD_TAG = "ChildThread";

        @SuppressLint("HandlerLeak")
        public void run() {
            this.setName("ChildThread");

            // 初始化消息循环队列，需要在Handler创建之前
            Looper.prepare();

            childHander = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Log.i("TAG",
                            "Got an incoming message from the main thread - "
                                    + (String) msg.obj);

                    try {

                        // 在子线程中可以做一些耗时的工作
                        String sMsg = "";
                        sleep(1000);

                        Message toMain = new Message();

                        // mMainHandler.obtainMessage();
                        sMsg = String.valueOf(++nClickTimes);
                        toMain.obj = sMsg + "This is "
                                + this.getLooper().getThread().getName()
                                + ".  你发送了消息: \"" + (String) msg.obj + "\"?"
                                + "这是第" + sMsg + "次 ";

                        mMainHandler.sendMessage(toMain);
                        Message toChild = new Message();
                        toChild.obj = "over";
                        // mChildHandler.sendMessage(toChild);

                        Log.i(CHILD_TAG, "Send a message to the main thread - "
                                + (String) toMain.obj);

                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                }

            };

            // 启动子线程消息循环队列
            Looper.loop();

        }
    }
}
