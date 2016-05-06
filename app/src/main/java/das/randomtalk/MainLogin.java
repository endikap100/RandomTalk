package das.randomtalk;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class MainLogin extends /*AppCompat*/Activity implements DoHTTPRequest.AsyncResponse {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final static String TAG = "MainActivity";
    Context context;
    public static String user;
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        if (checkPlayServices()) {
            //registro gcm
            String regid = getRegistrationId(context);
            if (regid.isEmpty()) {
                registerInBackground();
            }
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public String getRegistrationId(Context context) {
        //return null;
        SharedPreferences settings = getSharedPreferences("lugaresFavoritos", 0);
        String registrationId = settings.getString("registrationId", "");

        return registrationId;
    }

    private void storeRegistrationId(Context context, String regid) {
        SharedPreferences settings = getSharedPreferences("lugaresFavoritos", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("registrationId", regid);
        editor.commit();
    }


    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            //new AsyncTask(){
            GoogleCloudMessaging gcm = null;
            //static final String SENDER_ID = "sturdy-shelter-125313";
            static final String SENDER_ID = "349004489598";

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                String regid;
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID = " + regid;


                    storeRegistrationId(context, regid);

                    ////////////
                    Log.i("GCM", msg);
                    ///////////
                } catch (IOException ex) {
                    msg = "Error: " + ex.getMessage();
                }
                return msg;
            }

            /*@Override
            protected  void onPostExecute(String msg){
                mDisplay.append(msg+"\n");
            }*/
        }.execute(null, null, null);
    }

    public void login(View v){
        boolean loged = false;


            String nombre = ((EditText) findViewById(R.id.name)).getText().toString();
            String password = ((EditText) findViewById(R.id.password)).getText().toString();
            //String token = ObtenerRegistrationTokenEnGcm(this);
            String[] datuak = {nombre, password};
            DoHTTPRequest httpRequest = new DoHTTPRequest(MainLogin.this, this, "codigo_02", -1, datuak);
            httpRequest.execute();
    }
    public void signup(View v){
        Intent i = new Intent(this,Signup.class);
        startActivity(i);
    }

    @Override
    public void processFinish(String output, String mReqId) {
        if(output.equals("1")) {
            Toast.makeText(this, "Loged Successfully", Toast.LENGTH_LONG).show();
            user = ((EditText) findViewById(R.id.name)).getText().toString();
            Intent i = new Intent(this,BuscarActivity.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Failed Login", Toast.LENGTH_LONG).show();
        }
    }

}
