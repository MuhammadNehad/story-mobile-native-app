package mainproject.mainroject.story;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class privacy_policy extends AppCompatActivity {
    String url = "https://story.flycricket.io/privacy.html";
    WebView privaciesPoclicies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        privaciesPoclicies = (WebView) findViewById(R.id.privaciesPolicies);
        privaciesPoclicies.loadUrl(url);
    }
}
