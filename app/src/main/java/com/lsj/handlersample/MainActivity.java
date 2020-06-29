package com.lsj.handlersample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListAdapter.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MESSAGE_WHAT = 0;

    private RecyclerView mRecyclerView;
    private List<String> mList;

    private ListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        sendMessageToMainThreadByWorkThread();
        initData();
        initView();
    }


    private void initData() {
        mList = new ArrayList<>();
        mList.add("ThreadLocal");//0

    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mListAdapter = new ListAdapter(this,mList));

        mListAdapter.setOnItemClickListener(this);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_WHAT:
                    Log.d(TAG, "main thread receiver message: " + ((String) msg.obj));
                    break;
            }
        }
    };


    /**
     * 通常我们在主线程中创建一个Handler，
     * 然后重写该Handler的handlerMessage方法，可以看到该方法传入了一个参数Message，
     * 该参数就是我们从其他线程传递过来的信息。
     *
     * 我们在来看下子线程中如何传递的信息，子线程通过Handler的obtainMessage()方法获取到一个Message实例，
     * 我们来看看Message的几个属性：
     * Message.what------------------>用来标识信息的int值，通过该值主线程能判断出来自不同地方的信息来源
     * Message.arg1/Message.arg2----->Message初始定义的用来传递int类型值的两个变量
     * Message.obj------------------->用来传递任何实例化对象
     * 最后通过sendMessage将Message发送出去。
     *
     * Handler所在的线程通过handlerMessage方法就能收到具体的信息了，如何判断信息的来源呢？当然是通过what值啦。
     * 怎么样很简单吧
     */
    private void sendMessageToMainThreadByWorkThread() {
        new Thread(){
            @Override
            public void run() {
                Message message = mHandler.obtainMessage(MESSAGE_WHAT);
                message.obj = "I am message from work thread";
                mHandler.sendMessage(message);
            }
        }.start();
    }


    @Override
    public void onItemClick(View view, int postion) {
        Intent intent;
        switch (postion) {
            case 0:
                intent = new Intent(this, ThreadLocalActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }
}
