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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity implements DoHTTPRequest.AsyncResponse, LocationListener {
    Location location;
    Geocoder geocoder;
    static Handler handler;
    TextView texto;
    String User_contrario;
    String User_contrario_Pais;
    static boolean acabado = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        geocoder = new Geocoder(this, Locale.getDefault());
        texto = (TextView) findViewById(R.id.chattext);
        if (texto != null) {
            texto.setText("");
        }
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message inputMessage) {
                try {
                    if (((String[]) inputMessage.obj)[0].equals("user")){
                        User_contrario = ((String[]) inputMessage.obj)[1];
                        User_contrario_Pais = ((String[]) inputMessage.obj)[2];
                        texto.setText("\n" + User_contrario+" conectado desde "+User_contrario_Pais);
                    }else if (((String[]) inputMessage.obj)[0].equals("text")){
                        texto.setText(texto.getText() + "\n" + User_contrario+": " + ((String[]) inputMessage.obj)[1].toString());
                    }
                }catch (Exception e) {
                    texto.setText(texto.getText() + "\n" + inputMessage.obj.toString());
                }
            }

        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(MainLogin.user != null){
            do{
                String[] s = {getRegistrationId(this), "/Nombre" };
                DoHTTPRequest request = new DoHTTPRequest(ChatActivity.this, this, "sendtext", -1, s);
                request.execute();
            }while(User_contrario==null);
        }
        acabado = true;
    }

    public void send(View view){
        EditText text = (EditText) findViewById(R.id.sendtext);
        TextView texto = (TextView) findViewById(R.id.chattext);
        texto.setText(texto.getText()+ "\n" + MainLogin.user+": "+text.getText());
        String[] s = {getRegistrationId(this),"/Text"+text.getText().toString()};
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

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void desconectar(View v){

    }
}
