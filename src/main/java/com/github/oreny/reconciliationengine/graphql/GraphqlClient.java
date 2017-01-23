package com.github.oreny.reconciliationengine.graphql;

import java.io.IOException;

public interface GraphqlClient {
    public String runQuery(String query) throws IOException;
}
