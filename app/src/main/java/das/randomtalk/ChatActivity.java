package das.randomtalk;

import android.Manifest;
import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
    boolean desconectado = false;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQ_CODE_SPEECH_INPUT = 5;
    TextToSpeech tts;
    private final int TTS = 6;
    Boolean isTTV = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, TTS);

        EditText text = (EditText) findViewById(R.id.sendtext);
        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    ViewGroup.LayoutParams params = texto.getLayoutParams();
                    params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 125, getResources().getDisplayMetrics());
                    texto.setLayoutParams(params);
                }else{
                    ViewGroup.LayoutParams params = texto.getLayoutParams();
                    params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                    texto.setLayoutParams(params);
                }
            }
        });
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
                        tts.speak(((String[]) inputMessage.obj)[1].toString(),TextToSpeech.QUEUE_FLUSH,null);
                    }else if(((String[]) inputMessage.obj)[0].equals("desconectar")){
                        finish();
                    }else if(((String[]) inputMessage.obj)[0].equals("image")){
                        startActivity(new Intent(getApplicationContext(), Imagen.class).putExtra("image", ((String[]) inputMessage.obj)[1].toString()).putExtra("text", texto.getText().toString()).putExtra("User_Contrario", User_contrario).putExtra("User_Contrario_Pais", User_contrario_Pais));
                        desconectado = true;
                    }
                }catch (Exception e) {
                    texto.setText(texto.getText() + "\n" + inputMessage.obj.toString());
                }
            }

        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photo:
                dispatchTakePictureIntent();
                return true;
            case R.id.Voice:
                promptSpeechInput();
                return true;
            case R.id.Read:
                isTTV = !isTTV;
                if (isTTV){
                    tts.speak(getResources().getString(R.string.Graciasporactivarlalecturadetexto), TextToSpeech.QUEUE_FLUSH, null);
                }
                return true;



    }
        return false;
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getResources().getString(R.string.Dime));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.Nosoportado),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        desconectado = true;
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = null;
            if(data.getData()==null){
                imageBitmap = (Bitmap)data.getExtras().get("data");
            }else{
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String[] s = {BitMapToString(imageBitmap)};
            DoHTTPRequest request = new DoHTTPRequest(ChatActivity.this , this, "subirfoto", -1,s);
            request.execute();
        }else if( REQ_CODE_SPEECH_INPUT == requestCode) {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    EditText texto = (EditText) findViewById(R.id.sendtext);

                    String query = result.get(0);
                    texto.setText(query.toString());
                }
        }else if(requestCode == TTS){
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                tts=new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener()
                {
                    @Override
                    public void onInit(int status)
                    {
                        tts.setLanguage(Locale.getDefault());
                        tts.setPitch(1.0f);
                        tts.setSpeechRate(1f);
                    }
                });
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
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
        texto.requestFocus();
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
        if(mReqId.equals("subirfoto")){
            String[] s = {getRegistrationId(getApplicationContext()),"/Image"+output};
            DoHTTPRequest request = new DoHTTPRequest(ChatActivity.this , this, "sendtext", -1,s);
            request.execute();
        }else {
            User_contrario = output.split(":")[0];
            if (output.split(":").length == 1) {
                User_contrario_Pais = getResources().getString(R.string.Ubicaciondesconocida);
            } else {
                User_contrario_Pais = output.split(":")[1];
            }
            texto.setText("\n" + User_contrario + " " + getResources().getString(R.string.conectadodesde)+ " " + User_contrario_Pais + "\n" + texto.getText().toString());
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        if(!desconectado) {
            desconectar();
            Toast.makeText(getApplicationContext(), User_contrario + getResources().getString(R.string.sehadesconectado), Toast.LENGTH_LONG);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!desconectado) {
            desconectar();
        }
    }

    public void desconectar(View v){
        if(!desconectado) {
            desconectar();
        }
    }

    public void desconectar(){
        String[] datuak = {getRegistrationId(this)};
        DoHTTPRequest httpRequest = new DoHTTPRequest(null, this, "desconectar", -1, datuak);
        httpRequest.execute();
        finish();
        desconectado = true;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.keyboard == Configuration.KEYBOARDHIDDEN_NO) {
            Toast.makeText(this, getResources().getString(R.string.keyboardvisible), Toast.LENGTH_SHORT).show();
        } else if (newConfig.keyboard == Configuration.KEYBOARDHIDDEN_YES) {
            Toast.makeText(this, getResources().getString(R.string.keyboardhidden), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("text", texto.getText().toString());
        outState.putString("User_contrario", User_contrario);
        outState.putString("User_contrario_Pais", User_contrario_Pais);

    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
