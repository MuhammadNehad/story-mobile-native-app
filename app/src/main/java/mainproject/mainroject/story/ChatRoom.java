package mainproject.mainroject.story;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mainproject.mainroject.story.Tables.chatdatabaseinserver;


public class ChatRoom extends Fragment {
    private DatabaseReference chatdatabasechild;
    private DatabaseReference privatechattargets;
    private Class<chatdatabaseinserver> fadas;
    private Service storyservice;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public ChatRoom() {
        // Required empty public constructor
    }
    FirebaseRecyclerAdapter<chatdatabaseinserver,blogholders> chatlist ;
    DatabaseReference chatdatabase = FirebaseDatabase.getInstance().getReference().child("chatMsgs");
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    RecyclerView chalist;
    MyAdapter customrecyclerviewAdapter;
    List<chatdatabaseinserver> getchatdata = new ArrayList<chatdatabaseinserver>();


    ImageButton sendthemessage;
    EditText writethemessage;
    TextView privtemessagenum;
    TextView messageviews;
    TextView sendername;
    TextView checkpvmsgs;
    FloatingActionButton fab;
    Animation fabopent,fabclose,rotate,rerotate;
    static ArrayList<String> arraynames = new ArrayList<String>();
    ArrayList<String> arrayprivatemsgs = new ArrayList<String>();
    SwipeRefreshLayout mrefreshlayout;
    public static int limit;
    private int pages;
    int total;
    public static final int total_items_to_Load =7;
    private static int mCurrentpage= 1;
    private int itempos = 0;
    private String mLastKey="";
    private String mPreviousKey="";

    Query chatquery;
    public Query getChatquery() {
        return chatquery;
    }

    public void setChatquery(Query chatquery) {
        this.chatquery = chatquery;
    }



    public void setArraynames(ArrayList<String> arraynames) {
        this.arraynames = arraynames;
    }

    public ArrayList<String> getArraynames() {
        return arraynames;
    }
    Boolean isScroll =false;
    FirebaseRecyclerOptions<chatdatabaseinserver> chatoption;
    LinearLayoutManager chatlayout;
    int i = 0;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    interface IEquality<T>{}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View chatroom = inflater.inflate(R.layout.fragment_chat_room, container, false);

        // TODO:IDS

        checkpvmsgs=chatroom.findViewById(R.id.notificationsnum);
        mrefreshlayout =chatroom.findViewById(R.id.refreshrecycler);
        privtemessagenum =chatroom.findViewById(R.id.notificationsnum);
        chalist= chatroom.findViewById(R.id.listofchat);
        chalist.setHasFixedSize(true);
        chatlayout= new LinearLayoutManager(getContext());
        chatlayout.setStackFromEnd(true);
        chalist.setLayoutManager(chatlayout);
        sendthemessage = chatroom.findViewById(R.id.sendmesg);
        writethemessage = chatroom.findViewById(R.id.messageedit);
        messageviews =chatroom.findViewById(R.id.msgstxt);
        sendername = chatroom.findViewById(R.id.msgownertext);
        fab = chatroom.findViewById(R.id.notifications);
        fabopent = AnimationUtils.loadAnimation(getContext(), R.anim.translation);
        fabclose = AnimationUtils.loadAnimation(getContext(), R.anim.returnvalueoftranslation);
        rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        rerotate = AnimationUtils.loadAnimation(getContext(), R.anim.antirotate);
        LayoutInflater vi1= getLayoutInflater();
        chalist.setItemAnimator(new DefaultItemAnimator());
        chalist.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

        customrecyclerviewAdapter = new MyAdapter(chalist,ChatRoom.this,getchatdata);

//        mrefreshlayout.setEnabled(false);
        chalist.setAdapter(customrecyclerviewAdapter);
        usechildlistner();
        refreshing();

//        chalist.addOnScrollListener(new EndlessRecyclerOnScrollListener(chatlayout) {
//    @Override
//    public void onLoadMore(int current_page) {
//        mCurrentpage = current_page;
//        usechildlistner();
//    }
//});
//        mrefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mCurrentpage++;
//                itempos=0;
//
//                loadMoreData();
//
//            }
//        });
        //        ChatRoom cr = new ChatRoom()

//        Runnable r =new Runnable() {
//            @Override
//            public void run() {
//                long time = System.currentTimeMillis() + 10000;
//                while (System.currentTimeMillis() < time) {
//                  synchronized (this) {
//                      try {
//                          wait(time - System.currentTimeMillis());
//                      } catch (InterruptedException e) {
//                          e.printStackTrace();
//                      }
//                  }
//                }
//            }
//        };
//        Thread chatload = new Thread(r);
//        chatload.start();
//        chatlist.notifyDataSetChanged();


        //        chatload.start();
//        chatload.isAlive();
//        Thread.sleep(32131);
//        chatload.join();

        //.limitToLast(pages*limit+total)
//        chalist.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                isScroll=true;
//            }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
////                total+=pages*limit;
////
////               pages= chatlayout.getChildCount();
////               total= chatlayout.getItemCount();
////               limit= chatlayout.findFirstVisibleItemPosition();
////               if((isScroll) && (pages+limit == total))
////               {
////                    isScroll=false;
//////                   chatitemdesign();
////                   mCurrentpage++;
////
//                   chats();
//        chalist.setAdapter(chatlist);
//         chatlist.notifyDataSetChanged();
//////chatlayout.removeAndRecycleAllViews(chalist.Recycler());
////               }
//
//
//            }
//        });
//        chalist.setSelection(chalist.getAdapter().getCount());

        sendthemessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(writethemessage.getText().toString()))
                {
                if(arraynames.size() == 0) {
                    chatdatabasechild = chatdatabase.push();
                    chatdatabasechild.child("messageowner").setValue(firebaseUser.getDisplayName());
                    chatdatabasechild.child("message_content").setValue(writethemessage.getText().toString().trim());
                    chatdatabasechild.child("respectful").setValue(0);
                    chatdatabasechild.child("Likes").setValue(0);
                    chatdatabasechild.child("disLikes").setValue(0);
                    chatdatabasechild.child("rudness").setValue(0);
                    chatdatabasechild.child("reports").setValue(0);

                }


                    if (arraynames.size()>0){
//                                privatechattargets = FirebaseDatabase.getInstance().getReference().child("PrivateMessages").push();
//                                privatechattargets.child("Message").setValue(writethemessage.getText().toString());
                        chatdatabasechild = chatdatabase.push();
                        chatdatabasechild.child("messageowner").setValue(firebaseUser.getDisplayName());
                        chatdatabasechild.child("message_content").setValue(writethemessage.getText().toString());
                        chatdatabasechild.child("Recievers").setValue(arraynames);
                        chatdatabasechild.child("respectful").setValue(0);
                        chatdatabasechild.child("Likes").setValue(0);
                        chatdatabasechild.child("disLikes").setValue(0);
                        chatdatabasechild.child("rudness").setValue(0);
                        chatdatabasechild.child("reports").setValue(0);

                        int counter = 0;

                        for(int i = 0; i<=arraynames.size();i++) {

//                                    if(holder.sendernamea.getText().toString().equals(firebaseUser.getDisplayName()))
//                                    { i++;}
//                                    counter= counter++;
                            final String recieverssnames =arraynames.get(i).toString();
//                                    if(!holder.sendernamea.getText().toString().equals(firebaseUser.getDisplayName())) {

                            FirebaseDatabase.getInstance().getReference().child("UserDetail").child(recieverssnames).child("Totalpvmsgs").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    int i2 = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                                    int AAA=i2+1;
                                    FirebaseDatabase.getInstance().getReference().child("UserDetail").child(recieverssnames).child("Totalpvmsgs").setValue(String.valueOf(AAA));
                                    privtemessagenum.setText(String.valueOf(dataSnapshot.getValue()));

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            break;

                        }
                    }

                }
                else if(TextUtils.isEmpty(writethemessage.getText().toString())){

                    writethemessage.setHint("You need to write the message");
                    writethemessage.setHintTextColor(Color.RED);

                }
            }
        });

        privtemessagenum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("UserDetail"). child(firebaseUser.getDisplayName()).child("Totalpvmsgs").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int i2 = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));

                        FirebaseDatabase.getInstance().getReference().child("UserDetail").child(firebaseUser.getDisplayName()).child("Totalpvmsgs").setValue(String.valueOf(i2-i2));
                        privtemessagenum.setText(String.valueOf(dataSnapshot.getValue()));
                        DatabaseReference sendername = chatdatabase;
                        Query msg = chatdatabase.orderByChild("Recievers");
                        msg.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                List<ArrayList<String>> recievers = new ArrayList<>();
                                ArrayList<String> recieversnames = dataSnapshot.getValue(chatdatabaseinserver.class).getRecievers();
                                chatdatabaseinserver ctdbis = dataSnapshot.getValue(chatdatabaseinserver.class);
                                recievers.add(recieversnames);
                                String currentreciever =new String();
                                String currentcontet  = new String();
                                for(i=0; i<=recievers.size(); i++) {
                                  if (recievers.get(i).contains(firebaseUser.getDisplayName())) {
                                       currentreciever = ctdbis.getMessageowner();

                                      currentcontet = ctdbis.getMessage_content();

                                  }

                              }
                                privatemsgscheck(currentcontet, currentreciever);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                         }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
//        usechildlistner();

//    final RelativeLayout.LayoutParams forlist = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                 forlist.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);




//        chalist.getMaxScrollAmount();
//        chalist.smoothScrollToPosition(chatlist.getCount());

//

        return chatroom;
    }
