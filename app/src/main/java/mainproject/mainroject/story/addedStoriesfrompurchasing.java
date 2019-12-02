package mainproject.mainroject.story;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.firebase.ui.database.*;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import mainproject.mainroject.story.Tables.purchasedstories;


/**
 * A simple {@link Fragment} subclass.
 */
public class
addedStoriesfrompurchasing extends Fragment {

    private FirebaseRecyclerOptions<purchasedstories> option;
    private RecyclerView recyclerView;

    private FirebaseRecyclerAdapter<purchasedstories, blogholder> fbra;
    FirebaseAuth auth =FirebaseAuth.getInstance();
    private TextView csa;
    private TextView csc;
    private TextView csn;
currentstory cs =new currentstory();
ProgressDialog pd;  
    public addedStoriesfrompurchasing() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View psView = inflater.inflate(R.layout.fragment_added_storiesfrompurchasing, container, false);
        recyclerView = (RecyclerView) psView.findViewById(R.id.userpurchasedstries);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        csa =(TextView)psView.findViewById(R.id.currentstoryAuthor);
        pd=new ProgressDialog(getContext());
         csn = (TextView) psView.findViewById(R.id.currentstoryname);
        csc = (TextView)psView.findViewById(R.id.currentstorycontent);
        Query query= FirebaseDatabase.getInstance().getReference().child("purchasedstories").orderByChild("purchasername").equalTo(auth.getCurrentUser().getDisplayName());

        option = new FirebaseRecyclerOptions.Builder<purchasedstories>()
                .setQuery(query, purchasedstories.class)
                .build();
        fbra =new FirebaseRecyclerAdapter<purchasedstories, blogholder>(option) {
            @Override
            public blogholder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchasedstoriesnew, parent, false);

                return new blogholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final blogholder holder, int position, @NonNull final purchasedstories model) {
//                holder.setContent(model.getStory_content());
//                holder.setAuthor(model.getAuthor());
//

                holder.setStoryNAme(model.getStory_name());
                holder.setMimgurl(getContext(),model.getStory_ImgUrl());
                holder.setAuthor(model.getStory_Author());
                holder.setStodesc(model.getStrytype());
//                holder.setStoryNAme(model.getStory_name());
 holder.mview.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         Bundle bundle = new Bundle();
         bundle.putString("StoryName", model.getStory_name());
         bundle.putString("StoryDesc", model.getStory_Desc());
         bundle.putString("StoryCont", model.getStory_Content());
         bundle.putString("StoryAuth", model.getStory_Author());
         bundle.putString("StoryIMG", model.getStory_ImgUrl());

         sendstory(model.getStorySrc(), bundle);

     }
 });
            }


        };
        recyclerView.setAdapter(fbra);


        return psView;
    }

    private void sendstory(String strysRC, Bundle bundle){

        if(strysRC.equals("AppCreationStory")) {
            pd.setMessage("Loading");
            pd.show();
            FragmentManager fragmentManager1 = getFragmentManager();
            currentstory css = new currentstory();
            css.setArguments(bundle);
            fragmentManager1.beginTransaction().replace(R.id.content, css).addToBackStack(null).commit();
            pd.dismiss();
        }
        if(strysRC.equals("PDFSTORY")){
//             Bundle bundle = new Bundle();
            pd.setMessage("Loading...");
            pd.show();

            FragmentManager fragmentManager2 = getFragmentManager();
            pdfview pdfv = new pdfview();
            pdfv.setArguments(bundle);
            fragmentManager2.beginTransaction().replace(R.id.content, pdfv).addToBackStack(null).commit();
            pd.dismiss();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
    fbra.startListening();
    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        fbra.stopListening();
//    }

    public static class blogholder extends RecyclerView.ViewHolder{
        View mview;
        TextView Authors_Name;
        TextView logourld;
        TextView Story_CONTent;
        TextView strysrc;
        TextView mimgurl;
        RatingBar Strate;
        TextView sto_desc;
        ImageView st_cover;
        TextView story___NAme;
        TextView price;
//       private onclicklistener mlistener;
//
//       public void setMlistener(onclicklistener mlistener) {
//           this.mlistener = mlistener;
//       }

        //       public interface onclicklistener{
//    void onItemClick(int position);
//}
        public blogholder(View itemView) {
            super(itemView);

            mview = itemView;
            mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
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
        }

        public TextView getStrysrc() {
            return strysrc;
        }

        public void setStrysrc(TextView strysrc) {
            this.strysrc = strysrc;
        }

        public TextView getStory_CONTent() {
            return Story_CONTent;
        }

        public void setStory_CONTent(String story_CONTent) {
//            Story_CONTent =(TextView)mview.findViewById(R.id.purchasedstorycontent);
        }

        public void setMimgurl(final Context ctx, final String mimgurl) {
            st_cover= (ImageView) mview.findViewById(R.id.puchasedstryimg);
            st_cover.setBackground(null);
            Picasso.with(ctx).load(mimgurl).networkPolicy(NetworkPolicy.OFFLINE).fit().into(st_cover, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(mimgurl).into(st_cover);
                }
            });
        }


        public void setContent(String mcontent) {
//           content = (TextView)mview.findViewById(R.id.description);
//           content.setText("Story Description" + mcontent);
        }
//
//        public void setPrice(String sprice) {
//            price= (TextView)mview.findViewById(R.id.storypricetxt);
//            price.setText("Price :" + " " + sprice);
//        }


        public void setAuthor(String author) {
            Authors_Name= (TextView)mview.findViewById(R.id.puchasedstoryAuthor);
            Authors_Name.setText( " " + author);
        }

        public void setStoryNAme(String storyNAMe) {
            story___NAme = (TextView)mview.findViewById(R.id.puchasedstoryname);
            story___NAme.setText(" " + storyNAMe);

        }


        public void setStodesc(String stodesc1) {
//            sto_desc =(TextView)mview.findViewById(R.id.puchasedstoryType);
//            sto_desc.setText(stodesc1);
        }

        public void setStrate(String strate1) {
            Strate =(RatingBar)mview.findViewById(R.id.stratebar);
            Strate.setRating(Float.parseFloat(strate1));
        }
    }

}
