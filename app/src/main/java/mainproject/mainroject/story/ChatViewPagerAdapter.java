package mainproject.mainroject.story;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
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
