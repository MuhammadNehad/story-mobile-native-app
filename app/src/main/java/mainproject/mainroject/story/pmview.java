package mainproject.mainroject.story;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import mainproject.mainroject.story.Tables.chatdatabaseinserver;


public class pmview extends Fragment {

    DatabaseReference chatdatabase = FirebaseDatabase.getInstance().getReference().child("chatMsgs");
    TextView comingsoontxt;
    FirebaseListOptions<String> options;
    FirebaseListAdapter<String > fillPMs;

    public pmview() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_pmview, container, false);
        // Inflate the layout for this fragment
        comingsoontxt =(TextView) view.findViewById(R.id.comingsoon1);
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
            public ChatRoom.blogholders onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.totalprivatechats, parent, false);


                return new ChatRoom.blogholders(view);
            }
        };
        activatethtedialog.show();
    }
}
