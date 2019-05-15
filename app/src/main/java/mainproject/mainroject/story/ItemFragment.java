package mainproject.mainroject.story;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;

import mainproject.mainroject.story.Tables.PDFFILES;
import mainproject.mainroject.story.Tables.Stories;
import mainproject.mainroject.story.Tables.comments;
import mainproject.mainroject.story.dummy.DummyContent.DummyItem;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static mainproject.mainroject.story.R.id;
import static mainproject.mainroject.story.R.layout;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = "itemFragment";
    private static int UPDOWN = 0;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ArrayList<String> musername = new ArrayList<>();
    private OnListFragmentInteractionListener mListener;
    private ArrayAdapter<String> arrayAdapter;
    public static final int PAYPAL_REQUEST_CODE = 7171;
//    maincontent MainCon = new maincontent();
    String amount_to_pay="";
    String searchquery;
    protected static PayPalConfiguration config3 = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .defaultUserEmail(paypalconfig.paypal_Publiser_Email);
//    .clientId(paypalconfig.paypal_client_Id
    private DatabaseReference booklikes;
    private DatabaseReference bookdislikes;
    private DatabaseReference reports;
    private AlertDialog.Builder Storyreports;
    int currentamount=5;
    float totalcount = 0;
    private EditText SearchET;

    public void settotalcount(float total_count){
        this.totalcount = total_count;
    }
    public float getTotalcount(){return totalcount;}

    float totalrate = 0;
    public void settotalrate(float total_rate){
        this.totalrate = total_rate;
    }
    float rates;
    public void setrate(float userRate){
        this.rates = userRate;
    }
    public float getrate(){return rates;}

    public float getTotalrate(){return totalrate;}
    public void setcurrentamount(int currentamounts){
        this.currentamount = currentamounts;
    }
    public int getCurrentamount(){
        return currentamount;
    }
    private double newamount=1;
    private int total_items_to_Load=2;
    int mCurrentpage=1;

    private static int startingAt =1;
    private static int viewAmount =10;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
//    maincontent mc =new maincontent();

    public ItemFragment() {
    }
    int SearchBoxVisiblity = View.GONE;
    int SearchBoxVisiblityVisible = View.VISIBLE;
    int SearchBoxVisiblityInVisible = View.INVISIBLE;


    public void setRelativeLayoutVisibility(int value){
        this.SearchBoxVisiblity=value;
//        return this.SearchBoxVisiblity;
    }
    public int getSearchBoxVisiblity(){
        return this.SearchBoxVisiblity;
    }


    // TODO: Customize parameter initialization
    AlertDialog.Builder StoryDetailsl;
    FirebaseRecyclerAdapter<Stories , blogholder> fbra;
    FirebaseRecyclerAdapter<PDFFILES, blogholder> fbpdfra;
    final ArrayList<comments> models = new ArrayList<comments>();

    protected FirebaseListAdapter<comments> fbla;
    FirebaseRecyclerOptions<PDFFILES> pdfoption;

    RecyclerView recyclerView;
    RecyclerView pdfrecyclerView;
    FirebaseDatabase mydatabase=FirebaseDatabase.getInstance();
    DatabaseReference myRef = mydatabase.getReference().child("StoriesDetails");
    DatabaseReference mystrateRef = mydatabase.getReference().child("storyratings");
    DatabaseReference psdb = mydatabase.getReference().child("purchasedstories");
    DatabaseReference osdb = mydatabase.getReference().child("StoriesDetails");
    DatabaseReference pdfosdb = mydatabase.getReference().child("pdfStoriesdetails");
    DatabaseReference CurUserLinks = mydatabase.getReference().child("UserDetail");
    FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
    String curAuthDN = auth.getDisplayName();
   public static String clientpaypalid;
    public boolean increaserate = false;
    public boolean decreasecreaserate = false;
    public boolean reportrate = false;
    ImageButton convertingSearch;
    ProgressDialog mprogress;
    FirebaseRecyclerOptions<Stories> option;
    LinearLayout pdfrv;
    Button pdfviewerbtn, onsiteview, Education,Horror,Action,Politics,Religious,all;
    public Query getQuery() {
        return query;
    }
    public void setQuery(Query query) {
        this.query = query;
    }
    private String bookkey="";
    Query query =FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByChild("storyNaMe").startAt(bookkey).limitToFirst(viewAmount);
    Query loadmoreQuery =FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByKey().startAt(bookkey).limitToFirst(viewAmount);

    public Query getPdfquery() {
        return pdfquery;
    }
    public void setPdfquery(Query pdfquery) {
        this.pdfquery = pdfquery;
    }
    int nextpage =1;
    Query pdfquery =FirebaseDatabase.getInstance().getReference().child("pdfStoriesdetails");

    int itpos = 0;
    ProgressDialog loading;
    ScrollView scrollView;
    private String storiesname= "";
    public boolean checkExistedpaypalEmail(String publishers){
        final boolean[] Found = {false};
        Query PaypalEmailExistance = CurUserLinks.child(publishers).child("userpaypalacc");
        PaypalEmailExistance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Found[0] = true;
                if(!dataSnapshot.exists()){
                    Toast.makeText(getContext(),"You havent Submitted Paypal ACCOUNT",Toast.LENGTH_LONG).show();
                }else{
                    paypalconfig.paypal_Publiser_Email =dataSnapshot.getValue() == null? null : dataSnapshot.getValue().toString();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return Found[0];
    }
//    RelativeLayout SearchBox;
    RelativeLayout SearchBox;
    HorizontalScrollView CategorySearch;
    int count = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(layout.fragment_item_list, container, false);
        // TODO: RecyclerView
        loading = new ProgressDialog(getContext());
        Religious=(Button)view.findViewById(R.id.Religious);
        Action=(Button)view.findViewById(R.id.Action);
        Politics=(Button)view.findViewById(R.id.Politics);
        Horror=(Button)view.findViewById(R.id.Horror);
        Education=(Button)view.findViewById(R.id.Education);
        pdfrv =(LinearLayout)view.findViewById(R.id.pdffilesview);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        SearchBox = view.findViewById(R.id.relLayout1);
        SearchET =view.findViewById(R.id.input_search);
        CategorySearch = view.findViewById(id.CategoriesSearch);
        convertingSearch = view.findViewById(id.convertSeaching);
//        SearchBox.setVisibility(getSearchBoxVisiblity());
        scrollView = view.findViewById(id.itemfragscrollview);
        all=(Button)view.findViewById(R.id.All);
//        pdfrecyclerView.setHasFixedSize(true);
        recyclerView.setHasFixedSize(true);
        myRef.keepSynced(true);
        pdfosdb.keepSynced(true);
        scrollView.setFocusable(false);

        //TODO:SEARCH
        final LinearLayoutManager pdfrecyclerViewlayout= new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        final RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager);


        convertingSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SearchBox.getVisibility() == View.VISIBLE){
                    SearchBox.setVisibility(View.GONE);
                    CategorySearch.setVisibility(View.VISIBLE);
                }else{
                    SearchBox.setVisibility(View.VISIBLE);
                    CategorySearch.setVisibility(View.GONE);

                }
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {

//                viewAmount += viewAmount + 10;
//                Toast.makeText(getContext(),String.valueOf(viewAmount),Toast.LENGTH_SHORT).show();
                //               try {
//
//
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        System.out.println("The RecyclerView is not scrolling");
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        System.out.println("Scrolling now");
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        System.out.println("Scroll Settling");
                        break;

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                dx=0;
                if (dx > 0) {
                    System.out.println("Scrolled Right");
                } else if (dx < 0) {
                    System.out.println("Scrolled Left");
                } else {
                    System.out.println("No Horizontal Scrolled");
                }
                if (dy >= recyclerView.getBottom()) {
                    System.out.println("Scrolled Downwards");
                    UPDOWN= 1;
                    loadingOtherData();
                } else if (dy <= recyclerView.getTop()) {
                    System.out.println("Scrolled Upwards");
                    UPDOWN= 2;
//                    loadingOtherData();

                } else {
                    System.out.println("No Vertical Scrolled");
                }
            }
        });

            option = new FirebaseRecyclerOptions.Builder<Stories>()
                    .setQuery(query, Stories.class)
                    .build();

            fbra = new FirebaseRecyclerAdapter<Stories, blogholder>(option) {

                @Override
                public blogholder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(layout.fragment_item, parent, false);

                    return new blogholder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull blogholder holder, int position, @NonNull final Stories model) {

                    bookkey = getRef(viewAmount - 1).getKey();
//                    Toast.makeText(getContext(), bookkey, Toast.LENGTH_SHORT).show();

//                bookkey = storiesListener.getSnapshot(position++).getKey();
//                holder.setContent(model.getStory_content());
                    holder.setAuthor(model.getAuthor());
                    holder.setStoryNAme(model.getStoryNaMe());
//                holder.setPrice(model.getStory_price());
                    holder.setMimgurl(getContext(), model.getLogoUrl());
                    holder.setStodesc(model.getStrType());

                    final int finalPosition = position;
                    holder.mview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), bookkey, Toast.LENGTH_SHORT).show();

                            Stories stories = fbra.getItem(finalPosition);
                            showDetailsDialog(stories.getAuthor(), stories.getSTDESC(), stories.getStory_price(), stories.getLogoUrl(), stories.getStoryNaMe(), stories.getStory_content(), (stories.getStorySavingsrc() == null ? "AppCreationStory" : stories.getStorySavingsrc()), bookkey);
                        }
                    });
                }
            };

