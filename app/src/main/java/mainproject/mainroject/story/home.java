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
import android.provider.MediaStore;
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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.android.gms.vision.Frame;

import com.google.android.gms.common.api.CommonStatusCodes;


import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

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

// Stories Types Identifying
    String[] stryMainCategory = {"Education","Movies","theatre","series","literature"};

    String[] literature= {"Drama","Fable","Fiction","Poetry","Novel","Non-fiction","Short story","Prose","Biography"
            ,"Science Fiction"
            ,"Essay","Autobiography","Fairy Tale","Legend","Horror fiction","Myth"
            ,"Satire","Children's literature","Speech","Fantasy","Humor","Fable"
            ,"Short Story","Realistic Fiction","Folklore","Historical Fiction","Horror","Tall Tale","Legend"
            ,"Mystery","Fiction in Verse"};

    String[] Education = {"Language","Mathematical","Religion"
            ,"healthcare administration","graphic design","psychology", "accounting"
            ,"criminal justice","nursing"
            ,"computer science","engineering","business administration",
            "organizational skills","communication skills","analytical skills","detail oriented"
            ,"compassion","critical-thinking skills","patience"};

    String[] MoviesAndSeries={"Absurdist/surreal/whimsical","Action","Adventure","Comedy","Thriller","Drama","Epic","Horror","Adventure","Animation","Comedy","Crime"
            ,"Documentary","Biography","Family","Fantasy","Film-Noir","History",
            "Musical","Mystery","Romance","Sci-fi","Sport","War","Western","Eastern","MiddleEast"};

    String[] Theatres={"Tragedy","Drama","Fringe","Immersive","Melodrama","Autobiographicals","Comedy"
            ,"Historic Plays","Farce","Solo Theatre","Epic"};

    EditText editText, prices,storydesc,Storyname,AuthorsOfBook;
    Button pdfupload;
    ImageButton StryCovers;
    ImageView img;
    TextView text;
    Button btnSubmitStory;
    FirebaseAuth mAuth;
    private String userName;
     Uri selectedFileURI=null;
    String Filepath,CoverPath;
    public static final int PAYPAL_REQUEST_CODE1 = 7171;
    PayPalConfiguration config2 = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .defaultUserEmail(paypalconfig.Live_paypal_Publiser_Email)
            .clientId(paypalconfig.paypal_client_live_Id_key)
            .acceptCreditCards(true);

    private AlertDialog.Builder StoryDetailsl;
      String subCategoryName;
    String AuthorName;


    public home() {

    }
