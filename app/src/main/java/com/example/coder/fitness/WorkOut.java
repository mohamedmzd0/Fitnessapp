package com.example.coder.fitness;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Date;

public class WorkOut extends AppCompatActivity {

    ImageView imageView;
    Button start;
    String url;
    int timer;
    long remainTimer = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.image);
        start = (Button) findViewById(R.id.start);
        url = getIntent().getStringExtra("url");
        timer = getIntent().getIntExtra("time", 0);
        Glide.with(getApplicationContext()).load(url)
                .asGif()
                .error(R.drawable.error)
                .listener(new RequestListener<String, GifDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                        Toast.makeText(WorkOut.this, "can't connect...", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        start.setVisibility(View.VISIBLE);
                        findViewById(R.id.progress).setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(imageView);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerMethod(timer);
            }
        });
    }

    public void timerMethod(long t) {
        if (remainTimer == -1 || t != timer) {
            new CountDownTimer(t, 1000) {
                public void onTick(long millisUntilFinished) {
                    remainTimer = millisUntilFinished;
                    start.setText(getString(R.string.seconds_rem) + remainTimer / 1000);

                }

                public void onFinish() {
                    start.setText(R.string.done);
                    DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                    SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
                    Date d = new Date();
                    String date = d.getDay() + " / " + d.getMonth() + " / " + d.getYear();
                    String status;
                    if (timer == 60000)
                        status = "beginner";
                    else if (timer == 120000)
                        status = "medium";
                    else
                        status = "hard";
                    helper.insertprogress(getIntent().getStringExtra("name"), date, status);
                }
            }.start();

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("remain", remainTimer);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            remainTimer = savedInstanceState.getLong("remain");
            timerMethod(remainTimer);
        }
    }
}
