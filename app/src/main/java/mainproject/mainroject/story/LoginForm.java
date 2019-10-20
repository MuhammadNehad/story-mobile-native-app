package mainproject.mainroject.story;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.security.NoSuchAlgorithmException;

import static java.security.spec.MGF1ParameterSpec.SHA1;
import static mainproject.mainroject.story.HashCode.SHA1;

public class LoginForm extends AppCompatActivity {
    private static final String TAG = "Login" ;
    private EditText email,password;
Button login;
DatabaseReference mDatabase;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button)findViewById(R.id.Login_btn);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onStartlogin();

            }
        });
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null){
                    startActivity(new Intent(LoginForm.this,maincontent.class));
                }
            }
        };

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        if (user != null) {
//             Name, email address, and profile photo Url
//            String name = user.getDisplayName();
//            String email = user.getEmail();

//             Check if user's email is verified
//            boolean emailVerified = user.isEmailVerified();

//             The user's ID, unique to the Firebase project. Do NOT use this value to
//             authenticate with your backend server, if you have one. Use
//             FirebaseUser.getToken() instead.
//            String uid = user.getUid();
//        }
    }
//    @Override
    FirebaseDatabase fd;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }
    String Email;
    public void onStartlogin() {
//        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
       Email = email.getText().toString().trim();
        String password1 = password.getText().toString().trim();
        Email = String.valueOf(Email).replace("*", ".");
        if(TextUtils.isEmpty(Email)||TextUtils.isEmpty(password1))
        {
            Toast.makeText(LoginForm.this,"fields are empty",Toast.LENGTH_LONG).show();
        }
        else{

try {
    mAuth.signInWithEmailAndPassword(Email,SHA1(password1)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (!task.isSuccessful()) {
                Toast.makeText(LoginForm.this, "Sign In Problem", Toast.LENGTH_LONG).show();
            }
        }

    });
}catch(NullPointerException excepion){
//    throw GooglePlayServicesNotAvailableException'A';
    StringBuffer buffer =new StringBuffer();
    buffer.append("Add Google acc to your phone");
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
} catch (UnsupportedEncodingException e) {
    e.printStackTrace();
}
        }


//            FirebaseUser currentUser = mAuth.getCurrentUser();
//            mAuth.addAuthStateListener(authStateListener);
//            updateUI(currentUser);
        }


//    @Override
//    protected void onStop() {
//        super.onStop();
//if(authStateListener != null){
//    mAuth.removeAuthStateListener(authStateListener);
//}
//
//    }
//    private void signIn(String email,String password)
//        {Log.d(TAG,"signIn:"+email);
//            if(!validateForm()){
//                return;
//            }
//
//            mAuth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "signInWithEmail:success");
//                                FirebaseUser user = mAuth.getCurrentUser();
//                                updateUI(user);
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "signInWithEmail:failure", task.getException());
//                                Toast.makeText(LoginForm.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//                                updateUI(null);
//                            }
//
//                            // ...
//                        }
//                    });
//        }
//        private void signout()
//        {
//            mAuth.signOut();
//            updateUI(null);
//        }
//
//    private boolean validateForm() {
//        boolean valid = true;
//     String email1 =email.getText().toString();
//        if(TextUtils.isEmpty(email1))
//        {
//            email.setError("add your correct email");
//            valid=false;
//        }else {
//            email.setError(null);
//        }
//     String mpassword = password.getText().toString();
//        if(TextUtils.isEmpty(mpassword))
//        {
//            password.setError("add your correct password");
//            valid=false;
//        }else {
//            password.setError(null);
//        }
//        return valid;
//    }
//
//    private void updateUI(FirebaseUser currentUser) {
//        maincontent Main = new maincontent();
//
//        TextView useremail = Main.findViewById(R.id.email_user);
//        TextView userusername = Main.findViewById(R.id.username);
//
//        if(currentUser!= null)
//
//    {
//        useremail.setText(getString(R.string.firebase_database_url,currentUser.getEmail()));
//        userusername.setText(getString(R.string.firebase_database_url,currentUser.getUid()));
//
//    }else
//        {
//
//    }
//    }
//


    public void gotosignup(View view) {
        Intent intent = new Intent(getApplicationContext(), signup.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    public void gotoEmailVerify(View view) {
         }
/*
public void onClick(View v)
{
    switch (v.getId())
    {
        case R.id.Login_btn:
            signIn(email.getText().toString(),password.getText().toString());
            break;
    }
}
*/
}
