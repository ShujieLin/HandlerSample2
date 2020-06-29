package com.lsj.handlersample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class ThreadLocalActivity extends AppCompatActivity {

    private static final String TAG = ThreadLocalActivity.class.getSimpleName();
    private ThreadLocal<Boolean> mBooleanThreadLocal = new ThreadLocal<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_local);

        init();


    }

    /**
     * 从日志可以看出，虽然在不同线程中访问的是同一个ThreaLocal对象，但是他们通过ThreadLocal取到的值确实不一样的。
     * 在不同线程访问同一个ThreadLocal的get方法，ThreadLocal内部会从各自的线程中取出一个数组。然后再从数组中根据当前ThreadLocal的索引去查找对应的value值。
     */
    private void init() {
        mBooleanThreadLocal.set(true);
        Log.d(TAG,"[Thread#main]mBooleanThreadLocal = " + mBooleanThreadLocal.get());

        new Thread("Thread#1"){
            @Override
            public void run() {
                mBooleanThreadLocal.set(false);
                Log.d(TAG,"[Thread#1]mBooleanThreadLocal = " + mBooleanThreadLocal.get());
            }
        }.start();

        new Thread("Thread#2"){
            @Override
            public void run() {
                Log.d(TAG,"[Thread#2]mBooleanThreadLocal = " + mBooleanThreadLocal.get());
            }
        }.start();

    }
}
