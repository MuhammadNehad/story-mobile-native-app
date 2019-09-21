package mainproject.mainroject.story;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import mainproject.mainroject.story.Tables.Stories;
import mainproject.mainroject.story.Tables.comments;


/**
 * A simple {@link Fragment} subclass.
 */
public class othersprofiles_and_details extends Fragment {

    private DatabaseReference mimgref = FirebaseDatabase.getInstance().getReference().child("Images");
    private DatabaseReference muserref = FirebaseDatabase.getInstance().getReference().child("UserDetail");
    private StorageReference myStorage;
    FirebaseDatabase mydatabase=FirebaseDatabase.getInstance();
    FirebaseUser auth1 = FirebaseAuth.getInstance().getCurrentUser();
    String currentU;
    DatabaseReference child;
    Uri uri;
    DatabaseReference imgnames;
    ImageButton visitorimgview;
    private Uri downloaduri = null;
    String imgpath;
    String downloadurl;
    DatabaseReference StsRef = FirebaseDatabase.getInstance().getReference().child("StoriesDetails");
    private Profile.OnFragmentInteractionListener mListener;
    private Button userimgbtn;
    Button targetstories;
    ImageButton followees, voteTo,voteAgainst;
    private Button purchsedstoriesbtn;
    private ProgressDialog mProgress;
    private FirebaseRecyclerOptions<Stories> troption;
    private FirebaseRecyclerAdapter<Stories, trblogholder> trfbra;
    private AlertDialog.Builder StoryDetailsl;
    private FirebaseAuth auth ;
    private FirebaseListAdapter<comments> trfbla;
    DatabaseReference mystrateRef = mydatabase.getReference().child("storyratings");
    DatabaseReference psdb = mydatabase.getReference().child("purchasedstories");
    DatabaseReference osdb = mydatabase.getReference().child("StoriesDetails");
    final DatabaseReference  mFollowingsRef=  mydatabase.getReference().child("Cur_User_Followings");

    private ProgressDialog mprogress;
    ImageButton voteHonor;

    public othersprofiles_and_details() {
        // Required empty public constructor
    }
    String dispname;
    Query curimg;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View othersprofiles =  inflater.inflate(R.layout.fragment_othersprofiles_and_details, container, false);
        RecyclerView hoststories = othersprofiles.findViewById(R.id.trlist);
        hoststories.setHasFixedSize(true);
        followees= (ImageButton) othersprofiles.findViewById(R.id.follow);
        voteTo =(ImageButton) othersprofiles.findViewById(R.id.votelike);
        voteAgainst =(ImageButton) othersprofiles.findViewById(R.id.votedislike);
        voteHonor =(ImageButton) othersprofiles.findViewById(R.id.votehonest);

        //        hoststories.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),3);
        hoststories.setLayoutManager(manager);
        hoststories.setHasFixedSize(true);
        currentU = auth1.getEmail();
//        currentuid = currentuser.getUid();
        String user = String.valueOf(auth1.getUid());
        Bundle buncoe =getArguments();
        assert buncoe != null;
        dispname =buncoe.getString("DisplayName");
        String dispmail =buncoe.getString("UserMail");

        assert dispname != null;
        DatabaseReference curuser=muserref.child(dispname);
        curimg = curuser.orderByChild("UserImg");
        final TextView un = othersprofiles.findViewById(R.id.UserName);
    //        curimg = curuser.orderByChild("UserImg");
//        urlimg = othersprofiles.findViewById(R.id.UserName);
        visitorimgview = (ImageButton) othersprofiles.findViewById(R.id.visitorimgview);
        targetstories= (Button) othersprofiles.findViewById(R.id.Current2userStories);

        //        purchsedstoriesbtn= (Button) othersprofiles.findViewById(R.id.Purchasestorie);
        Query userguestview = muserref.orderByChild("Email").equalTo(dispmail);
        Query recStrs = StsRef.orderByChild("Author").equalTo(dispname);
        un.setText(dispname);

