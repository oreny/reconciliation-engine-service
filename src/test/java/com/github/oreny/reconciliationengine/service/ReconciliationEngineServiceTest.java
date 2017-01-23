package com.github.oreny.reconciliationengine.service;

import com.github.oreny.reconciliationengine.engine.PaymentPayableMatcher;
import com.github.oreny.reconciliationengine.engine.RecallDrivenPaymanetPayableMatcher;
import com.github.oreny.reconciliationengine.graphql.GraphqlClient;
import com.github.oreny.reconciliationengine.graphql.GraphqlClientMock;
import com.google.gson.Gson;
import okhttp3.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ReconciliationEngineServiceTest {

    private static ReconciliationEngineService service;
    private static final int port = 50123;

    @BeforeClass
    public static void startServer() {

        GraphqlClient mockClient = new GraphqlClientMock();
        PaymentPayableMatcher matcher = new RecallDrivenPaymanetPayableMatcher();
        service = new ReconciliationEngineService(mockClient, matcher);
        service.startService(port);
    }

    @Test
    public void testValidRequest() {
        String payment = "{\"amount\":55.12, \"payment_reference\":\"AC123\", \"payment_date\":\"2016-10-22\"}";
        OkHttpClient client = new OkHttpClient();
        Request request = buildRequest(payment);
        try (Response response = client.newCall(request).execute()) {
            Gson gson = new Gson();
            String[] returnedIds = gson.fromJson(response.body().string(), String[].class);
            assertEquals(2, returnedIds.length);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testInvaliddRequest() {
        String payment = "ayment_reference\":\"AC123\", \"payment_date\":\"2016-10-22\"}";
        OkHttpClient client = new OkHttpClient();
        Request request = buildRequest(payment);
        try (Response response = client.newCall(request).execute()) {
            assertEquals(400, response.code());
        } catch (IOException e) {
            fail();
        }
    }

    private Request buildRequest(String body) {
        return new Request.Builder()
                .url("http://localhost:" + port + "/link")
                .method("POST", RequestBody.create(MediaType.parse("application/json"), body))
                .build();
    }

    @AfterClass
    public static void stopServer() {
        if (service != null) {
            service.shutdown();;
        }
    }
}