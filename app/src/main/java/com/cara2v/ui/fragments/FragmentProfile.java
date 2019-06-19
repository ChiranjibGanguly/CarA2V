package com.cara2v.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.R;
import com.cara2v.responceBean.MyReviewsResponce;
import com.cara2v.responceBean.SignInRespoce;
import com.cara2v.ui.activity.ActivityEditProfile;
import com.cara2v.ui.activity.consumer.AddVehicleInfoActivity;
import com.cara2v.ui.activity.owner.AddServicesActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.Htps_loader;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentProfile extends Fragment implements View.OnClickListener {

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    private CoordinatorLayout mainLayout;
    // private RecyclerView topRecycler;
    private RelativeLayout imgLay;
    private RelativeLayout colLay;
    private ImageView userProfileImage;
    private ImageView lkUnlkImg;
    private LinearLayout editLay;
    private Toolbar toolbar;
    private LinearLayout infoLay;
    private TextView userNameTxt;
    private AppCompatRatingBar userRating;
    private TextView userReview;
    private TextView addressTxt;
    private RelativeLayout tabLay;
    private LinearLayout fstPrflTab;
    private ImageView fstPrfltabImg;
    private TextView fstPrfltabTxt;
    private LinearLayout sndPrflTab;
    private ImageView sndPrfltabImg;
    private TextView sndPrfltabTxt;
    private LinearLayout trdPrflTab;
    private ImageView trdPrfltabImg;
    private TextView trdPrfltabTxt;
    private FrameLayout childFragmentPlace;
    private ImageView addFabBtn;
    private FrameLayout addFrem;
    private ProgressBar progressBar;

    private Context mContext;
    private Gson gson = new Gson();
    private int userType = 0;
    private int clickedId = 0;
    Picasso picasso;
    private FragmentManager fm;

    public static FragmentProfile newInstance(String go) {
        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putString("Go", go);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        picasso = Htps_loader.get(mContext.getApplicationContext());
        userType = PreferenceConnector.readInteger(mContext, PreferenceConnector.UserType, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeView(view);
        getReviews();
        replaceFragment(new OnDevelopmentFragment(), false, R.id.childFragmentPlace);

        Bundle args = getArguments();
        if (args != null) {
            String go = "";
            go = args.getString("Go");
            if (go.equals("complete")) {
                clickedId = R.id.trdPrflTab;
                inActiveBg();
                trdPrflTab.setBackgroundResource(R.drawable.active_profile_bg);
                trdPrfltabImg.setImageResource(R.drawable.completed_services_active);
                trdPrfltabTxt.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                addFrem.setVisibility(View.GONE);
                replaceFragment(new ChildFragmentMyComJobService(), false, R.id.childFragmentPlace);
            }
        }
    }

    private void initData() {
        String responce = PreferenceConnector.readString(mContext, PreferenceConnector.userInfoJson, "");
        SignInRespoce signInResponce = gson.fromJson(responce, SignInRespoce.class);
        updateUi(signInResponce);
    }


    private void initializeView(View view) {
        fm = getChildFragmentManager();
        appBarLayout = view.findViewById(R.id.appBarLayout);
        collapsingToolbar = view.findViewById(R.id.collapsingToolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        imgLay = view.findViewById(R.id.imgLay);
        colLay = view.findViewById(R.id.colLay);
        userProfileImage = view.findViewById(R.id.userProfileImage);

        lkUnlkImg = view.findViewById(R.id.lkUnlkImg);
        editLay = view.findViewById(R.id.editLay);
        toolbar = view.findViewById(R.id.toolbar);
        infoLay = view.findViewById(R.id.infoLay);
        userNameTxt = view.findViewById(R.id.userNameTxt);
        userRating = view.findViewById(R.id.userRating);
        userReview = view.findViewById(R.id.userReview);
        addressTxt = view.findViewById(R.id.addressTxt);
        tabLay = view.findViewById(R.id.tabLay);
        fstPrflTab = view.findViewById(R.id.fstPrflTab);
        fstPrfltabImg = view.findViewById(R.id.fstPrfltabImg);
        fstPrfltabTxt = view.findViewById(R.id.fstPrfltabTxt);
        sndPrflTab = view.findViewById(R.id.sndPrflTab);
        sndPrfltabImg = view.findViewById(R.id.sndPrfltabImg);
        sndPrfltabTxt = view.findViewById(R.id.sndPrfltabTxt);
        trdPrflTab = view.findViewById(R.id.trdPrflTab);
        trdPrfltabImg = view.findViewById(R.id.trdPrfltabImg);
        trdPrfltabTxt = view.findViewById(R.id.trdPrfltabTxt);
        childFragmentPlace = view.findViewById(R.id.childFragmentPlace);
        addFabBtn = view.findViewById(R.id.addFabBtn);
        addFrem = view.findViewById(R.id.addFrem);
        progressBar = view.findViewById(R.id.progressBar);
        fstPrflTab.setOnClickListener(this);
        sndPrflTab.setOnClickListener(this);
        trdPrflTab.setOnClickListener(this);
        addFrem.setOnClickListener(this);
        editLay.setOnClickListener(this);
        clickedId = fstPrflTab.getId();
        manageTab();
    }

    private void manageTab() {
        if (userType == Constant.userTypeOwner) {
            sndPrfltabImg.setImageResource(R.drawable.prfl_service_inactive);
            addFabBtn.setImageResource(R.drawable.add_service);
            sndPrfltabTxt.setText("Services");
            trdPrfltabTxt.setText(Html.fromHtml("Completed" + "<br/>" + "Jobs"));
        } else if (userType == Constant.uerTypeConsumer) {
            sndPrfltabImg.setImageResource(R.drawable.profile_vehicle_inactive);
            sndPrfltabTxt.setText("My Vehicle");
            trdPrfltabTxt.setText(Html.fromHtml("Completed" + "<br/>" + "Services"));
            addFabBtn.setImageResource(R.drawable.add_vehicle);
        }
    }

    private void updateUi(final SignInRespoce signInResponce) {
        String imgPath = signInResponce.getData().getProfileImage();
        Log.e("updateUi", "profile: " + imgPath);
        if (!TextUtils.isEmpty(imgPath)) {
            try {
                picasso.load(imgPath)
                        .placeholder(R.drawable.user_place_holder)
                        //.resize(userProfileImage.getWidth(), userProfileImage.getHeight())
                        .fit()
                        .into(userProfileImage, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (userType == Constant.uerTypeConsumer) {
            userNameTxt.setText(signInResponce.getData().getUserName());
        } else if (userType == Constant.userTypeOwner) {
            userNameTxt.setText(signInResponce.getData().getBusinessName());
        }
        addressTxt.setText(signInResponce.getData().getAddress());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fstPrflTab:
                if (clickedId != R.id.fstPrflTab) {
                    clickedId = R.id.fstPrflTab;
                    inActiveBg();
                    fstPrflTab.setBackgroundResource(R.drawable.active_profile_bg);
                    fstPrfltabImg.setImageResource(R.drawable.profile_post_active);
                    fstPrfltabTxt.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    addFrem.setVisibility(View.GONE);
                    replaceFragment(new OnDevelopmentFragment(), false, R.id.childFragmentPlace);
                }

                break;
            case R.id.sndPrflTab:
                if (clickedId != R.id.sndPrflTab) {
                    clickedId = R.id.sndPrflTab;
                    inActiveBg();
                    sndPrflTab.setBackgroundResource(R.drawable.active_profile_bg);
                    sndPrfltabTxt.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    if (userType == Constant.uerTypeConsumer) {
                        sndPrfltabImg.setImageResource(R.drawable.profile_vehicle_active);
                    } else if (userType == Constant.userTypeOwner) {
                        sndPrfltabImg.setImageResource(R.drawable.prfl_service_active);
                    }
                    addFrem.setVisibility(View.VISIBLE);
                    replaceFragment(new ChildFragmentServiceVehicle(), false, R.id.childFragmentPlace);
                }
                break;
            case R.id.trdPrflTab:
                if (clickedId != R.id.trdPrflTab) {
                    clickedId = R.id.trdPrflTab;
                    inActiveBg();
                    trdPrflTab.setBackgroundResource(R.drawable.active_profile_bg);
                    trdPrfltabImg.setImageResource(R.drawable.completed_services_active);
                    trdPrfltabTxt.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    addFrem.setVisibility(View.GONE);
                    replaceFragment(new ChildFragmentMyComJobService(), false, R.id.childFragmentPlace);
                }
                break;
            case R.id.addFrem:
                if (userType == Constant.userTypeOwner) {
                    if (PreferenceConnector.readBoolean(mContext, PreferenceConnector.IsLcnAprv, false)) {
                        Intent intent = new Intent(getActivity(), AddServicesActivity.class);
                        intent.putExtra("FROM", "");
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "License approval is pending, login again to check", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Intent intent = new Intent(getActivity(), AddVehicleInfoActivity.class);
                    intent.putExtra("FROM", "");
                    startActivity(intent);
                }
                break;
            case R.id.editLay:
                Intent intent = new Intent(mContext, ActivityEditProfile.class);
                startActivity(intent);
                break;
        }
    }

    private void inActiveBg() {
        fstPrflTab.setBackgroundResource(R.drawable.profile_bg);
        sndPrflTab.setBackgroundResource(R.drawable.profile_bg);
        trdPrflTab.setBackgroundResource(R.drawable.profile_bg);
        fstPrfltabImg.setImageResource(R.drawable.profile_post__inactive);
        fstPrfltabTxt.setTextColor(ContextCompat.getColor(mContext, R.color.colorInActiveText));
        sndPrfltabTxt.setTextColor(ContextCompat.getColor(mContext, R.color.colorInActiveText));
        trdPrfltabTxt.setTextColor(ContextCompat.getColor(mContext, R.color.colorInActiveText));
        trdPrfltabImg.setImageResource(R.drawable.completed_services_inactive);

        if (userType == Constant.uerTypeConsumer) {
            sndPrfltabImg.setImageResource(R.drawable.profile_vehicle_inactive);
        } else if (userType == Constant.userTypeOwner) {
            sndPrfltabImg.setImageResource(R.drawable.prfl_service_inactive);
        }
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        try {
            String backStackName = fragment.getClass().getName();
            int i = fm.getBackStackEntryCount();
            if (i > 0) {
                boolean fragmentPopped = getFragmentManager().popBackStackImmediate(backStackName, 0);
                while (i > 0) {
                    fm.popBackStackImmediate();
                    i--;
                }
                if (!fragmentPopped) {
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    // transaction.setCustomAnimations(R.anim.slide_right_out, R.anim.slide_right_in);
                    transaction.replace(containerId, fragment, backStackName).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    if (addToBackStack)
                        transaction.addToBackStack(backStackName);
                    transaction.commit();
                }
            } else {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.slide_right_out, R.anim.slide_right_in);
                transaction.replace(containerId, fragment, backStackName).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                if (addToBackStack)
                    transaction.addToBackStack(backStackName);
                transaction.commit();
            }

        } catch (Exception e) {
            Log.e("Tranjection", e.getMessage());
        }
    }

    public void getReviews() {
        if (Constant.isNetworkAvailable(mContext, mainLayout)) {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getReviewsListUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            System.out.println("#" + response);
                            Log.e("MyReviews", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    MyReviewsResponce myReviewsResponce = new Gson().fromJson(response, MyReviewsResponce.class);
                                    updateReviewRating(myReviewsResponce);
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    if (getActivity() != null)
                                        Constant.showLogOutDialog(getActivity());
                                } else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            Constant.errorHandle(error, getActivity());
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(mContext, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userId", "");
                    String string = new JSONObject(params).toString();
                    return string.getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=" + getParamsEncoding();
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
        }
    }

    private void updateReviewRating(MyReviewsResponce myReviewsResponce) {
        if (myReviewsResponce.getData().size() > 0) {
            float rating = 0;
            for (MyReviewsResponce.DataBean dataBean : myReviewsResponce.getData()) {
                rating = rating + dataBean.getRating();
            }
            float finalRating = rating / myReviewsResponce.getData().size();
            userRating.setRating(finalRating);
            userReview.setText(myReviewsResponce.getData().size() + " Reviews");
        }
    }
}
