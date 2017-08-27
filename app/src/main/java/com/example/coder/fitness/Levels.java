package com.example.coder.fitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Levels extends AppCompatActivity implements View.OnClickListener {

    Button easy, medium, hard;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        easy = (Button) findViewById(R.id.easy);
        medium = (Button) findViewById(R.id.medium);
        hard = (Button) findViewById(R.id.hard);
        url = getIntent().getStringExtra("url");
        easy.setOnClickListener(this);
        medium.setOnClickListener(this);
        hard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (getIntent() == null)
            return;
        Intent intent = new Intent(getApplicationContext(), WorkOut.class);
        intent.putExtra("url", url);
        intent.putExtra("name", getIntent().getStringExtra("name").toString());
        if (v.getId() == R.id.easy) {

            intent.putExtra("time", 60000);
            startActivity(intent);
        } else if (v.getId() == R.id.medium) {
            intent.putExtra("time", 120000);
            startActivity(intent);
        } else if (v.getId() == R.id.hard) {
            intent.putExtra("time", 200000);
            startActivity(intent);
        }
    }
}
