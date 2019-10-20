package mainproject.mainroject.story;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static mainproject.mainroject.story.HashCode.SHA1;


/**
 * A simple {@link Fragment} subclass.
 */
public class updateProfile extends Fragment {
    private FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();

    private AlertDialog.Builder StoryDetailsl;
    private EditText newnamebox,paypalbox;
    private Button acceptname;
    AlertDialog.Builder updateDetailsl;
    private Button acceptpassword;
    private EditText newPasswordbox;
    private EditText oldPasswordbox;
    ProgressDialog mprogress;
    private String TAG;
    String email = user.getEmail();
    boolean Access;
   AlertDialog.Builder updateDetails2,updateDetails3;

    public updateProfile() {
        // Required empty public constructor
    }

    EditText AddPhoneNumberText;

ImageButton NameEdit,PasswordEdit,UserNameedit,paypalbutton,AddingPhoneNumber;
DatabaseReference userdetaildb = FirebaseDatabase.getInstance().getReference().child("UserDetail").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    Query qr1 = userdetaildb.child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View updatinprof = inflater.inflate(R.layout.fragment_update_profile, container, false);
        mprogress=new ProgressDialog(getContext());
        NameEdit =updatinprof.findViewById(R.id.Nameupadte);
        PasswordEdit =updatinprof.findViewById(R.id.passwordupdate);
        AddPhoneNumberText =updatinprof.findViewById(R.id.PhoneNumber);
        AddingPhoneNumber=updatinprof.findViewById(R.id.AddingPhoneNumber);
        paypalbox = (EditText) updatinprof.findViewById(R.id.PaypalAcc);
        paypalbutton = (ImageButton) updatinprof.findViewById(R.id.AddingPaypal);

        paypalbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                paypalAccountAddingupdating();
//                userdetaildb.child("userpaypalacc").setValue(paypalbox.getText().toString());

            }
        });
        NameEdit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        NameEdit();
    }
});
        PasswordEdit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        PasswordEdit();
    }
});
        AddingPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        PhoneEdit();

//                userdetaildb.child("PhoneNumber").setValue(AddPhoneNumberText.getText().toString());
            }
        });
        return updatinprof;
    }
    protected void paypalAccountAddingupdating(){
        updateDetails3 =new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View updatedialog = inflater.inflate(R.layout.password_confirm,null);
        final EditText ConfirmPassword = (EditText) updatedialog.findViewById(R.id.confirmpassword);
        Button acceptingpassword = (Button) updatedialog.findViewById(R.id.acceptpassword);

        updateDetails3.setView(updatedialog);
        final AlertDialog alertDialog = updateDetails3.create();
        alertDialog.show();
        alertDialog.setTitle("Password Confirm");
        mprogress=new ProgressDialog(getContext());
        final String[] passwordConfirmation = new String[1];
        ConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                     passwordConfirmation[0] = SHA1(ConfirmPassword.getText().toString());


                     userdetaildb.child("Password").addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             Log.d("updatingProfile",String.valueOf(dataSnapshot.getValue()));

                             if (String.valueOf(dataSnapshot.getValue()).equals(passwordConfirmation[0]))
                             {
                                    Access =true;
                             }else
                             {
                                 Access =false;

                             }
                         }

                         @Override
                         public void onCancelled(DatabaseError databaseError) {

                         }
                     });

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        acceptingpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mprogress.show();


                  Log.d("updatingProfile",String.valueOf(passwordConfirmation[0]));

                    if(Access)
                    {
                        Toast.makeText(getContext(),"Password is confirmed successfully",Toast.LENGTH_LONG).show();
                        userdetaildb.child("userpaypalacc").setValue(paypalbox.getText().toString());
                        mprogress.dismiss();
                        alertDialog.dismiss();

                    }
                    else if (!Access)
                    {
                        Toast.makeText(getContext(),"Make sure Of Password",Toast.LENGTH_LONG).show();


                    }


            }
        });

    }

    protected void PhoneEdit(){

        updateDetails3 =new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View updatedialog = inflater.inflate(R.layout.password_confirm,null);
        final EditText ConfirmPassword = (EditText) updatedialog.findViewById(R.id.confirmpassword);
        Button acceptingpassword = (Button) updatedialog.findViewById(R.id.acceptpassword);

        updateDetails3.setView(updatedialog);
        final AlertDialog alertDialog = updateDetails3.create();
        alertDialog.show();
        alertDialog.setTitle("Password Confirm");
        mprogress=new ProgressDialog(getContext());
        final String[] passwordConfirmation = new String[1];
        ConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    passwordConfirmation[0] = SHA1(ConfirmPassword.getText().toString());


                    userdetaildb.child("Password").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("updatingProfile",String.valueOf(dataSnapshot.getValue()));

                            if (String.valueOf(dataSnapshot.getValue()).equals(passwordConfirmation[0]))
                            {
                                Access =true;
                            }else
                            {
                                Access =false;

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        acceptingpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Access)
                {
//                    Access=true;
                    Toast.makeText(getContext(),"Password is confirmed successfully",Toast.LENGTH_LONG).show();
                    userdetaildb.child("PhoneNumber").setValue(AddPhoneNumberText.getText().toString());
                    alertDialog.dismiss();
                }
                if (!Access)
                {
                    Toast.makeText(getContext(),"Make sure Of Password",Toast.LENGTH_LONG).show();

//                    Access=false;
                }


            }
        });

    }
