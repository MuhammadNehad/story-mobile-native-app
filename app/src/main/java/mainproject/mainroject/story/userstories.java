package mainproject.mainroject.story;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import mainproject.mainroject.story.Tables.PDFFILES;
import mainproject.mainroject.story.Tables.Stories;


/**
 * A simple {@link Fragment} subclass.
 */
public class userstories extends Fragment {

    FirebaseAuth auth =FirebaseAuth.getInstance();
//    pdffiles= new Stories();;
    public userstories() {

    }
    // Required empty public constructor
    private FirebaseRecyclerAdapter<Stories, blogholder> fbra;
    FirebaseRecyclerAdapter<PDFFILES, userstories.blogholder> fbra1;
    Query query;
    FirebaseRecyclerOptions<Stories> option;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View curuserstories = inflater.inflate(R.layout.fragment_userstories, container, false);
        final Button checking=(Button) curuserstories.findViewById(R.id.check);
        final RecyclerView recyclerView = (RecyclerView) curuserstories.findViewById(R.id.userstry);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
//          recyclerView.addItemDecoration();

        query= FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByChild("Author").equalTo(auth.getCurrentUser().getDisplayName());
//        Query query1= FirebaseDatabase.getInstance().getReference().child("pdfStoriesdetails").orderByChild("Author").equalTo(auth.getCurrentUser().getDisplayName());

        option = new FirebaseRecyclerOptions.Builder<Stories>()
                .setQuery(query, Stories.class)
                .build();
        checking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checking.getText().equals(R.string.check_your_pdf_stories))
                {

                    checking.setText(R.string.check_your_short_stories);
                    fbra.stopListening();
                    changecheckeditems();
                    fbra1.startListening();
                    recyclerView.setAdapter(fbra1);
                }else if (checking.getText().equals(R.string.check_your_short_stories)){
                    checking.setText(R.string.check_your_pdf_stories);
                    shortcheckeditems();
                    fbra.startListening();
                }
                else{

                    Intent restartIntent = getActivity().getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                    assert restartIntent != null;
                    restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(restartIntent);
                }

            }
        });

        fbra =new FirebaseRecyclerAdapter<Stories, userstories.blogholder>(option) {
            @Override
            public userstories.blogholder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ownerstories, parent, false);

                return new userstories.blogholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull userstories.blogholder holder, int position, @NonNull final Stories model) {
//                holder.setContent(model.getStory_content());
//                holder.setAuthor(model.getAuthor());
//

                holder.setPrice(model.getStory_price());
                holder.setStoryNAme(model.getStoryNaMe());
                holder.setMimgurl(getContext(),model.getLogoUrl());
            holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("StoryName", model.getStoryNaMe());
                bundle.putString("StoryDesc", model.getSTDESC());
                bundle.putString("StoryCont", model.getStory_content());
                bundle.putString("StoryAuth", model.getAuthor());
                bundle.putString("StoryIMG", model.getLogoUrl());
                bundle.putString("StoryPrc", model.getStory_price());
                bundle.putString("StoryType", model.getStorySavingsrc());
                FragmentManager fragmentManager1 = getFragmentManager();
                updatestorycontent usc = new updatestorycontent();
                usc.setArguments(bundle);
                fragmentManager1.beginTransaction().replace(R.id.content, usc).addToBackStack(null).commit();

                  }
                });
            }


        };
        recyclerView.setAdapter(fbra);


        return curuserstories;
    }
    public void shortcheckeditems(){
        query =FirebaseDatabase.getInstance().getReference().child("Storiesdetails").orderByChild("Author").equalTo(auth.getCurrentUser().getDisplayName());
        option = new FirebaseRecyclerOptions.Builder<Stories>()
                .setQuery(query, Stories.class)
                .build();
    }
    public void changecheckeditems(){
        query =FirebaseDatabase.getInstance().getReference().child("pdfStoriesdetails").orderByChild("Author").equalTo(auth.getCurrentUser().getDisplayName());
        FirebaseRecyclerOptions<PDFFILES> option1 = new FirebaseRecyclerOptions.Builder<PDFFILES>()
                .setQuery(query, PDFFILES.class)
                .build();
        fbra1 =new FirebaseRecyclerAdapter<PDFFILES, userstories.blogholder>(option1) {
            @Override
            public userstories.blogholder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ownerstories, parent, false);

                return new userstories.blogholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull userstories.blogholder holder, int position, @NonNull final PDFFILES model) {
//                holder.setContent(model.getStory_content());
//                holder.setAuthor(model.getAuthor());
//

                holder.setPrice(model.getPdfstory_price());
                holder.setStoryNAme(model.getPdfstoryNaMe());
                holder.setMimgurl(getContext(),model.getLogoUrl());
                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("StoryName", model.getPdfstoryNaMe());
                        bundle.putString("StoryDesc", model.getPdfSTDESC());
                        bundle.putString("StoryCont", model.getStory_content());
                        bundle.putString("StoryAuth", model.getPdfAuthor());
                        bundle.putString("StoryIMG", model.getPdfLogoSrc());
                        bundle.putString("StoryPrc", model.getPdfstory_price());
                        bundle.putString("StoryType", model.getStrType());

                        FragmentManager fragmentManager1 = getFragmentManager();
                        updatestorycontent usc = new updatestorycontent();
                        usc.setArguments(bundle);
                        fragmentManager1.beginTransaction().replace(R.id.content, usc).addToBackStack(null).commit();

                    }
                });
            }


        };
    }

    @Override
    public void onStart() {
        super.onStart();
        fbra.startListening();
    }

    public static class blogholder extends RecyclerView.ViewHolder{
        View mview;
        TextView Authors;
        TextView logourld;
        TextView content;
        TextView price;
        TextView storytype;
        TextView storyNAme;
        TextView mimgurl;
        ImageView cover;

        public blogholder(View itemView) {
            super(itemView);
            mview = itemView;
//       Authors = (TextView)itemView.findViewById(R.id.id);
        }

        public void setMimgurl(final Context ctx, final String mimgurl) {
            cover= (ImageView) mview.findViewById(R.id.stryimg);
            cover.setBackground(null);
            Picasso.with(ctx).load(mimgurl).networkPolicy(NetworkPolicy.OFFLINE).fit().into(cover, new Callback() {
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
            content = (TextView)mview.findViewById(R.id.description);
            content.setText(mcontent);
        }

        public void setPrice(String sprice) {
            price= (TextView)mview.findViewById(R.id.pricevalue);
            price.setText(sprice + " " +"$");
        }


        public void setAuthor(String author) {
            Authors = (TextView)mview.findViewById(R.id.Author);
            Authors.setText(author);
        }
        public void setStorytype(String storytypetext) {
            storytype =(TextView)mview.findViewById(R.id.StOrYtYpE);
            storytype.setText(storytypetext);
        }
        public void setStoryNAme(String storyNAMe) {
            storyNAme = (TextView)mview.findViewById(R.id.storyname);
            storyNAme.setText(storyNAMe);

        }
    }

}
