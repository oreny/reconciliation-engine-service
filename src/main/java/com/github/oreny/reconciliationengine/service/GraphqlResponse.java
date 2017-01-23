package com.github.oreny.reconciliationengine.service;

import com.github.oreny.reconciliationengine.data.Payable;
import com.github.oreny.reconciliationengine.data.serializer.PayableJsonSerializer;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import java.util.ArrayList;
import java.util.List;

public class GraphqlResponse {
    private Object document;

    static public GraphqlResponse parseJson(String json) {
        if (json == null) {
            throw new IllegalArgumentException("JSON data cannot be null");
        }
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        return new GraphqlResponse(document);
    }

    private GraphqlResponse(Object document) {
        this.document = document;
    }

    public List<String> getTransactionIds() {
        return JsonPath.read(document, "$.data.business.businessTransactions.edges[*].node.id");
    }

    public List<Payable> getPayablesForTransactionId(String transactionId) {
        String query = "$.data.business.businessTransactions.edges[?(@.node.id == '" + transactionId
                + "')].node.payables.edges[*].node";
        List<Object> payables = JsonPath.read(document, query);
        List<Payable> result = new ArrayList<>(payables.size());
        PayableJsonSerializer serializer = new PayableJsonSerializer();
        payables.forEach(k -> result.add(serializer.fromJson(k.toString())));
        return result;
    }
}
