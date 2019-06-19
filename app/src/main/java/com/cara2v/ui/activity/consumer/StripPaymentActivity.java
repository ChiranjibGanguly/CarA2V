package com.cara2v.ui.activity.consumer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cara2v.R;
import com.cara2v.ui.fragments.consumer.BankPayFragment;
import com.cara2v.ui.fragments.consumer.CardPayFragment;
import com.cara2v.util.MyApplication;


public class StripPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    private FrameLayout containerFrm;
    private Button btnCard, btnBank;
    private ImageView menuBtn;
    private TextView titleTxt;
    private FragmentManager fm;
    private int clickId = 0;
    private String requestId="",type="",userId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.strip_payment_activity);
        MyApplication.addActivity(StripPaymentActivity.this);
        context = getApplication();
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            requestId=bundle.getString("requestId");
            type=bundle.getString("type");
            userId=bundle.getString("userId");
        }
        initializeView();
    }

    private void initializeView() {
        fm = getSupportFragmentManager();

        btnCard = findViewById(R.id.btnCard);
        btnCard.setOnClickListener(this);
        btnBank = findViewById(R.id.btnBank);
        btnBank.setOnClickListener(this);
        containerFrm = findViewById(R.id.containerFrm);
        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);
        menuBtn.setImageResource(R.drawable.back_icon);
        titleTxt.setText("Payment");

        menuBtn.setOnClickListener(this);
        btnCard.setBackground(ContextCompat.getDrawable(context, R.drawable.blue_left_btn_bg));
        btnCard.setTextColor(ContextCompat.getColor(context, R.color.white));
        btnBank.setBackground(ContextCompat.getDrawable(context, R.drawable.white_right_btn_bg));
        btnBank.setTextColor(ContextCompat.getColor(context, R.color.black));
        clickId = btnCard.getId();

        replaceFragment(CardPayFragment.newInstance(requestId,type,userId), false, R.id.containerFrm);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuBtn:
                finish();
                break;
            case R.id.btnCard:
                if (clickId != view.getId()) {
                    btnCard.setBackground(ContextCompat.getDrawable(context, R.drawable.blue_left_btn_bg));
                    btnCard.setTextColor(ContextCompat.getColor(context, R.color.white));
                    btnBank.setBackground(ContextCompat.getDrawable(context, R.drawable.white_right_btn_bg));
                    btnBank.setTextColor(ContextCompat.getColor(context, R.color.black));
                    clickId = view.getId();
                    replaceFragment(CardPayFragment.newInstance(requestId,type,userId), false, R.id.containerFrm);
                }
                break;
            case R.id.btnBank:
                if (clickId != view.getId()) {
                    btnBank.setBackground(ContextCompat.getDrawable(context, R.drawable.blue_right_btn_bg));
                    btnBank.setTextColor(ContextCompat.getColor(context, R.color.white));
                    btnCard.setBackground(ContextCompat.getDrawable(context, R.drawable.white_left_btn_bg));
                    btnCard.setTextColor(ContextCompat.getColor(context, R.color.black));
                    clickId = view.getId();
                    replaceFragment(BankPayFragment.newInstance(requestId,type,userId), false, R.id.containerFrm);
                }
                break;
        }
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = getFragmentManager().popBackStackImmediate(backStackName, 0);
        int i = fm.getBackStackEntryCount();
        while (i > 0) {
            fm.popBackStackImmediate();
            i--;
        }
        if (!fragmentPopped) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
           // transaction.setCustomAnimations(R.anim.slide_right_out, R.anim.slide_right_in);
            transaction.replace(containerId, fragment, backStackName).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }


}
