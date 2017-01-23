package com.github.oreny.reconciliationengine.data.serializer;

import com.google.gson.Gson;

public abstract class GsonSerializer<T> {
    private static Gson gson = new Gson();
    private Class<T> type;

    protected GsonSerializer(Class<T> type) {
        this.type = type;
    }

    public T fromJson(String json) {
        if (json == null) {
            throw new IllegalArgumentException("json cannot be null");
        }
        T result = gson.fromJson(json, type);
        if (!isValid(result)) {
            throw new IllegalArgumentException("Some of the payment fields are missing in the json");
        }
        return result;
    }

    protected abstract boolean isValid(T obj);
}
