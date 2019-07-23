package mainproject.mainroject.story;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mainproject.mainroject.story.Tables.chatdatabaseinserver;

public class RecyclerViewAdapters extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    //        public List<chatdatabaseinserver> getChatdatabase2() {
//            return chatdatabase2;
//        }
    LinearLayoutManager layoutManager;
    List data;
    List<Object> items;
    private int lastvisibleitem;
    private int totalitemcount;
    Boolean isLoading=true;
    //ILoadMore Iloadmore;
    private int visiblethreehold = 7;
    Loader load;
    private int previoustotal=0;
    Context cx;


//    public class blogholders extends RecyclerView.ViewHolder {
//        Context cxs;
//        TextView pvsendermsg;
//        TextView pvsendername;
//        TextView sendermsg;
//        TextView sendernamea;
//        ImageView ownerimgas;
//        ImageView pvownerimgas;
//        LinearLayout dasda;
//        View vmg;
//
//        blogholders(View itv){
//            super(itv);
//            vmg =itv;
//            sendernamea =(TextView)vmg.findViewById(R.id.msgownertext);
//            if(sendernamea.getText().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
//                vmg.findViewById(R.id.msgsview).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT));
//            }
//        }
//
//        public TextView getPvsendermsg() {
//            return pvsendermsg;
//        }
//
//        public void setPvsendermsg(String pvsendermsgs) {
//            pvsendermsg = vmg.findViewById(R.id.pvmsgcont);
//            pvsendermsg.setText(pvsendermsgs);
//        }
//
//        public TextView getPvsendername() {
//            return pvsendername;
//        }
//
//        public void setPvsendername(String pvsendernames) {
//            pvsendername = vmg.findViewById(R.id.pvsendersname);
//            pvsendermsg.setText(pvsendernames);
//
//        }
//
//        public ImageView getPvownerimgas() {
//            return pvownerimgas;
//        }
//
//        public void setPvownerimgas(final Context ctx, final String pvownerimgass) {
//            pvownerimgas =  (ImageView) vmg.findViewById(R.id.pvsenderimg);
//            Picasso.with(ctx).load(pvownerimgass).networkPolicy(NetworkPolicy.OFFLINE).into(pvownerimgas, new Callback() {
//                @Override
//                public void onSuccess() {
//
//                }
//
//                @Override
//                public void onError() {
//                    Picasso.with(ctx).load(pvownerimgass).into(pvownerimgas);
//                }
//            });}
//
//        public Context getCxs() {
//            return cxs;
//        }
//
//        public void setCxs(Context cxs) {
//            this.cxs = cxs;
//        }
//
//        public TextView getSendermsg() {
//            return sendermsg;
//        }
//
//        void setSendermsg(String sendermsgs) {
//            sendermsg = (TextView)vmg.findViewById(R.id.msgstxt);
//            sendermsg.setText(sendermsgs);
//        }
//
//        public ImageView getOwnerimgas() {
//            return ownerimgas;
//        }
//
//        public void setOwnerimgas(final Context cex, final String ownerimgass) {
//            ownerimgas =  (ImageView) vmg.findViewById(R.id.chatterimg);
//            Picasso.with(cex).load(ownerimgass).networkPolicy(NetworkPolicy.OFFLINE).into(ownerimgas, new Callback() {
//                @Override
//                public void onSuccess() {
//
//                }
//
//                @Override
//                public void onError() {
//                    Picasso.with(cex).load(ownerimgass).into(ownerimgas);
//                }
//            });
//
//        }
//
//        TextView getSendernamea() {
//            return sendernamea;
//        }
//
//        void setSendernamea(String sendernamea1) {
//            sendernamea.setText(sendernamea1);
//        }
//
//
//    }
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
//            sendernamea =(TextView)vmg.findViewById(R.id.msgownertext);
//            if(sendernamea.getText().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
//                vmg.findViewById(R.id.msgsview).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.RIGHT));
//            }
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

    RecyclerViewAdapters(RecyclerView recyclerView, List data) {

        this.data = data;
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
//    @Override
//    public int getItemViewType(int position) {
//        fuser = FirebaseAuth.getInstance().getCurrentUser();
//        if(data.get(position).equals(fuser.getDisplayName())){
//
////                return chatdatabase2.get(position) == null?1:0;
//            return right_msg;
//        } else if(!data.get(position).getMessageowner().equals(fuser.getDisplayName())){
//            return left_msg;
//
//        }
////            return chatdatabase2.get(position) == null?1:0;
//        return -1;
//    }

    public void setLoad(Loader load) {
        this.load = load;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

//        switch (viewType) {
//
//            case 0:
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatcontentview, parent, false);
//                return new ChatRoom.MyAdapter.blogholders(view);
//            case 1:
//                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatcontentviewuser, parent, false);
//                return new ChatRoom.MyAdapter.blogholders2(view2);
//            default:
//                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
//                viewHolder = null;
//                break;
//        }

        return null;
    }

    List<User> userlists;
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
                Object cdis = data.get(position);
                blogholders vholder1 =(blogholders) holder;
                configureViewHolder1(vholder1, position, cdis);

    }
    private void configureDefaultViewHolder(ChatRoom.MyAdapter vh, int position) {

    }

    private void configureViewHolder1(final blogholders vh1, int position, final Object obj) {
        if (obj != null) {

            final Query userimgincmt = FirebaseDatabase.getInstance().getReference().child("UserDetail").child(vh1.getSendernamea().getText().toString());

            userimgincmt.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
