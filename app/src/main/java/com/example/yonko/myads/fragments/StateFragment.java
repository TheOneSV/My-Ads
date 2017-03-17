package com.example.yonko.myads.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yonko.myads.R;

public class StateFragment extends Fragment {

    AdvertisingListFragment mAdvertisingListFragment;
    AdvertisingContainerFragment mAdvertisingContainerFragment;

    public StateFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    public AdvertisingListFragment getAdvertisingListFragment() {
        return mAdvertisingListFragment;
    }

    public void setAdvertisingListFragment(AdvertisingListFragment advertisingListFragment) {
        this.mAdvertisingListFragment = advertisingListFragment;
    }

    public AdvertisingContainerFragment getAdvertisingContainerFragment() {
        return mAdvertisingContainerFragment;
    }

    public void setAdvertisingContainerFragment(AdvertisingContainerFragment advertisingContainerFragment) {
        this.mAdvertisingContainerFragment = advertisingContainerFragment;
    }
}
