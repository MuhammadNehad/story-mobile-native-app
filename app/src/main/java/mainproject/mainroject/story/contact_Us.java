package mainproject.mainroject.story;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class contact_Us extends AppCompatActivity {

    EditText subjects,Message;
    Button sendingbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__us);
        subjects =(EditText)findViewById(R.id.subject);
        Message =(EditText)findViewById(R.id.message);
        sendingbtn=(Button)findViewById(R.id.sendemail);
    }


    public void sendTheMail(View view) {
        reportcontent(subjects.getText().toString(),Message.getText().toString());
    }
    public void reportcontent(final String Subject,String MessageS){
                DatabaseReference rc = FirebaseDatabase.getInstance().getReference().child("mailsToOrg").push();
                rc.child("sender").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                rc.child("subject").setValue(Subject);
                rc.child("content").setValue(MessageS);
//            String subject = Subject.getText().toString();
                String Message = MessageS;
                Intent intent= new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"storyorg55@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT,Subject);
                intent.putExtra(Intent.EXTRA_TEXT,Message);
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent,"choose an Email Client"));



    }
}
