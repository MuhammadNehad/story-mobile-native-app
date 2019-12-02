package mainproject.mainroject.story;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.BreakIterator;
import java.util.StringTokenizer;

/**
 * Created by UNiversaL on 3/27/2018.
 */

public class pagesFragment extends Fragment {
   home hm = new home();
    String uploadedFileName;
     Button pdfslct;
    private StringTokenizer tokens;
    private String first;
    private String file_1;
    private BreakIterator txt_file_name_1;
     TextView filename;
     StorageReference myStorage = FirebaseStorage.getInstance().getReference();
   StorageReference mstr2 = myStorage.child("PDFuploads").child(FirebaseAuth.getInstance().getCurrentUser().getEmail()).child("StoryDetail");
    private Uri downloaduri = null;
     DatabaseReference child;
    private String Filepath;
    private AlertDialog.Builder StoryDetails2;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager = getFragmentManager();

    DatabaseReference muserref = FirebaseDatabase.getInstance().getReference().child("UserDetail");
    DatabaseReference mimgref = FirebaseDatabase.getInstance().getReference().child("PDFFILES");
    private Uri selectedFileURI =null;
    private ProgressDialog mProgress;

    public pagesFragment(){

    }
    EditText editText;
     Button pdfupload;
ImageButton StryCovers;
    EditText storydesc;
    viewpageradapter viewpa;
@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pages, null);
    editText = view.findViewById(R.id.userEntry);
    storydesc = view.findViewById(R.id.strydescription);
    pdfslct =(Button)view.findViewById(R.id.selectpdffile);
    pdfupload =(Button)view.findViewById(R.id.uploadpdffile);
    filename = (TextView)view.findViewById(R.id.uploadedfilename);

    pdfupload =(Button)view.findViewById(R.id.uploadpdffile);
        pdfslct =(Button)view.findViewById(R.id.selectpdffile);
        storydesc = view.findViewById(R.id.strydescription);
        StryCovers =hm.logoupload;
        filename = (TextView)view.findViewById(R.id.uploadedfilename);

//        viewpa.pdfslct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("application/*");
//                startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), 1);
//
//            }
//        });
//        viewpa.pdfupload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String strytitle =  hm.Storyname.getText().toString();
//                String strydescr =  storydesc.getText().toString();
//restrictdata(strydescr ,strytitle);
//            }
//        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    DatabaseReference myStoryRef = FirebaseDatabase.getInstance().getReference().child("StoriesDetails");
    protected void Submitingpdf(final String stryDescri, final String storyNaMe , final String FileUrl){
        StoryDetails2 =new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();
        final View detaildialog = inflater.inflate(R.layout.pricedialog,null);
        StoryDetails2.setView(detaildialog);

        final EditText prices= (EditText) detaildialog.findViewById(R.id.pdfprice);
        final Button accept = (Button) detaildialog.findViewById(R.id.submitingPDFbutton);
        final AlertDialog alertDialog = StoryDetails2.create();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fileprice = prices.toString().trim();

                final DatabaseReference Story_Content = myStoryRef.child(storyNaMe);
                final DatabaseReference Story_Name = myStoryRef.child(storyNaMe);
                final DatabaseReference Story_price = myStoryRef.child(storyNaMe);
                final DatabaseReference fk = myStoryRef.child(storyNaMe);
                final DatabaseReference Stdesc = myStoryRef.child(storyNaMe);
                myStoryRef.child(storyNaMe).setValue(storyNaMe);
                Story_Name.child("storyNaMe").setValue(storyNaMe);
                fk.child("Author").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                Story_Content.child("story_content").setValue(FileUrl);
                Story_price.child("story_price").setValue(fileprice);
                myStoryRef.child(storyNaMe).child("LogoSrc").setValue(hm.uri.getLastPathSegment());
                Stdesc.child("STDESC").setValue(stryDescri);
                prices.setText(null);
                hm.Storyname.setText(null);

                Toast.makeText(getContext(), "Story have been Submitted successfully", Toast.LENGTH_LONG).show();

            }
        });


        alertDialog.show();
    }
//    public void restrictdata(final String StOrYDeScRi , final String StOrYnAmE){
//
////        String storytitle = Storyname.getText().toString().trim();
////        String storycontent = viewpa.editText.getText().toString().trim();
//        if(!TextUtils.isEmpty(StOrYDeScRi) && !TextUtils.isEmpty(StOrYnAmE) && selectedFileURI!=null)
//        {
//            mProgress.setMessage("Uploading");
//            mProgress.show();
//            mProgress.setCancelable(false);
//
//            File file = new File(selectedFileURI.getPath().toString());
//            Log.d("", "File : " + file.getName());
//            uploadedFileName = file.getName().toString();
//            tokens = new StringTokenizer(uploadedFileName, ":");
//            first = tokens.nextToken();
//            file_1 = tokens.nextToken().trim();
//            filename.setText(file_1);
////            child = mimgref.push();
//            StorageReference mstrr = mstr2.child(selectedFileURI.getLastPathSegment());
//            mstrr.putFile(selectedFileURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    downloaduri = taskSnapshot.getDownloadUrl();
////               Picasso.with(getActivity()).load(downloaduri).fit().into(imgupload);
////               child = mimgref.push();
////               child.child("PDFURL").setValue(downloaduri.toString());
//
//
//                    muserref.child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child("PDFFILE").setValue(downloaduri.toString());
//Submitingpdf(StOrYDeScRi ,StOrYnAmE,downloaduri.toString());
//                      }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });
//    }
//    }
public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                selectedFileURI = data.getData();

                Filepath = selectedFileURI.getLastPathSegment();
