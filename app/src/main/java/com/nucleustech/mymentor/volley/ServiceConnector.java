package com.nucleustech.mymentor.volley;

import com.nucleustech.mymentor.constant.Consts;

import org.json.JSONObject;

abstract class ServiceConnector {

	protected static String baseURL = Consts.BASE_URL;

	protected static String versionCodeURL = baseURL + "dbversion/";

	protected JSONObject outputJson;

	public static String getBaseURL() {
		return baseURL;
	}

	public static String getVersionCodeURL() {
		return versionCodeURL;
	}

	public JSONObject getOutputJson() {
		return outputJson;
	}

	public void setOutputJson(JSONObject outputJson) {
		this.outputJson = outputJson;
	}

}
