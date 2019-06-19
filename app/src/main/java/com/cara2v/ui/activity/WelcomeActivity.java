package com.cara2v.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cara2v.R;
import com.cara2v.util.PreferenceConnector;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ownerLay;
    private ImageView ownerImg;
    private TextView ownerTxt;
    private LinearLayout consumerLay;
    private ImageView consumerImg;
    private TextView consumerTxt;
    private int clickId = 0;
    private int userType = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initializeView();
    }

    private void initializeView() {
        ownerLay = (LinearLayout) findViewById(R.id.ownerLay);
        ownerImg = (ImageView) findViewById(R.id.ownerImg);
        ownerTxt = (TextView) findViewById(R.id.ownerTxt);
        consumerLay = (LinearLayout) findViewById(R.id.consumerLay);
        consumerImg = (ImageView) findViewById(R.id.consumerImg);
        consumerTxt = (TextView) findViewById(R.id.consumerTxt);

        ownerLay.setOnClickListener(this);
        consumerLay.setOnClickListener(this);
        clickId = R.id.consumerLay;
        consumerImg.setImageResource(R.drawable.active_consumer);
        consumerTxt.setTextColor(ContextCompat.getColor(this, R.color.colorBlueText));
        ownerImg.setImageResource(R.drawable.owner);
        ownerTxt.setTextColor(ContextCompat.getColor(this, R.color.colorInActiveText));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ownerLay:
              /*  if (clickId != R.id.ownerLay) {
                    clickId = R.id.ownerLay;*/
                    userType = 1;
                    ownerImg.setImageResource(R.drawable.active_owner);
                    ownerTxt.setTextColor(ContextCompat.getColor(this, R.color.colorBlueText));
                    consumerImg.setImageResource(R.drawable.consumer);
                    consumerTxt.setTextColor(ContextCompat.getColor(this, R.color.colorInActiveText));
                    PreferenceConnector.writeInteger(this, PreferenceConnector.UserType, userType);
                    startActivity(new Intent(WelcomeActivity.this, ActivityLogin.class));
              //  }
                break;
            case R.id.consumerLay:
                /*if (clickId != R.id.consumerLay) {
                    clickId = R.id.consumerLay;*/
                    userType = 2;
                    consumerImg.setImageResource(R.drawable.active_consumer);
                    consumerTxt.setTextColor(ContextCompat.getColor(this, R.color.colorBlueText));
                    ownerImg.setImageResource(R.drawable.owner);
                    ownerTxt.setTextColor(ContextCompat.getColor(this, R.color.colorInActiveText));
                    PreferenceConnector.writeInteger(this, PreferenceConnector.UserType, userType);
                    startActivity(new Intent(WelcomeActivity.this, ActivityLogin.class));
              //  }
                break;
        }
    }
}
