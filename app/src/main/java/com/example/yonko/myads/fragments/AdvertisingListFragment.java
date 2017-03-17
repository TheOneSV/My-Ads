package com.example.yonko.myads.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yonko.myads.R;
import com.example.yonko.myads.adapters.AdvertisingContent;
import com.example.yonko.myads.adapters.MyAdvertisingRecyclerViewAdapter;
import com.example.yonko.myads.model.Advertising;

import java.util.ArrayList;
import java.util.List;

public class AdvertisingListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String KEY_ADV_LIST = "adv-list";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView mRecycleView;
    private MyAdvertisingRecyclerViewAdapter myAdvertisingViewAdapter;
    private ArrayList<Advertising> mValues;

    public AdvertisingListFragment() {
    }

    @SuppressWarnings("unused")
    public static AdvertisingListFragment newInstance(int columnCount) {
        AdvertisingListFragment fragment = new AdvertisingListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advertising_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecycleView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecycleView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecycleView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            if (savedInstanceState != null && savedInstanceState.containsKey(KEY_ADV_LIST)) {
                mValues = savedInstanceState.getParcelableArrayList(KEY_ADV_LIST);
            } else {
                // Only for testing
                //mValues = AdvertisingContent.createDummyObjects(2);
                mValues = new ArrayList<>();
            }

            myAdvertisingViewAdapter = new MyAdvertisingRecyclerViewAdapter(getContext(), mValues, mListener);
            mRecycleView.setAdapter(myAdvertisingViewAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public List<Advertising> getListItems() {
        List<Advertising> items = null;

        if (myAdvertisingViewAdapter != null) {
            items = myAdvertisingViewAdapter.getItems();
        }

        return items;
    }

    public void addItem(Advertising ad) {
        if (myAdvertisingViewAdapter != null) {
            myAdvertisingViewAdapter.getItems().add(ad);
            myAdvertisingViewAdapter.notifyItemChanged(myAdvertisingViewAdapter.getItemCount());
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListItemSelected(Advertising item);
        void onListSavePressed(List<Advertising> items);
    }
}