private void refreshing(){
    mrefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mCurrentpage++;
            itempos=0;

            loadMoreData();
        }
    });
}
    private void setItempos(int Itempos,String PreviousKey,String LastKey){
        this.itempos=Itempos;
        this.mPreviousKey =PreviousKey;
        this.mLastKey=LastKey;

    }
    private void loadMoreData(){
        chatdatabase.orderByKey().endAt(mLastKey).limitToLast(7).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                chatdatabaseinserver cdbis = dataSnapshot.getValue(chatdatabaseinserver.class);
                String mLastKeyalt = dataSnapshot.getKey();


                if (!mPreviousKey.equals(mLastKeyalt)){
                    getchatdata.add(itempos++,cdbis);

//                    /**/                    chalist.setAdapter(customrecyclerviewAdapter);
                }else{
                    mPreviousKey =mLastKeyalt;
                }

                if(itempos == 1){

                    mLastKey = mLastKeyalt;
                }

                Log.d("TotalKeys","LastKey: "+mLastKey+" previous key:"+mPreviousKey+" messagekey"+ mLastKeyalt);


                customrecyclerviewAdapter.notifyDataSetChanged();
//                customrecyclerviewAdapter = new MyAdapter(chalist,ChatRoom.this,getchatdata);
                mrefreshlayout.setRefreshing(false);
                chatlayout.scrollToPositionWithOffset(7,0);

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

        onStart();
        //        mCurrentpage++;
//        usechildlistner();
    }

    public void usechildlistner(){

        chatdatabase.limitToLast(mCurrentpage*total_items_to_Load ).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                chatdatabaseinserver cdbis = dataSnapshot.getValue(chatdatabaseinserver.class);

                itempos++;
                if(itempos == 1){
                    String mLastKeyalt = dataSnapshot.getKey();

//                    setItempos(itempos,mLastKeyalt,mLastKeyalt);
                    mLastKey = mLastKeyalt;
                    mPreviousKey = mLastKeyalt;

                }
                getchatdata.add(cdbis);

//                itempos++;
//                chalist.scrollToPosition(getchatdata.size()-1);

                customrecyclerviewAdapter.notifyDataSetChanged();
                chalist.scrollToPosition(getchatdata.size()-1);
                mrefreshlayout.setRefreshing(false);
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
    onStart();
    }
    private void chatthreads(){

    }
    public void chats(){

        chatquery = chatdatabase;
        chatquery.keepSynced(true);
//        limitToLast(pages*limit+total)

        chatoption = new FirebaseRecyclerOptions.Builder<chatdatabaseinserver>()
            .setQuery(getChatquery(),chatdatabaseinserver.class)
            .build();

        final Context finalCotx = null;
        chatlist = new FirebaseRecyclerAdapter<chatdatabaseinserver, blogholders>(chatoption) {
//        //                     Activity activity;
        @Override
        public blogholders onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatcontentview, parent, false);


            return new blogholders(view);
        }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            String chatername;

        @Override
        protected void onBindViewHolder(@NonNull final blogholders holder, final int position, @NonNull chatdatabaseinserver model) {
//            model =datas.get(position);

            holder.setSendermsg(model.getMessage_content());
            holder.setSendernamea(model.getMessageowner());
//                         sendername.setText(model.getMessageowner());
//                setSendernamea();
            LinearLayout chatcontent = (LinearLayout)holder.vmg.findViewById(R.id.msgsview);
            LinearLayout chatcontentchild = (LinearLayout)holder.vmg.findViewById(R.id.chatcontentform);
            String senname = holder.getSendernamea().getText().toString();
            this.chatername = senname;
            //            position++;


            Query userimgincmt = FirebaseDatabase.getInstance().getReference().child("UserDetail").child(holder.getSendernamea().getText().toString());

            userimgincmt.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String ds = String.valueOf(dataSnapshot.child("UserImg").getValue());
//                                                          for(DataSnapshot dss:dataSnapshot.getChildren()) {
//                                                          User us = new User();
//                                                          us.setUserImg(dss.getValue(us.getClass()).getUserImg());
////                                                        String img = String.valueOf(dataSnapshot.getValue(User.class));
////                                                        if(img == null)
////                                                        {((ImageView)v.findViewById(id.comterimg)).setBackground(null);}
////                                                        else if(img != null) {
                    holder.setOwnerimgas(getContext(),ds);
////                                                        }
////                                                        .setImageURI(Uri.parse(commterimg));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

//                if(model.getMessageowner().equals(firebaseUser.getDisplayName()))
//                {
//                    chatcontent.setGravity(View.FOCUS_RIGHT);
//                    chatcontent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.END));
//                    chatcontent.animate().rotationYBy(180).translationX(ViewGroup.LayoutParams.MATCH_PARENT);
//
////  holder.vmg.animate().rotationX(180);
//                }
//chatlist.getItem(position).getMessageowner().equals(firebaseUser.getDisplayName());

            holder.vmg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    chatlayout.findViewByPosition(position);
                    chatlist.getItem(position);
                    chatlayout.onScrollStateChanged(position);
                    chatlayout.onItemsChanged(chalist);
                    if(!arraynames.contains(chatername)) {
                        arraynames.add(chatername);

                            holder.vmg.findViewById(R.id.chatcontentform).setBackgroundColor(Color.MAGENTA);
                    }
                    else if(arraynames.contains(chatername)){

                        Toast.makeText(getContext(),"You Already Taged This Person",Toast.LENGTH_LONG ).show();
                        arraynames.remove(chatername);
                        holder.vmg.findViewById(R.id.chatcontentform).setBackgroundColor(Color.CYAN);
                    }

                    return true;
                }

            });

            holder.vmg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msgsrating(holder.sendermsg.getText().toString(),holder.sendernamea.getText().toString());
                }
            });
        }



    };
