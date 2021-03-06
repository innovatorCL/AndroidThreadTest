package com.innovator.intentservicetest;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;


/**
 * 使用 HandlerThread
 */
public class HandlerThreadActivity extends AppCompatActivity {

    private TextView mTvServiceInfo;

    private HandlerThread mCheckMsgThread;
    //HandlerThread 的 handler
    private Handler mCheckMsgHandler;
    private boolean isUpdateInfo;

    private static final int MSG_UPDATE_INFO = 0x110;

    //与UI线程管理的handler
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread);

        //创建后台线程
        initBackThread();

        mTvServiceInfo = findViewById(R.id.id_textview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //开始查询
        isUpdateInfo = true;
        mCheckMsgHandler.sendEmptyMessage(MSG_UPDATE_INFO);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //停止查询
        isUpdateInfo = false;
        mCheckMsgHandler.removeMessages(MSG_UPDATE_INFO);

    }

    private void initBackThread() {
        mCheckMsgThread = new HandlerThread("check-message-coming");
        mCheckMsgThread.start();

        //绑定 HandlerThread 的 Looper
        mCheckMsgHandler = new Handler(mCheckMsgThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {

                checkForUpdate();
                if (isUpdateInfo) {
                    //循环执行 HandlerThread Handler 的 handleMessage()
                    mCheckMsgHandler.sendEmptyMessageDelayed(MSG_UPDATE_INFO, 1000);
                }
            }
        };


    }

    /**
     * 模拟从服务器解析数据
     */
    private void checkForUpdate() {
        try {
            //模拟耗时
            Thread.sleep(1000);
            //切换回主线程
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    String result = "实时更新中，当前大盘指数：<font color='red'>%d</font>";
                    result = String.format(result, (int) (Math.random() * 3000 + 1000));
                    mTvServiceInfo.setText(Html.fromHtml(result));
                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        mCheckMsgThread.quit();
    }

}