        if(un.getText().toString().equals(auth1.getDisplayName()))
        {
            followees.setEnabled(false);
           voteAgainst.setEnabled(false);
           voteHonor.setEnabled(false);
           voteTo.setEnabled(false);
        }
        if(curimg != null) {
            curimg.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User us = new User();
                    us.setUserImg(dataSnapshot.getValue(User.class).getUserImg());
//                                                        String img = String.valueOf(dataSnapshot.getValue(User.class));
//                                                        if(img == null)
//                                                        {((ImageView)v.findViewById(id.comterimg)).setBackground(null);}
//                                                        else if(img != null) {
                    Picasso.with(getContext()).load(us.getUserImg()).fit().into(visitorimgview);
//
//              s      try {
//                        visitorimgview .setBackground(null);
//                        Picasso.with(getContext()).load(imgurl).into(visitorimgview );
////                            urlimg.setText(dataSnapshot.getValue(User.class).getName());
//                    }
//                    catch (IllegalArgumentException e)
//                    {
//                        visitorimgview.setImageResource(R.drawable.ic_image_black_24dp);
////    throw new IllegalArgumentException("Path must not be empty.");
//                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });//
//                    String imgurl = String.valueOf(dataSnapshot.getValue());


        }
        else if(curimg == null){
            Toast.makeText(getContext(),"Profile",Toast.LENGTH_LONG).show();
        }
        troption = new FirebaseRecyclerOptions.Builder<Stories>()
                .setQuery(recStrs, Stories.class)
                .build();
        trfbra =new FirebaseRecyclerAdapter<Stories, trblogholder>(troption) {
            @Override
            public trblogholder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.targetstories, parent, false);
        mProgress=new ProgressDialog(getContext());


                return new trblogholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull trblogholder holder, final int position, @NonNull final Stories model) {

//                holder.setContent(model.getStory_content());
                holder.setAuthor(model.getAuthor());
                holder.setStoryNAme(model.getStoryNaMe());
//                holder.setPrice(model.getStory_price());
                holder.setMimgurl(getContext(),model.getLogoUrl());
                holder.setContent(model.getSTDESC());
                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Stories stories = trfbra.getItem(position);
                        showDetailsDialog(stories.getAuthor() ,stories.getSTDESC(), stories.getStory_price(),stories.getLogoUrl(),stories.getStoryNaMe(),stories.getStory_content(),"AppCreationStory");
                    }
                });
            }


        };
        // TODO:Setting OnClickListeners
        // submit Following
        followees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  DatabaseReference  mFollowings=  FirebaseDatabase.getInstance().getReference().child("Cur_User_Followings").child(un.getText().toString().trim()+"_"+auth1.getDisplayName().trim());
                    mFollowings.child("CurUserFollowingName").setValue(un.getText().toString());
                    mFollowings.child("CurrentUserName").setValue(auth1.getDisplayName());
//                    FirebaseDatabase.getInstance().getReference().child("Cur_User_Followees").push().child("CurrentUserName").setValue(auth1.getDisplayName());

                    Toast.makeText(getContext(),"that is you",Toast.LENGTH_SHORT).show();

            }
        });

        voteTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    final DatabaseReference  mFollowings=  FirebaseDatabase.getInstance().getReference().child("Cur_User_Followings");
                    mFollowings.child(un.getText().toString()+auth1.getDisplayName()).orderByChild("CurUserFollowingName").equalTo(un.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child("CurrentUserName").getValue() == auth1.getDisplayName())
                                        {
                                            mFollowings.child("voteTo").setValue(1);
                                        }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    mFollowings.child("CurrentUserName").setValue(auth1.getDisplayName());
