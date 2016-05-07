package das.randomtalk;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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

public class ChatActivity extends AppCompatActivity implements DoHTTPRequest.AsyncResponse{

    static Handler handler;
    TextView texto;
    String User_contrario;
    String User_contrario_Pais;
    static boolean acabado = false;
    String textoprev = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        texto = (TextView) findViewById(R.id.chattext);
        if (texto != null) {
            texto.setText(textoprev);
        }
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message inputMessage) {
                try {
                    if (((String[]) inputMessage.obj)[0].equals("text")){
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
            String[] s = {getRegistrationId(this)};
            DoHTTPRequest request = new DoHTTPRequest(ChatActivity.this, this, "UserInfo", -1, s);
            request.execute();
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
        User_contrario = output.split(":")[0];
        User_contrario_Pais = output.split(":")[1];
        texto.setText("\n" + User_contrario+" conectado desde "+User_contrario_Pais);
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
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            textoprev = texto.getText().toString();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            textoprev = texto.getText().toString();
        }
    }
}