//        option = new FirebaseRecyclerOptions.Builder<Stories>()
//                .setQuery(query, Stories.class)
//                .build();
//        fbra =new FirebaseRecyclerAdapter<Stories, blogholder>(option) {
//
//            @Override
//            public blogholder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(layout.fragment_item, parent, false);
//
//                return new blogholder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull blogholder holder, int position, @NonNull final Stories model) {
//
//                bookkey = getRef(10).getKey();
//                Toast.makeText(getContext(),bookkey,Toast.LENGTH_LONG).show();
//
////                bookkey = storiesListener.getSnapshot(position++).getKey();
////                holder.setContent(model.getStory_content());
//                holder.setAuthor(model.getAuthor());
//                holder.setStoryNAme(model.getStoryNaMe());
////                holder.setPrice(model.getStory_price());
//                holder.setMimgurl(getContext(),model.getLogoUrl());
//                holder.setStodesc(model.getStrType());
//
//                final int finalPosition = position;
//                holder.mview.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getContext(),bookkey,Toast.LENGTH_LONG).show();
//
//                        Stories stories = fbra.getItem(finalPosition);
//                        showDetailsDialog(stories.getAuthor() ,stories.getSTDESC(), stories.getStory_price(),stories.getLogoUrl(),stories.getStoryNaMe(),stories.getStory_content(),(  stories.getStorySavingsrc() == null ? "AppCreationStory" : stories.getStorySavingsrc()),bookkey);
//                    }
//                });
//            }
//        };


//        pdfoption = new FirebaseRecyclerOptions.Builder<PDFFILES>()
//                .setQuery(pdfquery, PDFFILES.class)
//                .build();
//        fbpdfra =new FirebaseRecyclerAdapter<PDFFILES, blogholder>(pdfoption) {
//
//            @Override
//            public blogholder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(layout.pdflistitem, parent, false);
//
//
//                return new blogholder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull blogholder holder, int position, @NonNull final PDFFILES model) {
//
//
//                holder.setPdfAuthor(model.getPdfAuthor());
//                holder.setPdfstname(model.getPdfstoryNaMe());
//                holder.setPdfcover(getContext(),model.getLogoUrl());
//                holder.setPdfdesc(model.getStrType());
////                holder.setContent(model.getStory_content());
////                holder.setAuthor(model.getAuthor());
////                holder.setStoryNAme(model.getStoryNaMe());
//////                holder.setPrice(model.getStory_price());
////                holder.setMimgurl(getContext(),model.getLogoUrl());
////                holder.setContent(model.getSTDESC());
//
//                final int finalPosition = position;
//                holder.mview.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        PDFFILES pdfstories = fbpdfra.getItem(finalPosition);
//
//                        showDetailsDialog(pdfstories.getPdfAuthor() ,pdfstories.getPdfSTDESC(), pdfstories.getPdfstory_price(),pdfstories.getLogoUrl(),pdfstories.getPdfstoryNaMe(),pdfstories.getStory_content(),"PDFSTORY",bookkey);
//                    }
//                });
//
//            }
//
//
//        };

        recyclerView.setAdapter(fbra);

        //        pdfrecyclerView.setAdapter(fbpdfra);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnContextClickListener(new View.OnContextClickListener() {
                @Override
                public boolean onContextClick(View v) {
                    return false;
                }
            });
        }

        SearchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Toast.makeText(getContext(), charSequence.toString(), Toast.LENGTH_LONG).show();
                try{

                    setQuery(FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByChild("storyNaMe").startAt(charSequence.toString()).endAt(charSequence.toString()+"\uf8ff"));
                    Thread.sleep(200);
                    searching(getQuery());


                }catch (Exception ex){
                    Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 try {
                   setQuery(FirebaseDatabase.getInstance().getReference().child("StoriesDetails"));
//                     setPdfquery(FirebaseDatabase.getInstance().getReference().child("pdfStoriesdetails"));

                     Thread.sleep(1000);
                     SelectType(getQuery());
//                     pdfrecyclerView.setVisibility(View.GONE);
                 } catch (InterruptedException e) {
                    e.printStackTrace();
                    loading.show();
                     loading.setMessage("No Story");
                }
            }
        });
        Religious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    setQuery(FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByChild("StrType").equalTo("Religious"));
//                    setPdfquery(FirebaseDatabase.getInstance().getReference().child("pdfStoriesdetails").orderByChild("StrType").equalTo("Religious"));

                    Thread.sleep(1000);
                    SelectType(getQuery());
//                    pdfrecyclerView.setVisibility(View.GONE);
                    fbra.notifyDataSetChanged();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
             }});


        Politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    setQuery(FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByChild("StrType").equalTo("Politics"));
//                    setPdfquery(FirebaseDatabase.getInstance().getReference().child("pdfStoriesdetails").orderByChild("StrType").equalTo("Politics"));

                    Thread.sleep(1000);
                    SelectType(getQuery());
//                    pdfrecyclerView.setVisibility(View.GONE);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        });
        Action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              try {
                  setQuery(FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByChild("StrType").equalTo("Action"));

                  Thread.sleep(1000);
                  SelectType(getQuery());

              } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               }
        });

        Horror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               try {
                   setQuery(FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByChild("StrType").equalTo("Horror"));

                   Thread.sleep(1000);
                   SelectType(getQuery());

               } catch (InterruptedException e) {
                    e.printStackTrace();
                }

               }
        });
        Education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


          try {

              setQuery(FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByChild("StrType").equalTo("Science"));
//              setPdfquery(FirebaseDatabase.getInstance().getReference().child("pdfStoriesdetails").orderByChild("StrType").equalTo("Science"));
              Thread.sleep(1000);
              SelectType(getQuery());
//              pdfrecyclerView.setVisibility(View.GONE);

          } catch (InterruptedException e) {
                            e.printStackTrace();

                        }

                       }

                });

        //TODO: loading more
