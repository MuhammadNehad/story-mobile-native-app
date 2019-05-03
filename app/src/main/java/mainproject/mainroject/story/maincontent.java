package mainproject.mainroject.story;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.app.SearchManager;
import android.support.v7.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;

import java.util.ArrayList;

public class maincontent extends AppCompatActivity {
    private static final String TAG = "Value";
    private ListView data;
    SearchView searchView;
    Toolbar tb;
    protected static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(paypalconfig.paypal_client_Id);

    private ArrayList<String> datas = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager = getSupportFragmentManager();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

             fragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction.replace(R.id.content, new ItemFragment(),null).addToBackStack(null).commit();
                    return true;
                case R.id.navigation_dashboard:
                    fragmentTransaction.replace(R.id.content, new ChatTabs(),null).addToBackStack(null).commit();
                    return true;
                case R.id.profile_nav:
                    fragmentTransaction.replace(R.id.content, new Profile(),null).addToBackStack(null).commit();
                    return true;
            }
            return true;
}



    };

    public maincontent() {
    }
home h = new home();
    ItemFragment itemFragment= new ItemFragment();

    String img;
String storytitle,price;
ItemFragment it = new ItemFragment();

    FrameLayout fl;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincontent);
//
//        if(savedInstanceState == null){
//    h.viewpa.editText.setHint("Type Your Story Here");
//}else{
//            String content  = savedInstanceState.getString("contentfromapp");
//
//    h.viewpa.editText.setText(content);
//}
        mDatabase = FirebaseDatabase.getInstance().getReference("UserDetail");

        fl =(FrameLayout)findViewById(R.id.content);

        while (h.story_Name != null) {
            storytitle = h.story_Name.toString();
            break;
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        data.setAdapter(arrayAdapter);
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("UserDetail");
        Intent intent2 = new Intent(this, PayPalService.class);
        intent2.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent2);
//        Intent intent =getIntent();

        //mTextMessage.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {

//        mAuth.getInstance().signOut(getApplicationContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
//         public void onComplete(@NonNull Task<Void> task) {
//
//             Intent intent = new Intent(maincontent.this, LoginForm.class);
//             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//             intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//             startActivity(intent);
//             finish();
//         // ...
//     }
//    });


//    }
//});
//        FirebaseAuth.getInstance().signOut();
        fragmentManager.beginTransaction().replace(R.id.content, new ItemFragment()).commit();

        Toolbar tb =(Toolbar)findViewById(R.id.tb);
        setSupportActionBar(tb);
        tb.setTitle("Story");
        tb.setSubtitle("Books Warehouse");

    }
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.mainmenu,menu);
        MenuItem searchitem =menu.findItem(R.id.SearchIcon);
        searchView = (SearchView) searchitem.getActionView();
        searchView.setQueryHint("Search Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
////         searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) maincontent.this);
              return true;
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        SharedPreferences sharedPreferences=getSharedPreferences("MyPref",0);
//    SharedPreferences.Editor editor =sharedPreferences.edit();
//    editor.putString("nameValue",h.Storyname.getText().toString());
//        editor.putString("nameValue",h.Storyname.getText().toString());
//        editor.putString("nameValue",h.$pricebox.getText().toString());
//editor.commit();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg=" ";
        switch (item.getItemId()) {
            case R.id.add_icon:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.content, new home(),null).addToBackStack(null).commit();
                return true;
            case R.id.signout_bar:
                mAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginForm.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.update_bar:

                fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.content, new updateProfile(),null).addToBackStack(null).commit();
                return true;
            case R.id.SearchIcon:
                    msg="search";
                return  true;
        }
        return true;
    }
viewpageradapter viewpas = new viewpageradapter(this);
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//            outState.putString(fl.getFocusedChild());

        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("contentfromapp", h.viewpa.editText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
       String content  = savedInstanceState.getString("contentfromapp");
//       viewpas.editText.setText(content);
    }
}
