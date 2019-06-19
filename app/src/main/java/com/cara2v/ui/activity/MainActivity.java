package com.cara2v.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.R;
import com.cara2v.chat.ChatFragment;
import com.cara2v.chat.ChatUserBean;
import com.cara2v.responceBean.SignInRespoce;
import com.cara2v.ui.fragments.FragmentFeed;
import com.cara2v.ui.fragments.FragmentNearby;
import com.cara2v.ui.fragments.FragmentNotifications;
import com.cara2v.ui.fragments.FragmentProfile;
import com.cara2v.ui.fragments.FragmentTermsPolicies;
import com.cara2v.ui.fragments.OnDevelopmentFragment;
import com.cara2v.ui.fragments.OnlineJobsNServicesFragment;
import com.cara2v.ui.fragments.consumer.FragmentMyServices;
import com.cara2v.ui.fragments.owner.FragmentAddEditAccount;
import com.cara2v.ui.fragments.owner.FragmentMyJobs;
import com.cara2v.ui.fragments.owner.FragmentPromoCode;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.ProgressDialog;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout contentView;
    private LinearLayout navigationView, footer;
    private DrawerLayout drawerLayout;
    private ImageView menuBtn;
    private final float END_SCALE = 0.7f;
    private ViewPager viewPager;
    private TabLayout tabLay;
    private TextView titleTxt;
    private CoordinatorLayout coOrdinateLay;

    private RelativeLayout firstTabLay;
    private ImageView firstTabImage;
    private RelativeLayout secondTabLay;
    private ImageView secondTabImage;
    private RelativeLayout thirdTabLay;
    private ImageView thirdTabImage;
    private RelativeLayout fourthTabLay;
    private ImageView fourthTabImage;
    private RelativeLayout fifthTabLay;
    private ImageView fifthTabImage;

    private FrameLayout notiFrm;
    private TextView notiTxt;

    private View fstTbVw;
    private View ndTbVw;
    private View rdTbVw;
    private View frthTbVw;
    private View fifthTbVw;
    //Slide
    private TextView fstSlTab;
    private TextView sndSlTab;
    private TextView rdSlTab;
    private TextView frthSlTab;
    private TextView fifthSlTab;
    private TextView sxthSlTab;
    private TextView svnthSlTab;
    private TextView ethSlTab;
    private FrameLayout frthCountFrm;
    private FrameLayout rdCountFrm;
    private TextView frthCountTxt;
    private TextView rdCountTxt;

    //--------
    private int userType = 0;
    private FragmentManager fm;
    private Boolean doubleBackToExitPressedOnce = false;
    static Runnable runnable;

    private int clickId = 0;
    private ServiceNVehicleInterface serviceNVehicleInterface;
    private String requestId = "", notiType = "";
    public static String BaseFragment = "";

    private ProgressDialog progressDialog;
    private SignInRespoce signInRespoce;
    public static int notificationCount = 0;
    public static int chatCount = 0;
    public static String countType = "";

    {
        countType = "";
    }

    public interface ServiceNVehicleInterface {
        void onRefresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = (Bundle) getIntent().getExtras();
        if (bundle != null) {
            requestId = "" + bundle.get("requestId");
            notiType = bundle.getString("NotifyType");
            try {
                Log.e("requestId", requestId);
                Log.e("NotifyType", notiType);
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage());
            }
        }
        if (requestId.equals("null")) requestId = "";
        userType = PreferenceConnector.readInteger(MainActivity.this, PreferenceConnector.UserType, 0);
        signInRespoce = new Gson().fromJson(PreferenceConnector.readString(MainActivity.this, PreferenceConnector.userInfoJson, ""), SignInRespoce.class);
        updateFirebaseToken();
        initializeView();
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                           @Override
                                           public void onDrawerSlide(View drawerView, float slideOffset) {
                                               //labelView.setVisibility(slideOffset > 0 ? View.VISIBLE : View.GONE);

                                               // Scale the View based on current slide offset
                                               final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                                               final float offsetScale = 1 - diffScaledOffset;
                                               contentView.setScaleX(offsetScale);
                                               contentView.setScaleY(offsetScale);

                                               // Translate the View, accounting for the scaled width
                                               final float xOffset = drawerView.getWidth() * slideOffset;
                                               final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                                               final float xTranslation = xOffset - xOffsetDiff;  //for left side drawer
                                               // final float xTranslation = xOffsetDiff - xOffset; //for right side drawer
                                               contentView.setTranslationX(xTranslation);
                                           }

                                           @Override
                                           public void onDrawerClosed(View drawerView) {
                                               //labelView.setVisibility(View.GONE);
                                           }
                                       }
        );
