package mainproject.mainroject.story;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import mainproject.mainroject.story.Tables.Stories;

import static android.app.Activity.RESULT_OK;


public class updatestorycontent extends Fragment {

    private AlertDialog mProgress;

    public updatestorycontent() {
        // Required empty public constructor
    }
    private static final int Gallery_intent = 2;

    private DatabaseReference muserstoriesref = FirebaseDatabase.getInstance().getReference().child("StoriesDetails");
    private StorageReference myStorage = FirebaseStorage.getInstance().getReference();;
    StorageReference mstr1 ;
    FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
    String currentU;
    DatabaseReference child;
    Uri uri;
    DatabaseReference imgnames;
    ImageButton imgupload;
    private Uri downloaduri = null;
    String imgpath;
    String userEmail = currentuser != null ? currentuser.getEmail() : null;;
    String downloadurl;
    private Button userimgbtn;
    private Button userstoriesbtn;
    private Button purchsedstoriesbtn;
    ImageButton img;
    TextView titles ,cotent;
    Button btns;
    EditText newdesc,newpric;
    Bundle bundle;
    String stype = null;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_updatestorycontent, container, false);
//        assert bundle != null;
//        mstr1 = myStorage.child("Photos").child(currentU).child("storiesCovers");
        mProgress = new ProgressDialog(getContext());
        img = (ImageButton)view.findViewById(R.id.storynewimg);
        titles = (TextView)view.findViewById(R.id.titles);
        cotent= (TextView)view.findViewById(R.id.contents);
        newdesc = (EditText)view.findViewById(R.id.EditingDesc);
        newpric = (EditText)view.findViewById(R.id.newprice);
        btns =(Button)view.findViewById(R.id.updatedata);
        bundle =getArguments();
        String type;
        Picasso.with(getContext()).load(bundle.getString("StoryIMG")).into(img);
        titles.setText(bundle.getString("StoryName"));
        cotent.setText(bundle.getString("StoryCont"));
        newdesc.setText(bundle.getString("StoryDesc"));
        newpric.setText(bundle.getString("StoryPrc"));
        type = bundle.getString("StoryType");
        if(type != null) {
            if (type.equals("AppCreationStory")) {
                final DatabaseReference striesdb = FirebaseDatabase.getInstance().getReference().child("StoriesDetails").child(titles.getText().toString());

                final Query updatablestrydata = striesdb.orderByChild("storyNaMe").equalTo(titles.getText().toString());

                btns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        striesdb.child("STDESC").setValue(newdesc.getText().toString());
                        striesdb.child("story_price").setValue(newpric.getText().toString());
                    }
                });
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, Gallery_intent);
                    }
                });
                stype = "AppCreationStory";
//        muserstoriesref.child(bundle.getString("StoryName")).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Stories stories= dataSnapshot.getValue(Stories.class);
//                assert stories != null;
//                String imgs=stories.getLogoUrl();
//                Picasso.with(getActivity()).load(imgs).fit().into(img);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
            } else if (type.equals("PDFSTORY")) {
                stype = "PDFSTORY";
                Toast.makeText(getContext(), "coming soon", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getContext(),"this storyType not specified",Toast.LENGTH_LONG).show();
        }
        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_intent && resultCode == RESULT_OK){

            mProgress.setMessage("Updating...");
            mProgress.show();
            uri =data.getData();
//            Picasso.with(getActivity()).load(downloaduri).fit().into(imgupload);
//            child = mimgref.push();
                StorageReference mstr = myStorage.child("Photos").child(userEmail).child("storylogo").child(bundle.getString("StoryName")+"/").child(uri.getLastPathSegment());
                mstr.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override

                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Updating Successful", Toast.LENGTH_LONG).show();

                        mProgress.dismiss();
                        downloaduri = taskSnapshot.getDownloadUrl();
                        Picasso.with(getActivity()).load(downloaduri).fit().into(img);

                        muserstoriesref.child(bundle.getString("StoryName")).child("LogoUrl").setValue(downloaduri.toString());
//                    muserstoriesref.child(currentuser.getDisplayName()).child("UserImg").setValue(downloaduri.toString());

                    }
                });

//            imgpath = uri.getLastPathSegment();
//            child.child("OwnerEmail").setValue(currentU);
//            child.child("ImageName").setValue(imgpath);

//            FileDownloadTask img_name = mstr.getFile(Uri.parse(uri.getLastPathSegment()));

//            imgnames = mimgref.child(child.toString().replace("#",",").replace(".",",").replace("$",",").replace("[",",").replace("]",","));
//String img_names = String.valueOf(imgnames);

//imgnames += img_names.replace(".",",");
//            DatabaseReference imgdate = mimgref.child();

//            downloaduri= taskSnapshot.getDownloadUrl();
            //            imgpath.replace(".",",");
//               das= String.valueOf(myStorage.getDownloadUrl());
//           String downloaduripath = uri.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null

            //            child.child("ImgUrl").setValue(das);

            //            imgnames.child("ImgSize").setValue(mstr.getBytes(Long.parseLong(img_name.toString())));

        }
    }

    /*
      This interface must be implemented by activities that contain this
      fragment to allow an interaction in this fragment to be communicated
      to the activity and potentially other fragments contained in that
      activity.
      <p>
      See the Android Training lesson <a href=
      "http://developer.android.com/training/basics/fragments/communicating.html"
      >Communicating with Other Fragments</a> for more information.
     */

}