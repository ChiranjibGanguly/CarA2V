package com.cara2v.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.cara2v.ui.activity.WelcomeActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by mindiii on 10/8/16.
 */
public class Constant {
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE   = 102;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA                   =103;
    public static final int MY_PERMISSIONS_REQUEST_INTERNET                 =104;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION                 =105;
    public static final int REQUEST_CAMERA      = 111;
    public static final int SELECT_FILE         = 234;
    public static final int REQUEST_PICTURE     = 124;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final int GPS_RESULT_CODE = 126;

    public static final int INTENTCAMERA        = 1000;
    public static final int INTENTGALLERY       = 1100;
    public static final int INTENTREQUESTCAMERA = 1200;
    public static final int INTENTREQUESTREAD   = 1300;
    public static final int INTENTREQUESTWRITE  = 1400;
    public static final int MEETING             = 1500;
    public static final int INTENTREQUESTNET    = 1600;

    public static final String FACEBOOK = "facebook";
    public static final int GOOGLE = 123;
    public static final String GOOGLE_LOGIN = "google";
    public static final String USER_AUTHENTICATION_SUCCESS = "User authentication successfully done!";
    public static final String STATUS = "status";
    public static final String MESSAGE = "message";
    public static final String SUCCESS = "success";

    public static String msg = "Please Login Again";
    public static String title = "Session Expired";
    public static String ok = "LogOut";

    //
    public static String notificationQuotes = "Quotes";
    public static String notificationRequest = "request";
    public static String notificationStart = "Start";
    public static String notificationProgress = "Progress";
    public static String notificationAlmost = "Almost";
    public static String notificationEnd = "End";
    public static String notificationAsk = "Ask";
    public static String notificationAccept = "Accept";

    //userType
    public static int userTypeOwner = 1;
    public static int uerTypeConsumer = 2;

    //chat
    public static final String deleteBy = "deleteby";
    public static final String notyficationTitle = "title";
    public static final String notyficationBody = "body";
    public static final String message = "message";
    public static final String rcvName = "username";
    public static final String rcvUId = "uid";
    public static final String rcvFireBaseToken = "firebaseToken";
    public static final String rcvPrflImg = "image";
    public static final String notifactionType = "NotifyType";
    public static final String requestType = "requestType";
    public static final String requestId = "requestId";

    //request Type
    public static final String reqPending = "pending";
    public static final String reqConfirm = "confirm";
    public static final String reqComplete = "complete";
    public static final String reqNone = "";

    public static final String ARG_CHAT_ROOMS = "chat_rooms";
    public static final String ARG_CHAT_HISTORY = "chat_history";
    public static final String ARG_CHAT_BLOCK = "BlockUsers";
    public static final String ARG_CHAT_BLOCK_KEY = "blockedBy";
    public static final String ARG_USERS = "users";
    //url
    //public static final String BASE_URL = "http://cara2v.com/";
    public static final String BASE_URL = "http://cara2v.com:8000/";
    public static final String logInUrl = "service/login";
    public static final String logOutUrl = "service/logout";
    public static final String registrationUrl = "service/registration";
    public static final String updateProfileUrl = "user/updateUser";
    public static final String forgotPasswordUrl = "service/forgotPassword";
    public static final String getCarUrl = "car/getCar";
    public static final String addVehicleUrl = "car/addCar";
    public static final String getServicesUrl = "services/getServices";
    public static final String addRemoveServicesUrl = "services/addAndGetService";
    public static final String getMyCarUrl = "car/getMyCar";
    public static final String addServiceRequestUrl = "request/serviceRequest";
    public static final String updateServiceRequestUrl = "request/UpdateRequest";
    public static final String getMyRequestUrl = "request/getMyRequest";
    public static final String getMyOrderUrl = "request/myOrderList";
    public static final String getCarRequestDetailUrl = "request/requestDetail";
    public static final String sendRequestQuoteUrl = "request/requestInvoice";
    public static final String acceptServiceRequestUrl = "request/requestAccept";
    public static final String getMyJobUrl = "request/getMyJob";
    public static final String changeServiceStatusUrl = "request/changeStatus";
    public static final String addStripeBankAccountUrl = "stripe/addBankAcc";
    public static final String getStripeBankAccountUrl = "stripe/getBankAcc";
    public static final String giveReviewUrl = "request/reviewRating";
    public static final String getUserProfileInfoUrl = "user/userInfo";
    public static final String addSocialPostUrl = "social/socialPost";
    public static final String getMySocialPostUrl = "social/getMySocialPost";
    public static final String getAllSocialPostUrl = "social/getSocialPost";
    public static final String getPostLikeUrl = "social/postLike";
    public static final String getPostCommentUrl = "social/postComment";
    public static final String addUpdatePromoCodeUrl = "request/addPromo";
    public static final String getPromoCodeUrl = "request/getPromo";
    public static final String checkPromoUrl = "request/CheckPromo";
    public static final String updateInvoiceUrl = "request/updateInvoce";
    public static final String bankSecondPayUrl = "stripe/bankSecondPay";
    public static final String bankFirstPayUrl = "stripe/bankFirstPay";
    public static final String cardFirstPayUrl = "stripe/cardFirstPay";
    public static final String cardSecondPayUrl = "stripe/cardSecondPay";
    public static final String getReviewsListUrl = "user/reviewList";
    public static final String getMyNotification = "request/getMyNotify";
    public static final String getNearestUser = "user/nearUser";
    public static final String getTermsNPolicies = "service/getTandC";

