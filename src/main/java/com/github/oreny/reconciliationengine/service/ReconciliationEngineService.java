package com.github.oreny.reconciliationengine.service;


import com.github.oreny.reconciliationengine.data.Payment;
import com.github.oreny.reconciliationengine.data.serializer.PaymentJsonSerializer;
import com.github.oreny.reconciliationengine.engine.PaymentPayableMatcher;
import com.github.oreny.reconciliationengine.engine.RecallDrivenPaymanetPayableMatcher;
import com.github.oreny.reconciliationengine.engine.ReconciliationEngine;
import com.github.oreny.reconciliationengine.graphql.GraphqlClient;
import com.github.oreny.reconciliationengine.graphql.GraphqlClientMock;

import java.io.IOException;
import java.util.List;

import static spark.Spark.*;

public class ReconciliationEngineService {

    private ReconciliationEngine engine;

    public ReconciliationEngineService(GraphqlClient graphqlClient, PaymentPayableMatcher matcher) {
        engine = new ReconciliationEngine(graphqlClient, matcher);
    }

    public void startService(int port) {
        spark.Spark.port(port);
        get("/admin/shutdown", (req, res) -> {
            shutdown();
            return "Server successfully shut down";
        });
        get("/link", (req, res) -> {
            try {
                PaymentJsonSerializer serializer = new PaymentJsonSerializer();
                Payment payment = serializer.fromJson(req.body());
               a res.type("application/json");
                res.status(200);
                return link(payment);
            }
            catch (Exception e) {
                res.status(500);
                res.type("text");
                return e.getMessage();
            }
        });
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
