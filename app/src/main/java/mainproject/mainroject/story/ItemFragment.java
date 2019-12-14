package mainproject.mainroject.story;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.os.ConfigurationCompat;
import androidx.core.widget.ListViewAutoScrollHelper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
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
import java.util.Hashtable;
import java.util.Locale;

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
    private static final String TAG = "itemFragment";
    private static final String TAG4 = "get_Data";
    private static int UPDOWN = 0;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ArrayList<String> musername = new ArrayList<>();
    private OnListFragmentInteractionListener mListener;
    private ArrayAdapter<String> arrayAdapter;
    public static final int PAYPAL_REQUEST_CODE = 7171;
    String amount_to_pay="";
    protected static PayPalConfiguration config3 = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .acceptCreditCards(true)
            .clientId(paypalconfig.paypal_client_live_Id_key)
            .defaultUserEmail(paypalconfig.Live_paypal_Publiser_Email);

    private DatabaseReference booklikes;
    private DatabaseReference bookdislikes;
    private DatabaseReference reports;
    private AlertDialog.Builder Storyreports;
    int currentamount=5;
    float totalcount = 0;
    public static EditText SearchET;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner typespinner;
    private LinearLayout search_btns;
    float totalrate = 0;
    float rates;

    public void setcurrentamount(int currentamounts){
        this.currentamount = currentamounts;
    }
    public int getCurrentamount(){
        return currentamount;
    }
    private double newamount=1;

    final Dictionary<Object,Object> dictionary = new Hashtable<Object, Object>();
    private int total_items_to_Load=2;
    int mCurrentpage=1;

    private static int startingAt =1;
    private static int viewAmount =10;

    String[] stryMainCategory = {"Education","Movies","theatre","series","literature"};

    String[] literature= {"Drama","Fable","Fiction","Poetry","Novel","Non-fiction","Short story","Prose","Biography"
            ,"Science Fiction"
            ,"Essay","Autobiography","Fairy Tale","Legend","Horror fiction","Myth"
            ,"Satire","Children's literature","Speech","Fantasy","Humor","Fable"
            ,"Short Story","Realistic Fiction","Folklore","Historical Fiction","Horror","Tall Tale","Legend"
            ,"Mystery","Fiction in Verse"};

    String[] EducationSec = {"Language","Mathematical","Religion"
            ,"healthcare administration","graphic design","psychology", "accounting"
            ,"criminal justice","nursing"
            ,"computer science","engineering","business administration",
            "organizational skills","communication skills","analytical skills","detail oriented"
            ,"compassion","critical-thinking skills","patience"};

    String[] MoviesAndSeries={"Absurdist/surreal/whimsical","Action","Adventure","Comedy","Thriller","Drama","Epic","Horror","Adventure","Animation","Comedy","Crime"
            ,"Documentary","Biography","Family","Fantasy","Film-Noir","History",
            "Musical","Mystery","Romance","Sci-fi","Sport","War","Western","Eastern","MiddleEast"};

    String[] Theatres={"Tragedy","Drama","Fringe","Immersive","Melodrama","Autobiographicals","Comedy"
            ,"Historic Plays","Farce","Solo Theatre","Epic"};

    Dictionary<String,String[]> spinnerDividers =new Hashtable<String, String[]>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */;

    public ItemFragment() {
    }
    int SearchBoxVisiblity = View.GONE;
    int SearchBoxVisiblityVisible = View.VISIBLE;
    int SearchBoxVisiblityInVisible = View.INVISIBLE;

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
    DatabaseReference pdfosdb = mydatabase.getReference().child("pdfStoriesdetails");
    DatabaseReference CurUserLinks = mydatabase.getReference().child("UserDetail");
    FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
    String curAuthDN = auth.getDisplayName();
    Button btn[] = new Button[stryMainCategory.length];
    public boolean increaserate = false;
    public boolean decreasecreaserate = false;
    public boolean reportrate = false;
    ImageButton convertingSearch,close_search;
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
//    .startAt(bookkey).limitToFirst(viewAmount)
    Query query =FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByChild("storyNaMe");
    Query loadmoreQuery =FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByKey();

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
    boolean Found = false;

    private String storiesname= "";
    public void checkExistedpaypalEmail(String publishers){
        final Query PaypalEmailExistance = CurUserLinks.child(publishers);
                PaypalEmailExistance.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild("userpaypalacc")){

                            paypalconfig.paypal_Publiser_Email =dataSnapshot.child("userpaypalacc").getValue() == null? null : dataSnapshot.child("userpaypalacc").getValue().toString();
                            Found = true;

                        }else{
                            Found = false;

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



    }
//    RelativeLayout SearchBox;
    String passMainCatName="";
    static RelativeLayout SearchBox;
    static HorizontalScrollView CategorySearch;
    LinearLayout.LayoutParams linearlayoutParams;
    int count = 0;
    TextView nobookText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(layout.fragment_item_list, container, false);
