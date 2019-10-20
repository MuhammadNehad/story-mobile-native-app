package mainproject.mainroject.story;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Calendar;

import static mainproject.mainroject.story.HashCode.SHA1;

public class signup extends AppCompatActivity {
    private static final String TAG ="Images" ;
    private static final String TAG2 ="Email" ;

    private EditText username,name,password,confirmpassword,email,birthdate;
    private Button signup;
    private TextView textView;
    private String UserName;
    private String Name;
    private String confirmPassword,Password,birthdatestr;
    protected String EMAIL;
    private FirebaseAuth mAuth;
    private  String email1;
    private  String password1;

    DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatabaseReference dataTable;
    FirebaseUser user = null;
    final FirebaseDatabase mydatabase=FirebaseDatabase.getInstance();
    final DatabaseReference myRef = mydatabase.getReference().child("UserDetail");
    FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    Query CheckEmail= myRef.orderByChild("Email").equalTo(email1);
    boolean uDNAMeFound = false,usersEmailFound =false;
    public boolean checkExistedUSerEmail(final String useremail){
        final boolean[] Found = {true};
        Query UserEmailExistance = myRef.orderByChild("Email").equalTo(useremail.trim());
        UserEmailExistance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("signup",String.valueOf(dataSnapshot.getChildrenCount()));
                if (dataSnapshot.getChildrenCount()>0)
                {

                    usersEmailFound=true;

                }else{
//                    Found[0]=false;
                    usersEmailFound=false;

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return Found[0];
    }

    public boolean checkExistedUSerDisplayName(final String username){
        final boolean[] Found = {false};
        Query UserEmailExistance = myRef.orderByKey().equalTo(username.trim());
        UserEmailExistance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("signup",String.valueOf(dataSnapshot.getChildrenCount()));
                if(dataSnapshot.getChildrenCount()>0)
                {
                    Found[0]=true;
                    uDNAMeFound=true;
                }else{
                    Found[0]=false;
                    uDNAMeFound=false;

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return Found[0];
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//                final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());

//        progressDialog.setMessage("Please wait");
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_signup);


        textView=findViewById(R.id.errormsg);
        username=(EditText)findViewById(R.id.UserName);
        name=(EditText)findViewById(R.id.Name);
        password=(EditText)findViewById(R.id.passwordsp);
        email=(EditText)findViewById(R.id.email_sp);
        confirmpassword=(EditText)findViewById(R.id.confirpasswordsp);
        birthdate = (EditText)findViewById(R.id.birthdate);
        signup=(Button)findViewById(R.id.signup_btn);
        textView=(TextView)findViewById(R.id.errormsg);


        final FirebaseUser finalUser = user;

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int months, int day) {
                months= months+1;
                birthdate.setText(year+"-"+months+"-"+day);
            }
        };
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkExistedUSerDisplayName(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkExistedUSerEmail(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 createAccount(email1,password1);
//                                 This method is called once with the initial value and again
//                 whenever data at this location is updated.

                UserName = username.getText().toString().trim();
                Name = name.getText().toString().trim();
                Password = password.getText().toString().trim();
                confirmPassword = confirmpassword.getText().toString().trim();
                EMAIL = email.getText().toString().trim();
                birthdatestr = birthdate.getText().toString().trim();
                final DatabaseReference userMainName = myRef.child(UserName);
                final DatabaseReference useremail = myRef.child(UserName);
                DatabaseReference userpassword = myRef.child(UserName);
                final DatabaseReference PassWord = myRef.child(UserName);
                final DatabaseReference UserImg = myRef.child(UserName);
                final DatabaseReference CreatedDate = myRef.child(UserName);
                final DatabaseReference BirthDate = myRef.child(UserName);

                //                final Query myRefchild = myRef.child(UserName);
                final Query myRefchildemil = myRef.orderByChild("Email");



                if(!uDNAMeFound){
                    if(UserName.isEmpty()) {
                        username.setError("required");
                    }else
                 if(EMAIL.isEmpty()) {
                        email.setError("required");
                    }else
                 if(Name.isEmpty()) {
                        name.setError("required");
                    }else
                 if(Password.isEmpty()) {
                        password.setError("required");
                    }
                    else{
                  if(!usersEmailFound){
                     if (Password.equals(confirmPassword)) {

//                         Toast.makeText(signup.this, myRefchildemil.toString(), Toast.LENGTH_LONG).show();
                         mAuth.fetchProvidersForEmail(EMAIL).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                                                                       @Override
                                                                                       public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                                                                           boolean check = !task.getResult().getProviders().contains(EMAIL);
                                                                                           if (!check) {
                                                                                               Toast.makeText(signup.this, "UseralreadyExisted", Toast.LENGTH_LONG).show();

                                                                                           } else if (check) {
                                                                                               try {
                                                                                                   mAuth.createUserWithEmailAndPassword(EMAIL, SHA1(Password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                                       @Override
                                                                                                       public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                                           if (task.isSuccessful()) {

                                                                                                               myRef.child(UserName).setValue(UserName);
                                                                                                               useremail.child("Email").setValue(EMAIL);
                                                                                                               userMainName.child("Name").setValue(Name);
                                                                                                               // TODO: Changeable
                                                                                                               UserImg.child("UserImg").setValue("");
                                                                                                               try {
                                                                                                                   PassWord.child("Password").setValue(SHA1(Password));
                                                                                                               } catch (NoSuchAlgorithmException e) {
                                                                                                                   e.printStackTrace();
                                                                                                               } catch (UnsupportedEncodingException e) {
                                                                                                                   e.printStackTrace();
                                                                                                               }
                                                                                                               CreatedDate.child("createdDate").setValue(Calendar.getInstance().getTime());
                                                                                                               BirthDate.child("birthDate").setValue(birthdatestr);
                                                                                                               FirebaseUser user = mAuth.getCurrentUser();
                                                                                                               UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(UserName).build();
                                                                                                               user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                   @Override
                                                                                                                   public void onComplete(@NonNull Task<Void> task2) {
                                                                                                                       if (task2.isSuccessful()) {
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
                                                                                               } catch (NoSuchAlgorithmException e) {
                                                                                                   e.printStackTrace();
                                                                                               } catch (UnsupportedEncodingException e) {
                                                                                                   e.printStackTrace();
                                                                                               }
                                                                                           }
                                                                                       }
                                                                                   }
                         );
                     }else{
                         confirmpassword.setError("not matched with password");

//                         Toast.makeText(signup.this, "Email Existed", Toast.LENGTH_LONG).show();

                     }
                 }else{
//                      email.setError("Email is used before");

                     }
//                        }
//                        else if(myRefchildemil.toString() == EMAIL)
//                        {Toast.makeText(mainproject.mainroject.story.signup.this,"Email is existed",Toast.LENGTH_LONG).show();
//                        }


                    }



            }else{
                    username.setError("existed");
                }
            }
        });

    }

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

    public void showDatePiker(View v)
    {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                    mDateSetListener,year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

}
