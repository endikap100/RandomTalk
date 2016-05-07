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
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
    String textoprev;
    ScrollView mScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        /*mScrollView = (ScrollView) findViewById(R.id.SCROLLER_ID);
        mScrollView.post(new Runnable()
        {
            public void run()
            {
                mScrollView.smoothScrollTo(0, texto.getBottom());
            }
        });*/

        if(savedInstanceState!=null) {
            textoprev = savedInstanceState.getString("text");
            User_contrario = savedInstanceState.getString("User_contrario");
            User_contrario_Pais = savedInstanceState.getString("User_contrario_Pais");
        }else{
            textoprev = "";
            if(MainLogin.user != null){
                String[] s = {getRegistrationId(this)};
                DoHTTPRequest request = new DoHTTPRequest(ChatActivity.this, this, "UserInfo", -1, s);
                request.execute();
            }
        }

        texto = (TextView) findViewById(R.id.chattext);
        if (texto != null) {
            texto.setText(textoprev);
        }
        texto.setMovementMethod(new ScrollingMovementMethod());
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message inputMessage) {
                try {
                    if (((String[]) inputMessage.obj)[0].equals("text")){
                        texto.setText(texto.getText() + "\n" + User_contrario+": " + ((String[]) inputMessage.obj)[1].toString());
                        scrollToBotton();
                    }else if(((String[]) inputMessage.obj)[0].equals("desconectar")){
                        finish();
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

        scrollToBotton();
    }

    private void scrollToBotton(){
        int scrollAmount = texto.getLayout().getLineTop(texto.getLineCount()) - texto.getHeight();
        if (scrollAmount > 0)
            texto.scrollTo(0, scrollAmount);
        else
            texto.scrollTo(0, 0);
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
        if (output.split(":").length == 1){
            User_contrario_Pais = "Ubicación desconocida";
        }else {
            User_contrario_Pais = output.split(":")[1];
        }
        texto.setText("\n" + User_contrario+" conectado desde "+User_contrario_Pais+"\n"+texto.getText().toString());
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
        String[] datuak = {getRegistrationId(this)};
        DoHTTPRequest httpRequest = new DoHTTPRequest(null, this, "desconectar", -1, datuak);
        httpRequest.execute();
        finish();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("text", texto.getText().toString());
        outState.putString("User_contrario", User_contrario);
        outState.putString("User_contrario_Pais", User_contrario_Pais);

    }
}
