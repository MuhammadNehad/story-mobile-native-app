package mainproject.mainroject.story;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by UNiversaL on 4/4/2018.
 */

public class currentstory extends Fragment {
        TextView csa;
        TextView csn;
        TextView csc;
        ImageView csi;
        TextView csd;
        Float scale = 1f;
        ScaleGestureDetector sgd;
        TextView hc,sc;
        RatingBar ratebar;
        android.graphics.Matrix matrix = new android.graphics.Matrix();

LinearLayout heads;
    public currentstory(){}
DatabaseReference ratedf = FirebaseDatabase.getInstance().getReference().child("storyratings");
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.currentstory, container, false);
  final Bundle bundle =getArguments();
        heads = (LinearLayout)view.findViewById(R.id.headers);
        ratebar =(RatingBar)view.findViewById(R.id.ratestrycontent);
        hc= (TextView)view.findViewById(R.id.hidecontents);
        sc= (TextView)view.findViewById(R.id.showcontents1);
        csa =(TextView)view.findViewById(R.id.currentstoryAuthor);
        csn = (TextView) view.findViewById(R.id.currentstoryname);
        csc = (TextView)view.findViewById(R.id.currentstorycontent);
        csd = (TextView)view.findViewById(R.id.currentstorydesc);
        csi =(ImageView)view.findViewById(R.id.currentstryimg);
        
        csn.setText(bundle.getString("StoryName"));
        csd.setText(bundle.getString("StoryDesc"));
        csc.setText(bundle.getString("StoryCont"));
        csa.setText(bundle.getString("StoryAuth"));
        csi.setBackground(null);
        csc.setTextIsSelectable(false);
        hc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heads.setVisibility(View.GONE);
                sc.setVisibility(View.VISIBLE);
            }
        });
        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heads.setVisibility(View.VISIBLE);
                sc.setVisibility(View.GONE);
            }
        });
        Picasso.with(getContext()).load(bundle.getString("StoryIMG")).fit().into(csi);
        DatabaseReference childs =  ratedf.child(bundle.getString("StoryName")+ FirebaseAuth.getInstance().getCurrentUser().getDisplayName());



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
// if(i>0) {
//    myRef.child(storyNAME).child("stRating").setValue(String.valueOf(total));
//}
//                }
//                }
           int i =0 ;
                DatabaseReference childs =  ratedf.child(bundle.getString("StoryName")+ FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                childs.child("STRYname").setValue(bundle.getString("StoryName"));
                childs.child("styrates").setValue(String.valueOf(rating));
                childs.child("userEmail").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            }
        });
        childs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
    if(dataSnapshot.hasChild("styrates")){
                if(!dataSnapshot.child("styrates").getValue().equals(null)) {
    float rates = Float.parseFloat(String.valueOf(dataSnapshot.child("styrates").getValue()));
    ratebar.setRating(rates);
}else {
    ratebar.setRating(0);
}
    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                }
        });

        return view;

    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale= scale*detector.getScaleFactor();
            scale =Math.max(0.1f,Math.min(scale,5f));
            matrix.setScale(scale,scale);
            csc.setTextScaleX(scale);

        return true;
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        sgd.onTouchEvent(event);
        return true;
    }
}
