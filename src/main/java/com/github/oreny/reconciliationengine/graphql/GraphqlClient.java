package com.github.oreny.reconciliationengine.graphql;


import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class GraphqlClient {
    private String host;

    public GraphqlClient(String host) {
        this.host = host;
    }

    public String runQuery(String query) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder()
                .host(host)
                .addQueryParameter("query", query)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
