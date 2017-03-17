package com.example.yonko.myads.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yonko.myads.R;
import com.example.yonko.myads.fragments.AdvertisingListFragment.OnListFragmentInteractionListener;
import com.example.yonko.myads.model.Advertising;

import java.util.List;
import java.util.Locale;

public class MyAdvertisingRecyclerViewAdapter extends RecyclerView.Adapter<MyAdvertisingRecyclerViewAdapter.ViewHolder> {

    private static final int VIEW_TYPE_AD = 0;
    private static final int VIEW_TYPE_SAVE = 1;

    private final List<Advertising> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;

    public MyAdvertisingRecyclerViewAdapter(Context context, List<Advertising> items, OnListFragmentInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            View view = null;
            switch (viewType) {
                case VIEW_TYPE_AD: {
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advertising, parent, false);
                } break;
                case VIEW_TYPE_SAVE: {
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_save, parent, false);
                    ((Button) view.findViewById(R.id.button_save)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != mListener) {
                                mListener.onListSavePressed(mValues);
                            }
                        }
                    });
                }
            }
            view.setFocusable(true);
            return new ViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecycleViewSelection");
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position < mValues.size()) {
            Advertising item = mValues.get(position);
            holder.mItem = item;

            Glide.with(mContext).load(item.getImage(0).getPath()).into(holder.mImageView);
            holder.mTitleView.setText(item.getTitle());
            holder.mLocationView.setText(
                    item.isShouldShowLocation()
                    ? mContext.getText(R.string.default_location) : mContext.getText(R.string.not_specified)
            );
            String price = null;
            if (Math.signum(item.getPrice()) == 0) {
                price = mContext.getString(R.string.not_specified);
            } else {
                price = String.format(
                        Locale.getDefault(),
                        "%1$.2f %2$s", item.getPrice(),
                        mContext.getString(R.string.price_sign)
                );
            }
            holder.mPriceView.setText(price);
            holder.mDescriptionView.setText(item.getDescription());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onListItemSelected(holder.mItem);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position < mValues.size())? VIEW_TYPE_AD : VIEW_TYPE_SAVE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mTitleView;
        public final TextView mPriceView;
        public final TextView mLocationView;
        public final TextView mDescriptionView;
        public Advertising mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.imageView);
            mTitleView = (TextView) view.findViewById(R.id.text_title);
            mPriceView = (TextView) view.findViewById(R.id.text_price);
            mLocationView = (TextView) view.findViewById(R.id.text_location);
            mDescriptionView = (TextView) view.findViewById(R.id.text_description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

    public List<Advertising> getItems() {
        return mValues;
    }
}