//        String user_country0 = getContext().getResources().getConfiguration().getLocales().toString();
        String user_country1 = getContext().getResources().getConfiguration().locale.getDisplayCountry();
        String user_country2 = getContext().getResources().getConfiguration().locale.getISO3Country();
        String user_country3 = getContext().getResources().getConfiguration().locale.getDisplayLanguage();
//        String user_country4 = getContext().getResources().getConfiguration().locale.getDisplayScript();

        String user_country = getContext().getResources().getConfiguration().locale.getCountry();

    Locale loc = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);

//    Toast.makeText(getContext(),loc.getCountry()+" "+user_country +" "+" "+user_country1+" "+user_country2+" "+user_country3+" ",Toast.LENGTH_SHORT).show();
        // TODO: RecyclerView
        loading = new ProgressDialog(getContext());
        pdfrv =(LinearLayout)view.findViewById(R.id.pdffilesview);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        SearchBox = view.findViewById(R.id.relLayout1);
        SearchET =view.findViewById(R.id.input_search);
        CategorySearch = view.findViewById(id.CategoriesSearch);
        convertingSearch = view.findViewById(id.convertSeaching);
        typespinner = view.findViewById(id.searchSubCategories);
        search_btns = view.findViewById(id.search_btns);
//        close_search =view.findViewById(id.close_search);
        nobookText = view.findViewById(id.nobookText);
        /***** build LinearLayout Params ****/
        linearlayoutParams= new LinearLayout.LayoutParams(0,80,1.0f);
        linearlayoutParams.leftMargin = 2;



        /***** Add Dictionary Data***/
        spinnerDividers.put(stryMainCategory[0],EducationSec);
        spinnerDividers.put(stryMainCategory[1],MoviesAndSeries);
        spinnerDividers.put(stryMainCategory[2],Theatres);
        spinnerDividers.put(stryMainCategory[3],MoviesAndSeries);
        spinnerDividers.put(stryMainCategory[4],literature);


        scrollView = view.findViewById(id.itemfragscrollview);
        all=(Button)view.findViewById(R.id.All);
//        pdfrecyclerView.setHasFixedSize(true);
        recyclerView.setHasFixedSize(true);

//        #80FAFAFF
        /*****  Prepare Search buttons     ****/
        for (int i = 0;i<stryMainCategory.length;i++) {
            btn[i] = new Button(getContext());
            btn[i].setText(stryMainCategory[i]);
            btn[i].setId(i+872912);
            btn[i].setTextColor(Color.BLACK);
            btn[i].setBackgroundColor(Color.parseColor("#80FAFAFF"));
            btn[i].setShadowLayer(1,111,111,Color.BLACK);
            btn[i].setHighlightColor(Color.RED);
            btn[i].setTextSize(10);
            btn[i].setLayoutParams(linearlayoutParams);
            final int finalI = i;

            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spinnerAdapter =new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,spinnerDividers.get(stryMainCategory[finalI]));
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    passMainCatName = stryMainCategory[finalI];
                    typespinner.setAdapter(spinnerAdapter);
                    typespinner.setVisibility(View.VISIBLE);
                    try {

                        setQuery(FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByChild("StrType").equalTo(stryMainCategory[finalI]));

                        Thread.sleep(1000);
                        SelectType(getQuery());

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            search_btns.addView(btn[i]);
        }
        myRef.keepSynced(true);
        pdfosdb.keepSynced(true);
        scrollView.setFocusable(false);
        
        //TODO:SEARCH
        final LinearLayoutManager pdfrecyclerViewlayout= new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        final RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),3);
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
//        close_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {

