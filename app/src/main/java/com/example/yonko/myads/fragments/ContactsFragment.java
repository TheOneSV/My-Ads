package com.example.yonko.myads.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.yonko.myads.R;
import com.example.yonko.myads.interfaces.OnFragmentActionListener;


public class ContactsFragment extends Fragment {

    public static final String SENDER_ID = ContactsFragment.class.getSimpleName();
    // format: String
    public static final String EXTRA_PHONE = "phone";
    // format: boolean
    public static final String EXTRA_FACEBOOK = "phone";
    // format: boolean
    public static final String EXTRA_LOCATION = "phone";

    private OnFragmentActionListener mListener;

    private TextView mPhoneTextView;
    private CheckBox mShareOnFacebookCheckBox;
    private CheckBox mShowMyLocationCheckBox;

    public ContactsFragment() {
    }

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
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
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        mPhoneTextView = (TextView) view.findViewById(R.id.et_phone);
        mShareOnFacebookCheckBox = (CheckBox) view.findViewById(R.id.cb_facebook);
        mShowMyLocationCheckBox = (CheckBox) view.findViewById(R.id.cb_location);

        Button btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveHandler();
            }
        });

        return view;
    }

    public void onSaveHandler() {
        if (mListener != null) {
            Bundle bundle = new Bundle(3);
            bundle.putString(EXTRA_PHONE, mPhoneTextView.getText().toString());
            bundle.putBoolean(EXTRA_FACEBOOK, mShareOnFacebookCheckBox.isChecked());
            bundle.putBoolean(EXTRA_LOCATION, mShowMyLocationCheckBox.isChecked());
            mListener.onFragmentAction(bundle, SENDER_ID);
        }
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
}
