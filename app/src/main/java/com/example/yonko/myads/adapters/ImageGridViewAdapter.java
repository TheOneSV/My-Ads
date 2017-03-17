package com.example.yonko.myads.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.yonko.myads.R;
import com.example.yonko.myads.model.AdvertisingImage;

import java.util.List;

public class ImageGridViewAdapter extends BaseAdapter {
    private final List<AdvertisingImage> mValues;
    private final Context mContext;

    public ImageGridViewAdapter(Context context, List<AdvertisingImage> items) {
        mContext = context;
        mValues = items;
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public AdvertisingImage getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_image, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.string.tag_key_holder, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.string.tag_key_holder);
        }

        holder.mItem = mValues.get(position);
        if (holder.mItem.getType() == AdvertisingImage.ImageType.EMPTY) {
            Glide.with(mContext).load(R.drawable.ic_photo).into(holder.mImageView);
        } else {
            Log.d("Adapter", holder.mItem.getPath().getPath());
            Glide.with(mContext).load(holder.mItem.getPath()).into(holder.mImageView);
        }

        return convertView;
    }

    public static class ViewHolder {
        public ImageView mImageView;
        public AdvertisingImage mItem;

        public ViewHolder(View view) {
            mImageView = (ImageView) view;
        }

        @Override
        public String toString() {
            return super.toString() + mItem.getType() + " '" + mItem.getPath() + "'";
        }
    }
}
