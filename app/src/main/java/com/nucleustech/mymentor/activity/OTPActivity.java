package com.nucleustech.mymentor.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.model.UserClass;
import com.nucleustech.mymentor.util.Util;
import com.nucleustech.mymentor.volley.ServerResponseCallback;
import com.nucleustech.mymentor.volley.VolleyTaskManager;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ritwik.rai on 28/11/17.
 */

public class OTPActivity extends AppCompatActivity implements ServerResponseCallback {

    private EditText otp;
    private VolleyTaskManager volleyTaskManager;
    private static String otpp = "";
    SmsVerifyCatcher smsVerifyCatcher;
    Dialog dialog;
    EditText tv_userType;
    private boolean isCheckPhoneService = false;
    private boolean isGetOtpService = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otp = (EditText) findViewById(R.id.otp);
        volleyTaskManager = new VolleyTaskManager(OTPActivity.this);
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {


                String code = parseCode(message);//Parse verification code
                if (dialog.isShowing()) {
                    tv_userType.setText(code);//set code in edit text

                }
            }
        });

    }

    public void onOTPClick(View v) {
        String otpString = otp.getText().toString().trim();
        if (TextUtils.isEmpty(otpString)) {
            Util.showMessageWithOk(OTPActivity.this, "Please enter your phone number.");
            return;
        } else if (otpString.length() < 10) {
            Util.showMessageWithOk(OTPActivity.this, "Please enter a correct phone number.");
            return;
        }
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("mobile", "" + otpString.trim());
        isCheckPhoneService = true;
        volleyTaskManager.doPostCheckPhoneNumber(requestMap, true);

    }

    @Override
    public void onSuccess(JSONObject resultJsonObject) {

        if (isGetOtpService) {
            isGetOtpService = false;
            if (resultJsonObject.optString("code").trim().equalsIgnoreCase("200")) {
                otpp = resultJsonObject.optString("auth");
                dialog = new Dialog(OTPActivity.this);
                dialog.setContentView(R.layout.dialog_verify_otp);
                dialog.setCancelable(false);
                Button btnOk = (Button) dialog.findViewById(R.id.btn_verify);
                tv_userType = (EditText) dialog.findViewById(R.id.etOTPRecived);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String receiverOTP = tv_userType.getText().toString().trim();
                        if (TextUtils.isEmpty(receiverOTP)) {
                            Toast.makeText(OTPActivity.this, "Please enter the OTP.", Toast.LENGTH_SHORT).show();
                        } else if (receiverOTP.length() < 6) {
                            Toast.makeText(OTPActivity.this, "Please enter the correct OTP.", Toast.LENGTH_SHORT).show();
                        } else if (!receiverOTP.equalsIgnoreCase(otpp)) {
                            Toast.makeText(OTPActivity.this, "Please enter the correct OTP.", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            gotoNextActivity();
                        }
                    }
                });
                dialog.show();
            }
            else {
                Toast.makeText(OTPActivity.this, "Error! Something went wrong.", Toast.LENGTH_LONG).show();
            }
        }
        else if(isCheckPhoneService){
            isCheckPhoneService = false;
            if (resultJsonObject.optString("code").trim().equalsIgnoreCase("200")) {
                if (resultJsonObject.optString("isExist").trim().equalsIgnoreCase("1")) {
                    gotoNextActivity();
                } else if (resultJsonObject.optString("isExist").trim().equalsIgnoreCase("0")) {
                    HashMap<String, String> requestMap = new HashMap<>();
                    requestMap.put("mobile", "" + otp.getText().toString().trim());
                    isGetOtpService = true;
                    volleyTaskManager.doGetOtp(requestMap, true);
                }
            } else if (resultJsonObject.optString("code").trim().equalsIgnoreCase("400") &&
                    resultJsonObject.optString("isExist").trim().equalsIgnoreCase("0")) {
                HashMap<String, String> requestMap = new HashMap<>();
                requestMap.put("mobile", "" + otp.getText().toString().trim());
                isGetOtpService = true;
                volleyTaskManager.doGetOtp(requestMap, true);
            } else {
                Toast.makeText(OTPActivity.this, "Error! Something went wrong.", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void gotoNextActivity() {

        UserClass userClass = Util.fetchUserClass(OTPActivity.this);
        if (userClass == null) {
            userClass = new UserClass();
        }
        userClass.setPhone(otp.getText().toString().trim());
        Util.saveUserClass(OTPActivity.this, userClass);

        Intent intent = new Intent(OTPActivity.this, SigninActivity.class);
        intent.putExtra("phone", "" + otp.getText().toString().trim());
        startActivity(intent);
        finish();

    }

    @Override
    public void onError() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only six numbers from message string
     */
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }
}
