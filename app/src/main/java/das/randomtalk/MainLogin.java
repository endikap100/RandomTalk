package das.randomtalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

        String [] datuak = {nombre,password};
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
}
