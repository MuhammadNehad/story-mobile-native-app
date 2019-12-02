package mainproject.mainroject.story;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatViewPagerAdapter extends FragmentStatePagerAdapter {
    Map<Integer,String> mfragmentTags;
    FragmentManager mfragmentmanager;
    private final List<Fragment> fragmentList=new ArrayList<>();
    private final List<String> fragmentslistTitles=new ArrayList<>();
    int numoftabs;
    Context context;
//    private LayoutInflater inflater;

    ChatViewPagerAdapter(FragmentManager childFragmentManager, int tabCount) {
        super(childFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentslistTitles.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentslistTitles.get(position);
    }

    public void AddFragment(Fragment fragment,String Title){
        fragmentList.add(fragment);
        fragmentslistTitles.add(Title);
    }
}
