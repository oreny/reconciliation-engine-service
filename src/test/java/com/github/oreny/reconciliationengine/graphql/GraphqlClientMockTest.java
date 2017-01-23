package com.github.oreny.reconciliationengine.graphql;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class GraphqlClientMockTest {

    @Test
    public void testRunQuery() {
        GraphqlClientMock client = new GraphqlClientMock();
        try {
            String response = client.runQuery("Any query");
            assertNotNull(response);
        } catch (IOException e) {
            fail();
        }
    }

}