    //notification_key
    public static String FROM = "fromm";
    public static String REQUEST_ID = "requestId";
    public static String TITLE = "title";
    public static String MSG = "msg";
    public static String TYPE = "type";
    public static String COUNTYPE = "countType";


    public static final int MY_PERMISSIONS_REQUEST_CEMERA = 1001;

    public static final int READ_CONTACT = 107;

    public static float ADMIN_DEPOSITE = 10;

    public static String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month1 = month + 1;
        return (year + "-" + month1 + "-" + day);
    }

    public static String getCurrentTime() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int munite = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        return (hour + ":" + munite + ":" + sec);

    }


    public static String ConvertMilliSecondsToFormattedDate(String dateTime) {
        try {
            SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            return sfd.format(Long.parseLong(dateTime));
        } catch (Exception e) {

        }
        return "";
    }

    public static String ConvertMilliSecondsToFormattedDateToTime(String dateTime) {
        try {
            SimpleDateFormat sfd = new SimpleDateFormat(/*"yyyy-MM-dd*/"hh:mm a");

            return sfd.format(Long.parseLong(dateTime));
        } catch (Exception e) {

        }
        return "";
    }

    public static String formattedDate(String dateTime) {
        DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        Date startDate;
        try {
            startDate = df.parse(dateTime);
            return df.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dateFormateChange(String date) {

        // String input = "2014-04-25 17:03:13";
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("E, MMM dd, yyyy");
        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dateFormateChangePromoCode(String date) {

        // String input = "2014-04-25 17:03:13";
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dateFormateChangePromoUpdate(String date) {

        // String input = "2014-04-25 17:03:13";
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dateTimeFormateChange(String dateTime) {

        // String input = "2014-04-25 17:03:13";
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       // DateFormat outputFormat = new SimpleDateFormat("EEEE MMM dd',' hh:mm a");
        DateFormat outputFormat = new SimpleDateFormat("EEEE MMM dd");
        try {
            return outputFormat.format(inputFormat.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dateTimeFormateChangeForOnlnList(String dateTime) {

        // String input = "2014-04-25 17:03:13";
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //DateFormat outputFormat = new SimpleDateFormat("MMM dd, hh:mm a");
        DateFormat outputFormat = new SimpleDateFormat("MMM dd");
        try {
            return outputFormat.format(inputFormat.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean checkReturnDateTimeGrater(String departDateTime, String returnDateTime) {
        boolean isgrater = false;

        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            Date startDate = simpleDateFormat.parse(departDateTime);
            Date endDate = simpleDateFormat.parse(returnDateTime);

            //milliseconds
            long different = endDate.getTime() - startDate.getTime();

            System.out.println("startDate : " + startDate);
            System.out.println("endDate : " + endDate);
            System.out.println("different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            Log.e("elapsedDays: ", "" + elapsedDays);
            Log.e("elapsedHours: ", "" + elapsedHours);
            Log.e("elapsedMinutes: ", "" + elapsedMinutes);
            Log.e("elapsedSeconds: ", "" + elapsedSeconds);
            if (elapsedDays > 0) {
                isgrater = true;
            } else if (elapsedDays == 0) {
                if (elapsedHours > 0) {
                    isgrater = true;
                } else if (elapsedHours == 0) {
                    if (elapsedMinutes > 0) {
                        isgrater = true;
                    }
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isgrater;
    }

    public static void delete_file(String filePath) {
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, filePath);

        if (file.exists()) // check if file exist
        {
            file.delete();
        }
    }

    public static String setTimeFormat(String time) {
        String formatedTime = "";
        String[] TimeList = time.split(":");
        String hourSt = TimeList[0];
        String minute = TimeList[1];
        int hour = Integer.parseInt(hourSt);
        String format;
        if (hour == 0) {
            hour += 12;
            format = "A.M.";
        } else if (hour == 12) {
            format = "P.M.";
        } else if (hour > 12) {
            hour -= 12;
            format = "P.M.";
        } else {
            format = "A.M.";
        }
        formatedTime = "" + hour + ":" + minute + " " + format;
        return formatedTime;
    }


    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNetworkAvailable(Context context, View coordinatorLayout) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_SHORT)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return false;
        }
        return false;
    }

    public static void snackbar(View coordinatorLayout, String message) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void errorHandle(VolleyError error, Activity activity) {
        NetworkResponse networkResponse = error.networkResponse;
        String errorMessage = "Unknown error";
        if (networkResponse == null) {
            if (error.getClass().equals(TimeoutError.class)) {
                errorMessage = "Request timeout";
            } else if (error.getClass().equals(NoConnectionError.class)) {
                errorMessage = "Failed to connect server";
            }
        } else {
            String result = new String(networkResponse.data);
            try {
                JSONObject response = new JSONObject(result);

                String status = response.getString("responseCode");
                String message = response.getString("message");

                if (status.equals("300")) {
                    if (activity != null) {
                        showLogOutDialog(activity);
                    }
                }

                Log.e("Error Status", "" + status);
                Log.e("Error Message", message);

                if (networkResponse.statusCode == 404) {
                    errorMessage = "Resource not found";
                } else if (networkResponse.statusCode == 401) {
                    errorMessage = message + " Please login again";
                } else if (networkResponse.statusCode == 400) {
                    errorMessage = message + " Check your inputs";
                } else if (networkResponse.statusCode == 500) {
                    errorMessage = message + " Something is getting wrong";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if (activity != null) {

                }
            }
        }
    }

    public static void printBigLogcat(String TAG, String response) {
        int maxLogSize = 1000;
        for (int i = 0; i <= response.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > response.length() ? response.length() : end;
            Log.e(TAG, response.substring(start, end));
        }
    }

    public static void showAlertDialog_SingleButton(final Activity con, String msg, String title, String ok, String cancel) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(con);
        builder1.setTitle(title);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton(ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //con.finish();
                    }
                });
        builder1.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {

            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void showLogOutDialog(final Activity activity) {
        try {


            AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
            builder1.setTitle(title);
            builder1.setMessage(msg);
            builder1.setCancelable(false);
            builder1.setPositiveButton(ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            if (activity != null) logout(activity);
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logout(Activity activity) {
        NotificationManager nMgr = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
        PreferenceConnector.clear(activity);
        activity.startActivity(new Intent(activity, WelcomeActivity.class));
        activity.finish();
    }

    public static String getDayDifference(String departDateTime, String returnDateTime) {
        boolean isgrater = false;
        String returnDay = "";
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            Date startDate = simpleDateFormat.parse(departDateTime);
            Date endDate = simpleDateFormat.parse(returnDateTime);

            //milliseconds
            long different = endDate.getTime() - startDate.getTime();

            System.out.println("startDate : " + startDate);
            System.out.println("endDate : " + endDate);
            System.out.println("different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            if (elapsedDays == 0) {
                if (elapsedHours == 0) {
                    if (elapsedMinutes == 0) {
                        returnDay = /*elapsedSeconds +*/ " Just now";
                    } else {
                        returnDay = elapsedMinutes + " minutes ago";
                    }
                } else if (elapsedHours == 1) {
                    returnDay = elapsedHours + " hour ago";
                } else {
                    returnDay = elapsedHours + " hours ago";
                }
            } else if (elapsedDays == 1) {
                returnDay =/* elapsedDays + */" yesterday";
            } else {
                returnDay = elapsedDays + " days ago";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDay;
    }

    public static String getAddressFromLocation(Activity activity, double lat, double lng) {
        Geocoder geocoder;
        String totalAddress = "";
        List<Address> addresses = null;
        geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                totalAddress = address + ", " + city + ", " + state;
            } else {
                Log.e("Address", "Not Getting");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("LocationSampleActivity", "IO Exception in getFromLocation()");
            e.printStackTrace();
        }
        return totalAddress;
    }

    public static String DecimalFormat(double value) {
        return DecimalFormat(value, null);
    }

    public static String DecimalFormat(double value, String precision) {
        if (precision == null) {
            precision = "#.00";
        } else if (precision.isEmpty()) {
            precision = "#.00";
        }
        if (value < 1) {
            precision = "0.00";
        }
        DecimalFormat formater = new DecimalFormat(precision);
        return formater.format(value);
    }

   /* public static double roundOff(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }*/

    public static Double distanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return null;
        }

        return (SphericalUtil.computeDistanceBetween(point1, point2) / 1000);
    }
}
