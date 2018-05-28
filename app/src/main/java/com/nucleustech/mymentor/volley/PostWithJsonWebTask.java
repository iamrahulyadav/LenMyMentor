package com.nucleustech.mymentor.volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.util.Util;

import java.util.Map;

/**
 * @author raisahab.ritwik
 */
public class PostWithJsonWebTask {

	private static int mStatusCode;
	private static ProgressDialog progressdialog;
	private static Activity activity;
	private static ServerStatus status = new ServerStatus();

	// Method to Call Web service through volley
	public static void callPostWithJsonWebtask(Activity mactivity, final String url,
                                               final Map<String, String> map, final ServerResponseStringCallback serverResponseCallback,
                                               final boolean isLoader, int method) {
		RequestQueue queue = Volley.newRequestQueue(mactivity);
		Util.printLog(0, "CALLING REQUEST", "URL:" + url + "\nDATA" + map);

		activity = mactivity;
		if (isLoader) {
			progressdialog = new ProgressDialog(mactivity);
			progressdialog.setMessage("Please wait...");
			progressdialog.show();
		}

		StringRequest jsObjRequest = new StringRequest(method, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {

				// Log.e("Response => ",response.toString());
				// BaseActivity.context.closeProgressbar();
				try {
					Util.printLog(0, "SERVER RESPONSE", "URL:" + url + "\nSTATUS CODE:" + mStatusCode
							+ "\nMESSAGE:   RESPONSE:" + response.toString());
				} catch (Exception e) {
					// BaseActivity.context.closeProgressbar();
					progressdialog.dismiss();
					e.printStackTrace();
				} finally {

					switch (mStatusCode) {
					case 200:
						if (isLoader) {
							// BaseActivity.context.closeProgressbar();
							progressdialog.dismiss();
							// BaseActivity.printLog(0,"inside"+mStatusCode,"");
						}
						status.setCode(mStatusCode);
						serverResponseCallback.onSuccess(response.toString());

						break;
					default:
						// BaseActivity.context.closeProgressbar();
						if (isLoader) {
							progressdialog.dismiss();
						}
						Util.alertMessage(activity, "Alert!!", "Ok", response.toString());
						// System.out.println("postwithJsonwebtask "+status.getMessage());
						break;
					}
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// BaseActivity.context.closeProgressbar();
				Util.printLog(0, "Json", "class--" + activity.getLocalClassName());
				progressdialog.dismiss();
				Log.e("error => " + mStatusCode, error.toString());

				if (error instanceof NoConnectionError) {
					Util.alertMessage(activity, "Alert!!", "ok", activity.getString(R.string.no_internet));
				} else if (error instanceof ServerError) {
					Util.alertMessage(activity, "Alert!!", "ok", activity.getString(R.string.server_error));
				} else if (error instanceof TimeoutError) {
					Util.alertMessage(activity, "Alert!!", "ok",
							activity.getString(R.string.response_timeout));
				} else if (error instanceof AuthFailureError) {
					Util.alertMessage(activity, "Alert!!", "ok",
							activity.getString(R.string.response_timeout));
				}
				if (serverResponseCallback != null) {
					serverResponseCallback.ErrorMsg(error); // handle the error in  the activity
				}

			}
		}) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.android.volley.Request#getParams()
			 */
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				return map;
			}

			@Override
			protected Response<String> parseNetworkResponse(NetworkResponse response) {
				mStatusCode = response.statusCode;
				// Log.e("mStatusCode", " "+mStatusCode);
				return super.parseNetworkResponse(response);
			}

			/*
			 * @Override public Map<String, String> getHeaders() throws
			 * AuthFailureError { HashMap<String, String> headers = new
			 * HashMap<String, String>(); headers.put("Content-Type",
			 * "application/json;"); return headers; }
			 */
		};

		jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(jsObjRequest);
	}

}