//                    FirebaseDatabase.getInstance().getReference().child("Cur_User_Followees").push().child("CurrentUserName").setValue(auth1.getDisplayName());


            }
        });
        voteAgainst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    final DatabaseReference  mFollowings=  FirebaseDatabase.getInstance().getReference().child("Cur_User_Followings");
                    mFollowings.child(un.getText().toString()+auth1.getDisplayName()).orderByChild("CurUserFollowingName").equalTo(un.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child("CurrentUserName").getValue() == auth1.getDisplayName())
                            {
                                mFollowings.child("voteAgainst").setValue(1);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    mFollowings.child("CurrentUserName").setValue(auth1.getDisplayName());
//                    FirebaseDatabase.getInstance().getReference().child("Cur_User_Followees").push().child("CurrentUserName").setValue(auth1.getDisplayName());


            }
        });
        voteHonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    final DatabaseReference  mFollowings=  FirebaseDatabase.getInstance().getReference().child("Cur_User_Followings");
                    mFollowings.child(un.getText().toString()+auth1.getDisplayName()).orderByChild("CurUserFollowingName").equalTo(un.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child("CurrentUserName").getValue() == auth1.getDisplayName())
                            {
                                if(!dataSnapshot.hasChild("voteHonor")){
                                    mFollowings.child("voteHonor").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            FirebaseDatabase.getInstance().getReference().child("Cur_User_Followings").child("").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    mFollowings.child("CurrentUserName").setValue(auth1.getDisplayName());
//                    FirebaseDatabase.getInstance().getReference().child("Cur_User_Followees").push().child("CurrentUserName").setValue(auth1.getDisplayName());


            }
        });
        hoststories.setAdapter(trfbra);
        return othersprofiles;
    }
    DatabaseReference commentsdb = mydatabase.getReference().child("comments");

    public void showDetailsDialog(final String Author1, final String Desc, String price1, final String ImgUrl, final String storyNAME, final String storyCoNtEnT, final String StrySrc){
        StoryDetailsl =new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View detaildialog = inflater.inflate(R.layout.detailalertdialog,null);
        StoryDetailsl.setView(detaildialog);
        //TODO: Identify IDS
        final RatingBar ratebar =(RatingBar)detaildialog.findViewById(R.id.stratebar);
        final ScrollView firstscroll = (ScrollView)detaildialog.findViewById(R.id.highlightscroll);
        ImageView cover = (ImageView) detaildialog.findViewById(R.id.story__img);
        TextView Descr = (TextView) detaildialog.findViewById(R.id.description);
        final Button price = (Button) detaildialog.findViewById(R.id.storypricetxt);
        TextView Authors = (TextView) detaildialog.findViewById(R.id.Author);
        TextView storyNAme = (TextView) detaildialog.findViewById(R.id.stname);
        Picasso.with(getContext()).load(ImgUrl).into(cover);
        final TextView comtxt = (TextView) detaildialog.findViewById(R.id.commentstxt1);
        final ListView comlist =(ListView)detaildialog.findViewById(R.id.commentslist);
        final EditText comentry =(EditText)detaildialog.findViewById(R.id.usernewcomment);
        final ImageButton commenterimg =(ImageButton)detaildialog.findViewById(R.id.comerimg);
        final ImageButton exitcoms = (ImageButton) detaildialog.findViewById(R.id.cmtexitbtn);
        ImageButton exitdialogbtn =(ImageButton)detaildialog.findViewById(R.id.closedialog);
        final Button sendcoms = (Button) detaildialog.findViewById(R.id.sendcombtn);
        final LinearLayout comlayout =(LinearLayout)detaildialog.findViewById(R.id.commentsview);
        final Button comments1 = (Button) detaildialog.findViewById(R.id.commentsbtn);

        final TextView comtername =(TextView)detaildialog.findViewById(R.id.commentername);
        final TextView comttxt =(TextView)detaildialog.findViewById(R.id.commentstxt1);
        ImageView comimg =(ImageView)detaildialog.findViewById(R.id.comterimg);
        final TextView comname = (TextView)detaildialog.findViewById(R.id.commentername);
        //        LinearLayout totalcom =(LinearLayout)detaildialog.findViewById(id.totalcomments);
        Query newquery =FirebaseDatabase.getInstance().getReference().child("UserDetail").child(dispname != null ? dispname:"").orderByChild("UserImg");
        Query commentsquery = commentsdb.child(storyNAME).orderByChild("currentstoryname").equalTo(storyNAME) ;
        FirebaseListOptions<comments> options = new FirebaseListOptions.Builder<comments>()
                .setQuery(commentsquery, comments.class)
//                .setLayout(R.layout.comments)
//                .setLifecycleOwner(this)
                .build();
        sendcoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curusercomment = comentry.getText().toString().trim();
                DatabaseReference commentschild = commentsdb.child(storyNAME);
                DatabaseReference commentschildrows = commentschild.push();
                commentschildrows.child("username1").setValue(auth1.getDisplayName());
                commentschildrows.child("usercomment").setValue(curusercomment);
                commentschildrows.child("user___Email").setValue(auth1.getEmail());
                commentschildrows.child("currentstoryname").setValue(storyNAME);
            }

        });
        trfbla= new FirebaseListAdapter<comments>(options) {
            @Override
            protected void populateView(View v, final comments model, int position) {
                Query userimgincmt = mydatabase.getReference().child("UserDetail").orderByChild("Email").equalTo(model.getUser___Email());
                comtername.setText(model.getUserName1());
                comttxt.setText(model.getUsercomment());
                final String usermail = model.getUser___Email();
                final User st = new User();

                comtername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle buncom =new Bundle();

                        buncom.putString("DisplayName",comtername.getText().toString());
                        buncom.putString("UserMail",usermail);
                    }
                });
                userimgincmt.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String commterimg = String.valueOf(dataSnapshot.child("UserImg").getValue(User.class).getUserImg());

                        ((ImageView)detaildialog.findViewById(R.id.comterimg)).setImageURI(Uri.parse(commterimg));
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
        };

        comlist.setAdapter(trfbla);

        Query Ownedstoriescheck = osdb.orderByChild("Author").equalTo(auth1.getDisplayName());
