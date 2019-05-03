package mainproject.mainroject.story;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.util.FileUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.BreakIterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class home extends Fragment {


//editText
    String uploadedFileName;
    Button pdfslct;
    private StringTokenizer tokens;
    private String first;
    private String file_1;
    private BreakIterator txt_file_name_1;
    String[] strytypes = {"Science","Horror","Action","Religious","Politics"};
    TextView filename;
    StorageReference  myStorage = FirebaseStorage.getInstance().getReference();
    StorageReference mstr2 = myStorage.child("PDFuploads").child(FirebaseAuth.getInstance().getCurrentUser().getEmail()).child("StoryDetail");
    private Uri downloaduri = null;
    DatabaseReference child;

    private AlertDialog.Builder StoryDetails2;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager = getFragmentManager();

    DatabaseReference muserref = FirebaseDatabase.getInstance().getReference().child("UserDetail");
//    DatabaseReference mimgref = FirebaseDatabase.getInstance().getReference().child("PDFFILES");
//    private Uri selectedFileURI =null;
String[] mimeTypes =
        {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                "text/plain",
                "application/pdf",
                "application/zip"};
    EditText editText;
    Button pdfupload;
    EditText prices;
    ImageButton StryCovers;
    EditText storydesc;
    EditText Storyname;
    ImageView img;
    TextView text;
    Button btnSubmitStory;
    FirebaseAuth mAuth;
    private String userName;
     Uri selectedFileURI=null;
    String Filepath;
    public static final int PAYPAL_REQUEST_CODE1 = 7171;
    PayPalConfiguration config2 = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .defaultUserEmail("mnmfas-facilitator@gmail.com");
    private AlertDialog.Builder StoryDetailsl;

    public home() {
    }

    private DatabaseReference mimgref = FirebaseDatabase.getInstance().getReference().child("Images");
    final DatabaseReference mypdfStoryRef = FirebaseDatabase.getInstance().getReference().child("pdfStoriesdetails");
    final DatabaseReference myStoryRef = FirebaseDatabase.getInstance().getReference().child("StoriesDetails");
    final FirebaseDatabase mydatabase=FirebaseDatabase.getInstance();
    FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
    final DatabaseReference myRef = mydatabase.getReference().child("UserDetail");
    String currentU;
    String currentUDN =currentuser.getDisplayName();
    LoginForm loginForm;
    Query fk;
    signup su;
    private ProgressDialog mProgress;
    String strydescr;
    Uri uri = null;
    private Uri uri2 = null;
    private Uri uri3 = null;

    ImageButton logoupload;
    String story_Name;
    String userEmail = null;
    ViewPager viewPager;
    viewpageradapter viewpa;
    EditText $pricebox,pdfstrydescription;
    MenuView.ItemView StoryImg;
    private static final int Gallery_intent1 = 2;
    private static final int Gallery_intent2 = 3;
    private static final int Gallery_intent3 =4;
    String strytitle;
    String amount_to_pay="";
    Button appentries,pdfentries,changeForm;
    LinearLayout pdfform,transform,appentryprice;
    EditText storydesc2 ;
    Spinner typespinner;
    ArrayAdapter<String> spinnerAdapter;
    SharedPreferences sharedPreferences;
    String selectingitem;


    public boolean checkExistedpaypalEmail(final View view, final View inflate, final String stryDescri, final String storyNaMe){
        final boolean[] Found = {false};
        Query PaypalEmailExistance = myRef.child(currentUDN).orderByChild("userpaypalacc");
        PaypalEmailExistance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Found[0] = true;
                if(!dataSnapshot.exists()){
                    Toast.makeText(getContext(),"You havent Submitted Paypal ACCOUNT",Toast.LENGTH_LONG).show();
                    AddPaypalAccDialog();
                }else{
                    if(view.getId() == inflate.findViewById(R.id.submitStory).getId()) {
                        shortstoriesProgress();
                    }else if(view.getId() == inflate.findViewById(R.id.submitingPDFbutton).getId())
                    {
                        pdfFunSub(stryDescri, storyNaMe);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return Found[0];
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);
        return matcher.matches();
    }
    public void AddPaypalAccDialog(){
      StoryDetailsl =new AlertDialog.Builder(getContext());
      LayoutInflater inflater = getLayoutInflater();
      final View detaildialog = inflater.inflate(R.layout.paypalaccadd,null);
              StoryDetailsl.setView(detaildialog);
        final EditText PayPalACCTxT = detaildialog.findViewById(R.id.Paypal_ACc_Add);
        final Button PayPalACCSubmit = detaildialog.findViewById(R.id.Paypal_ACc_Submit);
        PayPalACCSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!PayPalACCTxT.getText().toString().isEmpty() && isEmailValid(PayPalACCSubmit.getText().toString()))
                {
                    myRef.child(currentUDN).child("userpaypalacc").setValue(PayPalACCSubmit.getText().toString());
                }else{
                    Toast.makeText(getContext(),"Your account is not in right expression",Toast.LENGTH_SHORT).show();

                }
            }
        });

        final AlertDialog alertDialog = StoryDetailsl.create();
        alertDialog.show();
    }
    public String getSelectingitem() {
        return selectingitem;
    }

    public void setSelectingitem(String selectingitem) {
        this.selectingitem = selectingitem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View inflate = inflater.inflate(R.layout.fragment_home, container, false);
        typespinner =(Spinner)inflate.findViewById(R.id.addingstoriestypes);
//        storydesc = view.findViewById(R.id.strydescription);
        pdfslct =(Button)inflate.findViewById(R.id.selectpdffile);
        pdfupload =(Button)inflate.findViewById(R.id.uploadpdffile);
        filename = (TextView)inflate.findViewById(R.id.uploadedfilename);
        appentries = (Button)inflate.findViewById(R.id.useAppentry);
        pdfentries =(Button)inflate.findViewById(R.id.uploadingpdfform);
        pdfform = (LinearLayout)inflate.findViewById(R.id.pdfform);
        storydesc2= (EditText)inflate.findViewById(R.id.pdfstrydescription);
        transform= (LinearLayout)inflate.findViewById(R.id.transform);
        changeForm=(Button)inflate.findViewById(R.id.closingpages);
        pdfstrydescription =(EditText)inflate.findViewById(R.id.pdfstrydescription);
        appentryprice =(LinearLayout)inflate.findViewById(R.id.appentryprice);
        // TODO:StoryPages
//        fragmentManager.beginTransaction().replace(R.id.content2, child).commit();
        spinnerAdapter =new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,strytypes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        typespinner.setAdapter(spinnerAdapter);

        Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.imgstyle);
        RoundedBitmapDrawable imground = RoundedBitmapDrawableFactory.create(getResources(),img);

        imground.setCornerRadius(25f);
        final String[] spitems = new String[1];
        viewPager = (ViewPager)inflate.findViewById(R.id.storypages);
        viewpa =new viewpageradapter(getContext());
        viewPager.setAdapter(viewpa);
        typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                setSelectingitem(strytypes[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

           }

        });
        this.selectingitem =spitems[0];
        int[] story = {viewpa.getItemPosition(viewPager)};
        for (  int i=0;
            i<= viewpa.getCount();
            i++
             ) {

        }

        // end view pager
        StoryImg =(MenuView.ItemView) inflate.findViewById(R.id.StoryImg);
        btnSubmitStory =inflate.findViewById(R.id.submitStory);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            viewpa.editText.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//                }
