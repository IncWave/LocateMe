package com.mikhailzaitsevfls.locateme.statisticFragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;



public class StatisticFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private StatisticFragment() {
    }

    public static StatisticFragment newInstance() {
        return new StatisticFragment();
    }

}
