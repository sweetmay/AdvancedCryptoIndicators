package com.sweetmay.advancedcryptoindicators;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.sweetmay.advancedcryptoindicators.Fragments.FragmentBounce;
import com.sweetmay.advancedcryptoindicators.Fragments.FragmentFearGreed;
import com.sweetmay.advancedcryptoindicators.Fragments.FragmentPagerAdapter;
import com.sweetmay.advancedcryptoindicators.Fragments.FragmentTimeSeq;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private MenuItem settingsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabs);
        viewPager2 = findViewById(R.id.pager);
        initFragmentPager();
        viewPager2.setAdapter(fragmentPagerAdapter);
        attachTabLayoutMediator();
        settingsButton = toolbar.getMenu().findItem(R.id.action_settings);
        onSettingsButtonClick();
    }

    private void onSettingsButtonClick() {
        settingsButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }


    private void attachTabLayoutMediator() {
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText(R.string.fear_greed);
                        break;
                    case 1:
                        tab.setText(R.string.bounce);
                        break;
                    case 2:
                        tab.setText(R.string.timeseq);
                        break;
                }
            }
        }).attach();
    }

    private void initFragmentPager() {
        fragmentPagerAdapter = new FragmentPagerAdapter(this);
        fragmentPagerAdapter.addFragment(new FragmentFearGreed());
        fragmentPagerAdapter.addFragment(new FragmentBounce());
        fragmentPagerAdapter.addFragment(new FragmentTimeSeq());
    }

}