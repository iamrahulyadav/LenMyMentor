package com.nucleustech.mymentor.volley;

import com.android.volley.VolleyError;

/**
 * @author raisahab.ritwik
 */
public interface ServerResponseStringCallback {

	public void onSuccess(String resultJsonObject);

	/**
	 * If there occurs any error while communicating with server
	 * 
	 * @param error
	 */
	public void ErrorMsg(VolleyError error);
}
