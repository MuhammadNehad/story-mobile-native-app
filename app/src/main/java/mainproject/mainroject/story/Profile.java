package mainproject.mainroject.story;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import static android.app.Activity.RESULT_OK;
import static mainproject.mainroject.story.home.maxSize;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference mimgref = FirebaseDatabase.getInstance().getReference().child("Images");
    private DatabaseReference muserref = FirebaseDatabase.getInstance().getReference().child("UserDetail");
    private StorageReference myStorage;
    FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
    String currentU;
    DatabaseReference child;
    Uri uri;
    DatabaseReference imgnames;
    ImageButton imgupload;
    private Uri downloaduri = null;
    String imgpath;
    String downloadurl;
    private Button userimgbtn;
    private Button userstoriesbtn;
    private Button purchsedstoriesbtn;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int Gallery_intent = 2;
    private static final int CAMERA_REQUEST_CODE = 1;

    private OnFragmentInteractionListener mListener;
    private long maxfilesize=0;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    StorageReference mstr1 ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private ProgressDialog mProgress;
    String currentuid;
    Query curimg;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager = getFragmentManager();
    signup sp =new signup();
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.profile, container, false);
        currentU = currentuser.getEmail();
        currentuid = currentuser.getUid();
        String user = String.valueOf(currentuser.getUid());
        DatabaseReference curuser=muserref.child(currentuser.getDisplayName());
        curimg = curuser.orderByChild("UserImg");

        final TextView urlName = view.findViewById(R.id.UserName);
        imgupload = (ImageButton) view.findViewById(R.id.imgupload);
        userimgbtn= (Button) view.findViewById(R.id.totalcurrentuserimgs);
        userstoriesbtn= (Button) view.findViewById(R.id.CurrentuserStories);
        purchsedstoriesbtn= (Button) view.findViewById(R.id.Purchasestorie);
        myStorage = FirebaseStorage.getInstance().getReference();
        mstr1 = myStorage.child("Photos").child(currentU).child("ProfileImages");