//    paypalconfig pc = new paypalconfig();
    final FirebaseDatabase mydatabase=FirebaseDatabase.getInstance();
    private DatabaseReference mimgref = FirebaseDatabase.getInstance().getReference().child("Images");
    final DatabaseReference mypdfStoryRef = FirebaseDatabase.getInstance().getReference().child("pdfStoriesdetails");
    final DatabaseReference myStoryRef = mydatabase.getReference().child("StoriesDetails");
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
    Dictionary<String,String[]> spinnerDividers =new Hashtable<String, String[]>();
    ArrayAdapter<String> spinnerAdapter;
    ArrayAdapter<String> spinnerDividersAdapter;

    SharedPreferences sharedPreferences;
    String selectingitem;
    String selectingSubCategory;

    boolean submitShortStories=false;
    boolean submitPDFStories=false;
    boolean oneTimeDialog=false;
    boolean oneTimeDialogPDFs=false;

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
                    if(submitShortStories) {
                        shortstoriesProgress();
                        submitShortStories=false;

                    }else if(submitPDFStories)
                    {

                        pdfFunSub(stryDescri, storyNaMe);
                        submitPDFStories=false;

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
    Spinner subCategory;
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
        subCategory =(Spinner)inflate.findViewById(R.id.addingSubCategories);
        //        storydesc = view.findViewById(R.id.strydescription);
        spinnerDividers.put(stryMainCategory[0],Education);
        spinnerDividers.put(stryMainCategory[1],MoviesAndSeries);
        spinnerDividers.put(stryMainCategory[2],Theatres);
        spinnerDividers.put(stryMainCategory[3],MoviesAndSeries);
        spinnerDividers.put(stryMainCategory[4],literature);

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
        AuthorsOfBook =(EditText) inflate.findViewById(R.id.BookAuthors);

        // TODO:StoryPages
//        fragmentManager.beginTransaction().replace(R.id.content2, child).commit();
        spinnerAdapter =new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,stryMainCategory);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        typespinner.setAdapter(spinnerAdapter);

        Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.imgstyle);
        RoundedBitmapDrawable imground = RoundedBitmapDrawableFactory.create(getResources(),img);

        imground.setCornerRadius(25f);
        final String[] spitems = new String[1];
        viewPager = (ViewPager)inflate.findViewById(R.id.storypages);
        viewpa =new viewpageradapter(getContext());
        viewPager.setAdapter(viewpa);
        // Spinners Selected Items
        typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                setSelectingitem(stryMainCategory[position]);
                subCategoryName =stryMainCategory[position];
                spinnerDividersAdapter =new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,spinnerDividers.get(stryMainCategory[position].trim()));
                spinnerDividersAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                subCategory.setAdapter(spinnerDividersAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

           }

        });
        subCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Object s =subCategoryName;
                try {
                    selectingSubCategory = spinnerDividers.get(subCategoryName)[i];
                }catch(Exception ex){
                    Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();

                }
                }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        this.selectingitem =spitems[0];

        // end view pager
        StoryImg =(MenuView.ItemView) inflate.findViewById(R.id.StoryImg);
        btnSubmitStory =inflate.findViewById(R.id.submitStory);


        $pricebox =inflate.findViewById(R.id.pricebox);

        Storyname=inflate.findViewById(R.id.story_name);

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
         AuthorName = AuthorsOfBook.getText().toString();
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
                submitShortStories = true;
                oneTimeDialog=true;
                checkExistedpaypalEmail(v,inflate, null, null);
            }
        });
        pdfslct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult( Intent.createChooser(intent, "Select a PDF File to Upload"), 1);

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

            Thread uploadingBookCover = new Thread(){
                @Override
                public void run(){


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
            };
            uploadingBookCover.start();
        }
    }
    private void startposting2(final String StRurl){
        String storytitle = Storyname.getText().toString().trim();
        String storycontent = storydesc2.getText().toString().trim();
        if(uri!=null)
        {
            mProgress.setMessage("Uploading");
            mProgress.show();
            Thread uploadingBookCover = new Thread(){
                @Override
                public void run() {
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
            };
            uploadingBookCover.start();


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

    String publisheR,AuthoRs,DescriP,pRICe,IMGUrL,StorYNamE, StRContEnT,StrYsRc,StryTypes,subCategories;
    public void addingstdataafterpaying(String publisher,String Authors, String Descrip, String price11, String ImgUrl1, String STOryNAme, String STRCOntEnT, String STRYSrC, String selectingtype, String subCategoryName)
    {   this.publisheR =publisher;
        this.DescriP = Descrip;
        this.pRICe =price11;
        this.AuthoRs = Authors;
        this.IMGUrL = ImgUrl1;
        this.StorYNamE = STOryNAme;
        this.StRContEnT = STRCOntEnT;
        this.StrYsRc = STRYSrC;
        this.StryTypes = selectingtype;
        this.subCategories = subCategoryName;
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
               submitPDFStories =true;
               oneTimeDialogPDFs=true;

               checkExistedpaypalEmail(v,detaildialog,stryDescri,storyNaMe);
            }
        });

        final AlertDialog alertDialog = StoryDetails2.create();
        alertDialog.show();
    }

    public void pdfFunSub(final String stryDescri, final String storyNaMe)
    {
        final String fileprice = prices.getText().toString().trim();

         myStoryRef.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                        if (oneTimeDialogPDFs) {

                            if (Filepath != null && !Filepath.isEmpty()) {
                                if (CoverPath != null && !CoverPath.isEmpty()) {
                                    if (storyNaMe == null || storyNaMe.isEmpty()) {
                                        Toast.makeText(getContext(), "Story should have a Title", Toast.LENGTH_LONG).show();
                                    } else {


                                            if (Float.parseFloat(fileprice) > 0) {


                                                addingstdataafterpaying(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),AuthorsOfBook.getText().toString(), stryDescri, fileprice, uri.getLastPathSegment(), storyNaMe, selectedFileURI.getLastPathSegment(), "PDFSTORY", getSelectingitem(), selectingSubCategory);

                                                amount_to_pay = "0.1";
                                                PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount_to_pay), "USD", " ", PayPalPayment.PAYMENT_INTENT_SALE);

                                                Intent intent = new Intent(getContext(), PaymentActivity.class);
                                                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config2);

                                                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                                                startActivityForResult(intent, PAYPAL_REQUEST_CODE1);
                                                oneTimeDialogPDFs = false;
                                            } else if (TextUtils.isEmpty(fileprice)) {
                                                prices.setError("can't be Empty");
                                                Toast.makeText(getContext(), "Add a value to price", Toast.LENGTH_SHORT).show();
                                            } else if (Float.parseFloat(fileprice) <= 0) {
                                                prices.setError("can't be 0 or Less than 0");
                                                Toast.makeText(getContext(), "Price Value Should be more than zero", Toast.LENGTH_SHORT).show();

                                            }

                                    }
                                } else {
                                    Toast.makeText(getContext(), "add cover image", Toast.LENGTH_LONG).show();

                                }
                            } else {
                                Toast.makeText(getContext(), "you must select pdf File", Toast.LENGTH_LONG).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_intent1 && resultCode == RESULT_OK) {


            uri = data.getData();
            File cover = new File(uri.toString());
//            uploadedCoverName = cover.getName();
//            tokens = new StringTokenizer(uploadedFileName, ":");
//            first = tokens.nextToken();
//            file_1 = tokens.nextToken().trim();
            CoverPath = cover.getPath();

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
                File file = new File(selectedFileURI.toString());

                Log.d("", "File : " + file.getName());
                uploadedFileName = file.getName();
                tokens = new StringTokenizer(uploadedFileName, ":");
                first = tokens.nextToken();
//            file_1 = tokens.nextToken().trim();
                Filepath = file.getPath();
                filename.setText(file.getPath());
//                Filepath = selectedFileURI.getLastPathSegment();
//                child.child("OwnerEmail").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
//                child.child("FileName").setValue(file.getName());

            }
        }else if(resultCode == Camera_Request)
        {
            Bitmap image = BitmapFactory.decodeResource(getContext().getResources(),(int) data.getExtras().get("data"));

            BarcodeDetector detector = new BarcodeDetector.Builder(getContext())
                    .setBarcodeFormats(Barcode.DATA_MATRIX)
                    .build();
            if(!detector.isOperational())
            {
                return;
            }
            Frame frame = new Frame.Builder().setBitmap(image).build();
            SparseArray<Barcode> barcodes = detector.detect(frame);
            Barcode thiscode =barcodes.valueAt(0);
            String barcode = thiscode.rawValue;
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
//                               final DatabaseReference Story_Name = myStoryRef.child(StorYNamE);
                               final DatabaseReference Story_Name = myStoryRef.push();
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
                               Story_Name.child("StorySavingsrc").setValue("AppCreationStory");
                               Story_Name.child("subCategory").setValue(subCategories);
                               Story_Name.child("publishDate").setValue(Calendar.getInstance().getTime());
                               Story_Name.child("StrCatSearchObj").setValue(StryTypes+subCategories);

                               $pricebox.setText(null);
                               Storyname.setText(null);
                               Toast.makeText(getContext(), "Story have been Submitted successfully", Toast.LENGTH_LONG).show();
                               break;

                           case "PDFSTORY":
                               final DatabaseReference Pdf_Story_Name  = myStoryRef.push();
                               restrictdata(Pdf_Story_Name.getKey());
                               startposting2(Pdf_Story_Name.getKey());
                               Pdf_Story_Name.child("storyNaMe").setValue(StorYNamE);
                               Pdf_Story_Name.child("publishers").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//                Story_Content.child("story_content").setValue(FileUrl);
                               Pdf_Story_Name.child("Author").setValue(AuthorName);

                               Pdf_Story_Name.child("LogoSrc").setValue(uri.getLastPathSegment());
                               Pdf_Story_Name.child("Likes").setValue(0);
                               Pdf_Story_Name.child("disLikes").setValue(0);
                               Pdf_Story_Name.child("Reports").setValue(0);
                               Pdf_Story_Name.child("STDESC").setValue(DescriP);
                               Pdf_Story_Name.child("StrType").setValue(StryTypes);
                               Pdf_Story_Name.child("subCategory").setValue(subCategories);
                               Pdf_Story_Name.child("StorySavingsrc").setValue("PDFSTORY");
                               Pdf_Story_Name.child("publishDate").setValue(Calendar.getInstance().getTime());
                               Pdf_Story_Name.child("StrCatSearchObj").setValue(StryTypes+subCategories);

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
    int Camera_Request = 123;

    public void readISBN(View v)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,Camera_Request);
    }
    public void shortstoriesProgress(){

        final String prices = $pricebox.getText().toString().trim();
        final String story_content = viewpa.editText.getText().toString().trim();
        final String story_description = viewpa.storydesc.getText().toString().trim();
        story_Name = Storyname.getText().toString().trim();

            myStoryRef.addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(oneTimeDialog) {
                    String storiesname = String.valueOf(dataSnapshot.child("storyNaMe").getValue());

                    if (dataSnapshot.hasChild(story_Name)) {
                        Toast.makeText(getContext(), "Story is already existed, please change the Title", Toast.LENGTH_LONG).show();

                    }
                    if (!dataSnapshot.hasChild(story_Name)) {

                        if (story_content.length() > 3000) {
                            Toast.makeText(getContext(), "short Stories Content Should be less than or equal  3000 char(100 line)", Toast.LENGTH_SHORT).show();
                        } else {

                                final String fk1 = userName.toString().trim();


                                final AlertDialog.Builder select = new AlertDialog.Builder(getContext());
                                AlertDialog alertDialog1 = null;
                                select.setPositiveButton("Paypal", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        addingstdataafterpaying(fk1,AuthorsOfBook.getText().toString(), story_description, prices, uri.getLastPathSegment(), story_Name, story_content, "AppCreationStory", getSelectingitem(), selectingSubCategory);
                                        amount_to_pay = "2.0";
                                        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount_to_pay), "USD", "Story org", PayPalPayment.PAYMENT_INTENT_SALE);
//                            PayPalPayment payPalPayment1 = new PayPalPayment(new BigDecimal((90*Integer.parseInt(amount_to_pay))/100),"USD","Pay to"+Author1 ,PayPalPayment.PAYMENT_INTENT_SALE);

                                        Intent intent = new Intent(getContext(), PaymentActivity.class);
                                        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config2);
                                        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                                        startActivityForResult(intent, PAYPAL_REQUEST_CODE1);
                                        dialog.dismiss();

                                    }
                                });
                                select.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();

                                    }
                                });


                                alertDialog1 = select.create();
                                alertDialog1.show();
                                oneTimeDialog=false;
//                            } else if (Float.parseFloat(prices) > 10) {
//                                Toast.makeText(getContext(), "Price should be lower than or equal to 10 $", Toast.LENGTH_LONG).show();
//                            }
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
    @Override
    public void onCreateOptionsMenu(Menu menu,
                                    MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.mainmenu, menu);
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
//
            File file = new File(selectedFileURI.getPath());

            Thread uploadingPDF = new Thread(){
                @Override
                public void run() {
                    StorageReference mstrr = mstr2.child(selectedFileURI.getLastPathSegment());
                    mstrr.putFile(selectedFileURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            downloaduri = taskSnapshot.getDownloadUrl();

                            String stryname = Storyname.getText().toString().trim();
                            myStoryRef.child(dt).child("story_content").setValue(downloaduri.toString());
//                    Submitingpdf(sdr,snm,downloaduri.toString());
                            mProgress.dismiss();
                        }
                    });
                }
            };
            uploadingPDF.start();

        }
    }


}
