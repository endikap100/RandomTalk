package das.randomtalk;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

public class DoHTTPRequest extends AsyncTask<String, Void, String> {

    public AsyncResponse delegate=null;

    // Aldagai orokorrak:
    private static final String TAG = "DoHTTPRequest";

    private Context mContext;
    private String mReqId;
    private String param = "";
    private ProgressBar mProgressBar = null;
    private int mProgressBarId;
    private HttpURLConnection urlConnection = null;
    private String errorMessage = "";

    // Constructor:
    public DoHTTPRequest(AsyncResponse delegate, Context context, String reqId, int progressBarId, String [] datuak) {

        this.delegate = delegate;
        mContext = context;
        mReqId = reqId;
        mProgressBarId = progressBarId;
        errorMessage = "";

        // Datuak, bidaltzeko formatuan ezarri:
        switch(mReqId){
            case "codigo_01":
                try {
                    param = "user=" + URLEncoder.encode(datuak[0], "UTF-8");
                    param += "&password=" + URLEncoder.encode(datuak[1], "UTF-8");
                    param += "&email=" + URLEncoder.encode(datuak[2], "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case "codigo_02":
                try {
                    param = "user=" + URLEncoder.encode(datuak[0], "UTF-8");
                    param += "&password=" + URLEncoder.encode(datuak[1], "UTF-8");
                    param += "&rId=" + URLEncoder.encode(datuak[2], "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case "sendregid":
                try {
                    param = "rid=" + URLEncoder.encode(datuak[0], "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            default:
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mProgressBarId != -1) {
            mProgressBar = (ProgressBar) ((Activity) mContext).findViewById(mProgressBarId);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }
    }

    @Override
    protected String doInBackground(String... params) {

        String st = "";
        switch(mReqId) {
            case "codigo_02": st = "http://galan.ehu.eus/jnieto011/WEB/login12.php"; break;
            case "codigo_01": st = "http://galan.ehu.eus/jnieto011/WEB/save12.php"; break;
            case "sendregid": st = "http://galan.ehu.eus/jnieto011/WEB/save_rid.php"; break;
            default: break;
        }

        // Begiratu internetera konektatu daitekeen:
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (!(netInfo != null && netInfo.isConnected())) {
            errorMessage = "No Internet Connection";
            return errorMessage;
        }

        // Ajax eskaera sortu:
        String targetURLstr = st;
        InputStream inputStream;
        try {
            // java.net.URL objektu bat sortu:
            URL targetURL = new URL(targetURLstr);
            urlConnection = (HttpURLConnection) targetURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Accept-Language", Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry());
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setDoOutput(true);

            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(param);
            out.close();

            int statusCode = urlConnection.getResponseCode();

            /* 200 represents HTTP OK */
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;
                String result = "";
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                // Stream-a itxi:
                inputStream.close();
                String response = result;
                return response;
            }
            else{
                errorMessage = "Error connecting to server";
                urlConnection.disconnect();
                return errorMessage;
            }
        } catch (Exception e) {
            errorMessage = "Error connecting to Internet";
            Log.d("EconInt", e.getMessage());
            return errorMessage;
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(final String result) {

        // Amaiera eman:
        switch(mReqId){
            case "codigo_01":case "codigo_02":case "sendregid":
                delegate.processFinish(result, mReqId);
                break;
            default:
                break;
        }

        // Progreso-barra desagerrarazi:
        if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
    }

    @Override
    protected void onCancelled() {
        // Progreso-barra desagerrarazi:
        if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
    }

    // Interfaze metodoa:
    public interface AsyncResponse {
        void processFinish(String output, String mReqId);
    }

}
