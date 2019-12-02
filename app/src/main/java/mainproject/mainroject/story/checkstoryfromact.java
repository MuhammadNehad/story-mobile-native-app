package mainproject.mainroject.story;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class checkstoryfromact extends AppCompatActivity {
    private static int SPLASH_TIME_OUT= 120000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_checkstoryfromact);
        TextView  chstco = (TextView)findViewById(R.id.checkedstorycontent);
        if(chstco.getLineCount()>14){
            char[] chars = chstco.getText().toString().toCharArray();
//            chstco.getText().toString().split(",",chstco.length()/chstco.getLineCount());
        }
        Intent getcheckedcontent = getIntent();
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
