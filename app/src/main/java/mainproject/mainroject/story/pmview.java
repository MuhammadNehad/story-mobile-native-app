package mainproject.mainroject.story;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class pmview extends Fragment {

    public pmview() {
        // Required empty public constructor
    }
    TextView comingsoontxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_pmview, container, false);
        // Inflate the layout for this fragment
        comingsoontxt =(TextView) view.findViewById(R.id.comingsoon1);
        return view;
    }

}
