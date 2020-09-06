package com.sweetmay.advancedcryptoindicators.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sweetmay.advancedcryptoindicators.CallBackOnResultCoinData;
import com.sweetmay.advancedcryptoindicators.OnSearchRVItemClick;
import com.sweetmay.advancedcryptoindicators.Prediction;
import com.sweetmay.advancedcryptoindicators.R;
import com.sweetmay.advancedcryptoindicators.SearchRVAdapter;
import com.sweetmay.advancedcryptoindicators.predictionLineChartActivity;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.GetListData;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.ListDatum;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.onResultListCallBack;
import com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin.CoinData;
import com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin.GetCoinData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Response;

public class FragmentTimeSeq extends Fragment implements OnSearchRVItemClick {

    private Prediction prediction;
    private GetCoinData getCoinData;
    private Map<String, String> coinsAndIcons = new LinkedHashMap<>();
    private Map<String, String> coinsAndIconsToShow = new LinkedHashMap<>();
    private LinkedList<String> coins = new LinkedList<>();
    private RecyclerView searchRV;
    private SearchRVAdapter searchRVAdapter;
    private GetListData getListData;
    private Handler uiHandler;
    private SearchView searchView;
    private MaterialProgressBar progressBar;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_timeseq, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = getView().findViewById(R.id.progress_bar_timeseq);
        progressBar.setVisibility(View.VISIBLE);
        uiHandler = new Handler();
        initRV();
        initSearch();
        hideElements();
        fillSearchList();
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
        showElements();
    }


    private void hideElements(){
        searchView.setVisibility(View.GONE);
        searchRV.setVisibility(View.GONE);
    }

    private void showElements() {
        searchRV.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void onQueryTextChange() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                coinsAndIconsToShow.clear();
                for (int i = 0; i < coins.size(); i++) {
                    if(coins.get(i).contains(s)){
                        coinsAndIconsToShow.put(coins.get(i), coinsAndIcons.get(coins.get(i)));
                    }
                }
                updateRV(coinsAndIconsToShow);
                return false;
            }
        });
    }

    private void initSearch() {
        searchView = getView().findViewById(R.id.search_view);
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
            public void onResultList(Response<List<ListDatum>> listDataResponse) {
                for (int i = 0; i < listDataResponse.body().size(); i++) {
                    coinsAndIcons.put(listDataResponse.body().get(i).getId(),
                            listDataResponse.body().get(i).getImage());
                    coins.add(listDataResponse.body().get(i).getId());
                }
                updateRV(coinsAndIcons);
                onQueryTextChange();
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showElements();
                    }
                });

            }
        });
    }

    private void updateRV(Map<String, String> values) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                searchRVAdapter.invalidateRV(values);
            }
        });
    }

    private void requestForecast(String coin) {
        getCoinData = new GetCoinData();
        getCoinData.requestCoinData(coin, "max", new CallBackOnResultCoinData() {
            @Override
            public void onCoinResult(String coin, Response<CoinData> response) {
                double[] dates = new double[response.body().getPrices().size()];
                double[] data = new double[response.body().getPrices().size()];

                for (int i = 0; i < response.body().getPrices().size(); i++) {
                    data[i] = response.body().getPrices().get(i).get(1);
                    dates[i] = response.body().getPrices().get(i).get(0);
                }
                intent = new Intent(getContext(), predictionLineChartActivity.class);
                intent.putExtra("data", data);
                intent.putExtra("dates", dates);

                startActivity(intent);
            }

            @Override
            public void onCoinResult(Map.Entry<String, String> coinAndImage, Response<CoinData> response) {

            }

            @Override
            public void onCoinFail() {

            }

            @Override
            public void onDone() {

            }
        });
    }

    @Override
    public void onRVSearchClick(String coin) {
        hideElements();
        progressBar.setVisibility(View.VISIBLE);
        requestForecast(coin);
    }
}
