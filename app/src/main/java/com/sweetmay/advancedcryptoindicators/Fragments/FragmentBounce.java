package com.sweetmay.advancedcryptoindicators.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sweetmay.advancedcryptoindicators.R;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.GetListData;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.ListDatum;
import com.sweetmay.advancedcryptoindicators.request_coingeckoList.onResultListCallBack;
import com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin.GetCoinData;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Response;

public class FragmentBounce extends Fragment {


    private LinkedList<String> coins;
    private Handler uiHandler;
    private GetCoinData getCoinData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        uiHandler = new Handler();
        coins = new LinkedList<>();
        return inflater.inflate(R.layout.fragment_tab_bounce, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GetListData getListData = new GetListData();
        getListData.requestList(new onResultListCallBack() {
            @Override
            public void onResult(Response<List<ListDatum>> listDataResponse) {
                fillCoinsList(listDataResponse);
                getCoinData = new GetCoinData();
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getCoinData.requestCoinData(coins, "2");
                    }
                });

            }
        });
    }

    private void fillCoinsList(Response<List<ListDatum>> listDataResponse) {
        for (int i = 0; i < listDataResponse.body().size(); i++) {
            if(listDataResponse.body().get(i).getPriceChangePercentage1hInCurrency() <= -1f){
                coins.add(listDataResponse.body().get(i).getId());
            }
        }
    }


}
