package mainproject.mainroject.story;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class checkedpdf extends AppCompatActivity {
    private static int SPLASH_TIME_OUT= 120000;
    PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkedpdf);
        pdfView =(PDFView)findViewById(R.id.checkedpdfview);

        Intent i = getIntent();
        String content = i.getStringExtra("CoNtEnT");
        new Retrievepdffile().execute(content);

        JSONObject pdfViewing= new JSONObject();
        JSONArray list = new JSONArray();
        try {
//            pdfView.documentFitsView();
            pdfView.fitToWidth(1);
            pdfView.getCurrentPage();
            pdfView.loadPages();
            pdfView.isSwipeVertical();

            list.put(pdfView.getCurrentPage());
            if(content != null ) {
                pdfViewing.put("Content", content);
                pdfViewing.put("Pages", list);
                pdfViewing.put("Views", pdfView.getCurrentPage());
            }
            try
            {
                FileWriter jsonFW = new FileWriter("PDFJSON.json");
                jsonFW.write(pdfViewing.toString());
                jsonFW.flush();

            }catch (IOException ex){
                ex.printStackTrace();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeintent = new Intent(checkedpdf.this, maincontent.class);
                startActivity(homeintent);
                finish();
            }
        },SPLASH_TIME_OUT);

        }
    class Retrievepdffile extends AsyncTask<String,Void,InputStream>
    {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                if(urlConnection.getResponseCode() == 200){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
        }
    }
}
