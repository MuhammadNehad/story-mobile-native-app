package mainproject.mainroject.story;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;

/**
 * Created by UNiversaL on 4/7/2018.
 */

public class addpaymentmethod  extends Fragment{
    public  addpaymentmethod(){}
    Spinner paymentmethtypes;
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(paypalconfig.paypal_client_Id);
String paydetail;
    String[] typesnames = {"Paypal", "CreditCards"};
    customadapter ca;
    int[] typeslogo = {R.drawable.ic_local_parking_24dp,R.drawable.ic_local_parking_24dp};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View paymeth = inflater.inflate(R.layout.addingpaymentmethod, container, false);
        paymentmethtypes =(Spinner)paymeth.findViewById(R.id.paytype);
        ca =new customadapter(getContext(),typesnames,typeslogo);
        paymentmethtypes.setAdapter(ca);
        paymentmethtypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(),typesnames[position],Toast.LENGTH_LONG).show();
    if(typesnames[position] == "Paypal")
    {

    }if(typesnames[position] == "CreditCards"){}
            }
        });


        return paymeth;
    }
    public class customadapter extends ArrayAdapter<String> {
        Context cx;
        String[] names;
        int[] imgs;

         public customadapter(@NonNull Context context, String[] names, int[] imgs) {
             super(context, R.layout.payspinnercont);
             this.cx = context;
             this.names = names;
             this.imgs = imgs;
         }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row= inflater.inflate(R.layout.payspinnercont,null);
                TextView txt = (TextView)row.findViewById(R.id.paytext);
                ImageView img =(ImageView)row.findViewById(R.id.paylogo);
                txt.setText(names[position]);
                img.setImageResource(imgs[position]);

            return row;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row= inflater.inflate(R.layout.payspinnercont,null);
            TextView txt = (TextView)row.findViewById(R.id.paytext);
            ImageView img =(ImageView)row.findViewById(R.id.paylogo);
            txt.setText(names[position]);
            img.setImageResource(imgs[position]);

            return row;
        }
    }
}