//        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(recyclerViewlayout) {
//            @Override
//            public void onLoadMore(int current_page) {
//                setQuery(FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByKey().endAt(bookkey).limitToFirst(2));
//                setPdfquery(FirebaseDatabase.getInstance().getReference().child("pdfStoriesdetails").orderByKey().endAt(bookkey).limitToFirst(2));
//                mCurrentpage++;
//                loadmorestories();
//            }
//        });
        //TODO:
//        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(DummyContent.ITEMS, (OnListFragmentInteractionListener) musername));
//        }


//
//        FirebaseListOptions<String> options = new FirebaseListOptions.Builder<String>()
//                .setQuery(query, String.class)
//                .setLayout()
//                .build();
//        FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(options) {
//            @Override
//            protected void populateView(View v, String  model, int position) {
//                TextView text = (TextView)view.findViewById(R.id.id);
////                text.setText(model);
//            }
//        };
//
//        FirebaseRecyclerAdapter_LifecycleAdapter<String> option = new FirebaseListOptions.Builder<Stories>()
//                .setQuery(query, Stories.class)
//                .setLayout(android.R.layout.simple_list_item_1)
//                .build();
        FirebaseRecyclerOptions<String> option1 =
                new FirebaseRecyclerOptions.Builder<String>()
                        .setQuery(query, String.class)
                        .build();




        return view;
    }
    public void loadingOtherData(){
        FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));
                Toast.makeText(getContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();

                if(count <= i){
                    try{
                        count +=viewAmount;
                        setQuery(FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByChild("storyNaMe").startAt(bookkey).limitToFirst(viewAmount+2));
                        Thread.sleep(100);
                        SelectType(getQuery());
                    }catch (Exception ex){
                        Log.d(TAG,ex.getMessage());
                    }

                }else{

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public FirebaseRecyclerAdapter getAllStories(Query queryLoads){
      try{
        option = new FirebaseRecyclerOptions.Builder<Stories>()
                .setQuery(queryLoads, Stories.class)
                .build();
        fbra =new FirebaseRecyclerAdapter<Stories, blogholder>(option) {

            @Override
            public blogholder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(layout.fragment_item, parent, false);

                return new blogholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull blogholder holder, int position, @NonNull final Stories model) {

                try{
                bookkey = getRef(viewAmount-1).getKey();
                Toast.makeText(getContext(),bookkey,Toast.LENGTH_SHORT).show();
                }catch (Exception ex){

                }
//                bookkey = storiesListener.getSnapshot(position++).getKey();
//                holder.setContent(model.getStory_content());
                holder.setAuthor(model.getAuthor());
                holder.setStoryNAme(model.getStoryNaMe());
//                holder.setPrice(model.getStory_price());
                holder.setMimgurl(getContext(),model.getLogoUrl());
                holder.setStodesc(model.getStrType());

                final int finalPosition = position;
                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getContext(),bookkey,Toast.LENGTH_LONG).show();

                        Stories stories = fbra.getItem(finalPosition);
                        showDetailsDialog(stories.getAuthor() ,stories.getSTDESC(), stories.getStory_price(),stories.getLogoUrl(),stories.getStoryNaMe(),stories.getStory_content(),(  stories.getStorySavingsrc() == null ? "AppCreationStory" : stories.getStorySavingsrc()),bookkey);
                    }
                });
            }
        };
        return fbra;
      }catch (Exception ex){
          Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
      }
        return null;
    }
    public void SelectType(Query query12){
         if(query12 == null){
            loading.setMessage("no stories for that type");
            loading.show();
        }
        final Dictionary<Object,Object> dictionary = new Dictionary<Object, Object>() {
            @Override
            public int size() {
                return this.size();
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Enumeration keys() {
                return null;
            }

            @Override
            public Enumeration elements() {
                return null;
            }

            @Override
            public Stories get(Object o) {
                return null;
            }

            @Override
            public Object put(Object o, Object o2) {
                return null;
            }

            @Override
            public Stories remove(Object o) {
                return null;
            }
        };
        assert query12 != null;
        final LruCache<String,Stories> mObjectCache = new LruCache<String,Stories>(count){
            @Override
            protected int sizeOf(String key, Stories value) {
                return super.sizeOf(key, value);
            }
        };
//
        SnapshotParser<Stories> snapshotParser= new SnapshotParser<Stories>() {
            @NonNull
            @Override
            public Stories parseSnapshot(@NonNull DataSnapshot snapshot) {
                return mObjectCache.get(snapshot.getKey());
            }
        };

        option = new FirebaseRecyclerOptions.Builder<Stories>()
                .setIndexedQuery(query12,osdb,snapshotParser)
//                .setQuery(query12, Stories.class)
                    .build();
            fbra = new FirebaseRecyclerAdapter<Stories, blogholder>(option) {

                @Override
                public blogholder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(layout.fragment_item, parent, false);


                    return new blogholder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull blogholder holder, int position, @NonNull final Stories model) {

                      mObjectCache.put("publisher",model);
                      mObjectCache.put("storyName",model);
                      mObjectCache.put("Img",model);
                      mObjectCache.put("Category",model);

                    bookkey = getRef(position).getKey();

                    dictionary.put("publisher",model.getAuthor());
                    dictionary.put("storyName",model.getStoryNaMe());
                    dictionary.put("Img",model.getLogoUrl());
                    dictionary.put("Category",model.getStrType());

//                bookkey = storiesListener.getSnapshot(position++).getKey();
//                holder.setContent(model.getStory_content());
                    holder.setAuthor(model.getAuthor());
                    holder.setStoryNAme(model.getStoryNaMe());
//                holder.setPrice(model.getStory_price());
                    holder.setMimgurl(getContext(), model.getLogoUrl());
                    holder.setStodesc(model.getStrType());

                    final int finalPosition = position;
                    holder.mview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Stories stories = fbra.getItem(finalPosition);
                            showDetailsDialog(stories.getAuthor(), stories.getSTDESC(), stories.getStory_price(), stories.getLogoUrl(), stories.getStoryNaMe(), stories.getStory_content(), (stories.getStorySavingsrc()== null ? "AppCreationStory" : stories.getStorySavingsrc()), bookkey);
                        }
                    });
                }
            };
        View parentLayout = null;
//        if(pdfquery1 ==null)
//        {
//            Toast.makeText(getContext(),"there are no books of this type", Toast.LENGTH_LONG);
//        }
//        assert pdfquery1 != null;
//        pdfoption = new FirebaseRecyclerOptions.Builder<PDFFILES>()
//                    .setQuery(pdfquery1, PDFFILES.class)
//                    .build();
//            fbpdfra = new FirebaseRecyclerAdapter<PDFFILES, blogholder>(pdfoption) {
//                @Override
//                public blogholder onCreateViewHolder(ViewGroup parent, int viewType) {
//                    View view = LayoutInflater.from(parent.getContext())
//                            .inflate(layout.pdflistitem, parent, false);
//                    return new blogholder(view);
//                }
//                @Override
//                protected void onBindViewHolder(@NonNull blogholder holder, int position, @NonNull final PDFFILES model) {
//                    holder.setPdfAuthor(model.getPdfAuthor());
//                    holder.setPdfstname(model.getPdfstoryNaMe());
//                    holder.setPdfcover(getContext(), model.getLogoUrl());
//                    holder.setPdfdesc(model.getStrType());
////                holder.setContent(model.getStory_content());
////                holder.setAuthor(model.getAuthor());
////                holder.setStoryNAme(model.getStoryNaMe());
//////                holder.setPrice(model.getStory_price());
////                holder.setMimgurl(getContext(),model.getLogoUrl());
////                holder.setContent(model.getSTDESC());
//
//                    final int finalPosition = position;
//                    holder.mview.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            PDFFILES pdfstories = fbpdfra.getItem(finalPosition);
//
//                            showDetailsDialog(pdfstories.getPdfAuthor(), pdfstories.getPdfSTDESC(), pdfstories.getPdfstory_price(), pdfstories.getLogoUrl(), pdfstories.getPdfstoryNaMe(), pdfstories.getStory_content(), "PDFSTORY", bookkey);
//                        }
//                    });
//                }
//            };
//

        recyclerView.setAdapter(fbra);