//            }
//        });
        typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {

                    setQuery(FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByChild("StrCatSearchObj").equalTo((passMainCatName+spinnerDividers.get(passMainCatName)[i]).trim()));
//                    setPdfquery(FirebaseDatabase.getInstance().getReference().child("pdfStoriesdetails").orderByChild("StrType").equalTo("Politics"));

                    Thread.sleep(1000);
                    SelectType(getQuery());
//                    pdfrecyclerView.setVisibility(View.GONE);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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


                    dictionary.put("publisher",model.getPublishers());
                    dictionary.put("storyName",model.getStoryNaMe());
                    dictionary.put("Img",model.getLogoUrl());
                    dictionary.put("Category",model.getStrType());
                    dictionary.put("lastKey",getRef(position).getKey());

                    Log.d("listcount", String.valueOf(((Hashtable<Object, Object>) dictionary).elements()));
                    if(fbra.getItemCount()==0)
                    {
                        nobookText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(GONE);
                    }else{
                        nobookText.setVisibility(GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
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
                            bookkey = getRef(finalPosition).getKey();

                            showDetailsDialog(stories.getPublishers(),stories.getAuthor(), stories.getSTDESC(), stories.getStory_price(), stories.getLogoUrl(), stories.getStoryNaMe(), stories.getStory_content(), (stories.getStorySavingsrc() == null ? "AppCreationStory" : stories.getStorySavingsrc()), bookkey, new int[]{stories.getLikes(), stories.getDislikes(), stories.getReports()});
                        }
                    });
                }
            };

        recyclerView.setAdapter(fbra);


        SearchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "Searching: "+charSequence.toString());
                try{

                    setQuery(FirebaseDatabase.getInstance().getReference().child("StoriesDetails").orderByChild("storyNaMe").startAt(charSequence.toString()).endAt(charSequence.toString()+"\uf8ff"));
                    Thread.sleep(200);
                    searching(getQuery());


                }catch (Exception ex){
                    Log.e(TAG,ex.getMessage());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typespinner.setVisibility(View.GONE);
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


        /****
         * Control Back Button
         *
         */

//        view.setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                    if(i == keyEvent.KEYCODE_BACK)
//                    {
//                        if(SearchBox.getVisibility()!= View.GONE) {
//                            SearchBox.setVisibility(View.GONE);
//                            CategorySearch.setVisibility(View.VISIBLE);
//                        }
//                        return true;
//                    }
//                    return false;
//                }
//            });

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

                        Stories stories = fbra.getItem(finalPosition);
                        try{
                            bookkey = getRef(finalPosition).getKey();
                        }catch (Exception ignored){

                        }

                        showDetailsDialog(stories.getPublishers(),stories.getAuthor() ,stories.getSTDESC(), stories.getStory_price(),stories.getLogoUrl(),stories.getStoryNaMe(),stories.getStory_content(),(  stories.getStorySavingsrc() == null ? "AppCreationStory" : stories.getStorySavingsrc()),bookkey, new int[]{stories.getLikes(), stories.getDislikes(), stories.getReports()});
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
        assert query12 != null;

        option = new FirebaseRecyclerOptions.Builder<Stories>()
//                .setIndexedQuery(query12,osdb,snapshotParser)
                .setQuery(query12, Stories.class)
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

                    dictionary.put("publisher",model.getPublishers());
                    dictionary.put("storyName",model.getStoryNaMe());
                    dictionary.put("Img",model.getLogoUrl());
                    dictionary.put("Category",model.getStrType());
                    dictionary.put("lastKey",getRef(position).getKey());

                    Log.d("listcount", String.valueOf(((Hashtable<Object, Object>) dictionary).elements()));
                    if(fbra.getItemCount()<=0)
                    {
                        nobookText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(GONE);
                    }else{
                        nobookText.setVisibility(GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
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
                            bookkey = getRef(finalPosition).getKey();

                            showDetailsDialog(stories.getPublishers(),stories.getAuthor(), stories.getSTDESC(), stories.getStory_price(), stories.getLogoUrl(), stories.getStoryNaMe(), stories.getStory_content(), (stories.getStorySavingsrc()== null ? "AppCreationStory" : stories.getStorySavingsrc()), bookkey, new int[]{stories.getLikes(), stories.getDislikes(), stories.getReports()});
                        }
                    });
                }
            };
        View parentLayout = null;

        recyclerView.setAdapter(fbra);

        fbra.startListening();
        onStart();
    }
    private void searching(Query searchstring) {
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
                if(fbra.getItemCount()<=0)
                {
                    nobookText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(GONE);
                }else{
                    nobookText.setVisibility(GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
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
                        showDetailsDialog(stories.getPublishers(),stories.getAuthor(), stories.getSTDESC(), stories.getStory_price(), stories.getLogoUrl(), stories.getStoryNaMe(), stories.getStory_content(), (stories.getStorySavingsrc() == null?"AppCreationStory":stories.getStorySavingsrc().trim()), bookkey, new int[]{stories.getLikes(), stories.getDislikes(), stories.getReports()});
                    }
                });
            }

        };

        recyclerView.setAdapter(fbra);
            fbra.notifyDataSetChanged();

            fbra.startListening();
        onStart();
        }catch (Exception ex){
            Log.d(TAG, ex.getMessage());

        }
        }

        DatabaseReference commentsdb = mydatabase.getReference().child("comments");
    public void showDetailsDialog(final String publishers, final String Author1, final String Desc, final String price1, final String ImgUrl, final String storyNAME, final String storyCoNtEnT, final String StrySrc, final String bookkeys, int[] rankings){
        checkExistedpaypalEmail(publishers);
        final String[] publisherData= new String[6];


        currentamount=5;

        StoryDetailsl =new AlertDialog.Builder(getContext());
      LayoutInflater inflater = getLayoutInflater();
      final View detaildialog = inflater.inflate(R.layout.detailalertdialog,null);

      StoryDetailsl.setView(detaildialog);
        boolean like = true;

      //TODO: Identify IDS

        final ImageButton increase = (ImageButton)detaildialog.findViewById(id.Increasestrate);
        final ImageButton decrease = (ImageButton)detaildialog.findViewById(id.decreaseitsrate);
        final ImageButton report = (ImageButton)detaildialog.findViewById(id.reportbook);

        final RatingBar ratebar =(RatingBar)detaildialog.findViewById(R.id.stratebar);
        final ScrollView firstscroll = (ScrollView)detaildialog.findViewById(id.highlightscroll);
        ImageView cover = (ImageView) detaildialog.findViewById(R.id.story__img);
        TextView Descr = (TextView) detaildialog.findViewById(R.id.description);
        final Button price = (Button) detaildialog.findViewById(R.id.storypricetxt);
        final TextView publishersText = (TextView) detaildialog.findViewById(R.id.publishers);
        TextView Authors = (TextView) detaildialog.findViewById(id.Authors);
        TextView storyNAme = (TextView) detaildialog.findViewById(R.id.stname);
        if(!ImgUrl.isEmpty() && !ImgUrl.equals(null) && !ImgUrl.equals(" ")) {
            Picasso.with(getContext()).load(ImgUrl).fit().into(cover);
        }
        final TextView comtxt = (TextView) detaildialog.findViewById(R.id.commentstxt1);
        final ListView comlist =(ListView)detaildialog.findViewById(R.id.commentslist);
        final EditText comentry =(EditText)detaildialog.findViewById(R.id.usernewcomment);
        final ImageButton commenterimg =(ImageButton)detaildialog.findViewById(R.id.comerimg);
        final ImageButton exitcoms = (ImageButton) detaildialog.findViewById(R.id.cmtexitbtn);
        ImageButton exitdialogbtn =(ImageButton)detaildialog.findViewById(id.closedialog);
        final Button sendcoms = (Button) detaildialog.findViewById(R.id.sendcombtn);
        final LinearLayout comlayout =(LinearLayout) detaildialog.findViewById(R.id.commentsview);
        final Button comments1 = (Button) detaildialog.findViewById(R.id.commentsbtn);

        final Button checkingbtn =(Button)detaildialog.findViewById(id.checkingbtn);
        final TextView totallikes = (TextView) detaildialog.findViewById(id.totallikes);
        final TextView totaldislikes = (TextView) detaildialog.findViewById(id.totaldislikes);
        final TextView totalreports = (TextView) detaildialog.findViewById(id.totalreports);

        price.setText(price1);
        totallikes.setText(String.valueOf(rankings[0]));
        totaldislikes.setText(String.valueOf(rankings[1]));
        totalreports.setText(String.valueOf(rankings[2]));

        CurUserLinks.orderByKey().equalTo(publishers).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                publisherData [0] = String.valueOf(dataSnapshot.child(publishers).child("userpaypalacc").getValue());
                publisherData [1] = String.valueOf(dataSnapshot.child(publishers).getKey());
                publisherData [2] = String.valueOf(dataSnapshot.child(publishers).child("Email").getValue());
                publisherData [3] =String.valueOf(dataSnapshot.child(publishers).child("Name").getValue());
                publishersText.setText(String.format("publishers : %s", publisherData[3]));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        comlayout.setVisibility(GONE);
        final AlertDialog alertDialog = StoryDetailsl.create();
        price.setText(price1);
        FirebaseDatabase.getInstance().getReference().child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(bookkeys)) {
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
                FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
           if(dataSnapshot.hasChild("Views")){
            if(dataSnapshot.child("Views").hasChild(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
                if(!dataSnapshot.child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).hasChild(bookkeys)) {
                if (StrySrc.equals("AppCreationStory")) {
                    FirebaseDatabase.getInstance().getReference().child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child(bookkeys).setValue(true);
//                   FirebaseDatabase.getInstance().getReference().child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//                   FirebaseDatabase.getInstance().getReference().child("Views").push().setValue(StrySrc);
                    Intent shortappstory = new Intent(getContext(), checkstoryfromact.class);
                    shortappstory.putExtra("CoNtEnT", storyCoNtEnT);
                    shortappstory.putExtra("StRyNaMe", storyNAME);
                    shortappstory.putExtra("bookKey", bookkeys);
                    startActivity(shortappstory);

                }
                if (StrySrc.equals("PDFSTORY")) {
                    FirebaseDatabase.getInstance().getReference().child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child(bookkeys).setValue(true);

                    Intent shortpdfstory = new Intent(getContext(), checkedpdf.class);
                    shortpdfstory.putExtra("CoNtEnT", storyCoNtEnT);
                    shortpdfstory.putExtra("StRyNaMe", storyCoNtEnT);
                    shortpdfstory.putExtra("bookKey", bookkeys);

                    startActivity(shortpdfstory);

                }}else {
                Toast.makeText(getContext(),"you have already viewed this book",Toast.LENGTH_LONG).show();
            }
                }else{
                if (StrySrc.equals("AppCreationStory")) {
                    FirebaseDatabase.getInstance().getReference().child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child(bookkeys).setValue(true);
//                   FirebaseDatabase.getInstance().getReference().child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//                   FirebaseDatabase.getInstance().getReference().child("Views").push().setValue(StrySrc);
                    Intent shortappstory = new Intent(getContext(), checkstoryfromact.class);
                    shortappstory.putExtra("CoNtEnT", storyCoNtEnT);
                    shortappstory.putExtra("StRyNaMe", storyNAME);
                    shortappstory.putExtra("bookKey", bookkeys);
                    startActivity(shortappstory);

                }
                if (StrySrc.equals("PDFSTORY")) {
                    FirebaseDatabase.getInstance().getReference().child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child(bookkeys).setValue(true);

                    Intent shortpdfstory = new Intent(getContext(), checkedpdf.class);
                    shortpdfstory.putExtra("CoNtEnT", storyCoNtEnT);
                    shortpdfstory.putExtra("StRyNaMe", storyNAME);
                    shortpdfstory.putExtra("bookKey", bookkeys);

                    startActivity(shortpdfstory);

                }
                }
            }
            else{
               if (StrySrc.equals("AppCreationStory")) {
                   FirebaseDatabase.getInstance().getReference().child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child(bookkeys).setValue(true);
//                   FirebaseDatabase.getInstance().getReference().child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//                   FirebaseDatabase.getInstance().getReference().child("Views").push().setValue(StrySrc);
                   Intent shortappstory = new Intent(getContext(), checkstoryfromact.class);
                   shortappstory.putExtra("CoNtEnT", storyCoNtEnT);
                   shortappstory.putExtra("StRyNaMe", storyNAME);
                   shortappstory.putExtra("bookKey", bookkeys);
                   startActivity(shortappstory);

               }
               if (StrySrc.equals("PDFSTORY")) {
                   FirebaseDatabase.getInstance().getReference().child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child(bookkeys).setValue(true);

                   Intent shortpdfstory = new Intent(getContext(), checkedpdf.class);
                   shortpdfstory.putExtra("CoNtEnT", storyCoNtEnT);
                   shortpdfstory.putExtra("StRyNaMe", storyCoNtEnT);
                   shortpdfstory.putExtra("bookKey", bookkeys);

                   startActivity(shortpdfstory);

               }
            }

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

            }
        });

        Query newquery =mydatabase.getReference().child("UserDetail").child(auth.getDisplayName()).orderByChild("UserImg");
