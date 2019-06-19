package com.cara2v.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.support.design.widget.CoordinatorLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.Interface.MyOnCheckListioner;
import com.cara2v.R;
import com.cara2v.adapter.NearByOwnerAdapter;
import com.cara2v.responceBean.GetNearestOwnerResponce;
import com.cara2v.ui.activity.UserProfileLayoutActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;
import static com.cara2v.util.Constant.MY_PERMISSIONS_REQUEST_LOCATION;

public class FragmentNearby extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LocationListener, MyOnCheckListioner {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView nearbyRecycler;
    private LinearLayout visibleLayout;
    private ProgressBar progressBar;
    private CoordinatorLayout snackLayout;
    private Context mContext;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private String latitude = "";
    String longitude = "";
    private ArrayList<GetNearestOwnerResponce.DataBean> nearByOwnerList = new ArrayList<>();
    private NearByOwnerAdapter nearByOwnerAdapter;
    private LatLng currentLatLng;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
    private boolean isFirstTime=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nearby, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.setClickable(true);
        view.requestFocus();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        initializeView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        isFirstTime=false;
        checkLocationability();
    }

    private void initializeView(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        nearbyRecycler = view.findViewById(R.id.nearbyRecycler);
        visibleLayout = view.findViewById(R.id.visibleLayout);
        progressBar = view.findViewById(R.id.progressBar);
        snackLayout = view.findViewById(R.id.snackLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        getNearest(latitude, longitude);
    }

    public void getNearest(final String latitude, final String longitude) {
        if (Constant.isNetworkAvailable(mContext, snackLayout)) {
            if (!isFirstTime)progressBar.setVisibility(View.VISIBLE);
            isFirstTime=true;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getNearestUser,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            swipeRefreshLayout.setRefreshing(false);
                            System.out.println("#" + response);
                            Log.e("NearBy", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                //String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    GetNearestOwnerResponce getNearestOwnerResponce = new Gson().fromJson(response, GetNearestOwnerResponce.class);
                                    updateUi(getNearestOwnerResponce);
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    if (getActivity() != null)
                                        Constant.showLogOutDialog(getActivity());
                                } else {
                                    visibleLayout.setVisibility(View.VISIBLE);
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
                            swipeRefreshLayout.setRefreshing(false);
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
                    params.put("lat", latitude);
                    params.put("long", longitude);
                    params.put("distance", "28");
                    params.put("userType", "1");
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
    private void updateUi(GetNearestOwnerResponce getNearestOwnerResponce) {
        nearByOwnerList.clear();
        nearByOwnerList.addAll(getNearestOwnerResponce.getData());
        nearByOwnerAdapter = new NearByOwnerAdapter(mContext, nearByOwnerList, currentLatLng,this);
        nearbyRecycler.setAdapter(nearByOwnerAdapter);
        if (getNearestOwnerResponce.getData().size() > 0) {
            visibleLayout.setVisibility(View.GONE);
        } else {
            visibleLayout.setVisibility(View.VISIBLE);
        }
    }

    private void checkLocationability() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                getFusedLocation();
            }
        } else {
            if (checkLocationPermissionLowerApi()) {
                try {
                    locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

                    // getting GPS status
                    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                    // getting network status
                    isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    Location location = null;
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        //     locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                            if (location != null) {
                                latitude = String.valueOf(location.getLatitude());
                                longitude = String.valueOf(location.getLongitude());
                                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                getNearest(latitude, longitude);
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            //  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = String.valueOf(location.getLatitude());
                                    longitude = String.valueOf(location.getLongitude());
                                    currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    getNearest(latitude, longitude);
                                } else {
                                    checkLocationability();
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showSettingsAlert();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //(just doing it here for now, note that with this code, no explanation is shown)
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {

            return true;
        }
    }

    private boolean checkLocationPermissionLowerApi() {
        boolean isEnable = true;
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                isEnable = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isEnable;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.cancel();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("TAG", "onRequestPermissionsResult: if");

                    }
                } else {
                    Toast.makeText(mContext, "permission denied", Toast.LENGTH_LONG).show();
                    //Util.displayToast(getActivity(), "YOU DENIED PERMISSION CANNOT SELECT IMAGE", Util.SHORT_TOAST);
                    Log.e("TAG", "onRequestPermissionsResult: else");
                }
            }
        }
    }

    private void getFusedLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.e("TAG", "onSuccess: " + location.getLatitude() + " " + location.getLongitude());
                            String latitude = String.valueOf(location.getLatitude());
                            String longitude = String.valueOf(location.getLongitude());
                            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            getNearest(latitude, longitude);

                        } else {
                            Toast.makeText(mContext, "Please turn on location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void OnCheck(int position) {
        Intent intent = new Intent(mContext, UserProfileLayoutActivity.class);
        intent.putExtra("userId", nearByOwnerList.get(position).get_id());
        startActivity(intent);
    }
}