//        pdfrecyclerView.setAdapter(fbpdfra);
//        fbra.notifyDataSetChanged();
//        recyclerView.

        fbra.startListening();
//        fbpdfra.startListening();
        onStart();
    }
    public void searching(Query searchstring) {
        try {

            Thread.sleep(100);

        option = new FirebaseRecyclerOptions.Builder<Stories>()
                .setQuery(searchstring, Stories.class)
                .build();
        fbra = new FirebaseRecyclerAdapter<Stories, blogholder>(option) {

            @Override
            public blogholder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(layout.fragment_item, parent, false);


                return new blogholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull blogholder holder, final int position, @NonNull final Stories model) {
                final String bookkey = getRef(position).getKey();

//                holder.setContent(model.getStory_content());
                holder.setAuthor(model.getAuthor());
                holder.setStoryNAme(model.getStoryNaMe());
//                holder.setPrice(model.getStory_price());
                holder.setMimgurl(getContext(), model.getLogoUrl());
                holder.setStodesc(model.getStrType());

                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Stories stories = fbra.getItem(position);
                        showDetailsDialog(stories.getAuthor(), stories.getSTDESC(), stories.getStory_price(), stories.getLogoUrl(), stories.getStoryNaMe(), stories.getStory_content(), (stories.getStorySavingsrc() == null?"AppCreationStory":stories.getStorySavingsrc().trim()), bookkey);
                    }
                });
            }
        };

//        pdfoption = new FirebaseRecyclerOptions.Builder<PDFFILES>()
//                .setQuery(getPdfquery(), PDFFILES.class)
//                .build();
//        fbpdfra =new FirebaseRecyclerAdapter<PDFFILES, blogholder>(pdfoption) {
//
//            @Override
//            public blogholder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(layout.pdflistitem, parent, false);
//
//
//                return new blogholder(view);
//            }

//            @Override
//            protected void onBindViewHolder(@NonNull blogholder holder, final int position, @NonNull final PDFFILES model) {
//
//                final String bookkey = getRef(position).getKey();
//                holder.setPdfAuthor(model.getPdfAuthor());
//                holder.setPdfstname(model.getPdfstoryNaMe());
//                holder.setPdfcover(getContext(),model.getLogoUrl());
//                holder.setPdfdesc(model.getStrType());
////                holder.setContent(model.getStory_content());
////                holder.setAuthor(model.getAuthor());
////                holder.setStoryNAme(model.getStoryNaMe());
//////                holder.setPrice(model.getStory_price());
////                holder.setMimgurl(getContext(),model.getLogoUrl());
////                holder.setContent(model.getSTDESC());
//
//                holder.mview.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        PDFFILES pdfstories = fbpdfra.getItem(position);
//
//                        showDetailsDialog(pdfstories.getPdfAuthor() ,pdfstories.getPdfSTDESC(), pdfstories.getPdfstory_price(),pdfstories.getLogoUrl(),pdfstories.getPdfstoryNaMe(),pdfstories.getStory_content(),"PDFSTORY",bookkey);
//                    }
//                });
//
//            }
//
//
//        };
        recyclerView.setAdapter(fbra);
//        pdfrecyclerView.setAdapter(fbpdfra);}
            fbra.notifyDataSetChanged();

            fbra.startListening();
        onStart();
        }catch (Exception ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();

        }
        }
    public void loadmorestories(){
            option = new FirebaseRecyclerOptions.Builder<Stories>()
                    .setQuery(getQuery(), Stories.class)
                    .build();
            fbra =new FirebaseRecyclerAdapter<Stories, blogholder>(option) {

                @Override
                public blogholder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(layout.fragment_item, parent, false);


                    return new blogholder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull blogholder holder, final int position, @NonNull final Stories model) {
                    final String bookkey = getRef(position).getKey();

//                holder.setContent(model.getStory_content());
                    holder.setAuthor(model.getAuthor());
                    holder.setStoryNAme(model.getStoryNaMe());
//                holder.setPrice(model.getStory_price());
                    holder.setMimgurl(getContext(),model.getLogoUrl());
                    holder.setStodesc(model.getStrType());

                    holder.mview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Stories stories = fbra.getItem(position);
                            showDetailsDialog(stories.getAuthor() ,stories.getSTDESC(), stories.getStory_price(),stories.getLogoUrl(),stories.getStoryNaMe(),stories.getStory_content(),"AppCreationStory",bookkey);
                        }
                    });
                }
            };

            pdfoption = new FirebaseRecyclerOptions.Builder<PDFFILES>()
                    .setQuery(getPdfquery(), PDFFILES.class)
                    .build();
            fbpdfra =new FirebaseRecyclerAdapter<PDFFILES, blogholder>(pdfoption) {

                @Override
                public blogholder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(layout.pdflistitem, parent, false);


                    return new blogholder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull blogholder holder, final int position, @NonNull final PDFFILES model) {

                    final String bookkey = getRef(position).getKey();
                    holder.setPdfAuthor(model.getPdfAuthor());
                    holder.setPdfstname(model.getPdfstoryNaMe());
                    holder.setPdfcover(getContext(),model.getLogoUrl());
                    holder.setPdfdesc(model.getStrType());
//                holder.setContent(model.getStory_content());
//                holder.setAuthor(model.getAuthor());
//                holder.setStoryNAme(model.getStoryNaMe());
////                holder.setPrice(model.getStory_price());
//                holder.setMimgurl(getContext(),model.getLogoUrl());
//                holder.setContent(model.getSTDESC());

                    holder.mview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PDFFILES pdfstories = fbpdfra.getItem(position);

                            showDetailsDialog(pdfstories.getPdfAuthor() ,pdfstories.getPdfSTDESC(), pdfstories.getPdfstory_price(),pdfstories.getLogoUrl(),pdfstories.getPdfstoryNaMe(),pdfstories.getStory_content(),"PDFSTORY",bookkey);
                        }
                    });

                }


            };

            recyclerView.setAdapter(fbra);
            pdfrecyclerView.setAdapter(fbpdfra);
        };
        DatabaseReference commentsdb = mydatabase.getReference().child("comments");
    public void showDetailsDialog(final String Author1, final String Desc, final String price1, final String ImgUrl, final String storyNAME, final String storyCoNtEnT, final String StrySrc, final String bookkeys){
      StoryDetailsl =new AlertDialog.Builder(getContext());
      LayoutInflater inflater = getLayoutInflater();
      final View detaildialog = inflater.inflate(R.layout.detailalertdialog,null);

      StoryDetailsl.setView(detaildialog);
        boolean like = true;

      //TODO: Identify IDS
        if (FirebaseDatabase.getInstance().getReference().child("UserDetail").child(Author1) != null){
        Query paypalquery = FirebaseDatabase.getInstance().getReference().child("UserDetail").child(Author1);
paypalquery.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String paypalcliendid = String.valueOf(dataSnapshot.child("userpaypalacc").getValue());
if(paypalcliendid.equals(null)){
    Toast.makeText(getContext(),"you cant pay for this book",Toast.LENGTH_SHORT).show();
}
else{
//  clientpaypalid.equals(paypalcliendid);
}
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
        }else{
            Toast.makeText(getContext(),"THIS STORY HAVE BEEN DELETED",Toast.LENGTH_SHORT).show();
        }
        final ImageButton increase = (ImageButton)detaildialog.findViewById(id.Increasestrate);
        final ImageButton decrease = (ImageButton)detaildialog.findViewById(id.decreaseitsrate);
        final ImageButton report = (ImageButton)detaildialog.findViewById(id.reportbook);

        final RatingBar ratebar =(RatingBar)detaildialog.findViewById(R.id.stratebar);
        final ScrollView firstscroll = (ScrollView)detaildialog.findViewById(id.highlightscroll);
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
        ImageButton exitdialogbtn =(ImageButton)detaildialog.findViewById(id.closedialog);
        final Button sendcoms = (Button) detaildialog.findViewById(R.id.sendcombtn);
        final LinearLayout comlayout =(LinearLayout) detaildialog.findViewById(R.id.commentsview);
        final Button comments1 = (Button) detaildialog.findViewById(R.id.commentsbtn);

        final TextView[] comtername = {(TextView) detaildialog.findViewById(id.commentername)};
        final TextView[] comttxt = {(TextView) detaildialog.findViewById(id.commentstxt1)};
        ImageView comimg =(ImageView)detaildialog.findViewById(id.comterimg);
        final TextView comname = (TextView)detaildialog.findViewById(id.commentername);
        final Button checkingbtn =(Button)detaildialog.findViewById(id.checkingbtn);
        //        LinearLayout totalcom =(LinearLayout)detaildialog.findViewById(id.totalcomments);
        comlayout.setVisibility(GONE);
        final AlertDialog alertDialog = StoryDetailsl.create();
        price.setText(price1);
        checkExistedpaypalEmail(Author1);
        FirebaseDatabase.getInstance().getReference().child("Views").child(storyNAME+StrySrc).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())&& dataSnapshot.hasChild(storyNAME)) {
                checkingbtn.setEnabled(false);
                    checkingbtn.setText("You Have Checked it once");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        checkingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Views").child(storyNAME+StrySrc).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
            if(!dataSnapshot.hasChild(storyNAME+StrySrc))
               if(StrySrc.equals("AppCreationStory"))
               {
                   FirebaseDatabase.getInstance().getReference().child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//                   FirebaseDatabase.getInstance().getReference().child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//                   FirebaseDatabase.getInstance().getReference().child("Views").push().setValue(StrySrc);
                   Intent shortappstory = new Intent(getContext(),checkstoryfromact.class);
                   shortappstory.putExtra("CoNtEnT",storyCoNtEnT);
                   shortappstory.putExtra("StRyNaMe",storyCoNtEnT);
                   startActivity(shortappstory);

               }
               if(StrySrc.equals("PDFSTORY")){
                   FirebaseDatabase.getInstance().getReference().child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//                   FirebaseDatabase.getInstance().getReference().child("Views").push().setValue(storyNAME);
//                   FirebaseDatabase.getInstance().getReference().child("Views").push().setValue(StrySrc);

                   Intent shortpdfstory = new Intent(getContext(),checkedpdf.class);
                   shortpdfstory.putExtra("CoNtEnT",storyCoNtEnT);
                   shortpdfstory.putExtra("StRyNaMe",storyCoNtEnT);
                   startActivity(shortpdfstory);

               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

            }
        });
        Query newquery =mydatabase.getReference().child("UserDetail").child(auth.getDisplayName()).orderByChild("UserImg");
