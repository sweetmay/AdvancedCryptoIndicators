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

import com.sweetmay.advancedcryptoindicators.BounceEntity;
import com.sweetmay.advancedcryptoindicators.BounceRVAdapter;
import com.sweetmay.advancedcryptoindicators.CallBackOnResultCoinData;
import com.sweetmay.advancedcryptoindicators.OnRSIResultCallBack;
import com.sweetmay.advancedcryptoindicators.R;
import com.sweetmay.advancedcryptoindicators.RSICalculator;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.GetListData;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.ListDatum;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.onResultListCallBack;
import com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin.CoinData;
import com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin.GetCoinData;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Response;

public class FragmentBounce extends Fragment implements CallBackOnResultCoinData {

    private static final Object monitorForList = new Object();

    private Map<String, String> coinsWithImages;
    private Handler uiHandler;
    private GetCoinData getCoinData;
    private CallBackOnResultCoinData callBackOnResultCoinData;
    private Map<String, Response<CoinData>> fetchedCoins;

    private BounceRVAdapter adapter;
    private RecyclerView recyclerView;

    private MaterialProgressBar progressBar;


    private HandlerThread RSIResolverThread;
    private Handler RSIHandler;

    private RSICalculator rsiCalculator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        uiHandler = new Handler();
        coinsWithImages = new HashMap<>();
        callBackOnResultCoinData = this;
        fetchedCoins = new HashMap<>();

        initRSIResolverThread();

        return inflater.inflate(R.layout.fragment_tab_bounce, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void initRSIResolverThread() {
        RSIResolverThread = new HandlerThread("RSIResolverThread");
        RSIResolverThread.start();
        RSIHandler = new Handler(RSIResolverThread.getLooper());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCoinData = new GetCoinData();
        initRV();
        fetchCoinsData();
        rsiCalculator = new RSICalculator();
        progressBar = getView().findViewById(R.id.determinateBar);
    }

    private void initRV() {
        recyclerView = getView().findViewById(R.id.bounce_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        adapter = new BounceRVAdapter();
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void fetchCoinsData() {
        GetListData getListData = new GetListData();
        getListData.requestList(new onResultListCallBack() {
            @Override
            public void onResult(Response<List<ListDatum>> listDataResponse) {
                fillCoinsList(listDataResponse);
                getCoinData.requestCoinData(coinsWithImages, "90", callBackOnResultCoinData);
            }
        });
    }

    private void fillCoinsList(Response<List<ListDatum>> listDataResponse) {
        for (int i = 0; i < listDataResponse.body().size(); i++) {
            if(listDataResponse.body().get(i).getPriceChangePercentage1hInCurrency() <= -1f
                    || listDataResponse.body().get(i).getPriceChangePercentage1hInCurrency() >= 1f){
                coinsWithImages.put(listDataResponse.body().get(i).getId(), listDataResponse.body().get(i).getImage());
            }
        }
        progressBar.setProgress(0);
        progressBar.setMax(coinsWithImages.size());
    }

    @Override
    public void onCoinFail() {

    }

    @Override
    public void onCoinResult(String coin, Response<CoinData> response) {

    }

    @Override
    public void onCoinResult(Map.Entry<String, String> coinAndImage, Response<CoinData> response) {
        fetchedCoins.put(coinAndImage.getKey(), response);
        List<Float> pricesList = new ArrayList<>();
        for (int i = 0; i < fetchedCoins.get(coinAndImage.getKey()).body().getPrices().size(); i++) {
            pricesList.add(fetchedCoins.get(coinAndImage.getKey()).body().getPrices().get(i).get(1));
        }

        rsiCalculator.calculateRSI(pricesList, new OnRSIResultCallBack() {
            @Override
            public void onResultRSI(String rsiData) {
                if(Float.parseFloat(rsiData) >= 60 || Float.parseFloat(rsiData)<=30){
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.invalidateRV(new BounceEntity(coinAndImage.getKey(), rsiData, coinAndImage.getValue()));
                            progressBar.setProgress(progressBar.getProgress() + 1);
                            if (progressBar.getProgress() == progressBar.getMax()) {
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    });
            }
                progressBar.setProgress(progressBar.getProgress() + 1);
                fetchedCoins.remove(coinAndImage.getKey());
            }
        });
    }
}
