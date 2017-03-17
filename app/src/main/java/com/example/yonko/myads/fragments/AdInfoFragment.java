package com.example.yonko.myads.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.yonko.myads.R;
import com.example.yonko.myads.interfaces.OnFragmentActionListener;

public class AdInfoFragment extends Fragment {
    public static final String SENDER_ID = AdInfoFragment.class.getSimpleName();
    // format: string
    public static final String EXTRA_TITLE = "extra-title";
    // format: string
    public static final String EXTRA_DESCRIPTION = "extra-description";
    // format: float
    public static final String EXTRA_PRICE = "extra-price";

    private OnFragmentActionListener mListener;

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private EditText mEditTextPrice;

    private TextInputLayout mInputLayoutTitle;
    private TextInputLayout mInputLayoutDescription;
    private TextInputLayout mInputLayoutPrice;

    public AdInfoFragment() {
    }

    public static AdInfoFragment newInstance() {
        AdInfoFragment fragment = new AdInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_ad_info, container, false);

        mEditTextTitle = (EditText) view.findViewById(R.id.et_title);
        mEditTextDescription = (EditText) view.findViewById(R.id.et_description);
        mEditTextPrice = (EditText) view.findViewById(R.id.et_price);

        mInputLayoutTitle = (TextInputLayout) view.findViewById(R.id.input_layout_title);
        mInputLayoutDescription = (TextInputLayout) view.findViewById(R.id.input_layout_description);
        mInputLayoutPrice = (TextInputLayout) view.findViewById(R.id.input_layout_price);

        mEditTextTitle.addTextChangedListener(new MyTextWatcher(mEditTextTitle));
        mEditTextDescription.addTextChangedListener(new MyTextWatcher(mEditTextDescription));
        mEditTextPrice.addTextChangedListener(new MyTextWatcher(mEditTextPrice));

        Button btnSave = (Button) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });

        return view;
    }

    public void onSave() {
        if (mListener != null && validateForm()) {
            Bundle bundle = new Bundle(3);
            bundle.putString(EXTRA_TITLE, mEditTextTitle.getText().toString());
            bundle.putString(EXTRA_DESCRIPTION, mEditTextDescription.getText().toString());
            String price = mEditTextPrice.getText().toString();
            if (TextUtils.isEmpty(price)) price = "0";
            bundle.putFloat(EXTRA_PRICE, Float.parseFloat(price));
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
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean validateForm() {
        return validateTitle() && validateDescription() && validatePrice();
    }

    private boolean validateTitle() {
        if (mEditTextTitle.getText().toString().trim().isEmpty()) {
            mInputLayoutTitle.setError(getString(R.string.err_msg_title));
            requestFocus(mEditTextTitle);
            return false;
        } else {
            mInputLayoutTitle.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDescription() {
        if (mEditTextDescription.getText().toString().trim().isEmpty()) {
            mInputLayoutDescription.setError(getString(R.string.err_msg_description));
            requestFocus(mEditTextDescription);
            return false;
        } else {
            mInputLayoutDescription.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePrice() {
        if (mEditTextPrice.getText().toString().trim().isEmpty()) {
            mInputLayoutPrice.setError(getString(R.string.err_msg_price));
            requestFocus(mEditTextPrice);
            return false;
        }

        try {
            float price = Float.parseFloat(mEditTextPrice.getText().toString());
            if (Math.signum(price) < 0) {
                mInputLayoutPrice.setError(getString(R.string.err_msg_price_not_negative));
                requestFocus(mEditTextPrice);
                return false;
            }
        } catch (NumberFormatException ex) {
            mInputLayoutPrice.setError(getString(R.string.err_msg_price_not_negative));
            requestFocus(mEditTextPrice);
            return false;
        }

        mInputLayoutPrice.setErrorEnabled(false);

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_title:
                    validateTitle();
                    break;
                case R.id.et_description:
                    validateDescription();
                    break;
                case R.id.et_price:
                    validatePrice();
                    break;
            }
        }
    }
}