//            });
//        }
        $pricebox =inflate.findViewById(R.id.pricebox);
//int sda = R.drawable.edittxt_img;
//        EditText editText = inflate.findViewById(R.id.userEntry);
        //        img=inflate.findViewById(R.id.imagesuseradd);
//        String userEmail1 =su.EMAIL;

        Storyname=inflate.findViewById(R.id.story_name);
//        text=inflate.findViewById(R.id.EntryOutPut);

        logoupload = inflate.findViewById(R.id.story_logo);

        currentU = currentuser.getUid();
        mProgress=new ProgressDialog(getContext());

              logoupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,Gallery_intent1);

            }
        });
         userEmail = currentuser.getEmail();
         userName = currentuser.getDisplayName();
        final DatabaseReference myRefchild = myRef.getRef();
        final int price = 100;
        appentries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setVisibility(View.VISIBLE);
                transform.setVisibility(View.GONE);
                appentryprice.setVisibility(View.VISIBLE);
                changeForm.setVisibility(View.VISIBLE);
            }
        });
        pdfentries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transform.setVisibility(View.GONE);
                pdfform.setVisibility(View.VISIBLE);
                changeForm.setVisibility(View.VISIBLE);
            }
        });
        changeForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeForm.setVisibility(View.GONE);
                transform.setVisibility(View.VISIBLE);
            if(viewPager.getVisibility()==View.VISIBLE) {
                appentryprice.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
            }
            if(pdfform.getVisibility()==View.VISIBLE)
            {
                pdfform.setVisibility(View.GONE);
            }

            }
        });

        btnSubmitStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkExistedpaypalEmail(v,inflate, null, null);
            }
        });
        pdfslct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), 1);

            }
        });
        pdfupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strytitle =  Storyname.getText().toString();
               String strydescr =  pdfstrydescription.getText().toString();