chalist.setAdapter(chatlist);
        chatlayout.getStackFromEnd();

        chatlist.notifyDataSetChanged();}
    private void chatitemdesign(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

        for(int i = 0; i<5; i++){
            setChatquery(chatdatabase.limitToLast((int) Math.floor(Math.random()*5)));

            chatlist.notifyDataSetChanged();
                }
            }
        }, 2000);

    }
    AlertDialog.Builder chatsdegree;
    AlertDialog.Builder privatemsgcheckingandreply;

    Query chataddrank=chatdatabase;
    private void privatemsgscheck(final String msgcontent, final String msgsender) {

        chatsdegree = new AlertDialog.Builder(getContext());
        final View chatdegreesview = getLayoutInflater().inflate(R.layout.totalpvchatsrecyclervlist, null);
        AlertDialog activatethtedialog = chatsdegree.create();
        chatsdegree.setView(chatdegreesview);

        final TextView pvsendername = chatdegreesview.findViewById(R.id.pvsendersname);

        TextView pvmsgcon = chatdegreesview.findViewById(R.id.pvmsgcont);
        ImageView pvimg = chatdegreesview.findViewById(R.id.pvsenderimg);
        RecyclerView totalpvmsgs = chatdegreesview.findViewById(R.id.totalpvmsgs);
        LinearLayoutManager recview= new LinearLayoutManager(getContext());
        recview.getStackFromEnd();
        totalpvmsgs.setLayoutManager(recview);
        Query asda= chatdatabase.orderByChild("Recievers");
        FirebaseRecyclerOptions<chatdatabaseinserver>pvmsgs = new FirebaseRecyclerOptions.Builder<chatdatabaseinserver>()
                        .setQuery(asda,chatdatabaseinserver.class)
                        .build();
        FirebaseRecyclerAdapter<chatdatabaseinserver,blogholders> sdsad =new FirebaseRecyclerAdapter<chatdatabaseinserver, blogholders>(pvmsgs) {
            @Override
            protected void onBindViewHolder(@NonNull blogholders holder, int position, @NonNull chatdatabaseinserver model) {
                    holder.setPvsendername(msgsender);
                    holder.setPvsendermsg(msgcontent);

                Query userimgincmt = FirebaseDatabase.getInstance().getReference().child("UserDetail").child(pvsendername.getText().toString());

                userimgincmt.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String ds = String.valueOf(dataSnapshot.child("UserImg").getValue());
                        //                                                        for(DataSnapshot dss:dataSnapshot.getChildren()) {
//                                                            User us = new User();
//                                                            us.setUserImg(dss.getValue(us.getClass()).getUserImg());
////                                                        String img = String.valueOf(dataSnapshot.getValue(User.class));
////                                                        if(img == null)
////                                                        {((ImageView)v.findViewById(id.comterimg)).setBackground(null);}
////                                                        else if(img != null) {
                        Picasso.with(getContext()).load(ds).fit().into(((ImageView) chatdegreesview.findViewById(R.id.comterimg)));
////                                                        }
////                                                        .setImageURI(Uri.parse(commterimg));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public blogholders onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.totalprivatechats, parent, false);


                return new blogholders(view);
            }
        };
        activatethtedialog.show();
    }
    private void msgsrating(final String msgcontent, String msgsender){

        chatsdegree =new AlertDialog.Builder(getContext());
        View chatdegreesview =getLayoutInflater().inflate(R.layout.chatsranks,null);
        chatsdegree.setView(chatdegreesview);
        AlertDialog activatethtedialog = chatsdegree.create();
        Button respectbutton =chatdegreesview.findViewById(R.id.respect);
        Button Likemsgbutton =chatdegreesview.findViewById(R.id.Likemsg);
        Button dislikemsgbutton =chatdegreesview.findViewById(R.id.disLikemsg);
        Button rudemsgbutton =chatdegreesview.findViewById(R.id.rudemsg);
        Button reportmsgbutton =chatdegreesview.findViewById(R.id.reportmsg);

        final DatabaseReference respectedchats = FirebaseDatabase.getInstance().getReference().child("respectedMsgs");
        final DatabaseReference Likedchats = FirebaseDatabase.getInstance().getReference().child("LikedMsgs");
        final DatabaseReference dislikedchats = FirebaseDatabase.getInstance().getReference().child("disLikedMsgs");
        final DatabaseReference rudechats = FirebaseDatabase.getInstance().getReference().child("rudeMsgs");
        final DatabaseReference reportedchats = FirebaseDatabase.getInstance().getReference().child("reportedMsgs");
//        respectful Likes disLikes rudness reports
        respectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
respectedchats.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.child(msgcontent).hasChild(firebaseUser.getDisplayName())){
            respectedchats.child(msgcontent).child(firebaseUser.getDisplayName()).removeValue();
            chataddrank.orderByChild("message_content").equalTo(msgcontent).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("respectful").getValue()));
                    chatdatabase.child(chatdatabase.getKey()).child("respectful").setValue(i-1);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if(!dataSnapshot.child(msgcontent).hasChild(firebaseUser.getDisplayName())){
            respectedchats.child(msgcontent).child(firebaseUser.getDisplayName()).setValue("true");
            chataddrank.orderByChild("message_content").equalTo(msgcontent).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("respectful").getValue()));
                    chatdatabase.child("message_content").equalTo(msgcontent).getRef().child("respectful").setValue(i+1);
                    int ii = Integer.parseInt(String.valueOf(dataSnapshot.child("reports").getValue()));
                    int iii = Integer.parseInt(String.valueOf(dataSnapshot.child("Likes").getValue()));
                    int v = Integer.parseInt(String.valueOf(dataSnapshot.child("disLikes").getValue()));
                    int iv = Integer.parseInt(String.valueOf(dataSnapshot.child("rudness").getValue()));

                    if(i>0) {
                        chatdatabase.child(chatdatabase.getKey()).child("Likes").setValue(iii - 1);
                        chatdatabase.child(chatdatabase.getKey()).child("disLikes").setValue(v - 1);
                        chatdatabase.child(chatdatabase.getKey()).child("reports").setValue(ii - 1);
                        chatdatabase.child(chatdatabase.getKey()).child("rudness ").setValue(iv - 1);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
            }
        });
