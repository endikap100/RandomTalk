package das.randomtalk;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static String ObtenerRegistrationTokenEnGcm(Context context)
    {
        InstanceID instanceID = InstanceID.getInstance(context);
        String token = null;
        try {
            token = instanceID.getToken("349004489598",
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return token;
    }


    public static String RegistrarseEnAplicacionServidor(Context context,String registrationToken2) throws  Exception
    {

        String stringUrl = "http://galan.ehu.eus/jnieto011/WEB/Dispositivos.php?user=" + "" + "&registrationId=" +  URLEncoder.encode(registrationToken2, "UTF-8");
        URL url = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(10000 /* milliseconds */);
        connection.setConnectTimeout(15000 /* milliseconds */);
        connection.setRequestMethod("GET");

        int codigoEstado = connection.getResponseCode();
        if(codigoEstado != 200)
            throw new Exception("Error al procesar registro. Estado Http: " + codigoEstado);

        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

        String respuesta = "",linea;
        while ((linea = bufferedReader.readLine()) != null) {
            respuesta = respuesta + linea;
        }

        bufferedReader.close();
        inputStream.close();

        if(!respuesta.equals("OK"))
            throw new Exception("Error al registrarse en aplicacion servidor: " + respuesta);

        return respuesta;

    }
}
