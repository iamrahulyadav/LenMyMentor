package com.nucleustech.mymentor.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.model.Student;
import com.nucleustech.mymentor.model.StudentList;
import com.nucleustech.mymentor.model.UserClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class
 * @author raisahab.ritwik
 */
@SuppressWarnings("unused")
public class Util {

    private static String USERCLASS = "USERCLASS";
    private static String STUDENTCLASS = "STUDENTCLASS";

    private static int CONNECTIONTIMEOUT = 8000;
    private static StringBuilder sb = null;
    private static final String PREF_NAME = "MohanOverseasPrefs";
    private static final String PREF_NAME_STUDENT_LIST = "MohanOverseasStudentsPrefs";

    public static final String URL_STORAGE_REFERENCE = "gs://mohanoverseas-a4244.appspot.com";
    public static final String FOLDER_STORAGE_IMG = "images";
    public static final String FOLDER_STORAGE_FILE="files";


    /**
     * Verify active internet connection.
     */
    public static boolean verificaConexao(Context context) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        conectado = conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected();
        return conectado;
    }

    public static String local(String latitudeFinal, String longitudeFinal) {
        return "https://maps.googleapis.com/maps/api/staticmap?center=" + latitudeFinal + "," + longitudeFinal + "&zoom=18&size=280x280&markers=color:red|" + latitudeFinal + "," + longitudeFinal;
    }

    public static void initToast(Context c, String message) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * To check Internet Connection
     */
    public static boolean isInternetAvailable(Context context) {

        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conManager.getActiveNetworkInfo();
        if ((i == null) || (!i.isConnected()) || (!i.isAvailable())) {

            return false;
        }
        return true;
    }

    /**
     * Hide Soft Keyboard
     **/
    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    /**
     * Hide Soft Keyboard
     **/
    public static void showSoftKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        ;
    }

    /**
     * Shows custom alert dialog with OK option.
     **/
    public static void showMessageWithOk(final Activity mContext, final String message) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {

                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle(R.string.app_name);
                alert.setMessage(message);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });
    }

    /**
     * Shows custom alert dialog with OK option, success and NO Callback
     **/
    public static void showMessageWithOk_success(final Context mContext, final String message, final String title) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {
                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle(title);
                //alert.setIcon(R.drawable.success);
                alert.setMessage(message);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });
    }

    /**
     * Shows custom alert dialog with OK option and a callback
     **/
    public static void showCallBackMessageWithOkCallback_success(final Context mContext, final String message,
                                                                 final AlertDialogCallBack callBack) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {


                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle(R.string.app_name);
                alert.setCancelable(false);
                //alert.setIcon(R.drawable.success);
                alert.setMessage(message);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        callBack.onSubmit();
                    }
                });

                alert.show();
            }
        });
    }

    /**
     * Shows custom alert dialog with OK and cancel
     **/
    public static void showCallBackMessageWithOkCancel(final Context mContext, final String message,
                                                       final AlertDialogCallBack callBack) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {

                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle(R.string.app_name);
                alert.setCancelable(false);
                alert.setMessage(message);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        callBack.onSubmit();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        callBack.onCancel();
                    }
                });
                alert.show();
            }
        });
    }


    /**
     * Shows custom alert dialog with OK option and a callback
     **/
    public static void showCallBackMessageWithOkCallback(final Context mContext, final String message,
                                                         final AlertDialogCallBack callBack) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {

                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle(R.string.app_name);
                alert.setCancelable(false);
                alert.setMessage(message);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        callBack.onSubmit();
                    }
                });

                alert.show();
            }
        });
    }

    /**
     * Shows custom alert dialog with Success header, OK option and a callback
     **/
    public static void showCallBackMessageWithOkCallback_success(final Context mContext, final String message,
                                                                 final AlertDialogCallBack callBack, final String title) {

        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {

                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle(title);
                // alert.setIcon(R.drawable.success);
                alert.setCancelable(false);
                alert.setMessage(message);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        callBack.onSubmit();
                    }
                });

                alert.show();
            }
        });
    }

    /**
     * Saving UserClass details
     **/
    public static void saveUserClass(final Context mContext, UserClass userClass) {
        SharedPreferences KnwEduPrefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = KnwEduPrefs.edit();
        try {
            prefsEditor.putString(USERCLASS, ObjectSerializer.serialize(userClass));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefsEditor.commit();
    }

    /**
     * Fetching UserClass details
     **/
    public static UserClass fetchUserClass(final Context mContext) {
        SharedPreferences KnwEduPrefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        UserClass userClass = null;
        String serializeOrg = KnwEduPrefs.getString(USERCLASS, null);
        try {
            if (serializeOrg != null) {
                userClass = (UserClass) ObjectSerializer.deserialize(serializeOrg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return userClass;
    }
    /**
     * Saving Student details
     **/
    public static void saveStudentList(final Context mContext, StudentList students) {
        SharedPreferences KnwEduPrefs = mContext.getSharedPreferences(PREF_NAME_STUDENT_LIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = KnwEduPrefs.edit();
        try {
            prefsEditor.putString(STUDENTCLASS, ObjectSerializer.serialize(students));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefsEditor.commit();
    }

    /**
     * Fetching Student details
     **/
    public static StudentList fetchStudentList(final Context mContext) {
        SharedPreferences KnwEduPrefs = mContext.getSharedPreferences(PREF_NAME_STUDENT_LIST, Context.MODE_PRIVATE);
        StudentList userClass = null;
        String serializeOrg = KnwEduPrefs.getString(STUDENTCLASS, null);
        try {
            if (serializeOrg != null) {
                userClass = (StudentList) ObjectSerializer.deserialize(serializeOrg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return userClass;
    }


    /**
     * Get the current date
     **/
    @SuppressLint("SimpleDateFormat")
    public static String getDate() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return date;
    }

    /**
     * Get the current Time
     **/
    @SuppressLint("SimpleDateFormat")
    public static String getTime() {
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        return date;
    }

    /**
     * show global alert message
     *
     * @param context   application context
     * @param title     alert title
     * @param btn_title alert click button name
     * @param msg       alert message
     */
    public static void alertMessage(Context context, String title, String btn_title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg).setCancelable(false).setPositiveButton(btn_title, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Show log
     *
     * @param type       of log 0 for info,1 error,2 for verbosa
     * @param logtitle
     * @param logcontent
     * @return
     */
    public static void printLog(int type, String logtitle, String logcontent) {

        switch (type) {
            case 0:
                Log.i(logtitle, logcontent + "");
                break;
            case 1:
                Log.e(logtitle, logcontent + "");
                break;
            case 2:
                Log.v(logtitle, logcontent + "");
                break;

            default:
                Log.i(logtitle, logcontent + "");
                break;
        }
    }

    //	public static String postMethodWay_json(String hostName, String data) throws ClientProtocolException, IOException {
    //
    //		sb = new StringBuilder();
    //		HttpClient client = new DefaultHttpClient();
    //		HttpConnectionParams.setConnectionTimeout(client.getParams(), CONNECTIONTIMEOUT);
    //		HttpConnectionParams.setSoTimeout(client.getParams(), CONNECTIONTIMEOUT);
    //		HttpResponse response;
    //
    //		try {
    //			HttpPost post = new HttpPost(hostName);
    //
    //			StringEntity se = new StringEntity(data);
    //			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
    //			post.setEntity(se);
    //			response = client.execute(post);
    //			int statuscode = response.getStatusLine().getStatusCode();
    //
    //			/* Checking response */
    //			if (statuscode == 200) {
    //
    //				if (response != null) {
    //					InputStream in = response.getEntity().getContent();
    //					// InputStream in = /* your InputStream */;
    //					InputStreamReader is = new InputStreamReader(in);
    //
    //					BufferedReader br = new BufferedReader(is);
    //					String read = br.readLine();
    //
    //					while (read != null) {
    //						// System.out.println(read);
    //						sb.append(read);
    //						read = br.readLine();
    //
    //					}
    //					// the entity
    //					Log.i("tAG", "" + sb.toString());
    //					return sb.toString();
    //				}
    //			} else {
    //				// the entity
    //				System.out.println("Response code is:: " + response.getStatusLine().getStatusCode());
    //				sb.append(statuscode);
    //				return sb.toString();
    //			}
    //
    //		} catch (Exception e) {
    //			e.printStackTrace();
    //			sb.append(e.getLocalizedMessage());
    //			Log.i("connection", "Cannot Estabilish Connection! " + e.getLocalizedMessage());
    //		}
    //
    //		return sb.toString();
    //	}

    /**
     * Get base64 string from bitmap
     */
    public static String getBase64StringFromBitmap(Bitmap mBitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();
        return (Base64.encodeToString(ba, Base64.DEFAULT));
    }

    /**
     * Get bitmap from base64 string
     */
    public static Bitmap getBitmapBase64FromString(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return (decodedByte);
    }

    /**
     * Shows a system dialog for GPS settings.
     */
 /*   public static AlertDialog showSettingsAlert(final Context applicationContext, AlertDialog systemAlertDialog) {
        Log.v("calling showSettingsAlert()", "true");

        AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext);
        builder.setTitle("GPS Disabled");
       / builder.setIcon(R.drawable.warning);
        builder.setCancelable(false);
        builder.setMessage("Your GPS seems to be disabled, tap Ok to enable it?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // mContext.startActivity(new
                // Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // <-- Newly
                // added
                // line
                applicationContext.startActivity(viewIntent);
                dialog.dismiss();
            }
        });
        //		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
        //			public void onClick(DialogInterface dialog, int whichButton) {
        //				dialog.dismiss();
        //			}
        //		});
        systemAlertDialog = builder.create();
        systemAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        systemAlertDialog.show();
        return systemAlertDialog;
    }*/

    /**
     * This method parses the local json from the assets folder
     *
     * @throws JSONException
     */
    public static JSONObject loadJSONFromAsset(Context mContext, String jsonFileName) throws JSONException {

        String json = null;
        try {
            InputStream is = mContext.getAssets().open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return new JSONObject(json);
    }

}
