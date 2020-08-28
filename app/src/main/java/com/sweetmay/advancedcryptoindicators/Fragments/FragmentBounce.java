package com.sweetmay.advancedcryptoindicators.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sweetmay.advancedcryptoindicators.CallBackOnResultCoinData;
import com.sweetmay.advancedcryptoindicators.R;
import com.sweetmay.advancedcryptoindicators.RSICalculator;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.GetListData;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.ListDatum;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.onResultListCallBack;
import com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin.CoinData;
import com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin.GetCoinData;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

public class FragmentBounce extends Fragment implements CallBackOnResultCoinData {

    private static final Object monitorForList = new Object();

    private LinkedList<String> coins;
    private Handler uiHandler;
    private GetCoinData getCoinData;
    private CallBackOnResultCoinData callBackOnResultCoinData;
    private Map<String, Response<CoinData>> fetchedCoins;

    private BounceRVAdapter adapter;
    private RecyclerView recyclerView;

    private HandlerThread RSIResolverThread;
    private Handler RSIHandler;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        uiHandler = new Handler();
        coins = new LinkedList<>();
        callBackOnResultCoinData = this;
        fetchedCoins = new HashMap<>();

        initRSIResolverThread();

        return inflater.inflate(R.layout.fragment_tab_bounce, container, false);
    }

    private void initRSIResolverThread() {
        RSIResolverThread = new HandlerThread("RSIResolverThread");
        RSIResolverThread.start();
        RSIHandler = new Handler(RSIResolverThread.getLooper());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRV();
    }

    private void initRV() {
        recyclerView = getView().findViewById(R.id.bounce_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        adapter = new BounceRVAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        fetchCoinsData();
    }

    private void fetchCoinsData() {
        GetListData getListData = new GetListData();
        getListData.requestList(new onResultListCallBack() {
            @Override
            public void onResult(Response<List<ListDatum>> listDataResponse) {
                fillCoinsList(listDataResponse);
                getCoinData = new GetCoinData();
                getCoinData.requestCoinData(coins, "90", callBackOnResultCoinData);
            }
        });
    }

    private void fillCoinsList(Response<List<ListDatum>> listDataResponse) {
        for (int i = 0; i < listDataResponse.body().size(); i++) {
            if(listDataResponse.body().get(i).getPriceChangePercentage1hInCurrency() <= -1f
                    || listDataResponse.body().get(i).getPriceChangePercentage1hInCurrency() >= 1f){
                coins.add(listDataResponse.body().get(i).getId());
            }
        }
    }


    @Override
    public void onCoinResult(String coin, Response<CoinData> response) {

        synchronized (monitorForList){
            fetchedCoins.put(coin, response);
        }

        RSIHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (monitorForList){
                    List<Float> pricesList = new ArrayList<>();
                    for (int i = 0; i < fetchedCoins.get(coin).body().getPrices().size(); i++) {
                        pricesList.add(fetchedCoins.get(coin).body().getPrices().get(i).get(1));
                    }
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.invalidateRV(new BounceEntity(coin, String.valueOf(RSICalculator.calculateRSI(pricesList))));
                        }
                    });
                    fetchedCoins.remove(coin);
                }
            }
        });
    }
}
