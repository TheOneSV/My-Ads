package com.example.yonko.myads.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yonko.myads.R;
import com.example.yonko.myads.model.Advertising;


public class AdvertisingContainerFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private OnAdCreatedListener mListener;

    public AdvertisingContainerFragment() {
    }

    public static AdvertisingContainerFragment newInstance(String param1) {
        AdvertisingContainerFragment fragment = new AdvertisingContainerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advertising_container, container, false);
    }

    public void saveAd(Advertising ad) {
        if (mListener != null) {
            mListener.onAdCreated(ad);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAdCreatedListener) {
            mListener = (OnAdCreatedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAdCreatedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAdCreatedListener {
        void onAdCreated(Advertising ad);
    }
}
