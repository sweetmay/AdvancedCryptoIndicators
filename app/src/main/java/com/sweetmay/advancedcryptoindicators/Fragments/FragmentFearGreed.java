package com.sweetmay.advancedcryptoindicators.Fragments;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sweetmay.advancedcryptoindicators.R;
import com.sweetmay.advancedcryptoindicators.XAxisDateFormatter;
import com.sweetmay.advancedcryptoindicators.request_coingeckoBTC.CoinGeckoRequest;
import com.sweetmay.advancedcryptoindicators.request_coingeckoBTC.GetCryptoData;
import com.sweetmay.advancedcryptoindicators.CallBackOnResultFnG;
import com.sweetmay.advancedcryptoindicators.request_fng.Datum;
import com.sweetmay.advancedcryptoindicators.request_fng.FngData;
import com.sweetmay.advancedcryptoindicators.request_fng.GetFnGData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import pl.pawelkleczkowski.customgauge.CustomGauge;
import retrofit2.Response;

public class FragmentFearGreed extends Fragment implements CallBackOnResultFnG{

    private GetFnGData getFnGData;
    private CustomGauge customGauge;
    private TextView resultValue;
    private Handler uiHandler;
    private TextView resultEval;
    private SimpleDateFormat simpleDateFormat;
    private LineChart lineChart;
    private LineDataSet fngDataSet;
    private LineDataSet coinGeckoDataSet;


    private boolean fetchedFng;
    private boolean fetchedCoinGecko;

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
        FnGGet();
        BTCGet();
        uiHandler = new Handler();
        simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
        setOnNetworkAvailableUpdate();
    }

    private void setOnNetworkAvailableUpdate() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        connectivityManager.registerNetworkCallback(builder.build(), new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(@NonNull Network network) {
                FnGGet();
                BTCGet();
                super.onAvailable(network);
            }

            @Override
            public void onUnavailable() {
                resultValue.setText(R.string.network_unavailable);
                super.onUnavailable();
            }
        });
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void BTCGet(){
        GetCryptoData getCryptoData = new GetCryptoData(this);
        getCryptoData.requestHistory("bitcoin", "usd", "100");
    }

    private void FnGGet() {
        getFnGData = new GetFnGData(this);
        getFnGData.requestFnG("100");
    }

    @Override
    public void onResultFnG(Response<FngData> fngRequestResponse) {
        if(fngRequestResponse.body() != null){
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    String resultEvalTemp = fngRequestResponse.body().getData().get(0).getValueClassification();
                    String result = fngRequestResponse.body().getData().get(0).getValue();
                    initFnGDataForChart(fngRequestResponse.body().getData());
                    fetchedFng = true;
                    if(fetchedCoinGecko){
                        initChart();
                    }
                    animateGauge(Integer.parseInt(result));
                    resultValue.setText(result);
                    resultEval.setText(resultEvalTemp);
                }
            });
        }
    }

    private void initChart() {
        XAxisDateFormatter xFormatter = new XAxisDateFormatter();
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        fngDataSet.setAxisDependency(lineChart.getAxisLeft().getAxisDependency());
        coinGeckoDataSet.setAxisDependency(lineChart.getAxisRight().getAxisDependency());

        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        fngDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        fngDataSet.setCubicIntensity(0.3f);
        fngDataSet.setLineWidth(4f);
        fngDataSet.setDrawCircles(false);

        coinGeckoDataSet.setColor(Color.BLACK);
        coinGeckoDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        coinGeckoDataSet.setCubicIntensity(0.1f);
        coinGeckoDataSet.setLineWidth(3f);
        coinGeckoDataSet.setDrawCircles(false);

        dataSets.add(fngDataSet);
        dataSets.add(coinGeckoDataSet);
        LineData lineData = new LineData(dataSets);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawBorders(true);
        lineChart.getXAxis().setValueFormatter(xFormatter);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    @Override
    public void onResultCoinGecko(Response<CoinGeckoRequest> coinGeckoRequestResponse) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if(coinGeckoRequestResponse.body() != null){
                    initCoinGeckoDataForChart(coinGeckoRequestResponse.body().getPrices());
                    fetchedCoinGecko = true;
                    if(fetchedFng){
                        initChart();
                    }
                }
            }
        });
    }


    private void initCoinGeckoDataForChart(List<List<Float>> data){
        ArrayList<Entry> entries = new ArrayList<>();
        for (List<Float> priceDate: data) {
            entries.add(new Entry((priceDate.get(0)/1000), priceDate.get(1)));
        }
        coinGeckoDataSet = new LineDataSet(entries, "Bitcoin");
    }


    private void initFnGDataForChart(List<Datum> data){
        ArrayList<Entry> entries = new ArrayList<>();
        Collections.reverse(data);
        for (Datum datum: data) {
            entries.add(new Entry(Float.parseFloat(datum.getTimestamp()), Float.parseFloat(datum.getValue())));
        }
        fngDataSet = new LineDataSet(entries, "Fear and Greed");
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
        customGauge = getView().findViewById(R.id.FnGGauge);
        resultValue = getView().findViewById(R.id.value_text_view);
        resultEval = getView().findViewById(R.id.value_eval_text_view);
        resultEval.setVisibility(View.GONE);
    }
}
