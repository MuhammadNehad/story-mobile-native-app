package mainproject.mainroject.story;


import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import mainproject.mainroject.story.Tables.chatdatabaseinserver;


public class pmview extends Fragment {
//    TODO: def variables
    FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference chatdatabase = FirebaseDatabase.getInstance().getReference().child("chatMsgs");
    DatabaseReference userDetail = FirebaseDatabase.getInstance().getReference().child("UserDetail");

    FirebaseListOptions<chatdatabaseinserver> options;
    FirebaseListAdapter<chatdatabaseinserver> fillPMs;

    Query myUnReadMessages;
    Query getProfilesImagesQuery;
    ListView newMessagesView;
    TextView comingsoontxt;
    String curUserDN;
    String messengersDN;
    public pmview() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_pmview, container, false);
        // Inflate the layout for this fragment
//        TODO:initialize Variables values (widgets, UIS)
//        comingsoontxt =(TextView) view.findViewById(R.id.comingsoon1);
        curUserDN = mAuth.getDisplayName();
        newMessagesView = view.findViewById(R.id.newMessagesViews);
        comingsoontxt = view.findViewById(R.id.comingsoonTxt);
        myUnReadMessages = chatdatabase.child("Recievers").orderByValue().equalTo(curUserDN);
        getProfilesImagesQuery = userDetail.orderByChild("UserName").equalTo(messengersDN);
        //        options = new FirebaseListOptions<String>()
          options = new FirebaseListOptions.Builder<chatdatabaseinserver>()
                .setLayout(R.layout.unread_messages_listitem)//Note: The guide doesn't mention this method, without it an exception is thrown that the layout has to be set.
                .setQuery(myUnReadMessages, chatdatabaseinserver.class)
                .build();
          fillPMs= new FirebaseListAdapter<chatdatabaseinserver>(options) {
            @Override
            protected void populateView(View v, final chatdatabaseinserver model, final int position) {
//                TODO:Initialize Adapter Widgets and vars
                ImageView messengerImg = v.findViewById(R.id.usersImages);
                TextView msgSenderName = v.findViewById(R.id.msgSenderName);
                TextView msgContents = v.findViewById(R.id.msgTextContent);
                if((!model.getRecievers().isEmpty()) || (model.getRecievers() != null )|| (model.getRecievers().size() != 0)) {
                    if(model.getRecievers().contains(curUserDN)) {
                        profileImagesFetch(messengerImg);
                        msgSenderName.setText(model.getMessageowner());
                        msgContents.setText(model.getMessage_content());
                        messengersDN = model.getMessageowner();

                    }

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chatdatabaseinserver  selectPVMSG = fillPMs.getItem(position);
                        privatemsgscheck(selectPVMSG.getMessage_content(),selectPVMSG.getMessageowner());
                    }
                });
                }
            }

        };

        return view;
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
        FirebaseRecyclerOptions<chatdatabaseinserver> pvmsgs = new FirebaseRecyclerOptions.Builder<chatdatabaseinserver>()
                .setQuery(asda,chatdatabaseinserver.class)
                .build();
        FirebaseRecyclerAdapter<chatdatabaseinserver,ChatRoom.blogholders> sdsad =new FirebaseRecyclerAdapter<chatdatabaseinserver, ChatRoom.blogholders>(pvmsgs) {
            @Override
            protected void onBindViewHolder(@NonNull ChatRoom.blogholders holder, int position, @NonNull chatdatabaseinserver model) {
                holder.setPvsendername(msgsender);
                holder.setPvsendermsg(msgcontent);

                Query userimgincmt = FirebaseDatabase.getInstance().getReference().child("UserDetail").child(pvsendername.getText().toString());

                userimgincmt.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String ds = String.valueOf(dataSnapshot.child("UserImg").getValue());
                        Picasso.with(getContext()).load(ds).fit().into(((ImageView) chatdegreesview.findViewById(R.id.pvsenderimg)));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public ChatRoom.blogholders onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.totalprivatechats, parent, false);


                return new ChatRoom.blogholders(view);
            }
        };
        activatethtedialog.show();
    }
//    TODO: fetch senders Images
    public void profileImagesFetch(final ImageView messengerImage){
        getProfilesImagesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imgurl = String.valueOf(dataSnapshot.child("ProfileImages"));
                Picasso.with(getContext()).load(imgurl).fit().into(messengerImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
