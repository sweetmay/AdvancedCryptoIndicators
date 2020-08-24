package com.sweetmay.advancedcryptoindicators;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sweetmay.advancedcryptoindicators.Fragments.FragmentSettings;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

public class SettingsActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TabLayout TabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_settings, new FragmentSettings())
                .commit();
        initToolbar();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TabLayout = findViewById(R.id.tabs);
        TabLayout.setVisibility(View.GONE);
        toolbar.getMenu().findItem(R.id.action_settings).setVisible(false);
        toolbar.setTitle(R.string.settings_title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
