package mainproject.mainroject.story;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class signup extends AppCompatActivity {
    private static final String TAG ="Images" ;
    private EditText username,name,password,confirmpassword,email;
private Button signup;
    private TextView textView;
    private String UserName;
    private String Name;
    private String Password;
    protected String EMAIL;
    private FirebaseAuth mAuth;
    private  String email1;
    private  String password1;
    private DatabaseReference dataTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//                final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());

//        progressDialog.setMessage("Please wait");
        mAuth = FirebaseAuth.getInstance();
FirebaseUser user = null;
        final FirebaseDatabase mydatabase=FirebaseDatabase.getInstance();
        final DatabaseReference myRef = mydatabase.getReference().child("UserDetail");
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        setContentView(R.layout.activity_signup);

        textView=findViewById(R.id.errormsg);
        username=(EditText)findViewById(R.id.UserName);
        name=(EditText)findViewById(R.id.Name);
        password=(EditText)findViewById(R.id.passwordsp);
        email=(EditText)findViewById(R.id.email_sp);
        confirmpassword=(EditText)findViewById(R.id.confirpasswordsp);
        signup=(Button)findViewById(R.id.signup_btn);
        textView=(TextView)findViewById(R.id.errormsg);
       //Query ddd= myRef.orderByChild("Email").equalTo(email1);

        final FirebaseUser finalUser = user;
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 createAccount(email1,password1);
//                                 This method is called once with the initial value and again
//                 whenever data at this location is updated.

                UserName = username.getText().toString().trim();
                Name = name.getText().toString().trim();
                Password = password.getText().toString().trim();
                EMAIL = email.getText().toString().trim();
//                              progressDialog.show();
//                            progressDialog.setCancelable(false);
//                EMAIL = String.valueOf(EMAIL).replace(".",
//                        "*");
                final DatabaseReference userMainName = myRef.child(UserName);
                final DatabaseReference useremail = myRef.child(UserName);
                DatabaseReference userpassword = myRef.child(UserName);
                final DatabaseReference PassWord = myRef.child(UserName);
                final DatabaseReference UserImg = myRef.child(UserName);
//                final Query myRefchild = myRef.child(UserName);
                final Query myRefchildemil = myRef.orderByChild("Email");

//                for (int i = 0; i <= myRef.toString().length(); i++) {


                    if(UserName.isEmpty()) {
                        username.setError("required");
//                        Toast.makeText(mainproject.mainroject.story.signup.this,"UserName is existed",Toast.LENGTH_LONG).show();
                    }
                 if(EMAIL.isEmpty()) {
                        email.setError("required");
//                        Toast.makeText(mainproject.mainroject.story.signup.this,"UserName is existed",Toast.LENGTH_LONG).show();
                    }
                 if(Name.isEmpty()) {
                        name.setError("required");
//                        Toast.makeText(mainproject.mainroject.story.signup.this,"UserName is existed",Toast.LENGTH_LONG).show();
                    }
                 if(Password.isEmpty()) {
                        password.setError("required");
//                        Toast.makeText(mainproject.mainroject.story.signup.this,"UserName is existed",Toast.LENGTH_LONG).show();
                    }
                    else if(!UserName.isEmpty()){
                        if (myRefchildemil.toString() != EMAIL) {

                            mAuth.fetchProvidersForEmail(UserName).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                    boolean check = !task.getResult().getProviders().isEmpty();
                                    if (check) {
                                        Toast.makeText(signup.this, "UseralreadyExisted", Toast.LENGTH_LONG).show();

                                    } else if (!check) {
                                        mAuth.createUserWithEmailAndPassword(EMAIL, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {

                                                    myRef.child(UserName).setValue(UserName);
                                                    useremail.child("Email").setValue(EMAIL);
                                                    userMainName.child("Name").setValue(Name);
                                                    // TODO: Changeable
                                                    UserImg.child("UserImg").setValue(" ");
                                                    PassWord.child("Password").setValue(Password);


//                                        userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(UserName).build();
//                                        finalUser.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    Log.d(TAG, "User profile updated.");
//                                                }
//                                            }
//                                        });
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(UserName).build();
                                                    user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d(TAG, "User profile updated.");
                                                            }
                                                        }
                                                    });
                                                    Toast.makeText(signup.this, "SignUp Successful", Toast.LENGTH_LONG).show();

                                                    gotohome();
                                                }
                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(signup.this, "SignUp Failed", Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });
                                    }
                                }
                            }
                            );
                        }
                        else if(myRefchildemil.toString() == EMAIL)
                        {Toast.makeText(mainproject.mainroject.story.signup.this,"Email is existed",Toast.LENGTH_LONG).show();
                        }


                    }
 /*               HashMap<String,String> datamap = new HashMap<String, String>();
                datamap.put("Name",Name);
                datamap.put("UserName",UserName);
                datamap.put("Email",EMAIL);
                datamap.put("Password",Password);*/

//              datamap.put("Name",Name);

//
 /*               myRef.push().setValue(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(signup.this,"data Has been registered", Toast.LENGTH_LONG).show();

                                    }else {
                                        Toast.makeText(signup.this,"Error",Toast.LENGTH_LONG).show();
                                    }

*/



            }
        });

    }
    public void checkuser(){

    }
//    public boolean checkuserExistance(String mail, DataSnapshot dataSnapshot)
//    {
//        Log.d(TAG,"CheckIfUsenameExisteds: checking if "+ mail +" alreadyexisrts" );
//    User user =new User();
//    for(DataSnapshot ds: dataSnapshot.getChildren()){
//
//        Log.d(TAG,"CheckIfUsenameExisteds: datasnapshot "+ ds);
//        user.setMail(ds.getValue(User.class).getMail());
//    }
//    return true;}

//    ///*
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
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
//        {
//            useremail.setText(getString(R.string.firebase_database_url,currentUser.getEmail()));
//            userusername.setText(getString(R.string.firebase_database_url,currentUser.getUid()));
//
//        }else
//        {
//
//        }
//    }
//*/
    public void gotoLogin(View view) {
    Intent intent = new Intent(signup.this, LoginForm.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    public void gotohome()
    {
//        .dismiss();
        Intent intent = new Intent(signup.this, maincontent.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
  /*  private void createAccount(String email1,String password1)
    {
        Log.d(TAG,"CreateAccont:"+email1);

        mAuth.createUserWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
*/}
/* Map fanoutObject =new hashmap();
    Map updatemap =new hashmap();
    updatemap.put(old,new);

 */