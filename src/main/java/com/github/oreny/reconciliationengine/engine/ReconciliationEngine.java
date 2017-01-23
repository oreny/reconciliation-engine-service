package com.github.oreny.reconciliationengine.engine;

import com.github.oreny.reconciliationengine.data.Payable;
import com.github.oreny.reconciliationengine.data.Payment;
import com.github.oreny.reconciliationengine.graphql.GraphqlClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class ReconciliationEngine {
    private static String graphqlQuery;
    private PaymentPayableMatcher matcher;
    private GraphqlClient graphqlClient;

    public ReconciliationEngine(GraphqlClient graphqlClient, PaymentPayableMatcher matcher) {
        this.matcher = matcher;
        this.graphqlClient = graphqlClient;
        if (graphqlQuery == null) {
            graphqlQuery = loadQueryFromFile();
        }
    }

    private String loadQueryFromFile() {
        InputStream inputStream = getClass().getResourceAsStream("graphql_query.txt");
        return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
    }

    public List<String> getTransactionIds(Payment payment) throws IOException {
        String graphqlReesp = graphqlClient.runQuery(graphqlQuery);
        GraphqlResponse response = GraphqlResponse.parseJson(graphqlReesp);
        return response.getTransactionIds().stream().filter(k -> {
            List<Payable> payables = response.getPayablesForTransactionId(k);
            return payables.stream().filter(p -> matcher.isMatch(payment, p)).findFirst().isPresent();
        }).collect(Collectors.toList());
    }
}

