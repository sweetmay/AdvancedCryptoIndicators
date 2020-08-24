package com.sweetmay.advancedcryptoindicators.Fragments;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sweetmay.advancedcryptoindicators.R;
import com.sweetmay.advancedcryptoindicators.XAxisDateFormatter;
import com.sweetmay.advancedcryptoindicators.request_fng.CallBackOnResultFnG;
import com.sweetmay.advancedcryptoindicators.request_fng.Datum;
import com.sweetmay.advancedcryptoindicators.request_fng.FngRequest;
import com.sweetmay.advancedcryptoindicators.request_fng.GetFnGData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.pawelkleczkowski.customgauge.CustomGauge;
import retrofit2.Response;

public class FragmentFearGreed extends Fragment implements CallBackOnResultFnG {

    private GetFnGData getFnGData;
    private CustomGauge customGauge;
    private TextView resultValue;
    private Handler uiHandler;
    private TextView resultEval;
    private SimpleDateFormat simpleDateFormat;
    private LineChart lineChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_tab_feargreed, container, false);
    }
    

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        getData();
        uiHandler = new Handler();
        simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void getData() {
        getFnGData = new GetFnGData(this);
        getFnGData.requestFnG("100");
    }

    @Override
    public void onResult(Response<FngRequest> fngRequestResponse) {
        if(fngRequestResponse.body() != null){
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    String resultEvalTemp = fngRequestResponse.body().getData().get(0).getValueClassification();
                    String result = fngRequestResponse.body().getData().get(0).getValue();
                    Date date = new Date();
                    initDataForChart(fngRequestResponse.body().getData());
                    animateGauge(Integer.parseInt(result));
                    resultValue.setText(result);
                    resultEval.setText(resultEvalTemp);
                }
            });
        }
    }

    private void initDataForChart(List<Datum> data){

        ArrayList<Entry> entries = new ArrayList<>();
        Collections.reverse(data);
        for (Datum datum: data) {
            entries.add(new Entry(Float.parseFloat(datum.getTimestamp()), Float.parseFloat(datum.getValue())));
        }
        LineDataSet dataSet = new LineDataSet(entries, "FnG");
        dataSet.setLineWidth(3f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.1f);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        XAxisDateFormatter xFormatter = new XAxisDateFormatter();
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.setDrawBorders(false);
        lineChart.getXAxis().setValueFormatter(xFormatter);
        lineChart.invalidate();
    }


    private void setEvalColor(int result) {
        resultEval.setTextColor(result);
    }

    private void animateGauge(int value) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, value);
        valueAnimator.setDuration(1500);
        valueAnimator.start();
        customGauge.setVisibility(View.VISIBLE);
        ArgbEvaluator evaluator = new ArgbEvaluator();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int tempValue = (int) valueAnimator.getAnimatedValue();
                customGauge.setStrokeColor((Integer) evaluator.evaluate(tempValue/100f, Color.RED, Color.GREEN ));
                resultValue.setText(String.valueOf(tempValue));
                customGauge.setValue(tempValue);
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                resultEval.setVisibility(View.VISIBLE);
                lineChart.setVisibility(View.VISIBLE);
                setEvalColor((Integer)evaluator.evaluate(
                        Integer.parseInt((String) resultValue.getText())/100f
                        , Color.RED, Color.GREEN ));
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    private void initViews() {
        lineChart = getView().findViewById(R.id.chartFnG);
        lineChart.setVisibility(View.GONE);
        customGauge = getView().findViewById(R.id.FnGGauge);
        resultValue = getView().findViewById(R.id.value_text_view);
        resultEval = getView().findViewById(R.id.value_eval_text_view);
        resultEval.setVisibility(View.GONE);
    }
}