//        Task<Uri> img =mstr1.getDownloadUrl();
        mProgress=new ProgressDialog(getContext());

        userimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userimgs ui = new userimgs();
                FragmentManager fragmentManager1 = getFragmentManager();
                assert fragmentManager1 != null;
                fragmentManager1.beginTransaction().replace(R.id.content, new userimgs(),null).addToBackStack(null).commit();
            }
        });
        userstoriesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                userimgs ui = new userimgs();
                FragmentManager fragmentManager1 = getFragmentManager();

                assert fragmentManager1 != null;
                fragmentManager1.beginTransaction().replace(R.id.content, new userstories(),null).addToBackStack(null).commit();
            }
        });

        purchsedstoriesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                userimgs ui = new userimgs();
                FragmentManager fragmentManager1 = getFragmentManager();
                assert fragmentManager1 != null;
                fragmentManager1.beginTransaction().replace(R.id.content, new addedStoriesfrompurchasing(),null).addToBackStack(null).commit();

            }
        });
        imgupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        if(maxSize > 0) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, Gallery_intent);
        }else{
            Toast.makeText(getContext(),"You reached max Limit",Toast.LENGTH_LONG).show();
        }
            }
        });
        if(curimg != null) {
            curuser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    urlName.setText(user.getName());
                    if(user.getUserImg()!=null) {
                        String imgurl = user.getUserImg();
                        if (!imgurl.isEmpty()) {
                            imgupload.setBackground(Drawable.createFromPath(imgurl));
                            Picasso.with(imgupload.getContext()).load(imgurl).fit().into(imgupload);
                            //                            urlimg.setText(dataSnapshot.getValue(User.class).getName());
                        } else {
//                            Toast.makeText(getContext(), "Profile form", Toast.LENGTH_LONG).show();
                            //    throw new IllegalArgumentException("Path must not be empty.");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(getContext(),"Profile",Toast.LENGTH_LONG).show();
        }

//        imgupload.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,CAMERA_REQUEST_CODE);
////                if (intent.resolveActivity(getLoaderManager()) != null) {
////                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
////                }
//                return true;
//            }
//        });
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,
                                    MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.mainmenu, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //
//    private void showdata(DataSnapshot dataSnapshot) {
//for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
//    Images images = new Images();
//  String urii =  images.setImgpath(dataSnapshot1.child(currentU).getValue(Images.class).getImgpath());
//    Picasso.with(getActivity()).load(Uri.parse(urii)).fit().into(imgupload);
//
//}
//    }

    public void  calculateUserStorageSize(){
        muserref.child(currentuser.getDisplayName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("storageUserSize")) {
                    long sizeOfuserStorage = Long.parseLong(String.valueOf(dataSnapshot.child("storageUserSize").getValue()));

                    if(dataSnapshot.hasChild("maxUserStorageSize")) {


                        long maxsizeOfuserStorage = Long.parseLong(String.valueOf(dataSnapshot.child("maxUserStorageSize").getValue()));

                        maxSize = maxsizeOfuserStorage - sizeOfuserStorage;
                        Log.d("FileSIZE", String.valueOf(maxSize) + " " + String.valueOf(sizeOfuserStorage));
                    }else{
                        maxSize = maxSize- sizeOfuserStorage;
                        muserref.child(currentuser.getDisplayName()).child("maxUserStorageSize").setValue(1024);

                    }
                    }else{
                    muserref.child(currentuser.getDisplayName()).child("storageUserSize").setValue(0);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    Query query1 = muserref.orderByChild("Email").equalTo(currentU);
    String das;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_intent && resultCode == RESULT_OK){

            mProgress.setMessage("Uploading...");
            mProgress.show();
             uri =data.getData();
            Cursor returndata = getContext().getContentResolver().query(uri,null,null,null,null);
            assert returndata != null;
            int sizeIndex = returndata.getColumnIndex(OpenableColumns.SIZE);
            returndata.moveToFirst();

            Double ImageSize=((Double.parseDouble(String.valueOf(returndata.getLong(sizeIndex)))/1000)/1000);
//                Formatter.formatFileSize(getContext(), returndata.getLong(sizeIndex))
            Log.d("FileSize", String.valueOf(ImageSize));

            if(maxSize>100){
                maxfilesize= 100;
            }else if(Integer.parseInt(String.valueOf(maxSize))<100)
            {
                maxfilesize = maxSize;
            }else{
                maxfilesize =0;
                Toast.makeText(getContext(),"you have reached storage limit",Toast.LENGTH_LONG).show();
            }
            if(ImageSize < maxSize) {

//                File cover = new File(uri.toString());
//                CoverPath = cover.getPath();
//                filesFilters filters = new filesFilters(cover);
////                if(filters.accept(cover)) {
//                Picasso.with(getActivity()).load(uri).fit().into(logoupload);
////                }else{
//                Toast.makeText(getContext(),"please, upload a real Image",Toast.LENGTH_LONG).show();
//                }

                child = mimgref.push();
                final boolean[] fileuploaded = {true};
                final boolean[] datasaved = {true};
                final StorageReference mstr = mstr1.child(uri.getLastPathSegment());
                mstr.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override

                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Uploading Successful", Toast.LENGTH_LONG).show();
                        mProgress.dismiss();
                        final long curuploadfileSize = taskSnapshot.getBytesTransferred();
                        downloaduri = taskSnapshot.getDownloadUrl();


                                if(fileuploaded[0]) {
                                    muserref.child(currentuser.getDisplayName()).child("storageUserSize").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(datasaved[0]) {
                                                Double curSize = Double.parseDouble(String.valueOf(dataSnapshot.getValue()));

                                                Log.d("checktheloop", "111");
                                                Double lastval =curSize + ((curuploadfileSize/1024)/1024);
                                                muserref.child(currentuser.getDisplayName()).child("storageUserSize").setValue(lastval);
                                                calculateUserStorageSize();
                                                datasaved[0] = false;

                                            }
                                            fileuploaded[0] = false;
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }


                                    });
                                }
                                Picasso.with(getActivity()).load(downloaduri).fit().into(imgupload);
                                child.child("ImgUrl").setValue(downloaduri.toString());
                                muserref.child(currentuser.getDisplayName()).child("UserImg").setValue(downloaduri.toString());


                    }
                });

                imgpath = uri.getLastPathSegment();
                child.child("OwnerEmail").setValue(currentU);
                child.child("ImageName").setValue(imgpath);

            }
             //            Picasso.with(getActivity()).load(downloaduri).fit().into(imgupload);


        }


        }
private void getimg()
{
    if(downloaduri != null) {
        child = mimgref.push();
        StorageReference mstr = mstr1.child(downloaduri.getLastPathSegment());
    }
}
public void img(){}
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
private void setimg(){

    myStorage.child(currentU+this.imgpath).getDownloadUrl();
}
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

/*
record Audio and upload audio
private button mrecordbtn;
private TextView txt;
private MediaRecorder mrecorder;
private String filename=null;
findviewbyid
filename = Environment.getExternalStorageDirectory().getAbsolutepath()
filename="/recorde_audio.3gp"
ontouch(
if(motionEvent.getaction()==MotionEvent.Action_Down){
StartRecording();
}
else if(motionEvent.getaction()==MotionEvent.Action_UP){
StopRecoding();
        }
        return false;)

 private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP/default);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

public uploadAudio(){
StorageReference filepath = mstorage.child("audio").child("new_audio_3gp")
URI uri=Uri.fromfile(new File(mfilename)
add onsuccess};

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
 */