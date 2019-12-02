package mainproject.mainroject.story;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import mainproject.mainroject.story.Tables.Images;

/**
 * Created by UNiversaL on 3/30/2018.
 */

public class userimgs extends Fragment {
    private FirebaseRecyclerOptions<Images> option;
    private RecyclerView recyclerView;
    public userimgs(){}
    private FirebaseRecyclerAdapter<Images, blogholder> fbra;
FirebaseAuth auth =FirebaseAuth.getInstance();
            String User = auth.getCurrentUser().getDisplayName();
DatabaseReference myfbdb = FirebaseDatabase.getInstance().getReference();
DatabaseReference userDetaildb= myfbdb.child("UserDetail").child(User);
@Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userimgs,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.pimgs);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
//          recyclerView.addItemDecoration();
    if(!auth.getCurrentUser().equals(null)) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Images").orderByChild("OwnerEmail").equalTo(auth.getCurrentUser().getEmail());

        option = new FirebaseRecyclerOptions.Builder<Images>()
                .setQuery(query, Images.class)
                .build();
        fbra =new FirebaseRecyclerAdapter<Images, userimgs.blogholder>(option) {
            @Override
            public userimgs.blogholder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profileimg, parent, false);

                return new userimgs.blogholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull userimgs.blogholder holder, int position, @NonNull final Images model) {
//                holder.setContent(model.getStory_content());
//                holder.setAuthor(model.getAuthor());
//
//                holder.setPrice(model.getStory_price());
                holder.setMimgurl(getContext(), model.getImgUrl());
                String imgurl = model.getImgUrl();
                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Profile profilesimg =new Profile();

                        userDetaildb.child("UserImg").setValue(model.getImgUrl());
                        FragmentManager fragmentManager1 = getFragmentManager();
                        assert fragmentManager1 != null;
                        fragmentManager1.beginTransaction().replace(R.id.content, new Profile()).commit();

                    }
                });
                holder.mview.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        return true;
                    }
                });
            }


        };
        recyclerView.setAdapter(fbra);
    }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        fbra.startListening();
    }

    public static class blogholder extends RecyclerView.ViewHolder {
        View mview;
        TextView Authors;
        TextView logourld;
        TextView content;
        TextView price;
        TextView mimgurl;
        ImageView profile;

        public blogholder(View itemView) {
            super(itemView);
            mview = itemView;
//       Authors = (TextView)itemView.findViewById(R.id.id);
        }

        public void setMimgurl(final Context ctx, final String mimgurl) {
            profile = (ImageView) mview.findViewById(R.id.profileimgs);
            profile.setBackground(null);
            Picasso.with(ctx).load(mimgurl).networkPolicy(NetworkPolicy.OFFLINE).fit().into(profile, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(mimgurl).fit().into(profile);
                }
            });
        }


//        public void setContent(String mcontent) {
//            content = (TextView)mview.findViewById(R.id.description);
//            content.setText(mcontent);
//        }
//
//        public void setPrice(String sprice) {
//            price= (TextView)mview.findViewById(R.id.storypricetxt);
//            price.setText(sprice);
//        }
//
//
//        public void setAuthor(String author) {
//            Authors = (TextView)mview.findViewById(R.id.Author);
//            Authors.setText(author);
//        }
//    }
    }
}
