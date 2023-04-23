package rcdiploma.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Switch;

import com.google.android.material.tabs.TabLayout;

public class TabDemoActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_demo);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.tab_viewpager);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        viewPager.setAdapter(new TabDemoAdapter(getSupportFragmentManager()));
    }

    private class TabDemoAdapter extends FragmentPagerAdapter {

        TabDemoAdapter(FragmentManager manager) {
            super(manager);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Chat";
                case 1:
                    return "Status";
                case 2:
                    return "Call";
                case 3:
                    return "Chat 1";
                case 4:
                    return "Status 1";
                case 5:
                    return "Call 1";
                case 6:
                    return "Chat 2";
                case 7:
                    return "Status 2";
                case 8:
                    return "Call 2";
            }
            return super.getPageTitle(position);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new ChatFragment();
                case 1:
                    return new StatusFragment();
                case 2:
                    return new CallFragment();
                case 3:
                    return new ChatFragment();
                case 4:
                    return new StatusFragment();
                case 5:
                    return new CallFragment();
                case 6:
                    return new ChatFragment();
                case 7:
                    return new StatusFragment();
                case 8:
                    return new CallFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 9;
        }
    }

}