Query commentsquery = commentsdb.child(storyNAME).orderByChild("currentstoryname").equalTo(storyNAME).limitToLast(getCurrentamount());


        FirebaseListOptions<comments> options = new FirebaseListOptions.Builder<comments>()
                .setQuery(commentsquery, comments.class)
                .setLayout(R.layout.comments)
                .setLifecycleOwner(this)
                 .build();
            sendcoms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String curusercomment = comentry.getText().toString().trim();
                    DatabaseReference commentschild = commentsdb.child(storyNAME);
                    DatabaseReference commentschildrows = commentschild.push();
                    commentschildrows.child("username1").setValue(auth.getDisplayName());
                    commentschildrows.child("usercomment").setValue(curusercomment);
                    commentschildrows.child("user___Email").setValue(auth.getEmail());
                    commentschildrows.child("currentstoryname").setValue(storyNAME);
                }

            });
        Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.imgstyle);
        final RoundedBitmapDrawable imground = RoundedBitmapDrawableFactory.create(getResources(),img);
                    imground.isCircular();


        final TextView expandingtext =(TextView)detaildialog.findViewById(id.expandingtext);
        fbla= new FirebaseListAdapter<comments>(options) {
            double newamount1;

            /**
             *
             */

            @Override
                                            protected void populateView(final View v, comments model, int position) {
                                                 String usernames = model.getUserName1();
                                         String usercomment = model.getUsercomment();
                                               final TextView comtername =(TextView)v.findViewById(id.commentername);
                                         comtername.setText(usernames);
                                                 TextView comttxt = (TextView)v.findViewById(R.id.commentstxt1);
                                                         comttxt.setText(usercomment);
                                                final String usermail = model.getUser___Email();
                                                final User st = new User();

                                                 ((TextView)v.findViewById(id.commentername)).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                        public void onClick(View v) {

                                                            Bundle buncom =new Bundle();
                                                            newamount++;
                                                            buncom.putString("DisplayName",comtername.getText().toString());
                                                            buncom.putString("UserMail",usermail);
                                                alertDialog.dismiss();
                                                    FragmentManager fragmentManager1 = getFragmentManager();
                                                    othersprofiles_and_details usc = new othersprofiles_and_details();
                                                    usc.setArguments(buncom);

                                                    fragmentManager1.beginTransaction().replace(R.id.content, usc,null).addToBackStack(null).commit();
                                                }

                                                    });
                                                 Query userimgincmt = mydatabase.getReference().child("UserDetail").child(comtername.getText().toString());
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
                                                        ((ImageView) v.findViewById(id.comterimg)).setImageDrawable(imground);
                                                            Picasso.with(getContext()).load(ds).fit().into(((ImageView) v.findViewById(id.comterimg)));
////                                                        }
////                                                        .setImageURI(Uri.parse(commterimg));

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });


                                            }

            @Override
            public View getView(final int position, final View view, final ViewGroup viewGroup) {
               final View vvv=  super.getView(position, view, viewGroup);

                                                 expandingtext.setOnClickListener(new View.OnClickListener() {
//ViewGroup.LayoutParams params =new ViewGroup.LayoutParams();
                    @Override
                    public void onClick(View v) {
                        setcurrentamount(currentamount+5);

                        if(comlist.getHeight()>fbla.getView(0,view,viewGroup).getMeasuredHeight()) {
                            comlist.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, comlist.getMeasuredHeight()*4/3));

                        }                  }
                });
                while  (((fbla.getCount()*50*1.3)) <= comlist.getHeight()){
                    expandingtext.setVisibility(GONE);
break;
                }


                return vvv ;
            }

        };

//        comlist.getMeasuredHeightAndState();
        comlist.requestFocusFromTouch();
        justifyListViewHeightBasedOnChildren(comlist);
        comlist.setAdapter(fbla);
        comlist.setSelection(comlist.getAdapter().getCount());

        Query Ownedstoriescheck = osdb.child(storyNAME);

        Ownedstoriescheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String values = String.valueOf(dataSnapshot.child("Author").getValue());
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if (String.valueOf(ds.child("Author").getValue())!=auth.getDisplayName()) {
                        price.setEnabled(true);
                    }
                    if (String.valueOf(ds.child("Author").getValue())==auth.getDisplayName()) {
                    price.setEnabled(false);
                    price.setText("it's yours");
                }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

    });

        Query purchasedstoriescheck = psdb.orderByChild("story_name").equalTo(storyNAME);
        purchasedstoriescheck.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String values = String.valueOf(dataSnapshot.child("purchasername").getValue());
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.child("purchasername").getValue().equals(auth.getDisplayName())) {
                        price.setEnabled(false);
                        price.setText("purchased");
                    }}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        newquery.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String img = String.valueOf(dataSnapshot.getValue());
                if(img == null)
                {commenterimg.setBackground(null);}
                else{
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

                comlayout.setVisibility(GONE);
                comments1.setVisibility(View.VISIBLE);
            }
        });
        comments1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comlayout.setVisibility(View.VISIBLE);
                comments1.setVisibility(GONE);
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
        final Intent intent1 =new Intent(getContext(), PayPalService.class);
        intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config3);

    //        final PayPalConfiguration config2 = new PayPalConfiguration()
