package das.randomtalk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class BuscarActivity extends AppCompatActivity implements DoHTTPRequest.AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        final Context context = this;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS Desactivado");  // GPS not found
            builder.setMessage("Habilita la localización."); // Want to enable?
            builder.setPositiveButton("Habilitar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });

            builder.create().show();
            return;
        }
    }

    public void buscar(View view){
        String[] s = {getRegistrationId(this)};
        DoHTTPRequest request = new DoHTTPRequest(BuscarActivity.this , this, "sendrgid", -1,s);
        request.execute();
    }

    @Override
    public void processFinish(String output, String mReqId) {
        if (output.equals("OK") && mReqId.equals("sendrgid")){
            Toast.makeText(this,"Estas en la lista de espera... \n\nEn breve se te unira a una sesión",Toast.LENGTH_LONG).show();
            String[] s = {getRegistrationId(this)};
            DoHTTPRequest request = new DoHTTPRequest(BuscarActivity.this , this, "emparejar", -1,s);
            request.execute();
        }
    }

    public String getRegistrationId(Context context) {
        //return null;
        SharedPreferences settings = getSharedPreferences("lugaresFavoritos", 0);
        String registrationId = settings.getString("registrationId", "");

        return registrationId;
    }
}
