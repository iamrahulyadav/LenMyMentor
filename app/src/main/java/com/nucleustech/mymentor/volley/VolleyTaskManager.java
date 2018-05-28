package com.nucleustech.mymentor.volley;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.constant.Consts;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("ShowToast")
/**
 * @author raisahab.ritwik
 */
public class VolleyTaskManager extends ServiceConnector {
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private String TAG = "";
    private String tag_json_obj = "jobj_req";
    private boolean isToShowDialog = true, isToHideDialog = true;


    public VolleyTaskManager(Context context) {
        mContext = context;

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading...");
        TAG = mContext.getClass().getSimpleName();

    }

    public void showProgressDialog() {
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    /**
     * Making json object request
     */
    private void makeJsonObjReq(int method, String url, final Map<String, String> paramsMap) {

        Log.e("TAG", "" + isToShowDialog);
        if (isToShowDialog) {
            showProgressDialog();
        }

        Log.e("JSONObject", new JSONObject(paramsMap).toString());
        //generateNoteOnSD(mContext, "Request.txt", "" + new JSONObject(paramsMap).toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(method, url, new JSONObject(paramsMap),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, response.toString());
                        // msgResponse.setText(response.toString());
                        if (isToHideDialog) {
                            hideProgressDialog();
                        }
                        // TODO On getting successful result:
                        ((ServerResponseCallback) mContext).onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                VolleyLog.e(TAG, "Error: " + error.getMessage());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.e("error ocurred", "TimeoutError");
                    Toast.makeText(mContext, mContext.getString(R.string.response_timeout), Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Log.e("error ocurred", "AuthFailureError");
                    Toast.makeText(mContext, mContext.getString(R.string.auth_failure), Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Log.e("error ocurred", "ServerError");
                    Toast.makeText(mContext, mContext.getString(R.string.server_error), Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Log.e("error ocurred", "NetworkError");
                    Toast.makeText(mContext, mContext.getString(R.string.network_error), Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Log.e("error ocurred", "ParseError");
                    error.printStackTrace();
                    Toast.makeText(mContext, mContext.getString(R.string.parse_error), Toast.LENGTH_LONG).show();
                }

                ((ServerResponseCallback) mContext).onError();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {

                return paramsMap;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    //*****************************************
    //********* GET ***************************
    //*****************************************

    /**
     * Service GET method to get the current play-store versionCode-->
     **/

    public void doGetAppVersionCodeFromPlaystore(String currentVersionCode) {

        this.isToHideDialog = true;

        String url = getVersionCodeURL() + currentVersionCode;
        int method = Method.GET;

        Log.e("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }


    /**
     * Service GET method to check Search Option
     */
    public void doGetShowSearchOption() {
    /*	this.isToHideDialog = true;
		isToShowDialog = false;
		String url = Consts.SEARCH_OPTION_URL ;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());*/
    }

    /**
     * Service GET method to get the current play-store versionCode-->
     **/

    public void doGetYoutubePlaylist(String playlistCode) {

        this.isToHideDialog = true;

        String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + playlistCode
                + "&maxResults=25&key=AIzaSyC-YyIxbDLPMw0_nkLjj_BfpVozRf84pEM";
        int method = Method.GET;

        Log.e("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    //*****************************************
    //********* POST **************************
    //*****************************************

    /**
     * Service method calling for Login -->
     **/

    public void doGetStudentList(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = Consts.GET_STUDENT_LIST;
        int method = Method.POST;

        Log.e("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }


    public void doGetOtp(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = Consts.GET_OTP;
        int method = Method.POST;

        Log.e("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }

    /**
     * Service method calling for Registration -->
     **/

    public void doRegistration(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = Consts.BASE_URL + "registration";
        int method = Method.POST;

        Log.e("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }
    /**
     * Service method calling for Fetch Universities -->
     **/
    public void doPostFetchAllUniversities(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = Consts.BASE_URL + "fetchUniversities";
        int method = Method.POST;
        Log.e("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }
    /**
     * Service method calling for Fetch One University-->
     **/
    public void doPostFetchUniversity(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = Consts.BASE_URL + "fetchUniversity";
        int method = Method.POST;
        Log.e("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }

    /**
     * Service method calling for Fetch ALL Schedule-->
     **/
    public void doPostFetchSchedules(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = Consts.BASE_URL + "fetchSchedules";
        int method = Method.POST;
        Log.e("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }

    /**
     * Service method calling for Fetch One University-->
     **/
    public void doPostSuggestUniversity(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = Consts.BASE_URL + "suggestUniversity";
        int method = Method.POST;
        Log.e("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }
    /**
     * Service method calling for Schedule Admin Chat -->
     **/

    public void doPostAdminChatSchedule(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = Consts.SCHEDULE_ADMIN_CHAT;
        int method = Method.POST;

        Log.e("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }
    /**
     * Service method calling for Schedule Admin Chat -->
     **/

    public void doPostCancelSchedule(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = Consts.CANCEL_SCHEDULE_MEETING;
        int method = Method.POST;

        Log.e("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }
    /**
     * Service method calling for Fetch User Profile -->
     **/

    public void doPostFetchProfile(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = Consts.GET_STUDENT_PROFILE;
        int method = Method.POST;

        Log.e("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }
    /**
     * Service method calling for gettingOtp -->
     **/

    public void doGetAdminCredentials(HashMap<String, String> paramsMap, boolean isToHideDialog, String url) {
        this.isToHideDialog = isToHideDialog;
        int method = Method.GET;

        Log.e("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }
    /**
     * Service method calling for Fetch Schedule-->
     **/
    public void doPostFetchSchedule(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = Consts.BASE_URL + "fetchSchedule";
        int method = Method.POST;
        Log.e("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }
    /**
     * Service method calling for Checking Phone-->
     **/
    public void doPostCheckPhoneNumber(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = Consts.BASE_URL + "checkMobile";
        int method = Method.POST;
        Log.e("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }
    /**
     * Service method calling for gettingOtp -->
     **/

    public void doPostGetOTP(HashMap<String, String> paramsMap, boolean isToHideDialog, String url) {
        this.isToHideDialog = isToHideDialog;
        int method = Method.POST;

        Log.e("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }


    private void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