//        Ownedstoriescheck.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                String values = String.valueOf(dataSnapshot.getValue());
//                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                    price.setEnabled(false);
//                    price.setText("it's yours");
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
        Query purchasedstoriescheck = psdb.orderByChild("purchasername").equalTo(auth1.getDisplayName());
//        purchasedstoriescheck.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                String values = String.valueOf(dataSnapshot.getValue());
//                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                    price.setEnabled(false);
//                    price.setText("purchased");
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        newquery.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String img = String.valueOf(dataSnapshot.getValue());
                if(img == null)
                {commenterimg.setBackground(null);}
                else if(img != null){
                    Picasso.with(getContext()).load(img).fit().into(commenterimg);

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
        // TODO: OnClick Listeners
        exitcoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comlayout.setVisibility(View.INVISIBLE);

                comments1.setVisibility(View.VISIBLE);
            }
        });
        comments1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comlayout.setVisibility(View.VISIBLE);

                comments1.setVisibility(View.INVISIBLE);
//                exitcoms.setVisibility(View.VISIBLE);
            }
        });
//comlayout.setOnTouchListener(new View.OnTouchListener() {
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
////        firstscroll.stopNestedScroll();
//        return false;
//    }
//});
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogress = new ProgressDialog(getContext());
                mprogress.setMessage("Purchasing...");
                mprogress.show();
                Query purchasername = StsRef.child("story_name").orderByChild("Author").equalTo(auth1.getDisplayName());
                Query purchasername1 = psdb.orderByChild("purchasername").equalTo(auth1.getDisplayName());
                if (Author1 != auth1.getDisplayName());
                {
                    DatabaseReference psdbchild = psdb.push();
                    psdbchild.child("purchasername").setValue(auth1.getDisplayName());

                    psdbchild.child("story_name").setValue(storyNAME);
                    psdbchild.child("Story_Author").setValue(Author1);
                    psdbchild.child("Story_Desc").setValue(Desc);
                    psdbchild.child("Story_Content").setValue(storyCoNtEnT);
                    psdbchild.child("Story_ImgUrl").setValue(ImgUrl);
                    psdbchild.child("StorySrc").setValue(StrySrc);
                    price.setEnabled(false);
                    price.setText("purchased");
                    Toast.makeText(getContext(),"Story have been added successfully to your purchased Stories",Toast.LENGTH_LONG).show();


                }if (Author1 == auth1.getDisplayName()){
                    Toast.makeText(getContext(),"You are the story author",Toast.LENGTH_LONG).show();

                }
//                while (Author1 == auth.getDisplayName() || purchasername != null || purchasername1 != null){   Toast.makeText(getContext(),"You are already the story author",Toast.LENGTH_LONG).show();
//                    break;}

                mprogress.dismiss();

            }
        });
        ratebar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getContext(),"You Rated Story By"+" "+ (int)rating+" "+ "stars",Toast.LENGTH_LONG).show();
//                int total=0;
//                for(int i = 0; i <= total; i++) {
//                if(fromUser == true){
//
//
//                    total += (int) rating;
//if(i>0) {
//    myRef.child(storyNAME).child("stRating").setValue(String.valueOf(total));
//}
//                }
//                }

                DatabaseReference childs =  mystrateRef.child(storyNAME+auth1.getDisplayName());
                childs.child("STRYname").setValue(storyNAME);
                childs.child("styrates").setValue(String.valueOf(rating));
                childs.child("userEmail").setValue(auth1.getEmail());
            }
        });
        storyNAme.setText("Story Name:"+" " + storyNAME);
        Authors.setText("author :"+" " + Author1);
        Descr.setText("story Description"+ " " + Desc);
        price.setText(price1 + " "+"$");
        final AlertDialog alertDialog = StoryDetailsl.create();

        alertDialog.show();
