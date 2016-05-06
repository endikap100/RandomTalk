package das.randomtalk;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity implements DoHTTPRequest.AsyncResponse, LocationListener {
    private LocationListener mlocationListener;
    private Location location;
    Geocoder geocoder;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        geocoder = new Geocoder(this, Locale.getDefault());


        if(MainLogin.user != null){
            String[] s = {getRegistrationId(this),"/User" + MainLogin.user + ":" + getAddress(new LatLng(this.getLocation().getLatitude(),this.getLocation().getLongitude()))};
            DoHTTPRequest request = new DoHTTPRequest(ChatActivity.this , this, "sendtext", -1,s);
            request.execute();
        }

    }
    public void send(View view){
        EditText text = (EditText) findViewById(R.id.sendtext);
        String[] s = {getRegistrationId(this),text.getText().toString()};
        DoHTTPRequest request = new DoHTTPRequest(ChatActivity.this , this, "sendtext", -1,s);
        request.execute();
        text.setText("");
    }

    public String getRegistrationId(Context context) {
        //return null;
        SharedPreferences settings = getSharedPreferences("lugaresFavoritos", 0);
        String registrationId = settings.getString("registrationId", "");

        return registrationId;
    }

    @Override
    public void processFinish(String output, String mReqId) {
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
                filterAddressOrigin = addresses.get(0).getCountryName()+" , "+ addresses.get(0).getSubLocality();
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
