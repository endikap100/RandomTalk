package das.randomtalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class BuscarActivity extends AppCompatActivity implements DoHTTPRequest.AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
    }

    public void buscar(View view){
        String[] s = {"sadniasdhuasidhduiashdiuashduiashdiashdiuashdiuas"};//getRegistrationId(this)};
        DoHTTPRequest request = new DoHTTPRequest(BuscarActivity.this , this, "sendrgid", -1,s);
    }

    @Override
    public void processFinish(String output, String mReqId) {

    }

    public String getRegistrationId(Context context) {
        //return null;
        SharedPreferences settings = getSharedPreferences("lugaresFavoritos", 0);
        String registrationId = settings.getString("registrationId", "");

        return registrationId;
    }
}