//                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
//                .clientId(clientpaypalid);
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mprogress = new ProgressDialog(getContext());
                 mprogress.setMessage("Purchasing...");
                 mprogress.show();
//                Thread showing_dialog = new Thread(){
//                    @Override
//                    public void run(){
                Query purchasername = myRef.child("story_name").orderByChild("Author").equalTo(auth.getDisplayName());
                Query purchasername1 = psdb.orderByChild("purchasername").equalTo(auth.getDisplayName());
                if (!Author1.equals(auth.getDisplayName()))
                 {


                    final AlertDialog.Builder select = new AlertDialog.Builder(getContext());
                    select.setPositiveButton("Paypal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addingstdataafterpaying(Author1, Desc, price1, ImgUrl,storyNAME ,storyCoNtEnT, StrySrc);
//                            Bundle stdata = new Bundle();
//                            stdata.putString("Authorize",Author1);
//                            stdata.putString("STORYNAME",storyNAME);
//                            stdata.putString("STCONTENT",storyCoNtEnT);
//                            stdata.putString("STDESCRIbE",Desc);
//                            stdata.putString("IMGURL",ImgUrl);
//                            stdata.putString("STSOURC",StrySrc);
                            amount_to_pay =String.valueOf(price1);
                            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount_to_pay),"USD",Author1 ,PayPalPayment.PAYMENT_INTENT_SALE);
//                            PayPalPayment payPalPayment1 = new PayPalPayment(new BigDecimal((90*Integer.parseInt(amount_to_pay))/100),"USD","Pay to"+Author1 ,PayPalPayment.PAYMENT_INTENT_SALE)
                            payPalPayment.payeeEmail(paypalconfig.paypal_Publiser_Email);
//                            payPalPayment;

                            Intent intent =new Intent(getContext(), PaymentActivity.class);
                            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config3);
                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,config3);
//                            intent.putExtra("Authorize",Author1);
//                            intent.putExtra("STORYNAME",storyNAME);
//                            intent.putExtra("STCONTENT",storyCoNtEnT);
//                            intent.putExtra("STDESCRIbE",Desc);
//                            intent.putExtra("IMGURL",ImgUrl);
//                            intent.putExtra("STSOURC",StrySrc);
                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
//                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment1);
                            startActivityForResult(intent,PAYPAL_REQUEST_CODE);
//ItemFragment ite = new ItemFragment();
//ite.setArguments(stdata);
                            dialog.dismiss();
                        }
                    });
                    select.setNegativeButton("CardPayment", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            paymentDialog(Author1, Desc, price1, ImgUrl, storyNAME,storyCoNtEnT,StrySrc,price);
                        dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog1 = select.create();
                    alertDialog1.show();
                    mprogress.dismiss();
//                  Toast.makeText(getContext(),"Story have been added successfully to your purchased Stories",Toast.LENGTH_LONG).show();


            }if (Author1.equals(auth.getDisplayName())){
                    Toast.makeText(getContext(),"You are the story publisher",Toast.LENGTH_LONG).show();

                }
//                while (Author1 == auth.getDisplayName() || purchasername != null || purchasername1 != null){   Toast.makeText(getContext(),"You are already the story author",Toast.LENGTH_LONG).show();
//                    break;}



                }

        });
final Query ratesquery = mystrateRef.orderByChild("STRYname").equalTo(storyNAME);
Query totalranks = mystrateRef.orderByKey().startAt(storyNAME);
//                Toast.makeText(getContext(),"You Rated Story By"+" "+ (int)rating+" "+ "stars",Toast.LENGTH_LONG).show();
ratesquery.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {


for(DataSnapshot ds: dataSnapshot.getChildren()) {
        float countsrate = Float.parseFloat(String.valueOf(ds.child("styrates").getValue()));
        totalcount += countsrate;
//       settotalcount(totalcount += countsrate);


//        settotalrate(totalcount / dataSnapshot.getChildrenCount());
        totalrate = (totalcount /dataSnapshot.getChildrenCount());
//        if (StrySrc.equals("AppCreationStory")) {
//                final float finalTotalrate = totalrate;
            final float finalTotalrate = getTotalrate();
            final float finalTotalcount = getTotalcount();
            ratebar.setRating(finalTotalrate);


//        }
//        if (StrySrc.equals("PDFSTORY")) {
//            FirebaseDatabase.getInstance().getReference().child("pdfStoriesdetails").child(storyNAME).child("stRating").setValue(String.valueOf(getTotalrate()));
//        }
        ratebar.setRating(getTotalrate());
//        Toast.makeText(getContext(), "You Rated Story By" + " " + getTotalrate() + " " + "stars", Toast.LENGTH_SHORT).show();
}
        myRef.child(storyNAME).child("stRating").setValue(String.valueOf(totalrate));

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});

        ratebar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                setrate(rating);
                ratingBar.setRating(rating);
                Toast.makeText(getContext(), "You Rated Story By" + " " + rating+ " " + "stars", Toast.LENGTH_SHORT).show();
                DatabaseReference Story_rate= mystrateRef.child(storyNAME+curAuthDN);
                Story_rate.child("STRYname").setValue(storyNAME);
                Story_rate.child("styrates").setValue(rating);
                Story_rate.child("userEmail").setValue(auth.getEmail());

            }
        });

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

//                DatabaseReference childs =  mystrateRef.child(storyNAME+auth.getDisplayName());
//                childs.child("STRYname").setValue(storyNAME);
//                childs.child("styrates").setValue(String.valueOf(rating));
//                childs.child("userEmail").setValue(auth.getEmail());
//            }

        storyNAme.setText(String.format("Story Name: %s", storyNAME));
        Authors.setText(String.format("author : %s", Author1));
        Descr.setText(String.format("story Description %s", Desc));
        price.setText(price1);
        alertDialog.show();
//
        booklikes= FirebaseDatabase.getInstance().getReference().child("Likes");
        bookdislikes= FirebaseDatabase.getInstance().getReference().child("disLikes");
        reports= FirebaseDatabase.getInstance().getReference().child("Reports");
        reports.keepSynced(true);
        booklikes.keepSynced(true);
        bookdislikes.keepSynced(true);
    report.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            increaserate=true;

            if(increaserate) {

                reports.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        booksreport(dataSnapshot, storyNAME);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        }
    });
    decrease.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            increaserate=true;

            if(increaserate) {

                bookdislikes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adddislike(dataSnapshot, storyNAME);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
            }
    });
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                increaserate=true;

