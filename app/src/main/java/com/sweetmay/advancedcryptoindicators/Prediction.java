package com.sweetmay.advancedcryptoindicators;

import android.util.Log;

import com.workday.insights.timeseries.arima.Arima;
import com.workday.insights.timeseries.arima.struct.ArimaParams;
import com.workday.insights.timeseries.arima.struct.ForecastResult;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class Prediction {

    private SimpleDateFormat simpleDateFormat;

    private ForecastResult forecastResult;

    private double[] data;
    private double[] dates;

    int p = 2;
    int d = 1;
    int q = 1;
    int P = 1;
    int D = 0;
    int Q = 0;
    int m = 0;
    int forecastSize;




    public Prediction(double[] data, double[] dates, int period){
        this.setData(data);
        this.forecastSize = period;
        this.setDates(dates);
        simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
    }

    public void forecast(){
        setForecastResult(Arima.forecast_arima(getData(), forecastSize, new ArimaParams(p, d, q, P, D, Q, m)));
        double[] forecastData = getForecastResult().getForecast();

        long tempDate = (long) dates[dates.length-1];
        Log.d("date" , simpleDateFormat.format(new Date(tempDate)));
        for (int i = 0; i < data.length; i++) {
            Log.d("before", simpleDateFormat.format(new Date((long) dates[i])) + " " + String.valueOf(data[i]));
        }
        for (int i = 0; i < forecastSize; i++) {
            tempDate+=86400000;

            Log.d("forecast", simpleDateFormat.format(new Date(tempDate)) + " " + String.valueOf(forecastData[i]));
        }
    }

    public ForecastResult getForecastResult() {
        return forecastResult;
    }

    public void setForecastResult(ForecastResult forecastResult) {
        this.forecastResult = forecastResult;
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public double[] getDates() {
        return dates;
    }

    public void setDates(double[] dates) {
        this.dates = dates;
    }
}
