package das.randomtalk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Jorge on 8/5/16.
 */
public class Imagen extends AppCompatActivity {
    String userContrario;
    String userContrarioPais;
    String texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);
        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageBitmap(StringToBitMap(getIntent().getStringExtra("image")));
        //userContrario = savedInstanceState.getString("User_Contrario");
        //userContrarioPais = savedInstanceState.getString("User_Contrario_Pais");
       // texto = savedInstanceState.getString("text");
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void volver (View v){
        startActivity(new Intent(getApplicationContext(),ChatActivity.class).putExtra("text",texto).putExtra("User_contrario", userContrario).putExtra("User_contrario_pais",userContrarioPais));
    }
}
