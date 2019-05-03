package mainproject.mainroject.story;

import android.app.ActionBar;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;


public class ChatTabs extends Fragment {
    TabLayout changingchatView;
    ViewPager chatviewspager;
    ChatViewPagerAdapter pagerAdapter;
    public ChatTabs() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View chattabs=inflater.inflate(R.layout.fragment_chat_tabs, container, false);
        changingchatView=(TabLayout)chattabs.findViewById(R.id.chattabs);
        chatviewspager=(ViewPager) chattabs.findViewById(R.id.chatviewspager);
        pagerAdapter = new ChatViewPagerAdapter(getChildFragmentManager(),changingchatView.getTabCount());
//
//        changingchatView.addTab(changingchatView.newTab().setText("Public"));
//        changingchatView.addTab(changingchatView.newTab().setText("Private"));
//        changingchatView.addTab(changingchatView.newTab().setText("Groups"));

        changingchatView.setTabGravity(TabLayout.GRAVITY_FILL);

        pagerAdapter.AddFragment(new ChatRoom(),"Public");
        pagerAdapter.AddFragment(new pmview(),"Private");
        pagerAdapter.AddFragment(new GroupMsgsView(),"Groups");
        chatviewspager.setAdapter(pagerAdapter);
        changingchatView.setupWithViewPager(chatviewspager);
//        ActionBar ab1=getActivity().getActionBar();
//        ab1.addTab(ab1.newTab().setText("Public").setTabListener((ActionBar.TabListener) getContext()));
//        ab1.addTab(ab1.newTab().setText("Private").setTabListener((ActionBar.TabListener) getContext()));
//        ab1.addTab(ab1.newTab().setText("Groups").setTabListener((ActionBar.TabListener) getContext()));

        chatviewspager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(changingchatView));
        changingchatView.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                    chatviewspager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return chattabs;
    }


}
