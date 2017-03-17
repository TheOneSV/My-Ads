package com.example.yonko.myads.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yonko.myads.R;
import com.example.yonko.myads.interfaces.OnFragmentActionListener;


public class CategoryFragment extends Fragment {

    public static final String SENDER_ID = CategoryFragment.class.getSimpleName();
    // format: String
    public static final String EXTRA_CATEGORY = "category";
    // format: String
    public static final String EXTRA_SUBCATEGORY = "subcategory";
    private static final String KEY_SUBCATEGORY_POSITION = "subcategory-position";

    private OnFragmentActionListener mListener;

    private ArrayAdapter mCategoryAdapter;
    private ArrayAdapter mSubcategoryAdapter;
    private Spinner mCategorySpinner;
    private Spinner mSubcategorySpinner;

    public CategoryFragment() {
    }

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mCategoryAdapter =
                new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item);
        mCategoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mCategoryAdapter.addAll(getResources().getStringArray(R.array.categories));
        mCategorySpinner = (Spinner) view.findViewById(R.id.spinner_category);
        mCategorySpinner.setAdapter(mCategoryAdapter);
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSubcategory((String) parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSubcategorySpinner = (Spinner) view.findViewById(R.id.spinner_subcategory);
        mSubcategoryAdapter =
                new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item);
        mSubcategoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSubcategorySpinner.setAdapter(mSubcategoryAdapter);
        updateSubcategory((String)mCategorySpinner.getSelectedItem());

        if (savedInstanceState != null) {
            int selection = savedInstanceState.getInt(KEY_SUBCATEGORY_POSITION, 0);
            mSubcategorySpinner.setSelection(selection, false);
        }

        Button btnNext = (Button) view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextHandler();
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_SUBCATEGORY_POSITION, mSubcategorySpinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentActionListener) {
            mListener = (OnFragmentActionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentActionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void onNextHandler() {
        if (mListener != null && !((String) mCategorySpinner.getSelectedItem()).equals(getString(R.string.category_default))) {
            Bundle bundle = new Bundle(2);
            bundle.putString(EXTRA_CATEGORY, (String) mCategorySpinner.getSelectedItem());
            bundle.putString(EXTRA_SUBCATEGORY, (String) mSubcategorySpinner.getSelectedItem());
            mListener.onFragmentAction(bundle, SENDER_ID);
        } else {
            Toast.makeText(getContext(), R.string.category_error_msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSubcategory(String category) {
        int arrId = getSelectedCategory(category);
        if (arrId != -1) {
            mSubcategoryAdapter.clear();
            mSubcategoryAdapter.addAll(getResources().getStringArray(arrId));
            mSubcategoryAdapter.notifyDataSetChanged();
        }
    }

    private int getSelectedCategory(String category) {
        int arrId = -1;
        if (category.equals(getString(R.string.category_for_sale))) {
            arrId = R.array.subcategory_for_sale;
        } else if (category.equals(getString(R.string.category_services))) {
            arrId = R.array.subcategory_services;
        } else if (category.equals(getString(R.string.category_vehicles))) {
            arrId = R.array.subcategory_vehicles;
        } else if (category.equals(getString(R.string.category_property))) {
            arrId = R.array.subcategory_property;
        } else if (category.equals(getString(R.string.category_default))) {
            arrId = R.array.subcategory_default;
        }
        return arrId;
    }
}
