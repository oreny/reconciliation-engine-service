package com.github.oreny.reconciliationengine.graphql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class GraphqlClientMock implements GraphqlClient {
    private String graphqlResponse;

    @Override
    public String runQuery(String query) throws IOException {
        if (graphqlResponse == null) {
            graphqlResponse = loadGraphqlResponse();
        }
        return graphqlResponse;
    }

    private String loadGraphqlResponse() {
        InputStream inputStream = getClass().getResourceAsStream("graphql_response.json");
        return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
    }
}
