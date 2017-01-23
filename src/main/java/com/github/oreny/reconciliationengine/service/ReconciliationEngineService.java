package com.github.oreny.reconciliationengine.service;


import com.github.oreny.reconciliationengine.data.Payment;
import com.github.oreny.reconciliationengine.data.serializer.PaymentJsonSerializer;
import com.github.oreny.reconciliationengine.engine.PaymentPayableMatcher;
import com.github.oreny.reconciliationengine.engine.RecallDrivenPaymanetPayableMatcher;
import com.github.oreny.reconciliationengine.engine.ReconciliationEngine;
import com.github.oreny.reconciliationengine.graphql.GraphqlClient;
import com.github.oreny.reconciliationengine.graphql.GraphqlClientMock;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.List;

import static spark.Spark.*;

public class ReconciliationEngineService {

    private ReconciliationEngine engine;
    private Gson gson = new Gson();

    public ReconciliationEngineService(GraphqlClient graphqlClient, PaymentPayableMatcher matcher) {
        engine = new ReconciliationEngine(graphqlClient, matcher);
    }

    public void startService(int port) {
        spark.Spark.port(port);
        get("/admin/shutdown", this::handleShutdownRequest);
        get("/link", this::handleLinkRequest);
        post("/link", this::handleLinkRequest);
    }

    private Object handleShutdownRequest(Request request, Response response) {
        shutdown();
        response.status(200);
        return "Server successfully shut down";
    }

    private Object handleLinkRequest(Request req, Response res) {
        Payment payment;
        try {
            PaymentJsonSerializer serializer = new PaymentJsonSerializer();
            payment = serializer.fromJson(req.body());
        } catch (Exception e) {
            res.type("text");
            res.status(400);
            return "Invalid input. Make sure Payable JSON is valid";
        }
        try {
            res.status(200);
            res.type("application/json");
            return gson.toJson(link(payment));
        } catch (Exception e) {
            res.status(500);
            res.type("text");
            return "Internal server error while executing request:\n" + e.getMessage();
        }
    }

    public List<String> link(Payment payment) throws IOException {
        return engine.getTransactionIds(payment);
    }

    public void shutdown() {
        stop();
    }

    public static void main(String[] args) {
        int port = 8080;

        GraphqlClient mockClient = new GraphqlClientMock();
        PaymentPayableMatcher matcher = new RecallDrivenPaymanetPayableMatcher();
        ReconciliationEngineService service = new ReconciliationEngineService(mockClient, matcher);
        service.startService(port);
    }
}
