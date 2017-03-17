package com.example.yonko.myads.gson;


import com.example.yonko.myads.model.Advertising;
import com.example.yonko.myads.model.AdvertisingImage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AdvertisingSerializer implements JsonSerializer<Advertising> {

    @Override
    public JsonElement serialize(final Advertising src, Type typeOfSrc, JsonSerializationContext context) {

        final JsonObject jsonObject = new JsonObject();

        final JsonElement jsonImages = context.serialize(src.getImages());
        jsonObject.add("images", jsonImages);

        jsonObject.addProperty("title", src.getTitle());
        jsonObject.addProperty("category", src.getCategory());
        jsonObject.addProperty("subcategory", src.getSubcategory());
        jsonObject.addProperty("description", src.getDescription());
        jsonObject.addProperty("price", src.getPrice());
        jsonObject.addProperty("phone", src.getPhone());
        jsonObject.addProperty("should_fb_share", src.isShouldFacebookShare());
        jsonObject.addProperty("should_show_location", src.isShouldShowLocation());

        return jsonObject;
    }
}