Query commentsquery = commentsdb.child(bookkeys).orderByChild("currentstoryname").equalTo(storyNAME).limitToLast(getCurrentamount());


        FirebaseListOptions<comments> options = new FirebaseListOptions.Builder<comments>()
                .setQuery(commentsquery, comments.class)
                .setLayout(R.layout.comments)
                .setLifecycleOwner(this)
                 .build();
            sendcoms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String curusercomment = comentry.getText().toString().trim();
                    DatabaseReference commentschild = commentsdb.child(bookkeys);
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
                final String userformalname = model.getUser___Email();

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
                 ((ImageView) v.findViewById(id.comterimg)).setImageDrawable(imground);
                 if(!ds.isEmpty() && ds.equals(null)){
                     ((ImageView) v.findViewById(id.comterimg)).setBackground(null);
                            Picasso.with(getContext()).load(ds).fit().into(((ImageView) v.findViewById(id.comterimg)));
                                                        }
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

                        } 
                         
                    }
                });

                if  (((fbla.getCount()*50*1.3)) <= comlist.getHeight()){
                    expandingtext.setVisibility(GONE);

                }


                return vvv ;
            }

        };

        comlist.requestFocusFromTouch();
        justifyListViewHeightBasedOnChildren(comlist);
        comlist.setAdapter(fbla);
        comlist.setSelection(comlist.getAdapter().getCount());

        if (FirebaseDatabase.getInstance().getReference().child("UserDetail").child(publishers) == null){
            Toast.makeText(getContext(),"THIS STORY publisher Not Registered Or Has Been Deleted",Toast.LENGTH_SHORT).show();
            price.setEnabled(false);
        }

        if (publishers.trim().equals(curAuthDN)) {
            price.setText("it's yours");

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            price.setEnabled(false);

        }

        Query purchasedstoriescheck = psdb.orderByChild("StoryKey").equalTo(bookkeys);
        purchasedstoriescheck.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildren().iterator().hasNext()) {
                    String pskey = String.valueOf(dataSnapshot.getChildren().iterator().next().getKey());

                    String values = String.valueOf(dataSnapshot.child(pskey).child("purchasername").getValue());
                    Log.d(TAG, String.valueOf(values) + " " + curAuthDN);
                    if (values != null) {
                        if (values.equals(curAuthDN)) {
                            price.setEnabled(false);
                            price.setText("purchased");
                        }
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        newquery.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String img = String.valueOf(dataSnapshot.getValue());
                if(img == null || img.isEmpty())
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

                if (Found) {

                    mprogress = new ProgressDialog(getContext());
                    mprogress.setMessage("Purchasing...");
                    mprogress.show();

                    Query purchasername = myRef.child("story_name").orderByChild("publishers").equalTo(auth.getDisplayName());
                    Query purchasername1 = psdb.orderByChild("purchasername").equalTo(auth.getDisplayName());


                        final AlertDialog.Builder select = new AlertDialog.Builder(getContext());
                        select.setPositiveButton("Paypal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addingstdataafterpaying(Author1,publishers, Desc, price1, ImgUrl, storyNAME, storyCoNtEnT, StrySrc,bookkeys);
                                PayPalPayment payPalPayment;
                                String user_country = getContext().getResources().getConfiguration().locale.getCountry();
                                amount_to_pay = String.valueOf(price1);
                                double priceinDouble =(Double.parseDouble(price1));
                                payPalPayment = new PayPalPayment(new BigDecimal(amount_to_pay), "USD", publishers, PayPalPayment.PAYMENT_INTENT_SALE);

//                                if (user_country.equals("EG")) {
//                                    amount_to_pay = String.valueOf((priceinDouble * 16.3));
//                                    payPalPayment = new PayPalPayment(new BigDecimal(amount_to_pay), "EGP", publishers, PayPalPayment.PAYMENT_INTENT_SALE);
//
//                                }else if(user_country.equals("GB")) {
//                                    amount_to_pay = String.valueOf((priceinDouble * 0.81));
//                                    payPalPayment = new PayPalPayment(new BigDecimal(amount_to_pay), "GBP", publishers, PayPalPayment.PAYMENT_INTENT_SALE);
//                                }

                                //                            PayPalPayment payPalPayment1 = new PayPalPayment(new BigDecimal((90*Integer.parseInt(amount_to_pay))/100),"USD","Pay to"+publishers ,PayPalPayment.PAYMENT_INTENT_SALE)
                                payPalPayment.payeeEmail(publisherData[0]);
//                            payPalPayment;

                                Intent intent = new Intent(getContext(), PaymentActivity.class);
                                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config3);
                                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, config3);
                                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);

                                startActivityForResult(intent, PAYPAL_REQUEST_CODE);

                                dialog.dismiss();
                            }
                        });
                        select.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog1 = select.create();
                        alertDialog1.show();
                        mprogress.dismiss();
                  Toast.makeText(getContext(),"Story have been added successfully to your purchased Stories",Toast.LENGTH_LONG).show();


