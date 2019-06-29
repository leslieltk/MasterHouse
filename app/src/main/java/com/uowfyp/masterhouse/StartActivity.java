package com.uowfyp.masterhouse;

import android.content.Intent;
import android.media.Image;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {

    ImageView logotop, logobottom;
    Animation fromtop, frombottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        logotop = (ImageView)findViewById(R.id.logoTop);
        logobottom = (ImageView)findViewById(R.id.logobottom);
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        logotop.setAnimation(fromtop);
        logobottom.setAnimation(frombottom);


        new CountDownTimer(3000, 3000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        }.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
