package com.example.yonko.myads.adapters;

import android.net.Uri;

import com.example.yonko.myads.R;
import com.example.yonko.myads.model.Advertising;
import com.example.yonko.myads.model.AdvertisingImage;

import java.util.ArrayList;
import java.util.List;

public class AdvertisingContent {

    public static ArrayList<Advertising> createDummyObjects(int count) {
        ArrayList<Advertising> ads = new ArrayList<>();

        if (count > 0) {
            for (int i = 1; i <= count; ++i) {
                ads.add(createDummyItem(i));
            }
        }

        return ads;
    }

    private static Advertising createDummyItem(int position) {
        Advertising ad = new Advertising();
        ad.addImage(
                new AdvertisingImage(AdvertisingImage.ImageType.GALLERY,
                        Uri.parse("android.resource://com.example.yonko.myads/" + R.mipmap.ic_launcher)
                )
        );
        ad.setCategory("Default category");
        ad.setSubcategory("Default subcategory");
        ad.setDescription(makeDetails(position));
        ad.setPhone("0887334230");
        ad.setPrice(2.34f);
        ad.setTitle("Title " + position);
        ad.setShouldFacebookShare((position % 2) == 0);
        ad.setShouldShowLocation((position % 2) == 0);
        return ad;
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
