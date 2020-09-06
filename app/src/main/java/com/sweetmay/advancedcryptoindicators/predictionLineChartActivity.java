package com.sweetmay.advancedcryptoindicators;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.appbar.MaterialToolbar;

public class predictionLineChartActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private LineChart lineChart;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prediction_layout);
        initAppBar();
        double[] initialData = getIntent().getDoubleArrayExtra("data");
        double[] initialDates = getIntent().getDoubleArrayExtra("date");
    }

    private void initAppBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.getMenu().findItem(R.id.action_settings).setVisible(false);
        toolbar.setTitle(R.string.ai_prediction);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