//                while (publishers == auth.getDisplayName() || purchasername != null || purchasername1 != null){   Toast.makeText(getContext(),"You are already the story author",Toast.LENGTH_LONG).show();
//                    break;}


                }
            }
        });
        final boolean[] RateAbletoChange = {false};
final Query ratesquery = mystrateRef.orderByChild("bookkeys").equalTo(bookkeys);

//Query totalranks = mystrateRef.ch.startAt(bookkeys);
//                Toast.makeText(getContext(),"You Rated Story By"+" "+ (int)rating+" "+ "stars",Toast.LENGTH_LONG).show();
ratesquery.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {



for(DataSnapshot ds: dataSnapshot.getChildren()) {
        double countsrate = Double.parseDouble(String.valueOf(ds.child("styrates").getValue()));
        totalcount += countsrate;
        Log.d(TAG4,"checking rate Count" +String.valueOf(totalcount));
}

        totalrate = dataSnapshot.getChildrenCount()>0? (totalcount /dataSnapshot.getChildrenCount()):0;
        ratebar.setRating(totalrate);


        myRef.child(bookkeys).child("stRating").setValue(String.valueOf(totalrate));
        RateAbletoChange[0] =true;
        totalcount=0;
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});

        ratebar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(RateAbletoChange[0]) {
//                    setrate(rating);
                    ratingBar.setRating(rating);
                    DatabaseReference Story_rate = mystrateRef.child(bookkeys + curAuthDN);
                    Story_rate.child("STRYname").setValue(storyNAME);
                    Story_rate.child("styrates").setValue(rating);
                    Story_rate.child("userEmail").setValue(auth.getEmail());
                    Story_rate.child("bookkeys").setValue(bookkeys);
                }

            }
        });


        storyNAme.setText(String.format("Story Name: %s", storyNAME));
        Authors.setText(String.format("Authors : %s", Author1));
        Descr.setText(String.format("story Description: %s", Desc));
        alertDialog.show();
