package das.randomtalk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class MainLogin extends /*AppCompat*/Activity implements DoHTTPRequest.AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View v){
        boolean loged = false;

        String nombre = ((EditText) findViewById(R.id.name)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String token = ObtenerRegistrationTokenEnGcm(this);
        String [] datuak = {nombre,password,token};
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
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Failed Login", Toast.LENGTH_LONG).show();
        }
    }

    public static String ObtenerRegistrationTokenEnGcm(Context context)
    {
        InstanceID instanceID = InstanceID.getInstance(context);
        String token = null;
        try {
            token = instanceID.getToken(context.getString(R.string.senderid), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return token;
    }
}
