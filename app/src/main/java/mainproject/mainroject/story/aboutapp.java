package mainproject.mainroject.story;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class aboutapp extends AppCompatActivity {

    String AboutUsContent = "App Story is an app that can beuse as your book store that can be used to sell"
            +" or buy some books from different publishers/Authors." +
            " Story App allows paypal gateway as payment method and it will allow more gateways" +
            " in future updates. Users can rate books and report them." +
            "  books are just allowed in app when user is connected to internet. we may look to some updates to allow" +
            "  it offline but it will still be able to be read through our app. \n\r" +
            " Conversations are allowed in Story App. at beginning, it may need Some upgrades. it is going to be much better than it is now";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutapp);
        TextView abouttext = (TextView)findViewById(R.id.aboutusText);
        abouttext.setText(AboutUsContent);
    }
}
