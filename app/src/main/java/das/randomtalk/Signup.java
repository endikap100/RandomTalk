package das.randomtalk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by endika on 27/04/16.
 */
public class Signup extends /*AppCompat*/Activity implements DoHTTPRequest.AsyncResponse {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }
    public void signup(View v){
        String nombre = ((EditText) findViewById(R.id.name)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String email = ((EditText) findViewById(R.id.email)).getText().toString();

        String [] datuak = {nombre,password,email};
        DoHTTPRequest httpRequest = new DoHTTPRequest(Signup.this, this, "codigo_01", -1, datuak);
        httpRequest.execute();

    }

    @Override
    public void processFinish(String output, String mReqId) {
        Toast.makeText(this, "Signup suscesful", Toast.LENGTH_LONG).show();
        this.finish();
    }
}
