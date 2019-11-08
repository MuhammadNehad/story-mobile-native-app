package mainproject.mainroject.story;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.TokenWatcher;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.app.SearchManager;
import android.support.v7.widget.SearchView;
import android.widget.RelativeLayout;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import static mainproject.mainroject.story.home.maxSize;

public class maincontent extends AppCompatActivity {
    private static final String TAG = "Value";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1 ;
    private ListView data;
    SearchView searchView;
    Toolbar tb;
    protected static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .acceptCreditCards(true)
            .clientId(paypalconfig.paypal_client_live_Id_key);

    private ArrayList<String> datas = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private LocationManager locationManager;
    String country_name;
    Geocoder geocoder;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager = getSupportFragmentManager();
    RelativeLayout SearchBox;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            fragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction.replace(R.id.content, new ItemFragment(), "itemfrags").addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

                    return true;
                case R.id.navigation_dashboard:
                    fragmentTransaction.replace(R.id.content, new ChatTabs(), null).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                    return true;
                case R.id.profile_nav:
                    fragmentTransaction.replace(R.id.content, new Profile(), null).addToBackStack(null).commit();
                    return true;
            }
            return true;
        }


    };

    public maincontent() {
    }
    final FirebaseDatabase mydatabase=FirebaseDatabase.getInstance();
    final DatabaseReference myRef = mydatabase.getReference().child("UserDetail");
    FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
    String currentUDN = currentuser != null ? currentuser.getDisplayName() : null;

    public static void getCurUserDetails()
    {
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUDN = currentuser != null ? currentuser.getDisplayName() : null;

        FirebaseDatabase.getInstance().getReference().child("UserDetail").child(currentUDN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("UserData", String.valueOf(dataSnapshot.getValue()));

                updateProfile.UserData.put("Name", (String) dataSnapshot.child("Name").getValue());
                updateProfile.UserData.put("Email", (String) dataSnapshot.child("Email").getValue());
                updateProfile.UserData.put("paypalAcc", (String) dataSnapshot.child("userpaypalacc").getValue());
                updateProfile.UserData.put("Phone", (String) dataSnapshot.child("PhoneNumber").getValue());
                updateProfile.UserData.put("filledStorageSize", String.valueOf(dataSnapshot.child("storageUserSize").getValue()));
                updateProfile.UserData.put("MaxStorageSize", String.valueOf(dataSnapshot.child("maxUserStorageSize").getValue()));
                Log.d("UserData", String.valueOf(updateProfile.UserData.elements()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void  calculateUserStorageSize(){
        myRef.child(currentUDN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("storageUserSize")) {
                    long sizeOfuserStorage = Long.parseLong(String.valueOf(dataSnapshot.child("storageUserSize").getValue()));
//                    maxUserStorageSize
                    if(dataSnapshot.hasChild("maxUserStorageSize")) {


                        long maxsizeOfuserStorage = Long.parseLong(String.valueOf(dataSnapshot.child("maxUserStorageSize").getValue()));

                        maxSize = maxsizeOfuserStorage - sizeOfuserStorage;
                        Log.d("FileSIZE", String.valueOf(maxSize) + " " + String.valueOf(sizeOfuserStorage));
                    }else{
                        maxSize = maxSize- sizeOfuserStorage;
                        myRef.child(currentUDN).child("maxUserStorageSize").setValue(1024);

                    }
                }else{
                    myRef.child(currentUDN).child("storageUserSize").setValue(0);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    home h = new home();
    String img;
    String storytitle, price;
    ItemFragment it = new ItemFragment();
    //EditText SearchET;
    FrameLayout fl;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincontent);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(getApplicationContext());
        getCurUserDetails();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserDetail");
        calculateUserStorageSize();
        for (String provider :
                locationManager.getAllProviders()) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
                {

                }
                else{
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION


                    );
                }
                Toast.makeText(this,String.valueOf(provider),Toast.LENGTH_LONG).show();

                continue;
            }
            @SuppressWarnings("ResourseType") Location location = locationManager.getLastKnownLocation(provider);


            if(location != null)
            {
                try{
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if(addresses != null && addresses.size()>0)
                    {
                        country_name =addresses.get(0).getCountryName();
                        Log.d(TAG,"Location: "+country_name);
                    }
                    Toast.makeText(this,String.valueOf(addresses) ,Toast.LENGTH_LONG).show();

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
            Log.d(TAG,"Location ");
        }

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
//        intent2.putExtra(PayPalService.EXTRA_RECEIVER_EMAIL,config);

        startService(intent2);
        fragmentManager.beginTransaction().replace(R.id.content, new ItemFragment(),"itemsFrag").commit();

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
                finish();
                return true;
            case R.id.update_bar:

                fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.content, new updateProfile(),null).addToBackStack(null).commit();
                return true;

//            case R.id.about_us:
//                Intent intent1 = new Intent(this,aboutapp.class);
//                startActivity(intent1);
//                return true;
            case R.id.contact_us:
                Intent intent2 = new Intent(this,contact_Us.class);
                startActivity(intent2);
                return true;

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:{
                if(grantResults.length>0 &&  grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG,"Permission Granted");
                }else{
                    Log.d(TAG,"Permission Failed");

                }
                return;
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
       String content  = savedInstanceState.getString("contentfromapp");
//       viewpas.editText.setText(content);
    }
}
