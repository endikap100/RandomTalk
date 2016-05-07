package das.randomtalk;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainLogin extends /*AppCompat*/Activity implements DoHTTPRequest.AsyncResponse, LocationListener {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final static String TAG = "MainActivity";
    Context context;
    Geocoder geocoder;
    public static String user;
    private Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        geocoder = new Geocoder(this, Locale.getDefault());
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

        }.execute(null, null, null);
    }

    public void login(View v){
        boolean loged = false;
        /*LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS Desactivado");  // GPS not found
            builder.setMessage("Habilita la localización."); // Want to enable?
            builder.setPositiveButton("Habilitar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    getApplicationContext().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });

            builder.create().show();
            return;
        }else {*/
            String nombre = ((EditText) findViewById(R.id.name)).getText().toString();
            String password = ((EditText) findViewById(R.id.password)).getText().toString();
            String location = this.getAddress(new LatLng(getLocation().getLatitude(), getLocation().getLongitude()));
            String rid = getRegistrationId(getApplicationContext());
            String[] datuak = {nombre, password, location,rid};
            DoHTTPRequest httpRequest = new DoHTTPRequest(MainLogin.this, this, "codigo_02", -1, datuak);
            httpRequest.execute();
        //}
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

    public Location getLocation() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0 ,0, this);
            if (locationManager != null) {
                Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocationGPS != null) {
                    return lastKnownLocationGPS;
                } else {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    }
                    Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    return loc;
                }
            } else {
                return null;
            }
        } catch (Exception e) {

            return null;
        }
    }

    //Obtiene la direccion necesaria para saber la duracion del viaje, solo se puede con la direccion. No es lo mismo que la posicion
    public String getAddress(LatLng latlon) {

        String filterAddressOrigin = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latlon.latitude, latlon.longitude, 1);

            if (addresses.size() > 0) {
                /*for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
                    filterAddressOrigin += addresses.get(0).getAddressLine(i) + " | ";*/
                filterAddressOrigin = addresses.get(0).getCountryName();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }
        return filterAddressOrigin;
    }

    @Override
    public void onLocationChanged(Location mlocation) {
        this.location = mlocation;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
