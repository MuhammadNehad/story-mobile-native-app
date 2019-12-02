package mainproject.mainroject.story;


import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class pdfview extends Fragment {


    public pdfview() {
        // Required empty public constructor
    }
PDFView pdfView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pdfview, container, false);
    pdfView =(PDFView)view.findViewById(R.id.pdfviewerpage);
//    int pagescount = pdfView.getPageCount();
    Bundle bundle =getArguments();
        String strLink = bundle.getString("StoryCont");

        Log.d("pdfViews",strLink);
        assert bundle != null;

        new Retrievepdffile().execute(strLink);

    return view;}
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
        pdfView.fromStream(inputStream).password(null)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true).load();
    }
}
}
