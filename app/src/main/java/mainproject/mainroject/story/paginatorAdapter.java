package mainproject.mainroject.story;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.lang.reflect.Method;
import java.util.List;

import mainproject.mainroject.story.Tables.Stories;

//import mainproject.mainroject.story.Tables.Stories;


public class paginatorAdapter {
    int mCurrentpage = 0;
    int itempos = 0;
    private String mLastKey="";
    private String mPreviousKey="";
    public static final int total_items_to_Load =10;
//    private void refreshing(SwipeRefreshLayout mrefreshlayout){
//        mrefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
////                loadMoreData();
//            }
//        });
//    }
    private void loadMoreData(DatabaseReference dbRef, final RecyclerView.Adapter<RecyclerView.ViewHolder> MyAdapter, final List getData){
        mCurrentpage++;
        itempos=0;

        dbRef.orderByKey().endAt(mLastKey).limitToLast(10).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



//                    cl = Class.forName(ClassName);
//                    Method meth = cl.getMethod(ClassName);
//                ClassLoader clr =cl.getClassLoader();
//                    Class cls2 = Class.forName("java.lang.Thread", true, clr);

                   Stories cl = dataSnapshot.getValue(Stories.class);
                String mLastKeyalt = dataSnapshot.getKey();


                if (!mPreviousKey.equals(mLastKeyalt)){
                    getData.add(itempos++,cl);

//                    /**/                    chalist.setAdapter(customrecyclerviewAdapter);
                }else{
                    mPreviousKey =mLastKeyalt;
                }

                if(itempos == 1){

                    mLastKey = mLastKeyalt;
                }

                Log.d("TotalKeys","LastKey: "+mLastKey+" previous key:"+mPreviousKey+" messagekey"+ mLastKeyalt);


                MyAdapter.notifyDataSetChanged();
//                customrecyclerviewAdapter = new MyAdapter(chalist,ChatRoom.this,getchatdata);
//                mrefreshlayout.setRefreshing(false);
//                chatlayout.scrollToPositionWithOffset(7,0);

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

//        onStart();
        //        mCurrentpage++;
//        usechildlistner();
    }
    public void usechildlistner(DatabaseReference dbRef, final RecyclerView.Adapter<RecyclerView.ViewHolder> MyAdapter, final RecyclerView m_RecyclerView, final List getData){

        dbRef.limitToLast(mCurrentpage*total_items_to_Load ).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Stories cdbis = dataSnapshot.getValue(Stories.class);

                itempos++;
                if(itempos == 1){
                    String mLastKeyalt = dataSnapshot.getKey();

//                    setItempos(itempos,mLastKeyalt,mLastKeyalt);
                    mLastKey = mLastKeyalt;
                    mPreviousKey = mLastKeyalt;

                }
                getData.add(cdbis);

//                itempos++;
//                chalist.scrollToPosition(getchatdata.size()-1);

                MyAdapter.notifyDataSetChanged();
                m_RecyclerView.scrollToPosition(getData.size()-1);
//                mrefreshlayout.setRefreshing(false);
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
//        onStart();
    }
   }
   class passingTables {

       Class passingTables(String st) throws ClassNotFoundException {
            Class cl= Class.forName(st);
            return cl;
        }

   }
