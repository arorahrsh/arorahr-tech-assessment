package nz.co.acme.flights.utils;

import com.google.gson.Gson;

public final class GsonHelper {
    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }
}