if(increaserate)
{
        booklikes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addlike(dataSnapshot ,storyNAME);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
            }
        });
        reports.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(storyNAME).hasChild(auth.getDisplayName())) {
//                    report.setImageResource(R.color.BlackCo);

                } else if (!dataSnapshot.child(storyNAME).hasChild(auth.getDisplayName())) {
//                    report.setImageResource(R.color.Cyan);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bookdislikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(storyNAME).hasChild(auth.getDisplayName())) {
                    decrease.setImageResource(R.drawable.dislike);

                } else if (!dataSnapshot.child(storyNAME).hasChild(auth.getDisplayName())) {
                    decrease.setImageResource(R.drawable.dislike2);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        booklikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(storyNAME).hasChild(auth.getDisplayName())) {
                    increase.setImageResource(R.drawable.like_symbol);

                } else if (!dataSnapshot.child(storyNAME).hasChild(auth.getDisplayName())) {
                    increase.setImageResource(R.drawable.like_thumb);
                  }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        exitdialogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        fbla.startListening();

    }
    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
    @Override
    public void onPause(){

        super.onPause();
       }
    private void booksreport(DataSnapshot dataSnapshot, final String storyNAME) {
        if(dataSnapshot.child(storyNAME).hasChild(auth.getDisplayName())){

//            reports.child(storyNAME).child(auth.getDisplayName()).removeValue();
            reportcontent(storyNAME);

            myRef.child(storyNAME).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.child("Reports").exists()) {
                        int reportsCount = Integer.parseInt(String.valueOf(dataSnapshot.child("Reports").getValue()));

                        myRef.child(storyNAME).child("Reports").setValue(1);
                    }
                    else if (dataSnapshot.child("Reports").exists()) {
                        int reportsCount  = Integer.parseInt(String.valueOf(dataSnapshot.child("Reports").getValue()));
                        myRef.child(storyNAME).child("Reports").setValue(reportsCount + 1);
                   }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            increaserate =false;
        }else if(!dataSnapshot.child(storyNAME).hasChild(auth.getDisplayName())){
            reportcontent(storyNAME);
            reports.child(storyNAME).child(auth.getDisplayName()).setValue("RandomValue");
            myRef.child(storyNAME).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                int i = Integer.parseInt(String.valueOf(dataSnapshot.child("Reports").getValue()));
                myRef.child(storyNAME).child("Reports").setValue(i + 1);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            increaserate =false;
        }
    }

    private void adddislike(DataSnapshot dataSnapshot, final String storyNAME) {
        if(dataSnapshot.child(storyNAME).hasChild(auth.getDisplayName())){

            bookdislikes.child(storyNAME).child(auth.getDisplayName()).removeValue();

            myRef.child(storyNAME).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.child("disLikes").exists()) {
                        myRef.child(storyNAME).child("disLikes").setValue(0);
                    }
                    if (dataSnapshot.child("disLikes").exists()) {

                        int i = Integer.parseInt(String.valueOf(dataSnapshot.child("disLikes").getValue()));
                        if (i>0){
                        myRef.child(storyNAME).child("disLikes").setValue(i - 1);
                        }
                        }
                    }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            increaserate =false;
        }else if(!dataSnapshot.child(storyNAME).hasChild(auth.getDisplayName())){

            booklikes.child(storyNAME).child(auth.getDisplayName()).removeValue();
            bookdislikes.child(storyNAME).child(auth.getDisplayName()).setValue("RandomValue");
            myRef.child(storyNAME).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("disLikes") && dataSnapshot.hasChild("Likes")){
                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("disLikes").getValue()));
                    int ii = Integer.parseInt(String.valueOf(dataSnapshot.child("Likes").getValue()));

                    myRef.child(storyNAME).child("disLikes").setValue(i+1);
                    if(dataSnapshot.hasChild("likes")&& dataSnapshot.hasChild("dislikes") ) {

                        myRef.child(storyNAME).child("Likes").setValue(i + 1);
                        if(ii>0) {
                            myRef.child(storyNAME).child("disLikes").setValue(ii - 1);
                        }}else if(!dataSnapshot.hasChild("likes")&& dataSnapshot.hasChild("dislikes")){
                        myRef.child(storyNAME).child("likes").setValue(1);
                        myRef.child(storyNAME).child("dislikes").setValue(ii-1);
                    }else if(dataSnapshot.hasChild("likes")&& !dataSnapshot.hasChild("dislikes")){
                        myRef.child(storyNAME).child("likes").setValue(i+1);
                        myRef.child(storyNAME).child("dislikes").setValue(0);
                    }
                    else{
                        myRef.child(storyNAME).child("likes").setValue(1);
                        myRef.child(storyNAME).child("dislikes").setValue(0);
                    }
                    }
                    else{
                        if (!dataSnapshot.hasChild("disLikes"))
                        {myRef.child(storyNAME).child("disLikes").setValue(1);
                        }
                        if(!dataSnapshot.hasChild("Likes")){
                            myRef.child(storyNAME).child("Likes").setValue(0);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            increaserate =false;
        }
    }
    String[] Accounts ={"mnmfas@gmail.com", "mymeadd57@gmail.com"};

    public void reportcontent(final String storyNAME){
        Storyreports =new AlertDialog.Builder(getContext());
    LayoutInflater inflater = getLayoutInflater();
    final View reportdialog = inflater.inflate(layout.report,null);
    Storyreports.setView(reportdialog);
    final AlertDialog reportdialogs = Storyreports.create();
    final EditText reportcontent = (EditText)reportdialog.findViewById(id.reportcontent);
    Button reportacceptance = (Button) reportdialog.findViewById(id.reportsubmit);
    reportacceptance.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
   DatabaseReference rc = FirebaseDatabase.getInstance().getReference().child("ReportsContent").push();
            rc.child("Reporter").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            rc.child("storyname").setValue(storyNAME);
            rc.child("reportcontent").setValue(reportcontent.getText().toString().trim());
//            String subject = Subject.getText().toString();
            String Message = reportcontent.getText().toString();
            Intent intent= new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL,Accounts);
            intent.putExtra(Intent.EXTRA_SUBJECT,storyNAME+"Report");
            intent.putExtra(Intent.EXTRA_TEXT,Message);
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent,"choose an Email Client"));


        }
    });
    reportdialogs.show();

}
    public void addlike(DataSnapshot ds , final String storynaMe){
        if(ds.child(storynaMe).hasChild(auth.getDisplayName())){

        booklikes.child(storynaMe).child(auth.getDisplayName()).removeValue();
        myRef.child(storynaMe).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("Likes").exists()) {
                    myRef.child(storynaMe).child("Likes").setValue(0);
                }
                if (dataSnapshot.child("Likes").exists()) {

                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("Likes").getValue()));
                    myRef.child(storynaMe).child("Likes").setValue(i - 1);
                }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        increaserate =false;
    }else if(!ds.child(storynaMe).hasChild(auth.getDisplayName())){
        booklikes.child(storynaMe).child(auth.getDisplayName()).setValue("RandomValue");
        bookdislikes.child(storynaMe).child(auth.getDisplayName()).removeValue();

        myRef.child(storynaMe).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("disLikes") && dataSnapshot.hasChild("Likes")) {

                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("Likes").getValue()));
                    int ii = Integer.parseInt(String.valueOf(dataSnapshot.child("disLikes").getValue()));

                    if (dataSnapshot.hasChild("likes") && dataSnapshot.hasChild("dislikes")) {

                        myRef.child(storynaMe).child("Likes").setValue(i + 1);
                        if (ii > 0) {
                            myRef.child(storynaMe).child("disLikes").setValue(ii - 1);
                        }
                    } else if (!dataSnapshot.hasChild("likes") && dataSnapshot.hasChild("dislikes")) {
                        myRef.child(storynaMe).child("likes").setValue(1);
                        myRef.child(storynaMe).child("dislikes").setValue(ii - 1);
                    } else if (dataSnapshot.hasChild("likes") && !dataSnapshot.hasChild("dislikes")) {
                        myRef.child(storynaMe).child("likes").setValue(i + 1);
                        myRef.child(storynaMe).child("dislikes").setValue(0);
                    } else {
                        myRef.child(storynaMe).child("likes").setValue(1);
                        myRef.child(storynaMe).child("dislikes").setValue(0);
                    }
                }
                else{
                    if (!dataSnapshot.hasChild("disLikes"))
                    {myRef.child(storynaMe).child("disLikes").setValue(0);
                    }
                    if(!dataSnapshot.hasChild("Likes")){
                        myRef.child(storynaMe).child("Likes").setValue(1);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        increaserate =false;
    }
    }
    public void putimg(DataSnapshot ds ,String storynaMe){}
    String AuthoRs,DescriP,pRICe,IMGUrL,StorYNamE, StRContEnT,StrYsRc;

    public void addingstdataafterpaying(String Authors, String Descrip, String price11, String ImgUrl1,String STOryNAme ,String STRCOntEnT, String STRYSrC)
    {   this.AuthoRs =Authors;
        this.DescriP = Descrip;
        this.pRICe =price11;
        this.IMGUrL = ImgUrl1;
        this.StorYNamE = STOryNAme;
        this.StRContEnT = STRCOntEnT;
        this.StrYsRc = STRYSrC;
//        DatabaseReference psdbchild = psdb.push();
//
//        psdbchild.child("purchasername").setValue(auth.getDisplayName());
//        psdbchild.child("story_name").setValue(STOryNAme);
//        psdbchild.child("Story_Author").setValue(Authors);
//        psdbchild.child("Story_Desc").setValue(Descrip);
//        psdbchild.child("Story_Content").setValue(STRCOntEnT);
//        psdbchild.child("Story_ImgUrl").setValue(ImgUrl1);
//        psdbchild.child("StorySrc").setValue(STRYSrC);

    }

    private void paymentDialog(final String Author1, final String Desc, String price1, final String ImgUrl, final String storyNAME, final String stryContent , final String StrSrc, final Button prices){
        StoryDetailsl =new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View detaildialog2 = inflater.inflate(layout.paymentdialog,null);
        StoryDetailsl.setView(detaildialog2);
        final AlertDialog alertDialog2 = StoryDetailsl.create();
        alertDialog2.show();
        final CardForm cf = (CardForm)detaildialog2.findViewById(id.payingform);
        Button btnpay = (Button)detaildialog2.findViewById(id.btn_pay);
        btnpay.setText("Pay");
        cf.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                final AlertDialog.Builder verify  = new AlertDialog.Builder(getContext());
                verify.setTitle("Confirm Before Purchase");
                verify.setMessage("Card Name:"+cf.getCard().getName()+"\n"+"Card Number:" + cf.getCard().getNumber() +"\n" + "Card expiry Date: "+ cf.getCard().getExpMonth()+cf.getCard().getExpYear()
                +"\n"+ "Card CVC"+cf.getCard().getCVC()+"\n");
                verify.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference psdbchild = psdb.push();

                        psdbchild.child("purchasername").setValue(auth.getDisplayName());
                        psdbchild.child("story_name").setValue(storyNAME);
                        psdbchild.child("Story_Author").setValue(Author1);
                        psdbchild.child("Story_Desc").setValue(Desc);
                        psdbchild.child("Story_Content").setValue(stryContent);
                        psdbchild.child("Story_ImgUrl").setValue(ImgUrl);
                        psdbchild.child("StorySrc").setValue(StrSrc);
                        dialog.dismiss();
                        alertDialog2.dismiss();
                        prices.setEnabled(false);
                        prices.setText("purchased");

                        }
                });
                verify.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
                AlertDialog alertDialog2 = verify.create();
                alertDialog2.show();
            }
        });
    }

  @Override
    public void onStart() {
        super.onStart();
        fbra.startListening();
//        fbpdfra.startListening();
    }

    Bundle gtbund = getArguments();
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //                intent.putExtra("Authorize",Author1);
//                intent.putExtra("STORYNAME",storyNAME);
//                intent.putExtra("STCONTENT",storyCoNtEnT);
//                intent.putExtra("STDESCRIbE",Desc);
//                intent.putExtra("IMGURL",ImgUrl);
//                intent.putExtra("STSOURC",StrySrc);
//                data = getIntent();

