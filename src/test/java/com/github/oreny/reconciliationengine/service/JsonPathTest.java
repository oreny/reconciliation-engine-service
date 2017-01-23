package com.github.oreny.reconciliationengine.service;

import com.github.oreny.reconciliationengine.data.Payable;
import com.github.oreny.reconciliationengine.data.serializer.PayableJsonSerializer;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonPathTest {

    private static String jsonData;

    @BeforeClass
    public static void loadJsonData() {
        InputStream inputStream = JsonPathTest.class.getResourceAsStream("data.json");
        jsonData = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
        assertTrue(jsonData.length() > 0);
    }

    @Test
    public void testPayablesExtraction() {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonData);
        List<String> transactionIds = JsonPath.read(document, "$.data.business.businessTransactions.edges[*].node.id");
        assertEquals(8, transactionIds.size());
        List<Payable> payables = extractPayablesForTransactionId(document,
                "VHJhbnNhY3Rpb25Ob2RlOjRiYWRkMDI3LTg0YmEtNDJiMi05YWZkLTBhMWMwNWEwMjI1ZA==");
        assertEquals(1, payables.size());
        payables = extractPayablesForTransactionId(document,
                "VHJhbnNhY3Rpb25Ob2RlOjVhNmJiZGM5LTJjODAtNDAxOS1iOWRlLTg3ZDFmOGRlNTliNA==");
        assertEquals(0, payables.size());

    }

    private List<Payable> extractPayablesForTransactionId(Object document, String transactionId) {
        String query = "$.data.business.businessTransactions.edges[?(@.node.id == '" + transactionId
                + "')].node.payables.edges[*].node";
        List<Object> payables = JsonPath.read(document, query);
        List<Payable> result = new ArrayList<>(payables.size());
        PayableJsonSerializer serializer = new PayableJsonSerializer();
        payables.forEach(k -> result.add(serializer.fromJson(k.toString())));
        return result;
    }
}
