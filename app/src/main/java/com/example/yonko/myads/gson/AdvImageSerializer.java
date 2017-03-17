package com.example.yonko.myads.gson;

import com.example.yonko.myads.model.AdvertisingImage;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AdvImageSerializer implements JsonSerializer<AdvertisingImage> {

    @Override
    public JsonElement serialize(final AdvertisingImage src, Type typeOfSrc, JsonSerializationContext context) {

        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("type", src.getType().toString().toLowerCase());
        jsonObject.addProperty("path", src.getPath().toString());

        return jsonObject;
    }
}