//
        exitdialogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        trfbla.startListening();

    }

    private void paymentDialog(String Author1, String Desc, String price1, String ImgUrl, final String storyNAME){
        StoryDetailsl =new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View detaildialog = inflater.inflate(R.layout.detailalertdialog,null);
        StoryDetailsl.setView(detaildialog);}

    @Override
    public void onStart() {
        super.onStart();
        trfbra.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
    trfbra.stopListening();
    }

    public static class trblogholder extends RecyclerView.ViewHolder{
        View mview;
        TextView Authors,pdfAuthor;
        TextView logourld;
        TextView content,pdfcontent;
        TextView price,pdfprice;
        TextView mimgurl;
        RatingBar Strate,pdfrate;
        TextView stodesc,pdfdesc;
        ImageView cover,pdfcover;
        TextView storyNAme,pdfstname;
        public trblogholder(View itemView) {
            super(itemView);

            mview = itemView;

            mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });}
        //       private onclicklistener mlistener;
//
//       public void setMlistener(onclicklistener mlistener) {
//           this.mlistener = mlistener;
//       }

        public TextView getPdfAuthor() {
            return pdfAuthor;
        }

        public void setPdfAuthor(String pdfauthor) {
            pdfAuthor =(TextView)mview.findViewById(R.id.pdfAuthor);
            pdfAuthor.setText("Author" +": "+ pdfauthor);
        }

        public TextView getPdfcontent() {
            return pdfcontent;
        }

        public void setPdfcontent(TextView pdfcontent) {
//            pdfAuthor =(TextView)mview.findViewById(id.p);
//            pdfAuthor.setText("Author" +": "+ pdfauthor);
        }

        public TextView getPdfprice() {
            return pdfprice;
        }

        public void setPdfprice(String pdfprice1) {
            pdfprice =(TextView)mview.findViewById(R.id.pdfprice);
            pdfprice.setText( pdfprice1);}

        public RatingBar getPdfrate() {
            return pdfrate;
        }

        public void setPdfrate(RatingBar pdfrate) {
            this.pdfrate = pdfrate;
        }

        public TextView getPdfdesc() {
            return pdfdesc;
        }

        public void setPdfdesc(String pdfdesc1) {
            pdfdesc =(TextView)mview.findViewById(R.id.pdfType);
            pdfdesc.setText("Type" +": "+ pdfdesc1);
        }

        public ImageView getPdfcover() {
            return pdfcover;
        }

        public void setPdfcover(final Context ctxs, final String pdfcover1) {
            pdfcover =(ImageView) mview.findViewById(R.id.pdfstory__img);
            Picasso.with(ctxs).load(pdfcover1).networkPolicy(NetworkPolicy.OFFLINE).into(pdfcover, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctxs).load(pdfcover1).into(pdfcover);
                }
            });

        }

        public TextView getPdfstname() {
            return pdfstname;
        }

        public void setPdfstname(String pdfstname1) {
            pdfstname =(TextView)mview.findViewById(R.id.pdfstname);
            pdfstname.setText("StoryName" +": "+ pdfstname1);
        }

        //       public interface onclicklistener{
//    void onItemClick(int position);
//}

        //mview.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        if(mlistener!=null)
//        {
//int postion = getAdapterPosition();
//if(postion !=RecyclerView.NO_POSITION)
//{
//mlistener.onItemClick(postion);
//}
//        }
//    }
//});
//       Authors = (TextView)itemView.findViewById(R.id.id);


        public void setMimgurl(final Context ctx, final String mimgurl) {
            cover= (ImageView) mview.findViewById(R.id.trstory__img);
            Picasso.with(ctx).load(mimgurl).networkPolicy(NetworkPolicy.OFFLINE).into(cover, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(mimgurl).into(cover);
                }
            });
        }


        public void setContent(String mcontent) {
//           content = (TextView)mview.findViewById(R.id.description);
//           content.setText("Story Description" + mcontent);
        }

        public void setPrice(String sprice) {
            price= (TextView)mview.findViewById(R.id.storypricetxt);
            price.setText("Price :" + " " + sprice);
        }


        public void setAuthor(String author) {
            Authors = (TextView)mview.findViewById(R.id.trAuthor);
            Authors.setText("Author :" + " " + author);
        }

        public void setStoryNAme(String storyNAMe) {
            storyNAme = (TextView)mview.findViewById(R.id.trstname);
            storyNAme.setText("Story Name:"+" " + storyNAMe);

        }


        public void setStodesc(String stodesc1) {
            stodesc =(TextView)mview.findViewById(R.id.description);
            stodesc.setText("Story Description" + stodesc1);
        }

        public void setStrate(String strate1) {
            Strate =(RatingBar)mview.findViewById(R.id.stratebar);
            Strate.setRating(Float.parseFloat(strate1));
        }
    }

}