//    strydescr.replace(null, "no desc");

    Submitingpdf(strydescr, strytitle);

            }
        });
        logoupload.setImageDrawable(imground);

        return inflate;
    }


    private void startposting(final String StRurl){
        String storytitle = Storyname.getText().toString().trim();
        String storycontent = viewpa.editText.getText().toString().trim();
        if(!TextUtils.isEmpty(storytitle) && !TextUtils.isEmpty(storycontent) && uri!=null)
        {
            mProgress.setMessage("Uploading");
            mProgress.show();
            mProgress.setCancelable(false);

            StorageReference mstr = myStorage.child("Photos").child(userEmail).child("storylogo").child(uri.getLastPathSegment());
            mstr.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Uri downloaduri = taskSnapshot.getDownloadUrl();
//                    Picasso.with(getActivity()).load(downloaduri).fit().into(logoupload);

                    myStoryRef.child(StRurl).child("LogoUrl").setValue(downloaduri.toString());
mProgress.dismiss();
                }
            });
        }
    }
    private void startposting2(final String StRurl){
        String storytitle = Storyname.getText().toString().trim();
        String storycontent = storydesc2.getText().toString().trim();
        if(uri!=null)
        {
            mProgress.setMessage("Uploading");
            mProgress.show();
            mProgress.setCancelable(false);

            StorageReference mstr = myStorage.child("Photos").child(userEmail).child("storylogo").child(uri.getLastPathSegment());
            mstr.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Uri downloaduri = taskSnapshot.getDownloadUrl();
//                    Picasso.with(getActivity()).load(downloaduri).fit().into(logoupload);

                    mypdfStoryRef.child(StRurl).child("LogoUrl").setValue(downloaduri.toString());

                    mProgress.dismiss();
                }
            });
        }
    }
    public void selecttypes(){

    }
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getContext(),
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                    Uri.parse("package:" + getPackageName()));
//            startActivity(intent);
//            return;
//        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    String AuthoRs,DescriP,pRICe,IMGUrL,StorYNamE, StRContEnT,StrYsRc,StryTypes;
    public void addingstdataafterpaying(String Authors, String Descrip, String price11, String ImgUrl1, String STOryNAme, String STRCOntEnT, String STRYSrC, String selectingtype)
    {   this.AuthoRs =Authors;
        this.DescriP = Descrip;
        this.pRICe =price11;
        this.IMGUrL = ImgUrl1;
        this.StorYNamE = STOryNAme;
        this.StRContEnT = STRCOntEnT;
        this.StrYsRc = STRYSrC;
        this.StryTypes = selectingtype;
//        DatabaseReference psdbchild = psdb.push();
//
//        psdbchild.child("purchasername").setValue(auth.getDisplayName());
//        psdbchild.child("story_name").setValue(STOryNAme);
//        psdbchild.child("Story_Author").setValue(Authors);
//        psdbchild.child("Story_Desc").setValue(Descrip);
//        psdbchild.child("Story_Content").setValue(STRCOntEnT);
//        psdbchild.child("Story_ImgUrl").setValue(ImgUrl1);
//        psdbchild.child("StorySrc").setValue(STRYSrC);

    }

    public void Submitingpdf(final String stryDescri, final String storyNaMe ){
        StoryDetails2 =new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();
        final View detaildialog = inflater.inflate(R.layout.pricedialog,null);
        StoryDetails2.setView(detaildialog);

        prices= (EditText) detaildialog.findViewById(R.id.pdfprice);
        final Button accept = (Button) detaildialog.findViewById(R.id.submitingPDFbutton);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkExistedpaypalEmail(v,detaildialog,stryDescri,storyNaMe);
//                pdfFunSub(stryDescri, storyNaMe);
//                final String fileprice = prices.getText().toString().trim();
//                mypdfStoryRef.addChildEventListener(new ChildEventListener() {
//
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        if(storyNaMe == null){Toast.makeText(getContext(), "Story should have a Title", Toast.LENGTH_LONG).show();}
//                        else {
//
//                            if(dataSnapshot.hasChild(storyNaMe)){
//                                Toast.makeText(getContext(), "Story Title is Existed, please change it", Toast.LENGTH_LONG).show();
//                            }if(!dataSnapshot.hasChild(storyNaMe)){
//                                if(Float.parseFloat(fileprice)>=0) {
//
////                                    final DatabaseReference Story_Content = mypdfStoryRef.child(storyNaMe);
////                                    final DatabaseReference Story_price = mypdfStoryRef.child(storyNaMe);
////                                    final DatabaseReference fk = mypdfStoryRef.child(storyNaMe);
////                                    final DatabaseReference Stdesc = mypdfStoryRef.child(storyNaMe);
////                                    mypdfStoryRef.child(storyNaMe).setValue(storyNaMe);
//
////                                    final AlertDialog.Builder select = new AlertDialog.Builder(getContext());
////                                    select.setPositiveButton("Paypal", new DialogInterface.OnClickListener() {
////                                        @Override
////                                        public void onClick(DialogInterface dialog, int which) {
//
//                                            addingstdataafterpaying(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),stryDescri,fileprice,uri.getLastPathSegment(),storyNaMe,selectedFileURI.getLastPathSegment(),"PDFSTORY",getSelectingitem());
//
////                            Bundle stdata = new Bundle();
////                            stdata.putString("Authorize",Author1);
////                            stdata.putString("STORYNAME",storyNAME);
////                            stdata.putString("STCONTENT",storyCoNtEnT);
////                            stdata.putString("STDESCRIbE",Desc);
////                            stdata.putString("IMGURL",ImgUrl);
////                            stdata.putString("STSOURC",StrySrc);
//                                            amount_to_pay="5.0";
//                                            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount_to_pay),"USD"," " ,PayPalPayment.PAYMENT_INTENT_SALE);
////                            PayPalPayment payPalPayment1 = new PayPalPayment(new BigDecimal((90*Integer.parseInt(amount_to_pay))/100),"USD","Pay to"+Author1 ,PayPalPayment.PAYMENT_INTENT_SALE);
//
//                                            Intent intent =new Intent(getContext(), PaymentActivity.class);
//                                            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config2);
////                            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config2);
////                            intent.putExtra("Authorize",Author1);
////                            intent.putExtra("STORYNAME",storyNAME);
////                            intent.putExtra("STCONTENT",storyCoNtEnT);
////                            intent.putExtra("STDESCRIbE",Desc);
////                            intent.putExtra("IMGURL",ImgUrl);
////                            intent.putExtra("STSOURC",StrySrc);
//                                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
////                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment1);
//                                            startActivityForResult(intent,PAYPAL_REQUEST_CODE1);
////ItemFragment ite = new ItemFragment();
////ite.setArguments(stdata);
////                                            dialog.dismiss();
////                                        }
////                                    });
////                                    AlertDialog alertDialog1 = select.create();
////                                    alertDialog1.show();
//
//                                }
//                                else if(TextUtils.isEmpty(fileprice)){
//                                    prices.setError("can't be Empty");
//                                    Toast.makeText(getContext(), "Add a value to price", Toast.LENGTH_SHORT).show();
//                                }else if(Float.parseFloat(fileprice)<0){
//                                    prices.setError("can't be Less than 0");
//                                    Toast.makeText(getContext(), "Price Value Should be positive ", Toast.LENGTH_SHORT).show();
//
//                                }}
//
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//
//
//                });
            }
        });

        final AlertDialog alertDialog = StoryDetails2.create();
        alertDialog.show();
    }

    public void pdfFunSub(final String stryDescri, final String storyNaMe)
    {
        final String fileprice = prices.getText().toString().trim();
        mypdfStoryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(storyNaMe == null){Toast.makeText(getContext(), "Story should have a Title", Toast.LENGTH_LONG).show();}
                else {

                    if(dataSnapshot.hasChild(storyNaMe)){
                        Toast.makeText(getContext(), "Story Title is Existed, please change it", Toast.LENGTH_LONG).show();
                    }if(!dataSnapshot.hasChild(storyNaMe)){
                        if(Float.parseFloat(fileprice)>=0) {

//                                    final DatabaseReference Story_Content = mypdfStoryRef.child(storyNaMe);
//                                    final DatabaseReference Story_price = mypdfStoryRef.child(storyNaMe);
//                                    final DatabaseReference fk = mypdfStoryRef.child(storyNaMe);
//                                    final DatabaseReference Stdesc = mypdfStoryRef.child(storyNaMe);
//                                    mypdfStoryRef.child(storyNaMe).setValue(storyNaMe);

//                                    final AlertDialog.Builder select = new AlertDialog.Builder(getContext());
//                                    select.setPositiveButton("Paypal", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {

                            addingstdataafterpaying(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),stryDescri,fileprice,uri.getLastPathSegment(),storyNaMe,selectedFileURI.getLastPathSegment(),"PDFSTORY",getSelectingitem());

//                            Bundle stdata = new Bundle();
//                            stdata.putString("Authorize",Author1);
//                            stdata.putString("STORYNAME",storyNAME);
//                            stdata.putString("STCONTENT",storyCoNtEnT);
//                            stdata.putString("STDESCRIbE",Desc);
//                            stdata.putString("IMGURL",ImgUrl);
//                            stdata.putString("STSOURC",StrySrc);
                            amount_to_pay="5.0";
                            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount_to_pay),"USD"," " ,PayPalPayment.PAYMENT_INTENT_SALE);
//                            PayPalPayment payPalPayment1 = new PayPalPayment(new BigDecimal((90*Integer.parseInt(amount_to_pay))/100),"USD","Pay to"+Author1 ,PayPalPayment.PAYMENT_INTENT_SALE);

                            Intent intent =new Intent(getContext(), PaymentActivity.class);
                            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config2);
