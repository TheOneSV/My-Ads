package com.example.yonko.myads.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Advertising implements Parcelable {
    private ArrayList<AdvertisingImage> images = new ArrayList<>();
    private String title;
    private String category;
    private String subcategory;
    private String description;
    private float price;
    private String phone;
    private boolean shouldFacebookShare = false;
    private boolean shouldShowLocation = false;

    public Advertising() {}

    public ArrayList<AdvertisingImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<AdvertisingImage> images) {
        if (images != null) {
            this.images = images;
        }
    }

    public boolean addImage(AdvertisingImage image) {
        return images.add(image);
    }

    public AdvertisingImage getImage(int position) {
        AdvertisingImage image = null;

        if (position >= 0 && position < images.size()) {
            image = images.get(position);
        }

        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isShouldFacebookShare() {
        return shouldFacebookShare;
    }

    public void setShouldFacebookShare(boolean shouldFacebookShare) {
        this.shouldFacebookShare = shouldFacebookShare;
    }

    public boolean isShouldShowLocation() {
        return shouldShowLocation;
    }

    public void setShouldShowLocation(boolean shouldShowLocation) {
        this.shouldShowLocation = shouldShowLocation;
    }

    protected Advertising(Parcel in) {
        if (in.readByte() == 0x01) {
            images = new ArrayList<AdvertisingImage>();
            in.readList(images, AdvertisingImage.class.getClassLoader());
        } else {
            images = null;
        }
        title = in.readString();
        category = in.readString();
        subcategory = in.readString();
        description = in.readString();
        price = in.readFloat();
        phone = in.readString();
        shouldFacebookShare = in.readByte() != 0x00;
        shouldShowLocation = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (images == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(images);
        }
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(subcategory);
        dest.writeString(description);
        dest.writeFloat(price);
        dest.writeString(phone);
        dest.writeByte((byte) (shouldFacebookShare ? 0x01 : 0x00));
        dest.writeByte((byte) (shouldShowLocation ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Advertising> CREATOR = new Parcelable.Creator<Advertising>() {
        @Override
        public Advertising createFromParcel(Parcel in) {
            return new Advertising(in);
        }

        @Override
        public Advertising[] newArray(int size) {
            return new Advertising[size];
        }
    };
}