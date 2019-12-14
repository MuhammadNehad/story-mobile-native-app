package mainproject.mainroject.story;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.AuthResult;
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
import java.util.Calendar;
import java.util.regex.Pattern;

import static mainproject.mainroject.story.HashCode.SHA1;

public class signup extends AppCompatActivity {
    private static final String TAG ="Images" ;
    private static final String TAG2 ="Email" ;

    private EditText username,name,password,confirmpassword,email;
    private TextView birthdate;
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
     CheckBox aggrementCheckBox,DisClaimer;
     boolean AgreementChecked;
    boolean DisClaimerChecked;

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
    public void clickableText(String text, CheckBox ch, final String Type)
    {
        SpannableStringBuilder ss =new SpannableStringBuilder(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {

                Log.d("clickableText",Type);
                if(Type.equals("PP"))
                {
                    Intent pp = new Intent(signup.this,privacy_policy.class);
                    startActivity(pp);
                }else if(Type.equals("TC"))
                {
                    Intent pp = new Intent(signup.this,terms_and_conditions.class);
                    startActivity(pp);

                }else{
                    AlertDialog.Builder builder =new AlertDialog.Builder(signup.this);
                    builder.setMessage("Story Application is assumed to help booksellers in selling virtual copies for books" +
                            "." +
                            " It is intended for you as your books store." +
                            " Story app is intended for informational, educational and research purposes only." +
                            " Any other use makes user vulnerable to be disabled or deleted." +
                            " Books seller must make sure their books are available to be sold legally. " +
                            " Any book violates Policies and Terms, is vulnerable to be deleted." +
                            " Story app owner is not responsible for copyrights of books, and trustiness of Book." +
                            " purchasers are allowed to check books before buying it.")
                            .setTitle("DisClaimer");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
//        for(String t : Type) {
        int start = Type.equals("PP") ? 34 : ((Type.equals("TC")) ? 60 : 33);
        int end = Type.equals("PP") ? 50 : (Type.equals("TC") ? 80 : 43);
        ss.setSpan(clickableSpan,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }


        ch.setText(ss);
        ch.setMovementMethod(LinkMovementMethod.getInstance());
        ch.setHighlightColor(Color.TRANSPARENT);
    }
    public void clickableText(String text, CheckBox ch, final String Type,boolean append)
    {
        SpannableStringBuilder ss =new SpannableStringBuilder(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {

                Log.d("clickableText",Type);
                if(Type.equals("PP"))
                {
                    Intent pp = new Intent(signup.this,privacy_policy.class);
                    startActivity(pp);
                }else if(Type.equals("TC"))
                {
                    Intent pp = new Intent(signup.this,terms_and_conditions.class);
                    startActivity(pp);

                }else{
                    AlertDialog.Builder builder =new AlertDialog.Builder(signup.this);
                    builder.setMessage("Story Application is assumed to help booksellers in selling virtual copies for books" +
                            "." +
                            " It is intended for you as your books store." +
                            " Story app is intended for informational, educational and research purposes only." +
                            " Any other use makes user vulnerable to be disabled or deleted." +
                            " Books seller must make sure their books are available to be sold legally. " +
                            " Any book violates Policies and Terms, is vulnerable to be deleted." +
                            " Story app owner is not responsible for copyrights of books, and trustiness of Book." +
                            " purchasers are allowed to check books before buying it.")
                            .setTitle("DisClaimer");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
//        for(String t : Type) {
        int start = Type.equals("PP") ? 34 : ((Type.equals("TC")) ? 8 : 33);
        int end = Type.equals("PP") ? 50 : (Type.equals("TC") ? 28 : 43);
        ss.setSpan(clickableSpan,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }


        if(!append) {
            ch.setText(ss);
            ch.setMovementMethod(LinkMovementMethod.getInstance());
            ch.setHighlightColor(Color.TRANSPARENT);
        }else{
            ch.append(ss);
            ch.setMovementMethod(LinkMovementMethod.getInstance());
            ch.setHighlightColor(Color.TRANSPARENT);
        }
        }
    public void clickableTexto(String text, CheckBox ch, final String Type,String Clickabletext)
    {
        Pattern patterntext =Pattern.compile(Clickabletext);
        Linkify.addLinks(ch,patterntext,Type);
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
        birthdate = (TextView)findViewById(R.id.birthdate);
        signup=(Button)findViewById(R.id.signup_btn);
        textView=(TextView)findViewById(R.id.errormsg);
        aggrementCheckBox =(CheckBox) findViewById(R.id.check_agreementBox);
        DisClaimer =(CheckBox) findViewById(R.id.disclaimerBox);
        clickableText(aggrementCheckBox.getText().toString(),aggrementCheckBox,"PP",false);
        clickableText("and Our Terms and Conditions",aggrementCheckBox,"TC",true);
//        clickableTexto(aggrementCheckBox.getText().toString(),aggrementCheckBox,"pp","Our Privacies Policies");
//        clickableTexto(aggrementCheckBox.getText().toString(),aggrementCheckBox,"tc","Our Terms and Conditions");

        clickableText(DisClaimer.getText().toString(), DisClaimer, "DC",false);

        AgreementChecked = aggrementCheckBox.isChecked();
        DisClaimerChecked= DisClaimer.isChecked();
        aggrementCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AgreementChecked = aggrementCheckBox.isChecked();

            }
        });
        DisClaimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DisClaimerChecked= DisClaimer.isChecked();
            }
        });
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
                final DatabaseReference curUserDataParent = myRef.child(UserName);

                //                final Query myRefchild = myRef.child(UserName);
                final Query myRefchildemil = myRef.orderByChild("Email");


                if(AgreementChecked){
                    if(DisClaimerChecked) {
                        if (!uDNAMeFound) {
                            if (UserName.isEmpty()) {
                                username.setError("required");
                            } else if (EMAIL.isEmpty()) {
                                email.setError("required");
                            } else if (Name.isEmpty()) {
                                name.setError("required");
                            } else if (Password.isEmpty()) {
                                password.setError("required");
                            } else if(birthdatestr.isEmpty()) {
                                birthdate.setError("select birthdate");
                            }else{
                                if (!usersEmailFound) {
                                    if (Password.equals(confirmPassword)) {

//                         Toast.makeText(signup.this, myRefchildemil.toString(), Toast.LENGTH_LONG).show();
                                        mAuth.fetchProvidersForEmail(EMAIL).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                                                                                      @Override
                                                                                                      public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                                                                                          boolean check = !task.getResult().getProviders().contains(EMAIL);
                                                                                                          if (!check) {
                                                                                                              Toast.makeText(signup.this, "UseralreadyExisted", Toast.LENGTH_LONG).show();

                                                                                                          } else{
                                                                                                              try {
                                                                                                                  mAuth.createUserWithEmailAndPassword(EMAIL, SHA1(Password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                                                      @Override
                                                                                                                      public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                                                          if (task.isSuccessful()) {

                                                                                                                              myRef.child(UserName).setValue(UserName);
                                                                                                                              curUserDataParent.child("Email").setValue(EMAIL);
                                                                                                                              curUserDataParent.child("Name").setValue(Name);
                                                                                                                              // TODO: Changeable
//                                                                                                                              maxUserStorageSize
                                                                                                                              curUserDataParent.child("maxUserStorageSize").setValue(1024);

                                                                                                                              curUserDataParent.child("UserImg").setValue("");
                                                                                                                              try {
                                                                                                                                  curUserDataParent.child("Password").setValue(SHA1(Password));
                                                                                                                              } catch (NoSuchAlgorithmException e) {
                                                                                                                                  e.printStackTrace();
                                                                                                                              } catch (UnsupportedEncodingException e) {
                                                                                                                                  e.printStackTrace();
                                                                                                                              }
                                                                                                                              curUserDataParent.child("createdDate").setValue(Calendar.getInstance().getTime());
                                                                                                                              curUserDataParent.child("birthDate").setValue(birthdatestr);
                                                                                                                              curUserDataParent.child("disclaimerchecked").setValue(DisClaimerChecked);
                                                                                                                              curUserDataParent.child("policiesAndTermsChecked").setValue(AgreementChecked);
                                                                                                                              curUserDataParent.child("maxUserStorageSize").setValue(1024);
                                                                                                                              curUserDataParent.child("Type").setValue(0);
                                                                                                                              curUserDataParent.child("maxFreeTimes").setValue(1);
                                                                                                                              curUserDataParent.child("submittedFreeTimes").setValue(0);


                                                                                                                              Log.d("signing up", "onComplete: finished sign up data save");
                                                                                                                              FirebaseUser user = mAuth.getCurrentUser();

                                                                                                                              UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(UserName).build();
                                                                                                                              Log.d("signing up", "onComplete: ");

                                                                                                                              assert user != null;
                                                                                                                              user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                  @Override
                                                                                                                                  public void onComplete(@NonNull Task<Void> task2) {
                                                                                                                                      if (task2.isSuccessful()) {
                                                                                                                                          Log.d("signing up", "User profile updated.");
                                                                                                                                          gotohome();

                                                                                                                                      }else{

                                                                                                                                          Log.d("signing up", "User profile Failed.");
                                                                                                                                      }
                                                                                                                                  }
                                                                                                                              });
                                                                                                                              Toast.makeText(signup.this, "SignUp Successful", Toast.LENGTH_LONG).show();

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
                                                                                                              }finally {

                                                                                                              }
                                                                                                          }
                                                                                                      }
                                                                                                  }
                                        );
                                    } else {
                                        confirmpassword.setError("not matched with password");

//                         Toast.makeText(signup.this, "Email Existed", Toast.LENGTH_LONG).show();

                                    }
                                } else {
//                      email.setError("Email is used before");

                                }
//                        }
//                        else if(myRefchildemil.toString() == EMAIL)
//                        {Toast.makeText(mainproject.mainroject.story.signup.this,"Email is existed",Toast.LENGTH_LONG).show();
//                        }


                            }


                        } else {
                            username.setError("existed");
                        }
                    }else{
                        textView.setText("you must check disclaimer");

                    }
            }else{
                    textView.setText("you must agree Policies and Terms");
                }
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getScheme().equals("pp")) {
            Log.d("Intents", "pp Clicked");
            Intent pp = new Intent(signup.this, privacy_policy.class);
            startActivity(pp);
        } else if (intent.getScheme().equals("tc")) {
            Log.d("Intents", "tc Clicked");

            Intent pp = new Intent(signup.this, terms_and_conditions.class);
            startActivity(pp);

        }
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
