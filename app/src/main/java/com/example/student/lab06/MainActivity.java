package com.example.student.lab06;

import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    //在屬性要先設定動畫物件
    private AnimationDrawable animate;
    private TextView text;
    //view可以設定背景圖片，比imageview省資源
    private View viewlogo;
    private TextView logoname, message;
    //資源檔陣列
    // getResources().obtainTypedArray()會回傳TypedArray
    private TypedArray NBAlogos;
    //陣列有多少張圖
    private int logoNumber;
    private Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intiView();
        initNBAlogos();
        initAnimation();
    }

    private void intiView() {
        img = findViewById(R.id.img);
        text = findViewById(R.id.text);
        viewlogo = findViewById(R.id.viewlogo);
        logoname = findViewById(R.id.logoname);
        message = findViewById(R.id.message);
        go = findViewById(R.id.go);
    }

    //設定圖片陣列資源，設定view背景圖片
    private void initNBAlogos() {
        //obtainTypedArray():
        // Returns a TypedArray holding an array of the array values.
        //取得nbalogo陣列資源
        NBAlogos = getResources().obtainTypedArray(R.array.nbalogo);
        //取得陣列長度
        logoNumber = NBAlogos.length();
        //getDrawable (int index)
        //Retrieve the Drawable for the attribute at index.
        //取得陣列第0項的圖設定給view
        viewlogo.setBackground(NBAlogos.getDrawable(0));
    }

    private void initAnimation() {
        //設定imageview
        img.setBackgroundResource(R.drawable.frame_animation);
        //取得imageview的背景給動畫物件animate
        animate = (AnimationDrawable) img.getBackground();
    }


    public void click(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.start:
                animate.start(); //開始動畫
                break;
            case R.id.stop:
                animate.stop(); //結束動畫
                break;
            case R.id.secs:
                animation5seconds();
                break;
        }
    }

    //Handler 處理代辦事項
    //Each Handler instance is associated with
    // a single thread and that thread's message queue.
    //There are two main uses for a Handler:
    // (1) to schedule messages and runnables to
    // be executed as some point in the future; and
    // (2) to enqueue an action to be performed
    // on a different thread than your own.
    private Handler handle = new Handler();

    private void animation5seconds() {
        int delaytime = 5000;
        animate.start();
        Runnable task = new Task(); //多型的作法
        boolean result = handle.postDelayed(task, delaytime);
        text.setText(result ? "交付成功" : "交付失敗");
    }


    //這邊使用內部類別來implement Runnable，
    // 因為如果另外建立一個外部類別，將無法使用private變數
    //The Runnable interface should be implemented by
    // any class whose instances are intended to be executed by a thread.
    // The class must define a method of no arguments called run.
    private class Task implements Runnable {
        @Override
        public void run() {
            animate.stop();
            text.setText("時間到");
        }
    }

    private Runnable start = new startchange();
    private Runnable stop = new stopchange();

    public void go(View view) {
        handle.post(start); //立刻執行任務
        handle.postDelayed(stop, 3000);
        go.setEnabled(false);
    }


    private class startchange implements Runnable {

        @Override
        public void run() {
            //取得字串陣列資源
            String[] name = getResources().getStringArray(R.array.nabname);
            //隨機選取0~12的數字
            int index = (int) (Math.random() * logoNumber);
            //換圖
            viewlogo.setBackground(NBAlogos.getDrawable(index));
            //換隊名
            logoname.setText(name[index]);
            //this是指 startchange這個物件本身
            boolean result = handle.postDelayed(this, 100);
            message.setText(result ? "設定成功" : "設定失敗");
        }

    }

    private class stopchange implements Runnable {
        @Override
        public void run() {
            //取消任務start
            handle.removeCallbacks(start);
            go.setEnabled(true);
            //不可以放在go()裡面，
            // 因為go以後要等三秒後才會顯示時間到，
            // 如果放在go()裡面會被設定成功覆蓋掉
            message.setText("時間到");
        }
    }


}
