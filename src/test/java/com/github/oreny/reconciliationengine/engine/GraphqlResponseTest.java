package com.github.oreny.reconciliationengine.engine;

import com.github.oreny.reconciliationengine.data.Payable;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class GraphqlResponseTest {

    static private String jsonData;

    @BeforeClass
    public static void loadJsonData() {
        InputStream inputStream = JsonPathTest.class.getResourceAsStream("data.json");
        jsonData = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
        assertTrue(jsonData.length() > 0);
    }

    @Test
    public void testTransactionIds() {
        GraphqlResponse response = GraphqlResponse.parseJson(jsonData);
        assertEquals(8, response.getTransactionIds().size());
    }

    @Test
    public void testPayablesForTransactionId() {
        GraphqlResponse response = GraphqlResponse.parseJson(jsonData);
        List<Payable> payables = response.getPayablesForTransactionId(
                "VHJhbnNhY3Rpb25Ob2RlOjVhNmJiZGM5LTJjODAtNDAxOS1iOWRlLTg3ZDFmOGRlNTliNA==");
        assertEquals(0, payables.size());
        payables = response.getPayablesForTransactionId(
                "VHJhbnNhY3Rpb25Ob2RlOmNiODkxNDY3LTg4MjAtNDIxZi1hNDMwLWIwYWJjZTY5MDZhMA==");
        assertEquals(1, payables.size());
    }

    @Test
    public void testPayableData() {
        GraphqlResponse response = GraphqlResponse.parseJson(jsonData);
        List<Payable> payables = response.getPayablesForTransactionId(
                "VHJhbnNhY3Rpb25Ob2RlOmNiODkxNDY3LTg4MjAtNDIxZi1hNDMwLWIwYWJjZTY5MDZhMA==");
        assertEquals(1, payables.size());
        Payable payable = payables.get(0);
        assertEquals(new Double(55.12), payable.amount);
        assertEquals("BAB12A2-55", payable.referenceId);
    }


}