//                child.child("OwnerEmail").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
//                child.child("FileName").setValue(file.getName());

            }
    }
}
    public class viewpageradapter extends PagerAdapter {
        Context context;
        LayoutInflater inflater;
        private static final int Gallery_intent =1;


//    @Override
//    public Fragment getItem(int position) {
//        Bundle bundle = new Bundle();
//
//        return new pagesFragment();
//
//    }

        public viewpageradapter(Context context){
            this.context = context;
        }
        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return (view==(LinearLayout)object);
        }
        EditText editText,storydesc;
        Button strycover,pdfslct, pdfupload;
        TextView filename;

        @Override

        public Object instantiateItem(ViewGroup container, int position) {
            inflater =(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view= inflater.inflate(R.layout.pages,container,true);
            LinearLayout linearLayout= view.findViewById(R.id.pagenum);
            LinearLayout linearLayout2= view.findViewById(R.id.pdfform);
            editText = view.findViewById(R.id.userEntry);
            storydesc = view.findViewById(R.id.strydescription);
            pdfslct =(Button)view.findViewById(R.id.selectpdffile);
            pdfupload =(Button)view.findViewById(R.id.uploadpdffile);
            filename = (TextView)view.findViewById(R.id.uploadedfilename);
            pdfslct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("application/pdf");
                    startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), 1);

                }
            });
            pdfupload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strytitle =  hm.Storyname.getText().toString();
                    String strydescr =  storydesc.getText().toString();
                    restrictdata(strydescr ,strytitle);
                }
            });
            container.addView(view);

            return view;


        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            hm.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {
                    selectedFileURI = data.getData();

                    Filepath = selectedFileURI.getLastPathSegment();
//                child.child("OwnerEmail").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
//                child.child("FileName").setValue(file.getName());

                }
            }
        }
        protected void Submitingpdf(final String stryDescri, final String storyNaMe , final String FileUrl){
            StoryDetails2 =new AlertDialog.Builder(getContext());

            LayoutInflater inflater = getLayoutInflater();
            final View detaildialog = inflater.inflate(R.layout.pricedialog,null);
            StoryDetails2.setView(detaildialog);

            final EditText prices= (EditText) detaildialog.findViewById(R.id.pdfprice);
            final Button accept = (Button) detaildialog.findViewById(R.id.submitingPDFbutton);
            final AlertDialog alertDialog = StoryDetails2.create();
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String fileprice = prices.toString().trim();

                    final DatabaseReference Story_Content = myStoryRef.child(storyNaMe);
                    final DatabaseReference Story_Name = myStoryRef.child(storyNaMe);
                    final DatabaseReference Story_price = myStoryRef.child(storyNaMe);
                    final DatabaseReference fk = myStoryRef.child(storyNaMe);
                    final DatabaseReference Stdesc = myStoryRef.child(storyNaMe);
                    myStoryRef.child(storyNaMe).setValue(storyNaMe);
                    Story_Name.child("storyNaMe").setValue(storyNaMe);
                    fk.child("Author").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    Story_Content.child("story_content").setValue(FileUrl);
                    Story_price.child("story_price").setValue(fileprice);
                    myStoryRef.child(storyNaMe).child("LogoSrc").setValue(hm.uri.getLastPathSegment());
                    Stdesc.child("STDESC").setValue(stryDescri);
                    prices.setText(null);
                    hm.Storyname.setText(null);

                    Toast.makeText(getContext(), "Story have been Submitted successfully", Toast.LENGTH_LONG).show();

                }
            });


            alertDialog.show();
        }
        public void restrictdata(final String StOrYDeScRi , final String StOrYnAmE){

//        String storytitle = Storyname.getText().toString().trim();
//        String storycontent = viewpa.editText.getText().toString().trim();
            if(!TextUtils.isEmpty(StOrYDeScRi) && !TextUtils.isEmpty(StOrYnAmE) && selectedFileURI!=null)
            {
                mProgress.setMessage("Uploading");
                mProgress.show();
                mProgress.setCancelable(false);

                File file = new File(selectedFileURI.getPath().toString());
                Log.d("", "File : " + file.getName());
                uploadedFileName = file.getName().toString();
                tokens = new StringTokenizer(uploadedFileName, ":");
                first = tokens.nextToken();
                file_1 = tokens.nextToken().trim();
                filename.setText(file_1);
                child = mimgref.push();
                StorageReference mstr = mstr2.child(selectedFileURI.getLastPathSegment());
                mstr.putFile(selectedFileURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        downloaduri = taskSnapshot.getDownloadUrl();
//               Picasso.with(getActivity()).load(downloaduri).fit().into(imgupload);
//               child = mimgref.push();
//               child.child("PDFURL").setValue(downloaduri.toString());


                        muserref.child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child("PDFFILE").setValue(downloaduri.toString());
                        Submitingpdf(StOrYDeScRi ,StOrYnAmE,downloaduri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }
        @Override
        public void destroyItem(final ViewGroup container, int position, final Object object) {
//     container.removeView((LinearLayout)object);
//if(container==null) {
//}else if(container!=null){
//
//    for (int i=0; i<= getCount(); i++ ){
//
            container.removeView((LinearLayout)object);

//    }
        }
//((ViewPager) container).removeView((View) object);

        public void destroyItem(View container, int position, Object object) {
            throw new UnsupportedOperationException("Required method destroyItem was not overridden");
        }

        public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

            if(editText.getText().toString() != null || editText.getText().toString()!= " " ) {
                outState.putString("ourKey", editText.getText().toString());
                editText.onSaveInstanceState();
            }   }
    }
}