//        Likes disLikes rudness reports
        Likemsgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Likedchats.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(msgcontent).hasChild(firebaseUser.getDisplayName())){
                            respectedchats.child(msgcontent).child(firebaseUser.getDisplayName()).removeValue();
                            chataddrank.orderByChild("message_content").equalTo(msgcontent).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("Likes").getValue()));
                            if(i>0) {
                                chatdatabase.child(chatdatabase.getKey()).child("Likes").setValue(i - 1);
                            }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if(!dataSnapshot.child(msgcontent).hasChild(firebaseUser.getDisplayName())){
                            Likedchats.child(msgcontent).child(firebaseUser.getDisplayName()).setValue("true");
                            chataddrank.orderByChild("message_content").equalTo(msgcontent).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("Likes").getValue()));
                                   chatdatabase.child("message_content").equalTo(msgcontent).getRef().child("Likes").setValue(i + 1);
                                    int ii = Integer.parseInt(String.valueOf(dataSnapshot.child("respectful ").getValue()));
                                    int iii = Integer.parseInt(String.valueOf(dataSnapshot.child("reports").getValue()));
                                    int v = Integer.parseInt(String.valueOf(dataSnapshot.child("disLikes").getValue()));
                                    int iv = Integer.parseInt(String.valueOf(dataSnapshot.child("rudness").getValue()));

                                    if(i>0) {;
                                        chatdatabase.child(chatdatabase.getKey()).child("disLikes").setValue(v - 1);
                                        chatdatabase.child(chatdatabase.getKey()).child("respectful").setValue(ii - 1);
                                        chatdatabase.child(chatdatabase.getKey()).child("rudness ").setValue(iv - 1);
                                        chatdatabase.child(chatdatabase.getKey()).child("reports ").setValue(iii - 1);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
//        disLikes rudness reports
        dislikemsgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dislikedchats.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(msgcontent).hasChild(firebaseUser.getDisplayName())){
                            dislikedchats.child(msgcontent).child(firebaseUser.getDisplayName()).removeValue();
                            chataddrank.orderByChild("message_content").equalTo(msgcontent).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("disLikes").getValue()));
        if (i>0) {
        chatdatabase.child(chatdatabase.getKey()).child("disLikes").setValue(i - 1);
        }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if(!dataSnapshot.child(msgcontent).hasChild(firebaseUser.getDisplayName())){
                            dislikedchats.child(msgcontent).child(firebaseUser.getDisplayName()).setValue("true");
                            chataddrank.orderByChild("message_content").equalTo(msgcontent).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("disLikes").getValue()));
                                    chatdatabase.child("message_content").equalTo(msgcontent).getRef().child("disLikes").setValue(i+1);
                                    int ii = Integer.parseInt(String.valueOf(dataSnapshot.child("respectful ").getValue()));
                                    int iii = Integer.parseInt(String.valueOf(dataSnapshot.child("Likes").getValue()));
                                    int v = Integer.parseInt(String.valueOf(dataSnapshot.child("reports").getValue()));
                                    int iv = Integer.parseInt(String.valueOf(dataSnapshot.child("rudness").getValue()));

                                    if(i>0) {
                                        chatdatabase.child(chatdatabase.getKey()).child("Likes").setValue(iii - 1);
                                        chatdatabase.child(chatdatabase.getKey()).child("respectful").setValue(ii - 1);
                                        chatdatabase.child(chatdatabase.getKey()).child("rudness ").setValue(iv - 1);
                                        chatdatabase.child(chatdatabase.getKey()).child("reports ").setValue(v - 1);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
//        rudness reports
        rudemsgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rudechats.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(msgcontent).hasChild(firebaseUser.getDisplayName())){
                            rudechats.child(msgcontent).child(firebaseUser.getDisplayName()).removeValue();
                            chataddrank.orderByChild("message_content").equalTo(msgcontent).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("rudness").getValue()));
                                 if(i>0) {
                                     chatdatabase.child(chatdatabase.getKey()).child("rudness").setValue(i - 1);
                                 }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if(!dataSnapshot.child(msgcontent).hasChild(firebaseUser.getDisplayName())){
                            rudechats.child(msgcontent).child(firebaseUser.getDisplayName()).setValue("true");
                            chataddrank.orderByChild("message_content").equalTo(msgcontent).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("rudness").getValue()));
                                    chatdatabase.child("message_content").equalTo(msgcontent).getRef().child("rudness").setValue(i+1);
                                    int ii = Integer.parseInt(String.valueOf(dataSnapshot.child("respectful ").getValue()));
                                    int iii = Integer.parseInt(String.valueOf(dataSnapshot.child("Likes").getValue()));
                                    int v = Integer.parseInt(String.valueOf(dataSnapshot.child("disLikes").getValue()));

                                    if(i>0) {
                                        chatdatabase.child(chatdatabase.getKey()).child("Likes").setValue(iii - 1);
                                        chatdatabase.child(chatdatabase.getKey()).child("disLikes").setValue(v - 1);
                                        chatdatabase.child(chatdatabase.getKey()).child("respectful").setValue(ii - 1);

                                    }