private void NameEdit(){

    updateDetailsl =new AlertDialog.Builder(getContext());
    LayoutInflater inflater = getLayoutInflater();
    final View updatedialog = inflater.inflate(R.layout.updatename,null);
    newnamebox =(EditText)updatedialog.findViewById(R.id.newname);
    acceptname = (Button)updatedialog.findViewById(R.id.acceptupdname);
    RelativeLayout el =(RelativeLayout) updatedialog.findViewById(R.id.newpassform);
 updateDetailsl.setView(updatedialog);
    final AlertDialog alertDialog = updateDetailsl.create();
alertDialog.show();
    alertDialog.setTitle("Name Update");
    mprogress=new ProgressDialog(getContext());

    acceptname.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mprogress.show();

            userdetaildb.child("Name").setValue(newnamebox.getText().toString());
            Toast.makeText(getContext(),"Name is Updated",Toast.LENGTH_LONG).show();
            mprogress.dismiss();
            alertDialog.dismiss();
        }
    });
    }
    private void PasswordEdit() {

        updateDetails2 = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View updatedialog1 = inflater.inflate(R.layout.newpassword, null);
        oldPasswordbox = (EditText) updatedialog1.findViewById(R.id.oldpassword);
        newPasswordbox = (EditText) updatedialog1.findViewById(R.id.newnpassword);
        acceptpassword = (Button) updatedialog1.findViewById(R.id.acceptupdpassword);
        RelativeLayout el = (RelativeLayout) updatedialog1.findViewById(R.id.newpassform);
        updateDetails2.setView(updatedialog1);
        final AlertDialog alertDialog = updateDetails2.create();
        alertDialog.show();
        alertDialog.setTitle("Password Update");
        mprogress=new ProgressDialog(getContext());
        final Query qr = userdetaildb.child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        acceptpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpassword = null;
                String newpassword = null;
                try {
                    oldpassword = SHA1(oldPasswordbox.getText().toString());
                    newpassword = SHA1(newPasswordbox.getText().toString());

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                AuthCredential credential = EmailAuthProvider.getCredential(email,oldpassword);
                mprogress.show();
                final String finalNewpassword = newpassword;
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
//                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().build();
                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(finalNewpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                      @Override
                                                                                                                      public void onComplete(@NonNull Task<Void> task) {
                                                                                                                          if (task.isSuccessful()) {
                                                                                                                              userdetaildb.child("Password").setValue(finalNewpassword);

                                                                                                                              Toast.makeText(getContext(), "Password is Updated", Toast.LENGTH_LONG).show();
                                                                                                                              alertDialog.dismiss();
                                                                                                                          }
                                                                                                                          else {

                                                                                                                          }
                                                                                                                      }
                                                                                                                  });
                        }else {
                            Toast.makeText(getContext(), "Updating Password is failed", Toast.LENGTH_LONG).show();

//                            Snackbar snackbar_su = Snackbar
//                                    .make(ge, "Authentication Failed", Snackbar.LENGTH_LONG);
//                            snackbar_su.show();
                        }
                    }
                });
                mprogress.dismiss();
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,
                                    MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.mainmenu, menu);
    }
    @SuppressLint("WrongViewCast")
    public void createpaypal(){
        updateDetails2 = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View updatedialog1 = inflater.inflate(R.layout.newpassword, null);
        oldPasswordbox = (EditText) updatedialog1.findViewById(R.id.PaypalAcc);
        acceptpassword = (Button) updatedialog1.findViewById(R.id.AddingPaypal);

        updateDetails2.setView(updatedialog1);

        final AlertDialog alertDialog = updateDetails2.create();
        alertDialog.show();

    }
    }