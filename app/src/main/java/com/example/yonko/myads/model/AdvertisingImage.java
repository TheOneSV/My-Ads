package com.example.yonko.myads.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class AdvertisingImage implements Parcelable {
    public enum  ImageType {EMPTY, CAMERA, GALLERY}
    private ImageType type;
    private Uri path;


    public AdvertisingImage() {
        this.type = ImageType.EMPTY;
    }

    public AdvertisingImage(ImageType imageType, Uri path) {
        this.type = imageType;
        this.path = path;
    }

    public ImageType getType() {
        return type;
    }

    public Uri getPath() {
        return path;
    }

    public void setType(ImageType type) {
        this.type = type;
    }

    public void setPath(Uri path) {
        this.path = path;
    }

    protected AdvertisingImage(Parcel in) {
        type = (ImageType) in.readValue(ImageType.class.getClassLoader());
        path = (Uri) in.readValue(Uri.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(type);
        dest.writeValue(path);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AdvertisingImage> CREATOR = new Parcelable.Creator<AdvertisingImage>() {
        @Override
        public AdvertisingImage createFromParcel(Parcel in) {
            return new AdvertisingImage(in);
        }

        @Override
        public AdvertisingImage[] newArray(int size) {
            return new AdvertisingImage[size];
        }
    };
}