//from forground notification back
        if (manageAfternotificationBack(bundle)) return;

        //from BackGround Notification
        manageBackGroundNotification();
        chatCount = PreferenceConnector.readInteger(MainActivity.this, PreferenceConnector.chatCount, 0);
        notificationCount = PreferenceConnector.readInteger(MainActivity.this, PreferenceConnector.notiCount, 0);
        ;
        if (notificationCount > 0) {
            notiFrm.setVisibility(View.VISIBLE);
            notiTxt.setText("" + notificationCount);
        }
        if (chatCount > 0) {
            if (userType == Constant.uerTypeConsumer) {
                frthCountFrm.setVisibility(View.VISIBLE);
                frthCountTxt.setText("" + chatCount);
            } else {
                rdCountFrm.setVisibility(View.VISIBLE);
                rdCountTxt.setText("" + chatCount);
            }
        }
    }

    private void manageBackGroundNotification() {
        if (notiType != null) {
            notificationCount = notificationCount + 1;
            try {
                switch (notiType) {
                    case "request":
                        clickId = R.id.secondTabLay;
                        manageSndTabLay();
                        break;
                    case "Start":
                    case "Progress":
                    case "Almost":
                    case "End":
                    case "Ask":
                    case "payFirst":
                    case "reminder":
                        clickId = R.id.fourthTabLay;
                        manageFrthTabLay("pending");
                        break;
                    case "Quote":
                        clickId = R.id.secondTabLay;
                        manageSndTabLay();
                        break;
                    case "Accept":
                        clickId = R.id.fourthTabLay;
                        manageFrthTabLay("pending");
                        break;
                    case "Review":
                    case "paySecond":
                        clickId = R.id.thirdTabLay;
                        manageFrthTabLay("complete");
                        break;
                }
            } catch (Exception e) {

            }
        }
    }

    private boolean manageAfternotificationBack(Bundle bundle) {
        try {
            String go = bundle.getString("GO");
            if (go.equals("two")) {
                clickId = R.id.secondTabLay;
                manageSndTabLay();
            } else if (go.equals("four")) {
                clickId = R.id.fourthTabLay;
                manageFrthTabLay("pending");
            } else if (go.equals("three")) {
                clickId = R.id.thirdTabLay;
                manageRdTabLay("complete");
            } else if (go.equals("chat")) {
                if (userType == Constant.userTypeOwner) {
                    clickId = R.id.rdSlTab;
                    manageRdSlTab();
                } else if (userType == Constant.uerTypeConsumer) {
                    clickId = R.id.frthSlTab;
                    manageFrthSlTab();
                }
            }
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    private void initializeView() {
        footer = findViewById(R.id.footer);
        contentView = findViewById(R.id.contentView);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        menuBtn = findViewById(R.id.menuBtn);
        menuBtn.setOnClickListener(this);
        titleTxt = findViewById(R.id.titleTxt);
        coOrdinateLay = findViewById(R.id.coOrdinateLay);
        //tab
        firstTabLay = findViewById(R.id.firstTabLay);
        firstTabImage = findViewById(R.id.firstTabImage);
        secondTabLay = findViewById(R.id.secondTabLay);
        secondTabImage = findViewById(R.id.secondTabImage);
        thirdTabLay = findViewById(R.id.thirdTabLay);
        thirdTabImage = findViewById(R.id.thirdTabImage);
        fourthTabLay = findViewById(R.id.fourthTabLay);
        fourthTabImage = findViewById(R.id.fourthTabImage);
        fifthTabLay = findViewById(R.id.fifthTabLay);
        fifthTabImage = findViewById(R.id.fifthTabImage);

        firstTabLay.setOnClickListener(this);
        secondTabLay.setOnClickListener(this);
        thirdTabLay.setOnClickListener(this);
        fourthTabLay.setOnClickListener(this);
        fifthTabLay.setOnClickListener(this);

        fstTbVw = findViewById(R.id.fstTbVw);
        ndTbVw = findViewById(R.id.ndTbVw);
        rdTbVw = findViewById(R.id.rdTbVw);
        frthTbVw = findViewById(R.id.frthTbVw);
        fifthTbVw = findViewById(R.id.fifthTbVw);
        //slide
        fstSlTab = findViewById(R.id.fstSlTab);
        sndSlTab = findViewById(R.id.sndSlTab);
        rdSlTab = findViewById(R.id.rdSlTab);
        frthSlTab = findViewById(R.id.frthSlTab);
        fifthSlTab = findViewById(R.id.fifthSlTab);
        sxthSlTab = findViewById(R.id.sxthSlTab);
        svnthSlTab = findViewById(R.id.svnthSlTab);
        ethSlTab = findViewById(R.id.ethSlTab);

        rdCountFrm = findViewById(R.id.rdCountFrm);
        rdCountTxt = findViewById(R.id.rdCountTxt);
        frthCountFrm = findViewById(R.id.frthCountFrm);
        frthCountTxt = findViewById(R.id.frthCountTxt);

        notiFrm = findViewById(R.id.notiFrm);
        notiTxt = findViewById(R.id.notiTxt);

        fstSlTab.setOnClickListener(this);
        sndSlTab.setOnClickListener(this);
        rdSlTab.setOnClickListener(this);
        frthSlTab.setOnClickListener(this);
        fifthSlTab.setOnClickListener(this);
        sxthSlTab.setOnClickListener(this);
        svnthSlTab.setOnClickListener(this);
        ethSlTab.setOnClickListener(this);
        //

        fm = getSupportFragmentManager();
        progressDialog = new ProgressDialog(MainActivity.this);
        initialUiByUserType();
    }

    private void initialUiByUserType() {
        if (userType == Constant.userTypeOwner) {
            initializeOwnerUi();
        } else if (userType == Constant.uerTypeConsumer) {
            initializeConsumerUi();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuBtn:
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
                break;
            case R.id.firstTabLay:
                if (clickId != R.id.firstTabLay) {
                    clickId = R.id.firstTabLay;
                    manageFirstTabLay();
                }
                break;
            case R.id.secondTabLay:
                if (clickId != R.id.secondTabLay) {
                    clickId = R.id.secondTabLay;
                    manageSndTabLay();
                }
                break;
            case R.id.thirdTabLay:
                if (clickId != R.id.thirdTabLay && clickId != R.id.fstSlTab) {
                    clickId = R.id.thirdTabLay;
                    manageRdTabLay("");
                }
                break;
            case R.id.fourthTabLay:
                if (clickId != R.id.fourthTabLay) {
                    clickId = R.id.fourthTabLay;
                    manageFrthTabLay("pending");
                }
                break;
            case R.id.fifthTabLay:
                if (clickId != R.id.fifthTabLay) {
                    clickId = R.id.fifthTabLay;
                    MainActivity.notificationCount = 0;
                    notiFrm.setVisibility(View.GONE);
                    manageFifthTabLay();
                }
                break;
            case R.id.fstSlTab:
                drawerLayout.closeDrawer(navigationView);
                if (clickId != R.id.thirdTabLay && clickId != R.id.fstSlTab) {
                    clickId = R.id.fstSlTab;
                    manageFstSlTab();
                }
                break;
            case R.id.sndSlTab:
                drawerLayout.closeDrawer(navigationView);
                if (clickId != R.id.sndSlTab) {
                    clickId = R.id.sndSlTab;
                    manageSndSlTab();
                }
                break;
            case R.id.rdSlTab:
                drawerLayout.closeDrawer(navigationView);
                if (clickId != R.id.rdSlTab) {
                    clickId = R.id.rdSlTab;
                    manageRdSlTab();
                }
                break;
            case R.id.frthSlTab:
                drawerLayout.closeDrawer(navigationView);
                if (clickId != R.id.frthSlTab) {
                    clickId = R.id.frthSlTab;
                    manageFrthSlTab();
                }
                break;
            case R.id.fifthSlTab:
                drawerLayout.closeDrawer(navigationView);
                if (userType == Constant.uerTypeConsumer) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto: sam_eskn@yahoo.com"));
                    startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                } else {
                    if (clickId != R.id.fifthSlTab) {
                        clickId = R.id.fifthSlTab;
                        manageFifthSlTab();
                    }
                }
                break;
            case R.id.sxthSlTab:
                drawerLayout.closeDrawer(navigationView);
                if (userType == Constant.userTypeOwner) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto: sam_eskn@yahoo.com"));
                    startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                } else {
                    if (clickId != R.id.sxthSlTab) {
                        clickId = R.id.sxthSlTab;
                        manageSixSlTab();
                    }
                }
                break;
            case R.id.svnthSlTab:
                drawerLayout.closeDrawer(navigationView);
                if (clickId != R.id.svnthSlTab) {
                    clickId = R.id.svnthSlTab;
                    manageSvnSlTab();
                }
                break;
            case R.id.ethSlTab:
                /*drawerLayout.closeDrawer(navigationView);
                if (clickId != R.id.ethSlTab) {
                    clickId = R.id.ethSlTab;*/
                manageEthSlTab();
                // }
                break;
        }
    }

    private void initializeConsumerUi() {
        titleTxt.setText(R.string.consumer_fst_tab);
        fstSlTab.setText(getString(R.string.consumer_fst_sl_tab));
        sndSlTab.setText(getString(R.string.consumer_snd_sl_tab));
        rdSlTab.setText(getString(R.string.consumer_rd_sl_tab));
        frthSlTab.setText(getString(R.string.consumer_frth_sl_tab));
        fifthSlTab.setText(getString(R.string.consumer_fifth_sl_tab));
        sxthSlTab.setText(getString(R.string.consumer_sxth_sl_tab));
        svnthSlTab.setText(getString(R.string.consumer_svnth_sl_tab));
        ethSlTab.setText(getString(R.string.consumer_eth_sl_tab));

        firstTabImage.setImageResource(R.drawable.home);
        secondTabImage.setImageResource(R.drawable.online_services);
        thirdTabImage.setImageResource(R.drawable.profile);
        fourthTabImage.setImageResource(R.drawable.my_services);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fourthTabImage.getLayoutParams();
        layoutParams.width = (int) getResources().getDimension(R.dimen._38sdp);
        layoutParams.height = (int) getResources().getDimension(R.dimen._25sdp);
        fourthTabImage.setLayoutParams(layoutParams);

        fifthTabImage.setImageResource(R.drawable.bell);
        fstTbVw.setVisibility(View.VISIBLE);

        if (requestId.equals("")) {
            clickId = R.id.secondTabLay;
            manageSndTabLay();
        }
    }

    private void initializeOwnerUi() {
        titleTxt.setText(R.string.owner_fst_tab);
        fstSlTab.setText(getString(R.string.owner_fst_sl_tab));
        sndSlTab.setText(getString(R.string.owner_snd_sl_tab));
        rdSlTab.setText(getString(R.string.owner_rd_sl_tab));
        if (PreferenceConnector.readBoolean(this, PreferenceConnector.IsBankAccAdd, true)) {
            frthSlTab.setText("Edit Account Info");
        } else {
            frthSlTab.setText(getString(R.string.owner_frth_sl_tab));
        }
        fifthSlTab.setText(getString(R.string.owner_fifth_sl_tab));
        sxthSlTab.setText(getString(R.string.owner_sxth_sl_tab));
        svnthSlTab.setText(getString(R.string.owner_svnth_sl_tab));
        ethSlTab.setText(getString(R.string.owner_eth_sl_tab));

        firstTabImage.setImageResource(R.drawable.home);
        secondTabImage.setImageResource(R.drawable.online_jobs);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) secondTabImage.getLayoutParams();
        layoutParams.width = (int) getResources().getDimension(R.dimen._38sdp);
        layoutParams.height = (int) getResources().getDimension(R.dimen._25sdp);
        secondTabImage.setLayoutParams(layoutParams);

        thirdTabImage.setImageResource(R.drawable.profile);
        fourthTabImage.setImageResource(R.drawable.my_jobs);
        fifthTabImage.setImageResource(R.drawable.bell);
        fstTbVw.setVisibility(View.VISIBLE);

        if (requestId.equals("")) {
            clickId = R.id.secondTabLay;
            manageSndTabLay();
        }
    }

    //sliderClick
    private void manageFstSlTab() {
        tabLineHide();
        drawerLayout.closeDrawer(navigationView);
        rdTbVw.setVisibility(View.VISIBLE);
        footer.setVisibility(View.VISIBLE);
        BaseFragment = "FragmentProfile";
        replaceFragment(FragmentProfile.newInstance(""), false, R.id.fragmentPlace);
        if (userType == Constant.userTypeOwner) {
            titleTxt.setText(R.string.owner_fst_sl_tab);
        } else {
            titleTxt.setText(R.string.consumer_fst_sl_tab);
        }
    }

    private void manageSndSlTab() {

        drawerLayout.closeDrawer(navigationView);
        footer.setVisibility(View.GONE);
        if (userType == Constant.userTypeOwner) {
            titleTxt.setText(R.string.owner_snd_sl_tab);
            replaceFragment(new OnDevelopmentFragment(), false, R.id.fragmentPlace);
        } else {
            titleTxt.setText(R.string.consumer_snd_sl_tab);
            replaceFragment(new FragmentNearby(), false, R.id.fragmentPlace);
        }

    }

    private void manageRdSlTab() {
        tabLineHide();
        drawerLayout.closeDrawer(navigationView);
        rdTbVw.setVisibility(View.VISIBLE);
        footer.setVisibility(View.GONE);

        if (userType == Constant.userTypeOwner) {
            titleTxt.setText(R.string.owner_rd_sl_tab);
            BaseFragment = "ChatFragment";
            rdCountFrm.setVisibility(View.GONE);
            chatCount = 0;
            PreferenceConnector.writeInteger(this, PreferenceConnector.chatCount, chatCount);
            replaceFragment(new ChatFragment(), false, R.id.fragmentPlace);
        } else {
            titleTxt.setText(R.string.consumer_rd_sl_tab);
            addFragment(new OnDevelopmentFragment(), false, R.id.fragmentPlace);
        }
    }

    private void manageFrthSlTab() {
        drawerLayout.closeDrawer(navigationView);
        footer.setVisibility(View.GONE);

        if (userType == Constant.userTypeOwner) {
            if (PreferenceConnector.readBoolean(this, PreferenceConnector.IsBankAccAdd, true)) {
                titleTxt.setText("Update Account Info");
            } else {
                titleTxt.setText(R.string.owner_frth_sl_tab);
            }

            replaceFragment(new FragmentAddEditAccount(), false, R.id.fragmentPlace);
        } else {
            titleTxt.setText(R.string.consumer_frth_sl_tab);
            BaseFragment = "ChatFragment";
            frthCountFrm.setVisibility(View.GONE);
            chatCount = 0;
            PreferenceConnector.writeInteger(this, PreferenceConnector.chatCount, chatCount);
            replaceFragment(new ChatFragment(), false, R.id.fragmentPlace);
        }
    }

    private void manageFifthSlTab() {
        drawerLayout.closeDrawer(navigationView);
        footer.setVisibility(View.GONE);

        if (userType == Constant.userTypeOwner) {
            titleTxt.setText(R.string.owner_fifth_sl_tab);
            BaseFragment = "FragmentPromoCode";
            replaceFragment(new FragmentPromoCode(), false, R.id.fragmentPlace);
        } else {
            clickId = R.id.fifthSlTab;
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto: sam_eskn@yahoo.com"));
            startActivity(Intent.createChooser(emailIntent, "Send feedback"));
        }
    }

    private void manageSixSlTab() {
        drawerLayout.closeDrawer(navigationView);
        footer.setVisibility(View.GONE);
        addFragment(new OnDevelopmentFragment(), true, R.id.fragmentPlace);
        if (userType == Constant.userTypeOwner) {
            addFragment(new OnDevelopmentFragment(), true, R.id.fragmentPlace);
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto: sam_eskn@yahoo.com"));
            startActivity(Intent.createChooser(emailIntent, "Send feedback"));

            //startActivity(Intent.createChooser(intent, "Send Email"));

        } else {
            titleTxt.setText(R.string.consumer_sxth_sl_tab);
        }
    }

    private void manageSvnSlTab() {
        drawerLayout.closeDrawer(navigationView);
        footer.setVisibility(View.GONE);
        replaceFragment(new FragmentTermsPolicies(), false, R.id.fragmentPlace);
        if (userType == Constant.userTypeOwner) {
            titleTxt.setText(R.string.owner_svnth_sl_tab);
        } else {
            titleTxt.setText(R.string.consumer_svnth_sl_tab);
        }
    }

    private void manageEthSlTab() {
        doLogOut();
    }

    //tabBarClick
    private void manageFirstTabLay() {
        tabLineHide();
        fstTbVw.setVisibility(View.VISIBLE);
        replaceFragment(new FragmentFeed(), false, R.id.fragmentPlace);
        if (userType == Constant.userTypeOwner) {
            titleTxt.setText(R.string.owner_fst_tab);
        } else {
            titleTxt.setText(R.string.consumer_fst_tab);
        }
    }

    private void manageSndTabLay() {
        tabLineHide();
        ndTbVw.setVisibility(View.VISIBLE);
        if (userType == Constant.userTypeOwner) {
            titleTxt.setText(R.string.owner_snd_tab);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) secondTabImage.getLayoutParams();
            layoutParams.width = (int) getResources().getDimension(R.dimen._38sdp);
            layoutParams.height = (int) getResources().getDimension(R.dimen._25sdp);
            secondTabImage.setLayoutParams(layoutParams);
        } else {
            titleTxt.setText(R.string.consumer_snd_tab);
        }
        BaseFragment = "OnlineJobsNServicesFragment";
        replaceFragment(OnlineJobsNServicesFragment.newInstance(requestId), false, R.id.fragmentPlace);
        requestId = "";
    }

    public void manageRdTabLay(String state) {
        clickId = R.id.thirdTabLay;
        tabLineHide();
        rdTbVw.setVisibility(View.VISIBLE);
        BaseFragment = "FragmentProfile";
        replaceFragment(FragmentProfile.newInstance(state), false, R.id.fragmentPlace);
        if (userType == Constant.userTypeOwner) {
            titleTxt.setText(R.string.owner_rd_tab);
        } else {
            titleTxt.setText(R.string.consumer_rd_tab);
        }

    }

    private void manageFrthTabLay(String state) {
        tabLineHide();
        frthTbVw.setVisibility(View.VISIBLE);
        if (userType == Constant.userTypeOwner) {
            titleTxt.setText(R.string.owner_frth_tab);
            BaseFragment = "FragmentMyJobs";
            replaceFragment(FragmentMyJobs.newInstance(requestId, state), false, R.id.fragmentPlace);
        } else {
            titleTxt.setText(R.string.consumer_frth_tab);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fourthTabImage.getLayoutParams();
            layoutParams.width = (int) getResources().getDimension(R.dimen._38sdp);
            layoutParams.height = (int) getResources().getDimension(R.dimen._25sdp);
            fourthTabImage.setLayoutParams(layoutParams);
            BaseFragment = "FragmentMyServices";
            replaceFragment(FragmentMyServices.newInstance(requestId, state), false, R.id.fragmentPlace);
        }
        requestId = "";
    }

    private void manageFifthTabLay() {
        tabLineHide();
        fifthTbVw.setVisibility(View.VISIBLE);
        notificationCount = 0;
        PreferenceConnector.writeInteger(this, PreferenceConnector.notiCount, notificationCount);
        BaseFragment = "FragmentNotifications";
        replaceFragment(new FragmentNotifications(), false, R.id.fragmentPlace);
        if (userType == Constant.userTypeOwner) {
            titleTxt.setText(R.string.owner_fifth_tab);
        } else {
            titleTxt.setText(R.string.consumer_fifth_tab);
        }
    }

    private void tabLineHide() {
        fstTbVw.setVisibility(View.GONE);
        ndTbVw.setVisibility(View.GONE);
        rdTbVw.setVisibility(View.GONE);
        frthTbVw.setVisibility(View.GONE);
        fifthTbVw.setVisibility(View.GONE);
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
            //   transaction.setCustomAnimations(R.anim.slide_right_out, R.anim.slide_right_in);
            transaction.replace(containerId, fragment, backStackName).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    public void addFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = getFragmentManager().popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //   transaction.setCustomAnimations(R.anim.slide_right_out, R.anim.slide_right_in);
            transaction.add(containerId, fragment, backStackName).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (fm.getBackStackEntryCount() > 0) {
                int backStackEntryCount = fm.getBackStackEntryCount();
                Fragment fragment = fm.getFragments().get(backStackEntryCount - 1);
                if (fragment != null) {
                    fragment.onResume();
                }
                fm.popBackStackImmediate();
            } else {
                Handler handler = new Handler();
                if (!doubleBackToExitPressedOnce) {
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();
                    //Constant.snackbar(coOrdinateLay, "Click again to exit");
                    handler.postDelayed(runnable = new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                } else {
                    handler.removeCallbacks(runnable);
                    super.onBackPressed();
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "onBackPressed: " + e.getMessage().toString());

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.this.registerReceiver(receiver, new IntentFilter("NOTIFICATIONCOUNT"));
        if (userType == Constant.uerTypeConsumer) {
            getMyCars();
        } else if (userType == Constant.userTypeOwner) {
            getServices();
        }
    }

    public void getMyCars() {
        if (Constant.isNetworkAvailable(this, coOrdinateLay)) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getMyCarUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("#" + response);
                            Log.e("BRAND", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
          /*                          GetMyCarResponce getMyCarResponce = gson.fromJson(response, GetMyCarResponce.class);*/
                                    PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.myCarListJson, response);
                                    if (serviceNVehicleInterface != null)
                                        serviceNVehicleInterface.onRefresh();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(MainActivity.this);
                                } else if (status.equalsIgnoreCase("FAIL")) {
                                    PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.myCarListJson, response);
                                    if (serviceNVehicleInterface != null)
                                        serviceNVehicleInterface.onRefresh();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Constant.errorHandle(error, MainActivity.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(MainActivity.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


               /* @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("carId", carId);
                    String string = new JSONObject(params).toString();
                    return string.getBytes();
                }*/

                /*@Override
                public String getBodyContentType() {
                    return "application/json; charset=" + getParamsEncoding();
                }*/
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
        }
    }

    public void doLogOut() {
        if (Constant.isNetworkAvailable(this, coOrdinateLay)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.logOutUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println("#" + response);
                            Log.e("BRAND", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    drawerLayout.closeDrawer(navigationView);
                                    footer.setVisibility(View.GONE);
                                    FirebaseDatabase.getInstance()
                                            .getReference()
                                            .child(Constant.ARG_USERS)
                                            .child(String.valueOf(signInRespoce.getData().get_id()))
                                            .child("firebaseToken").setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Constant.logout(MainActivity.this);
                                        }
                                    });
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    drawerLayout.closeDrawer(navigationView);
                                    footer.setVisibility(View.GONE);
                                    Constant.logout(MainActivity.this);
                                } else {
                                    drawerLayout.closeDrawer(navigationView);
                                    footer.setVisibility(View.GONE);
                                    Constant.logout(MainActivity.this);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Constant.errorHandle(error, MainActivity.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(MainActivity.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


               /* @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("carId", carId);
                    String string = new JSONObject(params).toString();
                    return string.getBytes();
                }*/

                /*@Override
                public String getBodyContentType() {
                    return "application/json; charset=" + getParamsEncoding();
                }*/
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
        }
    }

    public void getServices() {
        if (Constant.isNetworkAvailable(this, coOrdinateLay)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getServicesUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("#" + response);
                            Log.e("MyServices", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                  /*  AddServicesResponce addServicesResponce = gson.fromJson(response, AddServicesResponce.class);*/
                                    PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.myServiceListJson, response);
                                    if (serviceNVehicleInterface != null)
                                        serviceNVehicleInterface.onRefresh();

                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(MainActivity.this);
                                } else {
                                    // Constant.snackbar(coOrdinateLay, message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Constant.errorHandle(error, MainActivity.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(MainActivity.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
        }
    }

    public void setListner(ServiceNVehicleInterface serviceNVehicleInterface) {
        this.serviceNVehicleInterface = serviceNVehicleInterface;
    }

    private void updateFirebaseToken() {
        ChatUserBean chatUserBean = new ChatUserBean();
        chatUserBean.email = signInRespoce.getData().getEmail();
        chatUserBean.firebaseId = String.valueOf(signInRespoce.getData().get_id());
        chatUserBean.firebaseToken = signInRespoce.getData().getDeviceToken();
        chatUserBean.name = signInRespoce.getData().getUserName();
        chatUserBean.profilePic = signInRespoce.getData().getProfileImage();
        chatUserBean.uid = String.valueOf(signInRespoce.getData().get_id());
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constant.ARG_USERS)
                .child(String.valueOf(signInRespoce.getData().get_id()))
                .setValue(chatUserBean).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public void manageSndTabAfterPayment() {
        clickId = R.id.secondTabLay;
        tabLineHide();
        ndTbVw.setVisibility(View.VISIBLE);
        footer.setVisibility(View.VISIBLE);
        if (userType == Constant.userTypeOwner) {
            titleTxt.setText(R.string.owner_snd_tab);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) secondTabImage.getLayoutParams();
            layoutParams.width = (int) getResources().getDimension(R.dimen._38sdp);
            layoutParams.height = (int) getResources().getDimension(R.dimen._25sdp);
            secondTabImage.setLayoutParams(layoutParams);
        } else {
            titleTxt.setText(R.string.consumer_snd_tab);
        }
        BaseFragment = "OnlineJobsNServicesFragment";
        replaceFragment(OnlineJobsNServicesFragment.newInstance(requestId), false, R.id.fragmentPlace);
        requestId = "";
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            countType = intent.getStringExtra(Constant.COUNTYPE);
            if (countType.equals("chatCount")) {
                if (!MainActivity.BaseFragment.equals("ChatFragment")) {
                    if (userType == Constant.uerTypeConsumer) {
                        frthCountFrm.setVisibility(View.VISIBLE);
                        frthCountTxt.setText("" + chatCount);
                    } else {
                        rdCountFrm.setVisibility(View.VISIBLE);
                        rdCountTxt.setText("" + chatCount);
                    }
                }
            } else {
                if (!MainActivity.BaseFragment.equals("FragmentNotifications")) {
                    notiFrm.setVisibility(View.VISIBLE);
                    notiTxt.setText("" + notificationCount);
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterWifiReceiver();
    }

    private void unregisterWifiReceiver() {
        MainActivity.this.unregisterReceiver(receiver);
    }
}
