package com.sweetmay.advancedcryptoindicators.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sweetmay.advancedcryptoindicators.CallBackOnResultCoinData;
import com.sweetmay.advancedcryptoindicators.OnSearchRVItemClick;
import com.sweetmay.advancedcryptoindicators.Prediction;
import com.sweetmay.advancedcryptoindicators.R;
import com.sweetmay.advancedcryptoindicators.SearchRVAdapter;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.GetListData;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.ListDatum;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.onResultListCallBack;
import com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin.CoinData;
import com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin.GetCoinData;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

public class FragmentTimeSeq extends Fragment implements OnSearchRVItemClick {

    private Prediction prediction;
    private GetCoinData getCoinData;
    private LinkedList<String> coins = new LinkedList<>();
    private int backTestNum = 0;
    private int forecastPeriod = 60;
    private RecyclerView searchRV;
    private SearchRVAdapter searchRVAdapter;
    private GetListData getListData;
    private Handler uiHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_timeseq, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uiHandler = new Handler();
        initRV();
        fillSearchList();
    }

    private void initRV() {
        searchRV = getView().findViewById(R.id.search_rv);
        searchRVAdapter = new SearchRVAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        searchRV.setLayoutManager(layoutManager);
        searchRV.setAdapter(searchRVAdapter);
    }

    private void fillSearchList() {
        getListData = new GetListData();
        getListData.requestList(new onResultListCallBack() {
            @Override
            public void onResult(Response<List<ListDatum>> listDataResponse) {
                for (int i = 0; i < listDataResponse.body().size(); i++) {
                    coins.add(listDataResponse.body().get(i).getId());
                }
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        searchRVAdapter.invalidateRV(coins);
                    }
                });

            }
        });
    }

    private void requestForecast(String coin) {
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

            @Override
            public void onCoinResult(Map.Entry<String, String> coinAndImage, Response<CoinData> response) {

            }

            @Override
            public void onCoinFail() {

            }
        });
    }

    @Override
    public void onRVSearchClick(String coin) {
        requestForecast(coin);
    }
}