//                            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config2);
//                            intent.putExtra("Authorize",Author1);
//                            intent.putExtra("STORYNAME",storyNAME);
//                            intent.putExtra("STCONTENT",storyCoNtEnT);
//                            intent.putExtra("STDESCRIbE",Desc);
//                            intent.putExtra("IMGURL",ImgUrl);
//                            intent.putExtra("STSOURC",StrySrc);
                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
//                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment1);
                            startActivityForResult(intent,PAYPAL_REQUEST_CODE1);
//ItemFragment ite = new ItemFragment();
//ite.setArguments(stdata);
//                                            dialog.dismiss();
//                                        }
//                                    });
//                                    AlertDialog alertDialog1 = select.create();
//                                    alertDialog1.show();

                        }
                        else if(TextUtils.isEmpty(fileprice)){
                            prices.setError("can't be Empty");
                            Toast.makeText(getContext(), "Add a value to price", Toast.LENGTH_SHORT).show();
                        }else if(Float.parseFloat(fileprice)<0){
                            prices.setError("can't be Less than 0");
                            Toast.makeText(getContext(), "Price Value Should be positive ", Toast.LENGTH_SHORT).show();

                        }}

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_intent1 && resultCode == RESULT_OK) {


            uri = data.getData();

            Picasso.with(getActivity()).load(uri).fit().into(logoupload);
//            FileDownloadTask img_name = mstr.getFile(Uri.parse(uri.getLastPathSegment()));
//            DatabaseReference imgnames = mimgref.child(uri.getLastPathSegment());
//            DatabaseReference imgdate = mimgref.child();

//            imgnames.child("ImgSize").setValue(mstr.getBytes(Long.parseLong(img_name.toString())));
            }
        else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                selectedFileURI = data.getData();
                try {
                    String path = String.valueOf(FileUtils.fileFromAsset(getContext(),selectedFileURI+ "-pdfview.pdf"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File file = new File(selectedFileURI.getPathSegments().toString());
                Log.d("", "File : " + file.getName());
                uploadedFileName = file.getName();
                tokens = new StringTokenizer(uploadedFileName, ":");
                first = tokens.nextToken();
//            file_1 = tokens.nextToken().trim();
                filename.setText(file.getPath());
                Filepath = selectedFileURI.getLastPathSegment();
//                child.child("OwnerEmail").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
//                child.child("FileName").setValue(file.getName());

            }
        }
       if (requestCode == PAYPAL_REQUEST_CODE1) {
           if (resultCode == Activity.RESULT_OK) {
               PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

               if (confirmation != null) {
                   String sate =confirmation.getProofOfPayment().getState();
                   if(sate.equals("approved")){
                       switch (StrYsRc) {
                           case "AppCreationStory":
                               startposting(StorYNamE);
                               final DatabaseReference Story_Name = myStoryRef.child(StorYNamE);
                               Story_Name.child("storyNaMe").setValue(StorYNamE);
                               Story_Name.child("Author").setValue(AuthoRs);
                               Story_Name.child("story_content").setValue(StRContEnT);
                               Story_Name.child("story_price").setValue(pRICe);
                               Story_Name.child("Likes").setValue(0);
                               Story_Name.child("disLikes").setValue(0);
                               Story_Name.child("Reports").setValue(0);
                               Story_Name.child("StrType").setValue(StryTypes);
                               Story_Name.child("LogoSrc").setValue(IMGUrL);
                               Story_Name.child("STDESC").setValue(DescriP);
                               $pricebox.setText(null);

                               Storyname.setText(null);
                               Toast.makeText(getContext(), "Story have been Submitted successfully", Toast.LENGTH_LONG).show();
                               break;

                           case "PDFSTORY":
                               final DatabaseReference Pdf_Story_Name = mypdfStoryRef.child(StorYNamE);

                               restrictdata(StorYNamE);
                               startposting2(StorYNamE);

                               Pdf_Story_Name.child("PdfstoryNaMe").setValue(StorYNamE);
                               Pdf_Story_Name.child("PdfAuthor").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//                Story_Content.child("story_content").setValue(FileUrl);
                               Pdf_Story_Name.child("Pdfstory_price").setValue(pRICe);


                               Pdf_Story_Name.child("PdfLogoSrc").setValue(uri.getLastPathSegment());
                               Pdf_Story_Name.child("Likes").setValue(0);
                               Pdf_Story_Name.child("disLikes").setValue(0);
                               Pdf_Story_Name.child("Reports").setValue(0);
                               Pdf_Story_Name.child("PdfSTDESC").setValue(DescriP);
                               Pdf_Story_Name.child("StrType").setValue(StryTypes);

                               prices.setText(null);
                               Storyname.setText(null);

                               Toast.makeText(getContext(), "Story have been Submitted successfully", Toast.LENGTH_SHORT).show();
                               break;

                           default:
                               Toast.makeText(getContext(), "error in payment", Toast.LENGTH_SHORT).show();
                               break;
                       }
                   //                    try {
//                        String paymentDetails = confirmation.toJSONObject().toString(4);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }}
               }else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
               {
                   Toast.makeText(getContext(), "Invalid", Toast.LENGTH_SHORT).show();
               }

           }
       }else if(resultCode == Activity.RESULT_CANCELED)
       {Toast.makeText(getContext(), "cancel", Toast.LENGTH_SHORT).show(); }
           }
    }
    public void shortstoriesProgress(){
        final String prices = $pricebox.getText().toString().trim();
        final String story_content = viewpa.editText.getText().toString().trim();
        final String story_description = viewpa.storydesc.getText().toString().trim();
        story_Name = Storyname.getText().toString().trim();
        myStoryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String storiesname =String.valueOf(dataSnapshot.child("storyNaMe").getValue());
                if(dataSnapshot.hasChild(story_Name)){
                    Toast.makeText(getContext(), "Story is already existed, please change the Title", Toast.LENGTH_LONG).show();

                }if(!dataSnapshot.hasChild(story_Name)){

                    if (story_content.length()>3000){Toast.makeText(getContext(), "short Stories Content Should be less than or equal  3000 char(100 line)", Toast.LENGTH_SHORT).show();}
                    else {

                        if (Float.parseFloat(prices) <= 10) {
                            final String fk1 = userName.toString().trim();
//                                    final DatabaseReference Story_Content = myStoryRef.child(story_Name);
//                                    final DatabaseReference Story_price = myStoryRef.child(story_Name);
//                                    final DatabaseReference fk = myStoryRef.child(story_Name);
//                                    final DatabaseReference Stdesc = myStoryRef.child(story_Name);
//                                    myStoryRef.child(story_Name).setValue(story_Name);

                            final AlertDialog.Builder select = new AlertDialog.Builder(getContext());
                            AlertDialog alertDialog1 = null;
                            select.setPositiveButton("Paypal", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    addingstdataafterpaying(fk1,story_description,prices,uri.getLastPathSegment(),story_Name,story_content,"AppCreationStory",getSelectingitem());
//                            Bundle stdata = new Bundle();
//                            stdata.putString("Authorize",Author1);
//                            stdata.putString("STORYNAME",storyNAME);
//                            stdata.putString("STCONTENT",storyCoNtEnT);
//                            stdata.putString("STDESCRIbE",Desc);
//                            stdata.putString("IMGURL",ImgUrl);
//                            stdata.putString("STSOURC",StrySrc);
                                    amount_to_pay=String.valueOf("2.0");
                                    PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount_to_pay),"USD","Story org" ,PayPalPayment.PAYMENT_INTENT_SALE);
//                            PayPalPayment payPalPayment1 = new PayPalPayment(new BigDecimal((90*Integer.parseInt(amount_to_pay))/100),"USD","Pay to"+Author1 ,PayPalPayment.PAYMENT_INTENT_SALE);

                                    Intent intent =new Intent(getContext(), PaymentActivity.class);
                                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config2);
//                            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config2);
//                            intent.putExtra("Authorize",Author1);
//                            intent.putExtra("STORYNAME",storyNAME);
//                            intent.putExtra("STCONTENT",storyCoNtEnT);
//                            intent.putExtra("STDESCRIbE",Desc);
//                            intent.putExtra("IMGURL",ImgUrl);
//                            intent.putExtra("STSOURC",StrySrc);
                                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
//                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment1);
                                    startActivityForResult(intent,PAYPAL_REQUEST_CODE1);
//ItemFragment ite = new ItemFragment();
//ite.setArguments(stdata);
                                    dialog.dismiss();

                                }
                            });
                            select.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

