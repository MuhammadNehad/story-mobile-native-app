package mainproject.mainroject.story;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class checkstoryfromact extends AppCompatActivity {
    private static int SPLASH_TIME_OUT= 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkstoryfromact);
        TextView  chstco = (TextView)findViewById(R.id.checkedstorycontent);
        Intent getcheckedcontent = getIntent();
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        String storycontent =  getcheckedcontent.getStringExtra("CoNtEnT");
       String storyname =  getcheckedcontent.getStringExtra("StRyNaMe");
        chstco.setText(storycontent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeintent = new Intent(checkstoryfromact.this, maincontent.class);
                startActivity(homeintent);
                finish();
            }
        },SPLASH_TIME_OUT);


    }
}