//        Likes disLikes rudness reports
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        reportmsgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportedchats.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(msgcontent).hasChild(firebaseUser.getDisplayName())){
                            reportedchats.child(msgcontent).child(firebaseUser.getDisplayName()).removeValue();
                            chataddrank.orderByChild("message_content").equalTo(msgcontent).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("reports").getValue()));
                                    if(i>0) {
                                        chatdatabase.child(chatdatabase.getKey()).child("reports").setValue(i - 1);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if(!dataSnapshot.child(msgcontent).hasChild(firebaseUser.getDisplayName())){
                            reportedchats.child(msgcontent).child(firebaseUser.getDisplayName()).setValue("true");
                            chataddrank.orderByChild("message_content").equalTo(msgcontent).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("reports").getValue()));
                                    chatdatabase.child("message_content").equalTo(msgcontent).getRef().child("reports").setValue(i+1);
                                    int ii = Integer.parseInt(String.valueOf(dataSnapshot.child("respectful ").getValue()));
                                    int iii = Integer.parseInt(String.valueOf(dataSnapshot.child("Likes").getValue()));
                                    int v = Integer.parseInt(String.valueOf(dataSnapshot.child("disLikes").getValue()));

                                    if(i>0) {
                                          chatdatabase.child(chatdatabase.getKey()).child("Likes").setValue(iii - 1);
                                            chatdatabase.child(chatdatabase.getKey()).child("disLikes").setValue(v - 1);
                                        chatdatabase.child(chatdatabase.getKey()).child("respectful").setValue(ii - 1);

    }
                                    //        respectful Likes disLikes rudness reports
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        activatethtedialog.show();

    }
    @Override
    public void onStart() {
        super.onStart();
//        chatlist.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
//    chatlist.stopListening();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,
                                    MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.mainmenu, menu);
        MenuItem item = menu.findItem(R.id.SearchIcon);
        item.setVisible(false);
    }
   public static class blogholders extends RecyclerView.ViewHolder {
        Context cxs;
        TextView pvsendermsg;
        TextView pvsendername;
        TextView sendermsg;
        TextView sendernamea;
        ImageView ownerimgas;
        ImageView pvownerimgas;
        LinearLayout dasda;
        View vmg;

        public blogholders(View itv){
            super(itv);
            vmg =itv;
            sendernamea =(TextView)vmg.findViewById(R.id.msgownertext);
            if(sendernamea.getText().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                vmg.findViewById(R.id.msgsview).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.RIGHT));
            }
        }

        public TextView getPvsendermsg() {
            return pvsendermsg;
        }

        public void setPvsendermsg(String pvsendermsgs) {
            pvsendermsg = vmg.findViewById(R.id.pvmsgcont);
            pvsendermsg.setText(pvsendermsgs);
        }

        public TextView getPvsendername() {
            return pvsendername;
        }

        public void setPvsendername(String pvsendernames) {
            pvsendername = vmg.findViewById(R.id.pvsendersname);
            pvsendermsg.setText(pvsendernames);

        }

        public ImageView getPvownerimgas() {
            return pvownerimgas;
        }

        public void setPvownerimgas(final Context ctx, final String pvownerimgass) {
            pvownerimgas =  (ImageView) vmg.findViewById(R.id.pvsenderimg);
            Picasso.with(ctx).load(pvownerimgass).networkPolicy(NetworkPolicy.OFFLINE).into(pvownerimgas, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(pvownerimgass).into(pvownerimgas);
                }
            });}

        public Context getCxs() {
            return cxs;
        }

        public void setCxs(Context cxs) {
            this.cxs = cxs;
        }

        public TextView getSendermsg() {
            return sendermsg;
        }

        public void setSendermsg(String sendermsgs) {
            sendermsg = (TextView)vmg.findViewById(R.id.msgstxt);
            sendermsg.setText(sendermsgs);
        }

        public ImageView getOwnerimgas() {
            return ownerimgas;
        }

        public void setOwnerimgas(final Context cex, final String ownerimgass) {
            ownerimgas =  (ImageView) vmg.findViewById(R.id.chatterimg);
            Picasso.with(cex).load(ownerimgass).networkPolicy(NetworkPolicy.OFFLINE).into(ownerimgas, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(cex).load(ownerimgass).into(ownerimgas);
                }
            });

        }

        public TextView getSendernamea() {
            return sendernamea;
        }

        public void setSendernamea(String sendernamea1) {
            sendernamea.setText(sendernamea1);
        }


    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//        public List<chatdatabaseinserver> getChatdatabase2() {
//            return chatdatabase2;
//        }
        LinearLayoutManager layoutManager;
        List<chatdatabaseinserver> chatdatabase2;
        List<Object> items;
        private int lastvisibleitem;
        private int totalitemcount;
        Boolean isLoading=true;