//
        booklikes= FirebaseDatabase.getInstance().getReference().child("likes");
        bookdislikes= FirebaseDatabase.getInstance().getReference().child("dislikes");
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
                        reportcontent(dataSnapshot,bookkeys);

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
                        adddislike(dataSnapshot, bookkeys);

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
                addlike(dataSnapshot ,bookkeys);

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
                if (dataSnapshot.child(bookkeys).hasChild(auth.getDisplayName())) {
//                    report.setImageResource(R.color.BlackCo);

                } else if (!dataSnapshot.child(bookkeys).hasChild(auth.getDisplayName())) {
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
                if (dataSnapshot.child(bookkeys).hasChild(auth.getDisplayName())) {
                    decrease.setImageResource(R.drawable.dislike);

                } else if (!dataSnapshot.child(bookkeys).hasChild(auth.getDisplayName())) {
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
                if (dataSnapshot.child(bookkeys).hasChild(auth.getDisplayName())) {
                    increase.setImageResource(R.drawable.like_symbol);

                } else if (!dataSnapshot.child(bookkeys).hasChild(auth.getDisplayName())) {
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
    private void booksreport(DataSnapshot dataSnapshot, final String rbookskeys) {
        if(dataSnapshot.child(rbookskeys).hasChild(auth.getDisplayName())){

//            reports.child(storyNAME).child(auth.getDisplayName()).removeValue();


            myRef.child(rbookskeys).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.child("Reports").exists()) {
                        if(!dataSnapshot.child("Reports").getValue().equals(null)) {
                            int reportsCount = Integer.parseInt(String.valueOf(dataSnapshot.child("Reports").getValue()));

                            myRef.child(rbookskeys).child("Reports").setValue(1);
                        }
                    }
                    else if (dataSnapshot.child("Reports").exists()) {
                        int reportsCount  = Integer.parseInt(String.valueOf(dataSnapshot.child("Reports").getValue()));
                        myRef.child(rbookskeys).child("Reports").setValue(reportsCount + 1);
                   }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            increaserate =false;
        }else if(!dataSnapshot.child(rbookskeys).hasChild(auth.getDisplayName())){
            reportcontent(dataSnapshot, rbookskeys);
            reports.child(rbookskeys).child(auth.getDisplayName()).setValue("RandomValue");
            myRef.child(rbookskeys).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                int i = Integer.parseInt(String.valueOf(dataSnapshot.child("Reports").getValue()));
                myRef.child(rbookskeys).child("Reports").setValue(i + 1);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            increaserate =false;
        }
    }

    private void adddislike(DataSnapshot dataSnapshot, final String rbookskeys) {
        if(dataSnapshot.child(rbookskeys).hasChild(auth.getDisplayName())){

            bookdislikes.child(rbookskeys).child(auth.getDisplayName()).removeValue();

            myRef.child(rbookskeys).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.child("dislikes").exists()) {
                        myRef.child(rbookskeys).child("dislikes").setValue(0);
                    }
                    if (dataSnapshot.child("dislikes").exists()) {

                        int i = Integer.parseInt(String.valueOf(dataSnapshot.child("dislikes").getValue()));
                        if (i>0){
                        myRef.child(rbookskeys).child("dislikes").setValue(i - 1);
                        }
                        }
                    }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            increaserate =false;
        }else if(!dataSnapshot.child(rbookskeys).hasChild(auth.getDisplayName())){

            booklikes.child(rbookskeys).child(auth.getDisplayName()).removeValue();
            bookdislikes.child(rbookskeys).child(auth.getDisplayName()).setValue("RandomValue");
            myRef.child(rbookskeys).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("dislikes") && dataSnapshot.hasChild("likes")){
                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("dislikes").getValue()));
                    int ii = Integer.parseInt(String.valueOf(dataSnapshot.child("likes").getValue()));

                    myRef.child(rbookskeys).child("dislikes").setValue(i+1);
                    if(dataSnapshot.hasChild("likes")&& dataSnapshot.hasChild("dislikes") ) {

                        myRef.child(rbookskeys).child("likes").setValue(i + 1);
                        if(ii>0) {
                            myRef.child(rbookskeys).child("dislikes").setValue(ii - 1);
                        }}else if(!dataSnapshot.hasChild("likes")&& dataSnapshot.hasChild("dislikes")){
                        myRef.child(rbookskeys).child("likes").setValue(1);
                        myRef.child(rbookskeys).child("dislikes").setValue(ii-1);
                    }else if(dataSnapshot.hasChild("likes")&& !dataSnapshot.hasChild("dislikes")){
                        myRef.child(rbookskeys).child("likes").setValue(i+1);
                        myRef.child(rbookskeys).child("dislikes").setValue(0);
                    }
                    else{
                        myRef.child(rbookskeys).child("likes").setValue(1);
                        myRef.child(rbookskeys).child("dislikes").setValue(0);
                    }
                    }
                    else{
                        if (!dataSnapshot.hasChild("dislikes"))
                        {myRef.child(rbookskeys).child("dislikes").setValue(1);
                        }
                        if(!dataSnapshot.hasChild("likes")){
                            myRef.child(rbookskeys).child("likes").setValue(0);
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
    String[] Accounts ={"storyorg55@gmail.com"};

    public void reportcontent(final DataSnapshot dataSnapshot, final String storyNAME){
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
            booksreport(dataSnapshot,storyNAME );


        }
    });
    reportdialogs.show();

}
    public void addlike(DataSnapshot ds , final String rbookskeys){
        if(ds.child(rbookskeys).hasChild(auth.getDisplayName())){

        booklikes.child(rbookskeys).child(auth.getDisplayName()).removeValue();
        myRef.child(rbookskeys).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("likes").exists()) {
                    myRef.child(rbookskeys).child("likes").setValue(0);
                }
                if (dataSnapshot.child("likes").exists()) {

                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("likes").getValue()));
                    myRef.child(rbookskeys).child("likes").setValue(i - 1);
                }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        increaserate =false;
    }else if(!ds.child(rbookskeys).hasChild(auth.getDisplayName())){
        booklikes.child(rbookskeys).child(auth.getDisplayName()).setValue("RandomValue");
        bookdislikes.child(rbookskeys).child(auth.getDisplayName()).removeValue();

        myRef.child(rbookskeys).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("dislikes") && dataSnapshot.hasChild("likes")) {

                    int i = Integer.parseInt(String.valueOf(dataSnapshot.child("likes").getValue()));
                    int ii = Integer.parseInt(String.valueOf(dataSnapshot.child("dislikes").getValue()));

                    if (dataSnapshot.hasChild("likes") && dataSnapshot.hasChild("dislikes")) {

                        myRef.child(rbookskeys).child("likes").setValue(i + 1);
                        if (ii > 0) {
                            myRef.child(rbookskeys).child("dislikes").setValue(ii - 1);
                        }
                    } else if (!dataSnapshot.hasChild("likes") && dataSnapshot.hasChild("dislikes")) {
                        myRef.child(rbookskeys).child("likes").setValue(1);
                        myRef.child(rbookskeys).child("dislikes").setValue(ii - 1);
                    } else if (dataSnapshot.hasChild("likes") && !dataSnapshot.hasChild("dislikes")) {
                        myRef.child(rbookskeys).child("likes").setValue(i + 1);
                        myRef.child(rbookskeys).child("dislikes").setValue(0);
                    } else {
                        myRef.child(rbookskeys).child("likes").setValue(1);
                        myRef.child(rbookskeys).child("dislikes").setValue(0);
                    }
                }
                else{
                    if (!dataSnapshot.hasChild("dislikes"))
                    {myRef.child(rbookskeys).child("dislikes").setValue(0);
                    }
                    if(!dataSnapshot.hasChild("likes")){
                        myRef.child(rbookskeys).child("likes").setValue(1);
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
    String AuthoRs,stpublishers,DescriP,pRICe,IMGUrL,StorYNamE, StRContEnT,StrYsRc,bookskeys;

    public void addingstdataafterpaying(String Authors,String StPublishers, String Descrip, String price11, String ImgUrl1,String STOryNAme ,String STRCOntEnT, String STRYSrC,String bookkey)
    {   this.AuthoRs =Authors;
        this.stpublishers = StPublishers;
        this.DescriP = Descrip;
        this.pRICe =price11;
        this.IMGUrL = ImgUrl1;
        this.StorYNamE = STOryNAme;
        this.StRContEnT = STRCOntEnT;
        this.StrYsRc = STRYSrC;
        this.bookkey =bookskeys;
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

    private void paymentDialog(final String publishers, final String Desc, String price1, final String ImgUrl, final String storyNAME, final String stryContent , final String StrSrc, final Button prices){
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
                        psdbchild.child("Story_Author").setValue(publishers);
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


                if (confirmation != null) {
            String sate =confirmation.getProofOfPayment().getState();
            if(sate.equals("approved")){

                DatabaseReference psdbchild = psdb.push();
//    String AuthoRs,DescriP,pRICe,IMGUrL,StorYNamE, StRContEnT,StrYsRc;

                psdbchild.child("purchasername").setValue(auth.getDisplayName());
                psdbchild.child("story_name").setValue(StorYNamE);
                psdbchild.child("Story_Author").setValue(AuthoRs);
                psdbchild.child("Story_Publishers").setValue(stpublishers);
                psdbchild.child("Story_Desc").setValue(DescriP);
                psdbchild.child("Story_Content").setValue(StRContEnT);
                psdbchild.child("Story_ImgUrl").setValue(IMGUrL);
                psdbchild.child("StorySrc").setValue(StrYsRc);
                psdbchild.child("StoryKey").setValue(bookskeys);


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
//            DisplayMetrics displayMetrics = new DisplayMetrics();
//            ((Activity) ctxs).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//            int screenwidth = displayMetrics.widthPixels;
//            if(screenwidth <= 450)
//            {
//                pdfcover.setMinimumWidth(((screenwidth/3)-7));
//                pdfcover.setMinimumHeight((pdfcover.getWidth()* 4/3));
//            }
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
          if(!mimgurl.isEmpty() && !mimgurl.equals(" ") && !mimgurl.equals(null)) {
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
//           Authors = (TextView)mview.findViewById(id.Author);
//           Authors.setText("Author :" + " " + author);
       }

       public void setStoryNAme(String storyNAMe) {
           storyNAme = (TextView)mview.findViewById(id.stname);
           storyNAme.setText(storyNAMe);

       }


       public void setStodesc(String stodesc1) {
//           stodesc =(TextView)mview.findViewById(id.normaltype);
//           stodesc.setText(stodesc1);
       }

       public void setStrate(String strate1) {
           Strate =(RatingBar)mview.findViewById(id.stratebar);
           Strate.setRating(Float.parseFloat(strate1));
       }
   }
}
