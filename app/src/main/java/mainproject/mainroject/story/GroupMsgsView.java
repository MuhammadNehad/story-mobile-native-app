package mainproject.mainroject.story;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupMsgsView extends Fragment {


    public GroupMsgsView() {
        // Required empty public constructor
    }
    TextView comingsoontxt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_groupview, container, false);
        comingsoontxt =(TextView) view.findViewById(R.id.comingsoon2);

        return view;
    }

}