//ILoadMore Iloadmore;
        private int visiblethreehold = 7;
        Loader load;
        private int previoustotal=0;
        Context cx;


        public class blogholders extends RecyclerView.ViewHolder {
            Context cxs;
            TextView pvsendermsg;
            TextView pvsendername;
            TextView sendermsg;
            TextView sendernamea;
            ImageView ownerimgas;
            ImageView pvownerimgas;
            LinearLayout dasda;
            View vmg;

            blogholders(View itv){
                super(itv);
                vmg =itv;
                sendernamea =(TextView)vmg.findViewById(R.id.msgownertext);
                if(sendernamea.getText().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                    vmg.findViewById(R.id.msgsview).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.RIGHT));
                }
            }

            public TextView getPvsendermsg() {
                return pvsendermsg;
            }

            public void setPvsendermsg(String pvsendermsgs) {
                pvsendermsg = vmg.findViewById(R.id.pvmsgcont);
                pvsendermsg.setText(pvsendermsgs);
            }

            public TextView getPvsendername() {
                return pvsendername;
            }

            public void setPvsendername(String pvsendernames) {
                pvsendername = vmg.findViewById(R.id.pvsendersname);
                pvsendermsg.setText(pvsendernames);

            }

            public ImageView getPvownerimgas() {
                return pvownerimgas;
            }

            public void setPvownerimgas(final Context ctx, final String pvownerimgass) {
                pvownerimgas =  (ImageView) vmg.findViewById(R.id.pvsenderimg);
                Picasso.with(ctx).load(pvownerimgass).networkPolicy(NetworkPolicy.OFFLINE).into(pvownerimgas, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ctx).load(pvownerimgass).into(pvownerimgas);
                    }
                });}

            public Context getCxs() {
                return cxs;
            }

            public void setCxs(Context cxs) {
                this.cxs = cxs;
            }

            public TextView getSendermsg() {
                return sendermsg;
            }

            void setSendermsg(String sendermsgs) {
                sendermsg = (TextView)vmg.findViewById(R.id.msgstxt);
                sendermsg.setText(sendermsgs);
            }

            public ImageView getOwnerimgas() {
                return ownerimgas;
            }

            public void setOwnerimgas(final Context cex, final String ownerimgass) {
                ownerimgas =  (ImageView) vmg.findViewById(R.id.chatterimg);
                Picasso.with(cex).load(ownerimgass).networkPolicy(NetworkPolicy.OFFLINE).into(ownerimgas, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(cex).load(ownerimgass).into(ownerimgas);
                    }
                });

            }

            TextView getSendernamea() {
                return sendernamea;
            }

            void setSendernamea(String sendernamea1) {
                sendernamea.setText(sendernamea1);
            }


        }
        public class blogholders2 extends RecyclerView.ViewHolder {
            Context cxs;
            TextView pvsendermsg;
            TextView pvsendername;
            TextView sendermsg;
            TextView sendernamea;
            ImageView ownerimgas;
            ImageView pvownerimgas;
            LinearLayout dasda;
            View vmg;

            blogholders2(View itv){
                super(itv);
                vmg =itv;
                sendernamea =(TextView)vmg.findViewById(R.id.msgownertext);
                if(sendernamea.getText().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                    vmg.findViewById(R.id.msgsview).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.RIGHT));
                }
            }

            public TextView getPvsendermsg() {
                return pvsendermsg;
            }

            public void setPvsendermsg(String pvsendermsgs) {
                pvsendermsg = vmg.findViewById(R.id.pvmsgcont);
                pvsendermsg.setText(pvsendermsgs);
            }

            public TextView getPvsendername() {
                return pvsendername;
            }

            public void setPvsendername(String pvsendernames) {
                pvsendername = vmg.findViewById(R.id.pvsendersname);
                pvsendermsg.setText(pvsendernames);

            }

            public ImageView getPvownerimgas() {
                return pvownerimgas;
            }

            public void setPvownerimgas(final Context ctx, final String pvownerimgass) {
                pvownerimgas =  (ImageView) vmg.findViewById(R.id.pvsenderimg);
                Picasso.with(ctx).load(pvownerimgass).networkPolicy(NetworkPolicy.OFFLINE).into(pvownerimgas, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ctx).load(pvownerimgass).into(pvownerimgas);
                    }
                });}

            public Context getCxs() {
                return cxs;
            }

            public void setCxs(Context cxs) {
                this.cxs = cxs;
            }

            public TextView getSendermsg() {
                return sendermsg;
            }

            void setSendermsg(String sendermsgs) {
                sendermsg = (TextView)vmg.findViewById(R.id.msgstxt);
                sendermsg.setText(sendermsgs);
            }

            public ImageView getOwnerimgas() {
                return ownerimgas;
            }

            public void setOwnerimgas(final Context cex, final String ownerimgass) {
                ownerimgas =  (ImageView) vmg.findViewById(R.id.chatterimg);
                Picasso.with(cex).load(ownerimgass).networkPolicy(NetworkPolicy.OFFLINE).into(ownerimgas, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(cex).load(ownerimgass).into(ownerimgas);
                    }
                });

            }

            TextView getSendernamea() {
                return sendernamea;
            }

            void setSendernamea(String sendernamea1) {
                sendernamea.setText(sendernamea1);
            }


        }
        MyAdapter(RecyclerView recyclerView, ChatRoom activity, List<chatdatabaseinserver> chatdatabase2) {

            this.chatdatabase2 = chatdatabase2;
             layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        totalitemcount = layoutManager.getItemCount();
                        lastvisibleitem = layoutManager.findFirstVisibleItemPosition();
                        isLoading = true;
                        if(isLoading){
                            if(totalitemcount>previoustotal){
                                isLoading=true;
                            }
                        }
                        if (!isLoading){
                            if(totalitemcount <= (lastvisibleitem + visiblethreehold)) {
                            isLoading = false;
            }
                        }
                    }
                }});
        }
        static final int left_msg = 0;
        static final int right_msg = 1;

        FirebaseUser fuser;
        @Override
        public int getItemViewType(int position) {
            fuser = FirebaseAuth.getInstance().getCurrentUser();
            if(chatdatabase2.get(position).getMessageowner().equals(fuser.getDisplayName())){

//                return chatdatabase2.get(position) == null?1:0;
                    return right_msg;
            } else if(!chatdatabase2.get(position).getMessageowner().equals(fuser.getDisplayName())){
                   return left_msg;

            }
//            return chatdatabase2.get(position) == null?1:0;
            return -1;
        }

        public void setLoad(Loader load) {
            this.load = load;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {

                case 0:
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatcontentview, parent, false);
                    return new blogholders(view);
                case 1:
                    View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatcontentviewuser, parent, false);
                    return new blogholders2(view2);
                default:
                    View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                    viewHolder = null;
                    break;
            }

            return viewHolder;
        }

        List<User> userlists;
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            chatdatabaseinserver cdis = chatdatabase2.get(position);
//            User us= userlists.get(position);
            switch (holder.getItemViewType()) {
                case 0:
                    blogholders vholder1 =(blogholders) holder;
                    configureViewHolder1(vholder1, position, cdis);
                    break;
//                    holder.setSendernamea(cdis.getMessageowner());
//                    holder.setSendermsg(cdis.getMessage_content());
//
//                    Query userimgincmt = FirebaseDatabase.getInstance().getReference().child("UserDetail").child(holder.getSendernamea().getText().toString());
//
//                    holder.vmg.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            holder.vmg.findViewById(R.id.msgsview).setBackgroundColor(Color.RED);
//                        }
//                    });
//                    userimgincmt.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//    //                    if(String.valueOf(dataSnapshot.getValue()).equals(firebaseUser.getDisplayName())){
//    ////                        holder.vmg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,Gravity.RIGHT));
//    ////                    holder.ownerimgas.setLayoutParams(new LinearLayout.LayoutParams(20,20));
//    ////                        holder.ownerimgas.);
//    //                    }
//                            String ds = String.valueOf(dataSnapshot.child("UserImg").getValue());
//    //                                                          for(DataSnapshot dss:dataSnapshot.getChildren()) {
//    //                                                          User us = new User();
//    //                                                          us.setUserImg(dss.getValue(us.getClass()).getUserImg());
//    ////                                                        String img = String.valueOf(dataSnapshot.getValue(User.class));
//    ////                                                        if(img == null)
//    ////                                                        {((ImageView)v.findViewById(id.comterimg)).setBackground(null);}
//    ////                                                        else if(img != null) {
//                            holder.setOwnerimgas(getContext(), ds);
//    ////                                                        }
//    ////                                                        .setImageURI(Uri.parse(commterimg));
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
                case 1:
                    blogholders2 vholder2 =(blogholders2) holder;
                    configureViewHolder2(vholder2, position, cdis);
                    break;

//                    holder.setSendernamea(cdis.getMessageowner());
//                    holder.setSendermsg(cdis.getMessage_content());
//
//                    Query userimgincmt2 = FirebaseDatabase.getInstance().getReference().child("UserDetail").child(holder.getSendernamea().getText().toString());
//
//                    holder.vmg.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            holder.vmg.findViewById(R.id.msgsview).setBackgroundColor(Color.RED);
//                        }
//                    });
//                    userimgincmt2.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            //                    if(String.valueOf(dataSnapshot.getValue()).equals(firebaseUser.getDisplayName())){
//                            ////                        holder.vmg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,Gravity.RIGHT));
//                            ////                    holder.ownerimgas.setLayoutParams(new LinearLayout.LayoutParams(20,20));
//                            ////                        holder.ownerimgas.);
//                            //                    }
//                            String ds = String.valueOf(dataSnapshot.child("UserImg").getValue());
//                            //                                                          for(DataSnapshot dss:dataSnapshot.getChildren()) {
//                            //                                                          User us = new User();
//                            //                                                          us.setUserImg(dss.getValue(us.getClass()).getUserImg());
//                            ////                                                        String img = String.valueOf(dataSnapshot.getValue(User.class));
//                            ////                                                        if(img == null)
//                            ////                                                        {((ImageView)v.findViewById(id.comterimg)).setBackground(null);}
//                            ////                                                        else if(img != null) {
//                            holder.setOwnerimgas(getContext(), ds);
//                            ////                                                        }
//                            ////                                                        .setImageURI(Uri.parse(commterimg));
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });

