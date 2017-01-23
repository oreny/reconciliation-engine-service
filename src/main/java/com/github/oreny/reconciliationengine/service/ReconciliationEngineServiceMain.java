package com.github.oreny.reconciliationengine.service;


import com.google.gson.Gson;

import static spark.Spark.*;

public class ReconciliationEngineServiceMain {

    public static void main(String[] args) {

        Gson gson = new Gson();

        port(8080);
        get("/hello", (req, res) -> "Hello World");
        get("/admin/shutdown", (req, res) -> {
            stop();
            return "Server successfully shut down";
        });
    }
}
