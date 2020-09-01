package com.sweetmay.advancedcryptoindicators;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class XAxisDateFormatter extends ValueFormatter {

    private SimpleDateFormat simpleDateFormat;

    public XAxisDateFormatter(){
        simpleDateFormat = new SimpleDateFormat("dd-MM", Locale.getDefault());
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
       return simpleDateFormat.format(new Date((long)value));
    }
}