//                                            paymentDialog(Author1, Desc, price1, ImgUrl, storyNAME,storyCoNtEnT,StrySrc,price);
                                    dialog.dismiss();

                                }
                            });


                            alertDialog1 = select.create();
                                   alertDialog1.show();

                        } else if (Float.parseFloat(prices) > 10) {
                            Toast.makeText(getContext(), "Price should be lower than or equal to 10 $", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void restrictdata(final String dt){
        final String storytitle11 = Storyname.getText().toString().trim();
        String storycontent1 = storydesc2.getText().toString().trim();

//        String storytitle = Storyname.getText().toString().trim();
//        String storycontent = viewpa.editText.getText().toString().trim();

//

        if(selectedFileURI!=null)
        {
            mProgress.setMessage("Uploading");
            mProgress.show();
            mProgress.setCancelable(false);
//
            File file = new File(selectedFileURI.getPath());

            StorageReference mstrr = mstr2.child(selectedFileURI.getLastPathSegment());
            mstrr.putFile(selectedFileURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    downloaduri = taskSnapshot.getDownloadUrl();

                    String stryname =Storyname.getText().toString().trim();
                    mypdfStoryRef.child(dt).child("story_content").setValue(downloaduri.toString());
//                    Submitingpdf(sdr,snm,downloaduri.toString());
            mProgress.dismiss();
                }
            });
        }
    }


}