//                String Auth = gtbund.getString("Authorize");
//                String SORNME = gtbund.getString("STORYNAME");
//                String STCNTT = gtbund.getString("STCONTENT");
//                String STDESCB = gtbund.getString("STDESCRIbE");
//                String IMAGEUL = gtbund.getString("IMGURL");
//                String STRSORC = gtbund.getString("STSOURC");

                if (confirmation != null) {
            String sate =confirmation.getProofOfPayment().getState();
            if(sate.equals("approved")){

                DatabaseReference psdbchild = psdb.push();
//    String AuthoRs,DescriP,pRICe,IMGUrL,StorYNamE, StRContEnT,StrYsRc;

                psdbchild.child("purchasername").setValue(auth.getDisplayName());
                psdbchild.child("story_name").setValue(StorYNamE);
                psdbchild.child("Story_Author").setValue(AuthoRs);
                psdbchild.child("Story_Desc").setValue(DescriP);
                psdbchild.child("Story_Content").setValue(StRContEnT);
                psdbchild.child("Story_ImgUrl").setValue(IMGUrL);
                psdbchild.child("StorySrc").setValue(StrYsRc);

                Toast.makeText(getContext(), "Payment approved"+"\n" +"Story have been added", Toast.LENGTH_SHORT).show();
                }
                else {Toast.makeText(getContext(), "error in payment", Toast.LENGTH_SHORT).show();}
                    //                    try {
//                        String paymentDetails = confirmation.toJSONObject().toString(4);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }else if(resultCode == Activity.RESULT_CANCELED){Toast.makeText(getContext(), "cancel", Toast.LENGTH_SHORT).show();

            }else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
                {
                Toast.makeText(getContext(), "Invalid", Toast.LENGTH_SHORT).show();
        }
    }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,
                                    MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.mainmenu, menu);
//        MenuItem item = menu.findItem(id.SearchIcon);
//        Context mContext = getContext();
//        assert mContext != null;
//        SearchManager searchManager = (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);
//        searchView = (SearchView) menu.findItem(id.SearchIcon).getActionView();
//        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//
//                return true;
//            }
//        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return true;
    }

    public void processpayment(){}

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
//    public static class blogholder extends RecyclerView.ViewHolder{}
class holderls extends ListViewAutoScrollHelper {
    public holderls(@NonNull ListView target) {
        super(target);
    }
}

    public static class blogholder extends RecyclerView.ViewHolder{
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
        public blogholder(View itemView) {
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
            pdfAuthor =(TextView)mview.findViewById(id.pdfAuthor);
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
            pdfprice =(TextView)mview.findViewById(id.pdfprice);
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
            pdfdesc =(TextView)mview.findViewById(id.pdfType);
            pdfdesc.setText("Type" +": "+ pdfdesc1);
        }

        public ImageView getPdfcover() {
            return pdfcover;
        }


        public void setPdfcover(final Context ctxs, final String pdfcover1) {
            pdfcover =(ImageView) mview.findViewById(id.pdfstory__img);
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
            pdfstname =(TextView)mview.findViewById(id.pdfstname);
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
           cover= (ImageView) mview.findViewById(id.story__img);
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
//           content = (TextView)mview.findViewById(R.id.description);
//           content.setText("Story Description" + mcontent);
       }

       public void setPrice(String sprice) {
           price= (TextView)mview.findViewById(id.storypricetxt);
           price.setText("Price :" + " " + sprice);
       }


       public void setAuthor(String author) {
           Authors = (TextView)mview.findViewById(id.Author);
           Authors.setText("Author :" + " " + author);
       }

       public void setStoryNAme(String storyNAMe) {
           storyNAme = (TextView)mview.findViewById(id.stname);
           storyNAme.setText("Story Name:"+" " + storyNAMe);

       }


       public void setStodesc(String stodesc1) {
           stodesc =(TextView)mview.findViewById(id.normaltype);
           stodesc.setText("Type" + stodesc1);
       }

       public void setStrate(String strate1) {
           Strate =(RatingBar)mview.findViewById(id.stratebar);
           Strate.setRating(Float.parseFloat(strate1));
       }
   }
}