//            holder.setOwnerimgas(getContext(),us.getUserImg());
                default:
                    MyAdapter vh =null;
                    configureDefaultViewHolder(vh,position);
                    break;
            }
        }
        private void configureDefaultViewHolder(MyAdapter vh, int position) {

        }
        private void configureViewHolder1(final blogholders vh1, int position, chatdatabaseinserver cdis) {
            if (cdis != null) {
                vh1.setSendernamea(cdis.getMessageowner());
                vh1.setSendermsg(cdis.getMessage_content());

                Query userimgincmt = FirebaseDatabase.getInstance().getReference().child("UserDetail").child(vh1.getSendernamea().getText().toString());

                vh1.vmg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        vh1.vmg.findViewById(R.id.msgsview).setBackgroundColor(Color.RED);
                    }
                });
                userimgincmt.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //                    if(String.valueOf(dataSnapshot.getValue()).equals(firebaseUser.getDisplayName())){
                        ////                        holder.vmg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,Gravity.RIGHT));
                        ////                    holder.ownerimgas.setLayoutParams(new LinearLayout.LayoutParams(20,20));
                        ////                        holder.ownerimgas.);
                        //                    }
                        String ds = String.valueOf(dataSnapshot.child("UserImg").getValue());
                        //                                                          for(DataSnapshot dss:dataSnapshot.getChildren()) {
                        //                                                          User us = new User();
                        //                                                          us.setUserImg(dss.getValue(us.getClass()).getUserImg());
                        ////                                                        String img = String.valueOf(dataSnapshot.getValue(User.class));
                        ////                                                        if(img == null)
                        ////                                                        {((ImageView)v.findViewById(id.comterimg)).setBackground(null);}
                        ////                                                        else if(img != null) {
                        vh1.setOwnerimgas(getContext(), ds);
                        ////                                                        }
                        ////                                                        .setImageURI(Uri.parse(commterimg));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }

        private void configureViewHolder2(final blogholders2 vh2, int position, chatdatabaseinserver cdis) {

            if (cdis != null) {
                vh2.setSendernamea(cdis.getMessageowner());
                vh2.setSendermsg(cdis.getMessage_content());

                Query userimgincmt = FirebaseDatabase.getInstance().getReference().child("UserDetail").child(vh2.getSendernamea().getText().toString());

                vh2.vmg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        vh2.vmg.findViewById(R.id.msgsview).setBackgroundColor(Color.RED);
                    }
                });
                userimgincmt.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //                    if(String.valueOf(dataSnapshot.getValue()).equals(firebaseUser.getDisplayName())){
                        ////                        holder.vmg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,Gravity.RIGHT));
                        ////                    holder.ownerimgas.setLayoutParams(new LinearLayout.LayoutParams(20,20));
                        ////                        holder.ownerimgas.);
                        //                    }
                        String ds = String.valueOf(dataSnapshot.child("UserImg").getValue());
                        //                                                          for(DataSnapshot dss:dataSnapshot.getChildren()) {
                        //                                                          User us = new User();
                        //                                                          us.setUserImg(dss.getValue(us.getClass()).getUserImg());
                        ////                                                        String img = String.valueOf(dataSnapshot.getValue(User.class));
                        ////                                                        if(img == null)
                        ////                                                        {((ImageView)v.findViewById(id.comterimg)).setBackground(null);}
                        ////                                                        else if(img != null) {
                        vh2.setOwnerimgas(getContext(), ds);
                        ////                                                        }
                        ////                                                        .setImageURI(Uri.parse(commterimg));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return chatdatabase2.size();
        }


    }
}
    // TODO: Rename method, update argument and hook method into UI event

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     **/