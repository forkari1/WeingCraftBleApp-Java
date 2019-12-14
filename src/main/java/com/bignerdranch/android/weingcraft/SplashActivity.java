package com.bignerdranch.android.weingcraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        try{
            Thread.sleep(0);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
