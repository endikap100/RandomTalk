package das.randomtalk;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jorge on 27/4/16.
 */

public class MiGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Cuando el mensage es recibido.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String title = data.getString("title");
        String subtitle = data.getString("subtitle");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        if(message.contains("/Text")){
            while(!ChatActivity.acabado){
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                }
            };
            Message msg = new Message();
            message = message.substring(5, message.length());
            String[] m = {"text", message};
            msg.obj = m;
            msg.setTarget(ChatActivity.handler);
            msg.sendToTarget();
        }else if(subtitle.contains("/desconectar")){
            while(!ChatActivity.acabado){
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                }
            };
            Message msg = new Message();
            //message = message.substring(5, message.length());
            String[] m = {"desconectar", message};
            msg.obj = m;
            msg.setTarget(ChatActivity.handler);
            msg.sendToTarget();
        }else if(message.contains("/Image")){
            while(!ChatActivity.acabado){
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                }
            };
            Message msg = new Message();
            message = message.substring(6, message.length());
            String[] m = {"image", message};
            msg.obj = m;
            msg.setTarget(ChatActivity.handler);
            msg.sendToTarget();
        }else {
            //En este caso mostraremos una notificacion
            this.MostrarNotification(message, subtitle, title);
        }
    }


    private void MostrarNotification(String message, String subtitle, String title) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title+":"+subtitle)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int idNotificacion = 1;
        notificationManager.notify(1, notificationBuilder.build());
    }

    public String getRegistrationId(Context context) {
        //return null;
        SharedPreferences settings = getSharedPreferences("lugaresFavoritos", 0);
        String registrationId = settings.getString("registrationId", "");

        return registrationId;
    }

}
