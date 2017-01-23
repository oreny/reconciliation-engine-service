package com.github.oreny.reconciliationengine.engine;

import com.github.oreny.reconciliationengine.data.Payment;
import com.github.oreny.reconciliationengine.graphql.GraphqlClient;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ReconciliationEngineTest {

    private static String graphqlResponse;

    @BeforeClass
    public static void loadJsonData() {
        InputStream inputStream = JsonPathTest.class.getResourceAsStream("graphql_response.json");
        graphqlResponse = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
        assertTrue(graphqlResponse.length() > 0);
    }

    @Test
    public void testServerCreation() {
        ReconciliationEngine engine = createReconciliationEngine();
        assertNotNull(engine);
    }

    @Test
    public void testEngineMatches() {
        ReconciliationEngine engine = createReconciliationEngine();
        try {
            Payment payment = new Payment(48.75, "ABASFA", new Date());
            assertEquals(1, engine.getTransactionIds(payment).size());
            payment = new Payment(45, "AC11144", new Date());
            assertEquals(2, engine.getTransactionIds(payment).size());
            payment = new Payment(65, "AB/1213/ABCA", new Date());
            assertEquals(1, engine.getTransactionIds(payment).size());
            payment = new Payment(55.12, "AC123", new Date());
            assertEquals(2, engine.getTransactionIds(payment).size());
            assertEquals(2, engine.getTransactionIds(payment).size());
            payment = new Payment(65, "BAB12A2 55", new Date());
            assertEquals(1, engine.getTransactionIds(payment).size());
            payment = new Payment(25.35, "Thanks! Paid for two invoices", new Date());
            assertEquals(0, engine.getTransactionIds(payment).size());
        } catch (IOException e) {
            fail();
        }
    }

    private ReconciliationEngine createReconciliationEngine() {
        GraphqlClient mockClient = new GraphqlClient() {
            @Override
            public String runQuery(String query) throws IOException {
                return graphqlResponse;
            }
        };
        PaymentPayableMatcher matcher = new RecallDrivenPaymanetPayableMatcher();
        return new ReconciliationEngine(mockClient, matcher);
    }






}