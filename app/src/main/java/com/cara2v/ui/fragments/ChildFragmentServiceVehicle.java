package com.cara2v.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.support.design.widget.CoordinatorLayout;
import android.widget.TextView;

import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.Interface.ServicesAddRemoveListioner;
import com.cara2v.R;
import com.cara2v.adapter.GetServiceAdapter;
import com.cara2v.adapter.MyVehicleAdapter;
import com.cara2v.responceBean.AddServicesResponce;
import com.cara2v.responceBean.GetMyCarResponce;
import com.cara2v.ui.activity.MainActivity;
import com.cara2v.ui.activity.MyVehicleInfoLayoutActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ChildFragmentServiceVehicle extends Fragment {

    private RecyclerView vehicleServiceRecycler;
    private LinearLayout visibleLayout;
    private ProgressBar progressBar;
    private CoordinatorLayout snackLayout;
    private TextView noRecordTxt;
    private Context mContext;
    private int userType = 0;
    private Gson gson = new Gson();
    private MyVehicleAdapter myVehicleAdapter;
    private GetServiceAdapter getServiceAdapter;
    private ArrayList<AddServicesResponce.DataBean> dataBeans = new ArrayList<>();
    private int width = 0, height = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.child_fragment_service_vehicle, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userType = PreferenceConnector.readInteger(mContext, PreferenceConnector.UserType, 0);
        initializeView(view);
        updateUi();

        ((MainActivity) getActivity()).setListner(new MainActivity.ServiceNVehicleInterface() {
            @Override
            public void onRefresh() {
                updateUi();
            }
        });
    }

    private void updateUi() {
        if (userType == Constant.uerTypeConsumer) {
            updateUiForConsumer();
        } else if (userType == Constant.userTypeOwner) {
            updateUiForOwner();
        }
    }


    private void initializeView(View view) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        vehicleServiceRecycler = view.findViewById(R.id.vehicleServiceRecycler);
        visibleLayout = view.findViewById(R.id.visibleLayout);
        progressBar = view.findViewById(R.id.progressBar);
        snackLayout = view.findViewById(R.id.snackLayout);
        noRecordTxt = view.findViewById(R.id.noRecordTxt);
    }

    private void updateUiForOwner() {
        String serviceJson = PreferenceConnector.readString(mContext, PreferenceConnector.myServiceListJson, "");
        if (!TextUtils.isEmpty(serviceJson)) {
            AddServicesResponce addServicesResponce = gson.fromJson(serviceJson, AddServicesResponce.class);
            dataBeans.clear();
           /* for (AddServicesResponce.DataBean dataBean : addServicesResponce.getData()) {
                if (dataBean.getStatus().equals("1")) {
                    dataBeans.add(dataBean);
                }
            }*/

            for (AddServicesResponce.DataBean dataBean : addServicesResponce.getData()) {
                if (dataBean.getStatus().equals("1")) {
                    AddServicesResponce.DataBean dataBean1 = new AddServicesResponce.DataBean();
                    ArrayList<AddServicesResponce.DataBean.SubServiceBean> subServiceBeans = new ArrayList<>();
                    for (AddServicesResponce.DataBean.SubServiceBean subServiceBean : dataBean.getSubService()) {
                        if (subServiceBean.getStatus() == 1) {
                            subServiceBeans.add(subServiceBean);
                        }
                    }
                    if (subServiceBeans.size() > 0) {
                        dataBean1.setSubService(subServiceBeans);
                        dataBean1.set_id(dataBean.get_id());
                        dataBean1.setUpd(dataBean.getUpd());
                        dataBean1.setCrd(dataBean.getCrd());
                        dataBean1.setStatus(dataBean.getStatus());
                        dataBean1.setServiceImage(dataBean.getServiceImage());
                        dataBean1.setServiceName(dataBean.getServiceName());
                        dataBean1.set__v(dataBean.get__v());
                        dataBeans.add(dataBean1);
                    }

                }
            }

            if (dataBeans.size() > 0) {
                getServiceAdapter = new GetServiceAdapter(mContext, dataBeans, new RemoveImageListioner() {
                    @Override
                    public void onClick(int position) {
                        popUpServiceInfo(dataBeans.get(position));
                    }
                }, new ServicesAddRemoveListioner() {
                    @Override
                    public void onClick(int position) {

                    }
                }, "profile");
                vehicleServiceRecycler.setAdapter(getServiceAdapter);
                visibleLayout.setVisibility(View.GONE);
            } else {
                visibleLayout.setVisibility(View.VISIBLE);
                if (getServiceAdapter != null) getServiceAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateUiForConsumer() {
        String carJson = PreferenceConnector.readString(mContext, PreferenceConnector.myCarListJson, "");
        if (!TextUtils.isEmpty(carJson)) {
            final GetMyCarResponce getMyCarResponce = gson.fromJson(carJson, GetMyCarResponce.class);
            if (getMyCarResponce.getData().size() > 0) {
                myVehicleAdapter = new MyVehicleAdapter(mContext, getMyCarResponce.getData(), new RemoveImageListioner() {
                    @Override
                    public void onClick(int position) {
                        GetMyCarResponce.DataBean carBean = getMyCarResponce.getData().get(position);
                        String carBeanJson = gson.toJson(carBean);
                        Log.e("carBeanJson", carBeanJson);
                        Intent intent = new Intent(getActivity(), MyVehicleInfoLayoutActivity.class);
                        intent.putExtra("carBeanJson", carBeanJson);
                        startActivity(intent);
                    }
                });
                vehicleServiceRecycler.setAdapter(myVehicleAdapter);
                visibleLayout.setVisibility(View.GONE);
            } else {
                visibleLayout.setVisibility(View.VISIBLE);
                if (myVehicleAdapter != null) myVehicleAdapter.notifyDataSetChanged();
            }
        }
    }

    void popUpServiceInfo(AddServicesResponce.DataBean dataBean) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_service_info);
        dialog.getWindow().setLayout((width * 10) / 11, (height * 1) / 2);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        final ImageButton cancel = (ImageButton) dialog.findViewById(R.id.popCancel);
        final TextView popTitle = (TextView) dialog.findViewById(R.id.popTitle);
        final TextView popDescription = (TextView) dialog.findViewById(R.id.popDescription);
        popTitle.setText(dataBean.getServiceName());
        popDescription.setText(dataBean.getServiceDescription());
        dialog.setCancelable(true);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
