package mainproject.mainroject.story;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Arrays;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
class GoogleApiRequest extends AsyncTask<String, Object, JSONObject> {
    private ConnectivityManager mConnectivityManager;
    private ConnectivityManager ConnectivityManager;

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onPreExecute()
    {
            if(!isNetworkConnected())
            {
                Log.i(getClass().getName(),"Not Connected to the Internet");
                cancel(true);
                return;
            }
    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    protected JSONObject doInBackground(String... strings) {
        if(isCancelled())
        {
            return null;
        }
        Log.d(getClass().getName(),"GoogleBooksAPI ISBN:" + strings[0]);

        String apiUrlSting = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + strings[0];
        Log.d(getClass().getName(),"GoogleBooksAPI ISBN:" +apiUrlSting );

        try{
            HttpURLConnection connection = null;
            // Build Connection
            try{
                URL url = new URL(apiUrlSting);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);
            }catch(MalformedURLException e)
            {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            int responseCode = connection.getResponseCode();
            if(responseCode != 200)
            {
                Log.w(getClass().getName(),"GoogleBooksAPI request failed. Response Code:" +responseCode);
                connection.disconnect();
                return null;
            }
            StringBuilder builder = new StringBuilder();
            BufferedReader responseReader =new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = responseReader.readLine();
            while (line != null)
            {
                builder.append(line);
                line = responseReader.readLine();
            }
            String responseString = builder.toString();
            Log.d(getClass().getName(),"Response String"+responseString);
            JSONObject responseJson = new JSONObject(responseString);
            connection.disconnect();
            return responseJson;
        } catch (SocketTimeoutException e)
        {
            Log.w(getClass().getName(),"Connection timed out.Returning null");
        return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
    private boolean isNetworkConnected() {
//        if (mConnectivityManager == null)
//        {mConnectivityManager =(ConnectivityManager)
//
//        }
        return true;
    }

}

