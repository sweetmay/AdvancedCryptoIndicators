package com.sweetmay.advancedcryptoindicators.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sweetmay.advancedcryptoindicators.CallBackOnResultCoinData;
import com.sweetmay.advancedcryptoindicators.Prediction;
import com.sweetmay.advancedcryptoindicators.R;
import com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin.CoinData;
import com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin.GetCoinData;

import java.util.LinkedList;

import retrofit2.Response;

public class FragmentTimeSeq extends Fragment {

    Prediction prediction;
    GetCoinData getCoinData;
    LinkedList<String> coin = new LinkedList<>();
    int backTestNum = 0;
    int forecastPeriod = 1300;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_timeseq, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        coin.add("medibloc");
        getCoinData = new GetCoinData();
        getCoinData.requestCoinData(coin, "max", new CallBackOnResultCoinData() {
            @Override
            public void onCoinResult(String coin, Response<CoinData> response) {
                double[] dates = new double[response.body().getPrices().size()-backTestNum];
                double[] data = new double[response.body().getPrices().size()-backTestNum];

                for (int i = 0; i < response.body().getPrices().size()-backTestNum; i++) {
                    data[i] = response.body().getPrices().get(i).get(1);
                    dates[i] = response.body().getPrices().get(i).get(0);
                }

                prediction = new Prediction(data, dates, forecastPeriod);
                prediction.forecast();
            }
        });
    }
}
